package com.lt.hm.wovideo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lt.hm.wovideo.http.HttpUtilBack;
import com.lt.hm.wovideo.utils.TLog;

import java.lang.reflect.Field;

/**
 * Created by leonardo on 16/3/21.
 */
public class BaseFragment extends Fragment implements BaseFragmentInterface,View.OnClickListener,HttpUtilBack {
	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_LOADMORE = 2;
	public static final int STATE_NOMORE = 3;
	public static final int STATE_PRESSNONE = 4;
	private boolean isInit = false;//真正要显示的View是否已经被初始化（正常加载）
	public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
	private boolean isLazyLoad = true;
	private boolean isStart = false;//是否处于可见状态，in the screen
	public static int mState = STATE_NONE;
	protected LayoutInflater inflater;
	private ViewGroup container;
	protected LayoutInflater mInflater;
	private Context context;
	private String title;
	private int iconId;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity().getApplicationContext();
	}

//	@Nullable
//	@Override
//	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		this.mInflater = inflater;
////		if (getLayoutId()!=0){
////			contentView = mInflater.inflate(getLayoutId(),container,false);
////		}
//		return contentView;
//	}


//	//子类通过重写onCreateView，调用setOnContentView进行布局设置，否则contentView==null，返回null
//	@Override
//	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//		this.inflater = inflater;
//		this.container = container;
//		contentView = inflateView(getLayoutId());
//		onCreateView(savedInstanceState);
//		if (contentView == null)
//			return super.onCreateView(inflater, container, savedInstanceState);
//		return contentView;
//	}

	protected void onCreateView(Bundle savedInstanceState) {

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
		TLog.log("TAG", "onDestroy() : ");
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

	public Context getApplicationContext() {
		return context;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}



	// http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	@Override
	public void onDetach() {
		TLog.log("TAG", "onDetach() : ");
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onBefore(String dialog) {

	}

	@Override
	public <T> void onSuccess(T value, int flag) {
		TLog.error("onsuccess-->"+value.toString()+"----flag----"+flag);
	}

	@Override
	public void onFail() {

	}
}
