package com.lt.hm.wovideo.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * @author leonardo
 * @create_date 16/1/5
 * @version 1.0
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private View mConvertView;
	private int mPostion;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
	                  int position) {
		this.mPostion = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
	                             ViewGroup parent, int position, int layoutId) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPostion = position;
			return holder;
		}
	}

	public View getMconvertView() {
		return mConvertView;
	}

	public int getmPostion() {
		return mPostion;
	}

	public <T extends View> T getView(int ViewId) {
		View view = mViews.get(ViewId);
		if (view == null) {
			view = mConvertView.findViewById(ViewId);
			mViews.put(ViewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为TextView 设置字体颜色
	 *
	 * @param viewId
	 * @param color
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int color) {
		TextView view = getView(viewId);
		view.setTextColor(color);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	public ViewHolder setImageDrawable(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	public ViewHolder setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public ViewHolder setMarginTop(int viewId, int dimension) {
		View view = getView(viewId);
		if (Build.VERSION.SDK_INT >= 17) {
			view.setPaddingRelative(0, dimension % 2, 0, 0);
		}
		return this;
	}
}
