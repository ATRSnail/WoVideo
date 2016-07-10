package com.lt.hm.wovideo.video.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.os.Handler;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.hls.DefaultHlsTrackSelector;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.metadata.MetadataTrackRenderer;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.metadata.id3.Id3Parser;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.ManifestFetcher.ManifestCallback;

import java.io.IOException;
import java.util.List;

/**
 * A {@link AVPlayer.RendererBuilder} for HLS.
 * Created by KECB on 7/5/16.
 */

public class HlsRendererBuilder implements AVPlayer.RendererBuilder {

  private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
  private static final int MAIN_BUFFER_SEGMENTS = 254;
  private static final int AUDIO_BUFFER_SEGMENTS = 54;
  private static final int TEXT_BUFFER_SEGMENTS = 2;

  private final Context mContext;
  private final String mUserAgent;
  private final String mUrl;

  private AsyncRendererBuilder mCurrentAsyncBuilder;

  public HlsRendererBuilder(Context context, String userAgent, String url) {
    mContext = context;
    mUserAgent = userAgent;
    mUrl = url;
  }

  @Override public void buildRenderers(AVPlayer player) {
    mCurrentAsyncBuilder = new AsyncRendererBuilder(mContext, mUserAgent, mUrl, player);
    mCurrentAsyncBuilder.init();
  }

  @Override public void cancel() {
    if (mCurrentAsyncBuilder != null) {
      mCurrentAsyncBuilder.cancel();
      mCurrentAsyncBuilder = null;
    }
  }

  private static final class AsyncRendererBuilder
      implements ManifestCallback<HlsPlaylist> {

    private final Context context;
    private final String userAgent;
    private final AVPlayer player;
    private final ManifestFetcher<HlsPlaylist> playlistFetcher;

    private boolean canceled;

    public AsyncRendererBuilder(Context context, String userAgent,String url, AVPlayer player) {
      this.context = context;
      this.userAgent = userAgent;
      this.player = player;
      HlsPlaylistParser parser = new HlsPlaylistParser();
      playlistFetcher =
          new ManifestFetcher<>(url, new DefaultUriDataSource(context, userAgent), parser);
    }

    public void init() {
      playlistFetcher.singleLoad(player.getMainHandler().getLooper(), this);
    }

    public void cancel() {
      canceled = true;
    }

    @Override public void onSingleManifest(HlsPlaylist manifest) {
      if (canceled) {
        return;
      }

      Handler mainHandler = player.getMainHandler();
      LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
      DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
      PtsTimestampAdjusterProvider timestampAdjusterProvider = new PtsTimestampAdjusterProvider();

      boolean haveSubtitles = false;
      boolean haveAudios = false;
      if (manifest instanceof HlsMasterPlaylist) {
        HlsMasterPlaylist masterPlaylist = (HlsMasterPlaylist) manifest;
        haveSubtitles = !masterPlaylist.subtitles.isEmpty();
        haveAudios = !masterPlaylist.audios.isEmpty();
      }

      // Build the video/id3 renderers.
      DataSource dataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
      HlsChunkSource chunkSource = new HlsChunkSource(true, dataSource, manifest,
          DefaultHlsTrackSelector.newDefaultInstance(context), bandwidthMeter,
          timestampAdjusterProvider, HlsChunkSource.ADAPTIVE_MODE_SPLICE);
      HlsSampleSource sampleSource =
          new HlsSampleSource(chunkSource, loadControl, MAIN_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE,
              mainHandler, player, AVPlayer.TYPE_VIDEO);
      MediaCodecVideoTrackRenderer videoRenderer =
          new MediaCodecVideoTrackRenderer(context, sampleSource, MediaCodecSelector.DEFAULT,
              MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5000, mainHandler, player, 50);
      MetadataTrackRenderer<List<Id3Frame>> id3Renderer =
          new MetadataTrackRenderer<>(sampleSource, new Id3Parser(), player,
              mainHandler.getLooper());

      // Build the audio renderer.
      MediaCodecAudioTrackRenderer audioRenderer;
      if (haveAudios) {
        DataSource audioDataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
        HlsChunkSource audioChunkSource = new HlsChunkSource(false /* isMaster */, audioDataSource,
            manifest, DefaultHlsTrackSelector.newAudioInstance(), bandwidthMeter,
            timestampAdjusterProvider, HlsChunkSource.ADAPTIVE_MODE_SPLICE);
        HlsSampleSource audioSampleSource = new HlsSampleSource(audioChunkSource, loadControl,
            AUDIO_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE, mainHandler, player,
            AVPlayer.TYPE_AUDIO);
        audioRenderer = new MediaCodecAudioTrackRenderer(
            new SampleSource[] {sampleSource, audioSampleSource}, MediaCodecSelector.DEFAULT, null,
            true, player.getMainHandler(), player, AudioCapabilities.getCapabilities(context),
            AudioManager.STREAM_MUSIC);
      } else {
        audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
            MediaCodecSelector.DEFAULT, null, true, player.getMainHandler(), player,
            AudioCapabilities.getCapabilities(context), AudioManager.STREAM_MUSIC);
      }

      // Build the text renderer. Implement this later
      //TrackRenderer textRenderer;
      //if (haveSubtitles) {
      //  DataSource textDataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
      //  HlsChunkSource textChunkSource = new HlsChunkSource(false /* isMaster */, textDataSource,
      //      manifest, DefaultHlsTrackSelector.newSubtitleInstance(), bandwidthMeter,
      //      timestampAdjusterProvider, HlsChunkSource.ADAPTIVE_MODE_SPLICE);
      //  HlsSampleSource textSampleSource = new HlsSampleSource(textChunkSource, loadControl,
      //      TEXT_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE, mainHandler, player, AVPlayer.TYPE_TEXT);
      //  textRenderer = new TextTrackRenderer(textSampleSource, player, mainHandler.getLooper());
      //} else {
      //  textRenderer = new Eia608TrackRenderer(sampleSource, player, mainHandler.getLooper());
      //}

      TrackRenderer[] renderers = new TrackRenderer[AVPlayer.RENDERER_COUNT];
      renderers[AVPlayer.TYPE_VIDEO] = videoRenderer;
      renderers[AVPlayer.TYPE_AUDIO] = audioRenderer;
      renderers[AVPlayer.TYPE_METADATA] = id3Renderer;
      //renderers[AVPlayer.TYPE_TEXT] = textRenderer;
      player.onRenderers(renderers, bandwidthMeter);
    }

    @Override public void onSingleManifestError(IOException e) {
      if (canceled) {
        return;
      }

      player.onRenderersError(e);
    }
  }
}
