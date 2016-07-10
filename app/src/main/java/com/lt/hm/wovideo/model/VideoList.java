package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class VideoList {


    private List<TypeListBean> typeList;

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    public static class TypeListBean {
        private String vfinfo_id;////视频主信息主键
        private long utime;
        private String stars;//主演
        private String name;
        private String state;
        private String img;//封面图片（竖）
        private String hIMG;//封面图片（横）
        private String director;//导演
        private int typeId;//类型ID
        private String gxzt;//更新状态 0更新1完结
        private long ctime;
        private int jjs;//集数
        private String isfree;//是否免费 1免费 0收费 2部分免费
        private String introduction;// 简介
        private String desc;
        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String gethIMG() {
            return hIMG;
        }

        public void sethIMG(String hIMG) {
            this.hIMG = hIMG;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getVfinfo_id() {
            return vfinfo_id;
        }

        public void setVfinfo_id(String vfinfo_id) {
            this.vfinfo_id = vfinfo_id;
        }

        public long getUtime() {
            return utime;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getGxzt() {
            return gxzt;
        }

        public void setGxzt(String gxzt) {
            this.gxzt = gxzt;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getJjs() {
            return jjs;
        }

        public void setJjs(int jjs) {
            this.jjs = jjs;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        @Override
        public String toString() {
            return "TypeListBean{" +
                    "vfinfo_id='" + vfinfo_id + '\'' +
                    ", utime=" + utime +
                    ", stars='" + stars + '\'' +
                    ", name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    ", img='" + img + '\'' +
                    ", hIMG='" + hIMG + '\'' +
                    ", director='" + director + '\'' +
                    ", typeId=" + typeId +
                    ", gxzt='" + gxzt + '\'' +
                    ", ctime=" + ctime +
                    ", jjs=" + jjs +
                    ", isfree='" + isfree + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", desc='" + desc + '\'' +
                    ", tag='" + tag + '\'' +
                    '}';
        }
    }
}
