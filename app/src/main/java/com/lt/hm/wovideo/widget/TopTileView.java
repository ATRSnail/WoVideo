package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/14.
 */
public class TopTileView extends LinearLayout {

    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right)
    TextView imageView;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;


    public TopTileView(Context context) {
        super(context);
        inflate(context, R.layout.include_title_text, this);
        ButterKnife.bind(this);
        rl_title.setVisibility(VISIBLE);
    }

    public void setTitleTv(String str) {
        titleTv.setText(str);
    }

    public void setImageVisiable(boolean b) {
        imageView.setVisibility(b ? VISIBLE : GONE);
    }

    public void setImageText(String str){
        imageView.setText(str);
    }

    public void setImage(int img) {
        imageView.setCompoundDrawablePadding(ScreenUtils.dp2px(getContext(),10));
        imageView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(img),null);
    }


}
