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
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.adapter.home.FilmListAdapter;
import com.lt.hm.wovideo.adapter.home.LikeListAdapter;
import com.lt.hm.wovideo.adapter.recommend.GridAdapter;
import com.lt.hm.wovideo.adapter.recommend.LiveAdapter;
import com.lt.hm.wovideo.base.BaseLazyFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.CateTagModel;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.model.LikeList;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LocalCityModel;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.response.ResponseBanner;
import com.lt.hm.wovideo.model.response.ResponseCateTag;
import com.lt.hm.wovideo.model.response.ResponseFilms;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.model.response.ResponseLocalCityModel;
import com.lt.hm.wovideo.ui.CityListPage;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.CustomGridView;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class CommonTypePage extends BaseLazyFragment {

    public static final String CHANNEL = "channel";
    private View view;
    private String typeName;
    private String channelCode;
    private ChannelModel channel;
    private List<RecomList.Videos> videos = new ArrayList<>();
    private List<CateTagModel> cateTags = new ArrayList<>();//标签
    private List<LocalCityModel> localCites = new ArrayList<>();
    private List<BannerList.Banner> banner_list = new ArrayList<>();//bar
    private List<LikeModel> grid_list = new ArrayList<LikeModel>();//猜你喜欢
    private List<FilmMode> films = new ArrayList<>();//电影电视剧列表
    private LikeListAdapter listAdapter;
    private FilmListAdapter filmListAdapter;

    @BindView(R.id.header)
    RecyclerViewHeader header;
    @BindView(R.id.img_indicator_vip)
    ImageIndicatorView imageIndicatorView;
    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerView;
    @BindView(R.id.recycle_cate)
    CustomGridView cateGv;
    @BindView(R.id.text_change_city)
    TextView changeCityBtn;
    @BindView(R.id.frame_recommend)
    View recommendImg;
    @BindView(R.id.fl_page)
    View topPageFl;
    @BindView(R.id.item_img_bg)
    ImageView imgTopPage;
    @BindView(R.id.item_title)
    TextView tvTitle;
    @BindView(R.id.item_type)
    TextView tvType;
    @BindView(R.id.item_desc)
    TextView tvDesc;

    @BindView(R.id.rl_title)
    View titleRl;
    @BindView(R.id.lv_live)
    CustomListView liveLv;

    private boolean isOnline = true;
    private LiveAdapter liveAdapter;
    private Context context;
    private boolean isHasView = false;//防止重复加载view

    public static CommonTypePage getInstance(ChannelModel channel) {
        CommonTypePage common = new CommonTypePage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHANNEL,channel);
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
        titleRl.setVisibility(View.GONE);
        addLikeListView();
        //addListView(liveModels);

        changeCityBtn.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), CityListPage.class));
        });
    }

    @Override
    public void initData() {
        context = getApplicationContext();

        if (getArguments() != null){
            channel = (ChannelModel) getArguments().getSerializable(CHANNEL);
            typeName = channel.getFunName();
            channelCode = channel.getFunCode();
        }
        TLog.error(typeName + "");

        switch (typeName) {
            case "推荐"://推荐
                getBarData();
                getYouLikeData();
                break;
            case "地区"://地方
                getTvsByCityCode();
                getYouLikeData();
                break;
            case "电影"://电影,电视剧,综艺,体育
            case "电视剧":
            case "综艺":
            case "体育":
                getCateTag("1");
                getListByType();
                break;
            default://其他

        }

        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));
        videos.add(new RecomList.Videos("", 1, "", "dd", "dddd", "ddd", "dd", "dd", "dd"));

    }
    /*
     地方直播列表
     */
    private void addLocalListView() {
        topPageFl.setVisibility(View.VISIBLE);
        changeCityBtn.setVisibility(View.VISIBLE);
        setDataToTopView(localCites.get(0).getTvName());
        localCites.remove(0);
        if (localCites.size() == 0) return;
        View headView = LayoutInflater.from(context).inflate(R.layout.include_title_text, null);
        liveLv.addHeaderView(headView);

        liveAdapter = new LiveAdapter(context, localCites, R.layout.item_live_cate);
        liveLv.setAdapter(liveAdapter);
    }

    /**
     * 第一个布局图显示字体
     */
    private void setDataToTopView(String name) {
        tvTitle.setText(name);
    }


    /**
     * 添加bar滚动
     *
     * @param mList
     */
    private void addBarView(List<BannerList.Banner> mList) {
        banner_list.addAll(mList);
        imageIndicatorView.setVisibility(View.VISIBLE);
        imageIndicatorView.setupLayoutByURL(mList);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imageIndicatorView.show();
        imageIndicatorView.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
//                            changePage(mList.get(position),typeListBean.getId());
//                            changePage(mList.get(position).getId(),);
                //               getVideoDetails(mList.get(position).getOutid());
                // TODO: 16/6/29 跳转页面
            }
        });
    }

    /*
     *地方直播列表
     *先加载这个方法,以免recycleview加不了header
     */
    private void addLikeListView() {

        LinearLayoutManager layoutManager = null;
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (typeName.equals("推荐") || typeName.equals("地区")) {
            listAdapter = new LikeListAdapter(R.layout.layout_new_home_item, grid_list);
        } else {
            if (typeName.equals("电影")){
                layoutManager = new GridLayoutManager(getApplicationContext(),3);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }

            filmListAdapter = new FilmListAdapter(R.layout.layout_new_home_item, films);
        }
        recyclerView.setLayoutManager(layoutManager);
        header.attachTo(recyclerView);
        recyclerView.setAdapter(typeName.equals("推荐") || typeName.equals("地区") ? listAdapter : filmListAdapter);
    }

    /**
     * 获取bar滚动条
     */
    private void getBarData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", 1);
        HttpApis.getBanners(map, HttpApis.http_two, new HttpCallback<>(ResponseBanner.class, this));
    }

    /**
     * 获取兴趣列表
     */
    private void getYouLikeData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("numPerPage", "10");
        map.put("typeid", typeName.equals("推荐") || typeName.equals("地区")? "" : channel.getId());
        HttpApis.getYouLikeList(map, HttpApis.http_thr, new HttpCallback<>(ResponseLikeList.class, this));

    }

    /*
     *获取标签
     */
    private void getCateTag(String type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        HttpApis.getCategoryTag(map, HttpApis.http_one, new HttpCallback<>(ResponseCateTag.class, this));
    }

    /**
     * 获取电台列表
     */
    private void getTvsByCityCode() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "0571");
        HttpApis.getTvsByCityCode(map, HttpApis.http_for, new HttpCallback<>(ResponseLocalCityModel.class, this));
    }

    /**
     * 获取电影,电视剧列表
     */
    private void getListByType() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelCode", channelCode);
        map.put("pageNum", "1");
        map.put("numPerPage", "10");
        HttpApis.getListByType(map, HttpApis.http_fiv, new HttpCallback<>(ResponseFilms.class, this));
    }

    /**
     * 电影,电视剧分类gridview
     */
    private void addCateView() {
        if (cateTags.size() > 9) {//最多显示9个标签
            cateTags = cateTags.subList(0, 9);
            cateTags.add(new CateTagModel("更多", "", 1));
        }
        cateGv.setVisibility(View.VISIBLE);
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), cateTags, R.layout.item_first_cate);
        cateGv.setAdapter(gridAdapter);
        cateGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        cateGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), NewClassDetailPage.class));
            }
        });
    }


    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {//电影,电视剧标签
            case HttpApis.http_one:
                ResponseCateTag responseCateTag = (ResponseCateTag) value;
                cateTags = responseCateTag.getBody().getLx();
                if (cateTags == null || cateTags.size() == 0) return;
                addCateView();
                break;
            case HttpApis.http_two://获取bar
                ResponseBanner responseBar = (ResponseBanner) value;
                banner_list = responseBar.getBody().getBannerList();
                if (banner_list == null || banner_list.size() == 0) return;
                addBarView(banner_list);
                break;
            case HttpApis.http_thr://获取like列表
                ResponseLikeList re = (ResponseLikeList) value;
                grid_list = re.getBody().getLikeList();
                if (grid_list == null || grid_list.size() == 0) return;

                if (typeName.equals("推荐")) {
                    recommendImg.setVisibility(View.VISIBLE);
                }
                if (typeName.equals("地区")) {
                    titleRl.setVisibility(View.VISIBLE);
                }
                listAdapter.notifyDataChangedAfterLoadMore(grid_list, true);
                break;
            case HttpApis.http_for://获取城市电台
                ResponseLocalCityModel cityRe = (ResponseLocalCityModel) value;
                localCites = cityRe.getBody().getCitys();
                if (localCites == null || localCites.size() == 0) return;
                addLocalListView();
                break;
            case HttpApis.http_fiv:
                ResponseFilms filmRe = (ResponseFilms) value;
                films = filmRe.getBody().getTypeList();
                if (films == null || films.size() == 0) return;
                topPageFl.setVisibility(View.VISIBLE);
                setDataToTopView(films.get(0).getName());
                films.remove(0);
                if (films.size() == 0) return;
                filmListAdapter.notifyDataChangedAfterLoadMore(films, true);
                break;
        }
    }

}
