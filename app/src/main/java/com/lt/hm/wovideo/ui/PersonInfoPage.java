package com.lt.hm.wovideo.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.SearchResult;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.ImageUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.CircleImageView;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.lt.hm.wovideo.R.id.p_info_logo;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/9
 */
public class PersonInfoPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter, View.OnClickListener {
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    @BindView(R.id.person_info_topbar)
    SecondTopbar personInfoTopbar;
    @BindView(p_info_logo)
    CircleImageView pInfoLogo;
    @BindView(R.id.p_info_logo_layout)
    RelativeLayout pInfoLogoLayout;
    @BindView(R.id.nick_layout)
    RelativeLayout nickLayout;
    @BindView(R.id.sex_layout)
    RelativeLayout sexLayout;
    @BindView(R.id.email_layout)
    RelativeLayout emailLayout;
    @BindView(R.id.modify_pwd)
    RelativeLayout modifyPwd;
    @BindView(R.id.nick_name)
    EditText nickName;
    @BindView(R.id.email_arrow)
    ImageView emailArrow;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_email)
    EditText tvEmail;
    @BindView(R.id.spinner)
    Spinner spinner;
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
    private UserModel model;
    private boolean isEdit = false;
    private ArrayAdapter<String> adapter;
    private ACache aCache;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_person_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        aCache= ACache.get(this);
        personInfoTopbar.setRightIsVisible(true);
        personInfoTopbar.setLeftIsVisible(true);
        personInfoTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {
        pInfoLogo.setOnClickListener(this);
        String user = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(user)){
            Toast.makeText(this, "您尚未登陆,无法使用此功能!请登陆后再试!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        model = new Gson().fromJson(user, UserModel.class);
        String img_url = HttpUtils.HOST + model.getHeadImg();
        Glide.with(this).load(img_url).asBitmap().centerCrop().error(R.drawable.icon_head).into(pInfoLogo);
        nickName.setText(model.getNickName());
        nickName.setCursorVisible(false);
        tvEmail.setCursorVisible(false);
        if (!model.getSex().equals("")) {
            int sex_num = Integer.parseInt(model.getSex());
            if (sex_num == 0) {
                tvSex.setText("男");
            } else if (sex_num == 1) {
                tvSex.setText("女");
            }
        } else {
            tvSex.setText("未知");
        }
        if (!model.getEmail().equals("")) {
            tvEmail.setText(model.getEmail());
        }

    }

    @Override
    public void initDatas() {

    }
    //使用XML形式操作
    class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            tvSex.setText(adapter.getItem(position).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        if (!isEdit) {
            personInfoTopbar.setRightImageResource(R.drawable.wancheng
            );
            isEdit = true;
            spinner.setClickable(true);
            nickName.setCursorVisible(true);
            int nickNameLength=nickName.getText().length();
            if (nickNameLength==0){
                nickName.setText(" ");
            }
            nickName.setSelection(nickNameLength);
            nickName.setBackgroundResource(R.drawable.et_persion);
            nickName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length()>8){
                        Toast.makeText(PersonInfoPage.this, "昵称最多只能是8个字哦", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            tvEmail.setCursorVisible(true);
            int emailLength=tvEmail.getText().length();
            if (emailLength==0){
                tvEmail.setText(" ");
//                emailLength=1;
            }
            tvEmail.setSelection(emailLength);
            tvEmail.setBackgroundResource(R.drawable.et_persion);
            initSpinner();
            int sex_num = Integer.parseInt(model.getSex());
            if (sex_num == 0) {
                tvSex.setText("男");
            } else if (sex_num == 1) {
                tvSex.setText("女");
            }
        } else {
            if (isCanCommit()){
                isEdit = false;
                personInfoTopbar.setRightImageResource(R.drawable.bianji);
                nickName.setCursorVisible(false);
                nickName.setBackgroundResource(Color.TRANSPARENT);
                tvEmail.setCursorVisible(false);
                tvEmail.setBackgroundResource(Color.TRANSPARENT);
                spinner.setClickable(false);
//                int sex_num = Integer.parseInt(model.getSex());
//                if (sex_num == 0) {
//                    tvSex.setText("男");
//                } else if (sex_num == 1) {
//                    tvSex.setText("女");
//                }
                sendEdit();
            }
        }

    }
  //"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private boolean isCanCommit(){
        String email=tvEmail.getText().toString();
        Log.e("SCY"," - - - -- -  "+email);
        if (tvSex.getText().toString().equals("")){
            Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (nickName.getText().toString().equals("")){
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (nickName.getText().toString().length()>8){
            Toast.makeText(this, "昵称太长了,试试八个字以内的吧", Toast.LENGTH_SHORT).show();
            return false;
        }else if (email.equals("")){
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (isEmail(email)){
            Toast.makeText(this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
            return false;
        }
        //778260328@qq.com
        return true;
    }
    public boolean isEmail(String strEmail) {
        Pattern p = Pattern.compile("\\\\w+([-+.]\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*");
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    private void sendEdit() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("userid", model.getId());
        maps.put("email", tvEmail.getText().toString());
        int sex=-1;
        if (tvSex.getText().toString().equals("男")){
            sex=0;
        }else {
            sex=1;
        }
        maps.put("sex", sex+"");
        maps.put("nickName", nickName.getText().toString());
        HttpApis.upUsers(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<SearchResult, RespHeader> resp = new ResponseObj<SearchResult, RespHeader>();
                ResponseParser.parse(resp, response, SearchResult.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    String toastMsg=resp.getHead().getRspMsg();
                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void updateUserInfo(){
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("userid", model.getId());
        HttpApis.getUsers(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                ResponseObj<UserModel, RespHeader> resp = new ResponseObj<UserModel, RespHeader>();
                ResponseParser.loginParse(resp,response,UserModel.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                    UserModel model = resp.getBody();
                    String json = new Gson().toJson(model);
                    cacheUserInfo(json);
                }else{
                    Toast.makeText(getApplicationContext(),resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void cacheUserInfo(String json) {
        aCache.put("userinfo",json);
    }
    private void initSpinner(){
        Resources res = getResources();
//        CharSequence[] platforms = res.getTextArray(R.array.sex_value);
        ArrayList<String> strings=new ArrayList<>();
        int sex_num = Integer.parseInt(model.getSex());
        if (sex_num == 0) {
            strings.add("男");
            strings.add("女");
        } else if (sex_num == 1) {
            strings.add("女");
            strings.add("男");
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,strings ) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.spinner_adapter, parent, false);
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText(getItem(position));
                return view;
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 修改Spinner选择后结果的字体颜色
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(PersonInfoPage.this);
                    convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                }
                //此处text1是Spinner默认的用来显示文字的TextView
                TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                tv.setTextColor(Color.TRANSPARENT);
                return convertView;
            }

        };
        //添加事件Spinner事件监听
        SpinnerXMLSelectedListener spinnerXMLSelectedListener=new SpinnerXMLSelectedListener();
        spinner.setOnItemSelectedListener(spinnerXMLSelectedListener);
        spinner.setAdapter(adapter);
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(PersonInfoPage.this, "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
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
            thePath = ImageUtils.getAbsoluteImagePath(PersonInfoPage.this, uri);
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
        ACache.get(this).put("img_url", protraitFile.getAbsolutePath());

        if (protraitBitmap != null) {
            String img64 = ImageUtils.imgToBase64(protraitFile.getAbsolutePath(), protraitBitmap, "JPG");
            HashMap<String, Object> map = new HashMap<>();
            String string = ACache.get(this).getAsString("userinfo");
            map.put("phone", new Gson().fromJson(string, UserModel.class).getPhoneNo());
            map.put("base", "image/jpg;base64," + img64);
            TLog.log(map.toString());
            HttpApis.uploadHeadImg(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    updateUserInfo();
                    Glide.with(PersonInfoPage.this).load(ACache.get(PersonInfoPage.this).getAsString("img_url")).asBitmap().centerCrop().error(R.drawable.icon_head).into(pInfoLogo);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case p_info_logo:
                // TODO: 16/6/6  变更头像 上传头像
                String user = ACache.get(getApplicationContext()).getAsString("userinfo");
                if (StringUtils.isNullOrEmpty(user)) {
                    Toast.makeText(this, getResources().getString(R.string.no_login_toast), Toast.LENGTH_SHORT).show();
                } else {
                    handleSelectPicture();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PersonInfoPage Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
