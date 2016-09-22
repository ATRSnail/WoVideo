package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.utils.BaseViewHolder;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/21
 */
public class AnthologyGvAdapter extends BaseAdapter{

    private Context mContext;
    private List<PlayList.PlaysListBean> list;

    public AnthologyGvAdapter(Context mContext,List<PlayList.PlaysListBean> list) {
        this.mContext = mContext;
        this.list = list;
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

        tv.setText(position+1+"");
        return convertView;
    }
}
