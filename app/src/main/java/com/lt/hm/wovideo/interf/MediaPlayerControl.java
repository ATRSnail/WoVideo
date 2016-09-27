package com.lt.hm.wovideo.interf;

import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 */
public interface MediaPlayerControl {

    void start();

    void pause();

    void releasePlay();

    int getDuration();

    long getCurrentPosition();

    void seekTo(long pos);

    boolean isPlaying();

    int getBufferPercentage();

    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();

    /**
     * Get the audio seesion id for the player used by this VideoView. This can be used to
     * apply audio effects to the audio track of a video.
     *
     * @return The audio session, or 0 if there was an error.
     */
    int getAudioSessionId();

    void toggleFullScreen(ScreenSwitchUtils screenSwitchUtils);
}
