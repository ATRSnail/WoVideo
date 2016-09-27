package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/14
 */
public class PlayList {


    private List<PlaysListBean> playsList;

    public List<PlaysListBean> getPlaysList() {
        return playsList;
    }

    public void setPlaysList(List<PlaysListBean> playsList) {
        this.playsList = playsList;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "playsList=" + playsList +
                '}';
    }
}
