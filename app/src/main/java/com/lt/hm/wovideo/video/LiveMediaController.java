package com.lt.hm.wovideo.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vov.vitamio.widget.MediaController;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class LiveMediaController extends MediaController implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    Context mContext;
    @BindView(R.id.pause)
    ImageView mPlayImg;
    @BindView(R.id.media_controller_progress)
    SeekBar mProgressSeekBar;
    @BindView(R.id.time)
    TextView mTimeTxt;
    @BindView(R.id.click_layout)
    RelativeLayout clickLayout;
    @BindView(R.id.view_menu_placeholder)
    View viewMenuPlaceholder;
    @BindView(R.id.expand)
    ImageView mExpandImg;
    @BindView(R.id.shrink)
    ImageView mShrinkImg;

    Unbinder unbinder;
    private MediaControlImpl mMediaControl;

    public void setMediaControl(MediaControlImpl mediaControl) {
        mMediaControl = mediaControl;
    }

    public LiveMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }


    public LiveMediaController(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;
      View view =   View.inflate(context, R.layout.layout_video_controller, this);
        unbinder = ButterKnife.bind(view);
        initEvents();

    }

    private void initEvents() {
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        mPlayImg.setOnClickListener(this);
        mShrinkImg.setOnClickListener(this);
        mExpandImg.setOnClickListener(this);
        setPageType(PageType.SHRINK);
        setPlayState(PlayState.PAUSE);
    }
    public void setPlayState(PlayState playState) {
        mPlayImg.setImageResource(playState.equals(PlayState.PLAY) ? com.android.tedcoder.wkvideoplayer.R.drawable.biz_video_pause : com.android.tedcoder.wkvideoplayer.R.drawable.biz_video_play);
    }

    public void setPlayProgressTxt(int nowSecond, int allSecond) {
        mTimeTxt.setText(getPlayTime(nowSecond, allSecond));
    }

    public void setPageType(PageType pageType) {
        mExpandImg.setVisibility(pageType.equals(PageType.EXPAND) ? GONE : VISIBLE);
        mShrinkImg.setVisibility(pageType.equals(PageType.SHRINK) ? GONE : VISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mMediaControl.onProgressTurn(ProgressState.DOING, progress);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.START, 0);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.STOP, 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pause) {
            mMediaControl.onPlayTurn();
        } else if (view.getId() == R.id.expand) {
            mMediaControl.onPageTurn();
        } else if (view.getId() == R.id.shrink) {
            mMediaControl.onPageTurn();
        }
    }
    private String getPlayTime(int playSecond, int allSecond) {
        String playSecondStr = "00:00";
        String allSecondStr = "00:00";
        if (playSecond > 0) {
            playSecondStr = formatPlayTime(playSecond);
        }
        if (allSecond > 0) {
            allSecondStr = formatPlayTime(allSecond);
        }
        return playSecondStr + "/" + allSecondStr;
    }

    @SuppressLint("SimpleDateFormat")
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    public void setProgressBar(int progress, int secondProgress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        if (secondProgress < 0) secondProgress = 0;
        if (secondProgress > 100) secondProgress = 100;
        mProgressSeekBar.setProgress(progress);
        mProgressSeekBar.setSecondaryProgress(secondProgress);
    }

    public void playFinish(int allTime) {
        mProgressSeekBar.setProgress(0);
        setPlayProgressTxt(0, allTime);
        setPlayState(PlayState.PAUSE);
    }
    /**
     * 播放样式 展开、缩放
     */
    public enum PageType {
        EXPAND, SHRINK
    }
    /**
     * 播放状态 播放 暂停
     */
    public enum PlayState {
        PLAY, PAUSE
    }

    public enum ProgressState {
        START, DOING, STOP
    }

    public interface MediaControlImpl {
        void onPlayTurn();

        void onPageTurn();

        void onProgressTurn(ProgressState state, int progress);

        void onSelectFormat(int position);

        void alwaysShowController();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
