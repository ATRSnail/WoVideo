package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.content.Intent;

import com.lt.hm.wovideo.ui.MainPage;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/27
 */
public class UIHelper {


    public static void ToMain(Context context){
        Intent intent = new Intent(context, MainPage.class);
        context.startActivity(intent);
    }
}
