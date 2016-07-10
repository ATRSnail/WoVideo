package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.utils.MD5Utils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.TimeCountUtil;
import com.lt.hm.wovideo.utils.UIHelper;
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

    @Override
    protected int getLayoutId() {
        return R.layout.layout_registe;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        registTopbar.setRightIsVisible(false);
        registTopbar.setLeftIsVisible(true);
        registTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {
        validateBtn.setOnClickListener((View v) -> {
            // TODO: 16/6/8  获取验证码
            if (TextUtils.isEmpty(etResigtAccount.getText())) {
                TLog.log("用户名不能为空");
                return;
            } else {
                sendValidateCode();
            }
        });
        btnRegistSubmit.setOnClickListener((View v) -> {
            if (TextUtils.isEmpty(etResigtAccount.getText())) {
                TLog.log("用户名不能为空");
                return;
            } else if (TextUtils.isEmpty(etRegistValidate.getText())) {
                TLog.log("验证码不能为空");
                return;
            } else if (TextUtils.isEmpty(etRegistPwd.getText())) {
                TLog.log("密码不能为空");
                return;
            } else if (TextUtils.isEmpty(etRegistPwdT.getText())) {
                TLog.log("请重新输入密码");
                return;
            } else if (!etRegistPwd.getText().toString().equals(etRegistPwdT.getText().toString())) {
                TLog.log("两次密码 不一致");
                return;
            } else {
                checkValidateCode();
            }

            // TODO: 16/6/12 自行验证 短信验证码
            // TODO: 16/6/8 注册

        });
        tvRegistProtocal.setOnClickListener((View v) -> {
            // TODO: 16/6/8  查看 协议

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
                    Toast.makeText(RegistPage.this, "注册成功", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UIHelper.ToLogin(RegistPage.this);
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
