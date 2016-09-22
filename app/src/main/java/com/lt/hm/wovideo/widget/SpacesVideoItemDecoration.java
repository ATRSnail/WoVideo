package com.lt.hm.wovideo.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lt.hm.wovideo.utils.TLog;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/25
 */
public class SpacesVideoItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isHasHeader = true;

    public SpacesVideoItemDecoration(int space, boolean isHasHeader) {
        this.space = space;
        this.isHasHeader = isHasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        TLog.error("outRect--->"+parent.getChildAdapterPosition(view) % 6);
        if (isHasHeader) {
            outRect.left = parent.getChildAdapterPosition(view) == 0 ? space * 2 : space;
            outRect.right = space;
        } else {
            outRect.left = parent.getChildAdapterPosition(view) % 6 == 0 ? space * 2 : space;
            outRect.right = parent.getChildAdapterPosition(view) % 6 == 5 ? space * 2 : space;
        }
        outRect.bottom = space;
        outRect.top = space;
    }
}
