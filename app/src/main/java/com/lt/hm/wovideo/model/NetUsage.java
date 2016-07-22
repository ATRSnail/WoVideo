package com.lt.hm.wovideo.model;

/**
 * Created by KECB on 7/22/16.
 */
public class NetUsage {
    private String videoId;
    private String userId;
    private String createTime;
    private String bytes;

    public NetUsage() {
    }

    public NetUsage(String videoId, String userId) {
        this.videoId = videoId;
        this.userId = userId;
        this.createTime = createTime;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }
}