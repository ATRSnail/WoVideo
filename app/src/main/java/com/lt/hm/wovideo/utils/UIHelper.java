package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.content.Intent;

import com.lt.hm.wovideo.ui.LoginPage;
import com.lt.hm.wovideo.ui.MainPage2;
import com.lt.hm.wovideo.ui.RegistPage;
import com.lt.hm.wovideo.ui.VideoPage;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/27
 */
public class UIHelper {


    public static void ToMain2(Context context) {
        Intent intent = new Intent(context, MainPage2.class);
        context.startActivity(intent);
    }

    public static void ToLogin(Context context) {
        Intent intent = new Intent(context, LoginPage.class);
        context.startActivity(intent);
    }

    public static void ToRegister(Context context) {
        Intent intent = new Intent(context, RegistPage.class);
        context.startActivity(intent);
    }

    public static void ToVideo(Context context) {
        Intent intent = new Intent(context, VideoPage.class);
        context.startActivity(intent);
    }
    public static void ToVideo2(Context context) {
        Intent intent = new Intent(context, com.lt.hm.wovideo.video.VideoPage.class);
        context.startActivity(intent);
    }




}
