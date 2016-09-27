package com.lt.hm.wovideo.interf;

import android.view.View;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 */
public interface OnMediaOtherListener {

    void onChooseChannel(View v);

    void onChooseMore(View v);

    void onShowQuality(View v);

    void onQualitySelect(String key, String value);

    /**
     * 电视剧选集
     * @param position
     */
    void onAnthologyItemClick(int position);
}
