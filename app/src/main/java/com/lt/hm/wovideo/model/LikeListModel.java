package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class LikeListModel {
    private String seed;
    private List<LikeModel> likeList;

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public List<LikeModel> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeModel> likeList) {
        this.likeList = likeList;
    }

    @Override
    public String toString() {
        return "LikeListModel{" +
                "seed='" + seed + '\'' +
                ", likeList=" + likeList +
                '}';
    }
}
