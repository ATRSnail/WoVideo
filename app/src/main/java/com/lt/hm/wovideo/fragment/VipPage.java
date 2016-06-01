package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class VipPage extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view  = inflater.inflate(R.layout.layout_home,container,false);
        return view;
    }
}
