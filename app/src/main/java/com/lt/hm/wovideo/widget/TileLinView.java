package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/14.
 */
public class TileLinView extends FrameLayout {

    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right)
    TextView imageView;


    public TileLinView(Context context) {
        this(context, null);

    }

    public TileLinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileLinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.include_title_text, this);
        ButterKnife.bind(this);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TopBar, 0, 0);
        try {
            String title = a.getString(R.styleable.TopBar_topbar_title);
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
            boolean showReturn = a.getBoolean(R.styleable.TopBar_topbar_show_right_arrow, true);
            int showRightImg = a.getInt(R.styleable.TopBar_topbar_right_img,0);
            String rightTitle = a.getString(R.styleable.TopBar_topbar_right_tv);
            if (showReturn){
                imageView.setVisibility(View.VISIBLE);
                imageView.setText(rightTitle);
                imageView.setCompoundDrawables(null,null,getResources().getDrawable(showRightImg),null);
                imageView.setCompoundDrawablePadding(10);
            }else {
                imageView.setVisibility(View.GONE);
            }

        } finally {
            a.recycle();
        }
    }

    public void setTitleTv(String str) {
        titleTv.setText(str);
    }

    /**
     * 设置标题文字
     *
     * @param title
     */
    public void setTopbar_title(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }
}
