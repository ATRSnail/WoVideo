package com.lt.hm.wovideo.model;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 */
public class PlaysListBean {
    private String id;//主键
    private long utime;
    private String vfId;//视频主信息ID
    private int index;//集数下标
    private int hitCount;//点击量
    private int zan;//点赞量
    private String freePlayUrl;//
    private String isfree;//是否免费 1 免费 0 收费
    private String img;//封面图片
    private long ctime;
    private String introduction;//简介

    private String standardUrl;//标清源地址
    private String superUrl;//超清源地址
    private String highUrl;//高清源地址
    private String blueUrl;//蓝光源地址
    private String fkUrl;//4K源地址
    private String fluentUrl;//流畅地址
    private String sc;//是否收藏 1是0否

    private boolean isSelect = false;//被选中

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFluentUrl() {
        return fluentUrl;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public void setFluentUrl(String fluentUrl) {
        this.fluentUrl = fluentUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getVfId() {
        return vfId;
    }

    public void setVfId(String vfId) {
        this.vfId = vfId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public String getFreePlayUrl() {
        return freePlayUrl;
    }

    public void setFreePlayUrl(String freePlayUrl) {
        this.freePlayUrl = freePlayUrl;
    }

    public String getIsfree() {
        return isfree;
    }

    public void setIsfree(String isfree) {
        this.isfree = isfree;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStandardUrl() {
        return standardUrl;
    }

    public void setStandardUrl(String standardUrl) {
        this.standardUrl = standardUrl;
    }

    public String getSuperUrl() {
        return superUrl;
    }

    public void setSuperUrl(String superUrl) {
        this.superUrl = superUrl;
    }

    public String getHighUrl() {
        return highUrl;
    }

    public void setHighUrl(String highUrl) {
        this.highUrl = highUrl;
    }

    public String getBlueUrl() {
        return blueUrl;
    }

    public void setBlueUrl(String blueUrl) {
        this.blueUrl = blueUrl;
    }

    public String getFkUrl() {
        return fkUrl;
    }

    public void setFkUrl(String fkUrl) {
        this.fkUrl = fkUrl;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    @Override
    public String toString() {
        return "PlaysListBean{" +
                "id='" + id + '\'' +
                ", utime=" + utime +
                ", vfId='" + vfId + '\'' +
                ", index=" + index +
                ", hitCount=" + hitCount +
                ", freePlayUrl='" + freePlayUrl + '\'' +
                ", isfree='" + isfree + '\'' +
                ", img='" + img + '\'' +
                ", ctime=" + ctime +
                ", introduction='" + introduction + '\'' +
                ", standardUrl='" + standardUrl + '\'' +
                ", superUrl='" + superUrl + '\'' +
                ", highUrl='" + highUrl + '\'' +
                ", blueUrl='" + blueUrl + '\'' +
                ", fkUrl='" + fkUrl + '\'' +
                ", fluentUrl='" + fluentUrl + '\'' +
                ", sc='" + sc + '\'' +
                '}';
    }
}
