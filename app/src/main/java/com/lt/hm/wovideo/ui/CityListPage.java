package com.lt.hm.wovideo.ui;

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
public class CityListPage extends BaseActivity {

    public static final int CITY_RESULT = 456;
    @BindView(R.id.city_list)
    RecyclerView cityList;
    @BindView(R.id.sort_key)
    TextView sortKey;
    @BindView(R.id.text_place)
    TextView placeText;
    CityListAdapter mAdapter;
    MySectionIndexer sectionIndexer;
    private List<City> cities = new ArrayList<>();
    private String city_json;

    /**
     * 定义字母表的排序规则
     */
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    protected void init(Bundle savedInstanceState) {
        city_json = SharedPrefsUtils.getStringPreference(getApplicationContext(), "position");
        TLog.log("json_city" + city_json);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_city_list;
    }

    @Override
    public void initViews() {

    }


    private void JsonToModel(String city_json) {
        Map<String,String> map = new HashMap<>();
        CityArrayModel cityArrayModel = new Gson().fromJson(city_json, CityArrayModel.class);
        if (cityArrayModel.list.size() > 0) {
            SiteBSTree<String> tree = new SiteBSTree<String>();
            for (int i = 0; i < cityArrayModel.list.size(); i++) {
                tree.insert(PinyinUtil
                        .getPinYinHeadChar(cityArrayModel.list.get(i).getCity()), cityArrayModel.list.get(i).getCity()+cityArrayModel.list.get(i).getCode());
                map.put(cityArrayModel.list.get(i).getCity(),cityArrayModel.list.get(i).getCode());
            }
            cities = tree.inOrder();
        }
    }

    @Override
    public void initDatas() {
        cities = FileUtil.cities;
        setValue(cities);
     //   readPositionFromAsset();
    }

    private static final String POSITION_TAG = "position";

    private void readPositionFromAsset() {
        new Thread(() -> {
            try {
                AssetManager manager = getAssets();
                InputStream is = manager.open("citys.json");
                city_json = FileUtil.readInStream(is);
                JsonToModel(city_json);
                if (cities != null) {
                    Message msg = new Message();
                    msg.obj = cities;
                    msg.what = SUCCESS_BST;
                    handler.sendMessage(msg);
                }
                TLog.log("json_city" + city_json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private final int SUCCESS_BST = 10001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_BST:
                    List<City> city = (List<City>) msg.obj;

                    setValue(city);
                    break;
            }
        }
    };

    private void setValue(List<City> list) {
        TLog.error(list.toString());
        MySectionIndexer<City> indexer = new MySectionIndexer(list, alphabet);
        mAdapter = new CityListAdapter(R.layout.item_ont_text,list, indexer);
        cityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //添加分割线
        cityList.addItemDecoration(new DividerDecoration(this,2,getResources().getColor(R.color.gray_lighter1)));
        cityList.setAdapter(mAdapter);

        placeText.setText("北京");
        sortKey.setText("您当前可能在");
        placeText.setVisibility(View.VISIBLE);
        sortKey.setVisibility(View.VISIBLE);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                TLog.error("position--->"+list.get(i).getCode());
                Intent intent = new Intent();
                intent.putExtra("city",list.get(i).getCity());
                setResult(CITY_RESULT,intent);
                finish();
            }
        });
    }

}
