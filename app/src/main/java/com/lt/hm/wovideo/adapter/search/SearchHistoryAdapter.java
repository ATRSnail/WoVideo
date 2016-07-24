package com.lt.hm.wovideo.adapter.search;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

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
    OnDelete listener;

    public void setListener(OnDelete listener) {
        this.listener = listener;
    }

    public SearchHistoryAdapter(Context context, List<String> data) {
        super(context, R.layout.layout_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.search_history_text,s);
        ImageView img = (ImageView) holder.convertView.findViewById(R.id.search_history_del);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.del(s);
                }
            }
        });
    }

    public interface  OnDelete{
        void del(String s);
    }
}
