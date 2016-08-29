package com.lt.hm.wovideo.adapter.home;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class LikeListAdapter extends BaseQuickAdapter<LikeModel> {

    private boolean isHig = true;//判断显示横图还是竖图,默认横图

    public LikeListAdapter(int layoutResId, List<LikeModel> data, boolean isHig) {
        super(layoutResId, data);
        this.isHig = isHig;
    }

    public LikeListAdapter(int layoutResId, List<LikeModel> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LikeModel likeModel) {

        baseViewHolder.setText(R.id.item_title, likeModel.getName());
        baseViewHolder.setText(R.id.item_desc, likeModel.getHit());
        baseViewHolder.setText(R.id.item_type, likeModel.getTypeName());

        ImageView img = baseViewHolder.getView(R.id.item_img_bg);

        ImageLoaderUtil.getInstance().loadImage(img, isHig ? likeModel.getHImg() : likeModel.getImg(), true);

        if (!likeModel.getIsfree().equals("1")) {
            baseViewHolder.setVisible(R.id.item_vip_logo, true);
        } else {
            baseViewHolder.setVisible(R.id.item_vip_logo, false);
        }
    }
}
