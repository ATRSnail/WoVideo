package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.LikeList;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/7
 */
public class VideoItemGridAdapter extends BaseQuickAdapter<LikeList.LikeListBean>{
    public VideoItemGridAdapter(Context context,List<LikeList.LikeListBean> data) {
        super(context, R.layout.layout_item_grid, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, LikeList.LikeListBean likeListBean) {
        holder.setText(R.id.grid_item_title, likeListBean.getName());
        ImageView view = (ImageView) holder.convertView.findViewById(R.id.grid_item_img_bg);
        if (likeListBean.getImg() != null) {
            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(likeListBean.getImg())).build());
        } else {
            ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(likeListBean.getImg())).build());
        }
        if (!likeListBean.getIsfree().equals("1")){
            holder.setVisible(R.id.item_vip_logo,true);
        }else{
            holder.setVisible(R.id.item_vip_logo,false);
        }

    }
}
