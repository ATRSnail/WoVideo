package com.lt.hm.wovideo.adapter.vip;

import android.content.Context;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.CommonAdapter;
import com.lt.hm.wovideo.base.ViewHolder;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/18.
 */
public class GridViewAapter extends CommonAdapter<LikeModel> {

    public void notityData(List<LikeModel> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public GridViewAapter(Context context, List<LikeModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder baseViewHolder, LikeModel likeModel) {

        baseViewHolder.setText(R.id.item_title, likeModel.getName());
        baseViewHolder.setText(R.id.item_desc, likeModel.getHit());
        baseViewHolder.setText(R.id.item_type, likeModel.getTypeName());

        ImageView img = (ImageView) baseViewHolder.getMconvertView().findViewById(R.id.item_img_bg);
        ImageLoaderUtil.getInstance().loadImage(mcontext, new ImageLoader.Builder().imgView(img).placeHolder( R.drawable.default_vertical).url(HttpUtils.appendUrl(likeModel.getImg())).build());

    }
}
