package com.lt.hm.wovideo.adapter.integral;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.Integral;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class IntegralAdapter extends BaseQuickAdapter<Integral> {

    public IntegralAdapter(Context context, int layoutResId, List<Integral> data) {
        super(layoutResId,data);
//        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Integral integral) {
        holder.setText(R.id.integral_name, integral.getName());
        holder.setText(R.id.integral_values, integral.getScores());
    }
}
