package com.lt.hm.wovideo.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/29
 */
public class HttpUtils {
    //内网测试
    public final static String HOST = "http://172.16.10.15:9100";
    private static String API_URL = "http://172.16.10.15:9100/wsp-web-restservice/%s";
    //外网
//    public final static String HOST = "http://59.108.94.40:9100";
//    private static String API_URL = "http://59.108.94.40:9100/wsp-web-restservice/%s";
//    public final static String HOST = "http://111.206.135.50:8080";
//    private static String API_URL = "http://111.206.133.134:8080/wsp-web-restservice/%s";

    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    public HttpUtils() {
    }

    public static void clearUserCookies(Context context) {
        // (new HttpClientCookieStore(context)).a();
    }


    public static void formPost(String url, HashMap<String, Object> maps, Callback<?> callback) {
        formPost(url, maps, -1, callback);
    }

    public static void formPost(String url, HashMap<String, Object> maps, int flag, Callback<?> callback) {
        PostFormBuilder builder;
        TLog.error("flag---"+flag);
        if (flag == -1) {//不加flag
            builder = OkHttpUtils.post().url(getAbsoluteApiUrl(url));
        } else {
            builder = OkHttpUtils.post().url(getAbsoluteApiUrl(url)).id(flag);
        }
        if (maps.size() > 0) {
            Set<String> keys = maps.keySet();
            for (String key :
                    keys) {
                if (!StringUtils.isNullOrEmpty(maps.get(key))) {
                    builder.addParams(key, maps.get(key).toString());
                }
            }
        }
        TLog.log("request_params" + maps.toString());
        builder.build().execute(callback);
    }

    public static void stringPost(String url, Object obj, Callback<?> callback) {
        PostStringBuilder builder = OkHttpUtils.postString().url(getAbsoluteApiUrl(url));
        builder.content(new Gson().toJson(obj));
        builder.build().execute(callback);
    }

    /**
     * 单文件上传
     *
     * @param url
     * @param file
     * @param callback
     */
    public static void filePost(String url, File file, FileCallBack callback) {
        PostFileBuilder builder = OkHttpUtils.postFile().url(getAbsoluteApiUrl(url));
        builder.file(file);
        builder.build().execute(callback);
    }

    /**
     * 多文件上传
     *
     * @param url
     * @param files
     * @param headers
     * @param callBack
     */
    public static void multiFilePost(String url, Map<String, File> files, Map<String, String> headers, FileCallBack callBack) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key :
                files.keySet()) {
            builder.addFile("mFile", key, files.get(key));
        }
        builder.params(headers);
        builder.build().execute(callBack);
    }

    //单一文件下载（注意下载文件可以使用FileCallback，需要传入文件需要保存的文件夹以及文件名）
    public static void downFile(String url, FileCallBack callback) {
        GetBuilder builder = OkHttpUtils.get().url(url);
        builder.build().execute(callback);
    }


    /**
     * GET 方法
     *
     * @param url
     * @param maps
     * @param callback
     */
    public static void formGet(String url, HashMap<String, Object> maps, Callback callback) {
        GetBuilder builder = OkHttpUtils.get().url(getAbsoluteApiUrl(url));
        if (maps.size() > 0) {
            Set<String> keys = maps.keySet();
            for (String key :
                    keys) {
                builder.addParams(key, maps.get(key).toString());
            }
        }
        builder.build().execute(callback);
    }

    /**
     * 拼接URL（非％s 形式）
     *
     * @param partUrl
     * @return
     */
    public static String appendUrl(String partUrl) {
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            partUrl = HOST + partUrl;
        }
        return partUrl;
    }

    /**
     * 拼接 URL
     *
     * @param partUrl
     * @return
     */
    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        Log.d("BASE_CLIENT", "request:" + url);
        return url;
    }


}
