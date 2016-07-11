package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.TypeList;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.CustomScrollView;
import com.lt.hm.wovideo.widget.SelectMenuPop;
import com.lt.hm.wovideo.widget.ViewPagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

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
 * @create_date 16/5/30
 */
public class VipPage extends BaseFragment implements CustomScrollView.OnScrollListener {
    @BindView(R.id.vip_view_indicator)
    ViewPagerIndicator vipViewIndicator;
    @BindView(R.id.vip_view_page)
    ViewPager vipViewPage;

    Unbinder unbinder;
    @BindView(R.id.vip_selector)
    ImageView vipSelector;
    List<TypeList.TypeListBean> mClass = new ArrayList<>();
    int CURRENT_POSITION;
    private List<String> mTitles;
    private List<BaseFragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    private String lx;
    private String sx;
    private String dq;
    private String nd;
    private boolean shown =false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        mTitles = new ArrayList<>();
        initView(view);
        initData();
        view.setTag("vip_page");
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        getClassInfos();
        vipSelector.setOnClickListener((View v) -> {
            if (CURRENT_POSITION != -1 && CURRENT_POSITION != 0) {
                SelectMenuPop pop = new SelectMenuPop(getActivity(),CURRENT_POSITION);
                pop.showPopupWindow(vipViewIndicator,shown);
                pop.setListener(new SelectMenuPop.OnRadioClickListener() {
                    @Override
                    public void clickListener(String key, String value) {
                        // TODO: 16/7/8 刷新Fragment  根据条件
                        // TODO: 16/7/8  动态控制 当前显示 的筛选菜单

//                        SearchChecked(key, value);
//                        listener.onChoosen(key, value);
                    }
                });
            }

        });
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
        if (mTitles.size() > 0) {
            mTitles.clear();
        }
        for (int i = 0; i < mClass.size(); i++) {
            mTitles.add(mClass.get(i).getTypeName());
        }
        mTitles.add(0, "推荐");
        for (int i = 0; i < mTitles.size(); i++) {
            BaseFragment fragment;
            if (i == 0) {
                fragment = new RecommendPage();
            } else {
                fragment = new VipItemPage();
            }

            Bundle bundle = new Bundle();
            if (i == 0) {
                bundle.putString("id", "0");
            } else {
                String id = mClass.get(i - 1).getId();
                bundle.putString("id", id);
            }
            bundle.putString("isvip", "1");
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        vipViewIndicator.setVisibleTabCount(5);
        vipViewIndicator.setTabItemTitles(mTitles);
        vipViewPage.setAdapter(mAdapter);
        vipViewIndicator.setOnPageChangeListener(new ViewPagerIndicator.PageOnChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENT_POSITION = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vipViewIndicator.setViewPager(vipViewPage, 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
    public void getVideoDetails(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {

                    if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToMoviePage(getActivity(), bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(getActivity(), bundle);

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(getActivity(), bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(getActivity(), bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.LIVE.getId()) {
                        UIHelper.ToLivePage(getActivity());
                    }
                }
            }
        });
    }
    @Override
    public void onScroll(int scrollY) {
    }
    private void SearchChecked(String key, String value) {
        if (key.equals("类型")) {
            if (value.equals("0")) {
                lx = "";
            } else {
                lx = value;
            }
        }
        if (key.equals("属性")) {
            if (value.equals("0")) {
                sx = "";
            } else {
                sx = value;
            }
        }
        if (key.equals("地区")) {
            if (value.equals("0")) {
                dq = "";
            } else {
                dq = value;
            }
        }
        if (key.equals("年代")) {
            if (value.equals("0")) {
                nd = "";
            } else {
                nd = value;
            }
        }
//        if (!first_open) {
//            getListDatas(mId);
//        }
    }

}
