package com.lt.hm.wovideo.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.recommend.TabFragmentAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.interf.OnPlaceChangeListener;
import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseChannel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UpdateLocationMsg;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.widget.MyPageChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/2/16
 */
public class NewChoicePage extends BaseFragment implements OnPlaceChangeListener, OnUpdateLocationListener {
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
        UserModel userModel = UserMgr.getUseInfo();
        if (userModel != null)
            map.put("userid", userModel.getId());
        HttpApis.getIndividuationChannel(map, HttpApis.http_one, new HttpCallback<>(ResponseChannel.class, this));
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
        vipSelector.setOnClickListener(v -> {
            if (UserMgr.isLogin()) {
                UIHelper.ToPersonlitySetPage(getContext());
             } else {
                DialogHelp.getMessageDialog(getContext(),"请先登录").show();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        getClassInfos();
        UpdateLocationMsg.getInstance().addRegisterSucListeners(this);
    }

    private String cityCode = "";
    private String cityName = "";

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {
            case HttpApis.http_one:
                ResponseChannel channelRe = (ResponseChannel) value;
                channels = channelRe.getBody().getSelectedChannels();
                cityCode = TextUtils.isEmpty(cityCode) ? SharedPrefsUtils.getStringPreference("city_code") : cityCode;
                cityName = TextUtils.isEmpty(cityName) ? SharedPrefsUtils.getStringPreference("city_name") : cityName;
                TLog.error("cityCode--->" + cityCode);
                initBottom(cityName);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initBottom(String str) {
        fragments.clear();
        mTitles.clear();
        channels.add(0, new ChannelModel("推荐", ChannelModel.RECOMMEND_ID));
        // TODO: 8/15/16 ADD Current position name
        channels.add(1, new ChannelModel(TextUtils.isEmpty(str) ? "地区" : str, ChannelModel.LOCAL_ID));

        for (int i = 0; i < channels.size(); i++) {
            bean = channels.get(i);
            mTitles.add(bean.getFunName());
            CommonTypePage page = CommonTypePage.getInstance(bean);
            if (bean.getFunCode().equals(ChannelModel.LOCAL_ID))
                page.setOnPlaceChangeListener(this);
//            VideoListFrg page = new VideoListFrg();
            fragments.add(page);
        }

        TLog.error("title--->" + mTitles.toString());
        tabFragmentAdapter = new TabFragmentAdapter(fragments, mTitles, getChildFragmentManager(), getApplicationContext());
        choiceViewPage.setAdapter(tabFragmentAdapter);
        choiceViewPage.setOffscreenPageLimit(mTitles.size());

        tabLayout.setupWithViewPager(choiceViewPage);
        tabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);
        choiceViewPage.addOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    public void onChangePlaceListener(String str) {
        TLog.log("return_str" + str);
        setLocalTabTile(str);
    }

    private void setLocalTabTile(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            if (tabLayout != null) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(1);
                if (vgTab == null) return;
                View tabViewChild = vgTab.getChildAt(1);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setText(cityName);
                }
                // TODO: 8/16/16  refresh  the area fragment data
            }
        }
    }

    @Override
    public void onUpdateLocListener(String name, String code) {
        TLog.error("place---" + name);
        setLocalTabTile(name);
    }

    @Override
    public void onDestroyView() {
        UpdateLocationMsg.getInstance().removeRegisterSucListeners(this);
        super.onDestroyView();
    }
}
