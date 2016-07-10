package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.search.SearchResultAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.SearchResult;
import com.lt.hm.wovideo.utils.StringUtils;
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
 * @create_date 16/6/29
 */
public class SearchResultPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {

    @BindView(R.id.search_result_list)
    RecyclerView searchResultList;
    SearchResultAdapter gridAdapter;
    String flag;
    @BindView(R.id.search_result_top)
    SecondTopbar searchResultTop;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("flag")) {
            flag = bundle.getString("flag");
        }
    }

    @Override
    public void initViews() {
        searchResultTop.setRightIsVisible(false);
        searchResultTop.setLeftIsVisible(true);
        searchResultTop.setOnTopbarClickListenter(this);
    }

    @Override
    public void initDatas() {
        searchDatas();
    }

    private void searchDatas() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfname", flag);
        maps.put("pageNum", 1);
        maps.put("numPerPage", 10);
        HttpApis.searchPage(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<SearchResult, RespHeader> resp = new ResponseObj<SearchResult, RespHeader>();
                ResponseParser.parse(resp, response, SearchResult.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    List<SearchResult.VfListBean> lists = new ArrayList<SearchResult.VfListBean>();
                    if (!StringUtils.isNullOrEmpty(resp.getBody().getVfList())&& resp.getBody().getVfList().size()>0){
                        lists.addAll(resp.getBody().getVfList());
                        gridAdapter = new SearchResultAdapter(SearchResultPage.this, lists);
                        searchResultList.setLayoutManager(new LinearLayoutManager(SearchResultPage.this));
                        searchResultList.setAdapter(gridAdapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }
}
