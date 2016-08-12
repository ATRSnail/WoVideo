package com.lt.hm.wovideo.model;

/**
 * 电影,电视剧标签
 * Created by xuchunhui on 16/8/12.
 */
public class CateTagModel {


    /**
     * id : 229
     * utime : null
     * name : 2015
     * seq : 2
     * parent : 153
     * code : 2
     * ctime : 1470707636000
     */

    private int id;
    private Object utime;
    private String name;
    private int seq;
    private int parent;
    private String code;
    private long ctime;

    public CateTagModel(String name, String code, int parent) {
        this.name = name;
        this.code = code;
        this.parent = parent;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        return "CateTagModel{" +
                "id=" + id +
                ", utime=" + utime +
                ", name='" + name + '\'' +
                ", seq=" + seq +
                ", parent=" + parent +
                ", code='" + code + '\'' +
                ", ctime=" + ctime +
                '}';
    }
}
