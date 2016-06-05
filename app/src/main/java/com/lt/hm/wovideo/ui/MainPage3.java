package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.widget.FrameLayout;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class MainPage3 extends BaseActivity {

    @BindView(R.id.leftfragment)
    FrameLayout leftfragment;
    @BindView(R.id.rightfragment)
    FrameLayout rightfragment;
    @BindView(R.id.slidingpanellayout)
    SlidingPaneLayout slidingpanellayout;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_main3;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }
}
