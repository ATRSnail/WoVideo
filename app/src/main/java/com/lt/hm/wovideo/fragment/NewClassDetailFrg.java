package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuchunhui on 16/8/10.
 */
public class NewClassDetailFrg extends BaseFragment {

    private static final String TITLE_TAG = "tag";
    private View view;
    private String title;
    @BindView(R.id.text)
    TextView textView;

    public static NewClassDetailFrg getInstance(String name) {
        NewClassDetailFrg frg = new NewClassDetailFrg();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_TAG, name);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_class_detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);

        initData();
        initView(view);
    }

    @Override
    public void initData() {
        title = getArguments().getString(TITLE_TAG);
    }

    @Override
    public void initView(View view) {
        textView.setText(title);
    }
}
