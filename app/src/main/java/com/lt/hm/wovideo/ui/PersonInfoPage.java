package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.widget.CircleImageView;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/9
 */
public class PersonInfoPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.person_info_topbar)
    SecondTopbar personInfoTopbar;
    @BindView(R.id.p_info_logo)
    CircleImageView pInfoLogo;
    @BindView(R.id.p_info_logo_layout)
    RelativeLayout pInfoLogoLayout;
    @BindView(R.id.nick_layout)
    RelativeLayout nickLayout;
    @BindView(R.id.sex_layout)
    RelativeLayout sexLayout;
    @BindView(R.id.email_layout)
    RelativeLayout emailLayout;
    @BindView(R.id.modify_pwd)
    RelativeLayout modifyPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_person_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        personInfoTopbar.setRightIsVisible(false);
        personInfoTopbar.setLeftIsVisible(true);
        personInfoTopbar.setOnTopbarClickListenter(this);
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
