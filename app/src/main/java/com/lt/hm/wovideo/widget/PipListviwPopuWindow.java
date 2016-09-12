package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxg on 16/8/3.
 */
public class PipListviwPopuWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private ListView mListView;
    private List<LiveModles> liveModles = new ArrayList<>();
    private VideoPipAdapter.ItemClickCallBack mItemCallBack;
    private Context mContext;

    private View rootView;
    private Button mButton;
    private VideoPipAdapter mAdapter = null;

    /**
     * @param context
     * @param liveModles    数据集
     * @param mItemCallBack item的点击事件
     */
    public PipListviwPopuWindow(Context context, List<LiveModles> liveModles, VideoPipAdapter.ItemClickCallBack mItemCallBack) {
        super();
        this.mContext = context;
        this.liveModles = liveModles;
        this.mItemCallBack = mItemCallBack;
        initPop();
    }


    private void initPop() {
        // TODO Auto-generated method stub
        rootView = LayoutInflater.from(mContext).inflate(R.layout.pip_list_popu, null);
        mListView = (ListView) rootView.findViewById(R.id.lv_pop_datas);
        mListView.setDivider(new ColorDrawable(Color.GRAY));
        mListView.setDividerHeight(1);
        mButton = (Button) rootView.findViewById(R.id.pip_btn_sure);
        this.setContentView(rootView);
        this.setWidth(ScreenUtils.getScreenWidth(mContext) / 3 * 2);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOnDismissListener(this);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //this.setAnimationStyle(animStyle);
        this.setContentView(rootView);
        this.mAdapter = new VideoPipAdapter(mContext, liveModles);
        this.mListView.setAdapter(mAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemCallBack.pipBtnCallBack(mAdapter.getUrls());
            }
        });
//        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView parent, View view,
//                                    int position, long id) {
//                if (mItemCallBack != null) {
//                    mItemCallBack.pipListcallBack(position);
//                }
//            }
//        });
    }

    @Override
    public void onDismiss() {

    }
}
