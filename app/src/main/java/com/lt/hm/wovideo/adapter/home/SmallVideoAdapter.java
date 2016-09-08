package com.lt.hm.wovideo.adapter.home;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.FilmMode;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/5
 */
public class SmallVideoAdapter extends BaseMultiItemQuickAdapter<FilmMode> {
    private final static int TYPE_COUNT = 2;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_HEAD = 0;

    public SmallVideoAdapter(List<FilmMode> data) {
        super(data);
        addItemType(TYPE_HEAD, R.layout.layout_new_home_item);
        addItemType(TYPE_CONTENT, R.layout.small_video_list_item);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, FilmMode filmMode) {
        int pos = baseViewHolder.getAdapterPosition();
        int type = getDefItemViewType(pos);
        switch (type) {
            case TYPE_HEAD:
                baseViewHolder.setText(R.id.item_title, filmMode.getName());
                baseViewHolder.setText(R.id.item_desc, filmMode.getHit());
                baseViewHolder.setText(R.id.item_type, filmMode.getTypeName());

                ImageView img1 = baseViewHolder.getView(R.id.item_img_bg);

                ImageLoaderUtil.getInstance().loadImage(img1, filmMode.gethImg(), true);
                if (!filmMode.getIsfree().equals("1")) {
                    baseViewHolder.setVisible(R.id.item_vip_logo, true);
                } else {
                    baseViewHolder.setVisible(R.id.item_vip_logo, false);
                }
                break;
            case TYPE_CONTENT:
                baseViewHolder.setText(R.id.item_title, filmMode.getName());
                baseViewHolder.setText(R.id.item_desc, filmMode.getHit());
                baseViewHolder.setText(R.id.item_type, filmMode.getTypeName());

                ImageView img = baseViewHolder.getView(R.id.item_img_bg);

                ImageLoaderUtil.getInstance().loadImage(img, filmMode.gethImg(), true);
                if (pos % 4 == 2) {
                    baseViewHolder.getView(R.id.ly_movie_item).setBackgroundColor(mContext.getResources().getColor(R.color.bg_smell_video_whit_item));
                } else {
                    baseViewHolder.getView(R.id.ly_movie_item).setBackgroundColor(mContext.getResources().getColor(R.color.bg_smell_video_item));
                }
                break;
        }
    }
}
