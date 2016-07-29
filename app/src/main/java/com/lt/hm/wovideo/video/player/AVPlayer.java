package com.lt.hm.wovideo.video.player;

import android.content.Context;
import android.media.MediaCodec;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer.CodecCounters;
import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.metadata.MetadataTrackRenderer;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * A wrapper around {@link ExoPlayer} that provides a high level interface, based on DemoPlayer.
 * Created by KECB on 7/5/16.
 */

public class AVPlayer implements ExoPlayer.Listener, HlsSampleSource.EventListener,
    MediaCodecVideoTrackRenderer.EventListener, MediaCodecAudioTrackRenderer.EventListener,
    MetadataTrackRenderer.MetadataRenderer<List<Id3Frame>> {

  /**
   * Builds renderers for player
   */
  public interface RendererBuilder {

    /**
     * Builds renderers for playback.
     *
     * @param player The player for which renderers are being built. {@link AVPlayer#onRenderers}
     * should be invoked once the renderers have been built. If building fails,
     * {@link AVPlayer#onRenderersError} should be invoked.
     */
    void buildRenderers(AVPlayer player);

    /**
     * Cancels the current build operation, if there is one. Else does nothing.
     * <p>
     * A canceled build operation must not invoke {@link AVPlayer#onRenderers} or
     * {@link AVPlayer#onRenderersError} on the player, which may have been released.
     */
    void cancel();
  }

  /**
   * A listener for core events.
   */
  public interface Listener {
    void onStateChanged(boolean playWhenReady, int playbackState);

    void onError(Exception e);

    void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
        float pixelWidthHeightRatio);
  }

  /**
   * A listener for internal errors.
   * <p>
   * These errors are not visible to the user, and hence this listener is provided for
   * informational purpose only. Note however that an internal error may cause a fatal
   * error if the player fails to recover. If this happens, {@link Listener#onError(Exception)}
   * will be invoked.
   */
  public interface InternalErrorListener {
    void onRendererInitializationError(Exception e);

    void onAudioTrackInitializationError(AudioTrack.InitializationException e);

    void onAudioTrackWriteError(AudioTrack.WriteException e);

    void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs);

    void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e);

    void onCryptoError(MediaCodec.CryptoException e);

    void onLoadError(int sourceId, IOException e);

    void onDrmSessionManagerError(Exception e);
  }

  /**
   * A listener for debugging information.
   */
  public interface InfoListener {
    void onVideoFormatEnabled(Format format, int trigger, long mediaTimeMs);

    void onAudioFormatEnabled(Format format, int trigger, long mediaTimeMs);

    void onDroppedFrames(int count, long elapsed);

    void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate);

    void onLoadStarted(int sourceId, long length, int type, int trigger, Format format,
        long mediaStartTimeMs, long mediaEndTimeMs);

    void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format,
        long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs);

    void onDecoderInitialized(String decoderName, long elapsedRealtimeMs,
        long initializationDurationMs);

    void onAvailableRangeChanged(int sourceId, TimeRange availableRange);
  }

  /**
   * A listener for receiving notifications of timed text.
   */
  public interface CaptionListener {
    void onCues(List<Cue> cues);
  }

  /**
   * A listener for receiving ID3 metadata parsed from the media stream.
   */
  public interface Id3MetadataListener {
    void onId3Metadata(List<Id3Frame> id3Frames);
  }

  // Constants pulled into this class for convenience.
  public static final int STATE_IDLE = ExoPlayer.STATE_IDLE;
  public static final int STATE_PREPARING = ExoPlayer.STATE_PREPARING;
  public static final int STATE_BUFFERING = ExoPlayer.STATE_BUFFERING;
  public static final int STATE_READY = ExoPlayer.STATE_READY;
  public static final int STATE_ENDED = ExoPlayer.STATE_ENDED;
  public static final int TRACK_DISABLED = ExoPlayer.TRACK_DISABLED;
  public static final int TRACK_DEFAULT = ExoPlayer.TRACK_DEFAULT;

  public static final int RENDERER_COUNT = 4;
  public static final int TYPE_VIDEO = 0;
  public static final int TYPE_AUDIO = 1;
  public static final int TYPE_TEXT = 2;
  public static final int TYPE_METADATA = 3;

  private static final int RENDERER_BUILDING_STATE_IDLE = 1;
  private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
  private static final int RENDERER_BUILDING_STATE_BUILT = 3;

  private final RendererBuilder mRendererBuilder;
  private final ExoPlayer mPlayer;
  private final AVPlayerControl mPlayerControl;
  private final Handler mMainHandler;
  private final CopyOnWriteArrayList<Listener> mListeners;

  private int rendererBuildingState;
  private int lastReportedPlaybackState;
  private boolean lastReportedPlayWhenReady;

  private Surface mSurface;
  private TrackRenderer mVideoRenderer;
  private CodecCounters mCodecCounters;
  private Format mVideoFormat;
  private int mVideoTrackToRestore;

  private BandwidthMeter mBandwidthMeter;
  private boolean mBackgrounded;

  private CaptionListener mCaptionListener;
  private Id3MetadataListener mId3MetadataListener;
  private InternalErrorListener mInternalErrorListener;
  private InfoListener mInfoListener;

  public AVPlayer(RendererBuilder rendererBuilder, Context context) {
    this.mRendererBuilder = rendererBuilder;
    mPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT, 1000, 5000);
    mPlayer.addListener(this);
    mPlayerControl = new AVPlayerControl(mPlayer, context);
    mMainHandler = new Handler();
    mListeners = new CopyOnWriteArrayList<>();
    lastReportedPlaybackState = STATE_IDLE;
    rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
    //Disable text initially.
    mPlayer.setSelectedTrack(TYPE_TEXT, TRACK_DISABLED);
  }

  public AVPlayerControl getPlayerControl() {
    return mPlayerControl;
  }

  public void addListener(Listener listener) {
    mListeners.add(listener);
  }

  public void removeListener(Listener listener) {
    mListeners.remove(listener);
  }

  public void setInternalErrorListener(InternalErrorListener listener) {
    mInternalErrorListener = listener;
  }

  public void setInfoListener(InfoListener listener) {
    mInfoListener = listener;
  }

  public void setCaptionListener(CaptionListener listener) {
    mCaptionListener = listener;
  }

  public void setMetadataListener(Id3MetadataListener listener) {
    mId3MetadataListener = listener;
  }

  public void setSurface(Surface surface) {
    this.mSurface = surface;
    pushSurface(false);
  }

  public Surface getSurface() {
    return mSurface;
  }

  public void blockingClearSurface() {
    mSurface = null;
    pushSurface(true);
  }

  public int getTrackCount(int type) {
    return mPlayer.getTrackCount(type);
  }

  public MediaFormat getTrackFormat(int type, int index) {
    return mPlayer.getTrackFormat(type, index);
  }

  public int getSelectedTrack(int type) {
    return mPlayer.getSelectedTrack(type);
  }

  public void setSelectedTrack(int type, int index) {
    mPlayer.setSelectedTrack(type, index);
    if (type == TYPE_TEXT && index < 0 && mCaptionListener != null) {
      mCaptionListener.onCues(Collections.<Cue>emptyList());
    }
  }

  public boolean getBackgrounded() {
    return mBackgrounded;
  }

  public void setBackgrounded(boolean backgrounded) {
    if (this.mBackgrounded == backgrounded) {
      return;
    }
    this.mBackgrounded = backgrounded;
    if (backgrounded) {
      mVideoTrackToRestore = getSelectedTrack(TYPE_VIDEO);
      setSelectedTrack(TYPE_VIDEO, TRACK_DISABLED);
      blockingClearSurface();
    } else {
      setSelectedTrack(TYPE_VIDEO, mVideoTrackToRestore);
    }
  }

  public void prepare() {
    if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT) {
      mPlayer.stop();
    }
    mRendererBuilder.cancel();
    mVideoFormat = null;
    mVideoRenderer = null;
    rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
    maybeReportPlayerState();
    mRendererBuilder.buildRenderers(this);
  }

  /**
   * Invoked with the results from a {@link RendererBuilder}.
   *
   * @param renderers Renderers indexed by {@link AVPlayer} TYPE_* constants. An individual
   * element may be null if there do not exist tracks of the corresponding type.
   * @param bandwidthMeter Provides an estimate of the currently available bandwidth. May be null.
   */
  void onRenderers(TrackRenderer[] renderers, BandwidthMeter bandwidthMeter) {
    for (int i = 0; i < RENDERER_COUNT; i++) {
      if (renderers[i] == null) {
        // Convert a null renderer to a dummy renderer.
        renderers[i] = new DummyTrackRenderer();
      }
    }
    // Complete preparation.
    this.mVideoRenderer = renderers[TYPE_VIDEO];
    this.mCodecCounters = mVideoRenderer instanceof MediaCodecTrackRenderer
        ? ((MediaCodecTrackRenderer) mVideoRenderer).codecCounters
        : renderers[TYPE_AUDIO] instanceof MediaCodecTrackRenderer
            ? ((MediaCodecTrackRenderer) renderers[TYPE_AUDIO]).codecCounters : null;
    this.mBandwidthMeter = bandwidthMeter;
    pushSurface(false);
    mPlayer.prepare(renderers);
    rendererBuildingState = RENDERER_BUILDING_STATE_BUILT;
  }

  void onRenderersError(Exception e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onRendererInitializationError(e);
    }
    for (Listener listener : mListeners) {
      listener.onError(e);
    }
    rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
    maybeReportPlayerState();
  }

  public void setPlayWhenReady(boolean playWhenReady) {
    mPlayer.setPlayWhenReady(playWhenReady);
  }

  public void seekTo(long positionMs) {
    mPlayer.seekTo(positionMs);
  }

  public void stop() {
    mPlayer.stop();
  }

  public void release() {
    mRendererBuilder.cancel();
    rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
    mSurface = null;
    mPlayer.release();
  }

  public int getPlaybackState() {
    if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILDING) {
      return STATE_PREPARING;
    }
    int playerState = mPlayer.getPlaybackState();
    if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT && playerState == STATE_IDLE) {
      // This is an edge case where the renderers are built, but are still being passed to the
      // palyer's playback thread.
      return STATE_PREPARING;
    }
    return playerState;
  }

  public long getCurrentPosition() {
    return mPlayer.getCurrentPosition();
  }

  public boolean getPlayWhenReady() {
    return mPlayer.getPlayWhenReady();
  }

  Looper getPlaybackLooper() {
    return mPlayer.getPlaybackLooper();
  }

  Handler getMainHandler() {
    return mMainHandler;
  }

  @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    //TODO add a playback state callback
    /**
     * when playbackState is {@link ExoPlayer.STATE_ENDED} tell activity to implement play next
     * episode feature.
     */
    if (playbackState == ExoPlayer.STATE_ENDED) {
      Log.d("Play State", "onPlayerStateChanged: play ended");
    }
    maybeReportPlayerState();
  }

  @Override public void onPlayWhenReadyCommitted() {
    // Do nothing.
  }

  @Override public void onPlayerError(ExoPlaybackException error) {
    rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
    for (Listener listener : mListeners) {
      listener.onError(error);
    }
  }

  private void maybeReportPlayerState() {
    boolean playWhenReady = mPlayer.getPlayWhenReady();
    int playbackState = getPlaybackState();
    if (lastReportedPlayWhenReady != playWhenReady || lastReportedPlaybackState != playbackState) {
      for (Listener listener : mListeners) {
        listener.onStateChanged(playWhenReady, playbackState);
      }
      lastReportedPlayWhenReady = playWhenReady;
      lastReportedPlaybackState = playbackState;
    }
  }

  private void pushSurface(boolean blockForSurfacePush) {
    if (mVideoRenderer == null) {
      return;
    }

    if (blockForSurfacePush) {
      mPlayer.blockingSendMessage(mVideoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE,
          mSurface);
    } else {
      mPlayer.sendMessage(mVideoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSurface);
    }
  }

  // HlsSampleSource.EventListener
  @Override public void onLoadStarted(int sourceId, long length, int type, int trigger,
      Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
    if (mInfoListener != null) {
      mInfoListener.onLoadStarted(sourceId, length, type, trigger, format, mediaStartTimeMs,
          mediaEndTimeMs);
    }
  }

  private long loadedBytes = 0;
  private long sumDuration = 0;

  @Override
  public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format,
      long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
    //TODO is free now?
    //125992483(actual is 125938756) bytes fluent using 748404ms loaded
    ConnectivityManager cm =
            (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    String user = SharedPrefsUtils.getStringPreference(context, "userinfo");
    UserModel userModel = new Gson().fromJson(user, UserModel.class);
    if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && userModel.getIsVip().equals("1")){
      loadedBytes += bytesLoaded;
      sumDuration += loadDurationMs;
      Log.w("LoadSize", "onLoadCompleted mobile net: " + loadedBytes + " bytes"
              + ", duration is :" + sumDuration + " ms");
    } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable()) {
      // wifi
      Log.w("LoadSize", "onLoadCompleted wifi: " + loadedBytes + " bytes"
              + ", duration is :" + sumDuration + " ms");
    }
    if (mInfoListener != null) {
      mInfoListener.onLoadCompleted(sourceId, bytesLoaded, type, trigger, format, mediaStartTimeMs,
          mediaEndTimeMs, elapsedRealtimeMs, loadDurationMs);
    }
  }

  public long getLoadedBytes() {
    return loadedBytes;
  }

  private long loadedBytesCanceled = 0;
  @Override public void onLoadCanceled(int sourceId, long bytesLoaded) {
    // Do nothing.
    //TODO grab the bytesloaded info to tell activity.
    loadedBytesCanceled += bytesLoaded;
    Log.w("LoadSize", "onLoadCanceled: " + loadedBytesCanceled + " bytes");
  }

  @Override public void onLoadError(int sourceId, IOException e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onLoadError(sourceId, e);
    }
  }

  @Override
  public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {
    // Do nothing.
  }

  @Override public void onDownstreamFormatChanged(int sourceId, Format format, int trigger,
      long mediaTimeMs) {
    if (mInfoListener == null) {
      return;
    }
    if (sourceId == TYPE_VIDEO) {
      mVideoFormat = format;
      mInfoListener.onVideoFormatEnabled(format, trigger, mediaTimeMs);
    } else if (sourceId == TYPE_AUDIO) {
      mInfoListener.onAudioFormatEnabled(format, trigger, mediaTimeMs);
    }
  }

  // MediaCodecVideoTrackRenderer.EventListener
  @Override public void onDroppedFrames(int count, long elapsed) {
    if (mInfoListener != null) {
      mInfoListener.onDroppedFrames(count, elapsed);
    }
  }

  @Override public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
      float pixelWidthHeightRatio) {
    for (Listener listener : mListeners) {
      listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
    }
  }

  @Override public void onDrawnToSurface(Surface surface) {
    // Do nothing.
  }

  @Override public void onDecoderInitializationError(
      MediaCodecTrackRenderer.DecoderInitializationException e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onDecoderInitializationError(e);
    }
  }

  @Override public void onCryptoError(MediaCodec.CryptoException e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onCryptoError(e);
    }
  }

  @Override public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs,
      long initializationDurationMs) {
    if (mInfoListener != null) {
      mInfoListener.onDecoderInitialized(decoderName, elapsedRealtimeMs, initializationDurationMs);
    }
  }

  // MetadataRenderer<List<Id3Frame>>
  @Override public void onMetadata(List<Id3Frame> metadata) {
    if (mId3MetadataListener != null && getSelectedTrack(TYPE_METADATA) != TRACK_DISABLED) {
      mId3MetadataListener.onId3Metadata(metadata);
    }
  }

  // MediaCodecAudioTrackRenderer.EventListener
  @Override public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onAudioTrackInitializationError(e);
    }
  }

  @Override public void onAudioTrackWriteError(AudioTrack.WriteException e) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onAudioTrackWriteError(e);
    }
  }

  @Override
  public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
    if (mInternalErrorListener != null) {
      mInternalErrorListener.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
    }
  }
}
