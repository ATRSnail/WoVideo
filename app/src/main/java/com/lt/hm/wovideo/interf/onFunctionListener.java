package com.lt.hm.wovideo.interf;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 * 赞,投屏,分享,收藏
 */
public interface onFunctionListener {
    /**
     * 点赞
     */
    void onZanClick();

    /**
     * 投屏
     */
    void onTouClick();

    /**
     * 分享
     */
    void onShareClick();

    /**
     * 收藏
     */
    void onCollectClick();
}
