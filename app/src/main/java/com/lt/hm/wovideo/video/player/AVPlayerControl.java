package com.lt.hm.wovideo.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.exoplayer.ExoPlayer;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;

/**
 * An implementation of {@link AVController.MediaPlayerControl} for controlling an {@link ExoPlayer}
 * instance.
 * <p>
 * Created by KECB on 7/7/16.
 */

public class AVPlayerControl implements AVController.MediaPlayerControl {

    private final ExoPlayer mExoPlayer;
    private final Context mContext;

    // Sensor
    private SensorManager mSensorManager;
    private boolean isLandScape = false;

    public AVPlayerControl(ExoPlayer exoPlayer, Context context) {
        mExoPlayer = exoPlayer;
        mContext = context;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    /**
     * This is an unsupported operation.
     * <p>
     * Application of audio effects is dependent on the audio renderer used. When using
     * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer}, the recommended approach is
     * to extend the class and override
     * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer#onAudioSessionId(int)}.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    @Override
    public int getAudioSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFullScreen() {
        //TODO depends on accelarate
      return isLandScape;
    }

    @Override
    public void toggleFullScreen(ScreenSwitchUtils screenSwitchUtils) {
        if (screenSwitchUtils != null){
            screenSwitchUtils.toggleScreen();
        }

    }

    @Override
    public int getBufferPercentage() {
        return mExoPlayer.getBufferedPercentage();
    }

    @Override
    public long getCurrentPosition() {
        return mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : mExoPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) mExoPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mExoPlayer.getPlayWhenReady();
    }

    @Override
    public void start() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void seekTo(long pos) {
        long seekPosition = mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : Math.min(Math.max(0, pos), getDuration());
        mExoPlayer.seekTo(seekPosition);
    }


}
