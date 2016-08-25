package com.lt.hm.wovideo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.db.HistoryDataBase;
import com.lt.hm.wovideo.db.NetUsageDatabase;
import com.lt.hm.wovideo.http.HttpUtilBack;
import com.lt.hm.wovideo.model.VideoHistory;
import com.lt.hm.wovideo.utils.TLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leonardo on 16/3/21.
 */
public abstract class BaseActivity extends BaseRequestActivity implements BaseViewInterface {
    protected LayoutInflater mInflater;
    private boolean _isVisible;
    private TextView mTvActionTitle;
    private boolean showActionBar = false;
    Unbinder unbinder;
    protected HistoryDataBase history;
    protected VideoHistory videoHistory;
    protected NetUsageDatabase netUsageDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasActionBar()) {
            // support RequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        onBeforeSetContentLayout();

        if (getLayoutId() != 0){
            TLog.error("getLayoutId()---"+getLayoutId());
            setContentView(getLayoutId());
        }


        unbinder = ButterKnife.bind(this);
        mInflater = getLayoutInflater();
        history = new HistoryDataBase(getApplicationContext());
        videoHistory = new VideoHistory();
        netUsageDatabase = new NetUsageDatabase(this);
        init(savedInstanceState);
//		//透明状态栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		//透明导航栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initViews();
        initDatas();
        _isVisible = true;
    }

    public void setShowActionBar(boolean show) {
        this.showActionBar = show;
    }

    public void setSupportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(resId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    protected abstract void init(Bundle savedInstanceState);


    /**
     * 初始化 ActionBar
     *
     * @param mActionBar
     */
    protected void initActionBar(ActionBar mActionBar) {
        if (mActionBar == null) return;
        if (hasBackButton()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        } else {
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            mActionBar.setDisplayUseLogoEnabled(true);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                mActionBar.setTitle(titleRes);
            }
        }
    }

    private int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(String title) {
        if (title.isEmpty()) {
            title = getString(R.string.app_name);
        }
//		if (hasActionBar() && mActionBar != null) {
//			mActionBar.setTitle(title);
//		}
    }

    protected void onBeforeSetContentLayout() {
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected boolean hasActionBar() {
        return true;
    }

    protected int getLayoutId() {
        return 0;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
