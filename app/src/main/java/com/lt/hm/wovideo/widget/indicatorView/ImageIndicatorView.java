package com.lt.hm.wovideo.widget.indicatorView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.interf.onChangeLister;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * user guide, image indicator
 *
 * @author savant-pan
 */
public class ImageIndicatorView extends RelativeLayout {
    /**
     * user guide anchor
     */
    public static final int INDICATE_USERGUIDE_STYLE = 1;
    private Context mContext;
    /**
     * ViewPager
     */
    private ViewPager viewPager;
    /**
     * anchor container
     */
    private LinearLayout indicateLayout;
    /**
     * page vies list
     */
    private List<View> viewList = new ArrayList<View>();
    private Handler refreshHandler;
    /**
     * item changed listener
     */
    private OnItemChangeListener onItemChangeListener;
    private onChangeLister onChangeLister;
    /**
     * item clicked listener
     */
    private OnItemClickListener onItemClickListener;
    /**
     * page total count
     */
    private int totelCount = 0;
    /**
     * current page
     */
    private int currentIndex = 0;
    /**
     * INDICATOR style
     */
    private int indicatorStyle = INDICATE_USERGUIDE_STYLE;

    /**
     * latest scroll time
     */
    private long refreshTime = 0l;

    public ImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ImageIndicatorView(Context context) {
        super(context);
        this.init(context);
    }

    /**
     * @param context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.image_indicator_layout, this);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.indicateLayout = (LinearLayout) findViewById(R.id.image_indicater_layout);

        this.viewPager.addOnPageChangeListener(new PageChangeListener());

        this.refreshHandler = new ScrollIndicateHandler(ImageIndicatorView.this);
        this.mContext = context;
    }

    /**
     * get ViewPager object
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * get current index
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * git view count
     */
    public int getTotalCount() {
        return this.totelCount;
    }

    /**
     * get latest scroll time
     */
    public long getRefreshTime() {
        return this.refreshTime;
    }

    /**
     * add single View
     *
     * @param view
     */
    public void addViewItem(View view) {
        final int position = viewList.size();
        view.setOnClickListener(new ItemClickListener(position));
        this.viewList.add(view);

    }

    /**
     * set Drawable array
     *
     * @param resArray Drawable array
     */
    public void setupLayoutByDrawable(final Integer resArray[]) {
        if (resArray == null)
            throw new NullPointerException();

        this.setupLayoutByDrawable(Arrays.asList(resArray));
    }

    /**
     * set Drawable list
     * 消除 数据刷新 导致的 图片和 指示器重复的问题
     *
     * @param resList Drawable list
     */
    public void setupLayoutByDrawable(final List<Integer> resList) {
        if (resList == null)
            throw new NullPointerException();
        if (this.viewList.size() > 0) {
            this.viewList.clear();
        }
        if (this.indicateLayout.getChildCount() > 0) {
            this.indicateLayout.removeAllViews();
        }
        final int len = resList.size();
        if (len > 0) {
            for (int index = 0; index < len; index++) {
                final View pageItem = new ImageView(getContext());
                pageItem.setBackgroundResource(resList.get(index));
                addViewItem(pageItem);
            }
        }
    }

    public void setupLayoutByClass(List<BannerList.Banner> resList) {
        if (resList == null)
            throw new NullPointerException();
        if (this.viewList.size() > 0) {
            this.viewList.clear();
        }
        if (this.indicateLayout.getChildCount() > 0) {
            this.indicateLayout.removeAllViews();
        }
        final int len = resList.size();
        if (len > 0) {
            for (int index = 0; index < len; index++) {
                BannerList.Banner bar = resList.get(index);
                View view = inflate(getContext(), R.layout.layout_new_home_item, null);
                TextView title = (TextView) view.findViewById(R.id.item_title);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img_bg);
                TextView item_type = (TextView) view.findViewById(R.id.item_type);
                TextView item_desc = (TextView) view.findViewById(R.id.item_desc);
                ImageView img_vip = (ImageView) view.findViewById(R.id.item_vip_logo);
                img_vip.setVisibility(bar.getIsvip().equals("0") ? VISIBLE : GONE);
                title.setText(bar.getVfName());
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(imageView).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(bar.getImg())).build());
                item_type.setText(bar.getTypeName());
                item_desc.setText(bar.getHit());
                addViewItem(view);
            }
        }
    }

    /**
     * 设置显示 网络图片，使用Glide
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setupLayoutByURL(final List<BannerList.Banner> urllist) {
        if (urllist == null)
            throw new NullPointerException();
        final int len = urllist.size();
        if (this.viewList.size() > 0) {
            this.viewList.clear();
        }
        if (this.indicateLayout.getChildCount() > 0) {
            this.indicateLayout.removeAllViews();
        }
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                FrameLayout frameLayout = new FrameLayout(getContext());

                final ImageView pageItem = new ImageView(getContext());
//				Glide.with(mContext).load(HttpUtils.appendUrl(urllist.get(i).getImg())).crossFade().into(pageItem);
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder().imgView(pageItem).placeHolder(R.drawable.default_horizental).url(HttpUtils.appendUrl(urllist.get(i).getImg())).build());

                pageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
                frameLayout.addView(pageItem);
                if (urllist.get(i).getIsvip().equals("1")) {
//					LinearLayout layout= new LinearLayout(getContext());
//					LinearLayout.LayoutParams params_liner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//					params_liner.gravity=Gravity.CENTER_HORIZONTAL;
//					layout.setLayoutParams(params_liner);

                    ImageView view = new ImageView(getContext());
                    view.setImageResource(R.drawable.icon_v_vip);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.TOP | Gravity.RIGHT;

//					layout.addView(view);
                    frameLayout.addView(view, params);
                }
                addViewItem(frameLayout);

            }
        }
    }

    /**
     * set show item current
     *
     * @param index postion
     */
    public void setCurrentItem(int index) {
        this.currentIndex = index;
    }

    /**
     * set anchor style, default INDICATOR_ARROW_ROUND_STYLE
     *
     * @param style INDICATOR_USERGUIDE_STYLE or INDICATOR_ARROW_ROUND_STYLE
     */
    public void setIndicateStyle(int style) {
        this.indicatorStyle = style;
    }

    /**
     * add OnItemChangeListener
     *
     * @param onItemChangeListener callback
     */
    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        if (onItemChangeListener == null) {
            throw new NullPointerException();
        }
        this.onItemChangeListener = onItemChangeListener;
    }

    public void setOnRefrshViewEnable(onChangeLister onChangeLister) {
        this.onChangeLister = onChangeLister;
    }

    /**
     * add setOnItemClickListener
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * show
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void show() {
        this.totelCount = viewList.size();
        final LayoutParams params = (LayoutParams) indicateLayout.getLayoutParams();
        params.bottomMargin = 45;
        this.indicateLayout.setLayoutParams(params);
        //init anchor
        for (int index = 0; index < this.totelCount; index++) {
            final View indicater = new ImageView(getContext());
            indicater.setLayoutParams(params);
            this.indicateLayout.addView(indicater, index);
        }
        this.refreshHandler.sendEmptyMessage(currentIndex);
        // set data for viewpager
        this.viewPager.setAdapter(new MyPagerAdapter(this.viewList));
        this.viewPager.setCurrentItem(currentIndex, false);
    }

    /**
     * refresh indicate anchor
     */
    protected void refreshIndicateView() {
        this.refreshTime = System.currentTimeMillis();

        for (int index = 0; index < totelCount; index++) {
            final ImageView imageView = (ImageView) this.indicateLayout.getChildAt(index);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 10, 0);
            imageView.setLayoutParams(lp);

            if (this.currentIndex == index) {
                imageView.setBackgroundResource(R.drawable.icon_circle_full);
            } else {
                imageView.setBackgroundResource(R.drawable.icon_circle_n_full);
            }
        }
        if (this.onItemChangeListener != null) {// notify item state changed
            try {
                this.onItemChangeListener.onPosition(this.currentIndex, this.totelCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * item changed callback
     */
    public interface OnItemChangeListener {
        void onPosition(int position, int totalCount);
    }

    /**
     * item clicked callback
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    /**
     * ScrollIndicateHandler
     */
    private static class ScrollIndicateHandler extends Handler {
        private final WeakReference<ImageIndicatorView> scrollIndicateViewRef;

        public ScrollIndicateHandler(ImageIndicatorView scrollIndicateView) {
            this.scrollIndicateViewRef = new WeakReference<ImageIndicatorView>(
                    scrollIndicateView);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageIndicatorView scrollIndicateView = scrollIndicateViewRef.get();
            if (scrollIndicateView != null) {
                scrollIndicateView.refreshIndicateView();
            }
        }
    }

    /**
     * set ItemClickListener
     */
    private class ItemClickListener implements OnClickListener {
        private int position = 0;

        public ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(view, position);
            }
        }
    }

    /**
     * deal page change
     */
    private class PageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
            currentIndex = index;
            refreshHandler.sendEmptyMessage(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (onChangeLister != null)
                onChangeLister.onChangeLister(arg0 == ViewPager.SCROLL_STATE_IDLE);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<View> pageViews = new ArrayList<View>();

        public MyPagerAdapter(List<View> pageViews) {
            this.pageViews = pageViews;
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            if (arg1 < viewList.size() && !StringUtils.isNullOrEmpty(pageViews.get(arg1))) {
                ((ViewPager) arg0).removeView(pageViews.get(arg1));
            }
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

}
