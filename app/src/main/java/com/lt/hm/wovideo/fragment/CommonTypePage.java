package com.lt.hm.wovideo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.adapter.recommend.GridAdapter;
import com.lt.hm.wovideo.adapter.recommend.LiveAdapter;
import com.lt.hm.wovideo.adapter.recommend.RecCateAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.base.BaseLazyFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.model.CateModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.ui.CityListPage;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.utils.ObjectUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.CustomGridView;
import com.lt.hm.wovideo.widget.CustomListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.lt.hm.wovideo.model.VideoType.MOVIE;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class CommonTypePage extends BaseLazyFragment {

    public static final String TYPE_KEY = "type";
    private View view;
    private int type;
    private List<RecomList.Videos> videos = new ArrayList<>();
    private List<CateModel> cates = new ArrayList<>();
    private List<LiveModles.LiveModel> liveModels = new ArrayList<>();
    private Bottom_ListAdapter listAdapter;

    @BindView(R.id.header)
    RecyclerViewHeader header;
    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerView;
    @BindView(R.id.recycle_cate)
    CustomGridView cateRecycle;
    @BindView(R.id.text_change_city)
    TextView changeCityBtn;
    @BindView(R.id.frame_recommend)
    View recommendImg;
    @BindView(R.id.lv_live)
    CustomListView liveLv;

    private boolean isOnline = false;
    private LiveAdapter liveAdapter;
    private Context context;
    private boolean isHasView = false;//防止重复加载view

    public static CommonTypePage getInstance(int type) {
        CommonTypePage common = new CommonTypePage();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_KEY, type);
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
        if (!isHasView) {
            view = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, view);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onFirstUserVisible() {
        if (isHasView) return;
        TLog.error("OnFirst---");
        isHasView = true;
        initData();
        initView(null);
    }

    @Override
    public void initView(View view) {
        initViewByType(type);
        addCateView(cates);
        addGridView(videos);
        addLiveListView(liveModels);

        changeCityBtn.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), CityListPage.class));
        });
    }

    @Override
    public void initData() {
        context = getApplicationContext();

        type = getArguments().getInt(TYPE_KEY);
        TLog.error(type + "");
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));

        liveModels.add(new LiveModles.LiveModel("dddd"));
        liveModels.add(new LiveModles.LiveModel("ddd"));
        liveModels.add(new LiveModles.LiveModel("ddd"));

        getCateTag("");
    }

    private void addGridView(List<RecomList.Videos> videos) {
        listAdapter = new Bottom_ListAdapter(getApplicationContext(), R.layout.layout_new_home_item, videos);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        header.attachTo(recyclerView);
        recyclerView.setAdapter(listAdapter);
    }

    private void initViewByType(int type) {
        switch (type) {
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
            case 5://直播
                changeCityBtn.setVisibility(View.VISIBLE);
                break;
            default:
                cateRecycle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 电影,电视剧分类gridview
     * @param cateTags
     */
    private void addCateView(List<CateModel> cateTags) {
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), cateTags, R.layout.item_first_cate);
        cateRecycle.setAdapter(gridAdapter);
        cateRecycle.setSelector(new ColorDrawable(Color.TRANSPARENT));
        cateRecycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), NewClassDetailPage.class));
            }
        });
    }

    /*
     地方直播列表
     */
    private void addLiveListView(List<LiveModles.LiveModel> models){
        View headView = LayoutInflater.from(context).inflate(R.layout.include_title_text,null);
        liveLv.addHeaderView(headView);
        liveAdapter = new LiveAdapter(context,models,R.layout.item_live_cate);
        liveLv.setAdapter(liveAdapter);
    }

    /*
     *获取标签
     */
    private void getCateTag(String type) {

        if (!isOnline){
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 0));
            cates.add(new CateModel("1", "dd", 1));
        }else {
            HashMap<String,Object> map = new HashMap<>();
            map.put("type",type);
            HttpApis.getCategoryTag(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);

                }
            });
        }



    }

}
