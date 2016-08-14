package com.lt.hm.wovideo.handler;

import android.content.Context;

import com.google.gson.Gson;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/9/16
 */
public class UserHandler {
	public static boolean isLogin(Context mContext) {
		UserModel model = getUserInfo(mContext);
		if (model != null) {
			return true;
		} else {
			return false;
		}
	}

	public static UserModel getUserInfo(Context mContext) {
		UserModel model = null;
		String userinfo = SharedPrefsUtils.getStringPreference(mContext, "userinfo");
		if (!StringUtils.isNullOrEmpty(userinfo)) {
			model = new Gson().fromJson(userinfo, UserModel.class);
		}
		return model;
	}

	public static boolean isVip(Context mContext) {
		UserModel model = getUserInfo(mContext);
		if (model != null && model.getIsVip().equals("1")) {
			return true;
		} else {
			return false;
		}
	}


}
