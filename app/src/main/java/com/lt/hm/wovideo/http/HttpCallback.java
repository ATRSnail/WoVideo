package com.lt.hm.wovideo.http;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by xuchunhui on 16/8/11.
 */
public class HttpCallback<T> extends StringCallback {
    private Class<T> clazz;
    private HttpUtilBack httpUtilBack;
    private String dialogStr;

    public HttpCallback(Class<T> clazz, HttpUtilBack httpUtilBack) {
        this(clazz, httpUtilBack, "");
    }

    public HttpCallback(Class<T> clazz, HttpUtilBack httpUtilBack, String dialogStr) {
        this.httpUtilBack = httpUtilBack;
        this.clazz = clazz;
        this.dialogStr = dialogStr;
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        httpUtilBack.onBefore(dialogStr);
        TLog.error("onBefore-->" + request.toString() + "---id---" + id);
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        TLog.error("-onAfter--id---" + id);
        httpUtilBack.onAfter(id);

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        TLog.error("error--->" + e.toString());
        httpUtilBack.onFail(e.toString(),id);
    }

    @Override
    public void onResponse(String response, int id) {
        TLog.error("onResponse==string==" + response + "---id---"+id);
        if (clazz == String.class) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
                if (obj.isNull("body") || obj.getString("body").equals("{}")) {
                    if (obj.has("head")) {
                        JSONObject obj_head = obj.getJSONObject("head");
                        RespHeader header = new Gson().fromJson(obj_head.toString(), RespHeader.class);
                        if (header.getRspCode().equals(ResponseCode.Success)) {
                            httpUtilBack.onSuccess(ResponseCode.Success, id);
                        } else {
                            httpUtilBack.onFail(header.getRspMsg(),id);
                        }
                    }
                } else {
                    httpUtilBack.onSuccess(response, id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                httpUtilBack.onFail(e.toString(),id);
            }

        } else {
            TLog.error("str---->" + response);
            try {
                ResponseObj bean = (ResponseObj) new Gson().fromJson(response, clazz);
                if (((RespHeader) bean.getHead()).getRspCode().equals(ResponseCode.Success)) {
                    TLog.error("onResponse==bean==" + bean.toString());
                    httpUtilBack.onSuccess(bean, id);
                } else {
                    httpUtilBack.onFail(((RespHeader) bean.getHead()).getRspMsg(),id);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                httpUtilBack.onFail(e.toString(),id);
            }

        }
    }
}
