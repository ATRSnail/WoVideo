package com.lt.hm.wovideo.widget.Anthology;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.OnMediaOtherListener;
import com.lt.hm.wovideo.model.PlaysListBean;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 */
public class QualityLinear extends LinearLayout {

    private RadioButton tv_liu;
    private RadioButton tv_biao;
    private RadioButton tv_blue;
    private RadioButton tv_gao;
    private RadioButton tv_chao;
    private RadioButton tv_4k;
    private RadioGroup rg_quality;

    private boolean isSelect = true;
    private PlaysListBean playIntro;
    private OnMediaOtherListener listener;

    public QualityLinear(Context context, PlaysListBean playIntro, OnMediaOtherListener listener) {
        super(context);
        this.playIntro = playIntro;
        this.listener = listener;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.linear_quality, this, true);

        rg_quality = (RadioGroup) findViewById(R.id.rg_quality);
        tv_liu = (RadioButton) findViewById(R.id.tv_liu);
        tv_biao = (RadioButton) findViewById(R.id.tv_biao);
        tv_blue = (RadioButton) findViewById(R.id.tv_blue);
        tv_gao = (RadioButton) findViewById(R.id.tv_gao);
        tv_chao = (RadioButton) findViewById(R.id.tv_chao);
        tv_4k = (RadioButton) findViewById(R.id.tv_4k);
        int select_color = Color.parseColor("#666666");
        if (TextUtils.isEmpty(playIntro.getFluentUrl())) {
            tv_liu.setClickable(false);
            tv_liu.setTextColor(select_color);
        } else {
            setSelect(tv_liu);
        }
        if (TextUtils.isEmpty(playIntro.getStandardUrl())) {
            tv_biao.setClickable(false);
            tv_biao.setTextColor(select_color);
        } else {
            setSelect(tv_biao);
        }
        if (TextUtils.isEmpty(playIntro.getBlueUrl())) {
            tv_blue.setClickable(false);
            tv_blue.setTextColor(select_color);
        } else {
            setSelect(tv_blue);
        }
        if (TextUtils.isEmpty(playIntro.getHighUrl())) {
            tv_gao.setClickable(false);
            tv_gao.setTextColor(select_color);
        } else {
            setSelect(tv_gao);
        }
        if (TextUtils.isEmpty(playIntro.getSuperUrl())) {
            tv_chao.setClickable(false);
            tv_chao.setTextColor(select_color);
        } else {
            setSelect(tv_chao);
        }
        if (TextUtils.isEmpty(playIntro.getFkUrl())) {
            tv_4k.setClickable(false);
            tv_4k.setTextColor(select_color);
        } else {
            setSelect(tv_4k);
        }

        rg_quality.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.tv_liu:
                    doSomeTing("流畅", playIntro.getFluentUrl());
                    break;
                case R.id.tv_biao:
                    doSomeTing("标清", playIntro.getStandardUrl());
                    break;
                case R.id.tv_blue:
                    doSomeTing("蓝光", playIntro.getBlueUrl());
                    break;
                case R.id.tv_gao:
                    doSomeTing("高清", playIntro.getHighUrl());
                    break;
                case R.id.tv_chao:
                    doSomeTing("超清", playIntro.getSuperUrl());
                    break;
                case R.id.tv_4k:
                    doSomeTing("4K", playIntro.getFkUrl());
                    break;
            }
        });

    }

    private void setSelect(RadioButton rb) {
        if (isSelect) {
            rb.setChecked(true);
            isSelect = false;
        }
    }

    private void doSomeTing(String FormatName, String FormatUrl) {
        if (listener != null) {
            listener.onQualitySelect(FormatName, FormatUrl);
        }
    }
}
