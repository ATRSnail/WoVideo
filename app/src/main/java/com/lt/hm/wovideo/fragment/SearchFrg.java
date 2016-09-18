package com.lt.hm.wovideo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.widget.BlurPopuwindow.BlurPopuwindow;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/14
 */
public class SearchFrg extends BlurPopuwindow{

    @BindView(R.id.tv_name)
    TextView tv_name;

    public static SearchFrg newInstance() {
        SearchFrg fragment = new SearchFrg();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_search,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this,getActivity());
        tv_name.setText("ssss");
    }
}
