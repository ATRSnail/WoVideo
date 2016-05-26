package com.lt.hm.wovideo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leonardo on 16/3/21.
 */
public class BaseFragment extends Fragment implements BaseFragmentInterface,View.OnClickListener {
	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_LOADMORE = 2;
	public static final int STATE_NOMORE = 3;
	public static final int STATE_PRESSNONE = 4;

	public static int mState = STATE_NONE;

	protected LayoutInflater mInflater;

	private String title;
	private int iconId;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.mInflater = inflater;
//		if (getLayoutId()!=0){
//			view = mInflater.inflate(getLayoutId(),container,false);
//		}
		View view = super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected int getLayoutId(){
		return 0;
	}
	protected  View inflateView(int resId){
		return this.mInflater.inflate(resId,null);
	}

	public boolean onBackPressed(){
		return false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}


	@Override
	public void initView(View view) {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}



}
