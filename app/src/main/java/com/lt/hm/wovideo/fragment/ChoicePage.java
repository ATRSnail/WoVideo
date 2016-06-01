package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_choice, container, false);
        unbinder= ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initView(View view) {
        super.initView(view);
        Integer[] imgs= new Integer[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
        imgIndicator.setupLayoutByDrawable(imgs);
        imgIndicator.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imgIndicator.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}


