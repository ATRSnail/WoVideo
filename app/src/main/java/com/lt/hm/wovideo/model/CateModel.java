package com.lt.hm.wovideo.model;

/**
 * Created by xuchunhui on 16/8/9.
 */
public class CateModel {

    private String name;
    private String id;
    private int tag;

    public CateModel(String id, String name, int tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
