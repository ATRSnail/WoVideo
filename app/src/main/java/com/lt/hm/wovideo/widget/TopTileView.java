package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/14.
 */
public class TopTileView extends LinearLayout {

    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.img_right)
    ImageView imageView;


    public TopTileView(Context context) {
        super(context);
        inflate(context, R.layout.include_title_text, this);
        ButterKnife.bind(this);
    }

    public void setTitleTv(String str) {
        titleTv.setText(str);
    }

    public void setImageVisiable(boolean b) {
        imageView.setVisibility(b ? VISIBLE : GONE);
    }


}
