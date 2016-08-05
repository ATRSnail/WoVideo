package com.lt.hm.wovideo;

import android.os.Bundle;

import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.UIHelper;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/26
 */
public class AppStart extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return super.getLayoutId();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        UIHelper.ToMain2(this);

        // TODO: 16/7/6   缓存  下一次开机广告／检查当前是否有图片 并且是否需要跳转
        String old_path= ACache.get(this).getAsString("ad_path");
        if (StringUtils.isNullOrEmpty(old_path)){
            GotNextAd();
            UIHelper.ToADPage(this);
            this.finish();
        }else{
//            UIHelper.ToMain2(AppStart.this);
        }

//        UIHelper.ToVideo(this);
    }

    private void GotNextAd() {
//        HashMap<String,Object> map = new HashMap<>();
//        HttpApis.getAdInfos(map, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                TLog.log(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                TLog.log(response);
//                // TODO: 16/7/6 存储图片
//            }
//        });
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }
}
