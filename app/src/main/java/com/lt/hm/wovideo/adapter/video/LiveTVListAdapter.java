package com.lt.hm.wovideo.adapter.video;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.utils.StringUtils;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class LiveTVListAdapter extends BaseQuickAdapter<LiveModles.LiveModel> {
//    public interface  ItemOnClick{
//        void onClick( BaseViewHolder holder,int position);
//    }
//    ItemOnClick listener;
//
//    public void setListener(ItemOnClick listener) {
//        this.listener = listener;
//    }

    public LiveTVListAdapter(Context context, List<LiveModles.LiveModel> data) {
        super(R.layout.layout_tv_list_item,data);
//        super(context, R.layout.layout_tv_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveModles.LiveModel liveTvListBean) {
        holder.setText(R.id.live_list_item_text,liveTvListBean.getTvName());
        if (!StringUtils.isNullOrEmpty(liveTvListBean.getNowPro())){
            holder.setText(R.id.live_list_item_current_text,"正在播放："+liveTvListBean.getNowPro());
        }
//        TextView view = (TextView) holder.itemView.findViewById(R.id.live_list_item_current_text);
//        view.setTag("测试"+holder.getPosition());
//        view.setOnClickListener((View v)->{
//            if (listener!=null){
//                listener.onClick(holder,holder.getPosition());
//            }
//        });

//        if (StringUtils.isNullOrEmpty(liveTvListBean.getNowPro()) || liveTvListBean.getNowPro().equals("暂无")){
//            holder.setVisible(R.id.live_list_item_current_text,false);
//        }else{
//            holder.setText(R.id.live_list_item_current_text,liveTvListBean.getNowPro());
//        }
//            live_list_item_current_text
    }
}
