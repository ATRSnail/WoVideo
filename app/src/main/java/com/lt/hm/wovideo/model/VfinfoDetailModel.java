package com.lt.hm.wovideo.model;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/29
 */
public class VfinfoDetailModel {
    private VfinfoModel vfinfo;

    public VfinfoModel getVfinfo() {
        return vfinfo;
    }

    public void setVfinfo(VfinfoModel vfinfo) {
        this.vfinfo = vfinfo;
    }

    @Override
    public String toString() {
        return "VfinfoDetailModel{" +
                "vfinfo=" + vfinfo +
                '}';
    }
}
