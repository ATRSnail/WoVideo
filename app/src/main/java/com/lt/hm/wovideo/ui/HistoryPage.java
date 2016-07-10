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
public class HistoryPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.history_topbar)
    SecondTopbar historyTopbar;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        historyTopbar.setRightIsVisible(false);
        historyTopbar.setLeftIsVisible(true);
        historyTopbar.setOnTopbarClickListenter(this);
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
