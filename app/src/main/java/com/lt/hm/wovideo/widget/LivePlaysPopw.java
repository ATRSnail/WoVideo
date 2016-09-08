package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.video.LivePopBigAdapter;
import com.lt.hm.wovideo.adapter.video.LivePopSmallAdapter;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/6
 */
public class LivePlaysPopw extends PopupWindow implements PopupWindow.OnDismissListener {

    private View rootView;
    RecyclerView bigRecycle;
    RecyclerView smallRecycle;

    private LivePopBigAdapter bigAdapter;
    private LivePopSmallAdapter smallAdapter;

    private LiveModles live;
    private LiveModel likeModel;
    private ArrayList<LiveModles> mList = new ArrayList<>();
    private List<LiveModel> liveModels = new ArrayList<>();

    private final String[] str = {"央视", "卫士", "地方台", "专业"};

    private Context mContext;

    private  LinearLayoutManager layoutManager;
    private  LinearLayoutManager layoutManager2;

    public LivePlaysPopw(Context context) {
        super(context);
        this.mContext = context;
        initPop();
    }

    private void initViews() {
        smallRecycle = (RecyclerView) rootView.findViewById(R.id.rv_small);
        bigRecycle = (RecyclerView) rootView.findViewById(R.id.rv_big);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2 = new LinearLayoutManager(mContext);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        bigAdapter = new LivePopBigAdapter(R.layout.item_live_big_pop,mList);
        smallAdapter = new LivePopSmallAdapter(R.layout.item_live_small_pop,liveModels);
        bigRecycle.setLayoutManager(layoutManager);
        smallRecycle.setLayoutManager(layoutManager2);
        bigRecycle.setAdapter(bigAdapter);
        smallRecycle.setAdapter(smallAdapter);

    }

    private void initData() {
        for (int j = 0 ;j<5;j++){
            likeModel = new LiveModel("ddd"+j);
            liveModels.add(likeModel);
        }

        for (int i = 0;i<str.length;i++){
            live = new LiveModles();
            live.setTitle(str[i]);
            live.setCctv(liveModels);
            mList.add(live);
        }
    }

    private void initPop() {
        // TODO Auto-generated method stub
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_live_popu, null);
        ButterKnife.bind(rootView);

        this.setContentView(rootView);
        this.setWidth(ScreenUtils.getScreenWidth(mContext) / 3 * 2);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOnDismissListener(this);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //this.setAnimationStyle(animStyle);
        initData();
        initViews();

    }

    @Override
    public void onDismiss() {

    }
}
