package com.lt.hm.wovideo.adapter.video;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModles;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/8
 */
public class LivePopBigAdapter extends BaseQuickAdapter<LiveModles> {
    public LivePopBigAdapter(int layoutResId, List<LiveModles> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LiveModles liveModles) {

        baseViewHolder.setText(R.id.tv_big_name,liveModles.getTitle());
    }
}
