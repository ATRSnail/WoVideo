package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.lt.hm.wovideo.utils.ScreenUtils;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/14
 */
public class WrapContentHeightViewPager extends ViewPager {
    Context mContext;
    public WrapContentHeightViewPager(Context context) {
        super(context);
        this.mContext= context;
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext= context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),heightMeasureSpec);
            child.measure(ScreenUtils.getScreenWidth(mContext)/3,heightMeasureSpec);
            int w = child.getMeasuredWidth();
            if (w > width) //采用最大的view的高度。
                width = w;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
