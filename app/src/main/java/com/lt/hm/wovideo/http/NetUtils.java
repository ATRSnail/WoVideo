package com.lt.hm.wovideo.http;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.TagListModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseBanner;
import com.lt.hm.wovideo.model.response.ResponseBullet;
import com.lt.hm.wovideo.model.response.ResponseCateTag;
import com.lt.hm.wovideo.model.response.ResponseChannel;
import com.lt.hm.wovideo.model.response.ResponseComment;
import com.lt.hm.wovideo.model.response.ResponseFilms;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.model.response.ResponseLiveList;
import com.lt.hm.wovideo.model.response.ResponseLocalCityModel;
import com.lt.hm.wovideo.model.response.ResponsePlayList;
import com.lt.hm.wovideo.model.response.ResponseTag;
import com.lt.hm.wovideo.model.response.ResponseValidateComment;
import com.lt.hm.wovideo.model.response.ResponseVideoRealUrl;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.Bullet;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;

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

    public static void getYouLikeData(int pageNum, int numPerPage, String typeId, int flag, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage);
        if (!TextUtils.isEmpty(typeId)) map.put("typeid", typeId);
        HttpApis.getYouLikeList(map, flag, new HttpCallback<>(ResponseLikeList.class, httpUtilBack));
    }

    /**
     * 获取视频标签
     *
     * @param type
     */
    public static void getCateTag(String type, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        HttpApis.getCategoryTag(map, HttpApis.http_cate_tag, new HttpCallback<>(ResponseCateTag.class, httpUtilBack));
    }

    /**
     * 获取本地电台列表
     *
     * @param cityCode     //默认010北京
     * @param httpUtilBack
     */
    public static void getTvsByCityCode(String cityCode, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", TextUtils.isEmpty(cityCode) ? "010" : cityCode);
        HttpApis.getTvsByCityCode(map, HttpApis.http_city_tv, new HttpCallback<>(ResponseLocalCityModel.class, httpUtilBack));
    }

    /**
     * 获取视频列表
     *
     * @param channelCode
     * @param pageNum
     * @param numPerPage
     * @param httpUtilBack
     */
    public static void getListByType(String channelCode, int pageNum, int numPerPage, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelCode", channelCode);
        map.put("pageNum", pageNum);
        map.put("numPerPage", numPerPage + 2);
        HttpApis.getListByType(map, HttpApis.http_video_list, new HttpCallback<>(ResponseFilms.class, httpUtilBack));
    }

    /**
     * bar条
     *
     * @param isVip        //0是非vip页面的bar;1是vip页面的bar
     * @param httpUtilBack
     */
    public static void getBarData(int isVip, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isVip", isVip);
        HttpApis.getBanners(map, HttpApis.http_bar, new HttpCallback<>(ResponseBanner.class, httpUtilBack));
    }

    /**
     * 获取弹幕
     *
     * @param mVideoId
     * @param httpUtilBack
     */
    public static void getBullets(String mVideoId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("pageNum", 1);
        maps.put("numPerPage", 10000);
        maps.put("vfPlayId", mVideoId);
        HttpApis.getBulletByVideoId(maps, HttpApis.http_bullet, new HttpCallback<>(ResponseBullet.class, httpUtilBack));
    }

    /**
     * 添加弹幕
     *
     * @param bullet
     * @param mVideoId
     * @param CurrentPosition
     * @param httpUtilBack
     */
    public static void addBullet(Bullet bullet, String mVideoId, long CurrentPosition, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        UserModel model = UserMgr.getUseInfo();
        //FIXME user id is incorrect!
        map.put("userId", model.getId());
        map.put("vfPlayId", mVideoId);
        map.put("time", CurrentPosition);
//            map.put("time", bullet.getTime());
        map.put("context", bullet.getContent());
        map.put("fontColor", bullet.getFontColor());
        map.put("fontSize", bullet.getFontSize());
        HttpApis.addBullet(map, HttpApis.http_add_bullet, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 更新服务器头像
     *
     * @param img64
     * @param httpUtilBack
     */
    public static void uploadHeadImg(String img64, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", UserMgr.getUseInfo().getPhoneNo());
        map.put("base", "image/jpg;base64," + img64);
        TLog.log(map.toString());
        HttpApis.uploadHeadImg(map, HttpApis.http_upload_head_img, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 获取个性化标签
     *
     * @param userId
     * @param httpUtilBack
     */
    public static void getPersonalTag(String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) map.put("userid", userId);
        HttpApis.getIndividuationTag(map, HttpApis.http_personal_tag, new HttpCallback<>(ResponseTag.class, httpUtilBack));
    }

    /**
     * 获取个性化频道all
     *
     * @param userId
     * @param httpUtilBack
     */
    public static void getPersonalChannel(String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) map.put("userid", userId);
        HttpApis.getIndividuationChannel(map, HttpApis.http_personal_channel, new HttpCallback<>(ResponseChannel.class, httpUtilBack));
    }

    /**
     * 保存用户频道
     *
     * @param channel
     * @param userId
     * @param httpUtilBack
     */
    public static void updateChannel(String channel, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) map.put("userid", userId);
        map.put("channel", channel);
        HttpApis.updateChannel(map, HttpApis.http_update_personal_channel, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 保存个性化标签
     *
     * @param channel
     * @param userId
     * @param httpUtilBack
     */
    public static void updateTag(String channel, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) map.put("userid", userId);
        map.put("tag", channel);
        HttpApis.updateTag(map, HttpApis.http_update_personal_tag, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 点赞
     *
     * @param playId
     * @param userId
     * @param httpUtilBack
     */
    public static void clickZan(String playId, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vfplayId", playId);
        map.put("userId", userId);
        HttpApis.clickZan(map, HttpApis.http_zan, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 验证敏感词
     *
     * @param s
     * @param httpUtilBack
     */
    public static void checkCommentValide(String s, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("context", s);
        HttpApis.CommentVerification(map, HttpApis.http_valide_comment, new HttpCallback<>(ResponseValidateComment.class, httpUtilBack));
    }

    /**
     * 取消视频收藏
     *
     * @param vfid
     * @param userId
     * @param httpUtilBack
     */
    public static void CancelVideoCollect(String vfid, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", userId);
        map.put("vfids", vfid);
        HttpApis.cancelCollect(map, HttpApis.http_video_uncollect, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 视频收藏
     *
     * @param vfid
     * @param userId
     * @param httpUtilBack
     */
    public static void VideoCollect(String vfid, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", userId);
        map.put("vfids", vfid);
        HttpApis.collectVideo(map, HttpApis.http_video_collect, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 发送评论
     *
     * @param content
     * @param vfid
     * @param userId
     * @param httpUtilBack
     */
    public static void SendComment(String content, String vfid, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("vfId", vfid);
        map.put("comment", content);
        HttpApis.pushComment(map, HttpApis.http_push_comment, new HttpCallback<>(String.class, httpUtilBack));
    }

    /**
     * 获取视频真实的播放地址
     *
     * @param sourceUrl
     * @param userPhone
     * @param httpUtilBack
     */
    public static void getVideoRealURL(String sourceUrl, String userPhone, HttpUtilBack httpUtilBack) {
        getVideoRealURL(sourceUrl, userPhone, HttpApis.http_video_real_url, httpUtilBack);
    }

    public static void getVideoRealURL(String sourceUrl, String userPhone, int flag, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("videoSourceURL", sourceUrl);
        if (UserMgr.isLogin()) {
            maps.put("cellphone", userPhone);
            maps.put("freetag", UserMgr.isVip() ? "1" : "0");
        } else {
            maps.put("cellphone", StringUtils.generateOnlyID());
            maps.put("freetag", "0");
        }
        HttpApis.getVideoRealURL(maps, flag, new HttpCallback<>(ResponseVideoRealUrl.class, httpUtilBack));
    }

    /**
     * 获取网络播放地址
     *
     * @param vfid
     * @param userId
     * @param httpUtilBack
     */
    public static void getVideoFakeUrl(String vfid, String userId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfid);
        if (!TextUtils.isEmpty(userId)) maps.put("userid", userId);
        HttpApis.getVideoListInfo(maps, HttpApis.http_video_fake_url, new HttpCallback<>(ResponsePlayList.class, httpUtilBack));
    }

    /**
     * 获取直播列表播放地址
     *
     * @param httpUtilBack
     */
    public static void getLiveList(HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        HttpApis.getLiveTvList(map, HttpApis.http_video_lives_url, new HttpCallback<>(ResponseLiveList.class, httpUtilBack));
    }

    /**
     * 获取评论列表
     *
     * @param vfId
     * @param httpUtilBack
     */
    public static void getCommentList(String vfId, HttpUtilBack httpUtilBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", UserMgr.getUseId());
        map.put("pageNum", 1);
        map.put("numPerPage", 50);
        map.put("vfId", vfId);
        HttpApis.commentList(map, HttpApis.http_comments, new HttpCallback<>(ResponseComment.class, httpUtilBack));
    }
}
