package com.lt.hm.wovideo.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TouchableImage extends ImageView {

    private WeakReference<Drawable> mCurrDrawable;

    public TouchableImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TouchableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableImage(Context context) {
        super(context);
    }

    private void setShadow() {
        Drawable localDrawable = getDrawable();
        if (localDrawable != null) {
            localDrawable.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
            setImageDrawable(null);
            setImageDrawable(localDrawable);
            this.mCurrDrawable = new WeakReference(localDrawable);
            postDelayed(new MyRunnable(), 50);
        }

    }

    private void removeShadow() {
        Drawable localDrawable = (Drawable) this.mCurrDrawable.get();
        if (localDrawable != null) {
            localDrawable.clearColorFilter();
            if (localDrawable != getDrawable())
                setImageDrawable(localDrawable);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            setShadow();
        }
        return super.onTouchEvent(event);
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            removeShadow();
        }

    }
}