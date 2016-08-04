package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/9
 */
public class LiveModles {
    public String  title;
    public List<LiveModel> sinatv;
    private List<LiveModel> localTV;
    private List<LiveModel> cctv;
    private List<LiveModel> otherTv;

    public List<LiveModel> getSinatv() {
        return sinatv;
    }

    public void setSinatv(List<LiveModel> sinatv) {
        this.sinatv = sinatv;
    }
    /**
     * 当前类别Item总数。Category也需要占用一个Item
     * @return
     */
    public int getItemCount() {
        return sinatv.size() + 1;
    }
    /**
     *  获取Item内容
     *
     * @param pPosition
     * @return
     */
    public Object getItem(int pPosition) {
        // Category排在第一位
        if (pPosition == 0) {
            return title;
        } else {
            return sinatv.get(pPosition - 1);
        }
    }

    public List<LiveModel> getLocalTV() {
        return localTV;
    }

    public void setLocalTV(List<LiveModel> localTV) {
        this.localTV = localTV;
    }

    public List<LiveModel> getCctv() {
        return cctv;
    }

    public void setCctv(List<LiveModel> cctv) {
        this.cctv = cctv;
    }

    public List<LiveModel> getOtherTv() {
        return otherTv;
    }

    public void setOtherTv(List<LiveModel> otherTv) {
        this.otherTv = otherTv;
    }

    @Override
    public String toString() {
        return "LiveModles{" +
                "title='" + title + '\'' +
                ", sinatv=" + sinatv +
                '}';
    }

    public class LiveModel{

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
}
