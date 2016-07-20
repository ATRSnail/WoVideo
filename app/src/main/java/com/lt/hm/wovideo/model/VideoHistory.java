package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/19
 */
public class VideoHistory {
    private String uId;
    private String mId;//视频ID
    private String mName;//视频 名称
    private String img_url;//视频 缩略图地址
    private String create_time;//创建时间
    private Long current_positon;//当前播放时间
    private String flag;//批量删除选中状态

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Long getCurrent_positon() {
        return current_positon;
    }

    public void setCurrent_positon(Long current_positon) {
        this.current_positon = current_positon;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "VideoHistory{" +
                "uId='" + uId + '\'' +
                ", mId=" + mId +
                ", mName='" + mName + '\'' +
                ", img_url='" + img_url + '\'' +
                ", create_time='" + create_time + '\'' +
                ", current_positon='" + current_positon + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
