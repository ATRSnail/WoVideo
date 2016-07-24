package com.lt.hm.wovideo.utils.imageloader;

import android.content.Context;

import com.lt.hm.wovideo.R;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/24
 */
public class ImageLoaderUtil {
    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int PIC_VERTICAL= R.drawable.default_vertical;
    public static final int PIC_HORIZENTAL= R.drawable.default_horizental;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderStrategy mStrategy;

    private ImageLoaderUtil(){
        mStrategy =new GlideImageLoaderStrategy();
    }

    //single instance
    public static ImageLoaderUtil getInstance(){
        if(mInstance ==null){
            synchronized (ImageLoaderUtil.class){
                if(mInstance == null){
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img){
        mStrategy.loadImage(context,img);
    }

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy){
        mStrategy =strategy;
    }

}
