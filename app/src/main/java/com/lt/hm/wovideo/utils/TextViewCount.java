package com.lt.hm.wovideo.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/4/11
 */
public class TextViewCount extends CountDownTimer {
	private Activity mActivity;
	private TextView btn;//按钮

	public TextViewCount(long millisInFuture, long countDownInterval, Activity mActivity, TextView btn) {
		super(millisInFuture, countDownInterval);
		this.mActivity = mActivity;
		this.btn = btn;
	}

	@Override
	public void onTick(long millisUntilFinished) {
//		btn.setClickable(false);//设置不能点击
		btn.setText("跳过"+millisUntilFinished / 1000 + "s");//设置倒计时时间

		//设置按钮为灰色，这时是不能点击的
		Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
		btn.setText(span);
	}

	@Override
	public void onFinish() {
		mActivity.finish();
//		btn.setText("获取验证码");
//		btn.setClickable(true);//重新获得点击
	}
}
