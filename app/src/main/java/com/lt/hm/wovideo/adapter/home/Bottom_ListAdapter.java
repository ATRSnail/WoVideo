package com.lt.hm.wovideo.adapter.home;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class Bottom_ListAdapter extends BaseQuickAdapter<RecomList.Videos> {
    public Bottom_ListAdapter(Context context, int layoutResId, List<RecomList.Videos> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecomList.Videos videos) {
        baseViewHolder.setText(R.id.home_item_title, videos.getName());
        baseViewHolder.setText(R.id.home_item_desc, videos.getDesc());
        ImageView img = (ImageView) baseViewHolder.convertView.findViewById(R.id.home_item_img_bg);
        if (videos.getImg() != null) {
            if (videos.getTag() != null && videos.getTag().equals("h")) {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(videos.getHimg())).build());
//                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(videos.getHimg()));
            } else {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(videos.getImg())).build());
//                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(videos.getImg()));
            }
//            ImageView img = (ImageView) baseViewHolder.convertView.findViewById(R.id.home_item_img_bg);
//            Glide.with(mContext).load(HttpUtils.appendUrl(videos.getImg())).asBitmap().placeholder(null).centerCrop().into(img);
        } else {
            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(videos.getImg())).build());
//            baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
        }

    }
}
