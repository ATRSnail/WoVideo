package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.model.PlaysListBean;
import com.lt.hm.wovideo.utils.BaseViewHolder;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/21
 */
public class AnthologyGvAdapter extends BaseAdapter{

    private Context mContext;
    private List<PlaysListBean> list;
    private int selectPos = 0;//选中
    private Drawable blueBg;

    public AnthologyGvAdapter(Context mContext,List<PlaysListBean> list,int selectPos) {
        this.mContext = mContext;
        this.selectPos = selectPos;
        this.list = list;
        blueBg = mContext.getResources().getDrawable(R.drawable.text_circle_blue);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_gv_anthology, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_anthology);
        ImageView iv = BaseViewHolder.get(convertView, R.id.img_anthology);

        if (selectPos == position){
            tv.setBackground(blueBg);
        }else {
            tv.setBackground(null);
        }

        tv.setText(position+1+"");
        return convertView;
    }

    public void notifyStyle(int selectPos){
        this.selectPos = selectPos;
        notifyDataSetChanged();
    }
}
