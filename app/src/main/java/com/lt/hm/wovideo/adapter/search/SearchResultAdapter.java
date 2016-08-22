package com.lt.hm.wovideo.adapter.search;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.SearchResult;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class SearchResultAdapter extends BaseQuickAdapter<SearchResult.VfListBean> {
    public SearchResultAdapter(Context context,List<SearchResult.VfListBean> data) {
        super(R.layout.layout_new_home_item,data);
//        super(context, R.layout.layout_home_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SearchResult.VfListBean vfListBean) {
        holder.setText(R.id.item_title, vfListBean.getName());
        holder.setText(R.id.item_desc, vfListBean.getHit()+"");
        holder.setText(R.id.item_type,vfListBean.getTypeName());
        ImageView img= (ImageView) holder.convertView.findViewById(R.id.item_img_bg);
        if (vfListBean.getImg() != null) {
//            holder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(vfListBean.getHIMG()));
            Glide.with(mContext).load(HttpUtils.appendUrl(vfListBean.getHImg())).placeholder(R.drawable.default_horizental).into(img);
        } else {
            holder.setImageResource(R.id.item_img_bg, R.drawable.img_4);
        }
        if (!vfListBean.getIsfree().equals("1")){
            holder.setVisible(R.id.item_vip_logo,true);
        }else{
            holder.setVisible(R.id.item_vip_logo,false);
        }

//        baseViewHolder.setText(R.id.item_title, likeModel.getName());
//        baseViewHolder.setText(R.id.item_desc, likeModel.getHit());
//        baseViewHolder.setText(R.id.item_type, likeModel.getTypeName());
//
//        ImageView img = (ImageView) baseViewHolder.convertView.findViewById(R.id.item_img_bg);
//        ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(img).placeHolder(channelCode ? R.drawable.default_horizental : R.drawable.default_vertical).url(HttpUtils.appendUrl(channelCode ? likeModel.gethImg() : likeModel.getImg())).build());

    }
}
