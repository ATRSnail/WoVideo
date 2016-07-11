package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/14
 */
public enum  VideoType {
    MOVIE(1),//电影
    TELEPLAY(2),//电视剧
    VARIATY(3),//综艺
    SPORTS(4),//体育
    LIVE(5), ;//直播
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
