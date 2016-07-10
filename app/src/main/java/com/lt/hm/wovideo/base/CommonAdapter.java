package com.lt.hm.wovideo.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author leonardo
 * @create_date 16/1/5
 * @version 1.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mcontext;
	protected List<T> mDatas;
	private int layoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.mcontext = context;
		this.mDatas = datas;
		this.layoutId = layoutId;
	}


	public Context getMcontext() {
		return mcontext;
	}


	public void setMcontext(Context mcontext) {
		this.mcontext = mcontext;
	}


	public List<T> getmDatas() {
		return mDatas;
	}


	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
	}


	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int arg0) {
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mcontext, convertView, parent, postion, layoutId);
		convert(holder, getItem(postion));
		return holder.getMconvertView();
	}

	public abstract void convert(ViewHolder holder, T t);
}
