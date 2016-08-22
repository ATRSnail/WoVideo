package com.lt.hm.wovideo.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lt.hm.wovideo.utils.AppUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/24
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {
    @Override
    public void loadImage(Context ctx, ImageLoader img) {

        boolean flag= false;
//        boolean flag= SettingUtils.getOnlyWifiLoadImg(ctx);
        //如果不是在wifi下加载图片，直接加载
        if(!flag){
            loadNormal(ctx,img);
            return;
        }

        int strategy =img.getWifiStrategy();
        if(strategy == ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI){
            int netType = AppUtils.getNetWorkType(ctx);
            //如果是在wifi下才加载图片，并且当前网络是wifi,直接加载
            if(netType == AppUtils.NETWORKTYPE_WIFI) {
                loadNormal(ctx, img);
            } else {
                //如果是在wifi下才加载图片，并且当前网络不是wifi，加载缓存
                loadCache(ctx, img);
            }
        }else{
            //如果不是在wifi下才加载图片
            loadNormal(ctx,img);
        }

    }


    /**
     * load image with Glide
     */
    private void loadNormal(Context ctx, ImageLoader img) {
 //       Glide.with(ctx).load(img.getUrl()).placeholder(img.getPlaceHolder()).into(img.getImgView());

        displayImageTarget(img.getImgView(), img.getUrl(), getTarget(img.getImgView(), img.getUrl()));
    }

    /**
     * 加载图片 Target
     *
     * @param imageView
     * @param target
     * @param url
     */
    public void displayImageTarget(ImageView imageView, final String
            url, BitmapImageViewTarget target) {
        Glide.get(imageView.getContext()).with(imageView.getContext())
                .load(url)
                .asBitmap()//强制转换Bitmap
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(target);
    }


    /**
     * 获取BitmapImageViewTarget
     */
    private BitmapImageViewTarget getTarget(ImageView imageView, final String url) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                //缓存Bitmap，以便于在没有用到时，自动回收
                LruCacheUtils.getInstance().addBitmapToMemoryCache(url,
                        resource);
            }
        };
    }


    /**
     *load cache image with Glide
     */
    private void loadCache(Context ctx, ImageLoader img) {
        Glide.with(ctx).using(new StreamModelLoader<String>() {
            @Override
            public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
                return new DataFetcher<InputStream>() {
                    @Override
                    public InputStream loadData(Priority priority) throws Exception {
                        throw new IOException();
                    }

                    @Override
                    public void cleanup() {

                    }

                    @Override
                    public String getId() {
                        return model;
                    }

                    @Override
                    public void cancel() {

                    }
                };
            }
        }).load(img.getUrl()).placeholder(img.getPlaceHolder()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img.getImgView());
    }
}
