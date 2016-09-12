package com.lt.hm.wovideo.widget.ViewMiddle;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.utils.TLog;

/**
 * @author 作者:xch
 * @version 创建时间：2015-1-22 下午3:06:28 类说明
 */
public class ViewMiddleAdapter extends ArrayAdapter<ViewMiddleModel> {

    private Context mContext;
    private List<ViewMiddleModel> mListData;
    private ViewMiddleModel[] mArrayData;
    private int selectedPos = -1;
    private String selectedText = "";
    private String selectNowText = "";
    private int selectedId;
    private int normalDrawbleId;
    private int selectedDrawble;
    private float textSize;
    private OnItemClickListener mOnItemClickListener;

    public ViewMiddleAdapter(Context context, List<ViewMiddleModel> listData, int sId, int nId) {
        super(context, R.string.no_data, listData);
        mContext = context;
        mListData = listData;
        selectedDrawble = mContext.getResources().getColor(sId);
        normalDrawbleId = mContext.getResources().getColor(nId);

        init();
    }

    private void init() {

    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (mListData != null && pos < mListData.size()) {
            selectedPos = pos;
            selectedText = mListData.get(pos).getTitle();
            selectNowText = mListData.get(pos).getTitle();
            selectedId = mListData.get(pos).getmSoundType();
            notifyDataSetChanged();
        } else if (mArrayData != null && pos < mArrayData.length) {
            selectedPos = pos;
            selectedText = mArrayData[pos].getTitle();
            selectNowText = mArrayData[pos].getTitle();
            selectedId = mArrayData[pos].getmSoundType();
            notifyDataSetChanged();
        }

    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (mListData != null && pos < mListData.size()) {
            selectedText = mListData.get(pos).getTitle();
            selectNowText = mListData.get(pos).getTitle();
            selectedId = mListData.get(pos).getmSoundType();
        } else if (mArrayData != null && pos < mArrayData.length) {
            selectedText = mArrayData[pos].getTitle();
            selectNowText = mArrayData[pos].getTitle();
            selectedId = mArrayData[pos].getmSoundType();
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mArrayData != null && selectedPos < mArrayData.length) {
            return selectedPos;
        }
        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }

        return -1;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_live_small_pop, parent, false);
            holder = new ViewHolder();
            holder.tv_small_name = (TextView) convertView.findViewById(R.id.tv_small_name);
            holder.tv_small_playing = (TextView) convertView.findViewById(R.id.tv_small_playing);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        ViewMiddleModel viewMiddleModel = mListData.get(position);
        String mNameString = "";
        String mNowString = "";
        int selectId = 0;
        if (mListData != null) {
            if (position < mListData.size()) {
                mNameString = viewMiddleModel.getTitle();
                mNowString = viewMiddleModel.getTitle();
                selectId = viewMiddleModel.getmSoundType();
            }
        } else if (mArrayData != null) {
            if (position < mArrayData.length) {
                mNameString = mArrayData[position].getTitle();
                mNowString = mArrayData[position].getTitle();
                selectId = mArrayData[position].getmSoundType();
            }
        }
        holder.tv_small_name.setText(mNameString);
        holder.tv_small_playing.setText("正在播放:"+mNowString);

        if (selectedText != null && selectedText.equals(mNameString) && selectedId != 0 && selectedId == selectId) {
            holder.tv_small_name.setTextColor(selectedDrawble);//设置选中的背景图片
            holder.tv_small_playing.setTextColor(selectedDrawble);
        } else {
            holder.tv_small_name.setTextColor(normalDrawbleId);//设置未选中状态背景图片
            holder.tv_small_playing.setTextColor(normalDrawbleId);
        }
        convertView.setOnClickListener(new OnClick(position));
        return convertView;
    }

    class OnClick implements OnClickListener {

        private int position;

        public OnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            TLog.error("touchPosition=---->"+position);
            selectedPos = position;
            setSelectedPosition(selectedPos);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, selectedPos);
            }
        }
    }

    class ViewHolder{
        TextView tv_small_name;
        TextView tv_small_playing;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
