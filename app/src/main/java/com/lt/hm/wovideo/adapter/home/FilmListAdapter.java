package com.lt.hm.wovideo.adapter.home;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class FilmListAdapter extends BaseQuickAdapter<FilmMode> {


    public FilmListAdapter(int layoutResId, List<FilmMode> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FilmMode likeModel) {


        baseViewHolder.setText(R.id.item_title, likeModel.getName());
        baseViewHolder.setText(R.id.item_desc, likeModel.getDq());
        baseViewHolder.setText(R.id.item_type,likeModel.getTypeName());

        ImageView img = (ImageView) baseViewHolder.convertView.findViewById(R.id.item_img_bg);
        if (likeModel.getImg() != null) {
            if (likeModel.getTag() != null && likeModel.getTag().equals("h")) {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(likeModel.getImg())).build());
            } else {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(likeModel.getImg())).build());
            }
        } else {
            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(likeModel.getImg())).build());
        }
    }
}
