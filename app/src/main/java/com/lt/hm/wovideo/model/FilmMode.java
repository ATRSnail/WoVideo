package com.lt.hm.wovideo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class FilmMode extends MultiItemEntity {

    /**
     * utime : 1468809257000
     * tag : 0002
     * isfree : 1
     * hImg:/vfinfoimg/85ce914fa31a4e6d9f33a0a3b741f9ee.jpg
     * state : 1
     * img : /vfinfoimg/ca7b225d4863477e825b595f027baf58.jpg
     * director : 沈东
     * ctime : 1468809271000
     * gxzt : 1
     * jjs : 1
     * lx : 剧情
     * typeName : 电影
     * cpId : PludYUQGvpWNhENEmRU7LiY2pG4tKPlF
     * hit :
     * vfinfo_id : 0A7vSCFQH7I61ZyvgenfvdeHIuVDVXEf
     * stars : 戴娇倩，吴其江，徐箭
     * nd : 2015
     * name : 相伴库里申科
     * copyrightImg : /vfinfocopyrightimg/86043d3231284843b75b87abe6506791.jpg
     * dq : 内地
     * typeId : 1
     * cpname :
     * fname : xbklsk
     * introduction : 影片讲述了抗战时期12岁的谭小慧在万州江边亲眼目睹母亲被炸死，也亲眼目睹了库里申科的飞机坠入江中，光荣牺牲。万州群众将库里申科葬到太白岩。50年代末，库里申科的墓地迁到了西山公园，库里申科的女儿伊娜前来祭拜。谭小慧主动向老园长请缨，成为了守陵员，她尽职尽责顾不上照顾家庭和年幼的孩子魏中华，还劝说丈夫魏喜旺放弃了住新房的机会，并说服儿子魏中华大学毕业后留在陵园为库里申科守墓。很多年后，伊娜再次来到万州，谭小慧的孙子魏子杰找到伊娜，伊娜答应了他以库里申科命名餐厅的请求，并前往陵园看望谭小慧，两人再次相见……
     * sx : 院线
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
    private String hit;
    private String vfinfo_id;
    private String stars;
    private String nd;
    private String name;
    private String copyrightImg;
    private String dq;
    private int typeId;
    private String cpname;
    private String fname;
    private String introduction;
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

    public String gethImg() {
        return hImg;
    }

    public void sethImg(String hImg) {
        this.hImg = hImg;
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

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
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

    @Override
    public String toString() {
        return "FilmMode{" +
                "utime=" + utime +
                ", tag='" + tag + '\'' +
                ", isfree='" + isfree + '\'' +
                ", state='" + state + '\'' +
                ", img='" + img + '\'' +
                ", director='" + director + '\'' +
                ", ctime=" + ctime +
                ", gxzt='" + gxzt + '\'' +
                ", jjs=" + jjs +
                ", lx='" + lx + '\'' +
                ", typeName='" + typeName + '\'' +
                ", cpId='" + cpId + '\'' +
                ", hit='" + hit + '\'' +
                ", vfinfo_id='" + vfinfo_id + '\'' +
                ", stars='" + stars + '\'' +
                ", nd='" + nd + '\'' +
                ", name='" + name + '\'' +
                ", copyrightImg='" + copyrightImg + '\'' +
                ", dq='" + dq + '\'' +
                ", typeId=" + typeId +
                ", cpname='" + cpname + '\'' +
                ", fname='" + fname + '\'' +
                ", introduction='" + introduction + '\'' +
                ", sx='" + sx + '\'' +
                '}';
    }
}
