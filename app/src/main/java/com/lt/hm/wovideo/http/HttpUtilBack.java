package com.lt.hm.wovideo.http;

/**
 * 作者：xch on 2016/7/11 12:01
 * 类说明：
 */
public interface HttpUtilBack {
    void onBefore(String dialog);
    <T> void onSuccess(T value, int flag);
    void onFail();
}
