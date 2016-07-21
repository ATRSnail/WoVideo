package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.history.CollectListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.CollectModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.CustomTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class CollectPage extends BaseActivity implements CustomTopbar.myTopbarClicklistenter, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.collect_topbar)
    CustomTopbar collectTopbar;
    @BindView(R.id.collect_list)
    ListView collectList;
    @BindView(R.id.collect_refresh)
    SwipeRefreshLayout collectRefresh;
    //    List<CollectModel> mList;
    List<CollectModel.CollListBean> mList;
    int pageNum = 1;
    int pageSize = 100;
    @BindView(R.id.select_all)
    Button selectAll;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.history_bottom_container)
    LinearLayout historyBottomContainer;
    private CollectListAdapter adapter;
    private boolean top_flag = false;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_collect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        adapter = new CollectListAdapter(mList, getApplicationContext());
        collectList.setAdapter(adapter);
        collectTopbar.setLeftIsVisible(true);
        collectTopbar.setRightIsVisible(true);
        collectTopbar.setRightText("编辑");
        collectRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    @Override
    public void initViews() {
        collectTopbar.setOnTopbarClickListenter(this);
        collectRefresh.setOnRefreshListener(this);
        selectAll.setOnClickListener((View v) -> {
            // 遍历list的长度，将MyAdapter中的map值全部设为true
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("true");
            }
            // 数量设为list的长度
//            checkNum = list.size();
            // 刷新listview和TextView的显示
            dataChanged();
        });
        delete.setOnClickListener((View v) -> {
            Iterator<CollectModel.CollListBean> iterator = mList.iterator();
            while (iterator.hasNext()) {
                CollectModel.CollListBean temp = iterator.next();
                if (temp.getFlag().equals("true")) {
                    iterator.remove();
                }
            }
//            checkNum = 0;
            // 通知列表数据修改
            dataChanged();
        });
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
        map.put("numPerPage", pageSize);
        HttpApis.collectList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("collect"+response);
                ResponseObj<CollectModel, RespHeader> resp = new ResponseObj<CollectModel, RespHeader>();
                ResponseParser.parse(resp, response, CollectModel.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    List<CollectModel.CollListBean> models = resp.getBody().getCollList();
                    mList.addAll(models);
                    dataChanged();
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    private void dataChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        //顶部文字切换
        if (!top_flag) {
            collectTopbar.setRightText("取消");
            historyBottomContainer.setVisibility(View.VISIBLE);
            showCheckView("false");
            top_flag = true;
        } else {
            collectTopbar.setRightText("编辑");
            historyBottomContainer.setVisibility(View.GONE);
            showCheckView("");
            top_flag = false;
        }

    }

    private void showCheckView(String tmp) {
        if (StringUtils.isNullOrEmpty(tmp)) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("");
            }
        } else {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("false");
            }
        }
        dataChanged();
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
