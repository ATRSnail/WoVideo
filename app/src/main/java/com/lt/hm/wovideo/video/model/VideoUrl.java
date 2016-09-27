package com.lt.hm.wovideo.video.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class VideoUrl implements Parcelable {

    private String mFormatName;//视频格式名称，例如高清，标清，720P等等
    private String mFormatUrl;//视频Url
    private boolean isOnlineVideo = true;//是否在线视频 默认在线视频


    protected VideoUrl(Parcel in) {
        mFormatName = in.readString();
        mFormatUrl = in.readString();
        isOnlineVideo = in.readByte() != 0;
    }

    public static final Creator<VideoUrl> CREATOR = new Creator<VideoUrl>() {
        @Override
        public VideoUrl createFromParcel(Parcel in) {
            return new VideoUrl(in);
        }

        @Override
        public VideoUrl[] newArray(int size) {
            return new VideoUrl[size];
        }
    };

    public String getFormatName() {
        return mFormatName;
    }

    public void setFormatName(String formatName) {
        mFormatName = formatName;
    }

    public String getFormatUrl() {
        return mFormatUrl;
    }

    public void setFormatUrl(String formatUrl) {
        mFormatUrl = formatUrl;
    }

    public boolean isOnlineVideo() {
        return isOnlineVideo;
    }

    public void setIsOnlineVideo(boolean isOnlineVideo) {
        this.isOnlineVideo = isOnlineVideo;
    }

    public boolean equal(VideoUrl url) {
        if (null != url) {
            return getFormatName().equals(url.getFormatName()) && getFormatUrl().equals(url.getFormatUrl());
        }
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mFormatName);
        dest.writeString(mFormatUrl);
        dest.writeByte((byte) (isOnlineVideo ? 1 : 0));
    }

    public VideoUrl() {
    }


}
