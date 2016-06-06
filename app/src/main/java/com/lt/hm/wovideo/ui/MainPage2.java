package com.lt.hm.wovideo.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.interf.OnTabReselectListener;
import com.lt.hm.wovideo.widget.MyFragmentTabHost;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class MainPage2 extends BaseActivity implements View.OnTouchListener, TabHost.OnTabChangeListener {
    @BindView(R.id.person_center)
    ImageView personCenter;
    @BindView(R.id.qr_scan)
    ImageView qrScan;
    @BindView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @BindView(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @BindView(android.R.id.tabhost)
    MyFragmentTabHost tabhost;
    @BindView(R.id.id_menu)
    LinearLayout idMenu;
//    @BindView(R.id.id_menu)
//    SlidingMenu idMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_main2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (Build.VERSION.SDK_INT > 10) {
            tabhost.getTabWidget().setShowDividers(0);
        }
        tabhost.setCurrentTab(0);
        tabhost.setOnTabChangedListener(this);
        initTabs();
    }

    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = tabhost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainPage2.this);
                }
            });
            tabhost.addTab(tab, mainTab.getClz(), null);
            tabhost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(tabhost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                tabhost.getCurrentTabTag());
    }

    @Override
    public void onTabChanged(String tabId) {
        final int size = tabhost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = tabhost.getTabWidget().getChildAt(i);
            if (i == tabhost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
        supportInvalidateOptionsMenu();
    }

//    @Override
//    public void OpenCenter() {
//        idMenu.openMenu();
//    }
//
//    @Override
//    public void CloseCenter() {
//        idMenu.closeMenu();
//    }
}
