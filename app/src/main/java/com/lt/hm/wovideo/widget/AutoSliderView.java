package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/29
 */
public class AutoSliderView extends BaseSliderView{

    private BannerList.Banner pageIconBean;
    public AutoSliderView(Context context, BannerList.Banner pageIconBean) {
        super(context);
        this.pageIconBean = pageIconBean;
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(getContext()).inflate( R.layout.layout_new_home_item, null);
        TextView title = (TextView) view.findViewById(R.id.item_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_img_bg);
        TextView item_type = (TextView) view.findViewById(R.id.item_type);
        TextView item_desc = (TextView) view.findViewById(R.id.item_desc);
        ImageView img_vip = (ImageView) view.findViewById(R.id.item_vip_logo);
        img_vip.setVisibility(pageIconBean.getIsvip().equals("0") ? View.VISIBLE : View.GONE);
        title.setText(pageIconBean.getVfName());
        item_type.setText(pageIconBean.getTypeName());
        item_desc.setText(pageIconBean.getHit());
        bindEventAndShow(view, imageView);
        return view;
    }
}
