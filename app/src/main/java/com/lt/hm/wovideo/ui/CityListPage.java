package com.lt.hm.wovideo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.city.CityListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.City;
import com.lt.hm.wovideo.model.CityArrayModel;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.PinyinUtil;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.SiteBSTree;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.CustomTopbar;
import com.lt.hm.wovideo.widget.DividerDecoration;
import com.lt.hm.wovideo.widget.MySectionIndexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityListPage extends BaseActivity implements CustomTopbar.myTopbarClicklistenter{

    public static final int CITY_RESULT = 456;
    public static String CITY_NAME = "cityName";
    @BindView(R.id.city_list)
    RecyclerView cityList;
    @BindView(R.id.sort_key)
    TextView sortKey;
    @BindView(R.id.text_place)
    TextView placeText;
    @BindView(R.id.person_topbar)
    CustomTopbar topbar;
    CityListAdapter mAdapter;
    private List<City> cities = new ArrayList<>();
    /**
     * 定义字母表的排序规则
     */
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String currentCity;

    @Override
    protected void init(Bundle savedInstanceState) {
        topbar.setLeftIsVisible(true);
        topbar.setRightIsVisible(true);
        topbar.setOnTopbarClickListenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_city_list;
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initDatas() {
        currentCity = SharedPrefsUtils.getStringPreference(getApplicationContext(),"city_name");
        cities = FileUtil.cities;
        setValue(cities);
    }

    private void setValue(List<City> list) {
        TLog.error(list.toString());
        MySectionIndexer<City> indexer = new MySectionIndexer(list, alphabet);
        mAdapter = new CityListAdapter(R.layout.item_ont_text, list, indexer);
        cityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //添加分割线
        cityList.addItemDecoration(new DividerDecoration(this, 2, getResources().getColor(R.color.gray_lighter1)));
        cityList.setAdapter(mAdapter);

        placeText.setText(currentCity);
        sortKey.setText("您当前可能在");
        placeText.setVisibility(View.VISIBLE);
        sortKey.setVisibility(View.VISIBLE);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                TLog.error("position--->" + list.get(i).getCode());
                Intent intent = new Intent();
                intent.putExtra("city", list.get(i).getCity());
                setResult(CITY_RESULT, intent);
                finish();
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
