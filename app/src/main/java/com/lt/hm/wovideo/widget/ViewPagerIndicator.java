package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import java.util.List;

/**
 * Created by leonardo on 16/3/9.
 */
public class ViewPagerIndicator extends LinearLayout {
	private Paint mPaint;
	private Path mPath;
	private int mTrangleWidth;
	private int mTrangleHeight;
	private static final float RADIO_TRANGLE_WIDTH = 1 / 6f;

	private int mInitTranslationX;
	private int mTranslationX;

	private int mTabInvisibleCount;
	public static final int COUNT_DEFAULT_TAB = 4;
	/**
	 *  三角形底边 最大宽度
	 */
	private  final int  DIMENSION_TRANGLE_WIDTH_MAX = (int) (getScreenWidth()/3 *RADIO_TRANGLE_WIDTH);
	public List<String> mTitles;

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取 可见  Tab 的数量
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
		mTabInvisibleCount = a.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
		if (mTabInvisibleCount < 0) {
			mTabInvisibleCount = COUNT_DEFAULT_TAB;
		}
		a.recycle();

		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 抗锯齿
		mPaint.setColor(context.getResources().getColor(R.color.tab_text_selected));
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
//		canvas.drawPath(mPath, mPaint);
		canvas.drawLine(0,0,mTrangleWidth,0,mPaint);
		canvas.restore();
		super.dispatchDraw(canvas);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int cCount = getChildCount();
		if (cCount == 0) return;
		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LayoutParams params = (LayoutParams) view.getLayoutParams();
			params.weight = 0;
			params.width = getScreenWidth() / mTabInvisibleCount;
			view.setLayoutParams(params);
		}
		setItemClickEvent();
	}

	private int getScreenWidth() {
		WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);

		return metrics.widthPixels;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTrangleWidth = (int) (w / mTabInvisibleCount * RADIO_TRANGLE_WIDTH);
		mTrangleWidth= Math.min(mTrangleWidth,DIMENSION_TRANGLE_WIDTH_MAX);
		mInitTranslationX = w / mTabInvisibleCount / 2 - mTrangleWidth / 2;
		initLines();
	}

	/**
	 * init trangle
	 */
	private void initLines() {
		mTrangleHeight = mTrangleWidth / 2;
		mPath = new Path();
		mPath.moveTo(0, 0);

		mPath.lineTo(mTrangleWidth, 0);
//		mPath.lineTo(mTrangleWidth / 2, -mTrangleHeight);
		mPath.close();
	}

	/**
	 * 指示器  跟随手指进行偏移
	 *
	 * @param position
	 * @param positionOffset 0-1 or 1-0 的梯度值
	 */
	public void scroll(int position, float positionOffset) {
		int tabWidth = getWidth() / mTabInvisibleCount;
		mTranslationX = (int) (tabWidth * (positionOffset + position));
		// 容器移动，当 Tab 处于 移动至最后一个时
		if (mTabInvisibleCount != 1) {
			if (position >= (mTabInvisibleCount - 2) && positionOffset > 0 && getChildCount() > mTabInvisibleCount) {
				this.scrollTo((int) ((position - mTabInvisibleCount + 2) * tabWidth + (int) tabWidth * positionOffset), 0);
			}
		} else {
			this.scrollTo((int) (position * tabWidth + tabWidth * positionOffset), 0);
		}
		//重绘
		invalidate();
	}

	public void setTabItemTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			mTitles = titles;
			for (String title :
					mTitles) {
				addView(generateTextView(title));
			}
			setItemClickEvent();
		}


	}

	private static final int COLOR_TEXT_NORMAL = 0xFF999999;
	private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFFC800;

	// 在 setTabTitles 之前调用
	public void setVisibleTabCount(int Count) {
		mTabInvisibleCount = Count;
	}

	private View generateTextView(String title) {
		TextView textView = new TextView(getContext());
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mTabInvisibleCount;
		textView.setText(title);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView.setTextColor(COLOR_TEXT_NORMAL);
		textView.setLayoutParams(lp);
		return textView;
	}

	private ViewPager mViewPager;

	public interface PageOnChangeListener {
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	public PageOnChangeListener mListener;

	public void setOnPageChangeListener(PageOnChangeListener listener) {
		this.mListener = listener;
	}

	public void setViewPager(ViewPager viewPager, int pos) {
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				scroll(position, positionOffset);
				if (mListener != null) {
					mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}
				hightLightTextView(position);

			}

			@Override
			public void onPageSelected(int position) {
				if (mListener != null) {
					mListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (mListener != null) {
					mListener.onPageScrollStateChanged(state);
				}
			}
		});
		mViewPager.setCurrentItem(pos);
		hightLightTextView(pos);
	}

	public void reSetTextColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}


	/**
	 * 文本高亮Tab文本
	 *
	 * @param pos
	 */
	private void hightLightTextView(int pos) {
		reSetTextColor();
		View view = getChildAt(pos);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
		}
	}

	/**
	 * 设置 Tab 点击事件
	 */
	private void setItemClickEvent(){
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j=i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

}
