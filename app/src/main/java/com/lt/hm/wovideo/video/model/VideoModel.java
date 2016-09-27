package com.lt.hm.wovideo.video.model;

import java.util.ArrayList;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class VideoModel {
    private String str_url;
    private String mVideoName;//视频名称 如房源视频、小区视频
    private VideoUrl mPlayUrl;//当前正在播放的地址。 外界不用传

    public String getStr_url() {
        return str_url;
    }

    public void setStr_url(String str_url) {
        this.str_url = str_url;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public VideoUrl getmPlayUrl() {
        return mPlayUrl;
    }

    public void setmPlayUrl(VideoUrl mPlayUrl) {
        this.mPlayUrl = mPlayUrl;
    }

    public boolean equal(VideoModel video){
        if(null != video){
            return mVideoName.equals(video.getmVideoName());
        }
        return false;
    }
}
