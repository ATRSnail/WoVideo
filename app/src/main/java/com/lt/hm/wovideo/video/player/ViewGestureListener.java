package com.lt.hm.wovideo.video.player;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by KECB on 7/10/16.
 */

public class ViewGestureListener implements GestureDetector.OnGestureListener {
  private static final String TAG = "ViewGestureListener";

  private static final int SWIPE_THRESHOLD = 60; // threshold of swipe
  public static final int SWIPE_LEFT = 1;
  public static final int SWIPE_RIGHT = 2;

  private AVPlayerGestureListener mListener;
  private Context mContext;

  public ViewGestureListener(Context context, AVPlayerGestureListener listener) {
    mListener = listener;
    mContext = context;
  }

  @Override public boolean onSingleTapUp(MotionEvent e) {
    mListener.onSingleTap();
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    //获取坐标点
    float mOldX = e1.getX(), mOldY = e1.getY();
    float deltaX = e2.getX() - e1.getX();
    float deltaY = e1.getY() - e2.getRawY();

    //窗口管理器
    Display disp = ((Activity)mContext).getWindowManager().getDefaultDisplay();
    //获得屏幕宽高
    int windowWidth = disp.getWidth();
    int windowHeight = disp.getHeight();

    if (Math.abs(deltaX) > Math.abs(deltaY)) {
      if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
        mListener.onHorizontalScroll(e2, deltaX);
      }
      return true;
    } else {
      if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
        if (mOldX > windowWidth * 4.0 / 5) {//left edge
          Log.e("deltaY", "" + deltaY);
          mListener.onVerticalScroll(e2, deltaY/windowHeight, SWIPE_LEFT);
        } else if (mOldX < windowWidth / 5) {//right edge
          Log.e("deltaY", "" + deltaY);
          mListener.onVerticalScroll(e2, deltaY/windowHeight, SWIPE_RIGHT);
        }
        return true;
      }
    }
    return false;

  }

  @Override public boolean onDown(MotionEvent e) {
    return false;
  }

  @Override public void onShowPress(MotionEvent e) {

  }

  @Override public void onLongPress(MotionEvent e) {

  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    return false;
  }
}
