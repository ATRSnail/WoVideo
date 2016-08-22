package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class SearchResult {


    private List<VfListBean> vfList;

    public List<VfListBean> getVfList() {
        return vfList;
    }

    public void setVfList(List<VfListBean> vfList) {
        this.vfList = vfList;
    }

    public static class VfListBean {


        /**
         * utime : 1471847519000
         * tag : 0003|0015|0010
         * isfree : 0
         * hImg : /himg/748ace4839e34ed19ea0ddaa34cbf30c.jpg
         * state : 1
         * img : /vimg/2214983125d44fb9a0c71bc0bc238770.jpg
         * director : 无
         * ctime : 1471585380000
         * gxzt : 1
         * jjs : 1
         * lx :
         * typeName : 短剧
         * cpId : rxTEP1PcxJPYaNoVieDalwztSlyI3yl2
         * hit : 4
         * vfinfo_id : udYQWqlJNmxvbxdxQBclSYtVK1gySndv
         * stars : 尤莉亚
         * nd :
         * name : 台湾ShowGirl萌萌哒电翻全场
         * copyrightImg : /copyrightimg/7ea39ae6398d4c219cba121aa5989880.jpg
         * dq :
         * typeId : 5
         * cpname :
         * introduction : CJ2016ShowGirl眼镜萌妹尤莉亚，肤白貌美，电眼萌萌哒。
         * fname : cjmmd
         * sx :
         */

        private long utime;
        private String tag;
        private String isfree;
        private String hImg;
        private String state;
        private String img;
        private String director;
        private long ctime;
        private String gxzt;
        private int jjs;
        private String lx;
        private String typeName;
        private String cpId;
        private int hit;
        private String vfinfo_id;
        private String stars;
        private String nd;
        private String name;
        private String copyrightImg;
        private String dq;
        private int typeId;
        private String cpname;
        private String introduction;
        private String fname;
        private String sx;

        public long getUtime() {
            return utime;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getHImg() {
            return hImg;
        }

        public void setHImg(String hImg) {
            this.hImg = hImg;
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

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public String getGxzt() {
            return gxzt;
        }

        public void setGxzt(String gxzt) {
            this.gxzt = gxzt;
        }

        public int getJjs() {
            return jjs;
        }

        public void setJjs(int jjs) {
            this.jjs = jjs;
        }

        public String getLx() {
            return lx;
        }

        public void setLx(String lx) {
            this.lx = lx;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getCpId() {
            return cpId;
        }

        public void setCpId(String cpId) {
            this.cpId = cpId;
        }

        public int getHit() {
            return hit;
        }

        public void setHit(int hit) {
            this.hit = hit;
        }

        public String getVfinfo_id() {
            return vfinfo_id;
        }

        public void setVfinfo_id(String vfinfo_id) {
            this.vfinfo_id = vfinfo_id;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public String getNd() {
            return nd;
        }

        public void setNd(String nd) {
            this.nd = nd;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCopyrightImg() {
            return copyrightImg;
        }

        public void setCopyrightImg(String copyrightImg) {
            this.copyrightImg = copyrightImg;
        }

        public String getDq() {
            return dq;
        }

        public void setDq(String dq) {
            this.dq = dq;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getCpname() {
            return cpname;
        }

        public void setCpname(String cpname) {
            this.cpname = cpname;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getSx() {
            return sx;
        }

        public void setSx(String sx) {
            this.sx = sx;
        }
    }
}
