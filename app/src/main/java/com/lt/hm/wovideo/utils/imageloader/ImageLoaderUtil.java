package com.lt.hm.wovideo.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/24
 */
public class ImageLoaderUtil {
    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int PIC_VERTICAL = R.drawable.default_vertical;
    public static final int PIC_HORIZENTAL = R.drawable.default_horizental;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderStrategy mStrategy;

    private ImageLoaderUtil() {
        mStrategy = new GlideImageLoaderStrategy();
    }

    //single instance
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img) {
        mStrategy.loadImage(context, img);
    }

    public void loadImage(ImageView imageView, String imgUrl, boolean isHor) {
        this.loadImage(imageView.getContext(), new ImageLoader.Builder().imgView(imageView).placeHolder(isHor ? R.drawable.default_horizental : R.drawable.default_vertical).url(imgUrl).build());
    }

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

}
