package com.lt.hm.wovideo.video.player;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.MediaPlayerControl;
import com.lt.hm.wovideo.interf.OnMediaOtherListener;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.video.model.Bullet;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by KECB on 7/6/16.
 */

public class AVController extends FrameLayout implements View.OnTouchListener, AVPlayerGestureListener {
    private static final String TAG = "AVController";

    private MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private AppCompatSeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mFromXml;
    private boolean mListenersSet;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private TextView mVideoTitle;
    private TextView mQualitySwitch;
    private ImageView mChooseChannel;//选台
    private TextView mChooseAnthology;//选集
    private ImageView mMore;//更多
    private SwitchCompat mBulletSwitch;
    private ImageButton mPauseButton;
    private ImageButton mFullscreenButton;
    private ImageButton mSoundButton;//广告声音
    private TextView tv_know_more;//广告详情
    private TextView tv_pass_ad;//跳过广告按钮
    private ImageView mBackButton;
    private ImageButton mSendBulletButton;
    private View layout_bottom;//进度条布局
    private View rl_top;//播放器头
    private View view_ad_count_dowm;//倒计时布局
    private ViewGroup textureViewContainer;
    private LinearLayout ly_seek;
    private Handler mHandler = new MessageHandler(this);
    private boolean mIsBulletScreenOn = false;
    // Getsutre
    private GestureDetector mGestureDetector;
    // Volume, Brightness controller & view
    private View mCenterLayout;
    private ImageView mCenterImage;
    private ProgressBar mCenterPorgress;
    private float mCurBrightness = -1;
    private float mCurVolume = -1;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private View mScheduleLayout;
    private TextView mScheduleText;
    private ScreenSwitchUtils screenSwitchUtils;

    private OnMediaOtherListener onMediaOtherListener;
    private OnInterfaceInteract mInterfaceListener;
    private onTouchAd onTouchAdListener;
    private onSeekChange onSeekChangeListener;

    private boolean isAd = false;

    public void setOnMediaOtherListener(OnMediaOtherListener listener) {
        this.onMediaOtherListener = listener;
    }

    public void setOnSeekChangeListener(onSeekChange listener) {
        this.onSeekChangeListener = listener;
    }

    public void setonTouchAd(onTouchAd listener) {
        this.onTouchAdListener = listener;
    }

    public void setInterfaceListener(OnInterfaceInteract interfaceListener) {
        this.mInterfaceListener = interfaceListener;
    }

    public AVController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        mFromXml = true;

        Log.i(TAG, TAG);
    }

    public AVController(Context context, ScreenSwitchUtils screenSwitchUtils) {
        super(context);
        mContext = context;
        this.screenSwitchUtils = screenSwitchUtils;

        Log.i(TAG, TAG);
    }

    public void setIsAd(boolean isAd) {
        this.isAd = isAd;
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
        updateFullScreen();
        if (isAd) {
            show(0);
        }
    }

    public void setBulletScreen(boolean isShow) {
        mIsBulletScreenOn = isShow;
        mBulletSwitch.setChecked(mIsBulletScreenOn);
    }

    public boolean getBulletScreen() {
        if (mBulletSwitch == null) return mIsBulletScreenOn;
        else
            return mBulletSwitch.isChecked();
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(ViewGroup view) {
        mAnchor = view;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * set gesture listen to control media player
     * include screen brightness and volume of video
     *
     * @param context
     */
    public void setGestureListener(Context context) {
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(context, new ViewGestureListener(context, this, screenSwitchUtils));
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.media_controller, null);

        initControllerView(mRoot);
        return mRoot;
    }

    private String videoId;
    private long totalTime;

    private void initControllerView(View v) {

        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        textureViewContainer = (RelativeLayout) v.findViewById(R.id.surface_container);
        if (textureViewContainer != null) {
            textureViewContainer.setOnTouchListener(this);
        }
        Typeface fontFace = Typeface.createFromAsset(mContext.getAssets(), "Regular.ttf");
        tv_pass_ad = (TextView) v.findViewById(R.id.tv_pass_ad);
        tv_pass_ad.setTypeface(fontFace);

        view_ad_count_dowm = v.findViewById(R.id.view_ad_count_dowm);
        view_ad_count_dowm.setOnClickListener(mPassAdListener);

        layout_bottom = v.findViewById(R.id.layout_bottom);
        rl_top = v.findViewById(R.id.rl_top);

        mSoundButton = (ImageButton) v.findViewById(R.id.btn_sound);
        tv_know_more = (TextView) v.findViewById(R.id.tv_know_more);

        mVideoTitle = (TextView) v.findViewById(R.id.video_title);
        mChooseChannel = (ImageView) v.findViewById(R.id.tv_choose_channel);
        mChooseAnthology = (TextView) v.findViewById(R.id.tv_choose_anthology);
        mMore = (ImageView) v.findViewById(R.id.img_video_more);

        ly_seek = (LinearLayout) v.findViewById(R.id.ly_seek);

        if (mMore != null) {
            mMore.setOnClickListener(v1 -> {
                if (onMediaOtherListener != null)
                    onMediaOtherListener.onChooseMore(v1);
            });
        }

        if (mChooseChannel != null) {
            mChooseChannel.setOnClickListener(v1 -> {
                if (onMediaOtherListener != null)
                    onMediaOtherListener.onChooseChannel(v1);
            });
        }
        if (mChooseAnthology != null) {
            mChooseAnthology.setOnClickListener(v1 -> {
                if (onMediaOtherListener != null)
                    onMediaOtherListener.onChooseChannel(v1);
            });
        }

        mQualitySwitch = (TextView) v.findViewById(R.id.quality_switch);
        if (mQualitySwitch != null) {
            mQualitySwitch.setOnClickListener(v1 -> onMediaOtherListener.onShowQuality(v1));
        }

        mSendBulletButton = (ImageButton) v.findViewById(R.id.send_bullet);
        if (mSendBulletButton != null) {
            mSendBulletButton.setOnClickListener(mBulletSendListener);
        }

        mBulletSwitch = (SwitchCompat) v.findViewById(R.id.bullet_switch);
        if (mBulletSwitch != null) {
            mBulletSwitch.setChecked(mIsBulletScreenOn);
            mBulletSwitch.setOnCheckedChangeListener(mBulletSwitchListener);
        }

        mBackButton = (ImageView) v.findViewById(R.id.back);
        if (mBackButton != null) {
            mBackButton.setOnClickListener(mBackListener);
        }

        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mFullscreenButton = (ImageButton) v.findViewById(R.id.fullscreen);
        if (mFullscreenButton != null) {
            mFullscreenButton.requestFocus();
            mFullscreenButton.setOnClickListener(mFullscreenListener);
        }

        mProgress = (AppCompatSeekBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        // Volume & Brightness
        mCenterLayout = v.findViewById(R.id.layout_center);
        mCenterLayout.setVisibility(GONE);
        mCenterImage = (ImageView) v.findViewById(R.id.image_center_bg);
        mCenterPorgress = (ProgressBar) v.findViewById(R.id.progress_center);
        mScheduleLayout = v.findViewById(R.id.layout_center_schedule);
        mScheduleLayout.setVisibility(GONE);
        mScheduleText = (TextView) v.findViewById(R.id.schedule_center_text);

    }

    public void setmQualitySwitch(String name) {
        if (mQualitySwitch != null) {
            mQualitySwitch.setText(name);
        }
    }

    /**
     * 广告布局
     */
    public void setAdUi() {
        layout_bottom.setBackgroundColor(Color.parseColor("#00000000"));
        if (rl_top != null)
            rl_top.setBackgroundColor(Color.parseColor("#00000000"));

        tv_know_more.setVisibility(VISIBLE);
        mSoundButton.setVisibility(VISIBLE);
        setSeekBarVisible(GONE);
        setPassAdVisility(VISIBLE);
        setVideoTitleVisility(GONE);
        setSwitchVisibility(GONE);
        setBulletVisible(GONE);
        setmChooseChannel(GONE);
        setmSendBulletVisible(GONE);
        setChooseAnthologyVisible(GONE);
        setmMoreVisible(GONE);
        //      show(0);
    }

    /**
     * 电影布局UI
     *
     * @param isMovie
     */
    public void setMovieUi(boolean isMovie) {
        layout_bottom.setBackgroundColor(Color.parseColor("#CC000000"));
        if (rl_top != null)
            rl_top.setBackgroundColor(Color.parseColor("#CC000000"));
        tv_know_more.setVisibility(GONE);
        mSoundButton.setVisibility(GONE);
        setmChooseChannel(GONE);
        setBulletVisible(VISIBLE);
        setSwitchVisibility(VISIBLE);
        setVideoTitleVisility(VISIBLE);
        setSeekBarVisible(VISIBLE);
        setmMoreVisible(VISIBLE);
        setChooseAnthologyVisible(isMovie ? GONE : VISIBLE);
        setmSendBulletVisible(mIsBulletScreenOn ? VISIBLE : GONE);
        setPassAdVisility(GONE);
    }

    /**
     * 直播布局
     */
    public void setLiveUi() {
        setSwitchVisibility(INVISIBLE);
        setmChooseChannel(VISIBLE);
        setBulletVisible(GONE);
        setSeekBarVisible(GONE);
        setmSendBulletVisible(GONE);
        setPassAdVisility(GONE);
        setChooseAnthologyVisible(GONE);
        tv_know_more.setVisibility(GONE);
        mSoundButton.setVisibility(GONE);
        setmMoreVisible(GONE);
    }

    /**
     * 广告倒计时按钮
     *
     * @param visibility
     */
    public void setPassAdVisility(int visibility) {
        if (view_ad_count_dowm != null) {
            view_ad_count_dowm.setVisibility(visibility);
        }
    }

    public void setChooseAnthologyVisible(int visible) {
        if (mChooseAnthology != null) {
            mChooseAnthology.setVisibility(visible);
        }
    }

    public void setmMoreVisible(int visible) {
        if (mMore != null) {
            mMore.setVisibility(visible);
        }
    }

    public void setVideoTitleVisility(int visibility) {
        if (mVideoTitle != null) {
            mVideoTitle.setVisibility(visibility);
        }
    }

    public void setSwitchVisibility(int visibility) {
        if (mQualitySwitch != null) {
            mQualitySwitch.setVisibility(visibility);
        }
    }

    public void setBulletVisible(int visible) {
        if (mBulletSwitch != null) {
            mBulletSwitch.setVisibility(visible);
        }
    }

    public void setmChooseChannel(int visible) {
        if (mChooseChannel != null) {
            mChooseChannel.setVisibility(visible);
        }
    }

    public void setmSendBulletVisible(int visible) {
        if (mSendBulletButton != null) {
            mSendBulletButton.setVisibility(visible);
        }
    }

    public void setSeekBarVisible(int visible) {
        if (ly_seek != null) {
            ly_seek.setVisibility(visible);
        }
    }

    public void setmFullscreenVisible(int visible) {
        if (mFullscreenButton != null) {
            mFullscreenButton.setVisibility(visible);
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        if (mPlayer == null) {
            return;
        }

        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
            );

            mAnchor.addView(this, tlp);
            mShowing = true;
        }
        updatePausePlay();
        updateFullScreen();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null) {
            return;
        }

        try {
            mAnchor.removeView(this);
            mHandler.removeMessages(SHOW_PROGRESS);
        } catch (IllegalArgumentException ex) {
            Log.w("MediaController", "already removed");
        }
        mShowing = false;
    }

    private String stringForTime(long timeMs) {
        int totalSeconds = (int) timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int duration;

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        TLog.error("---->setProgress");
        int position = (int) mPlayer.getCurrentPosition();
        duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        if (isAd) {
            showAdCountDown(((duration - position) / 1000) % 60);
            if (((duration - position) / 1000) % 60 == 0 && duration != 0) {
                TLog.error("time--->");
                onTouchAdListener.onAdComplete();
            }
            TLog.error("time--->" + ((duration - position) / 1000) % 60);
        } else {
            TLog.error("time--->" + position);
//            if (onSeekChangeListener != null && (position / 1000) % 10 == 0)
            if (onSeekChangeListener != null)
                onSeekChangeListener.onSeekChange(position / 1000, duration / 1000, false);

        }

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TLog.error("keyeventdd--dd->" + event.getAction());
        if (null != mGestureDetector) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlayer == null) {
            return true;
        }

        TLog.error("keyeventdd--->" + event.getAction());
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;

        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

//    show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private OnClickListener mBulletSendListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mInterfaceListener != null) {
                mInterfaceListener.onOpenBulletEditor();
            }
        }
    };

    private OnClickListener mPassAdListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onTouchAdListener != null) {
                onTouchAdListener.onPassAd();
            }
        }
    };


    private CompoundButton.OnCheckedChangeListener mBulletSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //TODO
            mIsBulletScreenOn = isChecked;
            mBulletSwitch.setChecked(mIsBulletScreenOn);
            toggleBulletScreen(isChecked);
            setmSendBulletVisible(mIsBulletScreenOn ? VISIBLE : GONE);
        }
    };

    /**
     * for override
     *
     * @param isShow
     */
    public void toggleBulletScreen(boolean isShow) {
        if (mInterfaceListener != null) {
            mInterfaceListener.onBulletSwitchCheck(isShow);
        }
    }

    public void setTitle(String titleName) {
        if (mVideoTitle != null) {
            mVideoTitle.setText(titleName);
        }
    }

    private View.OnClickListener mBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            doBackClick();
        }
    };

    /**
     * 返回处理
     */
    public void doBackClick() {
        if (mPlayer == null || screenSwitchUtils == null) {
            ((Activity) mContext).finish();
            return;
        }
        if (!screenSwitchUtils.isPortrait()) {
            doToggleFullscreen();
        } else {
            mPlayer.releasePlay();
            ((Activity) mContext).finish();
        }
    }

    private View.OnClickListener mPauseListener = v -> {
        doPauseResume();
//      show(sDefaultTimeout);
    };

    private View.OnClickListener mFullscreenListener = v -> {
        doToggleFullscreen();
//      show(sDefaultTimeout);
    };

    public void updatePausePlay() {
        if (mRoot == null || mPauseButton == null || mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_pause));
        } else {
            mPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play));
        }
    }

    public void updateFullScreen() {
        if (mRoot == null || mFullscreenButton == null || mPlayer == null || screenSwitchUtils == null) {
            return;
        }

        if (screenSwitchUtils.isPortrait()) {
            mFullscreenButton.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        } else {
            mFullscreenButton.setVisibility(GONE);
        }
    }

    private void doPauseResume() {
        if (mPlayer == null || onSeekChangeListener == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            onSeekChangeListener.onPauseOrPlay(false);
        } else {
            mPlayer.start();
            onSeekChangeListener.onPauseOrPlay(true);
        }
        updatePausePlay();
    }

    public void doPauseResume(boolean toPlay) {
        if (mPlayer == null || onSeekChangeListener == null) {
            return;
        }

        if (toPlay) {
            if (mPlayer.isPlaying()) return;
            mPlayer.start();
            onSeekChangeListener.onPauseOrPlay(true);
        } else {
            if (mPlayer.isPlaying()){
                mPlayer.pause();
                onSeekChangeListener.onPauseOrPlay(false);
            }
        }
        updatePausePlay();
    }

    public void doToggleFullscreen() {
        if (mPlayer == null) {
            return;
        }

        mPlayer.toggleFullScreen(screenSwitchUtils);
    }

    private long newposition;
    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;
            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);

            TLog.error("seek---->onStartTrackingTouch");
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            TLog.error("seek---->onProgressChanged");
            if (mPlayer == null) {
                return;
            }
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));

        }

        public void onStopTrackingTouch(SeekBar bar) {
            TLog.error("seek---->onStopTrackingTouch" + duration);
            mDragging = false;

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
            if (onSeekChangeListener != null && duration != 0)
                onSeekChangeListener.onSeekChange((int) newposition / 1000, duration / 1000, true);

        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AVController.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AVController.class.getName());
    }

    @Override
    public void onSingleTap() {
        if (isAd) return;

        if (isShowing()) {
            hide();
        } else {
            show(sDefaultTimeout);
        }

    }

    @Override
    public void onHorizontalScroll(MotionEvent event, float delta) {
        show(sDefaultTimeout);
        Log.i(TAG, delta + "");
        if (event.getPointerCount() == 1)
            if (mPlayer == null) return;
        long toPosition = mPlayer.getCurrentPosition() + Math.round(delta);
        updateSchedule(toPosition);
        mPlayer.seekTo(toPosition);
    }

    private void updateSchedule(long toPosition) {
        mScheduleLayout.setVisibility(VISIBLE);
        mCenterLayout.setVisibility(INVISIBLE);
        mScheduleText.setText(stringForTime(toPosition) + " / " + stringForTime(mPlayer.getDuration()));
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mScheduleLayout.setVisibility(GONE);
            }
        }, 1000);

    }

    @Override
    public void onVerticalScroll(MotionEvent event, float delta, int direction) {
        show(sDefaultTimeout);
        Log.i(TAG, delta + "");
        if (event.getPointerCount() == 1) {
            if (direction == ViewGestureListener.SWIPE_LEFT) {
                onBrightnessSlide(delta);
                Log.i(TAG, "onVerticalScroll: Brightness");
            } else {
                onVolumeSlide(delta);
                Log.i(TAG, "onVerticalScroll: Volume");
            }
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCenterLayout.setVisibility(GONE);
                }
            }, 1000);
        }
    }

    /**
     * 改变音量
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        mCenterImage.setImageResource(R.drawable.video_volumn_bg);
        mCenterLayout.setVisibility(VISIBLE);
        //判断当前音量是否是-1，是则没有获取当前系统音量
        if (mCurVolume == -1) {
            //获得系统当前音量
            mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            //判断当前音量是否为负数，是则设置最小为0
            if (mCurVolume < 0) {
                //如果当前音量小于0则给变量赋最小的值0
                mCurVolume = 0;
            }
        }
        mCurVolume = (percent * mMaxVolume) + mCurVolume;
        //计算用户滑动的距离
        int index = (int) mCurVolume;
        //判断该距离是否大于最大音量值，是则把给变量赋值为最大值，否则最小值
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        //设置音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        float per = (float) ((index * 1.0 / mMaxVolume) * 100);
        mCenterPorgress.setProgress((int) per);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        mCenterImage.setImageResource(R.drawable.video_bright_bg);
        mCenterLayout.setVisibility(VISIBLE);
        if (mCurBrightness < 0) {
            //获得屏幕亮度
            mCurBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            //判断当前亮度是否小于0.01f，是的话则给变量赋值0.01f放置屏幕变黑
            if (mCurBrightness <= 0.01f)
                mCurBrightness = 0.01f;
        }
        /**WindowManager.LayoutParams 是 WindowManager 接口的嵌套类；
         它继承于 ViewGroup.LayoutParams； 它用于向WindowManager描述Window的管理策略。**/
        //获取window窗口属性
        WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow().getAttributes();
        //将当前屏幕亮度加上我们移动的距离的值赋给lpa（窗口管理）
        mCurBrightness += percent;
        lpa.screenBrightness = mCurBrightness;
        //判断当前亮度是否大于1，是则设置为1,（亮度最大只能是1）
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
            //判断是否小于0.01，防止黑屏最小设置成0.01
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        //让设置好的亮度生效
        ((Activity) mContext).getWindow().setAttributes(lpa);
        float per = lpa.screenBrightness * 100;
        mCenterPorgress.setProgress((int) per);
    }

    protected boolean mTouchingProgressBar;
    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume;
    protected boolean mChangePosition;
    protected int mDownPosition;
    protected int mGestureDownVolume;
    protected int mSeekTimePosition;
    public static final int THRESHOLD = 80;
    protected int mScreenHeight;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchingProgressBar = true;

                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    /////////////////////
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (!screenSwitchUtils.isPortrait()) {
                        if (!mChangePosition && !mChangeVolume) {
                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                                // cancelProgressTimer();
                                if (absDeltaX >= THRESHOLD) {
                                    mChangePosition = true;
                                    //    mDownPosition = getCurrentPositionWhenPlaying();
                                } else {
                                    mChangeVolume = true;
                                    mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                }
                            }
                        }
                    }
//                    if (mChangePosition) {
//                        int totalTimeDuration = getDuration();
//                        mSeekTimePosition = (int) (mDownPosition + deltaX * totalTimeDuration / mScreenWidth);
//                        if (mSeekTimePosition > totalTimeDuration)
//                            mSeekTimePosition = totalTimeDuration;
//                        String seekTime = JCUtils.stringForTime(mSeekTimePosition);
//                        String totalTime = JCUtils.stringForTime(totalTimeDuration);
//
//                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
//                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                        onVolumeSlide(volumePercent);
                        //     showVolumDialog(-deltaY, volumePercent);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    mTouchingProgressBar = false;
                    mCenterLayout.setVisibility(GONE);
//                    if (mChangePosition) {
//                        onEvent(JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_POSITION);
//                        JCMediaManager.instance().mediaPlayer.seekTo(mSeekTimePosition);
//                        int duration = getDuration();
//                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
//                        progressBar.setProgress(progress);
//                    }
//                    if (mChangeVolume) {
//                        onEvent(JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_VOLUME);
//                    }
//                    startProgressTimer();
                    break;
            }
        }
        return false;
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<AVController> mView;

        MessageHandler(AVController view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            AVController view = mView.get();
            if (view == null || view.mPlayer == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = view.setProgress();
                    if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

    public void showAdCountDown(long time) {
        if (tv_pass_ad != null)
            tv_pass_ad.setText(time + "");
    }


    public interface OnInterfaceInteract {
        void onOpenBulletEditor();

        void onSendBulletClick(Bullet bullet);

        void onBulletSwitchCheck(boolean isChecked);

    }


    public interface onTouchAd {
        void onPassAd();

        void onAdComplete();
    }

    public interface onSeekChange {
        void onSeekChange(int time, int totalTime, boolean isActive);

        void onPauseOrPlay(boolean play);
    }

}
