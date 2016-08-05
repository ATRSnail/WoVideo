package com.lt.hm.wovideo.video.player;

import android.graphics.Color;

import com.lt.hm.wovideo.model.BulletModel;
import com.lt.hm.wovideo.utils.StringUtils;

import java.util.List;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

/**
 * Created by KECB on 7/19/16.
 */

public class WoDanmakuParser extends BaseDanmakuParser{

    private BulletModel mDanmuListData;

    public void setmDanmuListData(BulletModel mDanmuListData) {
        this.mDanmuListData = mDanmuListData;
    }

    @Override
    protected Danmakus parse() {
        if (mDanmuListData != null && mDanmuListData.getBarrageList().size() > 0) {
            return doParse(mDanmuListData);
        }
        return new Danmakus();
    }

    /**
     * @param danmakuListData 弹幕数据
     *                        传入的数组内包含普通弹幕，会员弹幕，锁定弹幕。
     * @return 转换后的Danmakus
     */
    private Danmakus doParse(BulletModel danmakuListData) {
        Danmakus danmakus = new Danmakus();
        if (danmakuListData == null || danmakuListData.getBarrageList() == null || danmakuListData.getBarrageList().size() == 0) {
            return danmakus;
        }
        for (int i = 0; i < danmakuListData.getBarrageList().size(); i++) {
            List<BulletModel.BarrageListBean> danmakuArray = danmakuListData.getBarrageList();
            if (danmakuArray != null) {
                danmakus = _parse(danmakuArray, danmakus);
            }
        }
        return danmakus;
    }

    private Danmakus _parse(List<BulletModel.BarrageListBean> barrageListBeen, Danmakus danmakus) {
        if (danmakus == null) {
            danmakus = new Danmakus();
        }
        if (barrageListBeen == null || barrageListBeen.size() == 0) {
            return danmakus;
        }
        for (BulletModel.BarrageListBean bullet : barrageListBeen) {

            int type = BaseDanmaku.TYPE_SCROLL_RL; // 弹幕类型
            if (type == 7)
                // FIXME : hard code
                // TODO : parse advance danmaku json
                continue;
            long time = bullet.getTime() * 1000; // 出现时间
            /**
             * {@link Color.BLACK} {@link Color#parseColor(String)}
             */
            bullet.setFontColor("#669900");
            int color = bullet.getFontColor().equals("") ? Color.BLACK : Color.parseColor(bullet.getFontColor()) ; // 颜色
            float textSize = 0;
            if (!StringUtils.isNullOrEmpty(bullet.getFontSize())){
                textSize= Float.parseFloat(bullet.getFontSize()); // 字体大小
            }else{
                textSize=30f;
            }
            BaseDanmaku item = mContext.mDanmakuFactory.createDanmaku(type, mContext);
            if (item != null) {
                item.time = time;
                item.textSize = textSize * (mDispDensity - 0.6f);
                item.textColor = color;
                item.textShadowColor = color <= Color.BLACK ? Color.WHITE : Color.BLACK;
                DanmakuUtils.fillText(item, bullet.getContext());
                item.setTimer(mTimer);
                danmakus.addItem(item);
            }
        }
        return danmakus;
    }
}
