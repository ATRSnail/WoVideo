package com.lt.hm.wovideo.http;

import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by xuchunhui on 16/8/11.
 */
public abstract class BeanCallback<T> extends Callback<T>
{

    @Override
    public void onResponse(T response, int id) {
        ResponseObj responseObj = (ResponseObj)response;
    }
}
