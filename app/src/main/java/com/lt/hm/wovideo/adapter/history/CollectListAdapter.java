package com.lt.hm.wovideo.adapter.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.model.CollectModel;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/20
 */
public class CollectListAdapter extends BaseAdapter {
    // 填充数据的list
    private List<CollectModel.CollListBean> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public CollectListAdapter(List<CollectModel.CollListBean> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_check_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int typeId = Integer.parseInt(list.get(position).getTypeid());
        if(VideoType.MOVIE.getId()== typeId){
            holder.itemMovieName.setText(list.get(position).getName());
        }else{
            if (StringUtils.isNullOrEmpty(list.get(position).getIndexs())){
                holder.itemMovieName.setText(list.get(position).getName());
            }else{
                holder.itemMovieName.setText(list.get(position).getName()+" 第"+list.get(position).getIndexs()+"集");
            }
        }
        holder.itemCurrentPosition.setVisibility(View.GONE);
//        holder.itemCurrentPosition.setText(list.get(position).getCurrent_positon()+"");
        ImageView img = holder.itemVideoImg;
//        Glide.with(context).load(HttpUtils.appendUrl(list.get(position).getImg())).centerCrop().into(img);
        ImageLoaderUtil.getInstance().loadImage(context, new ImageLoader.Builder().imgView(img).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(list.get(position).getImg())).build());

        // 根据flag来设置checkbox的选中状况

        if (!StringUtils.isNullOrEmpty(list.get(position).getFlag())){
            holder.itemCheckBox.setVisibility(View.VISIBLE);
            holder.itemCheckBox.setChecked(list.get(position).getFlag().equals("true"));
        }else{
            holder.itemCheckBox.setVisibility(View.GONE);
        }

        return convertView;
    }

    public final class ViewHolder {
        @BindView(R.id.item_check_box)
        public CheckBox itemCheckBox;
        @BindView(R.id.item_video_img)
        public ImageView itemVideoImg;
        @BindView(R.id.item_movie_name)
        public TextView itemMovieName;
        @BindView(R.id.item_current_position)
        public TextView itemCurrentPosition;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
