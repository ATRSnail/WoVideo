package com.lt.hm.wovideo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lt.hm.wovideo.base.BaseApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/26
 */
public class AppContext extends BaseApplication {
    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        _context = getApplicationContext();
        _resource = _context.getResources();
//        ShareSDK.initSDK(this);
        Stetho.initializeWithDefaults(this);
        initNetWork();
//        Glide.get(this).register(GlideUrl.class, InputStream.class,
//                new OkHttpClient.Factory(OkHttpUtils.getInstance()));
    }

    private void initNetWork() {
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L,TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("WoVideo"))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

}
