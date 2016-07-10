package com.lt.hm.wovideo.video.player;

import android.view.MotionEvent;

/**
 * Created by KECB on 7/10/16.
 */

public interface AVPlayerGestureListener {
  /**
   * single tap controller view
   */
  void onSingleTap();

  /**
   * Horizontal scroll to control progress of video
   * @param event
   * @param delta
   */
  void onHorizontalScroll(MotionEvent event, float delta);

  /**
   * vertical scroll listen
   * @param motionEvent
   * @param delta
   * @param direction  left or right edge for control brightness or volume
   */
  void onVerticalScroll(MotionEvent motionEvent, float delta, int direction);
}
