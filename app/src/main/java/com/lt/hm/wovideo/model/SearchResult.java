package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class SearchResult {

    /**
     * utime : 1464781808000
     * isfree : 1
     * state : 1
     * img : /recommendations/9f24b96bdca44e5da7586fba9384f3fe.jpg
     * director : 赵旭
     * ctime : 1464781805000
     * gxzt : 1
     * jjs : 1
     * lx : 4
     * hIMG : /recommendations/c7bde064ce7a47a6ac774a4d6934d6c6.jpg
     * id : 3
     * cpId : null
     * stars : 张无忌,李莫愁
     * nd : 4
     * name : 倚天屠龙记
     * copyrightImg :
     * dq : 3
     * typeId : 1
     * fname :
     * introduction : 这夜很离奇，自认废青的阿平来到谜之便利店第一晚上班，先遇上刻薄老板和神经质女同事，转头黑警来借厕所；刚出狱的愤怒老人即兴玩老笠，又有富豪和性感女伴乱入。众人各有故事，各怀鬼胎，一场大龙凤在便利店热闹上演。当炸弹狂徒来袭，最后殊途同归但又意想不到。笑他们太疯癫，只是这个城市病了我们看不穿，导演火火掌控节奏和精警对白非常准确，几位老戏骨交足功课，成就一部港片少见的黑色荒诞困兽斗好戏。
     * sx : 2
     */

    private List<VfListBean> vfList;

    public List<VfListBean> getVfList() {
        return vfList;
    }

    public void setVfList(List<VfListBean> vfList) {
        this.vfList = vfList;
    }

    public static class VfListBean {
        private long utime;
        private String isfree;
        private String state;
        private String img;
        private String director;
        private long ctime;
        private String gxzt;
        private int jjs;
        private String lx;
        private String hIMG;
        private String id;
        private String vfinfo_id;
        private Object cpId;
        private String stars;
        private String nd;
        private String name;
        private String copyrightImg;
        private String dq;
        private int typeId;
        private String fname;
        private String introduction;
        private String sx;

        public String gethIMG() {
            return hIMG;
        }

        public void sethIMG(String hIMG) {
            this.hIMG = hIMG;
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

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
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

        public String getHIMG() {
            return hIMG;
        }

        public void setHIMG(String hIMG) {
            this.hIMG = hIMG;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getCpId() {
            return cpId;
        }

        public void setCpId(Object cpId) {
            this.cpId = cpId;
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

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getSx() {
            return sx;
        }

        public void setSx(String sx) {
            this.sx = sx;
        }
    }
}
