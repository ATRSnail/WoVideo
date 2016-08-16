package com.lt.hm.wovideo.model;

/**
 * Created by xuchunhui on 16/8/16.
 */
public class TagModel {

    /**
     * id : 77
     * utime : 1470130131000
     * name : 搞笑
     * seq : 2
     * parent : 1
     * code : 0002
     * ctime : 1470130131000
     */

    private int id;
    private long utime;
    private String name;
    private int seq;
    private int parent;
    private String code;
    private long ctime;

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
}
