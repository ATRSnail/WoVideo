package com.lt.hm.wovideo;

import com.lt.hm.wovideo.base.BaseApplication;

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
    }

}
