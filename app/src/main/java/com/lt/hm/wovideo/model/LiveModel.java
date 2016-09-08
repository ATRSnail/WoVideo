package com.lt.hm.wovideo.model;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/7
 */
public class LiveModel {
    /**
     * id : 24
     * utime : null
     * goodsId : 额问问
     * tvName : 浙江卫视
     * isFree : 1
     * state : 1
     * img : /tv/da9aa4ab972247ac9c1dc00976ac8fb9.jpg
     * property : 1
     * nowPro : 广告
     * ctime : 1467871520000
     * url : ww.baidu.com
     */

    private int id;
    private Object utime;
    private String goodsId;
    private String tvName;
    private String isFree;
    private String state;
    private String img;
    private String property;
    private String nowPro;
    private long ctime;
    private String url;

    public LiveModel(String tvName) {
        this.tvName = tvName;
    }

    public LiveModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getUtime() {
        return utime;
    }

    public void setUtime(Object utime) {
        this.utime = utime;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getNowPro() {
        return nowPro;
    }

    public void setNowPro(String nowPro) {
        this.nowPro = nowPro;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LiveModel{" +
                "id=" + id +
                ", utime=" + utime +
                ", goodsId='" + goodsId + '\'' +
                ", tvName='" + tvName + '\'' +
                ", isFree='" + isFree + '\'' +
                ", state='" + state + '\'' +
                ", img='" + img + '\'' +
                ", property='" + property + '\'' +
                ", nowPro='" + nowPro + '\'' +
                ", ctime=" + ctime +
                ", url='" + url + '\'' +
                '}';
    }
}
