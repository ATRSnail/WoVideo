package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/10
 */
public class GuidPage extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    @BindView(R.id.point)
    LinearLayout point;

    private ViewPager guide_viewpager;
    private ViewPagerAdapter mAdapter;
    private List<View> views;
    private ImageView start_btn;
    private ImageView[] points;
    //记录当前选中位置
    private int currentIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.layout_guide_01, null));
        views.add(inflater.inflate(R.layout.layout_guide_02, null));
        views.add(inflater.inflate(R.layout.layout_guide_03, null));
        start_btn= (ImageView) views.get(views.size()-1).findViewById(R.id.btn_try_now);
        mAdapter = new ViewPagerAdapter(views, this);
        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        guide_viewpager.setAdapter(mAdapter);
        guide_viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void initViews() {
        start_btn.setOnClickListener((View v)->{
            UIHelper.ToMain2(this);
            GuidPage.this.finish();
        });
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        setCurDot(position);
        if (position == 3) {
            UIHelper.ToMain2(this);
            GuidPage.this.finish();
        } else {

            guide_viewpager.setCurrentItem(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class ViewPagerAdapter extends PagerAdapter {
        //创建两个变量，并作为参数传入构造方法
        private List<View> views;
        private Context context;

        public ViewPagerAdapter(List<View> views, Context context) {
            super();
            this.views = views;
            this.context = context;
        }

        //不要的时候，销毁View的方法,根据position进行索引，并制定View为ViewPager
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        //加载View的方法，类似于AdapterView中的getView方法
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //添加页卡(遍历View这个集合，加载所有View作为页卡）
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);

        }
    }

}
