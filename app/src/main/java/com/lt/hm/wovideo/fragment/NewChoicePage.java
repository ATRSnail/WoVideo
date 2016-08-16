package com.lt.hm.wovideo.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.recommend.TabFragmentAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.interf.OnPlaceChangeListener;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseChannel;
import com.lt.hm.wovideo.ui.PersonalitySet;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/2/16
 */
public class NewChoicePage extends BaseFragment implements OnPlaceChangeListener {
	Unbinder unbinder;
	@BindView(R.id.tablayout)
	TabLayout tabLayout;
	@BindView(R.id.img_plus)
	ImageView vipSelector;
	@BindView(R.id.choice_view_page)
	ViewPager choiceViewPage;
	int CURRENT_POSITION;
	private TabFragmentAdapter tabFragmentAdapter;
	private List<ChannelModel> channels = new ArrayList<>();
	private ChannelModel bean;
	private List<String> mTitles = new ArrayList<>();
	private List<Fragment> fragments = new ArrayList<>();
	private View view;
	private LayoutInflater inflater;
	private ViewGroup container;
	private Bundle savedInstanceState;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			this.inflater = inflater;
			this.container = container;
			this.savedInstanceState = savedInstanceState;
			view = inflater.inflate(getLayoutId(), container, false);
			unbinder = ButterKnife.bind(this, view);
			initView(view);
			initData();
		}
		return view;
	}

	private void getClassInfos() {
		HashMap<String, Object> map = new HashMap<>();
		UserModel userModel = UserMgr.getUseInfo(getApplicationContext());
		if (userModel != null)
			map.put("userid", userModel.getId());
		HttpApis.getIndividuationChannel(map, HttpApis.http_one, new HttpCallback<>(ResponseChannel.class, this));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (unbinder != null) {
			unbinder.unbind();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_new_choice;
	}

	@Override
	public void initView(View view) {
		super.initView(view);
		vipSelector.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getContext(), PersonalitySet.class),34);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
		getClassInfos();
	}

	@Override
	public <T> void onSuccess(T value, int flag) {
		super.onSuccess(value, flag);
		switch (flag) {
			case HttpApis.http_one:
				ResponseChannel channelRe = (ResponseChannel) value;
				channels = channelRe.getBody().getSelectedChannels();
				initBottom("");
				break;
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	private void initBottom(String str) {
		fragments.clear();
		mTitles.clear();
		channels.add(0, new ChannelModel("推荐", ChannelModel.RECOMMEND_ID));
		// TODO: 8/15/16 ADD Current position name
		channels.add(1, new ChannelModel(TextUtils.isEmpty(str)?"地区":str, ChannelModel.LOCAL_ID));

		for (int i = 0; i < channels.size(); i++) {
			bean = channels.get(i);
			mTitles.add(bean.getFunName());
			CommonTypePage page = CommonTypePage.getInstance(bean);
			if (bean.getFunCode().equals(ChannelModel.LOCAL_ID))
				page.setOnPlaceChangeListener(this);
			fragments.add(page);
		}

		TLog.error("title--->"+mTitles.toString());
		tabFragmentAdapter = new TabFragmentAdapter(fragments, mTitles, getChildFragmentManager(), getApplicationContext());
		choiceViewPage.setAdapter(tabFragmentAdapter);
		choiceViewPage.setOffscreenPageLimit(mTitles.size());

		tabLayout.setupWithViewPager(choiceViewPage);
		tabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);
	}

	@Override
	public void onChangePlaceListener(String str) {
		TLog.log("return_str" + str);
		if (!TextUtils.isEmpty(str)) {
			if (tabLayout != null) {
				ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
				ViewGroup vgTab = (ViewGroup) vg.getChildAt(1);
				View tabViewChild = vgTab.getChildAt(1);
				if (tabViewChild instanceof TextView) {
					((TextView) tabViewChild).setText(str);
				}
				// TODO: 8/16/16  refresh  the area fragment data
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case PersonalitySet.RESULT_PERSONALITY:
	//			view = null;
	//			onCreateView(inflater,container,savedInstanceState);
				break;
		}

	}
}
