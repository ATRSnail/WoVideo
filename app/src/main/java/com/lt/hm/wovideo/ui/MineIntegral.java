package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.integral.IntegralAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.Integral;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class MineIntegral extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.integral_topbar)
    SecondTopbar integralTopbar;
    @BindView(R.id.mine_integral)
    TextView mineIntegral;
    @BindView(R.id.integral_rules)
    FrameLayout integralRules;
    @BindView(R.id.integral_list)
    RecyclerView integralList;

    IntegralAdapter integralAdapter;
    List<Integral> list;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_integral_mine;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Integral integral = new Integral();
            integral.setName("测试" + i);
            integral.setScores("+" + i + "分");
            list.add(integral);
        }
        integralAdapter = new IntegralAdapter(this, R.layout.layout_integral_item, list);
        integralList.setLayoutManager(new LinearLayoutManager(this));
        integralList.setAdapter(integralAdapter);
        integralAdapter.notifyDataSetChanged();

    }
    @Override
    public void initViews() {
        integralTopbar.setRightIsVisible(false);
        integralTopbar.setOnTopbarClickListenter(this);
        integralTopbar.setLeftIsVisible(true);
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
