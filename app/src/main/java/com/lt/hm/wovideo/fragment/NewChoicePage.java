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
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.TypeList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.response.ResponseCateTag;
import com.lt.hm.wovideo.model.response.ResponseChannel;
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
    private List<ChannelModel> channels = new ArrayList<>();
    private ChannelModel bean;
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
        HttpApis.getIndividuationChannel(map,HttpApis.http_one,new HttpCallback<>(ResponseChannel.class, this));
    }

    private void initBottom() {
        fragments.clear();
        mTitles.clear();
        channels.add(0,new ChannelModel("推荐","6"));
        channels.add(1,new ChannelModel("地区","7"));
        for (int i = 0; i < channels.size(); i++) {
            bean = channels.get(i);
            mTitles.add(bean.getFunName());
            CommonTypePage page = CommonTypePage.getInstance(bean);
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
                startActivity(new Intent(getContext(), PersonalitySet.class));
            }
        });
        getClassInfos();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag){
            case HttpApis.http_one:
                ResponseChannel channelRe = (ResponseChannel) value;
                channels = channelRe.getBody().getSelectedChannels();
                if (channels == null || channels.size() == 0) return;
                initBottom();
                break;
        }
    }
}
