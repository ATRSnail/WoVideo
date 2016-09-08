package com.lt.hm.wovideo.adapter.video;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModel;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/8
 */
public class LivePopSmallAdapter extends BaseQuickAdapter<LiveModel> {
    public LivePopSmallAdapter(int layoutResId, List<LiveModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LiveModel likeModel) {
        baseViewHolder.setText(R.id.tv_small_name,likeModel.getTvName());
    }
}
