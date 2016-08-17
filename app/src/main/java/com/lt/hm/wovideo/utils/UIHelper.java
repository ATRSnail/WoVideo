package com.lt.hm.wovideo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.ui.AboutPage;
import com.lt.hm.wovideo.ui.BillsPage;
import com.lt.hm.wovideo.ui.ChangePassword1Aty;
import com.lt.hm.wovideo.ui.CollectPage;
import com.lt.hm.wovideo.ui.DemandPage;
import com.lt.hm.wovideo.ui.EventsPageActivity;
import com.lt.hm.wovideo.ui.FindPwdAty;
import com.lt.hm.wovideo.ui.GuidPage;
import com.lt.hm.wovideo.ui.HistoryPage;
import com.lt.hm.wovideo.ui.LivePage;
import com.lt.hm.wovideo.ui.LoginPage;
import com.lt.hm.wovideo.ui.MainPage2;
import com.lt.hm.wovideo.ui.MineIntegral;
import com.lt.hm.wovideo.ui.NewClassDetailPage;
import com.lt.hm.wovideo.ui.NewMoviePage;
import com.lt.hm.wovideo.ui.OpenVipActivity;
import com.lt.hm.wovideo.ui.PersonCenter;
import com.lt.hm.wovideo.ui.PersonInfoPage;
import com.lt.hm.wovideo.ui.PersonalitySet;
import com.lt.hm.wovideo.ui.RecallBackPage;
import com.lt.hm.wovideo.ui.RegistPage;
import com.lt.hm.wovideo.ui.SearchPage;
import com.lt.hm.wovideo.ui.SearchResultPage;
import com.lt.hm.wovideo.ui.SetPage;
import com.lt.hm.wovideo.ui.SkinManager;
import com.lt.hm.wovideo.ui.StartAdPage;
import com.lt.hm.wovideo.ui.UpdatePwdActivity;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/27
 */
public class UIHelper {


	public static void ToMain2(Context context) {
		Intent intent = new Intent(context, MainPage2.class);
		context.startActivity(intent);
	}

	public static void ToLogin(Context context) {
		Intent intent = new Intent(context, LoginPage.class);
		context.startActivity(intent);
	}

	public static void ToRegister(Context context) {
		Intent intent = new Intent(context, RegistPage.class);
		context.startActivity(intent);
	}


	public static void ToPerson(Context context) {
		Intent intent = new Intent(context, PersonCenter.class);
		context.startActivity(intent);
//        context.overridePendingTransition(R.anim.activity_open,0);

	}

	public static void ToSetPage(Context context) {
		Intent intent = new Intent(context, SetPage.class);
		context.startActivity(intent);
	}

	public static void ToAboutPage(Context context) {
		Intent intent = new Intent(context, AboutPage.class);
		context.startActivity(intent);
	}

	public static void ToSkinManager(Context context) {
		Intent intent = new Intent(context, SkinManager.class);
		context.startActivity(intent);
	}

	public static void ToChangePassword(Context context, String phoneNumber) {
		Intent intent = new Intent(context, ChangePassword1Aty.class);
		intent.putExtra("phoneNumber", phoneNumber);
		context.startActivity(intent);
	}

	public static void ToUpdatePassword(Context context) {
		Intent intent = new Intent(context, UpdatePwdActivity.class);
		context.startActivity(intent);
	}

	public static void ToReCallback(Context context) {
		Intent intent = new Intent(context, RecallBackPage.class);
		context.startActivity(intent);
	}

	public static void ToMineIntegral(Context context) {
		Intent intent = new Intent(context, MineIntegral.class);
		context.startActivity(intent);
	}

	public static void ToBillsPage(Context context) {
		Intent intent = new Intent(context, BillsPage.class);
		context.startActivity(intent);
	}

	public static void ToHistoryPage(Context context) {
		Intent intent = new Intent(context, HistoryPage.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转 我的收藏页面
	 *
	 * @param context
	 */
	public static void ToCollectPage(Context context) {
		Intent intent = new Intent(context, CollectPage.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到标签页
	 * @param context
     */
	public static void ToTagPage(Context context) {
		Intent intent = new Intent(context, PersonalitySet.class);
		intent.putExtra("isTag",true);
		context.startActivity(intent);
	}

	/**
	 * 从我的信息跳进去的
	 * @param context
	 * @param isEdit //编辑/跳过
     */
	public static void ToTagPage(Context context,boolean isEdit) {
		Intent intent = new Intent(context, PersonalitySet.class);
		intent.putExtra("isTag",true);
		intent.putExtra("isEdit",isEdit);
		context.startActivity(intent);
	}

	/**
	 * 跳转
	 *
	 * @param context
	 */
	public static void ToSearchPage(Context context) {
		Intent intent = new Intent(context, SearchPage.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转
	 *
	 * @param context
	 */
	public static void ToEventPage(Context context) {
		Intent intent = new Intent(context, EventsPageActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转点播页面
	 *
	 * @param context
	 */

	public static void ToDemandPage(Context context, Bundle bundle) {
		Intent intent = new Intent(context, DemandPage.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 跳转 开通VIp 页面
	 *
	 * @param context
	 */
	public static void ToOpenVipPage(Context context) {
		Intent intent = new Intent(context, OpenVipActivity.class);
		context.startActivity(intent);
	}


	/**
	 * 跳转分类详情页面
	 *
	 * @param context
	 * @param bundle
	 */
	public static void ToClassDetailsPage(Context context, Bundle bundle) {
//        Intent intent = new Intent(context,ClassDetailPage.class);
		Intent intent = new Intent(context, NewClassDetailPage.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 跳转电影页面
	 *
	 * @param context
	 */
	public static void ToMoviePage(Context context, Bundle bundle) {
//        Intent intent = new Intent(context,MoviePage.class);
		Intent intent = new Intent(context, NewMoviePage.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 跳转直播页面
	 *
	 * @param context
	 */
	public static void ToLivePage(Context context) {
		Intent intent = new Intent(context, LivePage.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转直播页面
	 *
	 * @param context
	 * @param bundle
     */
	public static void ToLivePage(Context context,Bundle bundle) {
		Intent intent = new Intent(context, LivePage.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 跳转个人信息页面
	 *
	 * @param context
	 */
	public static void ToPersonInfoPage(Context context) {
		Intent intent = new Intent(context, PersonInfoPage.class);
		context.startActivity(intent);
	}


	public static void ToSearchResultPage(Context context, Bundle bundle) {
		Intent intent = new Intent(context, SearchResultPage.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void ToADPage(Context context) {
		Intent intent = new Intent(context, StartAdPage.class);
		context.startActivity(intent);
	}

	public static void ToGuidePage(Context context) {
		Intent intent = new Intent(context, GuidPage.class);
		context.startActivity(intent);
	}

	public static void ToFindPwd(Context context) {
		Intent intent = new Intent(context, FindPwdAty.class);
		context.startActivity(intent);
	}

	public static void ToAllCateVideo(Activity activity,int typeId, String vfId){
		if (typeId == VideoType.MOVIE.getId()) {
			// TODO: 16/6/14 跳转电影页面
			Bundle bundle = new Bundle();
			bundle.putString("id", vfId);
			bundle.putInt("typeId", VideoType.MOVIE.getId());
			ToMoviePage(activity, bundle);
		} else if (typeId == VideoType.TELEPLAY.getId()) {
			// TODO: 16/6/14 跳转电视剧页面
			Bundle bundle = new Bundle();
			bundle.putString("id", vfId);
			bundle.putInt("typeId", VideoType.TELEPLAY.getId());
			ToDemandPage(activity, bundle);

		} else if (typeId == VideoType.SPORTS.getId()) {
			// TODO: 16/6/14 跳转 体育播放页面
			Bundle bundle = new Bundle();
			bundle.putString("id", vfId);
			bundle.putInt("typeId", VideoType.SPORTS.getId());
			ToDemandPage(activity, bundle);
		} else if (typeId == VideoType.VARIATY.getId()) {
			// TODO: 16/6/14 跳转综艺界面
			Bundle bundle = new Bundle();
			bundle.putString("id", vfId);
			bundle.putInt("typeId", VideoType.VARIATY.getId());
			ToDemandPage(activity, bundle);
		} else if (typeId == VideoType.SMIML.getId()){
			// TODO: 16/6/14 跳转小视屏页面
			Bundle bundle = new Bundle();
			bundle.putString("id", vfId);
			bundle.putInt("typeId", VideoType.SMIML.getId());
			ToMoviePage(activity, bundle);
		}
	}

}
