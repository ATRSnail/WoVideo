package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.lt.hm.wovideo.widget.transformer.AlphaPageTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class ChoicePage extends BaseFragment {
    @BindView(R.id.person_center)
    ImageView personCenter;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.qr_scan)
    ImageView qrScan;
    @BindView(R.id.img_indicator)
    ImageIndicatorView imgIndicator;
    Unbinder unbinder;
    @BindView(R.id.id_viewpager)
    ViewPager idViewpager;
    private PagerAdapter mAdapter;

    int[] imgRes = {R.drawable.img_1,R.drawable.img_2,R.drawable.img_3};
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
        idViewpager.setPageMargin(40);
        idViewpager.setOffscreenPageLimit(3);
        idViewpager.setAdapter(mAdapter=new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(getContext());
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                view.setLayoutParams(lp);
//                view.setText(position + ":" + view);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
//                view.setBackgroundColor(Color.parseColor("#44ff0000"));
                view.setImageResource(imgRes[position]);
                container.addView(view);
//                view.setAdjustViewBounds(true);
                return view;

            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView((View) object);
            }
            @Override
            public int getCount() {
                return imgRes.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
        idViewpager.setPageTransformer(true,new AlphaPageTransformer());
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        Integer[] imgs = new Integer[]{R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner};
        imgIndicator.setupLayoutByDrawable(imgs);
        imgIndicator.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imgIndicator.show();
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


