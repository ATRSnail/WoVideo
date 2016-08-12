package com.lt.hm.wovideo.model;

import java.io.Serializable;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class ChannelModel implements Serializable{

    /**
     * id : 136
     * funName : 电视剧
     * funCode : 0064
     * seq : 3
     * type : 0
     * url : #
     */

    private int id;
    private String funName;
    private String funCode;
    private int seq;
    private String type;
    private String url;

    public ChannelModel(String funName, String type) {
        this.funName = funName;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
