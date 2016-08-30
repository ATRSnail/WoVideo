package com.lt.hm.wovideo.model;

import java.io.Serializable;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/14
 */
public enum VideoType implements Serializable{
    MOVIE(1),//电影
    TELEPLAY(2),//电视剧
    VARIATY(3),//综艺
    SPORTS(4),//体育
    SMIML(5),//小视屏
    RECOMMEND(6),//推荐
    LOCAL(7),//地方
    LIVE(8);//直播
    private int id;

    VideoType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
