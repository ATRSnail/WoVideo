package com.lt.hm.wovideo.http;

import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class HttpApis {
	/**
	 * 登录
	 *
	 * @param maps
	 * @param callback
	 */
	public static void login(HashMap<String, Object> maps, Callback callback) {
		HttpUtils.formPost("mblUser/login", maps, callback);
	}

	/**
	 * 注册接口
	 *
	 * @param maps
	 * @param callback
	 */
	public static void regist(HashMap<String, Object> maps, Callback callback) {
		HttpUtils.formPost("mblUser/register", maps, callback);
	}

	/**
	 * 发送验证码
	 *
	 * @param map
	 * @param callback
	 */
	public static void sendValidateCode(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblIndex/sendSmsValidateCode", map, callback);
	}

	/**
	 * 校验验证码
	 *
	 * @param map
	 * @param callback
	 */
	public static void getValidateCode(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblIndex/smsOfferNumber", map, callback);
	}

	/**
	 * 获取 广告栏接口（推荐页面和 Vip 会员页面）
	 *
	 * @param maps
	 * @param callback
	 */
	public static void getBanners(HashMap<String, Object> maps, StringCallback callback) {
		HttpUtils.formPost("mblBanner/getBanner", maps, callback);
	}

	/**
	 * 获取 推荐 数据 （包括 水平和 纵向  接口）
	 *
	 * @param maps
	 * @param callback
	 */
	public static void getHotRecomm(HashMap<String, Object> maps, StringCallback callback) {
		HttpUtils.formPost("mblBanner/getRec", maps, callback);
	}

	/**
	 * 获取 分类信息
	 *
	 * @param map
	 * @param callback
	 */
	public static void getClassesInfo(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblType/getTypeList", map, callback);
	}

	/**
	 * 获取 直播节目单列表
	 *
	 * @param map
	 * @param callback
	 */
	public static void getLiveTvList(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblLive/getLiveTvList", map, callback);
	}

	/**
	 * 获取视频 真实播放地址
	 *
	 * @param map
	 * @param callback
	 */
	public static void getVideoRealURL(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblIndex/getPlayVideoURL", map, callback);
	}

	/**
	 * 根据类型获取 视频列表数据
	 *
	 * @param map
	 * @param callback
	 */
	public static void getListByType(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/getVfListByType", map, callback);
	}

	/**
	 * 获取视频详情信息
	 *
	 * @param map
	 * @param callback
	 */
	public static void getVideoInfo(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/getVfInfo", map, callback);
	}

	/**
	 * 获取视频第一级播放地址使用
	 *
	 * @param map
	 * @param callback
	 */
	public static void getVideoListInfo(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/getPlaysListByVf", map, callback);
	}

	/**
	 * 猜你喜欢数据
	 *
	 * @param map
	 * @param callback
	 */
	public static void getYouLikeList(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/youLike", map, callback);
	}

	/**
	 * 下单接口
	 *
	 * @param map
	 * @param callback
	 */
	public static void getPlaceOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/placeOrder", map, callback);

	}

	/**
	 * 购买
	 *
	 * @param map
	 * @param callback
	 */
	public static void purchOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblIndex/order", map, callback);
	}

	/**
	 * 完成订单
	 *
	 * @param map
	 * @param callback
	 */
	public static void finishOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/overOrder", map, callback);
	}

	/**
	 * 获取订单列表
	 *
	 * @param map
	 * @param callback
	 */
	public static void getBills(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/orderList", map, callback);
	}

	/**
	 * 搜索
	 *
	 * @param map
	 * @param callback
	 */
	public static void searchPage(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/getVfListByName", map, callback);
	}

	/**
	 * 上传头像
	 *
	 * @param map
	 * @param callback
	 */
	public static void uploadHeadImg(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblUser/upHeadImg", map, callback);
	}

	/**
	 * 获取开屏广告信息
	 *
	 * @param map
	 * @param callback
	 */
	public static void getAdInfos(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("", map, callback);
	}

	/**
	 * 修改个人信息
	 *
	 * @param map
	 * @param callback
	 */
	public static void upUsers(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblUser/upUser", map, callback);
	}

	/**
	 * 请求个人信息
	 *
	 * @param map
	 * @param callback
	 */
	public static void getUsers(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblUser/selUserByid", map, callback);
	}

	/**
	 * 收藏
	 *
	 * @param map
	 * @param callback
	 */
	public static void collectVideo(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblCollection/saveMyColl", map, callback);
	}

	/**
	 * 收藏列表
	 *
	 * @param map
	 * @param callback
	 */
	public static void collectList(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblCollection/collList", map, callback);
	}

	/**
	 * 评论列表
	 *
	 * @param map
	 * @param callback
	 */
	public static void commentList(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/viewComment", map, callback);
	}

	/**
	 * 提交评论
	 *
	 * @param map
	 * @param callback
	 */
	public static void pushComment(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/addComment", map, callback);
	}

	/**
	 * 根据类型获取 弹幕数据
	 *
	 * @param map
	 * @param callback
	 */
	public static void getBulletByVideoId(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/viewBarrage", map, callback);
	}

	/**
	 * 对指定视频添加弹幕
	 *
	 * @param map
	 * @param callback
	 */
	public static void addBullet(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/addBarrage", map, callback);
	}

	/**
	 * 对指定视频添加弹幕
	 *
	 * @param map
	 * @param callback
	 */
	public static void updatePwd(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblUser/upPass", map, callback);
	}

	/**
	 * 取消收藏
	 *
	 * @param map
	 * @param callback
	 */
	public static void cancelCollect(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblCollection/cancelMyColls", map, callback);
	}

	/**
	 * 增加点击量接口
	 *
	 * @param map
	 * @param callback
	 */
	public static void addVideoHit(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVf/addHit", map, callback);
	}

	/**
	 * 评论敏感词校验
	 *
	 * @param map
	 * @param callback
	 */
	public static void CommentVerification(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblSensitiveWord/filter", map, callback);
	}

	/**
	 * 0元购接口返回
	 *
	 * @param map
	 * @param callback
	 */
	public static void GotZeroOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/zeroOrder", map, callback);
	}

	/**
	 * 获取个人信息接口
	 *
	 * @param map
	 * @param callback
	 */
	public static void getPersonInfo(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblUser/selUserByid", map, callback);
	}

	/**
	 * 退订接口
	 *
	 * @param map
	 * @param callback
	 */
	public static void cancleOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblIndex/cancelOrder", map, callback);
	}

	/**
	 * 更新 APP
	 *
	 * @param map
	 * @param callback
	 */
	public static void updateApp(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblVersion/getVersionInfo", map, callback);
	}

	/**
	 * 订购回执 HM Inter
	 *
	 * @param map
	 * @param callback
	 */
	public static void receiptOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/aoyunOrder", map, callback);

	}

	/**
	 * 退订回执
	 *
	 * @param map
	 * @param callback
	 */
	public static void receiptCancelOrder(HashMap<String, Object> map, StringCallback callback) {
		HttpUtils.formPost("mblOrder/unaoyunOrder", map, callback);
	}


}
