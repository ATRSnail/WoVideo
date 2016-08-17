package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
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
import com.lt.hm.wovideo.utils.AppUtils;
import com.lt.hm.wovideo.utils.MD5Utils;
import com.lt.hm.wovideo.utils.PhoneUtils;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.TimeCountUtil;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/27
 */
public class RegistPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.regist_topbar)
    SecondTopbar registTopbar;
    @BindView(R.id.et_resigt_account)
    EditText etResigtAccount;
    @BindView(R.id.et_regist_validate)
    EditText etRegistValidate;
    @BindView(R.id.et_regist_pwd)
    EditText etRegistPwd;
    @BindView(R.id.et_regist_pwd_t)
    EditText etRegistPwdT;
    @BindView(R.id.btn_regist_submit)
    Button btnRegistSubmit;
    @BindView(R.id.tv_regist_protocal)
    TextView tvRegistProtocal;
    @BindView(R.id.btn_regist_validate)
    Button validateBtn;
    @BindView(R.id.btn_change_login)
    Button btnChangeLogin;
    @BindView(R.id.regist_validate_layout)
    PercentRelativeLayout regist_validate_layout;
    @BindView(R.id.divider_layout2)
            View divider_layout2;

    boolean Operators_flag = false;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_registe;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        aCache = ACache.get(getApplicationContext());
        registTopbar.setRightIsVisible(false);
        registTopbar.setLeftIsVisible(true);
        registTopbar.setOnTopbarClickListenter(this);
        etResigtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.length()==11){
                   if (PhoneUtils.isPhoneNum(s.toString())){
                       Operators_flag =true;
                       regist_validate_layout.setVisibility(View.VISIBLE);
                       divider_layout2.setVisibility(View.VISIBLE);
                   }else{
                       Operators_flag =false;
                       Toast.makeText(getApplicationContext(),"只支持联通手机号登录",Toast.LENGTH_SHORT).show();
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
        validateBtn.setOnClickListener((View v) -> {
            // TODO: 16/6/8  获取验证码
            if (TextUtils.isEmpty(etResigtAccount.getText())) {
//                TLog.log("用户名不能为空");
                Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            } else {
                    sendValidateCode();
            }
        });
        btnRegistSubmit.setOnClickListener((View v) -> {
            if (TextUtils.isEmpty(etResigtAccount.getText())) {
                TLog.log("用户名不能为空");
                Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            } else if (Operators_flag&&TextUtils.isEmpty(etRegistValidate.getText())) {
                TLog.log("验证码不能为空");
                Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(etRegistPwd.getText())) {
//                TLog.log("密码不能为空");
                Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();

                return;
            } else if (TextUtils.isEmpty(etRegistPwdT.getText())) {
                TLog.log("请重新输入密码");
                Toast.makeText(getApplicationContext(),"请重新输入密码",Toast.LENGTH_SHORT).show();
                return;
            } else if (!etRegistPwd.getText().toString().equals(etRegistPwdT.getText().toString())) {
                TLog.log("两次密码 不一致");
                Toast.makeText(getApplicationContext(),"两次密码 不一致",Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (Operators_flag){
                    checkValidateCode();
                }
            }

            // TODO: 16/6/12 自行验证 短信验证码
            // TODO: 16/6/8 注册

        });
        tvRegistProtocal.setOnClickListener((View v) -> {
            // TODO: 16/6/8  查看 协议
            Toast.makeText(getApplicationContext(),"协议起草中,请稍后",Toast.LENGTH_SHORT).show();
        });

    }

    private void checkValidateCode() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("cellphone", etResigtAccount.getText().toString());
        maps.put("validateCode", etRegistValidate.getText().toString());
        HttpApis.getValidateCode(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp, response, String.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    ToRegist();
                }
            }
        });
    }

    private void ToRegist() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", etResigtAccount.getText().toString());
        map.put("passWord", MD5Utils.getMD5Code(etRegistPwd.getText().toString()).toLowerCase());
        HttpApis.regist(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp, response, String.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    // TODO: 16/6/12 控制跳转
                    // TODO: 16/6/12 提示注册成功
                    ToLogin();
                    SharedPrefsUtils.setBooleanPreference(getApplicationContext(),"regist",true);
                    UpdateRecommedMsg.getInstance().downloadListeners.get(0).onUpdateTagLister();
                    Toast.makeText(RegistPage.this, "注册成功", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        //     UIHelper.ToMain2(RegistPage.this);
                            RegistPage.this.finish();
                        }
                    }, 1000);

                } else {
                    Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
                    TLog.log(resp.toString());
                }
            }
        });
    }

    private ACache aCache;
    private void ToLogin() {
        String account = etResigtAccount.getText().toString();
        String pwd = etRegistPwd.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", account);
        map.put("passWord", MD5Utils.getMD5Code(pwd).toLowerCase());
        map.put("platform", AppUtils.getPlatForms());
        HttpApis.login(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("result:" + response);
                ResponseObj<UserModel, RespHeader> resp = new ResponseObj<UserModel, RespHeader>();
                ResponseParser.loginParse(resp, response, UserModel.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {

                    SharedPrefsUtils.setBooleanPreference(getApplicationContext(),"regist",true);
                    UT.showNormal("注册成功");
                    UserModel model = resp.getBody();
                    String json = new Gson().toJson(model);
                    cacheUserInfo(json);
                    UpdateRecommedMsg.getInstance().downloadListeners.get(0).onUpdateTagLister();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RegistPage.this.finish();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cacheUserInfo(String json) {
        aCache.put("userinfo", json);
        SharedPrefsUtils.setStringPreference(getApplicationContext(),"userinfo",json);
    }

    private void sendValidateCode() {
        TimeCountUtil counter = new TimeCountUtil(RegistPage.this, 60000, 1000, validateBtn);
        counter.start();
        HashMap<String, Object> map = new HashMap<>();
        map.put("cellphone", etResigtAccount.getText().toString());
        HttpApis.sendValidateCode(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
                counter.cancel();
                counter.onFinish();
            }

            @Override
            public void onResponse(String response, int id) {
                ResponseObj<String,RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp,response,String.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Error)){
                    etResigtAccount.setHint(resp.getHead().getRspMsg());
                    counter.cancel();

                }
                TLog.log(response);
            }
        });
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
    @OnClick(R.id.btn_change_login)
    void changeLogin(){
        UIHelper.ToLogin(this);
        this.finish();
    }
}
