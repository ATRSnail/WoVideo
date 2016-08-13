package com.lt.hm.wovideo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.FilmListAdapter;
import com.lt.hm.wovideo.adapter.home.LikeListAdapter;
import com.lt.hm.wovideo.adapter.recommend.GridAdapter;
import com.lt.hm.wovideo.adapter.recommend.LiveAdapter;
import com.lt.hm.wovideo.base.BaseLazyFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.CateTagModel;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.model.LikeModel;
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
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;
import com.lt.hm.wovideo.widget.CustomGridView;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.CustomScrollView;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.yyydjk.library.BannerLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class CommonTypePage extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String CHANNEL = "channel";
    private View view;
    private int channelId;
    private String channelCode;
    private ChannelModel channel;
    private List<CateTagModel> cateTags = new ArrayList<>();//标签
    private List<LocalCityModel> localCites = new ArrayList<>();
    private List<BannerList.Banner> banner_list = new ArrayList<>();//bar
    private List<LikeModel> grid_list = new ArrayList<LikeModel>();//猜你喜欢
    private List<FilmMode> films = new ArrayList<>();//电影电视剧列表
    private BaseQuickAdapter listAdapter;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshWidget;
    @BindView(R.id.header)
    RecyclerViewHeader header;
    @BindView(R.id.img_indicator_vip)
    ImageIndicatorView imageIndicatorView;
    @BindView(R.id.recycler_recommend)
    RecyclerView mRecyclerView;
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
    @BindView(R.id.banner)
    BannerLayout bannerLayout;

    private boolean isOnline = true;
    private LiveAdapter liveAdapter;
    private Context context;
    private boolean isHasView = false;//防止重复加载view
    Unbinder unbinder;

    private int lastVisibleItem;
    private  LinearLayoutManager layoutManager;

    private int pageNum = 1;//页码
    private int numPerPage = 10;//每页条数
    private String seed;//翻页查询种子，这个参数如果不传会随机查询，然后会返回这个值，在翻页的时候要将这个值传入，否则会出现重复推荐，可选
    private String tag;//兴趣标签，用户的user信息,可选
    private String typeId;//视频类型，1电影2电视剧3综艺4体育，可选

    public static CommonTypePage getInstance(ChannelModel channel) {
        CommonTypePage common = new CommonTypePage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHANNEL, channel);
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
            unbinder= ButterKnife.bind(this, view);
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
        initBundleData();
        initData();
        initView(null);
    }

    @Override
    public void initView(View view) {
        titleRl.setVisibility(View.GONE);
        addLikeListView();

        mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                TLog.error("上拉加载--"+lastVisibleItem+"---"+listAdapter.getItemCount());
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 == listAdapter.getItemCount()) {
                    mSwipeRefreshWidget.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    TLog.error("上拉加载----page-->"+pageNum+"----seed--->"+seed);
                    getYouLikeData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        changeCityBtn.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), CityListPage.class));
        });
    }

    /**
     * 获取bundle值
     */
    private void initBundleData(){
        context = getApplicationContext();
         Bundle bundle = getArguments();
        if (bundle != null) {
            channel = (ChannelModel) bundle.getSerializable(CHANNEL);
            channelId = channel.getId();
            channelCode = channel.getFunCode();
        }
        TLog.error(channelId + "");
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        switch (channelId) {
            case ChannelModel.RECOMMEND_ID://推荐
                pageNum = 1;
                seed = "";
                grid_list.clear();
                banner_list.clear();
                getBarData();
                getYouLikeData();
                break;
            case ChannelModel.LOCAL_ID://地方
                getTvsByCityCode();
                getYouLikeData();
                break;
            case ChannelModel.FILM_ID://电影,电视剧,综艺,体育
                getCateTag("1");
                getListByType();
                break;
            case ChannelModel.TELEPLAY_ID://电视剧
                getCateTag("2");
                getListByType();
                break;
            case ChannelModel.SPORTS_ID://体育
                getCateTag("4");
                getListByType();
                break;
            case ChannelModel.VARIATY_ID://综艺
                getCateTag("3");
                getListByType();
                break;
            default://其他

        }
    }


    /*
     地方直播列表
     */
    private LocalCityModel localCityModel;

    private void addLocalListView() {
        topPageFl.setVisibility(View.VISIBLE);
        changeCityBtn.setVisibility(View.VISIBLE);
        localCityModel = localCites.get(0);
        setDataToTopView(localCityModel.getTvName(), localCityModel.getProperty(), localCityModel.getNowPro(), localCityModel.getImg());
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
    private void setDataToTopView(String name, String typeStr, String descStr, String imgUrl) {
        tvTitle.setText(name);
        ImageLoaderUtil.getInstance().loadImage(context, new ImageLoader.Builder().imgView(imgTopPage).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(imgUrl)).build());
        tvType.setText(typeStr);
        tvDesc.setText(descStr);
    }


    private void addBannerView(List<BannerList.Banner> mList)
    {
        bannerLayout.setVisibility(View.VISIBLE);
        List<String> strings= new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            strings.add(HttpUtils.appendUrl(mList.get(i).getImg()));
        }
        bannerLayout.setViewUrls(strings);
        //添加点击监听
        bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: 8/13/16 跳转页面
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
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

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (channelId == ChannelModel.RECOMMEND_ID || channelId == ChannelModel.LOCAL_ID) {
            listAdapter = new LikeListAdapter(R.layout.layout_new_home_item, grid_list);
        } else {
            if (channelId == ChannelModel.FILM_ID) {
                layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }

            listAdapter = new FilmListAdapter(R.layout.layout_new_home_item, films);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        header.attachTo(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);
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
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage);
        if (!TextUtils.isEmpty(seed)) map.put("seed", seed);
        if (!TextUtils.isEmpty(tag)) map.put("tag", tag);
        map.put("typeid", channelId == ChannelModel.RECOMMEND_ID || channelId == ChannelModel.LOCAL_ID ? "" : channelId);
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
//                addBarView(banner_list);
                addBannerView(banner_list);
                break;
            case HttpApis.http_thr://获取like列表
                ResponseLikeList re = (ResponseLikeList) value;
                seed = re.getBody().getSeed();
                grid_list = re.getBody().getLikeList();
                if (grid_list == null || grid_list.size() == 0) return;

                if (channelId == ChannelModel.RECOMMEND_ID) {
                    recommendImg.setVisibility(View.VISIBLE);
                }
                if (channelId == ChannelModel.LOCAL_ID) {
                    titleRl.setVisibility(View.VISIBLE);
                }
                if (pageNum == 1){
                    listAdapter.setNewData(grid_list);
                }else {
                    listAdapter.notifyDataChangedAfterLoadMore(grid_list,true);
                }
                pageNum = 1 ;
                break;
            case HttpApis.http_for://获取城市电台
                ResponseLocalCityModel cityRe = (ResponseLocalCityModel) value;
                localCites = cityRe.getBody().getCitys();
                if (localCites == null || localCites.size() == 0) return;
                localCites.addAll(localCites);
                addLocalListView();
                break;
            case HttpApis.http_fiv:
                ResponseFilms filmRe = (ResponseFilms) value;
                films = filmRe.getBody().getTypeList();
                if (films == null || films.size() == 0) return;
                topPageFl.setVisibility(View.VISIBLE);
                FilmMode filmMode = films.get(0);
                setDataToTopView(filmMode.getName(), filmMode.getTypeName(), filmMode.getDq(), filmMode.getImg());
                films.remove(0);
                if (films.size() == 0) return;
                listAdapter.notifyDataChangedAfterLoadMore(films, true);
                break;
        }
    }

    @Override
    public void onAfter(int flag) {
        super.onAfter( flag);
        switch (flag){
            case HttpApis.http_thr:
                if (mSwipeRefreshWidget != null && mSwipeRefreshWidget.isRefreshing()){
                    mSwipeRefreshWidget.setRefreshing(false);
                }
                break;
        }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
