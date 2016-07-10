package com.lt.hm.wovideo.ui;

import android.os.Bundle;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class CollectPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.collect_topbar)
    SecondTopbar collectTopbar;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_collect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        collectTopbar.setLeftIsVisible(true);
        collectTopbar.setRightIsVisible(false);
        collectTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }
}
