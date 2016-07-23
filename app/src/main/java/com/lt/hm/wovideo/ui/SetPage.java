package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.AppContext;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.MethodsCompat;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.io.File;

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
    TextView mCacheSize;
    @BindView(R.id.clean_cache)
    RelativeLayout cleanCache;
    @BindView(R.id.opinion_reback)
    RelativeLayout opinionReback;
    @BindView(R.id.about_us)
    RelativeLayout aboutUs;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.modify_pwd)
    RelativeLayout modifyPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setpage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);
        logout.setOnClickListener((View v) -> {
            ACache.get(this).clear();
            UIHelper.ToPerson(this);
            this.finish();
        });
        caculateCacheSize();

        // TODO: 16/6/6  获取缓存尺寸大小 并更新cacheSize
    }

    @Override
    public void initViews() {
        String userinfo = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(userinfo)) {
            logout.setVisibility(View.GONE);
            modifyPwd.setVisibility(View.GONE);
        } else {
            modifyPwd.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initDatas() {
    }

    @OnClick(R.id.skin_manager)
    public void ToSkin() {
        Toast.makeText(getApplicationContext(), "该功能尚未开发完成，敬请期待", Toast.LENGTH_SHORT).show();
//        UIHelper.ToSkinManager(this);
    }

    @OnClick(R.id.clean_cache)
    public void CleanCache() {
        // TODO: 16/6/6  clean cache datas
//        Toast.makeText(getApplicationContext(), "数据已清除", Toast.LENGTH_SHORT).show();
//        mCacheSize.setText("0.00M");
        FileUtil.cleanCacheData(getApplicationContext());
        mCacheSize.setText("0KB");
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
    public void LogOut() {
        // TODO: 16/6/6 注销用户信息。
        ACache.get(getApplicationContext()).put("userinfo","");
        this.finish();
    }

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
//        File filesDir = this.getFilesDir();
        File cacheDir = this.getCacheDir();

//        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(this);
            fileSize += FileUtil.getDirSize(externalCacheDir);
//            fileSize += FileUtil.getDirSize(new File(
//                    ACache.get(getApplicationContext()).);
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        mCacheSize.setText(cacheSize);
    }


    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }

    @OnClick(R.id.modify_pwd)
    public void onClick() {
        UIHelper.ToUpdatePassword(this);
    }
}
