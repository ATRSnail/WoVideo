package com.lt.hm.wovideo.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.interf.OnMediaOtherListener;
import com.lt.hm.wovideo.model.NetUsage;
import com.lt.hm.wovideo.model.response.ResponseBullet;
import com.lt.hm.wovideo.utils.AdvancedCountdownTimer;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.Bullet;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.BulletSendDialog;
import com.lt.hm.wovideo.video.player.DanmakuControl;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.video.player.KeyCompatibleMediaController;
import com.lt.hm.wovideo.video.player.WoDanmakuParser;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;
import com.victor.loading.rotate.RotateLoading;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.util.IOUtils;

/**
 * As a base class for all activity which needs to play a video.
 * Contains all the video control and bullet screen control methods in base activity for easy to maintain.
 * FIXME: Known issue, current version is messed up by the screen orientation logical, need to clean it.
 *
 * @author KECB
 *         Created by KECB on 7/19/16.
 * @version 1.0
 */

public class BaseVideoActivity extends BaseActivity implements SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener, AVController.OnInterfaceInteract, AVController.onTouchAd {
    public static final String CONTENT_ID_EXTRA = "content_id";
    public static final String CONTENT_TYPE_EXTRA = "content_type";
    public static final String PROVIDER_EXTRA = "provider";
    // Video thing
    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";
    private static final CookieManager defaultCookieManager;
    private ScreenSwitchUtils instance;

    static {
        defaultCookieManager = new CookieManager();
        defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private String mVideoId;

    private EventLogger mEventLogger;
    protected AVController mMediaController;
    private AspectRatioFrameLayout mVideoFrame;
    private View mShutterView;
    private RotateLoading mRotateLoading;
    private SurfaceView mSurfaceView;

    private AVPlayer mPlayer;
    private boolean mPlayerNeedsPrepare;
    protected long mPlayerPosition;
    private boolean mEnableBackgroundAduio = false;

    private Uri mContentUri;
    private int mContentType;
    private String mContentId;
    private String mProvider;

    private AudioCapabilitiesReceiver mAudioCapabilitiesReceiver;
    private IDanmakuView mDanmakuView;
    private DanmakuControl danmakuControl;

    private long mLoadedBytes;
    private int statueHight;//5.0以上的状态栏高度
    public boolean isMovie = true;//默认是电影,或者小视屏

    /**
     * Makes a best guess to infer the type from a media {@link Uri} and an optional overriding file
     * extension.
     *
     * @param uri           The {@link Uri} of the media.
     * @param fileExtension An overriding file extension.
     * @return The inferred type.
     */
    private static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension
                : uri.getLastPathSegment();
        return Util.inferContentType(lastPathSegment);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Activity lifecycle

    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = findViewById(R.id.video_root);
        instance = ScreenSwitchUtils.init(this.getApplicationContext());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statueHight = ScreenUtils.getStatusHeight(getApplicationContext());

            WindowManager manager = this.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            width = outMetrics.widthPixels;
            height = outMetrics.heightPixels;

        }

        root.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_ESCAPE
                    || keyCode == KeyEvent.KEYCODE_MENU) {
                return false;
            }
            return mMediaController.dispatchKeyEvent(event);
        });

        mRotateLoading = (RotateLoading) findViewById(R.id.loading);
        mShutterView = findViewById(R.id.shutter);
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);

        mVideoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

        mMediaController = new KeyCompatibleMediaController(this, mDanmakuView, instance);

        mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        mMediaController.setGestureListener(this);
        mMediaController.setonTouchAd(this);

        mMediaController.setIsAd(isAd);
        mMediaController.setAdUi();
        startNormalCountDownTime(18);

        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
        mAudioCapabilitiesReceiver.register();

        danmakuControl = new DanmakuControl(mDanmakuView);
        danmakuControl.hideDanmaku();

//        ((View) mDanmakuView).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //toggleControlsVisibility();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    v.performClick();
//                }
//                return false;
//            }
//        });

    }

    protected Intent onUrlGot(VideoModel video) {
        return new Intent(this, BaseVideoActivity.class)
                .setData(Uri.parse(video.getmPlayUrl().getFormatUrl()))
                .putExtra(CONTENT_ID_EXTRA, video.getmVideoName())
                .putExtra(CONTENT_TYPE_EXTRA, Util.TYPE_HLS)
                .putExtra(PROVIDER_EXTRA, "");
    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        mPlayerPosition = 0;
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            onShown();
        }
        if (instance != null) {
            instance.start(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            onShown();
        }
        danmakuControl.resumeDanmaku();
    }

    protected void onShown() {
        try {
            Intent intent = getIntent();
            mContentUri = intent.getData();
            mContentType = intent.getIntExtra(CONTENT_TYPE_EXTRA,
                    inferContentType(mContentUri, intent.getStringExtra(CONTENT_EXT_EXTRA)));
            mContentId = intent.getStringExtra(CONTENT_ID_EXTRA);
            mProvider = intent.getStringExtra(PROVIDER_EXTRA);
            if (mPlayer == null) {
                if (!maybeRequestPermission()) {
                    preparePlayer(true);
                }
            } else {
                mPlayer.setBackgrounded(false);
            }
            mShutterView.setVisibility(View.INVISIBLE);
        } catch (NullPointerException ex) {
            //TODO deal with this if needed
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer == null) return;
        mLoadedBytes = mPlayer.getLoadedBytes();
        if (Util.SDK_INT <= 23) {
            onHidden();
        }
        danmakuControl.pauseDanmaku();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            onHidden();
        }
        danmakuControl.releaseDanmaku();
        if (instance != null) {
            instance.stop();
        }

    }

    private void onHidden() {
        if (!mEnableBackgroundAduio) {
            releasePlayer();
        } else {
            mPlayer.setBackgrounded(true);
        }
        mShutterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {

        NetUsage usage = new NetUsage();
        usage.setUserId("");
        usage.setVideoId(mVideoId);
        usage.setCreateTime(System.currentTimeMillis() + "");
        usage.setBytes(String.valueOf(mLoadedBytes));
        netUsageDatabase.insert(usage);

        mAudioCapabilitiesReceiver.unregister();
        releasePlayer();
        danmakuControl.releaseDanmaku();
        //广告倒计时取消
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (mMediaController != null) {
            mMediaController.hide();
            mMediaController = null;
        }
        cancelOkhttps();
        super.onDestroy();
    }

    /**
     * ondestory方法取消网络请求
     */
    private void cancelOkhttps() {
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_video_fake_url);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_video_real_url);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_video_detail);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_comments);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_you_like);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_push_comment);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_video_uncollect);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_valide_comment);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_video_collect);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_zan);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_bullet);
        OkHttpUtils.getInstance().cancelTag(HttpApis.http_add_bullet);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TLog.error("sensor--999>");
        //Check the orientation of the screen

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_root));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );
            //TODO set title
//            mMediaController.setTitle(videoName.getText().toString());
            mVideoFrame.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mVideoFrame.setAspectRatio((float) height / (width + statueHight));
            } else {
                mVideoFrame.requestLayout();
            }

            mMediaController.setBulletScreen(true);

            ScreenUtils.setSystemUiVisibility(this, true);

            //show danmu
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //show status bar
            //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mMediaController.setBulletScreen(false);
            mMediaController.hide();

            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
            // when in portrait screen, turn off bullet screen.
            danmakuControl.hideDanmaku();
            ScreenUtils.setSystemUiVisibility(this, false);
        }

        mMediaController.setIsAd(isAd);
        if (isAd) {
            mMediaController.setAdUi();
        } else {
            mMediaController.setMovieUi(isMovie);
        }

    }


    // Surface Life cycle
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing...
    }

    // Internal methods

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.blockingClearSurface();
        }
    }

    /**
     * 当音频功能更改时调用
     *
     * @param audioCapabilities Current audio capabilities for the device.
     */
    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        if (mPlayer == null) {
            return;
        }
        boolean backgrounded = mPlayer.getBackgrounded();
        boolean playWhenReady = mPlayer.getPlayWhenReady();
        releasePlayer();
        preparePlayer(playWhenReady);
        mPlayer.setBackgrounded(backgrounded);
    }

    @Override
    public void onCues(List<Cue> cues) {

    }

    @Override
    public void onId3Metadata(List<Id3Frame> id3Frames) {

    }

    public boolean isAd = true;

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        TLog.error("playbackState--->" + playbackState);
        if (isAd) {
            if (playbackState == AVPlayer.STATE_ENDED) {//播放结束5
                adOnComplete();
                if (timer != null) {
                    timer.cancel();
                }
            }
            if (playbackState == AVPlayer.STATE_PREPARING) { //准备播放2

            }
            if (playbackState == AVPlayer.STATE_READY) { //播放4
                if (timer != null) {
                    if (timer.getIsFirst()) {
                        timer.start();
                    } else {
                        timer.resume();
                    }

                }
            }
            if (playbackState == AVPlayer.STATE_BUFFERING) {//缓冲3
                if (timer != null) {
                    timer.pause();
                }
            }
        }else {
            if (playbackState == AVPlayer.STATE_ENDED) {
                rewind(false);
            }
            final boolean showProgress = playbackState == AVPlayer.STATE_BUFFERING
                    || playbackState == AVPlayer.STATE_PREPARING;
            if (showProgress){
                mRotateLoading.start();
            }else {
                mRotateLoading.stop();
            }
        }

//        mRotateLoading.setVisibility(showProgress ? View.VISIBLE : View.GONE);

//        if (playbackState == AVPlayer.STATE_READY || playbackState == AVPlayer.STATE_ENDED) {
//
//            mRotateLoading.stop();
//            //TODO play next if exist.
//
//            danmakuControl.showDanmaku();
//            danmakuControl.seekToDanmaku(mPlayer.getCurrentPosition());
//
//        } else {
//            mRotateLoading.start();
//            danmakuControl.hideDanmaku();
//        }
        mMediaController.updatePausePlay();
    }

    private void rewind(boolean playWhenReady) {
        if (mPlayer != null) {
            mPlayerPosition = 0L;
            mPlayer.seekTo(mPlayerPosition);
            preparePlayer(playWhenReady);
        }
    }

    @Override
    public void onError(Exception e) {

    }

    private AdvancedCountdownTimer timer;

    private void startNormalCountDownTime(long time) {

        timer = new AdvancedCountdownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished, int percent) {
                TLog.error("millisUntilFinished---->" + millisUntilFinished);
                mMediaController.showAdCountDown(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

            }
        };

    }

    /**
     * 广告播放完成
     */
    public void adOnComplete() {
        isAd = false;
        mMediaController.setIsAd(false);
        mMediaController.setMovieUi(isMovie);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        mShutterView.setVisibility(View.GONE);
        mVideoFrame.setAspectRatio(
                height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
    }

    private AVPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(this, "AVPlayer");
        switch (mContentType) {
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(this, userAgent, mContentUri.toString());
            default:
                throw new IllegalStateException("Unsupported type:" + mContentType);
        }
    }

    private void preparePlayer(boolean playWhenReady) {
        if (mPlayer == null) {
            mPlayer = new AVPlayer(getRendererBuilder(), this);
            mPlayer.addListener(this);
            mPlayer.setCaptionListener(this);
            mPlayer.setMetadataListener(this);
            mPlayer.seekTo(mPlayerPosition);
            mPlayerNeedsPrepare = true;
            mMediaController.setMediaPlayer(mPlayer.getPlayerControl());
            mMediaController.setEnabled(true);
            mMediaController.setInterfaceListener(this);
            mEventLogger = new EventLogger();
            mEventLogger.startSession();
            mPlayer.addListener(mEventLogger);
            mPlayer.setInfoListener(mEventLogger);
            mPlayer.setInternalErrorListener(mEventLogger);
        }
        if (mPlayerNeedsPrepare) {
            mPlayer.prepare();
            mPlayerNeedsPrepare = false;
        }
        mPlayer.setSurface(mSurfaceView.getHolder().getSurface());
        mPlayer.setPlayWhenReady(playWhenReady);
    }

    protected void releasePlayer() {
        if (mPlayer != null) {
            mPlayerPosition = mPlayer.getCurrentPosition();
            mPlayer.release();
            mPlayer = null;
            if (mEventLogger == null) return;
            mEventLogger.endSession();
            mEventLogger = null;
        }
    }

    /**
     * 获取弹幕
     */
    protected void getBullets() {
        TLog.log("Bullet", "get bullets" + mVideoId);
        if (mDanmakuView != null) {
            mDanmakuView.release();
        }
        NetUtils.getBullets(mVideoId, this);
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {
            case HttpApis.http_bullet:
                ResponseBullet res = (ResponseBullet) value;
                danmakuControl.setDanmuListData(res.getBody());
                danmakuControl.prepareDanmaku();
                if (mMediaController.getBulletScreen()) {
                    mDanmakuView.hide();
                } else {
                    mDanmakuView.show();
                }
                break;
            case HttpApis.http_add_bullet:
                String str = (String) value;

                if (TextUtils.isEmpty(str) || !str.equals(ResponseCode.Success)) return;
                UT.showNormal("弹幕成功");
                TLog.error("bulletaa--->" + bullet.toString());
                danmakuControl.addDanmaku(bullet);
                break;
        }
    }

    @Override
    public void onFail(String error, int flag) {
        super.onFail(error, flag);
        switch (flag) {
            case HttpApis.http_add_bullet:
                UT.showNormal(error);
                break;

        }
    }

    private Bullet bullet;

    /**
     * 添加弹幕
     *
     * @param bullet {@link Bullet}
     */
    protected void addBullet(Bullet bullet) {
        if (UserMgr.isLogin()) {
            NetUtils.addBullet(bullet, mVideoId, mPlayer.getCurrentPosition() / 1000, this);
        } else {
            UnLoginHandler.unLogin(this);
        }
    }

    protected void setVideoTitle(String title) {
        mMediaController.setTitle(title);
    }

    protected void setQualitySwitchText(String name) {
        mMediaController.setmQualitySwitch(name);
    }

    protected void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    /**
     * Seek to certain position, unit is millisecond.
     *
     * @param positionMs
     */
    protected void seekTo(long positionMs) {
        if (mPlayer != null && mPlayer.getPlaybackState() != AVPlayer.STATE_PREPARING) {
            mPlayer.seekTo(positionMs);
        }
        if (mDanmakuView != null) {
            mDanmakuView.seekTo(positionMs);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onOpenBulletEditor() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment bulletDialog = BulletSendDialog.newInstance(this);
        bulletDialog.show(ft, "dialog");
    }

    @Override
    public void onSendBulletClick(Bullet bullet) {
        if (mMediaController.getBulletScreen()) {
            if (TextUtils.isEmpty(bullet.getContent().trim())) {
                UT.showNormal("弹幕不能为空");
                return;
            }
            if (!TextUtils.isEmpty(bullet.getContent().trim()) && bullet.getContent().length() > 50) {
                UT.showNormal("弹幕仅限50字");
                return;
            }
            this.bullet = bullet;
            addBullet(bullet);
        } else {
            UT.showNormal("请先开启弹幕功能");
        }
    }

    @Override
    public void onBulletSwitchCheck(boolean isChecked) {
        if (isChecked) {
            mMediaController.setBulletScreen(true);
            mDanmakuView.show();
        } else {
            mMediaController.setBulletScreen(false);
            mDanmakuView.hide();
        }
    }

    @Override
    public void onPassAd() {

    }

    /**
     * Checks whether it is necessary to ask for permission to read storage. If necessary, it also
     * requests permission.
     *
     * @return true if a permission request is made. False if it is not necessary.
     */
    @TargetApi(23)
    private boolean maybeRequestPermission() {
        if (requiresPermission(mContentUri)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(23)
    private boolean requiresPermission(Uri uri) {
        return Util.SDK_INT >= 23
                && Util.isLocalFileUri(uri)
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mMediaController != null) {
                mMediaController.doBackClick();
            }
        }
        return false;
    }
}
