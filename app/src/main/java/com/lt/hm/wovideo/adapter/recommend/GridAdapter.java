package com.lt.hm.wovideo.adapter.recommend;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.CommonAdapter;
import com.lt.hm.wovideo.base.ViewHolder;
import com.lt.hm.wovideo.model.CateModel;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/11.
 */
public class GridAdapter extends CommonAdapter<CateModel>{

    public GridAdapter(Context context, List<CateModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }


    @Override
    public void convert(ViewHolder holder, CateModel cateModel) {
        holder.setText(R.id.first_cat_name,cateModel.getName());
        if (cateModel.getTag() == 1){
            ((TextView) holder.getView(R.id.first_cat_name)).setTextColor(Color.WHITE);
            holder.getView(R.id.first_cat_name).setBackgroundResource(R.drawable.blue_circle);
        }
    }
}
