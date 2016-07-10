package com.lt.hm.wovideo.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.fragment.ChoicePage;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.interf.OnTabReselectListener;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.MyFragmentTabHost;
import com.lt.hm.wovideo.zxing.ui.MipcaActivityCapture;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class MainPage2 extends BaseActivity implements View.OnTouchListener, TabHost.OnTabChangeListener,ChoicePage.hideTopBar {
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
    @BindView(R.id.topbar_main)
    RelativeLayout topbar_main;
    private final static int SCANNIN_GREQUEST_CODE = 1;


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
        MainTab[] tabs = MainTab.values();

        checkLoginState();
    }

    private void checkLoginState() {
        String info  = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(info)){
            UnLoginHandler.unRegist(MainPage2.this);
        }else{
            UserModel model= new Gson().fromJson(info,UserModel.class);
            String tag= ACache.get(this).getAsString(model.getId()+"free_tag");
            if (StringUtils.isNullOrEmpty(tag)){
                // TODO: 16/7/9 弹出 免流注册提示框
                UnLoginHandler.freeDialog(MainPage2.this,model.getId());
            }
        }

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
        personCenter.setOnClickListener((View v)->{
            UIHelper.ToPerson(this);
        });

        qrScan.setOnClickListener((View v)->{
            Intent intent = new Intent();
            intent.setClass(MainPage2.this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Toast.makeText(getApplicationContext(),bundle.getString("result"),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    TLog.log("url"+bundle.getString("result"));
                    Uri content_url = Uri.parse(bundle.getString("result"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(content_url);
                    startActivity(intent);
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }


    @Override
    public void hideTop() {
        topbar_main.setVisibility(View.GONE);
    }
}
