package com.lt.hm.wovideo;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lt.hm.wovideo.base.BaseApplication;
import com.lt.hm.wovideo.widget.GlideImageLoader;
import com.networkbench.agent.impl.NBSAppAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.sharesdk.framework.ShareSDK;
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
        instance = this;
        _context = getApplicationContext();
        _resource = _context.getResources();
        ShareSDK.initSDK(this);
        Stetho.initializeWithDefaults(this);
        initNetWork();
//        initNewsLens();
//        CrashReport.initCrashReport(getApplicationContext(), "3a002a01fb", false);
//        Glide.get(this).register(GlideUrl.class, InputStream.class,
//                new OkHttpClient.Factory(OkHttpUtils.getInstance()));
        initGalleryFinal();
    }

    private void initGalleryFinal() {
        ThemeConfig theme = ThemeConfig.DARK;
//        ThemeConfig theme = new ThemeConfig.Builder()
//                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setEnableCrop(true)
                .setForceCrop(true)//强制裁剪
                .build();
       //配置imageloader
        ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .setAnimation(0)
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * NBSAppAgent init
     */
    private void initNewsLens() {
        NBSAppAgent nbsAppAgent = null;
        nbsAppAgent = NBSAppAgent.setLicenseKey("471bd7f647cc42c6974ef6594675ccc3").setRedirectHost("111.206.135.48:8081").withLocationServiceEnabled(true);
        nbsAppAgent.setHttpEnabled(true);
        nbsAppAgent.start(this.getApplicationContext());
    }

    private void initNetWork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("WoVideo"))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

}
