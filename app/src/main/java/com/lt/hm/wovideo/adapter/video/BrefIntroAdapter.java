package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class BrefIntroAdapter extends BaseAdapter {
    Context mContext;
    String[] names;
    String[] values;
    LayoutInflater mInlater;

    public BrefIntroAdapter(Context mContext, String[] names, String[] values) {
        this.mContext = mContext;
        this.names = names;
        this.values = values;
        mInlater = mInlater.from(mContext);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInlater.inflate(R.layout.layout_video_bref_introlist, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.introlName.setText(names[position]);
        holder.introlValue.setText(values[position]);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.introl_name)
        TextView introlName;
        @BindView(R.id.introl_value)
        TextView introlValue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
