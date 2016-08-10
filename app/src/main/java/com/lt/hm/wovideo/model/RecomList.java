package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class RecomList {
    private List<Videos> recList;

    public List<Videos> getRecList() {
        return recList;
    }

    public void setRecList(List<Videos> recList) {
        this.recList = recList;
    }

    public static class Videos {
        private int id;//主键
        private String vfId;//视频主信息ID
        private String img;//封面图片路径（竖）
        private String type;//0 热门推荐 ／1 最新推荐
        private String name; //视频名称
        private String des;//视频描述
        private String typeId;//视频类型 1.电影2.电视剧3.综艺4.体育
        private String himg;//封面图片路径（横）
        private String tag;//横屏显示和竖屏显示 tag 设定

        public Videos() {
        }

        public Videos(String vfId, int id, String img, String type, String name, String des, String typeId, String himg, String tag) {
            this.vfId = vfId;
            this.id = id;
            this.img = img;
            this.type = type;
            this.name = name;
            this.des = des;
            this.typeId = typeId;
            this.himg = himg;
            this.tag = tag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVfId() {
            return vfId;
        }

        public void setVfId(String vfId) {
            this.vfId = vfId;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return des;
        }

        public void setDesc(String desc) {
            this.des = desc;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getHimg() {
            return himg;
        }

        public void setHimg(String himg) {
            this.himg = himg;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "Videos{" +
                    "id=" + id +
                    ", vfId='" + vfId + '\'' +
                    ", img='" + img + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", des='" + des + '\'' +
                    ", typeId='" + typeId + '\'' +
                    ", himg='" + himg + '\'' +
                    ", tag='" + tag + '\'' +
                    '}';
        }
    }
}
