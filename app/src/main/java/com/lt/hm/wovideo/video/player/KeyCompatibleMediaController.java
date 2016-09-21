package com.lt.hm.wovideo.video.player;

import android.content.Context;
import android.view.KeyEvent;

import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;

import master.flame.danmaku.controller.IDanmakuView;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/19
 */
public class KeyCompatibleMediaController extends AVController {

    private MediaPlayerControl playerControl;
    private IDanmakuView mDanmakuView;

    public KeyCompatibleMediaController(Context context, IDanmakuView danmakuView,ScreenSwitchUtils screenSwitchUtils) {
        super(context,screenSwitchUtils);
        mDanmakuView = danmakuView;
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl playerControl) {
        super.setMediaPlayer(playerControl);
        this.playerControl = playerControl;
    }

    @Override
    public void toggleBulletScreen(boolean isShow) {
        super.toggleBulletScreen(isShow);
        if (isShow) {
            TLog.log("toggleBullet_status" + isShow);
            if (playerControl == null) {
                mDanmakuView.hide();
                return;
            }
            mDanmakuView.seekTo(playerControl.getCurrentPosition());
            mDanmakuView.show();
        } else {
            TLog.log("toggleBullet_status" + isShow);
            mDanmakuView.hide();
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void setTitle(String titleName) {
        super.setTitle(titleName);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        TLog.error("keyCode---->"+keyCode);
        if (playerControl.canSeekForward() && (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                playerControl.seekTo(playerControl.getCurrentPosition() + 15000); // milliseconds
                show();
            }
            return true;
        } else if (playerControl.canSeekBackward() && (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                playerControl.seekTo(playerControl.getCurrentPosition() - 5000); // milliseconds
                show();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
