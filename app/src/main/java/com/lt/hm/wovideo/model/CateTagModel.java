package com.lt.hm.wovideo.model;

import java.io.Serializable;

/**
 * 电影,电视剧标签
 * Created by xuchunhui on 16/8/12.
 */
public class CateTagModel implements Serializable{


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

    public CateTagModel(String name,String code) {
        this.name = name;
        this.code = code;
    }

    public CateTagModel(String name, int id) {
        this.name = name;
        this.id = id;
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
