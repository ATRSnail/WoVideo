package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.model.RecomList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/8
 */
public class Class_Item_Page extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.class_details_list)
    RecyclerView classDetailsList;

    Bottom_ListAdapter adapter;
    List<RecomList.Videos> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_class_detail_item, container, false);
        unbinder= ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RecomList.Videos videos= new RecomList.Videos();
            videos.setName("Title"+i);
            videos.setDesc("desc"+i);
            mList.add(videos);
        }
        adapter= new Bottom_ListAdapter(getActivity(),R.layout.layout_home_item,mList);
        classDetailsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        classDetailsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
