package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.search.SearchHistoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.db.SearchHistoryDataBase;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.PercentLinearLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/7
 */
public class SearchPage extends BaseActivity {

    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_cancel)
    Button searchCancel;
    @BindView(R.id.search_head)
    PercentLinearLayout searchHead;
    @BindView(R.id.search_history_list)
    RecyclerView searchHistoryList;
    @BindView(R.id.search_view_layout)
    LinearLayout searchViewLayout;
    SearchHistoryAdapter searchAdapter;
    SearchHistoryDataBase searchHistory;
    boolean flag =false;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        searchHistory= new SearchHistoryDataBase(getApplicationContext());
        searchHead.requestFocus();
        searchEdit.setFocusable(true);
        searchEdit.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {

            return editSearch(v, keyCode);
        });

    }

    private boolean editSearch(View v, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            if (!flag){
                String s = searchEdit.getText().toString();
                search(s);
                flag=true;
            }
        }
        return true;
    }

    private void search(String s){
        searchHistory.save(s);
        Bundle bundle= new Bundle();
        bundle.putString("flag",s);
        UIHelper.ToSearchResultPage(SearchPage.this,bundle);


    }
    @Override
    public void initViews() {
        searchCancel.setOnClickListener((View v)->{
            SearchPage.this.finish();
        });
        searchEdit.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (hasFocus) {
                searchIcon.setVisibility(View.GONE);
            } else {
                searchIcon.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initDatas() {
        List<String> historys=searchHistory.query("");
        if (!StringUtils.isNullOrEmpty(historys) && historys.size()>0) {
            searchAdapter = new SearchHistoryAdapter(this,historys);
            searchHistoryList.setLayoutManager(new LinearLayoutManager(this));
            searchHistoryList.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
            searchAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                        search(historys.get(i));
                }
            });
            searchAdapter.setListener(new SearchHistoryAdapter.OnDelete() {
                @Override
                public void del(String s) {
                    TLog.log("delete"+s);
                    searchHistory.delete(s);
                    initDatas();
                }
            });

        }else{
            searchHistoryList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
//                // 如果两次按键时间间隔大于2000毫秒，则不退出
//                Toast.makeText(this, getResources().getString(R.string.second_back_hint), Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();// 更新mExitTime
//            } else {
////				aCache.put("first_pay", "");
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//            }
            this.finish();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }
}
