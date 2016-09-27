package com.lt.hm.wovideo.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lt.hm.wovideo.http.HttpUtils;
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

        boolean flag = false;
//        boolean flag= SettingUtils.getOnlyWifiLoadImg(ctx);
        //如果不是在wifi下加载图片，直接加载
//        if(!flag){
//            loadNormal(ctx,img);
//            return;
//        }

        int strategy = img.getWifiStrategy();
        if (strategy == ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI) {
            int netType = AppUtils.getNetWorkType(ctx);
            //如果是在wifi下才加载图片，并且当前网络是wifi,直接加载
            if (netType == AppUtils.NETWORKTYPE_WIFI) {
                loadNormal(img);
            } else {
                //如果是在wifi下才加载图片，并且当前网络不是wifi，加载缓存
                loadCache(ctx, img);
            }
        } else {
            //如果不是在wifi下才加载图片
            loadNormal(img);
        }

    }


    /**
     * load image with Glide
     */
    private void loadNormal(ImageLoader img) {
        Glide.with(img.getImgView().getContext())
                .load(HttpUtils.appendUrl(img.getUrl()))
                .placeholder(img.getPlaceHolder())
                //  .asBitmap()//强制转换Bitmap
                .crossFade(1000)
                //  .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(img.getImgView());
    }


    /**
     * load cache image with Glide
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
        }).load(img.getUrl()).placeholder(img.getPlaceHolder()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img.getImgView());
    }
}
