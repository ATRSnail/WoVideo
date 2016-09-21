package com.lt.hm.wovideo.adapter.video;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.PlayList;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/21
 */
public class EpisodeAdapter extends BaseQuickAdapter<PlayList.PlaysListBean> {

    public EpisodeAdapter(List<PlayList.PlaysListBean> data) {
        super(R.layout.layout_video_episode_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PlayList.PlaysListBean playsListBean) {
        int pos = baseViewHolder.getAdapterPosition();
        TextView epi = baseViewHolder.getView(R.id.episode_text);
        epi.setText(pos + 1 + "");
        epi.setBackground(mContext.getResources().getDrawable(playsListBean.isSelect() ? R.drawable.blue_circle : R.drawable.grey_circle));
        epi.setTextColor(mContext.getResources().getColor(playsListBean.isSelect() ? R.color.white : R.color.class_font_color));
    }

}
