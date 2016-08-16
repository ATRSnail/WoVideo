package com.lt.hm.wovideo.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.model.Category;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<ChannelModel> videolist = new ArrayList<>();
    private List<TagModel> tagList = new ArrayList<>();
    private LayoutInflater inflater;
    private OnCateItemListener listener;
    private int type;
    private boolean isTag;
    public boolean isCanDel = false;

    public CategoryAdapter(boolean isTag, Context ctx, List<ChannelModel> categories, List<TagModel> tagList, OnCateItemListener listener, int type) {
        this.isTag = isTag;
        this.ctx = ctx;
        this.videolist = categories;
        this.tagList = tagList;
        this.listener = listener;
        this.type = type;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return type == Category.FIRST_TYPE ?
                new FirstViewHolder(inflater.inflate(R.layout.item_personal, null, false), listener, type) :
                new SecondViewHolder(inflater.inflate(R.layout.item_personal, null, false), listener, type);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (type == Category.FIRST_TYPE) {
            FirstViewHolder firstViewHolder = (FirstViewHolder) holder;
            firstViewHolder.firstCategory.setText(isTag ? tagList.get(position).getName() : videolist.get(position).getFunName());
            firstViewHolder.deleteImg.setVisibility(isCanDel ? View.VISIBLE : View.GONE);
            firstViewHolder.setIsCanDel(isCanDel);
            return;
        }
        SecondViewHolder secondViewHolder = (SecondViewHolder) holder;
        secondViewHolder.secondCategory.setText(isTag ? tagList.get(position).getName() : videolist.get(position).getFunName());
    }

    @Override
    public int getItemCount() {
        return isTag ? tagList.size() : videolist.size();
    }

    public void toggleCanDelete() {
        isCanDel = !isCanDel;
        notifyDataSetChanged();
    }

    public void setIsCanDelete(boolean isCanDel) {
        this.isCanDel = isCanDel;
        notifyDataSetChanged();
    }
}
