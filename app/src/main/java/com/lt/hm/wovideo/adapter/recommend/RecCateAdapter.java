package com.lt.hm.wovideo.adapter.recommend;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.CateModel;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/9.
 */
public class RecCateAdapter extends BaseQuickAdapter<CateModel> {

    public RecCateAdapter(int layoutResId, List<CateModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CateModel cateModel) {

        baseViewHolder.setText(R.id.first_cat_name,cateModel.getName());
    }
}
