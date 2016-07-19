package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.sendemail.SendEmailTools;
import com.lt.hm.wovideo.sendemail.ZhengZeTools;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;

import static com.lt.hm.wovideo.R.id.callback_content;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class RecallBackPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.callback_topbar)
    SecondTopbar callbackTopbar;
    @BindView(callback_content)
    EditText callbackContent;
    @BindView(R.id.content_count)
    TextView contentCount;
    @BindView(R.id.contact_mathods)
    EditText contactMathods;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recallback;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        callbackTopbar.setRightIsVisible(true);
        callbackTopbar.setLeftIsVisible(true);
        callbackTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {
        callbackContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==190){
                    Toast.makeText(RecallBackPage.this,  getResources().getString(R.string.option_phone_toast), Toast.LENGTH_SHORT).show();
                }
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
        // TODO: 16/6/12 提交触发
        String content=callbackContent.getText().toString();
        String phone=contactMathods.getText().toString();
        if (content.equals("")){
            callbackContent.setSelection(content.length());
            Toast.makeText(RecallBackPage.this, getResources().getString(R.string.option_content), Toast.LENGTH_SHORT).show();
            return;
        }else if (phone.equals("")){
            contactMathods.setSelection(phone.length());
            Toast.makeText(RecallBackPage.this,  getResources().getString(R.string.option_phone), Toast.LENGTH_SHORT).show();
            return;
        }else if (callbackContent.length()>200){
            Toast.makeText(RecallBackPage.this,  getResources().getString(R.string.option_content_toast), Toast.LENGTH_SHORT).show();
            return;
        }else {
            Log.e("SCY"," -- -- -  - ");
            if (ZhengZeTools.isMobileNO(phone)||ZhengZeTools.isEmail(phone)||ZhengZeTools.isQQ(phone)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendEmailTools.send_mail_file("beijing_shuaidan@163.com",
                                "beijing_shuaidan@163.com", "SMTP.163.COM",
                                "beijing_shuaidan@163.com", "123henan123", " 意见反馈 ",
                                "联系方式:  "+phone+"  "+" 征集内容: "+content, null);
                    }
                }).start();
                Toast.makeText(RecallBackPage.this,  getResources().getString(R.string.success_toast), Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(RecallBackPage.this,  getResources().getString(R.string.option_phone_toast), Toast.LENGTH_SHORT).show();
            }
        }

    }

}

