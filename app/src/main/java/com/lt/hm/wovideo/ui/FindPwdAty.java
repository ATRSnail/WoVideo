package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.utils.PhoneUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.TimeCountUtil;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by songchenyu on 16/7/21.
 */

public class FindPwdAty extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.set_topbar)
    SecondTopbar setTopbar;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.et_phontnumber)
    EditText etPhontnumber;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    boolean Operators_flag = false;
    @BindView(R.id.sent_yzm)
    Button sentYzm;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_findpwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);

    }

    @Override
    public void initViews() {
        sentYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhontnumber.getText())) {
                    Toast.makeText(FindPwdAty.this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Operators_flag) {
                     sendValidateCode();
                } else {
                    Toast.makeText(getApplicationContext(), "只支持联通手机号登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        etPhontnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 11) {
                    if (PhoneUtils.isPhoneNum(s.toString())) {
                        Operators_flag = true;
                    } else {
                        Operators_flag = false;
                        return;
                    }
                }
            }
        });
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
        if (TextUtils.isEmpty(etPhontnumber.getText())) {
            return;
        } else if (TextUtils.isEmpty(etYanzhengma.getText())){
            return;
        }else {
            checkValidateCode();
        }
    }
    private void checkValidateCode() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("cellphone", etPhontnumber.getText());
        maps.put("validateCode", etYanzhengma.getText());
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
                    UIHelper.ToChangePassword(FindPwdAty.this,etPhontnumber.getText().toString());
                }
            }
        });
    }
    private void sendValidateCode() {
        TimeCountUtil counter = new TimeCountUtil(FindPwdAty.this, 60000, 1000, sentYzm);
        counter.start();
        HashMap<String, Object> map = new HashMap<>();
        map.put("cellphone", etPhontnumber.getText().toString());
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
                    etYanzhengma.setHint(resp.getHead().getRspMsg());
                    counter.cancel();

                }
                TLog.log(response);
            }
        });
    }


}
