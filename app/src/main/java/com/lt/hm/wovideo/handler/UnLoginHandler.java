package com.lt.hm.wovideo.handler;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.IPhoneDialog;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/9
 */
public class UnLoginHandler {
    public static void unLogin(Context context){
//        message,negitive_str,negitive,positive_str,positive
                String message="您尚未登录，是否立即登录";
        String negitive_str= "稍后";
        String positive_str="前往";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog= DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
                UIHelper.ToLogin(context);
            }
        });
        dialog.show();
    }

    public static void unRegist(Context context){
//        message,negitive_str,negitive,positive_str,positive
        String message="联通用户注册后可享免流量观看视频，赶快注册吧!";
        String negitive_str= "取消";
        String positive_str="注册";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog= DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
                UIHelper.ToRegister(context);
            }
        });
        dialog.show();
    }

    public static void freeDialog(Context context,UserModel model){
//        message,negitive_str,negitive,positive_str,positive
        String message="领取0元流量包,免流量观看视频!";
        String negitive_str= "取消";
        String positive_str="领取";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog= DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
//                UIHelper.ToRegister(context);
                ACache.get(context).put(model.getId()+"free_tag","true");
                model.setIsOpen("true");
                SharedPrefsUtils.setStringPreference(context,"userinfo",new Gson().toJson(model,UserModel.class));
                Toast.makeText(context,"开通成功",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }





}
