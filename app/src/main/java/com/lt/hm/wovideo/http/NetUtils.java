package com.lt.hm.wovideo.http;

import android.text.TextUtils;

import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseBanner;
import com.lt.hm.wovideo.model.response.ResponseBullet;
import com.lt.hm.wovideo.model.response.ResponseCateTag;
import com.lt.hm.wovideo.model.response.ResponseFilms;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.model.response.ResponseLocalCityModel;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.Bullet;

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
     * @param httpUtilBack
     */
    public static void getYouLikeData(int pageNum, int numPerPage, String seed, String tag, String typeId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage);
        if (!TextUtils.isEmpty(seed)) map.put("seed", seed);
        if (!TextUtils.isEmpty(tag)) map.put("tag", tag);
        if (!TextUtils.isEmpty(typeId)) map.put("typeid", typeId);
        HttpApis.getYouLikeList(map, HttpApis.http_you_like, new HttpCallback<>(ResponseLikeList.class, httpUtilBack));
    }

    /**
     * 获取标签
     * @param type
     */
    public static void getCateTag(String type, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        HttpApis.getCategoryTag(map, HttpApis.http_cate_tag, new HttpCallback<>(ResponseCateTag.class, httpUtilBack));
    }

    /**
     * 获取本地电台列表
     * @param cityCode //默认010北京
     * @param httpUtilBack
     */
    public static void getTvsByCityCode(String cityCode,HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", TextUtils.isEmpty(cityCode) ? "010" : cityCode);
        HttpApis.getTvsByCityCode(map, HttpApis.http_city_tv, new HttpCallback<>(ResponseLocalCityModel.class, httpUtilBack));
    }

    /**
     * 获取视频列表
     * @param channelCode
     * @param pageNum
     * @param numPerPage
     * @param httpUtilBack
     */
    public static void getListByType(String channelCode,int pageNum,int numPerPage,HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelCode", channelCode);
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage + 2);
        HttpApis.getListByType(map, HttpApis.http_video_list, new HttpCallback<>(ResponseFilms.class, httpUtilBack));
    }

    /**
     * bar条
     * @param isVip //0是非vip页面的bar;1是vip页面的bar
     * @param httpUtilBack
     */
    public static void getBarData(int isVip,HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", isVip);
        HttpApis.getBanners(map, HttpApis.http_bar, new HttpCallback<>(ResponseBanner.class, httpUtilBack));
    }

    /**
     * 获取弹幕
     * @param mVideoId
     * @param httpUtilBack
     */
    public static void getBullets(String mVideoId,HttpUtilBack httpUtilBack) {
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("pageNum", 1);
        maps.put("numPerPage", 10000);
        maps.put("vfPlayId", mVideoId);
        HttpApis.getBulletByVideoId(maps, HttpApis.http_bullet, new HttpCallback<>(ResponseBullet.class, httpUtilBack));
    }

    protected void addBullet(Bullet bullet,String mVideoId,HttpUtilBack httpUtilBack) {
        if (UserMgr.isLogin()) {
            HashMap<String, Object> map = new HashMap<>();
            UserModel model = UserMgr.getUseInfo();
            //FIXME user id is incorrect!
            map.put("userId", model.getId());
            map.put("vfPlayId", mVideoId);
            map.put("time", mPlayer.getCurrentPosition() / 1000);
//            map.put("time", bullet.getTime());
            map.put("context", bullet.getContent());
            map.put("fontColor", bullet.getFontColor());
            map.put("fontSize", bullet.getFontSize());
            TLog.log("Bullet", "userId: " + model.getId()
                    + "; videoId: " + mVideoId + "; time: " + mPlayer.getCurrentPosition() / 1000
                    + "; content: " + bullet.getContent() + "; fontColor: " + bullet.getFontColor()
                    + "; fontSize: " + bullet.getFontSize());

            HttpApis.addBullet(map, HttpApis.http_add_bullet, new HttpCallback<>(String.class, this));
        } else {
            UnLoginHandler.unLogin(this);
        }
    }
}
