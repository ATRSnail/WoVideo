package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.vip.GridViewAapter;
import com.lt.hm.wovideo.adapter.vip.ListViewAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseBanner;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.CircleImageView;
import com.lt.hm.wovideo.widget.CustomGridView;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.TopTileView;
import com.lt.hm.wovideo.widget.indicatorView.AutoPlayManager;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by xuchunhui on 16/8/18.
 */
public class VipRecommendFrg extends BaseFragment {
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/WoVideo/Portrait/";
    private View view;
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
    @BindView(R.id.gv_movie)
    CustomGridView movieLv;
    @BindView(R.id.elv_teleplay)
    CustomListView teleLv;
    @BindView(R.id.elv_variaty)
    CustomListView varLv;
    @BindView(R.id.rl_title)
    View rl_title;
    @BindView(R.id.tv_right)
    TextView rightTv;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private GridViewAapter movieAdapter;
    private ListViewAdapter teleAdapter;
    private ListViewAdapter varAdapter;
    private AutoPlayManager autoPlayManager;
    private List<BannerList.Banner> banner_list = new ArrayList<>();
    private List<LikeModel> movieList = new ArrayList<>();//猜你喜欢
    private List<LikeModel> teleList = new ArrayList<>();//猜你喜欢
    private List<LikeModel> varlist = new ArrayList<>();//猜你喜欢

    private LayoutInflater inflater;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
    }

    private LikeModel likemodel;

    @Override
    public void initView(View view) {
        inflater = LayoutInflater.from(getContext());
        vipPersonLayout.setOnClickListener((View v) -> {
            UIHelper.ToOpenVipPage(getActivity());
        });
        if (FILE_SAVEPATH != null) {
            Glide.with(this).load(ACache.get(getActivity()).getAsString("img_url")).centerCrop().error(R.drawable.icon_head).into(vipPersonLogo);
        }

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

        movieAdapter = new GridViewAapter(getContext(), movieList, R.layout.layout_new_home_movie_item);
        teleAdapter = new ListViewAdapter(getContext(), teleList, R.layout.layout_new_home_item);
        varAdapter = new ListViewAdapter(getContext(), varlist, R.layout.layout_new_home_item);
        rl_title.setVisibility(View.VISIBLE);
        rightTv.setText("换一换");
        tv_title.setText("热门电影");
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_change),null);
        rightTv.setCompoundDrawablePadding(ScreenUtils.dp2px(getContext(),10));
        TopTileView teleHeadView = new TopTileView(getContext());
        teleHeadView.setTitleTv("热播剧");
        teleHeadView.setImageVisiable(true);
        teleHeadView.setImage(R.drawable.icon_change);
        teleLv.addHeaderView(teleHeadView);
        TopTileView varHeadView = new TopTileView(getContext());
        varHeadView.setTitleTv("热播综艺");
        varHeadView.setImageVisiable(true);
        varHeadView.setImage(R.drawable.icon_change);
        varLv.addHeaderView(varHeadView);
        movieLv.setAdapter(movieAdapter);
        teleLv.setAdapter(teleAdapter);
        varLv.setAdapter(varAdapter);

        rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYouLikeData1();
            }
        });
        teleHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYouLikeData2();
            }
        });
        varHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYouLikeData3();
            }
        });
        movieLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                likemodel = movieList.get(position);
                // 跳转视频详情页面
                changePage(likemodel.getTypeId(), likemodel.getId());

            }
        });
        teleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                likemodel = teleList.get(position-1);
                // 跳转视频详情页面
                changePage(likemodel.getTypeId(), likemodel.getId());
            }
        });
        varLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                likemodel = varlist.get(position-1);
                // 跳转视频详情页面
                changePage(likemodel.getTypeId(), likemodel.getId());

            }
        });
    }

    private void changePage(int typeId, String vfId) {
        UIHelper.ToAllCateVideo(getActivity(), typeId, vfId);
    }

    @Override
    public void initData() {
        getBannerDatas();
        getYouLikeData1();
        getYouLikeData2();
        getYouLikeData3();
    }

    private void getBannerDatas() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", 1);
        HttpApis.getBanners(map, HttpApis.http_one, new HttpCallback<>(ResponseBanner.class, this));
    }

    /**
     * 获取兴趣列表
     */
    private void getYouLikeData1() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", 6);
        map.put("typeid", 1);
        HttpApis.getYouLikeList(map, HttpApis.http_two, new HttpCallback<>(ResponseLikeList.class, this));
    }

    private void getYouLikeData2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", 3);
        map.put("typeid", 2);
        HttpApis.getYouLikeList(map, HttpApis.http_thr, new HttpCallback<>(ResponseLikeList.class, this));
    }

    private void getYouLikeData3() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", 3);
        map.put("typeid", 3);
        HttpApis.getYouLikeList(map, HttpApis.http_for, new HttpCallback<>(ResponseLikeList.class, this));
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        switch (flag) {
            case HttpApis.http_one:
                ResponseBanner responseBar = (ResponseBanner) value;
                banner_list = responseBar.getBody().getBannerList();
                if (banner_list == null || banner_list.size() == 0) return;
//                addBarView(banner_list);
                addBarView(banner_list);
                break;
            case HttpApis.http_two:
                ResponseLikeList re = (ResponseLikeList) value;
                movieList = re.getBody().getLikeList();
                if (movieList == null || movieList.size() == 0) return;
                movieAdapter.notityData(movieList);
                break;
            case HttpApis.http_thr:
                ResponseLikeList tele = (ResponseLikeList) value;
                teleList = tele.getBody().getLikeList();
                if (teleList == null || teleList.size() == 0) return;
                teleAdapter.notityData(teleList);
                break;
            case HttpApis.http_for:
                ResponseLikeList var = (ResponseLikeList) value;
                varlist = var.getBody().getLikeList();
                if (varlist == null || varlist.size() == 0) return;
                varAdapter.notityData(varlist);
                break;
        }
    }

    /**
     * 添加bar滚动
     *
     * @param mList
     */
    private void addBarView(List<BannerList.Banner> mList) {
        banner_list.addAll(mList);
        imgIndicatorVip.setVisibility(View.VISIBLE);
        imgIndicatorVip.setupLayoutByClass(mList);
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

    private void loopIndicatorView() {
        autoPlayManager = new AutoPlayManager(imgIndicatorVip);
        autoPlayManager.setBroadcastEnable(true);
        autoPlayManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoPlayManager.loop();
    }
}
