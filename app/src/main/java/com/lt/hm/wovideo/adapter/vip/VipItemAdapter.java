package com.lt.hm.wovideo.adapter.vip;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.VideoList;
import com.lt.hm.wovideo.utils.StringUtils;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class VipItemAdapter extends BaseQuickAdapter<VideoList.TypeListBean> {
    public VipItemAdapter(Context context, List<VideoList.TypeListBean> data) {
        super(context, R.layout.layout_home_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VideoList.TypeListBean typeListBean) {
        baseViewHolder.setText(R.id.home_item_title, typeListBean.getName());
        if (typeListBean.getImg() != null) {
            baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.getImg()));
        } else {
            baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
        }
        //Grid
        if (!StringUtils.isNullOrEmpty(typeListBean.getTag())&&typeListBean.getTag().equals("0")){
            baseViewHolder.setText(R.id.home_item_desc, typeListBean.getDesc());
            TextView  text = (TextView) baseViewHolder.convertView.findViewById(R.id.home_item_title);
            text.setPadding(0,0,0,0);
            text.setGravity(Gravity.CENTER);
            baseViewHolder.setVisible(R.id.home_item_desc,false);
            if (typeListBean.getImg() != null) {
                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.getImg()));
            } else {
                baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
            }
        }else{
            baseViewHolder.setText(R.id.home_item_desc, typeListBean.getDesc());
            baseViewHolder.setVisible(R.id.home_item_desc,true);
            if (typeListBean.gethIMG() != null) {
                baseViewHolder.setImageUrl(R.id.home_item_img_bg, HttpUtils.appendUrl(typeListBean.gethIMG()));
            } else {
                baseViewHolder.setImageResource(R.id.home_item_img_bg, R.drawable.img_4);
            }
        }
        if (!typeListBean.getIsfree().equals("1")){
            baseViewHolder.setVisible(R.id.item_vip_logo, true);
        }else{
            baseViewHolder.setVisible(R.id.item_vip_logo,false);
        }

    }
}
