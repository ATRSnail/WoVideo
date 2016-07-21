package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lt.hm.wovideo.ui.AboutPage;
import com.lt.hm.wovideo.ui.BillsPage;
import com.lt.hm.wovideo.ui.CollectPage;
import com.lt.hm.wovideo.ui.DemandPage;
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
import com.lt.hm.wovideo.ui.RecallBackPage;
import com.lt.hm.wovideo.ui.RegistPage;
import com.lt.hm.wovideo.ui.SearchPage;
import com.lt.hm.wovideo.ui.SearchResultPage;
import com.lt.hm.wovideo.ui.SetPage;
import com.lt.hm.wovideo.ui.SkinManager;
import com.lt.hm.wovideo.ui.StartAdPage;

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
        Intent intent = new Intent(context,SetPage.class);
        context.startActivity(intent);
    }

    public static void ToAboutPage(Context context) {
        Intent intent = new Intent(context,AboutPage.class);
        context.startActivity(intent);
    }

    public static void ToSkinManager(Context context) {
        Intent intent = new Intent(context,SkinManager.class);
        context.startActivity(intent);
    }

    public static void ToReCallback(Context context) {
        Intent intent = new Intent(context,RecallBackPage.class);
        context.startActivity(intent);
    }

    public static void ToMineIntegral(Context context) {
        Intent intent = new Intent(context,MineIntegral.class);
        context.startActivity(intent);
    }

    public static void ToBillsPage(Context context) {
        Intent intent = new Intent(context,BillsPage.class);
        context.startActivity(intent);
    }

    public static void ToHistoryPage(Context context) {
        Intent intent = new Intent(context,HistoryPage.class);
        context.startActivity(intent);
    }

    /**
     * 跳转 我的收藏页面
     * @param context
     */
    public static void ToCollectPage(Context context) {
        Intent intent = new Intent(context,CollectPage.class);
        context.startActivity(intent);
    }

    /**
     * 跳转
     * @param context
     */
    public static void ToSearchPage(Context context) {
        Intent intent = new Intent(context,SearchPage.class);
        context.startActivity(intent);
    }
    /**
     * 跳转点播页面
     * @param context
     */

    public static void ToDemandPage(Context context,Bundle bundle) {
        Intent intent = new Intent(context,DemandPage.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转 开通VIp 页面
     * @param context
     */
    public static void ToOpenVipPage(Context context) {
        Intent intent = new Intent(context,OpenVipActivity.class);
        context.startActivity(intent);
    }


    /**
     * 跳转分类详情页面
     * @param context
     * @param bundle
     */
    public static void ToClassDetailsPage(Context context, Bundle bundle) {
//        Intent intent = new Intent(context,ClassDetailPage.class);
        Intent intent = new Intent(context,NewClassDetailPage.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转电影页面
     * @param context
     */
    public static void ToMoviePage(Context context,Bundle bundle) {
//        Intent intent = new Intent(context,MoviePage.class);
        Intent intent = new Intent(context,NewMoviePage.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转直播页面
     * @param context
     */
    public static void ToLivePage(Context context){
        Intent intent = new Intent(context,LivePage.class);
        context.startActivity(intent);
    }
    /**
     * 跳转个人信息页面
     * @param context
     */
    public static void ToPersonInfoPage(Context context)
    {
        Intent intent = new Intent(context,PersonInfoPage.class);
        context.startActivity(intent);
    }


    public static void ToSearchResultPage(Context context, Bundle bundle) {
        Intent intent = new Intent(context,SearchResultPage.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void ToADPage(Context context) {
        Intent intent = new Intent(context,StartAdPage.class);
        context.startActivity(intent);
    }

    public static void ToGuidePage(Context context) {
        Intent intent = new Intent(context,GuidPage.class);
        context.startActivity(intent);
    }
}
