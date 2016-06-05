package com.lt.hm.wovideo.ui;

import android.os.Bundle;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.malmstein.fenster.controller.MediaFensterPlayerController;
import com.malmstein.fenster.view.FensterVideoView;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class VideoPage_03 extends BaseActivity {
    @BindView(R.id.play_video_texture)
    FensterVideoView playVideoTexture;
    @BindView(R.id.play_video_controller)
    MediaFensterPlayerController playVideoController;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_video_page2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        playVideoTexture.setMediaController(playVideoController);
        playVideoTexture.setVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                playVideoController.DEFAULT_VIDEO_START);
//
//        playVideoTexture.setVideo("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8",
//                playVideoController.DEFAULT_VIDEO_START);
        playVideoTexture.start();
    }
}
