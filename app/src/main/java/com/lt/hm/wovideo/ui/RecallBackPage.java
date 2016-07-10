package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.widget.EditText;
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
public class RecallBackPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.callback_topbar)
    SecondTopbar callbackTopbar;
    @BindView(R.id.callback_content)
    EditText callbackContent;
    @BindView(R.id.content_count)
    TextView contentCount;
    @BindView(R.id.contact_mathods)
    EditText contactMathods;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recallback;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        callbackTopbar.setRightIsVisible(false);
        callbackTopbar.setLeftIsVisible(true);
        callbackTopbar.setOnTopbarClickListenter(this);
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
        // TODO: 16/6/12 提交触发
    }
}
