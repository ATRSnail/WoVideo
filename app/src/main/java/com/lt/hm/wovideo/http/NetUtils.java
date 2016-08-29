package com.lt.hm.wovideo.http;

import android.text.TextUtils;

import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.response.ResponseLikeList;

import java.util.HashMap;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/29
 */
public class NetUtils {

    /**
     * 猜你喜欢接口调用
     *
     * @param pageNum      //页码
     * @param numPerPage   //每页条数
     * @param seed         //翻页查询种子，这个参数如果不传会随机查询，然后会返回这个值，在翻页的时候要将这个值传入，否则会出现重复推荐，可选
     * @param tag          //兴趣标签，用户的user信息,可选
     * @param typeId       //视频类型，1电影2电视剧3综艺4体育，可选
     * @param httpCallback
     */
    public static void getYouLikeData(int pageNum, int numPerPage, String seed, String tag, String typeId, HttpCallback httpCallback) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage);
        if (!TextUtils.isEmpty(seed)) map.put("seed", seed);
        if (!TextUtils.isEmpty(tag)) map.put("tag", tag);
        if (!TextUtils.isEmpty(typeId)) map.put("typeid", typeId);
        HttpApis.getYouLikeList(map, HttpApis.http_you_like, httpCallback);
    }
}
