package com.lt.hm.wovideo.ui;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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
public class MainPage2 extends BaseActivity implements View.OnTouchListener, TabHost.OnTabChangeListener{
    private final static int SCANNIN_GREQUEST_CODE = 1;
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
    @BindView(R.id.common_head_layout)
    RelativeLayout commonHeadLayout;
    @BindView(R.id.choice_head_layout)
    PercentRelativeLayout choiceHeadLayout;
    @BindView(R.id.choice_search_layout)
    LinearLayout choiceSearchLayout;
    @BindView(R.id.choice_qr_scan)
    ImageView choiceQrScan;
    @BindView(R.id.choice_person_center)
    ImageView choicePersonCenter;

    private long mExitTime = 0;

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
        commonHeadLayout.setVisibility(View.GONE);
        choiceHeadLayout.setVisibility(View.VISIBLE);

        tabhost.setOnTabChangedListener(this);
        initTabs();

        checkLoginState();
    }

    private void checkLoginState() {
        String info = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(info)) {
            UnLoginHandler.unRegist(MainPage2.this);
        } else {
            UserModel model = new Gson().fromJson(info, UserModel.class);
            String tag = ACache.get(this).getAsString(model.getId() + "free_tag");
            if (StringUtils.isNullOrEmpty(tag)) {
                UnLoginHandler.freeDialog(MainPage2.this, model.getId());
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
        personCenter.setOnClickListener((View v) -> {
            UIHelper.ToPerson(this);
        });
        choicePersonCenter.setOnClickListener((View v) -> {
            UIHelper.ToPerson(this);
        });

        qrScan.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setClass(MainPage2.this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });
        choiceQrScan.setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setClass(MainPage2.this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        });
        choiceSearchLayout.setOnClickListener((View v) -> {
            UIHelper.ToSearchPage(this);
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

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateNavigateUpTaskStack(builder);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                tabhost.getCurrentTabTag());
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals("推荐")) {
            commonHeadLayout.setVisibility(View.GONE);
            choiceHeadLayout.setVisibility(View.VISIBLE);
        } else {
            commonHeadLayout.setVisibility(View.VISIBLE);
            choiceHeadLayout.setVisibility(View.GONE);
        }
        View v = tabhost.getTabWidget().getChildTabViewAt(tabhost.getCurrentTab());
        v.setSelected(true);
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
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Toast.makeText(getApplicationContext(), bundle.getString("result"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    TLog.log("url" + bundle.getString("result"));
                    Uri content_url = Uri.parse(bundle.getString("result"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(content_url);
                    startActivity(intent);

                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, getResources().getString(R.string.second_back_hint), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
//				aCache.put("first_pay", "");
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
