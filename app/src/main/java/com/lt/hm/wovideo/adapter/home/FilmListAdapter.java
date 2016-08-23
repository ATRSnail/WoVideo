package com.lt.hm.wovideo.adapter.home;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class FilmListAdapter extends BaseQuickAdapter<FilmMode> {


    private boolean channelCode;

    public FilmListAdapter(int layoutResId, List<FilmMode> data, boolean channelCode) {
        super(layoutResId, data);
        this.channelCode = channelCode;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FilmMode likeModel) {

        baseViewHolder.setText(R.id.item_title, likeModel.getName());
        baseViewHolder.setText(R.id.item_desc, likeModel.getHit());
        baseViewHolder.setText(R.id.item_type, likeModel.getTypeName());

        ImageView img = baseViewHolder.getView(R.id.item_img_bg);

        ImageLoaderUtil.getInstance().loadImage(img,channelCode ? likeModel.gethImg() : likeModel.getImg(),channelCode);

        if (!likeModel.getIsfree().equals("1")){
            baseViewHolder.setVisible(R.id.item_vip_logo,true);
        }else{
            baseViewHolder.setVisible(R.id.item_vip_logo,false);
        }
    }
}
