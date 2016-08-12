package com.lt.hm.wovideo.http;

import com.google.gson.Gson;
import com.lt.hm.wovideo.utils.TLog;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

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
        this(clazz,httpUtilBack,"");
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
    public void onError(Call call, Exception e, int id) {
        TLog.error( "error--->" + e.toString());
        httpUtilBack.onFail();
    }

    @Override
    public void onResponse(String response, int id) {
        TLog.error("onResponse==string==" + response);
        if (clazz == String.class) {
            httpUtilBack.onSuccess(response, id);
        } else {
            ResponseObj bean = (ResponseObj) new Gson().fromJson(response, clazz);
            if (((RespHeader)bean.getHead()).getRspCode().equals(ResponseCode.Success)) {
                TLog.error("onResponse==bean==" + bean.toString());
                httpUtilBack.onSuccess(bean, id);
            } else {
                httpUtilBack.onFail();
            }
        }
    }
}
