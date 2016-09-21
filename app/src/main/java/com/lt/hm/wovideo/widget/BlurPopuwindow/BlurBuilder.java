package com.lt.hm.wovideo.widget.BlurPopuwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/21
 */
public class BlurBuilder {

    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View mBackgroundView = activity.getWindow().getDecorView();

        //retrieve background view, must be achieved on ui thread since
        //only the original thread that created a view hierarchy can touch its views.

        Rect rect = new Rect();
        mBackgroundView.getWindowVisibleDisplayFrame(rect);
        mBackgroundView.destroyDrawingCache();
        mBackgroundView.setDrawingCacheEnabled(true);
        mBackgroundView.buildDrawingCache(true);
        Bitmap mBackground = mBackgroundView.getDrawingCache(true);

        /**
         * After rotation, the DecorView has no height and no width. Therefore
         * .getDrawingCache() return null. That's why we  have to force measure and layout.
         */
        if (mBackground == null) {
            mBackgroundView.measure(
                    View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
            );
            mBackgroundView.layout(0, 0, mBackgroundView.getMeasuredWidth(),
                    mBackgroundView.getMeasuredHeight());
            mBackgroundView.destroyDrawingCache();
            mBackgroundView.setDrawingCacheEnabled(true);
            mBackgroundView.buildDrawingCache(true);
            mBackground = mBackgroundView.getDrawingCache(true);
        }
        return mBackground;
    }
}
