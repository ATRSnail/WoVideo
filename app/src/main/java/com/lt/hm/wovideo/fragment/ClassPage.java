package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.TypeList;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
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
 * @create_date 16/5/27
 */
@Deprecated
public class ClassPage extends BaseFragment {
    @BindView(R.id.img_movie)
    ImageView imgMovie;
    @BindView(R.id.movie_layout)
    FrameLayout movieLayout;
    @BindView(R.id.img_sport)
    ImageView imgSport;
    @BindView(R.id.sport_layout)
    FrameLayout sportLayout;
    @BindView(R.id.img_live)
    ImageView imgLive;
    @BindView(R.id.live_layout)
    FrameLayout liveLayout;
    @BindView(R.id.img_teleplay)
    ImageView imgTeleplay;
    @BindView(R.id.teleplay_layout)
    FrameLayout teleplayLayout;
    @BindView(R.id.img_variety)
    ImageView imgVariety;
    @BindView(R.id.variety_layout)
    FrameLayout varietyLayout;
    Unbinder unbinder;
    List<TypeList.TypeListBean> mList;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view==null){
            view = inflater.inflate(R.layout.layout_sort, container, false);
            unbinder=ButterKnife.bind(this, view);
            initView(view);
            initData();
        }
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mList = new ArrayList<>();
        movieLayout.setOnClickListener((View v)->{
            Bundle bundle= new Bundle();
            bundle.putString("key","电影");
            bundle.putString("id",getIdsByName("电影"));
            UIHelper.ToClassDetailsPage(ClassPage.this.getActivity(),bundle);
        });
        sportLayout.setOnClickListener((View v)->{
            Bundle bundle= new Bundle();
            bundle.putString("key","体育");
            bundle.putString("id",getIdsByName("体育"));
            UIHelper.ToClassDetailsPage(ClassPage.this.getActivity(),bundle);
        });
        //直播
        liveLayout.setOnClickListener((View v)->{
            UIHelper.ToLivePage(getActivity());
        });
        teleplayLayout.setOnClickListener((View v)->{
            Bundle bundle= new Bundle();
            bundle.putString("key","电视剧");
            bundle.putString("id",getIdsByName("电视剧"));
            UIHelper.ToClassDetailsPage(ClassPage.this.getActivity(),bundle);
        });
        varietyLayout.setOnClickListener((View v)->{
            Bundle bundle= new Bundle();
            bundle.putString("key","综艺");
            bundle.putString("id",getIdsByName("综艺"));
            UIHelper.ToClassDetailsPage(ClassPage.this.getActivity(),bundle);
        });
    }

    private String getIdsByName(String name) {
        for (int i = 0; i < mList.size(); i++) {
            TypeList.TypeListBean bean = mList.get(i);
            if (bean.getTypeName().equals(name)){
                return bean.getId();
            }else{
                continue;
            }
        }
        return null;
    }

    @Override
    public void initData() {
        super.initData();
        getClassInfos();
    }
    private void getClassInfos() {
        HashMap<String,Object> map= new HashMap<>();
        HttpApis.getClassesInfo(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }
            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<TypeList,RespHeader> resp= new ResponseObj<TypeList, RespHeader>();
                ResponseParser.parse(resp,response,TypeList.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals("0")){
                    TLog.log(resp.toString());
                    // TODO: 16/6/13 对应 数据进行显示 并进行控制
                    mList.addAll(resp.getBody().getTypeList());
                    ChagngeDatas(resp.getBody().getTypeList());
                }else{
                    TLog.log(resp.getHead().getRspMsg());
                }

            }
        });
    }

    private void ChagngeDatas(List<TypeList.TypeListBean> typeList) {
        for (int i = 0; i < typeList.size(); i++) {
            TypeList.TypeListBean bean = typeList.get(i);
            if (bean.getTypeName().equals("电影")){
                Glide.with(this).load(HttpUtils.appendUrl(bean.getImg())).placeholder(R.drawable.default_vertical).into(imgMovie);
            }else if (bean.getTypeName().equals("电视剧")){
                Glide.with(this).load(HttpUtils.appendUrl(bean.getImg())).placeholder(R.drawable.default_horizental).into(imgTeleplay);
            }else if(bean.getTypeName().equals("体育")){
                Glide.with(this).load(HttpUtils.appendUrl(bean.getImg())).placeholder(R.drawable.default_vertical).into(imgSport);
            }else if(bean.getTypeName().equals("综艺")){
                Glide.with(this).load(HttpUtils.appendUrl(bean.getImg())).placeholder(R.drawable.default_horizental).into(imgVariety);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
