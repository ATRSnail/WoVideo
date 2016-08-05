package com.lt.hm.wovideo.adapter.search;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.SearchResult;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class SearchResultAdapter extends BaseQuickAdapter<SearchResult.VfListBean> {
    public SearchResultAdapter(Context context,List<SearchResult.VfListBean> data) {
        super(R.layout.layout_home_item,data);
//        super(context, R.layout.layout_home_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SearchResult.VfListBean vfListBean) {
        holder.setText(R.id.home_item_title, vfListBean.getName());
        holder.setText(R.id.home_item_desc, vfListBean.getName());
        ImageView img= (ImageView) holder.convertView.findViewById(R.id.home_item_img_bg);
        if (vfListBean.getImg() != null) {
//            holder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(vfListBean.getHIMG()));
            Glide.with(mContext).load(HttpUtils.appendUrl(vfListBean.getHIMG())).placeholder(R.drawable.default_vertical).into(img);
        } else {
            holder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
        }
        if (!vfListBean.getIsfree().equals("1")){
            holder.setVisible(R.id.item_vip_logo,true);
        }else{
            holder.setVisible(R.id.item_vip_logo,false);
        }
    }
}
