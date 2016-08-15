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
        private String id;
        private String utime;
        private String img;
        private String type;//banner类型 0视频1广告
        private String ctime;
        private String outid;//跳转 视频播放页，使用 该ID outid
        private String isvip;
        private String vfName;
        private  String hit;
        private String typeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
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

        public String getVfName() {
            return vfName;
        }

        public void setVfName(String vfName) {
            this.vfName = vfName;
        }

        public String getHit() {
            return hit;
        }

        public void setHit(String hit) {
            this.hit = hit;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }
}
