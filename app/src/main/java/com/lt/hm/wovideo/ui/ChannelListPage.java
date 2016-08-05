package com.lt.hm.wovideo.ui;

import android.os.Bundle;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.widget.CustomTopbar;
import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/2/16
 */
public class ChannelListPage extends BaseActivity implements CustomTopbar.myTopbarClicklistenter {
	@BindView(R.id.channel_topbar)
	CustomTopbar channelTopbar;

	@Override
	protected void init(Bundle savedInstanceState) {
		channelTopbar.setRightIsVisible(true);
		channelTopbar.setLeftIsVisible(true);
		channelTopbar.setRightText("编辑");


	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_channel;
	}

	@Override
	public void initViews() {
		channelTopbar.setOnTopbarClickListenter(this);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void leftClick() {
		this.leftClick();
	}

	@Override
	public void rightClick() {
		// TODO: 8/2/16 控制 跳转 逻辑 添加
	}
}
