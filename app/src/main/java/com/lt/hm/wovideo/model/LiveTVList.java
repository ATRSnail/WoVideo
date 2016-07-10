package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class LiveTVList {

    private List<LiveTvListBean> liveTvList;

    public List<LiveTvListBean> getLiveTvList() {
        return liveTvList;
    }

    public void setLiveTvList(List<LiveTvListBean> liveTvList) {
        this.liveTvList = liveTvList;
    }

    @Override
    public String toString() {
        return "LiveTVList{" +
                "liveTvList=" + liveTvList +
                '}';
    }

    public static class LiveTvListBean {
        private int id;
        private Object utime;
        private String goodsId;
        private String tvName;
        private String isFree;
        private String state;
        private String img;
        private long ctime;
        private String url;
        private String nowPro;//正在直播的节目名

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

        public String getNowPro() {
            return nowPro;
        }

        public void setNowPro(String nowPro) {
            this.nowPro = nowPro;
        }

        @Override
        public String toString() {
            return "LiveTvListBean{" +
                    "id=" + id +
                    ", utime=" + utime +
                    ", goodsId='" + goodsId + '\'' +
                    ", tvName='" + tvName + '\'' +
                    ", isFree='" + isFree + '\'' +
                    ", state='" + state + '\'' +
                    ", img='" + img + '\'' +
                    ", ctime=" + ctime +
                    ", url='" + url + '\'' +
                    ", nowPro='" + nowPro + '\'' +
                    '}';
        }
    }
}
