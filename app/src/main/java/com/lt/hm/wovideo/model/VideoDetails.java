package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/14
 */
public class VideoDetails {

    private VfinfoBean vfinfo;

    public VfinfoBean getVfinfo() {
        return vfinfo;
    }

    public void setVfinfo(VfinfoBean vfinfo) {
        this.vfinfo = vfinfo;
    }


    @Override
    public String toString() {
        return "VideoDetails{" +
                "vfinfo=" + vfinfo +
                '}';
    }

    public static class VfinfoBean {
        private long utime;
        private String isfree; //是否免费 1免费0收费
        private String state;
        private String img;//封面图片
        private String sc;//用户是否收藏 1收藏0未收藏
        private String director;//导演
        private long ctime;
        private String gxzt;//更新状态  0更新1完结
        private int jjs;//总集数
        private String id;//视频ID
        private String hit; //点击量
        private String stars; //演员
        private String name;//视频名称
        private int typeId;//所属分类
        private String introduction;//简介
        private String lx;//影片二级分类
        private String nd;//年代
        private String dq;//地区
        private String sx;//属性
        private String cpId;//影片来源ID
        private String cpname;//影片来源


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

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHit() {
            return hit;
        }

        public void setHit(String hit) {
            this.hit = hit;
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

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getLx() {
            return lx;
        }

        public void setLx(String lx) {
            this.lx = lx;
        }

        public String getNd() {
            return nd;
        }

        public void setNd(String nd) {
            this.nd = nd;
        }

        public String getDq() {
            return dq;
        }

        public void setDq(String dq) {
            this.dq = dq;
        }

        public String getSx() {
            return sx;
        }

        public void setSx(String sx) {
            this.sx = sx;
        }

        public String getCpId() {
            return cpId;
        }

        public void setCpId(String cpId) {
            this.cpId = cpId;
        }

        public String getCpname() {
            return cpname;
        }

        public void setCpname(String cpname) {
            this.cpname = cpname;
        }

        @Override
        public String toString() {
            return "VfinfoBean{" +
                    "utime=" + utime +
                    ", isfree='" + isfree + '\'' +
                    ", state='" + state + '\'' +
                    ", img='" + img + '\'' +
                    ", sc='" + sc + '\'' +
                    ", director='" + director + '\'' +
                    ", ctime=" + ctime +
                    ", gxzt='" + gxzt + '\'' +
                    ", jjs=" + jjs +
                    ", id='" + id + '\'' +
                    ", hit='" + hit + '\'' +
                    ", stars='" + stars + '\'' +
                    ", name='" + name + '\'' +
                    ", typeId=" + typeId +
                    ", introduction='" + introduction + '\'' +
                    ", lx='" + lx + '\'' +
                    ", nd='" + nd + '\'' +
                    ", dq='" + dq + '\'' +
                    ", sx='" + sx + '\'' +
                    ", cpId='" + cpId + '\'' +
                    ", cpname='" + cpname + '\'' +
                    '}';
        }
    }
}
