package com.lt.hm.wovideo.widget.ptrpull;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lt.hm.wovideo.utils.TLog;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/25
 */
public class SamplePtrFrameLayout extends PtrFrameLayout {

    private boolean disScroll = false;
    public SamplePtrFrameLayout(Context context) {
        this(context, null);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SamplePtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        SmileyLoadingViewHeader mHeaderView = new SmileyLoadingViewHeader(getContext());
        setResistance(1.7f);
        setLoadingMinTime(1000);
        setDurationToCloseHeader(500);
        setRatioOfHeaderHeightToRefresh(1.0f);
        setKeepHeaderWhenRefresh(true);
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }

    public void setDisScroll(boolean disScroll){
        this.disScroll = disScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        TLog.error("slider---ACTION_DOWN"+disScroll);
        if (disScroll) {
            int action = e.getAction();
            switch (action){
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    disScroll = false;
                    break;
            }
            return dispatchTouchEventSupper(e);
        }

        return super.dispatchTouchEvent(e);
    }
}
