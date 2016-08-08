package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseLinearLayout;
import com.lt.hm.wovideo.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/8.
 */
public class TitleLinearlayout extends BaseLinearLayout {

 //   @BindView(R.id.text_top)
//    TextView topText;

    public TitleLinearlayout(Context context) {
        super(context);
///        LayoutInflater.from(context).inflate(R.layout.include_top_text, this);
        ButterKnife.bind(this);
    }


    public void setTopText(String str){
        if (StringUtils.isEmpty(str))return;
//        topText.setText(str);
    }
}
