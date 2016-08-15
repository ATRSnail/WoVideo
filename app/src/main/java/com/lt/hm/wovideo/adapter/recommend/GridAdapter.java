package com.lt.hm.wovideo.adapter.recommend;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.CommonAdapter;
import com.lt.hm.wovideo.base.ViewHolder;
import com.lt.hm.wovideo.model.CateTagModel;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/11.
 */
public class GridAdapter extends CommonAdapter<CateTagModel>{

    public GridAdapter(Context context, List<CateTagModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, CateTagModel cateModel) {
        holder.setText(R.id.first_cat_name,cateModel.getName());
        if (holder.getmPostion() == getCount()-1){
            holder.getView(R.id.img_more).setVisibility(View.VISIBLE);
          }
    }
}
