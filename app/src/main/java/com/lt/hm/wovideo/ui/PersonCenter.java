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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.ImageUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class PersonCenter extends BaseActivity implements View.OnClickListener {
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    @BindView(R.id.head_icon)
    ImageView headIcon;
    @BindView(R.id.login_tag)
    TextView loginTag;
    @BindView(R.id.regist_tag)
    TextView registTag;
    @BindView(R.id.person_head_bg)
    LinearLayout personHeadBg;
    @BindView(R.id.order)
    RelativeLayout order;
    @BindView(R.id.integral)
    RelativeLayout integral;
    @BindView(R.id.history)
    RelativeLayout history;
    @BindView(R.id.collect)
    RelativeLayout collect;
    @BindView(R.id.btn_set)
    ImageView btnSet;
    @BindView(R.id.btn_person_back)
    ImageView btnPersonBack;
    @BindView(R.id.unlogin_layout)
    LinearLayout unloginLayout;
    @BindView(R.id.login_layout)
    LinearLayout login_layout;
    @BindView(R.id.pc_username)
    TextView tv_username;
    private Uri origUri;
    private Uri cropUri;
    private String theLarge;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/WoVideo/Portrait/";
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private final static int CROP = 500;



    @Override
    protected int getLayoutId() {
        return R.layout.layout_person;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        overridePendingTransition(R.anim.activity_open,R.anim.activity_stay);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
       String string = ACache.get(this).getAsString("userinfo");
//        headIcon.setImageBitmap(ImageUtils.getBitmap(getApplicationContext(),protraitPath));
        if (FILE_SAVEPATH!=null){
            Glide.with(this).load(ACache.get(this).getAsString("img_url")).asBitmap().centerCrop().error(R.drawable.icon_head).into(headIcon);
        }
        if (!StringUtils.isNullOrEmpty(string)){
            UserModel model = new Gson().fromJson(string,UserModel.class);
            unloginLayout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
            String phoneNum = model.getPhoneNo();
            tv_username.setText(phoneNum.substring(0, phoneNum.length() - (phoneNum.substring(3)).length()) + "****" + phoneNum.substring(7));
        }else{
            unloginLayout.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initViews() {
        btnSet.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        loginTag.setOnClickListener(this);
        registTag.setOnClickListener(this);
        order.setOnClickListener(this);
        integral.setVisibility(View.GONE);
        integral.setOnClickListener(this);
        history.setOnClickListener(this);
        collect.setOnClickListener(this);
        btnPersonBack.setOnClickListener(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set:
                UIHelper.ToSetPage(this);
                break;
            case R.id.head_icon:
                // TODO: 16/6/6  变更头像 上传头像
                handleSelectPicture();

                break;
            case R.id.login_tag:
                UIHelper.ToLogin(this);
                break;
            case R.id.regist_tag:
                UIHelper.ToRegister(this);
                break;
            case R.id.order:
                UIHelper.ToBillsPage(this);
                break;
            case R.id.integral:
                UIHelper.ToMineIntegral(this);
                break;
            case R.id.history:
                // TODO: 16/6/6 观看历史
                UIHelper.ToHistoryPage(this);
                break;
            case R.id.collect:
                // TODO: 16/6/6 我的收藏
                String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
                if (StringUtils.isNullOrEmpty(userinfo)){
                    UnLoginHandler.unLogin(PersonCenter.this);
                }else{
                    UIHelper.ToCollectPage(this);
                }
                break;
            case R.id.btn_person_back:
                PersonCenter.this.finish();
                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.activity_close);
    }
    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(PersonCenter.this, "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
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

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
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
            thePath = ImageUtils.getAbsoluteImagePath(PersonCenter.this, uri);
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

    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
//        showWaitDialog("正在上传头像...");

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
//            AppContext.showToast("图像不存在，上传失败");
            TLog.log("图像不存在，上传失败");
        }
        ACache.get(this).put("img_url",protraitFile.getAbsolutePath());

        if (protraitBitmap != null) {
            String img64= ImageUtils.imgToBase64(protraitFile.getAbsolutePath(),protraitBitmap,"JPG");
            HashMap<String,Object> map= new HashMap<>();
            String string = ACache.get(this).getAsString("userinfo");
            map.put("phone",new Gson().fromJson(string,UserModel.class).getPhoneNo());
            map.put("base",img64);
            TLog.log(map.toString());
            HttpApis.uploadHeadImg(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    Glide.with(PersonCenter.this).load(ACache.get(PersonCenter.this).getAsString("img_url")).asBitmap().centerCrop().error(R.drawable.icon_head).into(headIcon);
                    // TODO: 16/7/6 刷新个人中心头像图片
                }
            });

        }
    }

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
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
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
                uploadNewPhoto();
                break;
        }
    }


}
