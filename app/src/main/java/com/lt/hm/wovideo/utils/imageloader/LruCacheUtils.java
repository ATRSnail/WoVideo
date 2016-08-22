package com.lt.hm.wovideo.utils.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/22
 */
public class LruCacheUtils extends LruCache<String, Bitmap> {

    //获取手机内存大小
    private static int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static LruCacheUtils cacheUtils;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruCacheUtils(int maxSize) {
        super(maxSize);
    }

    public static LruCacheUtils getInstance() {
        if (cacheUtils == null) {
            cacheUtils = new LruCacheUtils(MAXMEMONRY / 5);
        }
        return cacheUtils;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        if (cacheUtils.size() > 0) {
            cacheUtils.evictAll();
        }
    }

    /**
     * 添加缓存图片
     */
    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (cacheUtils.get(key) != null) {
            return;
        }
        if (!isEmpty(key) && bitmap != null) {
            cacheUtils.put(key, bitmap);
        }
    }


    /**
     * 获取缓存图片
     */
    public synchronized Bitmap getBitmapFromMemCache(String key) {
        if (isEmpty(key)) {
            return null;
        }
        Bitmap bm = cacheUtils.get(key);
        if (bm != null && !bm.isRecycled()) {
            return bm;
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (isEmpty(key)) {
            return;
        }
        Bitmap bm = cacheUtils.remove(key);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
    }


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }


}
