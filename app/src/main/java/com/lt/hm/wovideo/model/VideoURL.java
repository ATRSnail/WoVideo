package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class VideoURL {
    private String overstep;
    private String url;

    public String getOverstep() {
        return overstep;
    }

    public void setOverstep(String overstep) {
        this.overstep = overstep;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoURL{" +
                "overstep='" + overstep + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
