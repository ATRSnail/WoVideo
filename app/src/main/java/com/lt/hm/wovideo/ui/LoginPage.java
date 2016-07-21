package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.MD5Utils;
import com.lt.hm.wovideo.utils.PhoneUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/27
 */
public class LoginPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.login_topbar)
    SecondTopbar loginTopbar;
    @BindView(R.id.et_login_account)
    EditText etLoginAccount;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.btn_login_submit)
    Button btnLoginSubmit;
    @BindView(R.id.tv_login_forget)
    TextView tvLoginForget;
    @BindView(R.id.tv_login_regist)
    TextView tvLoginRegist;
    ACache aCache;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_login;
    }

    boolean Operators_flag = false;
    @Override
    protected void init(Bundle savedInstanceState) {
        aCache= ACache.get(this);
        loginTopbar.setRightIsVisible(false);
        loginTopbar.setLeftIsVisible(true);
        loginTopbar.setOnTopbarClickListenter(this);
        etLoginAccount.setText("18513179404");
        etLoginPwd.setText("123456");
        etLoginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==11){
                    if (PhoneUtils.isPhoneNum(s.toString())){
                        Operators_flag =true;
//                        regist_validate_layout.setVisibility(View.VISIBLE);
//                        divider_layout2.setVisibility(View.VISIBLE);
                    }else{
                        Operators_flag =false;
                        return;
//                       regist_validate_layout.setVisibility(View.GONE);
//                       divider_layout2.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initViews() {
        btnLoginSubmit.setOnClickListener((View v)->{
            if (TextUtils.isEmpty(etLoginAccount.getText())){
                TLog.log("用户名不能为空");
            }else if (TextUtils.isEmpty(etLoginPwd.getText())){
                TLog.log("密码不能为空");
            }else{
                if (PhoneUtils.isPhoneNum(etLoginAccount.getText().toString())){
                    Operators_flag =true;
                }else{
                    Operators_flag =false;
                }
                if (Operators_flag){
                    ToLogin();
                }else{
                    Toast.makeText(getApplicationContext(),"只支持联通手机号登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvLoginForget.setOnClickListener((View v)->{
            // TODO: 16/6/8 忘记密码 
        });
        tvLoginRegist.setOnClickListener((View v)->{
            UIHelper.ToRegister(this);
            this.finish();
        });
    }

    private void ToLogin() {
        String account = etLoginAccount.getText().toString();
        String pwd = etLoginPwd.getText().toString();
        HashMap<String,Object> map= new HashMap<>();
        map.put("phone",account);
        map.put("passWord", MD5Utils.getMD5Code(pwd).toLowerCase());
        HttpApis.login(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("result:"+response);
                ResponseObj<UserModel, RespHeader> resp = new ResponseObj<UserModel, RespHeader>();
                ResponseParser.loginParse(resp,response,UserModel.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                    UserModel model = resp.getBody();
                    String json = new Gson().toJson(model);
                    cacheUserInfo(json);
                    UIHelper.ToMain2(LoginPage.this);
                    LoginPage.this.finish();
                }else{
                    Toast.makeText(getApplicationContext(),resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cacheUserInfo(String json) {
      aCache.put("userinfo",json);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }
}
