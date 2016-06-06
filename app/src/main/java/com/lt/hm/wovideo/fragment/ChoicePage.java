package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.model.Videos;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.lt.hm.wovideo.widget.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class ChoicePage extends BaseFragment {


    @BindView(R.id.img_indicator)
    ImageIndicatorView imgIndicator;
    @BindView(R.id.search_view_layout)
    RelativeLayout searchViewLayout;
    @BindView(R.id.id_viewpager)
    ViewPager idViewpager;
    @BindView(R.id.home_recycler)
    RecyclerView homeRecycler;
    private PagerAdapter mAdapter;
    Unbinder unbinder;
    Bottom_ListAdapter bottom_adapter;
    List<Videos> b_list;

    int[] imgRes = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_choice, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);

        initData();
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        idViewpager.setPageMargin(5);
        idViewpager.setOffscreenPageLimit(3);
        idViewpager.setAdapter(mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(getActivity().getApplicationContext());
                ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
//                view.setText(position + ":" + view);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
//                view.setBackgroundColor(Color.parseColor("#44ff0000"));
                view.setImageResource(imgRes[position]);
                container.addView(view);
                view.setAdjustViewBounds(true);
                return view;

            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return imgRes.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        idViewpager.setPageTransformer(true, new ScaleInTransformer());



    }

    @Override
    public void initView(View view) {
        super.initView(view);
        Integer[] imgs = new Integer[]{R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner};
        imgIndicator.setupLayoutByDrawable(imgs);
        imgIndicator.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imgIndicator.show();
        b_list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Videos videos= new Videos();
            videos.setName("Titile"+i);
            videos.setDesc("desc"+i);
            b_list.add(videos);
        }
        bottom_adapter= new Bottom_ListAdapter(getActivity().getApplicationContext(),R.layout.layout_home_item,b_list);
        homeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeRecycler.setHasFixedSize(false);
        homeRecycler.setAdapter(bottom_adapter);
        bottom_adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.person_center:

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}


