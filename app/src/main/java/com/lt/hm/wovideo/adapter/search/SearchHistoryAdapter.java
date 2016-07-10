package com.lt.hm.wovideo.adapter.search;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String> {
    public SearchHistoryAdapter(Context context,  List<String> data) {
        super(context, R.layout.layout_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.search_history_text,s);
    }
}
