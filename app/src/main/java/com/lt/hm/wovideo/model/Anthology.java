package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class Anthology {
    private int number;
    private String url;
    private boolean isVip;

    @Override
    public String toString() {
        return "Anthology{" +
                "number=" + number +
                ", url='" + url + '\'' +
                ", isVip=" + isVip +
                '}';
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
