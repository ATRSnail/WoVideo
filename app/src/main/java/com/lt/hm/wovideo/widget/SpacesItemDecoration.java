package com.lt.hm.wovideo.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/25
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isHasHeader = true;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, boolean isHasHeader) {
        this.space = space;
        this.isHasHeader = isHasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (isHasHeader) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = parent.getChildAdapterPosition(view) % 3 == 1 ? space * 2 : space;
                outRect.right = parent.getChildAdapterPosition(view) % 3 == 0 ? space * 2 : space;
                outRect.bottom = space;
                if (parent.getChildAdapterPosition(view) != 1 || parent.getChildAdapterPosition(view) != 2 || parent.getChildAdapterPosition(view) != 3) {
                    outRect.top = space;
                }
            }
        } else {
            outRect.left = parent.getChildAdapterPosition(view) % 3 == 0 ? space * 2 : space;
            outRect.right = parent.getChildAdapterPosition(view) % 3 == 2 ? space * 2 : space;
            outRect.bottom = space;
            outRect.top = space;
        }

    }
}
