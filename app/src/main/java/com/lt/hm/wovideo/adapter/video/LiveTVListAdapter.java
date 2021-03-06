package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class LiveTVListAdapter extends BaseQuickAdapter<LiveModel> {
    //    public interface  ItemOnClick{
//        void onClick( BaseViewHolder holder,int position);
//    }
//    ItemOnClick listener;
//
//    public void setListener(ItemOnClick listener) {
//        this.listener = listener;
//    }
    private String selectedText = "";

    public void updateShowText(List<LiveModel> data, String selectedText) {
        this.selectedText = selectedText;
        setNewData(data);
    }

    public LiveTVListAdapter(Context context, List<LiveModel> data) {
        super(R.layout.layout_tv_list_item, data);
//        super(context, R.layout.layout_tv_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveModel liveTvListBean) {
        holder.setText(R.id.live_list_item_text, liveTvListBean.getTvName());
        if (!StringUtils.isNullOrEmpty(liveTvListBean.getNowPro())) {
            holder.setText(R.id.live_list_item_current_text, "正在播放：" + liveTvListBean.getNowPro());
        }
        if (selectedText.equals(liveTvListBean.getTvName())){
            TLog.error("select_name--->"+selectedText+"===="+liveTvListBean.getTvName());
            holder.setBackgroundColor(R.id.rl_tv_item,mContext.getResources().getColor(R.color.gray_lighter));
        }else {
            holder.setBackgroundColor(R.id.rl_tv_item,mContext.getResources().getColor(R.color.white));
        }
    }
}
