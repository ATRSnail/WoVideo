package com.lt.hm.wovideo.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.AppContext;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.ImageUtils;
import com.lt.hm.wovideo.utils.MethodsCompat;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

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
    String userinfo;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_setpage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userinfo= SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);
        // TODO: 16/6/6  获取缓存尺寸大小 并更新cacheSize
        caculateCacheSize();
        logout.setOnClickListener((View v) -> {
            ACache.get(this).clear();
            SharedPrefsUtils.setStringPreference(getApplicationContext(),"userinfo","");
           // UIHelper.ToPerson(this);
            UpdateRecommedMsg.getInstance().downloadListeners.get(0).onUpdateTagLister();
            this.finish();
        });
    }

    @Override
    public void initViews() {
//        String userinfo = ACache.get(this).getAsString("userinfo");
        String userinfo= SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        if (StringUtils.isNullOrEmpty(userinfo)) {
            logout.setVisibility(View.GONE);
            modifyPwd.setVisibility(View.GONE);
        } else  {
            UserModel model= new Gson().fromJson(userinfo,UserModel.class);
            modifyPwd.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initDatas() {
    }

    @OnClick(R.id.skin_manager)
    public void ToSkin() {
        handleSelectPicture();
//        Toast.makeText(getApplicationContext(), "该功能尚未开发完成，敬请期待", Toast.LENGTH_SHORT).show();
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

//    @OnClick(R.id.logout)
//    public void LogOut() {
//        // TODO: 16/6/6 注销用户信息。
//        ACache.get(getApplicationContext()).put("userinfo","");
//       String userinfo =  SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
//        UserModel model= new Gson().fromJson(userinfo,UserModel.class);
//        model.setIsLogin("false");
//        this.finish();
//    }

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



    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(this, "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                break;
            case ACTION_TYPE_PHOTO:
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                break;
            default:
                break;
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                TLog.error("photo--->"+resultList.get(0).getPhotoPath());
                protraitPath = resultList.get(0).getPhotoPath();
                // 获取头像缩略图
                if (!StringUtils.isEmpty(protraitPath)) {
                    protraitBitmap = ImageUtils
                            .loadImgThumbnail(protraitPath, 400, 200);
                }
                ACache.get(getApplicationContext()).put("img_back_url", protraitPath);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            UT.showNormal(errorMsg);
        }
    };

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    private Bitmap protraitBitmap;
    private String protraitPath;
}
