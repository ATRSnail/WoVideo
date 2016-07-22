package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.SearchResult;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.MD5Utils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.lt.hm.wovideo.R.id.et_oldpassword;

/**
 * Created by songchenyu on 16/7/22.
 */

public class UpdatePwdActivity extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.set_topbar)
    SecondTopbar setTopbar;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_comforim_pwd)
    EditText etComforimPwd;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(et_oldpassword)
    EditText etOldpassword;
    private UserModel model;
    private String phoneNumber;


    @Override
    protected int getLayoutId() {
        return R.layout.aty_updatepwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {
        String user = ACache.get(getApplicationContext()).getAsString("userinfo");
        model = new Gson().fromJson(user, UserModel.class);
        if (model != null) {
            phoneNumber = model.getPhoneNo();
//            Toast.makeText(this, "您尚未登陆,无法使用此功能!请登陆后再试!", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
        }
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    @OnClick(R.id.btn_finish)
    public void onClick() {
        if (btnCommit()) {
            if (StringUtils.isNullOrEmpty(phoneNumber)) {
                Toast.makeText(this, "号码获取失败!请登陆后重试", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, Object> maps = new HashMap<>();
            maps.put("phone", phoneNumber);
            maps.put("oldPassWord", MD5Utils.getMD5Code(etOldpassword.getText().toString()).toLowerCase());
            maps.put("passWord", MD5Utils.getMD5Code(etComforimPwd.getText().toString()).toLowerCase());
            HttpApis.updatePwd(maps, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log("error:" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    ResponseObj<SearchResult, RespHeader> resp = new ResponseObj<SearchResult, RespHeader>();
                    ResponseParser.parse(resp, response, SearchResult.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        String toastMsg = resp.getHead().getRspMsg();
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                        finish();
                    }else if (resp.getHead().getRspCode().equals(ResponseCode.FAILURE)){
                        String toastMsg = resp.getHead().getRspMsg();
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private boolean btnCommit() {
        String old_pwd=etOldpassword.getText().toString();
        String pwd = etPassword.getText().toString();
        String confirm_pwd = etComforimPwd.getText().toString();
        int oldpwd_length = old_pwd.length();
        int pwd_length = pwd.length();
        int confirm_length = confirm_pwd.length();
        if (oldpwd_length==0){
            Toast.makeText(this, "原密码输入的内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (oldpwd_length < 6 || oldpwd_length > 16){
            etOldpassword.setSelection(oldpwd_length);
            Toast.makeText(this, "原密码长度不符合要求", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwd_length == 0 || confirm_length == 0) {
            Toast.makeText(this, "新密码输入的内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwd_length < 6 || pwd_length > 16 || confirm_length < 6 || confirm_length > 16 ) {
            Toast.makeText(this, "新密码密码长度应在6--16之间", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, "两个新密码不一样,请重新输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
