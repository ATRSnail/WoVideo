package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lt.hm.wovideo.model.UserModel;

/**
 * Created by xuchunhui on 16/8/16.
 */
public class UserMgr {

    public static UserModel getUseInfo(Context context){
        UserModel model = null;
        String string = SharedPrefsUtils.getStringPreference(context,"userinfo");
        if (!TextUtils.isEmpty(string)) {
             model = new Gson().fromJson(string, UserModel.class);
        }
        return model;
    }
}
