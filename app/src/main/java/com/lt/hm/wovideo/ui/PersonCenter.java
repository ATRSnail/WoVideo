package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class PersonCenter extends BaseActivity {
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.login_tag)
    TextView loginTag;
    @BindView(R.id.regist_tag)
    TextView registTag;
    @BindView(R.id.person_head_bg)
    LinearLayout personHeadBg;
    @BindView(R.id.order_icon)
    ImageView orderIcon;
    @BindView(R.id.order)
    RelativeLayout order;
    @BindView(R.id.integral_icon)
    ImageView integralIcon;
    @BindView(R.id.integral)
    RelativeLayout integral;
    @BindView(R.id.history_icon)
    ImageView historyIcon;
    @BindView(R.id.history)
    RelativeLayout history;
    @BindView(R.id.cache_icon)
    ImageView cacheIcon;
    @BindView(R.id.cache)
    RelativeLayout cache;
    @BindView(R.id.collect_icon)
    ImageView collectIcon;
    @BindView(R.id.collect)
    RelativeLayout collect;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_person;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
