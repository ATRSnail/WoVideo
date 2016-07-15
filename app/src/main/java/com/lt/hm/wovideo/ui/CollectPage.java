package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.CollectModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

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
    int pageNum = 1;
    int pageSize = 10;


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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        collectList.setLayoutManager(manager);

    }

    @Override
    public void initViews() {
        collectTopbar.setOnTopbarClickListenter(this);
        collectRefresh.setOnRefreshListener(this);
    }

    @Override
    public void initDatas() {

        getCollectList();
    }

    private void getCollectList() {
        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
        UserModel model = new Gson().fromJson(userinfo, UserModel.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", model.getId());
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        HttpApis.collectList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<CollectModel, RespHeader> resp = new ResponseObj<CollectModel, RespHeader>();
                ResponseParser.parse(resp, response, CollectModel.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    List<CollectModel.CollListBean> models = resp.getBody().getCollList();

                    initRecyclerView();
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    private void initRecyclerView() {

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
        }, 3000);
    }
}
