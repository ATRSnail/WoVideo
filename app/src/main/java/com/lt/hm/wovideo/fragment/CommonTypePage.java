package com.lt.hm.wovideo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.adapter.recommend.RecCateAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.model.CateModel;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.ui.CityListPage;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.utils.TLog;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lt.hm.wovideo.model.VideoType.MOVIE;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class CommonTypePage extends BaseFragment{

    public static final String TYPE_KEY = "type";
    private View view;
    private int type;
    private List<RecomList.Videos> videos = new ArrayList<>();
    private List<CateModel> cates = new ArrayList<>();
    private Bottom_ListAdapter listAdapter;

    @BindView(R.id.header)
    RecyclerViewHeader header;
    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerView;
    @BindView(R.id.recycle_cate)
    RecyclerView cateRecycle;
    @BindView(R.id.text_change_city)
    TextView changeCityBtn;
    @BindView(R.id.frame_recommend)
    View recommendImg;
    @BindView(R.id.linear_live)
    View liveLinear;

    public static CommonTypePage getInstance(int type){
        CommonTypePage common = new CommonTypePage();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_KEY,type);
        common.setArguments(bundle);
        return common;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_common_type_page;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);

        initData();
        initView(null);

    }

    @Override
    public void initView(View view) {
        initViewByType(type);
        addCateView(cates);
        addGridView(videos);

        changeCityBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(),CityListPage.class));
        });
    }

    @Override
    public void initData() {

        type = getArguments().getInt(TYPE_KEY);
        TLog.error(type+"");
        videos.add(new RecomList.Videos("",1,"","dd","dddd","ddd","dd","dd","dd"));
        videos.add(new RecomList.Videos("",1,"","dd","dddd","ddd","dd","dd","dd"));
        videos.add(new RecomList.Videos("",1,"","dd","dddd","ddd","dd","dd","dd"));
        videos.add(new RecomList.Videos("",1,"","dd","dddd","ddd","dd","dd","dd"));
        videos.add(new RecomList.Videos("",1,"","dd","dddd","ddd","dd","dd","dd"));

        cates.add(new CateModel("1","dd"));
        cates.add(new CateModel("1","dd"));
        cates.add(new CateModel("1","dd"));
        cates.add(new CateModel("1","dd"));
    }

    private void addGridView(List<RecomList.Videos> videos){
        listAdapter = new Bottom_ListAdapter(getApplicationContext(),R.layout.layout_new_home_item,videos);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        header.attachTo(recyclerView);
        recyclerView.setAdapter(listAdapter);
    }

    private void initViewByType(int type){
        switch (type){
            case 1://电影
                cateRecycle.setVisibility(View.VISIBLE);
                break;
            case 2://电视剧
                cateRecycle.setVisibility(View.VISIBLE);
                break;
            case 3://综艺
                cateRecycle.setVisibility(View.VISIBLE);
                break;
            case 4://体育
                changeCityBtn.setVisibility(View.VISIBLE);
                liveLinear.setVisibility(View.VISIBLE);
            case 5://直播
                changeCityBtn.setVisibility(View.VISIBLE);
                liveLinear.setVisibility(View.VISIBLE);
                break;
            default:
                cateRecycle.setVisibility(View.VISIBLE);
        }
    }

    private void addCateView(List<CateModel> cates){
        RecCateAdapter adapter = new RecCateAdapter(R.layout.item_first_cate,cates);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),5);
        cateRecycle.setLayoutManager(manager);
        cateRecycle.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener((view1, i) -> {
            startActivity(new Intent(getActivity(),NewClassDetailPage.class));
        });
    }

}
