package com.lt.hm.wovideo.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.lt.hm.wovideo.base.BaseRequestActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.interf.OnPlaceChangeListener;
import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.interf.onChangeLister;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.CateTagListModel;
import com.lt.hm.wovideo.model.CateTagModel;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.City;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.LocalCityModel;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.response.ResponseBanner;
import com.lt.hm.wovideo.model.response.ResponseCateTag;
import com.lt.hm.wovideo.model.response.ResponseFilms;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.model.response.ResponseLocalCityModel;
import com.lt.hm.wovideo.ui.CityListPage;
import com.lt.hm.wovideo.ui.LivePage;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UpdateLocationMsg;
import com.lt.hm.wovideo.utils.ViewPageChangeMsg;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;
import com.lt.hm.wovideo.widget.CustomGridView;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.CustomScrollView;
import com.lt.hm.wovideo.widget.DividerDecoration;
import com.lt.hm.wovideo.widget.FastScrollView;
import com.lt.hm.wovideo.widget.TopTileView;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class CommonTypePage extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener,OnUpdateLocationListener,onChangeLister {

    public static final String CHANNEL = "channel";
    private View view;
    private String channelCode;
    private ChannelModel channel;
    private String cityCode;
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
    @BindView(R.id.scroll_view)
    FastScrollView scrollView;
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
    @BindView(R.id.line_tv)
    TextView tvLine;
    @BindView(R.id.item_type)
    TextView tvType;
    @BindView(R.id.item_desc)
    TextView tvDesc;

    @BindView(R.id.rl_title)
    View titleRl;
    @BindView(R.id.lv_live)
    CustomListView liveLv;
//    @BindView(R.id.banner)
//    BannerLayout bannerLayout;

    private boolean isOnline = true;
    private LiveAdapter liveAdapter;
    private Context context;
    private boolean isHasView = false;//防止重复加载view
    Unbinder unbinder;

    private LinearLayoutManager layoutManager;

    private boolean isLoading = false;//防止scrollview滚动多次请求数据
    private boolean isNoData = false;//数据加载完了
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
            unbinder = ButterKnife.bind(this, view);
            UpdateLocationMsg.getInstance().addRegisterSucListeners(this);
            ViewPageChangeMsg.getInstance().addRegisterSucListeners(this);
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
        addLikeListView();

        mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        scrollView.setOnBorderListener(new CustomScrollView.OnBorderListener() {
            @Override
            public void onBottom() {
                if (isLoading || isNoData) return;
                isLoading = true;
                TLog.error("上拉加载----");

                switch (channelCode) {
                    case ChannelModel.RECOMMEND_ID://推荐
                    case ChannelModel.LOCAL_ID://地方
                        getYouLikeData();
                        break;
                    case ChannelModel.FILM_ID://电影
                    case ChannelModel.TELEPLAY_ID://电视剧
                    case ChannelModel.SPORTS_ID://体育
                    case ChannelModel.VARIATY_ID://综艺
                    default://其他
                        getListByType();
                }
            }

            @Override
            public void onTop() {
                TLog.error("下拉加载--");
            }
        });

        changeCityBtn.setOnClickListener((View v) -> {
            startActivityForResult(new Intent(getActivity(), CityListPage.class), 99);
        });
        topPageFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (channelCode) {
                    case ChannelModel.LOCAL_ID://地方
                        if (localCityModel != null)
                        ToLivePage(localCityModel.getUrl(),localCityModel.getTvName());
                        break;
                    case ChannelModel.FILM_ID://电影
                    case ChannelModel.TELEPLAY_ID://电视剧
                    case ChannelModel.SPORTS_ID://体育
                    case ChannelModel.VARIATY_ID://综艺
                        default:
                        // 跳转视频详情页面
                        if (filmMode != null)
                        changePage(filmMode.getTypeId(), filmMode.getVfinfo_id());
                        break;
                }
            }
        });
    }

    /**
     * 获取bundle值
     */
    private void initBundleData() {
        context = getApplicationContext();
        Bundle bundle = getArguments();
        if (bundle != null) {
            channel = (ChannelModel) bundle.getSerializable(CHANNEL);
            channelCode = channel.getFunCode();
        }
        TLog.error(channelCode + "");
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        pageNum = 1;
        isNoData = false;
        seed = "";
        switch (channelCode) {
            case ChannelModel.RECOMMEND_ID://推荐
                grid_list.clear();
                if (banner_list.size() == 0) {
                    getBarData();
                }
                getYouLikeData();
                break;
            case ChannelModel.LOCAL_ID://地方
                localCites.clear();
                grid_list.clear();
                getTvsByCityCode();
                getYouLikeData();
                break;
            case ChannelModel.FILM_ID://电影
                films.clear();
                if (cateTags.size() == 0) {
                    getCateTag("1");
                }
                getListByType();
                break;
            case ChannelModel.TELEPLAY_ID://电视剧
                if (cateTags.size() == 0) {
                    getCateTag("2");
                }
                getListByType();
                break;
            case ChannelModel.SPORTS_ID://体育
                if (cateTags.size() == 0) {
                    getCateTag("4");
                }
                getListByType();
                break;
            case ChannelModel.VARIATY_ID://综艺
                if (cateTags.size() == 0) {
                    getCateTag("3");
                }
                getListByType();
                break;
            default://其他
                getListByType();
        }
    }

    /*
     地方直播列表
     */
    private LocalCityModel localCityModel;

    private void addLocalListView() {
        liveLv.setVisibility(VISIBLE);
        topPageFl.setVisibility(View.VISIBLE);
        changeCityBtn.setVisibility(View.VISIBLE);
        localCityModel = localCites.get(0);
        setDataToTopView(localCityModel.getTvName(), "", localCityModel.getNowPro(), localCityModel.getImg());
        localCites.remove(0);
        if (localCites.size() == 0) return;
        liveAdapter.notifyView(localCites);
        liveLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToLivePage(localCites.get(position-1).getUrl(),localCites.get(position-1).getTvName());
            }
        });
    }

    private void ToLivePage(String url,String name){
        Bundle bundle = new Bundle();
        bundle.putString(LivePage.PLAY_URL,url);
        bundle.putString(LivePage.PLAY_NAME,name);
        UIHelper.ToLivePage(getActivity(),bundle);
    }

    /**
     * 第一个布局图显示字体
     */
    private void setDataToTopView(String name, String typeStr, String descStr, String imgUrl) {

        tvTitle.setText(name);
        ImageLoaderUtil.getInstance().loadImage(context, new ImageLoader.Builder().imgView(imgTopPage).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(imgUrl)).build());
        if (TextUtils.isEmpty(typeStr)){
            tvDesc.setVisibility(GONE);
            tvLine.setVisibility(GONE);
            tvType.setCompoundDrawables(null,null,null,null);
        }
        tvType.setText("正在播放:"+descStr);
        tvDesc.setText(descStr);

    }


    /**
     * 添加bar滚动条
     *
     * @param mList
     */
//    private void addBannerView(List<BannerList.Banner> mList) {
//        bannerLayout.setVisibility(View.VISIBLE);
//        List<String> strings = new ArrayList<>();
//        for (int i = 0; i < mList.size(); i++) {
//            strings.add(HttpUtils.appendUrl(mList.get(i).getImg()));
//        }
//        bannerLayout.setViewUrls(strings);
//        //添加点击监听
//        bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // TODO: 8/13/16 跳转页面
//                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 添加bar滚动
     *
     * @param mList
     */
    private void addBarView(List<BannerList.Banner> mList) {
        banner_list.addAll(mList);
        imageIndicatorView.setVisibility(View.VISIBLE);
        imageIndicatorView.setupLayoutByClass(mList);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imageIndicatorView.show();
        imageIndicatorView.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
//                            changePage(mList.get(position),typeListBean.getId());
//                            changePage(mList.get(position).getId(),);
                getVideoDetails(mList.get(position).getOutid());
                // TODO: 16/6/29 跳转页面
            }
        });
        imageIndicatorView.setOnRefrshViewEnable(this);

    }

    /*
     *地方直播列表
     *先加载这个方法,以免recycleview加不了header
     */
    private boolean isNotFilm = true;
    private  FilmMode filmmode;
    private LikeModel likemodel;

    private void addLikeListView() {

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (channelCode .equals( ChannelModel.RECOMMEND_ID) || channelCode .equals( ChannelModel.LOCAL_ID)) {
            listAdapter = new LikeListAdapter(R.layout.layout_new_home_item, grid_list);
            listAdapter.setOnRecyclerViewItemClickListener((view1, i) -> {
                likemodel = (LikeModel) listAdapter.getData().get(i);
                // 跳转视频详情页面
                changePage(likemodel.getTypeId(), likemodel.getId());
            });
        } else {
            if (channelCode.equals(ChannelModel.FILM_ID)) {
                isNotFilm = false;
                layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            }
            listAdapter = new FilmListAdapter(R.layout.layout_new_home_item, films, isNotFilm);
            listAdapter.setOnRecyclerViewItemClickListener((view1, i) -> {
                filmmode = (FilmMode)listAdapter.getData().get(i);
                // 跳转视频详情页面
                changePage(filmmode.getTypeId(), filmmode.getVfinfo_id());
            });
        }
        mRecyclerView.setLayoutManager(layoutManager);
        header.attachTo(mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerDecoration(context, 2, getResources().getColor(R.color.white)));
        mRecyclerView.setAdapter(listAdapter);


        TopTileView headView = new TopTileView(context);
        headView.setTitleTv("北京直播");
        headView.setImageVisiable(true);
        liveLv.addHeaderView(headView);

        liveAdapter = new LiveAdapter(context, localCites, R.layout.item_live_cate);
        liveLv.setAdapter(liveAdapter);
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
        map.put("typeid", channelCode .equals( ChannelModel.RECOMMEND_ID) || channelCode .equals( ChannelModel.LOCAL_ID) ? "" : channelCode);
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
        map.put("code", TextUtils.isEmpty(cityCode) ? "010" : cityCode);
        HttpApis.getTvsByCityCode(map, HttpApis.http_for, new HttpCallback<>(ResponseLocalCityModel.class, this));
    }

    /**
     * 获取电影,电视剧列表
     */
    private void getListByType() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelCode", channelCode);
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage);
        HttpApis.getListByType(map, HttpApis.http_fiv, new HttpCallback<>(ResponseFilms.class, this));
    }

    /**
     * 电影,电视剧分类gridview
     */
    private void addCateView() {
        if (cateTags.size() > 9) {//最多显示9个标签
            cateTags = cateTags.subList(0, 9);
            cateTags.add(new CateTagModel("更多", -1));
        }
        cateGv.setVisibility(View.VISIBLE);
        GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), cateTags, R.layout.item_first_cate);
        cateGv.setAdapter(gridAdapter);
        cateGv.setNumColumns(5);
        cateGv.setPadding(20, 15, 20, 15);
        cateGv.setGravity(Gravity.CENTER);
        cateGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        cateGv.setOnItemClickListener((parent, view1, position, id) -> NewClassDetailPage.getInstance(getActivity(), cateTags.get(position), channelCode, cateTagListModel));
    }

    private CateTagListModel cateTagListModel;
    private FilmMode filmMode;

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {//电影,电视剧标签
            case HttpApis.http_one:
                ResponseCateTag responseCateTag = (ResponseCateTag) value;
                cateTags = responseCateTag.getBody().getLx();
                cateTagListModel = responseCateTag.getBody();
                if (cateTags == null || cateTags.size() == 0) return;
                addCateView();
                break;
            case HttpApis.http_two://获取bar
                ResponseBanner responseBar = (ResponseBanner) value;
                banner_list = responseBar.getBody().getBannerList();
                if (banner_list == null || banner_list.size() == 0) return;
//                addBarView(banner_list);
                addBarView(banner_list);
                break;
            case HttpApis.http_thr://获取like列表
                ResponseLikeList re = (ResponseLikeList) value;
                seed = re.getBody().getSeed();
                grid_list = re.getBody().getLikeList();
                if (grid_list == null || grid_list.size() == 0) {
                    isNoData = true;
                    UT.showNormal("暂无数据");
                    return;
                }

                if (channelCode .equals( ChannelModel.RECOMMEND_ID)) {
                    recommendImg.setVisibility(View.VISIBLE);
                }
                if (channelCode .equals( ChannelModel.LOCAL_ID)) {
                    titleRl.setVisibility(View.VISIBLE);
                }
                if (pageNum == 1) {
                    listAdapter.setNewData(grid_list);
                } else {
                    listAdapter.notifyDataChangedAfterLoadMore(grid_list, true);
                }
                pageNum++;
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
                filmMode = films.get(0);
                setDataToTopView(filmMode.getName(), filmMode.getTypeName(), filmMode.getHit(), filmMode.gethImg());
                films.remove(0);
                if (films.size() == 0) {
                    isNoData = true;
                    UT.showNormal("暂无数据");
                    return;
                }
                if (pageNum == 1) {
                    listAdapter.setNewData(films);
                } else {
                    listAdapter.notifyDataChangedAfterLoadMore(films, true);
                }
                pageNum++;
                break;
        }
    }

    @Override
    public void onAfter(int flag) {
        super.onAfter(flag);
        switch (flag) {
            case HttpApis.http_thr:
            case HttpApis.http_fiv:
                isLoading = false;
                if (mSwipeRefreshWidget != null && mSwipeRefreshWidget.isRefreshing()) {
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
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private OnPlaceChangeListener onPlaceChangeListener;

    public void setOnPlaceChangeListener(OnPlaceChangeListener listener) {
        this.onPlaceChangeListener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CityListPage.CITY_RESULT:
                cityCode = data.getStringExtra("city");
                if (onPlaceChangeListener != null) {
                    onPlaceChangeListener.onChangePlaceListener(cityCode.replaceAll("[^\u4E00-\u9FA5]", ""));
                }
                TLog.error("city-code-" + submit(cityCode));
                cityCode = submit(cityCode);
                initData();
                break;
        }
    }

    public String submit(String string) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.replaceAll("").trim();
    }

    public void getVideoDetails(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        // TODO: 16/6/26 获取app typeId 并填充
        String typeID = null;
        maps.put("typeid", typeID);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    changePage(resp.getBody().getVfinfo().getTypeId(),vfId);
                }
            }
        });
    }

    private void changePage(int typeId, String vfId) {
       UIHelper.ToAllCateVideo(getActivity(),typeId,vfId);
    }

    @Override
    public void onUpdateLocListener(String name, String code) {
        cityCode = code;
    }

    @Override
    public void onDestroyView() {
        UpdateLocationMsg.getInstance().removeRegisterSucListeners(this);
        ViewPageChangeMsg.getInstance().removeRegisterSucListeners(this);
        super.onDestroyView();
    }

    @Override
    public void onChangeLister(boolean isEnable) {
        TLog.error("viewpage---isscroll---"+isEnable);
        mSwipeRefreshWidget.setEnabled(isEnable);
    }
}
