package com.lt.hm.wovideo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.metadata.id3.ApicFrame;
import com.google.android.exoplayer.metadata.id3.GeobFrame;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.metadata.id3.PrivFrame;
import com.google.android.exoplayer.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer.metadata.id3.TxxxFrame;
import com.google.android.exoplayer.util.Util;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoURL;
import com.lt.hm.wovideo.model.response.ResponseVideoRealUrl;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class PipActivity extends BaseActivity implements AVPlayer.Id3MetadataListener,
        View.OnClickListener {


    private int contentType;

    private boolean playerNeedsPrepare;
    private boolean enableBackgroundAudio;

    private static final String TAG = "MainActivity";
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private SparseArray<View> sparseArray;

    private SparseArray<AVPlayer> demoPlayerSparseArray;
    /*
    播放集合urls
     */
    private ArrayList<String> stringLit;

    @Override
    protected void init(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle bundle = getIntent().getExtras();
        Log.e(getClass().getSimpleName(), "oncreate");
        if (bundle != null) {
            stringLit = bundle.getStringArrayList(LivePage.PIP_URLS);
        } else {
            stringLit = new ArrayList<>();
        }

    }

    @Override
    public void initViews() {
        view1 = findViewById(R.id.pip_video1);
        view2 = findViewById(R.id.pip_video2);
        view3 = findViewById(R.id.pip_video3);
        view4 = findViewById(R.id.pip_video4);
    }


    @Override
    public void initDatas() {

        switch (stringLit.size()) {
            case 1:
                for (int i = 1; i < 4; i++) {
                    stringLit.add("");
                }
                break;
            case 2:
                for (int i = 2; i < 4; i++) {
                    stringLit.add("");
                }
                break;
            case 3:
                for (int i = 3; i < 4; i++) {
                    stringLit.add("");
                }
                break;
            default:
                break;
        }
        sparseArray = new SparseArray<>();
        demoPlayerSparseArray = new SparseArray<>();
        sparseArray.put(0, view1);
        sparseArray.put(1, view2);
        sparseArray.put(2, view3);
        sparseArray.put(3, view4);
        for (int i = 0; i < sparseArray.size(); i++) {
            View view1 = sparseArray.get(i);
            if (0 == i) {
                ImageView imageView = (ImageView) view1.findViewById(R.id.pip_iv_back);
                imageView.setOnClickListener(v -> finish());
            }
            String url = stringLit.get(i);
            if (!TextUtils.isEmpty(url) && !"".equals(url)) {
                videoFrame = (AspectRatioFrameLayout) view1.findViewById(R.id.video_frame);
                surfaceView = (SurfaceView) view1.findViewById(R.id.surface_view);
                surfaceView.getHolder().addCallback(new MyCllback(i));
                videoFrame.setTag(i);
                videoFrame.setOnClickListener(this);
                Log.e(getClass().getSimpleName(), "url" + url);
                frameLayoutMap.put(FRAME_KEY+flags[i],videoFrame);
                surfaceViewMap.put(SURF_KEY+flags[i],surfaceView);
                getRealURL(url, flags[i]);
            }
        }
    }

    private Map<String,AspectRatioFrameLayout> frameLayoutMap = new HashMap<>();
    private Map<String,SurfaceView> surfaceViewMap = new HashMap<>();
    private static final String FRAME_KEY = "frame";
    private static final String SURF_KEY = "surf";
    private int[] flags = new int[]{120, 121, 123, 124};
    private AspectRatioFrameLayout videoFrame;
    private SurfaceView surfaceView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_pip;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        releasePlayer();
    }

    private void onShown(String url, AspectRatioFrameLayout videoFrame, SurfaceView surfaceView) {
        Uri videoUri = Uri.parse(url);

        contentType = Util.TYPE_HLS;
        AVPlayer player = demoPlayerSparseArray.get((int) videoFrame.getTag());
        if (!maybeRequestPermission(videoUri)) {
            preparePlayer(videoUri, true, videoFrame, surfaceView, player);
        } else if (null != player) {
            player.setBackgrounded(false);
        }

    }
    // Internal methods

    private AVPlayer.RendererBuilder getRendererBuilder(Uri contentUri) {
        String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        switch (contentType) {
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(this, userAgent, contentUri.toString());

            default:
                throw new IllegalStateException("Unsupported type: " + contentType);

        }
    }

    private void preparePlayer(Uri contentUri, boolean playWhenReady, AspectRatioFrameLayout videoFrame, SurfaceView surfaceView, AVPlayer player) {
        if (player == null) {
            player = new AVPlayer(getRendererBuilder(contentUri), this);
            player.addListener(new MyPlayListener(videoFrame));
            player.setMetadataListener(this);
            playerNeedsPrepare = true;
        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;

        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
        if (demoPlayerSparseArray.size() >= 1) {
            player.setSelectedTrack(AVPlayer.TYPE_AUDIO, -1);
        }
        demoPlayerSparseArray.put((int) videoFrame.getTag(), player);
    }


    @Override
    public void onId3Metadata(List<Id3Frame> id3Frames) {
        for (Id3Frame id3Frame : id3Frames) {
            if (id3Frame instanceof TxxxFrame) {
                TxxxFrame txxxFrame = (TxxxFrame) id3Frame;
                Log.i(TAG, String.format("ID3 TimedMetadata %s: description=%s, value=%s", txxxFrame.id,
                        txxxFrame.description, txxxFrame.value));
            } else if (id3Frame instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame) id3Frame;
                Log.i(TAG, String.format("ID3 TimedMetadata %s: owner=%s", privFrame.id, privFrame.owner));
            } else if (id3Frame instanceof GeobFrame) {
                GeobFrame geobFrame = (GeobFrame) id3Frame;
                Log.i(TAG, String.format("ID3 TimedMetadata %s: mimeType=%s, filename=%s, description=%s",
                        geobFrame.id, geobFrame.mimeType, geobFrame.filename, geobFrame.description));
            } else if (id3Frame instanceof ApicFrame) {
                ApicFrame apicFrame = (ApicFrame) id3Frame;
                Log.i(TAG, String.format("ID3 TimedMetadata %s: mimeType=%s, description=%s",
                        apicFrame.id, apicFrame.mimeType, apicFrame.description));
            } else if (id3Frame instanceof TextInformationFrame) {
                TextInformationFrame textInformationFrame = (TextInformationFrame) id3Frame;
                Log.i(TAG, String.format("ID3 TimedMetadata %s: description=%s", textInformationFrame.id,
                        textInformationFrame.description));
            } else {
                Log.i(TAG, String.format("ID3 TimedMetadata %s", id3Frame.id));
            }
        }
    }

    @Override
    public void onClick(View view) {
        //点击哪个哪个播放声音
        int tag = (int) view.getTag();
        int size = demoPlayerSparseArray.size();
//        DemoPlayer player2 = demoPlayerSparseArray.get(tag);
        for (int i = 0; i < size; i++) {
            AVPlayer player = demoPlayerSparseArray.get(i);
            if (null != player) {
                if (tag == i) {
                    player.setSelectedTrack(AVPlayer.TYPE_AUDIO, 0);
                } else {
                    player.setSelectedTrack(AVPlayer.TYPE_AUDIO, -1);
                }
            }
        }

    }


    class MyCllback implements SurfaceHolder.Callback {

        final AVPlayer player;

        MyCllback(int position) {
            player = demoPlayerSparseArray.get(position);
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {

            if (player != null) {
                player.setSurface(surfaceHolder.getSurface());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (player != null) {
                player.blockingClearSurface();
            }
        }
    }

    class MyPlayListener implements AVPlayer.Listener {
        final AspectRatioFrameLayout mvideoFrame;

        MyPlayListener(AspectRatioFrameLayout videoFrame) {
            this.mvideoFrame = videoFrame;
        }

        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {
            String text = "playWhenReady=" + playWhenReady + ", playbackState=";
            switch (playbackState) {
                case ExoPlayer.STATE_BUFFERING:
                    text += "buffering";
                    break;
                case ExoPlayer.STATE_ENDED:
                    text += "ended";
                    break;
                case ExoPlayer.STATE_IDLE:
                    text += "idle";
                    break;
                case ExoPlayer.STATE_PREPARING:
                    text += "preparing";
                    break;
                case ExoPlayer.STATE_READY:
                    text += "ready";
                    break;
                default:
                    text += "unknown";
                    break;
            }
            Log.e(TAG, text);
        }

        @Override
        public void onError(Exception e) {
            String errorString = null;
            if (e instanceof ExoPlaybackException && e.getCause() instanceof MediaCodecTrackRenderer.DecoderInitializationException) {
                MediaCodecTrackRenderer.DecoderInitializationException initializationException = (MediaCodecTrackRenderer.DecoderInitializationException) e.getCause();
                if (initializationException.decoderName == null) {
                    if (initializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = "找不到相关解码设备";
                    } else if (initializationException.secureDecoderRequired) {
                        errorString = "当前设备未提供稳定解码格式：" + initializationException.mimeType;
                    } else {
                        errorString = "当前设备未提供解码格式：" + initializationException.mimeType;
                    }
                } else {
                    errorString = "无法初始化编译器：" + initializationException.decoderName;
                }
            }
            if (errorString != null) {
                Toast.makeText(PipActivity.this, errorString, Toast.LENGTH_LONG).show();
            }
            playerNeedsPrepare = true;
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            mvideoFrame.setAspectRatio(height == 0 ? 1 : width * pixelWidthHeightRatio / height);
        }

    }

    @TargetApi(23)
    private boolean maybeRequestPermission(Uri contentUri) {
        if (requiresPermission(contentUri)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return true;
        }

        return false;
    }

    private boolean requiresPermission(Uri contentUri) {
        return Util.SDK_INT > 23 &&
                Util.isLocalFileUri(contentUri) &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            onHidden();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            onHidden();
        }
    }


    private void releasePlayer() {
        int size = demoPlayerSparseArray.size();
        for (int i = 0; i < size; i++) {
            AVPlayer player = demoPlayerSparseArray.get(i);
            if (player != null) {
                player.release();
            }
        }
        sparseArray.clear();
        demoPlayerSparseArray.clear();

    }

    private void onHidden() {
        if (!enableBackgroundAudio) {
            releasePlayer();
        } else {
            int size = demoPlayerSparseArray.size();
            for (int i = 0; i < size; i++) {
                AVPlayer player = demoPlayerSparseArray.get(i);
                if (null != player)
                    player.setBackgrounded(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url, int flag) {
        NetUtils.getVideoRealURL(url, UserMgr.getUsePhone(), flag, this);
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        ResponseVideoRealUrl va = (ResponseVideoRealUrl) value;
        onShown(va.getBody().getUrl(), frameLayoutMap.get(FRAME_KEY+flag), surfaceViewMap.get(SURF_KEY+flag));
    }

}
