package com.lt.hm.wovideo.video.player;

import android.graphics.Color;
import android.util.Log;

import com.lt.hm.wovideo.model.BulletModel;
import com.lt.hm.wovideo.video.model.Bullet;

import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/22
 */
public class DanmakuControl implements DrawHandler.Callback {
    private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
    private WoDanmakuParser mParser;

    public DanmakuControl(IDanmakuView mDanmakuView) {
        this.mDanmakuView = mDanmakuView;

        initData();
    }

    private void initData() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), new DanmakuCacheProxy(mDanmakuView)) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        if (mDanmakuView != null) {
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
            mDanmakuView.setCallback(this);
        }
    }

    /**
     * 弹幕准备
     */
    public void prepareDanmaku() {
        if (mContext != null && mDanmakuView != null) {
            mDanmakuView.prepare(mParser, mContext);
        }
    }

    /**
     * 显示弹幕
     */
    public void showDanmaku() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
        }
    }

    /**
     * 隐藏弹幕
     */
    public void hideDanmaku() {
        if (mDanmakuView != null)
            mDanmakuView.hide();
    }

    /**
     * 重启弹幕
     */
    public void resumeDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    /**
     * 暂停弹幕
     */
    public void pauseDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * 释放弹幕
     */
    public void releaseDanmaku() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            Log.d("Bullet", "onStop: danmu prepared and gonna release");
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    /**
     * 前进弹幕
     *
     * @param position 最终时间文字
     */
    public void seekToDanmaku(long position) {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.seekTo(position);
        }
    }

    public void setDanmuListData(BulletModel bulletModel) {
        mParser = new WoDanmakuParser();
        mParser.setmDanmuListData(bulletModel);

    }

    public void addDanmaku(Bullet bullet) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, mContext);
        if (danmaku == null || mDanmakuView == null || mParser == null) {
            return;
        }

        danmaku.text = bullet.getContent();
        danmaku.padding = 5;
        danmaku.isLive = false; //是否是直播弹幕
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 22f * (mParser.getDisplayer().getDensity() - 0.6f);
//            danMaKu.textColor = Color.parseColor(bullet.getFontColor());
        danmaku.textShadowColor = Color.WHITE;
        mDanmakuView.addDanmaku(danmaku);

    }

    @Override
    public void prepared() {
        mDanmakuView.start();
    }

    @Override
    public void updateTimer(DanmakuTimer timer) {

    }

    @Override
    public void danmakuShown(BaseDanmaku danmaku) {

    }

    @Override
    public void drawingFinished() {

    }
}
