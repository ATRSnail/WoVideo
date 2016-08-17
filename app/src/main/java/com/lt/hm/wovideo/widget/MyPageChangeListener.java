package com.lt.hm.wovideo.widget;

import android.support.v4.view.ViewPager;

import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.interf.onChangeLister;
import com.lt.hm.wovideo.utils.UpdateLocationMsg;
import com.lt.hm.wovideo.utils.ViewPageChangeMsg;

import java.util.ArrayList;

/**
 * Created by xuchunhui on 16/8/17.
 */
public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
    }

    private void enableDisableSwipeRefresh(boolean b) {

        if (ViewPageChangeMsg.getInstance().listers.size() > 0){
            removeBeforViews(b);
        }
    }

    private static void removeBeforViews(boolean isEnable) {
        ArrayList<onChangeLister> registerSucListeners = ViewPageChangeMsg.getInstance().listers;
        if (registerSucListeners == null || registerSucListeners.size() == 0) return;
        for (int i = 0; i < registerSucListeners.size(); i++) {
            registerSucListeners.get(i).onChangeLister(isEnable);
        }
    }
}
