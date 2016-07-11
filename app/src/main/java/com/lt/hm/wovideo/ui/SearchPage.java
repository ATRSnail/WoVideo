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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.search.SearchHistoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.PercentLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
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
    boolean flag =false;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
                try {
                    search(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                flag=true;
            }
        }
        return true;
    }

    private void search(String s) throws JSONException {
        JSONArray array = new JSONArray();
        JSONArray array1 = ACache.get(this).getAsJSONArray("search_history");
        if (StringUtils.isNullOrEmpty(s)){
            Toast.makeText(getApplicationContext(),"搜索内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isNullOrEmpty(array1) && array1.length()>0) {
            array = array1;
        }else{
            array.put(s);
            ACache.get(this).put("search_history", array);
        }
        if (array1!=null ){
            for (int i = 0; i < array1.length(); i++) {
                String tmp= (String) array1.get(i);
                if (tmp.equals(s)){
                    break;
                }else{
                    array.put(s);
                    ACache.get(this).put("search_history", array);
                    break;
                }
            }
        }

        Bundle bundle= new Bundle();
        bundle.putString("flag",searchEdit.getText().toString());
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
        List<String> historys= new ArrayList<>();
        JSONArray array = ACache.get(this).getAsJSONArray("search_history");
        if (!StringUtils.isNullOrEmpty(array) && array.length()>0) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    String tag= (String) array.get(i);
                    historys.add(tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            searchAdapter = new SearchHistoryAdapter(this,historys);
            searchHistoryList.setLayoutManager(new LinearLayoutManager(this));
            searchHistoryList.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
            searchAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    try {
                        search(historys.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
