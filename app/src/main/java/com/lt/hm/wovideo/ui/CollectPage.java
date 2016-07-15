package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.CollectModel;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class CollectPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.collect_topbar)
    SecondTopbar collectTopbar;
    @BindView(R.id.collect_list)
    RecyclerView collectList;
    @BindView(R.id.collect_refresh)
    SwipeRefreshLayout collectRefresh;
    List<CollectModel> mList;
    int pageNum=1;
    int pageSize=10;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_collect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        collectTopbar.setLeftIsVisible(true);
        collectTopbar.setRightIsVisible(false);
        collectRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    public void initViews() {
        collectTopbar.setOnTopbarClickListenter(this);
        collectRefresh.setOnRefreshListener(this);
    }

    @Override
    public void initDatas() {
        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(userinfo)){
            
        }
        getCollectList();
    }

    private void getCollectList() {

    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              collectRefresh.setRefreshing(false);
            }
        },3000);
    }
}
