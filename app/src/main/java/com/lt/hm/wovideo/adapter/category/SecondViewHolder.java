package com.lt.hm.wovideo.adapter.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.widget.autofittextview.AutofitTextView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class SecondViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView secondCategory;
    private OnCateItemListener listener;
    private int type;

    public SecondViewHolder(View itemView, OnCateItemListener listener, int type) {
        super(itemView);
        this.listener = listener;
        this.type = type;
        secondCategory = (AutofitTextView) itemView.findViewById(R.id.first_cat_name);
        secondCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = getAdapterPosition();
        listener.OnItemClick(type, pos);
    }
}
