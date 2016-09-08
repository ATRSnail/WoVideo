package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class BannerList {
    private List<Banner> bannerList;

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public String toString() {
        return "BannerList{" +
                "bannerList=" + bannerList +
                '}';
    }

    public class Banner{

        /**
         * typeName : 电影
         * id : 13
         * utime : 1465904126000
         * cb :
         * hit : null
         * img : /banner/0bce60b57d824908968d88d078783a85.jpg
         * vfType : 1
         * type : 0
         * ctime : 1465724716000
         * url :
         * outid : 3
         * isvip : 0
         */

        private String typeName;
        private int id;
        private long utime;
        private String cb;
        private String hit;
        private String img;
        private String vfType;
        private String type;
        private long ctime;
        private String url;
        private String outid;
        private String isvip;
        private String vfName;

        public String getVfName() {
            return vfName;
        }

        public void setVfName(String vfName) {
            this.vfName = vfName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getUtime() {
            return utime;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }

        public String getCb() {
            return cb;
        }

        public void setCb(String cb) {
            this.cb = cb;
        }

        public String getHit() {
            return hit;
        }

        public void setHit(String hit) {
            this.hit = hit;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getVfType() {
            return vfType;
        }

        public void setVfType(String vfType) {
            this.vfType = vfType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getOutid() {
            return outid;
        }

        public void setOutid(String outid) {
            this.outid = outid;
        }

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }
    }
}
