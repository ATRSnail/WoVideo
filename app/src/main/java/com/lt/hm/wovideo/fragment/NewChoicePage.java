package com.lt.hm.wovideo.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.recommend.TabFragmentAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.TypeList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.ui.CityListPage;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.ui.PersonalitySet;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.ViewPagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/2/16
 */
public class NewChoicePage extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.img_plus)
    ImageView vipSelector;
    @BindView(R.id.choice_view_page)
    ViewPager choiceViewPage;

    private TabFragmentAdapter tabFragmentAdapter;
    List<TypeList.TypeListBean> mClass = new ArrayList<>();
    TypeList.TypeListBean bean;
    int CURRENT_POSITION;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(view);
            initData();
        }
        return view;
    }

    private void getClassInfos() {

        HashMap<String, Object> map = new HashMap<>();
        HttpApis.getClassesInfo(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<TypeList, RespHeader> resp = new ResponseObj<TypeList, RespHeader>();
                ResponseParser.parse(resp, response, TypeList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals("0")) {
                    TLog.log(resp.toString());
                    if (mClass.size() > 0) {
                        mClass.clear();
                    }
                    mClass.addAll(resp.getBody().getTypeList());
                    initBottom();

                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }

            }
        });
    }

    private void initBottom() {
        fragments.clear();
        mTitles.clear();
        for (int i = 0; i < mClass.size(); i++) {
            bean = mClass.get(i);
            mTitles.add(bean.getTypeName());
            CommonTypePage page = CommonTypePage.getInstance(Integer.valueOf(bean.getId()));
            fragments.add(page);
        }

        tabFragmentAdapter = new TabFragmentAdapter(fragments, mTitles, getChildFragmentManager(), getApplicationContext());

        choiceViewPage.setAdapter(tabFragmentAdapter);
        choiceViewPage.setOffscreenPageLimit(mTitles.size());

        tabLayout.setupWithViewPager(choiceViewPage);
        tabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_new_choice;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        vipSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), NewClassDetailPage.class));
           //     startActivity(new Intent(getContext(), PersonalitySet.class));
            }
        });
        getClassInfos();
    }

    @Override
    public void initData() {
        super.initData();
    }
}
