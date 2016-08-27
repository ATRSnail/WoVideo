package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.lt.hm.wovideo.AppContext;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.model.UserModel;

/**
 * Created by xuchunhui on 16/8/16.
 */
public class UserMgr {

    public static final String USER_INFO = "userinfo";
    private static Context context = AppContext.getInstance();

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserModel getUseInfo() {
        UserModel model = null;
        String string = SharedPrefsUtils.getStringPreference(context, USER_INFO);
        if (!TextUtils.isEmpty(string)) {
            model = new Gson().fromJson(string, UserModel.class);
        }
        return model;
    }

    /**
     * 判断是否是vip
     *
     * @return
     */
    public static boolean isVip() {
        String userinfo = SharedPrefsUtils.getStringPreference(context, USER_INFO);
        if (!StringUtils.isNullOrEmpty(userinfo)) {
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            if (model.getIsVip() != null && model.getIsVip().equals("1")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 缓存用户信息
     *
     * @param json
     */
    public static void cacheUserInfo(String json) {
        ACache.get(context).put(USER_INFO, json);
        SharedPrefsUtils.setStringPreference(context, USER_INFO, json);
    }
}
