package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class AboutPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.about_topbar)
    SecondTopbar aboutTopbar;
    @BindView(R.id.version_code)
    TextView versionCode;
    @BindView(R.id.check_version)
    RelativeLayout checkVersion;
    @BindView(R.id.about_phone)
    RelativeLayout aboutPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_about;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        aboutTopbar.setLeftIsVisible(true);
        aboutTopbar.setRightIsVisible(false);
        aboutTopbar.setOnTopbarClickListenter(this);
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
