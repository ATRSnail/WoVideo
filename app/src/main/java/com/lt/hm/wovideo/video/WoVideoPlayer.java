package com.lt.hm.wovideo.video;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vov.vitamio.MediaPlayer;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class WoVideoPlayer extends RelativeLayout {
    private final int MSG_HIDE_CONTROLLER = 10;
    private final int MSG_UPDATE_PLAY_TIME = 11;
    Context mContext;
    @BindView(R.id.video_view)
    WoVideoView mSuperVideoView;
    @BindView(R.id.txt_dlna_title)
    TextView txtDlnaTitle;
    @BindView(R.id.txt_dlna_exit)
    TextView txtDlnaExit;
    @BindView(R.id.rel_dlna_root_layout)
    RelativeLayout relDlnaRootLayout;
    @BindView(R.id.controller)
    LiveMediaController mMediaController;
    @BindView(R.id.progressbar)
    View mProgressBarView;
    @BindView(R.id.video_close_view)
    FrameLayout videoCloseView;
    @BindView(R.id.video_share_tv_view)
    FrameLayout videoShareTvView;
    @BindView(R.id.video_inner_container)
    RelativeLayout videoInnerContainer;
    @BindView(R.id.video_back_icon)
    View back_icon;

    private LiveMediaController.PageType mCurrPageType = LiveMediaController.PageType.SHRINK;//当前是横屏还是竖屏
    //是否自动隐藏控制栏
    private boolean mAutoHideController = true;
    private VideoModel mNowPlayVideo;
    private VideoPlayCallbackImpl mVideoPlayCallback;
    private Timer mUpdateTimer;
    Unbinder unbinder;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_PLAY_TIME) {
                updatePlayTime();
                updatePlayProgress();
            } else if (msg.what == MSG_HIDE_CONTROLLER) {
                showOrHideController();
            }
            return false;
        }
    });
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    /*
                     * add what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING
                     * fix : return what == 700 in Lenovo low configuration Android System
                     */
//                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
//                            || what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
//                        mProgressBarView.setVisibility(View.GONE);
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START
                            || what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
                        mProgressBarView.setVisibility(View.GONE);
//                        setCloseButton(true);
//                        initDLNAInfo();
                        return true;
                    }
                    return false;
                }
            });

        }
    };
    private View.OnTouchListener mOnTouchVideoListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                showOrHideController();
            }
            return mCurrPageType == LiveMediaController.PageType.EXPAND;
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stopUpdateTimer();
            stopHideTimer(true);
            mMediaController.playFinish((int) mSuperVideoView.getDuration());
            mVideoPlayCallback.onPlayFinish();
            Toast.makeText(mContext, "视频播放完成", Toast.LENGTH_SHORT).show();
        }
    };
    private LiveMediaController.MediaControlImpl mMediaControl = new LiveMediaController.MediaControlImpl() {
        @Override
        public void alwaysShowController() {
            WoVideoPlayer.this.alwaysShowController();
        }

        @Override
        public void onSelectFormat(int position) {
            VideoUrl videoUrl = mNowPlayVideo.getmVideoUrl().get(position);
            if (mNowPlayVideo.getmPlayUrl().equal(videoUrl)) return;
            mNowPlayVideo.setPlayUrl(position);
            playVideoAtLastPos();
        }

        @Override
        public void onPlayTurn() {
            if (mSuperVideoView.isPlaying()) {
                pausePlay(true);
            } else {
                goOnPlay();
            }
        }

        /***
         * 继续播放
         */
        public void goOnPlay() {
            mSuperVideoView.start();
            mMediaController.setPlayState(LiveMediaController.PlayState.PLAY);
            resetHideTimer();
            resetUpdateTimer();
        }

        /**
         * 暂停播放
         *
         * @param isShowController 是否显示控制条
         */
        public void pausePlay(boolean isShowController) {
            mSuperVideoView.pause();
            mMediaController.setPlayState(LiveMediaController.PlayState.PAUSE);
            stopHideTimer(isShowController);
        }

        private void stopHideTimer(boolean isShowController) {
            mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            mMediaController.clearAnimation();
            mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
        }


        @Override
        public void onPageTurn() {
            mVideoPlayCallback.onSwitchPageType();
        }

        @Override
        public void onProgressTurn(LiveMediaController.ProgressState state, int progress) {
            if (state.equals(LiveMediaController.ProgressState.START)) {
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(LiveMediaController.ProgressState.STOP)) {
                resetHideTimer();
            } else {
                int time = (int) (progress * mSuperVideoView.getDuration() / 100);
                mSuperVideoView.seekTo(time);
                updatePlayTime();
            }
        }
    };

    public WoVideoPlayer(Context context) {
        super(context);
        initView(context);
    }

    public WoVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setVideoPlayCallback(VideoPlayCallbackImpl videoPlayCallback) {
        mVideoPlayCallback = videoPlayCallback;
    }

    /***
     *
     */
    private void showOrHideController() {
//        mMediaController.closeAllSwitchList();
        if (mMediaController.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    com.android.tedcoder.wkvideoplayer.R.anim.anim_exit_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mMediaController.setVisibility(View.GONE);
                }
            });
            mMediaController.startAnimation(animation);
        } else {
            mMediaController.setVisibility(View.VISIBLE);
            mMediaController.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    com.android.tedcoder.wkvideoplayer.R.anim.anim_enter_from_bottom);
            mMediaController.startAnimation(animation);
            resetHideTimer();
        }
    }

    /**
     * 更新播放的进度时间
     */
    private void updatePlayTime() {
        int allTime = (int) mSuperVideoView.getDuration();
        int playTime = (int) mSuperVideoView.getCurrentPosition();
        mMediaController.setPlayProgressTxt(playTime, allTime);
    }

    /**
     * 更新播放进度条
     */
    private void updatePlayProgress() {
        int allTime = (int) mSuperVideoView.getDuration();
        int playTime = (int) mSuperVideoView.getCurrentPosition();
        int loadProgress = mSuperVideoView.getBufferPercentage();
        int progress = playTime * 100 / allTime;
        mMediaController.setProgressBar(progress, loadProgress);
    }

    private void initView(Context context) {
        mContext = context;
        View view = View.inflate(context, R.layout.layout_video_view, this);
        unbinder= ButterKnife.bind(view);
        initDatas();
    }

    /**
     * 更换清晰度地址时，续播
     */
    private void playVideoAtLastPos() {
        int playTime = (int) mSuperVideoView.getCurrentPosition();
        mSuperVideoView.stopPlayback();
        loadAndPlay(mNowPlayVideo.getmPlayUrl(), playTime);
    }

    /**
     * 加载并开始播放视频
     *
     * @param videoUrl videoUrl
     */
    public void loadAndPlay(VideoUrl videoUrl, int seekTime) {
        showProgressView(seekTime > 0);
//        setCloseButton(true);
        if (TextUtils.isEmpty(videoUrl.getFormatUrl())) {
            Log.e("TAG", "videoUrl should not be null");
            return;
        }
        mSuperVideoView.setOnPreparedListener(mOnPreparedListener);
        if (videoUrl.isOnlineVideo()) {
            mSuperVideoView.setVideoPath(videoUrl.getFormatUrl());
        } else {
            Uri uri = Uri.parse(videoUrl.getFormatUrl());
            mSuperVideoView.setVideoURI(uri);
        }
        mSuperVideoView.setVisibility(VISIBLE);
        startPlayVideo(seekTime);
    }

    /**
     * 播放视频
     * should called after setVideoPath()
     */
    private void startPlayVideo(int seekTime) {
        if (null == mUpdateTimer) resetUpdateTimer();
        resetHideTimer();
        mSuperVideoView.setOnCompletionListener(mOnCompletionListener);
        mSuperVideoView.start();
        if (seekTime > 0) {
            mSuperVideoView.seekTo(seekTime);
        }
        mMediaController.setPlayState(LiveMediaController.PlayState.PLAY);
    }

    /**
     * 显示loading圈
     *
     * @param isTransparentBg isTransparentBg
     */
    private void showProgressView(Boolean isTransparentBg) {
        mProgressBarView.setVisibility(VISIBLE);
        if (!isTransparentBg) {
            mProgressBarView.setBackgroundResource(android.R.color.black);
        } else {
            mProgressBarView.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void initDatas() {
        mMediaController.setMediaControl(mMediaControl);
        mSuperVideoView.setOnTouchListener(mOnTouchVideoListener);
        mSuperVideoView.setHardwareDecoder(true);
        mSuperVideoView.setBufferSize(5*1024*1024);

        back_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrPageType==LiveMediaController.PageType.EXPAND){
                    // TODO: 16/6/3 退出全屏模式，切换至竖屏模式

                }else{
                    // TODO: 16/6/3  退出该页面并 清除  播放器 当前缓存，节约内存占用
                    mVideoPlayCallback.onCloseVideo();

                }
            }
        });
    }

    private void alwaysShowController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.setVisibility(View.VISIBLE);
    }

    private void resetHideTimer() {
        if (!isAutoHideController()) return;
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        int TIME_SHOW_CONTROLLER = 4000;
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, TIME_SHOW_CONTROLLER);
    }

    private void resetUpdateTimer() {
        mUpdateTimer = new Timer();
        int TIME_UPDATE_PLAY_TIME = 1000;
        mUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
            }
        }, 0, TIME_UPDATE_PLAY_TIME);
    }

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }

    private void stopHideTimer(boolean isShowController) {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.clearAnimation();
        mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
    }

    public boolean isAutoHideController() {
        return mAutoHideController;
    }

    public interface VideoPlayCallbackImpl {
        void onCloseVideo();

        void onSwitchPageType();

        void onPlayFinish();
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    /**
     * 关闭视频
     */
    public void close() {
        mMediaController.setPlayState(LiveMediaController.PlayState.PAUSE);
        setPageType(LiveMediaController.PageType.SHRINK);
        stopHideTimer(true);
        stopUpdateTimer();
        mSuperVideoView.pause();
        mSuperVideoView.stopPlayback();
        mSuperVideoView.setVisibility(GONE);
    }

    public void setPageType(LiveMediaController.PageType pageType) {
        mMediaController.setPageType(pageType);
        mCurrPageType = pageType;
    }

}
