package com.lt.hm.wovideo.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vov.vitamio.utils.Log;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/19
 */
public class Controller extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String LOG_TAG = Controller.class.getName();
    private static final int sDefaultTimeout = 3000;
    @BindView(R.id.controller_start)
    ImageView controllerStart;
    @BindView(R.id.controller_cur_position)
    TextView controllerCurPosition;
    @BindView(R.id.controller_expand)
    ImageView controllerExpand;
    @BindView(R.id.controller_total_duration)
    TextView controllerTotalDuration;
    @BindView(R.id.controller_seekbar)
    SeekBar controllerSeekbar;
    @BindView(R.id.controller_shink)
    ImageView controllerShink;
    private Context mContext;
    MediaPlayerControl mPlayer;
    private boolean mShowing;
    private View mAnchor;//?
    private boolean mFromXml = false;//?
    private PopupWindow mWindow;//?
    private int mAnimStyle;//?
    private AudioManager mAM;//音频管理器
    private OnShownListener mShownListener;
    private OnHiddenListener mHiddenListener;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;

    Unbinder unbinder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
//                    pos = setProgress();
//                    if (!mDragging && mShowing) {
//                        msg = obtainMessage(SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000 - (pos % 1000));
//                        updatePausePlay();
//                    }
                    break;
            }
        }
    };

    private void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                if (mFromXml)
                    setVisibility(View.GONE);
                else
                    mWindow.dismiss();
            } catch (IllegalArgumentException ex) {
                Log.d("MediaController alreaddy removed");
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    public void setmPlayer(MediaPlayerControl mPlayer) {
        this.mPlayer = mPlayer;
    }

    public Controller(Context context) {
        super(context);
        initViews(context);
    }

    public Controller(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        this.mContext = context;
        View view = View.inflate(context, R.layout.layout_my_controller, this);
        unbinder = ButterKnife.bind(view);
        mAM= (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        initEvents();
    }

    private void initEvents() {
        controllerSeekbar.setOnSeekBarChangeListener(this);
        controllerStart.setOnClickListener(this);
        controllerShink.setOnClickListener(this);
        controllerExpand.setOnClickListener(this);
//            setPageType(PageType.SHRINK);
//            setPlayState(PlayState.PAUSE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.controller_shink:
                break;
            case R.id.controller_expand:
                break;
            case R.id.controller_start:
                doPauseResume();
                break;
        }
    }


    public void show(int timeout) {
        if (!mShowing && mAnchor != null && mAnchor.getWindowToken() != null) {
            if (controllerStart != null)
                controllerStart.requestFocus();

            if (mFromXml) {
                setVisibility(View.VISIBLE);
            } else {
                int[] location = new int[2];

                mAnchor.getLocationOnScreen(location);
                Rect anchorRect = new Rect(location[0], location[1], location[0] + mAnchor.getWidth(), location[1] + mAnchor.getHeight());

                mWindow.setAnimationStyle(mAnimStyle);
                setWindowLayoutType();
                mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, anchorRect.left, anchorRect.bottom);
            }
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT), timeout);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setWindowLayoutType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                Method setWindowLayoutType = PopupWindow.class.getMethod("setWindowLayoutType", new Class[] { int.class });
                setWindowLayoutType.invoke(mWindow, WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
            } catch (Exception e) {
                Log.e("setWindowLayoutType", e);
            }
        }
    }


    private void setPlayPosition(int nowSecond, int allSecond){
        String cur_position = "00:00";
        String end_position="00:00";
        if (nowSecond>0){
            controllerCurPosition.setText(formatPlayTime(nowSecond));
        }
        if (allSecond>0){
            controllerTotalDuration.setText(formatPlayTime(allSecond));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }


    // seek bar change Listener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    private void doPauseResume() {
        if (mPlayer.isPlaying())
            mPlayer.pause();
        else
            mPlayer.start();
        updatePausePlay();
    }

    private void updatePausePlay() {
//        if (mPlayer.isPlaying())
//            controllerStart.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
//        else
//            controllerStart.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
        if (controllerStart==null) return;
        controllerStart.setImageResource(mPlayer.isPlaying() ? com.android.tedcoder.wkvideoplayer.R.drawable.biz_video_pause : R.drawable.icon_play);
    }

    //controller 显示
    public interface OnShownListener {
        public void onShown();
    }
    //controller 隐藏
    public interface OnHiddenListener {
        public void onHidden();
    }
    //controller 控制回调
    public interface MediaPlayerControl{

        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long pos);

        boolean isPlaying();

        int getBufferPercentage();

        void onPageTypeChange();

    }


    /**
     * 播放样式 展开、缩放
     */
    public enum MyPageType {
        EXPAND, SHRINK
    }
    //播放状态
    public enum MyPlayState{
        PLAY,PAUSE,FINISH;
    }
    //进度条状态
    public enum MyProgressState{
        START,STOP,PLAYING;
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

}
