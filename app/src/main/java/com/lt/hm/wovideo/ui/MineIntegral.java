package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.integral.IntegralAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.Integral;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class MineIntegral extends BaseActivity {
    @BindView(R.id.integral_back)
    ImageView integralBack;
    @BindView(R.id.mine_integral)
    TextView mineIntegral;
    @BindView(R.id.integral_rules)
    Button integralRules;
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
        list= new ArrayList<>();
        integralAdapter= new IntegralAdapter(this,R.layout.layout_integral_item,list);
        integralList.setLayoutManager(new LinearLayoutManager(this));
        integralList.setAdapter(integralAdapter);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }
}
