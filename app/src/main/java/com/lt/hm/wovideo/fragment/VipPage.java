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
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.widget.ViewPagerIndicator;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class VipPage extends BaseFragment {
    @BindView(R.id.img_indicator_vip)
    ImageIndicatorView imgIndicatorVip;
    @BindView(R.id.vip_person_logo)
    ImageView vipPersonLogo;
    @BindView(R.id.vip_person_account)
    TextView vipPersonAccount;
    @BindView(R.id.vip_person_vipicon)
    ImageView vipPersonVipicon;
    @BindView(R.id.vip_person_arrow)
    ImageView vipPersonArrow;
    @BindView(R.id.vip_view_indicator)
    ViewPagerIndicator vipViewIndicator;
    @BindView(R.id.vip_view_page)
    ViewPager vipViewPage;

    Unbinder unbinder;

    private List<String> mTitles = Arrays.asList("热门", "头条", "恋爱", "搞笑", "玄幻", "穿越");
    private List<BaseFragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        Integer[] imgs = new Integer[]{R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner};
        imgIndicatorVip.setupLayoutByDrawable(imgs);
        imgIndicatorVip.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imgIndicatorVip.show();

        initBottom();

    }

    private void initBottom() {
        for (String title :
                mTitles) {
            BaseFragment hotfragment = new ChoicePage();
            fragments.add(hotfragment);
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
        vipViewIndicator.setViewPager(vipViewPage, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
