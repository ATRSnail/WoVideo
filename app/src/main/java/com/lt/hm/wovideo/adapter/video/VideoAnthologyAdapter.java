package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class VideoAnthologyAdapter extends BaseQuickAdapter<String> {
    boolean tag=false;
    OnTextSelected listener;
    public void setListener(OnTextSelected listener) {
        this.listener = listener;
    }

    public VideoAnthologyAdapter(Context context, List<String> data) {
        super(R.layout.layout_video_anthology_item,data);
//        super(context, R.layout.layout_video_anthology_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String bean) {
        holder.setText(R.id.anthology_item_text,bean);
        TextView view = (TextView) holder.convertView.findViewById(R.id.anthology_item_text);
//        view.setOnClickListener((View v)->{
//            if (tag){
//                view.setBackgroundColor(R.color.white);
//            }else{
//                view.setBackgroundColor(android.R.color.holo_green_dark);
//            }
//        });
//
//        view.setOnClickListener((View v)->{
//            if (listener!=null){
//                listener.changeColor(v);
//            }
//        });
    }

    public interface  OnTextSelected{
        void changeColor(View v);
    }

}
