package com.lt.hm.wovideo.model;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class LocalCityModel {
    private String img;
    private String isFree;
    private String city;
    private long utime;
    private String goodsId;
    private String property;
    private String tvName;
    private long ctime;
    private int id;
    private String state;
    private String nowPro;
    private String url;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNowPro() {
        return nowPro;
    }

    public void setNowPro(String nowPro) {
        this.nowPro = nowPro;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LocalCityModel{" +
                "img='" + img + '\'' +
                ", isFree='" + isFree + '\'' +
                ", city='" + city + '\'' +
                ", utime=" + utime +
                ", goodsId='" + goodsId + '\'' +
                ", property='" + property + '\'' +
                ", tvName='" + tvName + '\'' +
                ", ctime=" + ctime +
                ", id=" + id +
                ", state='" + state + '\'' +
                ", nowPro='" + nowPro + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
