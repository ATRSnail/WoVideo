package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class SetPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.set_topbar)
    SecondTopbar setTopbar;
    @BindView(R.id.skin_manager)
    RelativeLayout skinManager;
    @BindView(R.id.cache_size)
    TextView cacheSize;
    @BindView(R.id.clean_cache)
    RelativeLayout cleanCache;
    @BindView(R.id.opinion_reback)
    RelativeLayout opinionReback;
    @BindView(R.id.about_us)
    RelativeLayout aboutUs;
    @BindView(R.id.logout)
    Button logout;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setpage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);
        logout.setOnClickListener((View v)->{
            ACache.get(this).clear();
            UIHelper.ToPerson(this);
        });
        // TODO: 16/6/6  获取缓存尺寸大小 并更新cacheSize
    }

    @Override
    public void initViews() {
        String userinfo = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(userinfo)){
            logout.setVisibility(View.GONE);
        }else{
            logout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initDatas() {
    }

    @OnClick(R.id.skin_manager)
    public void ToSkin() {
//        UIHelper.ToSkinManager(this);
    }

    @OnClick(R.id.clean_cache)
    public void CleanCache() {
        // TODO: 16/6/6  clean cache datas
        Toast.makeText(getApplicationContext(),"数据已清除",Toast.LENGTH_SHORT).show();
        cacheSize.setText("0.00M");
    }

    @OnClick(R.id.opinion_reback)
    public void ToOpinion() {
        UIHelper.ToReCallback(this);
    }

    @OnClick(R.id.about_us)
    public void ToAbout() {
        UIHelper.ToAboutPage(this);
    }
    @OnClick(R.id.logout)
    public void LogOut(){
        // TODO: 16/6/6 注销用户信息。
    }


    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }
}