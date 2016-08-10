package com.lt.hm.wovideo.model;

/**
 * Created by xuchunhui on 16/8/9.
 */
public class CateModel {

    private String name;
    private String id;

    public CateModel(String id, String name) {
        this.id = id;
        this.name = name;
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
}
