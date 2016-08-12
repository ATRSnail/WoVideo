package com.lt.hm.wovideo.adapter.recommend;


import android.content.Context;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.CommonAdapter;
import com.lt.hm.wovideo.base.ViewHolder;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LocalCityModel;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/11.
 */
public class LiveAdapter extends CommonAdapter<LocalCityModel> {


    public LiveAdapter(Context context, List<LocalCityModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, LocalCityModel liveModel) {
        holder.setText(R.id.name1,liveModel.getTvName());
    }
}
