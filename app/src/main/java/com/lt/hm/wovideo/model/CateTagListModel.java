package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class CateTagListModel {

    private List<CateTagModel> nd;
    private List<CateTagModel> dq;
    private List<CateTagModel> sx;
    private List<CateTagModel> lx;

    public List<CateTagModel> getNd() {
        return nd;
    }

    public void setNd(List<CateTagModel> nd) {
        this.nd = nd;
    }

    public List<CateTagModel> getDq() {
        return dq;
    }

    public void setDq(List<CateTagModel> dq) {
        this.dq = dq;
    }

    public List<CateTagModel> getSx() {
        return sx;
    }

    public void setSx(List<CateTagModel> sx) {
        this.sx = sx;
    }

    public List<CateTagModel> getLx() {
        return lx;
    }

    public void setLx(List<CateTagModel> lx) {
        this.lx = lx;
    }

    @Override
    public String toString() {
        return "CateTagListModel{" +
                "nd=" + nd +
                ", dq=" + dq +
                ", sx=" + sx +
                ", lx=" + lx +
                '}';
    }
}
