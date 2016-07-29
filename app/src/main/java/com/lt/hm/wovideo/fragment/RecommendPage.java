package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.CircleImageView;
import com.lt.hm.wovideo.widget.SpaceItemDecoration;
import com.lt.hm.wovideo.widget.indicatorView.AutoPlayManager;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/25
 */
public class RecommendPage extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/WoVideo/Portrait/";
    Unbinder unbinder;
    @BindView(R.id.img_indicator_vip)
    ImageIndicatorView imgIndicatorVip;
    @BindView(R.id.vip_person_logo)
    CircleImageView vipPersonLogo;
    @BindView(R.id.vip_person_account)
    TextView vipPersonAccount;
    @BindView(R.id.vip_person_vipicon)
    ImageView vipPersonVipicon;
    @BindView(R.id.vip_person_layout)
    RelativeLayout vipPersonLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;
    @BindView(R.id.recommend_container)
    LinearLayout recommend_container;
    List<BannerList.Banner> banner_list;
    AutoPlayManager autoPlayManager;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_recommend;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light
                , android.R.color.holo_red_light, android.R.color.holo_orange_light);
        swipe_refresh.setOnRefreshListener(this);
        vipPersonLayout.setOnClickListener((View v) -> {
            UIHelper.ToOpenVipPage(getActivity());
        });
        if (FILE_SAVEPATH != null) {
            Glide.with(this).load(ACache.get(getActivity()).getAsString("img_url")).centerCrop().error(R.drawable.icon_head).into(vipPersonLogo);
        }

//        String userinfo = ACache.get(getActivity()).getAsString("userinfo");
        String userinfo=  SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        if (!StringUtils.isNullOrEmpty(userinfo)){
            UserModel model= new Gson().fromJson(userinfo,UserModel.class);
            String phoneNum= model.getPhoneNo();
            vipPersonAccount.setText(phoneNum.substring(0, phoneNum.length() - (phoneNum.substring(3)).length()) + "****" + phoneNum.substring(7));
            if (model.getIsVip().equals("1")){
                vipPersonVipicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip_opened));
            }else{
                vipPersonVipicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip_unopened));
            }
            if (!StringUtils.isNullOrEmpty(model.getHeadImg())){
                Glide.with(this).load(HttpUtils.appendUrl(model.getHeadImg())).thumbnail(1f).into(vipPersonLogo);
            }else{
                vipPersonLogo.setImageDrawable(getResources().getDrawable(R.drawable.icon_head));
            }

        }else{
            vipPersonAccount.setText(getResources().getText(R.string.unlogin_hint));
            vipPersonVipicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_vip_unopened));
        }

    }

    @Override
    public void initData() {
        super.initData();
        getBannerDatas();
        getHotRecommDatas();
    }

    private void getHotRecommDatas() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("pageNum", 1);
        maps.put("numPerPage", 10);
        HttpApis.getHotRecomm(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<RecomList, RespHeader> resp = new ResponseObj<RecomList, RespHeader>();
                ResponseParser.parse(resp, response, RecomList.class, RespHeader.class);
                TLog.log(resp.toString());
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    List<RecomList.Videos> grid_list = new ArrayList<RecomList.Videos>();
                    grid_list.addAll(resp.getBody().getRecList());
                    RecomList.Videos typeListBean = new RecomList.Videos();
                    if (grid_list.size()>0){
                        recommend_container.removeAllViews();
                    }
                    typeListBean = grid_list.get(0);
                    List<RecomList.Videos> beanList = new ArrayList<RecomList.Videos>();
                    for (int i = 0; i < 3; i++) {
                        beanList.add(grid_list.get(i+1));
                    }
                    recommend_container.addView(addHViews(typeListBean));
                    recommend_container.addView(addGridViews(beanList));
                    recommend_container.addView(addHViews(grid_list.get(grid_list.size()-1)));
                    recommend_container.invalidate();
                }
            }
        });
    }


    private void getBannerDatas() {
        banner_list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", 1);
        HttpApis.getBanners(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<BannerList, RespHeader> resp = new ResponseObj<BannerList, RespHeader>();
                ResponseParser.parse(resp, response, BannerList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    TLog.log(resp.toString());
                    List<BannerList.Banner> mList = resp.getBody().getBannerList();
                    banner_list.addAll(mList);
                    imgIndicatorVip.setupLayoutByURL(mList);
                    imgIndicatorVip.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
                    imgIndicatorVip.show();
                    imgIndicatorVip.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
//                            changePage(mList.get(position),typeListBean.getId());
//                            changePage(mList.get(position).getId(),);
                            getVideoDetails(mList.get(position).getOutid());
                            // TODO: 16/6/29 跳转页面
                        }
                    });
                    loopIndicatorView();
                } else {
                    // TODO: 16/6/13 提示 取数异常等信息 获取缓存数据并 显示
                }

            }
        });
    }

    private void loopIndicatorView() {
        autoPlayManager = new AutoPlayManager(imgIndicatorVip);
        autoPlayManager.setBroadcastEnable(true);
        autoPlayManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoPlayManager.loop();
    }

    private View addHViews(RecomList.Videos typeListBean) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_home_item, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.s_200));
        view.setLayoutParams(params);
        ImageView bg = (ImageView) view.findViewById(R.id.home_item_img_bg);
        Glide.with(getActivity()).load(HttpUtils.appendUrl(typeListBean.getHimg())).placeholder(R.drawable.default_horizental).crossFade().into(bg);
        ((TextView) view.findViewById(R.id.home_item_title)).setText(typeListBean.getName());
        ((TextView) view.findViewById(R.id.home_item_desc)).setText(typeListBean.getDes());
        ((ImageView) view.findViewById(R.id.item_vip_logo)).setVisibility(View.VISIBLE);
        view.setOnClickListener((View v) -> {
            // 跳转视频详情页面
            changePage(Integer.parseInt(typeListBean.getTypeId()), typeListBean.getVfId());
        });

        return view;
    }

    private void changePage(int typeId, String vfId) {
        if (typeId == VideoType.MOVIE.getId()) {
            // TODO: 16/6/14 跳转电影页面
            Bundle bundle = new Bundle();
            bundle.putString("id", vfId);
            bundle.putInt("typeId",VideoType.MOVIE.getId());
            UIHelper.ToMoviePage(getActivity(), bundle);
        } else if (typeId == VideoType.TELEPLAY.getId()) {
            // TODO: 16/6/14 跳转电视剧页面
            Bundle bundle = new Bundle();
            bundle.putString("id", vfId);
            bundle.putInt("typeId",VideoType.TELEPLAY.getId());
            UIHelper.ToDemandPage(getActivity(), bundle);

        } else if (typeId == VideoType.SPORTS.getId()) {
            // TODO: 16/6/14 跳转 体育播放页面
            Bundle bundle = new Bundle();
            bundle.putString("id", vfId);
            bundle.putInt("typeId",VideoType.SPORTS.getId());
            UIHelper.ToDemandPage(getActivity(), bundle);
        } else if (typeId == VideoType.VARIATY.getId()) {
            // TODO: 16/6/14 跳转综艺界面
            Bundle bundle = new Bundle();
            bundle.putString("id", vfId);
            bundle.putInt("typeId",VideoType.VARIATY.getId());
            UIHelper.ToDemandPage(getActivity(), bundle);
        }
    }

    private View addGridViews(List<RecomList.Videos> b_list) {
        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams l_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l_params.setMargins(0, 10, 0, 10);
        layout.setLayoutParams(l_params);
        RecyclerView recyclerView = new RecyclerView(getActivity());
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.s_200));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.divider_width);

        recyclerView.setLayoutParams(params);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        Bottom_ListAdapter grid_adapter = new Bottom_ListAdapter(getActivity(),R.layout.layout_home_item, b_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        recyclerView.setAdapter(grid_adapter);
        grid_adapter.notifyDataSetChanged();
        grid_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
//                getChangePage(grid_list.get(i).getId());
                // TODO: 16/6/28 判断跳转页面
                changePage(Integer.parseInt(b_list.get(i).getTypeId()), b_list.get(i).getVfId());
            }
        });
        layout.addView(recyclerView);
        return layout;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                swipe_refresh.setRefreshing(false);
            }
        }, 2000);
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

                    if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putInt("typeId",VideoType.MOVIE.getId());
                        UIHelper.ToMoviePage(getActivity(), bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putInt("typeId",VideoType.TELEPLAY.getId());
                        UIHelper.ToDemandPage(getActivity(), bundle);

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putInt("typeId",VideoType.SPORTS.getId());
                        UIHelper.ToDemandPage(getActivity(), bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putInt("typeId",VideoType.VARIATY.getId());
                        UIHelper.ToDemandPage(getActivity(), bundle);
                    }
                }
            }
        });
    }


}
