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
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                startImagePick();
                break;
            case ACTION_TYPE_PHOTO:
                startTakePhoto();
                break;
            default:
                break;
        }
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/WoVideo/Camera/";
            TLog.log(savePath);
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                boolean flagg = savedir.mkdirs();
                if (!flagg) {
                    TLog.log("图片保存失败，请稍后重试");
                    savedir.mkdir();
                }
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
//            ToastUtils.showToastShort("无法保存照片，请检查SD卡是否挂载");
            TLog.log("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;
        if (origUri == null){
            origUri = Uri.fromFile(out);
        }
        TLog.error("origUri---->"+origUri);


        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/WoVideo/Portrait/";
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private final static int CROP = 500;
    private Uri origUri;
    private Uri cropUri;
    private String theLarge;
    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP*2);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                // 获取头像缩略图
                if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                    protraitBitmap = ImageUtils
                            .loadImgThumbnail(protraitPath, 400, 200);
                }
                ACache.get(getApplicationContext()).put("img_back_url", protraitFile.getAbsolutePath());
                break;
        }
    }


    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
//            AppContext.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "wovideo_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }
}
