package com.lt.hm.wovideo.ui;

import android.net.Uri;
import android.os.Bundle;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/31
 */
public class VideoPage extends BaseActivity {

    @BindView(R.id.video_view)
    VideoView videoView;
    MediaController controller;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_video;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!LibsChecker.checkVitamioLibs(this))
            return;

        controller = new MediaController(this);
        videoView.setVideoURI(Uri.parse("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"));
        videoView.setMediaController(controller);
        videoView.start();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
