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
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.ViewMiddle.ViewMiddle;
import com.lt.hm.wovideo.widget.ViewMiddle.ViewMiddleModel;

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
    private ViewMiddle viewMiddle;
    private ViewMiddleModel viewMiddleModel;

    private LivePopBigAdapter bigAdapter;
    private LivePopSmallAdapter smallAdapter;

    private final String[] str = {"央视", "卫士", "地方台", "专业"};

    private Context mContext;

    private  LinearLayoutManager layoutManager;
    private  LinearLayoutManager layoutManager2;
    private List<LiveModles> mList;

    public LivePlaysPopw(Context context,List<LiveModles> liveModlesList) {
        super(context);
        this.mContext = context;
        this.mList = liveModlesList;
        TLog.error("mmlist--->"+mList.toString());
        initPop();
    }

    private void initViews() {

//        viewMiddle.setOnSelectListener(new HotBroadcastOnSelectListener(this, viewMiddle));

//        smallRecycle = (RecyclerView) rootView.findViewById(R.id.rv_small);
//        bigRecycle = (RecyclerView) rootView.findViewById(R.id.rv_big);
//        layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager2 = new LinearLayoutManager(mContext);
//        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//        bigAdapter = new LivePopBigAdapter(R.layout.item_live_big_pop,mList);
//        smallAdapter = new LivePopSmallAdapter(R.layout.item_live_small_pop,mList.get(0).getSinatv());
//        bigRecycle.setLayoutManager(layoutManager);
//        smallRecycle.setLayoutManager(layoutManager2);
//        bigRecycle.setAdapter(bigAdapter);
//        smallRecycle.setAdapter(smallAdapter);
    }

    private void initPop() {
        // TODO Auto-generated method stub
        viewMiddle = new ViewMiddle(mContext,mList);
        this.setContentView(viewMiddle);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOnDismissListener(this);
        this.setAnimationStyle(R.style.AnimationRightFade);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        initVaule();
    }

    private void initVaule() {
        viewMiddle.updateShowText("卫士", "江西卫视");
    }

    @Override
    public void onDismiss() {

    }
}
