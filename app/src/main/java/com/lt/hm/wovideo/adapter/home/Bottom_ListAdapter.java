package com.lt.hm.wovideo.adapter.home;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.Videos;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class Bottom_ListAdapter extends BaseQuickAdapter<Videos> {
    public Bottom_ListAdapter(Context context, int layoutResId, List<Videos> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Videos videos) {
        baseViewHolder.setText(R.id.home_item_title, videos.getName());
        baseViewHolder.setText(R.id.home_item_desc, videos.getDesc());
        if (videos.getImg() != null) {
            baseViewHolder.setImageUrl(R.id.home_item_img_bg, videos.getImg());
        } else {
            baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
        }
    }
}
