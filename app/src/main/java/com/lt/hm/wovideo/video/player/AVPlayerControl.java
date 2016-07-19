package com.lt.hm.wovideo.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.exoplayer.ExoPlayer;

/**
 * An implementation of {@link AVController.MediaPlayerControl} for controlling an {@link ExoPlayer}
 * instance.
 * <p>
 * Created by KECB on 7/7/16.
 */

public class AVPlayerControl implements AVController.MediaPlayerControl, SensorEventListener{

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

  @Override public boolean canPause() {
    return true;
  }

  @Override public boolean canSeekBackward() {
    return true;
  }

  @Override public boolean canSeekForward() {
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
   * @throws  UnsupportedOperationException Always thrown.
   */
  @Override public int getAudioSessionId() {
    throw new UnsupportedOperationException();
  }

  @Override public boolean isFullScreen() {
    //TODO depends on accelarate
//    return ((Activity)mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? true : false ;
    return isLandScape;
  }

  @Override public void toggleFullScreen() {
    Activity activity = (Activity)mContext;
    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    if(isFullScreen()){
      activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      isLandScape = false;
    }else {
      activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      isLandScape = true;
    }
    //This is the default value
    //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }

  @Override public int getBufferPercentage() {
    return mExoPlayer.getBufferedPercentage();
  }

  @Override public long getCurrentPosition() {
    return mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
        :  mExoPlayer.getCurrentPosition();
  }

  @Override public int getDuration() {
    return mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
        : (int) mExoPlayer.getDuration();
  }

  @Override public boolean isPlaying() {
    return mExoPlayer.getPlayWhenReady();
  }

  @Override public void start() {
    mExoPlayer.setPlayWhenReady(true);
  }

  @Override public void pause() {
    mExoPlayer.setPlayWhenReady(false);
  }

  @Override public void seekTo(long pos) {
    long seekPosition = mExoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
        : Math.min(Math.max(0, pos), getDuration());
    mExoPlayer.seekTo(seekPosition);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    Activity activity = (Activity) mContext;
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      Log.d("Sensor", "sensor: " + event.sensor + ", x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2]);
      if (event.values[0] > 9 || event.values[0] < -9) {
        // rotate 90 degrees or rotate -90 degrees
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
          activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
          mSensorManager.unregisterListener(this);
        }
      }

      if (event.values[0] > -8 && event.values[0] < 8) {
        // portait
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || activity.getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT){
          activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
          mSensorManager.unregisterListener(this);
        }
      }
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

}
