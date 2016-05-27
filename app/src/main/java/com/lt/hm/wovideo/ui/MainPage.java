package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.fragment.HomeFragment;
import com.lt.hm.wovideo.widget.indicatorTab.IconPagerAdapter;
import com.lt.hm.wovideo.widget.indicatorTab.IconTabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/26
 */
public class MainPage extends BaseActivity implements IconTabPageIndicator.OnTabReselectedListener {
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    IconTabPageIndicator indicator;




    @Override
    protected int getLayoutId() {
        return R.layout.layout_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        List<BaseFragment> list= new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HomeFragment home = new HomeFragment();
            home.setTitle("首页");
            home.setIconId(R.mipmap.ic_launcher);
            list.add(home);
        }
        FragmentPagerAdapter adapter= new FragmentAdapter(list,getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator.setOnTabReselectedListener(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onTabReselected(int position) {
        viewPager.setCurrentItem(position);
    }


    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }
}
