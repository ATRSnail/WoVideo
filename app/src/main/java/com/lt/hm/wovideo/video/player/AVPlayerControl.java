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

/**
 * An implementation of {@link AVController.MediaPlayerControl} for controlling an {@link ExoPlayer}
 * instance.
 * <p>
 * Created by KECB on 7/7/16.
 */

public class AVPlayerControl implements AVController.MediaPlayerControl,SensorEventListener{

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
    isClickFullScreenButton = true;
//    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
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
    TLog.error("sensor-->"+isLandScape+"-----"+event.sensor.getType());
    float[] values = event.values;
    int orientation = 0;
    float X = -values[SensorManager.DATA_X];
    float Y = -values[SensorManager.DATA_Y];
    float Z = -values[SensorManager.DATA_Z];
    float magnitude = X * X + Y * Y;
    // Don't trust the angle if the magnitude is small compared to the y
    // value
    if (magnitude * 4 >= Z * Z) {
      float OneEightyOverPi = 57.29577957855f;
      float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
      orientation = 90 - (int) Math.round(angle);
      // normalize to 0 - 359 range
      if (orientation >= 360) {
        orientation -= 360;
      }
      if (orientation < 0) {
        orientation += 360;
      }
    }

    if (isClickFullScreenButton) {
      // 竖屏
      if (isLandScape&& (((orientation > 315 && orientation <= 360) || (orientation >= 0 && orientation <= 45)) || (orientation > 135 && orientation <= 225))) {
        isLandScape = false;
        isClickFullScreenButton = false;
        isSennor = true;
      }

      // 横屏
      if (!isLandScape
              && ((orientation > 45 && orientation <= 135) || (orientation > 225 && orientation <= 315))) {
        isLandScape = true;
        isClickFullScreenButton = false;
        isSennor = true;
      }
    }
    if (!isSennor) {// 判断是否要进行中断信息传递
      return;
    }
    if (rotateHandler != null) {//发送消息
      rotateHandler.obtainMessage(10001, orientation, 0).sendToTarget();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  private boolean isSennor;
  private boolean isClickFullScreenButton;
  /** 点击屏幕切换按钮的时候 同时调用该方法 ： 中断Handler信息传递 */
  public void setIsSennor() {
    isSennor = false;
  }

  private Handler rotateHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 10001:
          if ((msg.arg1 > 45 && msg.arg1 <= 135) || (msg.arg1 > 225 && msg.arg1 <= 315)) {
            ((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
          } else {
            if (((Activity)mContext).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
              ((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
          }
          break;
        default:
          break;
      }
    }
  };

}
