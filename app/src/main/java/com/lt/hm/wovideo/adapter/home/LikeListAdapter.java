package com.lt.hm.wovideo.adapter.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;
import com.lt.hm.wovideo.utils.imageloader.LruCacheUtils;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class LikeListAdapter extends BaseQuickAdapter<LikeModel> {


    public LikeListAdapter(int layoutResId, List<LikeModel> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LikeModel likeModel) {


        baseViewHolder.setText(R.id.item_title, likeModel.getName());
        baseViewHolder.setText(R.id.item_desc, likeModel.getHit());
        baseViewHolder.setText(R.id.item_type,likeModel.getTypeName());

        ImageView img = (ImageView) baseViewHolder.convertView.findViewById(R.id.item_img_bg);

        Glide.with(mContext).load(HttpUtils.appendUrl(likeModel.getHImg())).placeholder(R.drawable.default_horizental).into(img);

        //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
        Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(likeModel.getHImg());
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(likeModel.getHImg())).build());
        }

//        if (likeModel.getImg() != null) {
//            if (likeModel.getTag() != null && likeModel.getTag().equals("h")) {
//                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(likeModel.getHImg())).build());
//            } else {
//                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(likeModel.getHImg())).build());
//            }
//        } else {
//            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(likeModel.getImg())).build());
//        }

        if (!likeModel.getIsfree().equals("1")){
            baseViewHolder.setVisible(R.id.item_vip_logo,true);
        }else{
            baseViewHolder.setVisible(R.id.item_vip_logo,false);
        }
    }
}
