package com.lt.hm.wovideo;

import android.os.Bundle;

import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.UIHelper;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/26
 */
public class AppStart extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return super.getLayoutId();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        UIHelper.ToMain(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }
}
