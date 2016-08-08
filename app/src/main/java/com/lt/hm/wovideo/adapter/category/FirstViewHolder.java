package com.lt.hm.wovideo.adapter.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.widget.autofittextview.AutofitTextView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class FirstViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public TextView firstCategory;
    public ImageView deleteImg;
    private OnCateItemListener listener;
    private boolean isCanDel = false;
    private int type;

    public FirstViewHolder(View itemView, OnCateItemListener listener, int type) {
        super(itemView);
        this.listener = listener;
        this.type = type;
        firstCategory = (AutofitTextView) itemView.findViewById(R.id.first_cat_name);
        deleteImg = (ImageView) itemView.findViewById(R.id.img_delete);
        firstCategory.setOnClickListener(this);
        firstCategory.setOnLongClickListener(this);
    }

    public void setIsCanDel(boolean isCanDel){
        this.isCanDel  = isCanDel;
    }

    @Override
    public void onClick(View v) {
        if (v == null || !isCanDel) return;
        int pos = getAdapterPosition();
        listener.OnItemClick(type, pos);
    }

    @Override
    public boolean onLongClick(View v) {
        listener.OnItemLongClick();
        return false;
    }
}
