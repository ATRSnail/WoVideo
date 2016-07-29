package com.lt.hm.wovideo.handler;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.IPhoneDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/9
 */
public class UnLoginHandler {
    public static void unLogin(Context context) {
//        message,negitive_str,negitive,positive_str,positive
        String message = "您尚未登录，是否立即登录";
        String negitive_str = "稍后";
        String positive_str = "前往";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog = DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToLogin(context);
                    }
                }, 200);
            }
        });
        dialog.show();
    }

    public static void unRegist(Context context) {
//        message,negitive_str,negitive,positive_str,positive
        String message = "联通用户注册后可享免流量观看视频，赶快注册吧!";
        String negitive_str = "取消";
        String positive_str = "注册";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog = DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
                UIHelper.ToRegister(context);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void freeDialog(Context context, UserModel model) {
//        message,negitive_str,negitive,positive_str,positive
        String message = "领取0元流量包,免流量观看视频!";
        String negitive_str = "取消";
        String positive_str = "领取";

//        Context context, String message, String negitive_str, DialogInterface.OnClickListener negitive, String positive_str, DialogInterface.OnClickListener positive
        IPhoneDialog dialog = DialogHelp.showDialog(context, message, negitive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, positive_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 16/7/9 跳转 登录页面 ,根据是否返回 当前页面 进行控制需要传递一些额外参数
//                UIHelper.ToRegister(context);
                ACache.get(context).put(model.getId() + "free_tag", "true");
//                model.setIsOpen("true");
//                SharedPrefsUtils.setStringPreference(context, "userinfo", new Gson().toJson(model, UserModel.class));
//                Toast.makeText(context, "开通成功", Toast.LENGTH_SHORT).show();
                purchOrder(model, context);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 支付
     *
     * @param model
     */
    private static void purchOrder(UserModel model, Context mContext) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("cellphone", model.getPhoneNo());
        map.put("spid", "953");
        HttpApis.purchOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("placeOrder_pur"+response);
                ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp, response, String.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    finishOrder(model, mContext);
                } else {
                    Toast.makeText(mContext, resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 完成订单接口
     *
     * @param mContext
     * @param model
     */
    private static void finishOrder(UserModel model, Context mContext) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", model.getId());
        HttpApis.GotZeroOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("placeOrder_finish"+response);
                ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp, response, String.class, RespHeader.class);
                Toast.makeText(mContext, resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();

                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    Toast.makeText(mContext, mContext.getResources().getText(R.string.open_success), Toast.LENGTH_SHORT).show();
                    model.setIsVip("1");
                    SharedPrefsUtils.setStringPreference(mContext, "userinfo", new Gson().toJson(model, UserModel.class));
                    GetPersonInfo(model,mContext);

                } else {
                    Toast.makeText(mContext, mContext.getResources().getText(R.string.open_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void GetPersonInfo(UserModel model,Context mContext) {
        HashMap<String,Object> map= new HashMap<>();
        map.put("userid",model.getId());
        HttpApis.getPersonInfo(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("personInfo"+response);
                ResponseObj<UserModel,RespHeader> resp = new ResponseObj<UserModel, RespHeader>();
                ResponseParser.parse(resp,response,UserModel.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                    UserModel model1= resp.getBody();
                    model.setIsVip("1");
                    TLog.log("got_zero"+model.toString()+":::"+new Gson().toJson(model,UserModel.class));
                    SharedPrefsUtils.setStringPreference(mContext,"userinfo",new Gson().toJson(model,UserModel.class));
                }else{
                    Toast.makeText(mContext,resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
