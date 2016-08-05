package com.lt.hm.wovideo.adapter.comment;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.CommentModel;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/10
 * 评论列表 Adapter
 */
public class CommentAdapter extends BaseQuickAdapter<CommentModel.CommentListBean> {
    public CommentAdapter(Context context, List<CommentModel.CommentListBean> data) {
        super(R.layout.item_video_comment,data);
//        super(context, R.layout.item_video_comment,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentModel.CommentListBean bean) {
        String phoneNum = bean.getPhoneNo();
        holder.setText(R.id.comment_account,"手机用户"+phoneNum.substring(0, phoneNum.length() - (phoneNum.substring(3)).length()) + "****" + phoneNum.substring(7));
        holder.setText(R.id.comment_content,bean.getComment());
    }

}
