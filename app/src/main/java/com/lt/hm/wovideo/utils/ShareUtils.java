package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.lt.hm.wovideo.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import msgcopy.wovideo.onekeyshare.OnekeyShare;
import msgcopy.wovideo.onekeyshare.OnekeyShareTheme;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/2
 */
public class ShareUtils {
    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    public static void showShare(Context context, String platformToShare, boolean showContentEdit,String title,String desc, String img_url,String shareURL) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("myapp://jp.app/openwith?name=zhangsan&age=26");
        oks.setTitleUrl(shareURL);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(desc);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(img_url);
        // url仅在微信（包括好友和朋友圈）中使用
       // oks.setUrl("myapp://jp.app/openwith?name=zhangsan&age=26");
        oks.setUrl(shareURL);
        // 启动分享GUI
        oks.show(context);

    }

}
