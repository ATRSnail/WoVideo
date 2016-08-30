package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.search.SearchResultAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.SearchResult;
import com.lt.hm.wovideo.model.VfinfoDetailModel;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

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
    @BindView(R.id.empty_view)
    Button empty_view;

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
                TLog.log("search_result"+response);
                ResponseObj<SearchResult, RespHeader> resp = new ResponseObj<SearchResult, RespHeader>();
                ResponseParser.parse(resp, response, SearchResult.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
//                     lists = new ArrayList<SearchResult.VfListBean>();
                    if (!StringUtils.isNullOrEmpty(resp.getBody().getVfList())&& resp.getBody().getVfList().size()>0){
                        List<SearchResult.VfListBean> lists= resp.getBody().getVfList();
                        gridAdapter = new SearchResultAdapter(SearchResultPage.this, lists);
                        searchResultList.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        if (searchResultList!=null){
                            searchResultList.setLayoutManager(new LinearLayoutManager(SearchResultPage.this));
                            searchResultList.setAdapter(gridAdapter);
                            gridAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int i) {
                                    getChangePage(lists.get(i).getVfinfo_id());

                                }
                            });
                        }
                    }else{
                        empty_view.setVisibility(View.VISIBLE);
                        searchResultList.setVisibility(View.GONE);
                    }
                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    searchResultList.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void getChangePage(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        maps.put("typeid", VideoType.MOVIE.getId());
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("getchange" + response);
                ResponseObj<VfinfoDetailModel, RespHeader> resp = new ResponseObj<VfinfoDetailModel, RespHeader>();
                ResponseParser.parse(resp, response, VfinfoDetailModel.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    UIHelper.ToAllCateVideo(SearchResultPage.this,resp.getBody().getVfinfo().getTypeId(),vfId);
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
