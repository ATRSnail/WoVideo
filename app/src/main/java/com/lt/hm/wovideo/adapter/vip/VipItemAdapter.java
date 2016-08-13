package com.lt.hm.wovideo.adapter.vip;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.VideoList;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class VipItemAdapter extends BaseQuickAdapter<VideoList.TypeListBean> {
    public VipItemAdapter(Context context, List<VideoList.TypeListBean> data) {
        super(R.layout.layout_new_home_item,data);
//        super(context, R.layout.layout_home_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VideoList.TypeListBean typeListBean) {
        baseViewHolder.setText(R.id.item_title, typeListBean.getName());
        ImageView view = (ImageView) baseViewHolder.convertView.findViewById(R.id.item_img_bg);
//        if (typeListBean.getImg() != null) {
//            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(typeListBean.getImg())).build());
//
//
////            baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.getImg()));
//        } else {
//            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(typeListBean.getImg())).build());
//
////            baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
//        }
        //Grid
        if (!StringUtils.isNullOrEmpty(typeListBean.getTag())&&typeListBean.getTag().equals("0")){
//            baseViewHolder.setText(R.id.item_desc, typeListBean.getDesc());
            baseViewHolder.setText(R.id.item_desc, typeListBean.getHit());
            TextView  text = (TextView) baseViewHolder.convertView.findViewById(R.id.item_title);
            text.setPadding(0,0,0,0);
            text.setGravity(Gravity.CENTER);
            baseViewHolder.setVisible(R.id.item_type_container,false);
            baseViewHolder.setVisible(R.id.item_desc,true);
            if (typeListBean.getImg() != null) {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(typeListBean.getImg())).build());
//                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.getImg()));
            } else {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(typeListBean.getImg())).build());
//                baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
            }
        }else{
            baseViewHolder.setText(R.id.item_desc, typeListBean.getHit());
            baseViewHolder.setVisible(R.id.item_type_container,true);
            baseViewHolder.setText(R.id.item_type,typeListBean.getLx());
            baseViewHolder.setVisible(R.id.item_desc,true);
            if (typeListBean.getHImg() != null) {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(typeListBean.getHImg())).build());
//                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.gethIMG()));
            } else {
//                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(typeListBean.gethIMG())).build());
//                baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
            }
        }
        if (!typeListBean.getIsfree().equals("1")){
            baseViewHolder.setVisible(R.id.item_vip_logo, true);
        }else{
            baseViewHolder.setVisible(R.id.item_vip_logo,false);
        }

    }
}
