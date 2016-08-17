package com.lt.hm.wovideo.ui;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClientOption;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.base.BaseRequestActivity;
import com.lt.hm.wovideo.fragment.EventsPage;
import com.lt.hm.wovideo.fragment.LivePageFragment;
import com.lt.hm.wovideo.fragment.MineInfo;
import com.lt.hm.wovideo.fragment.NewChoicePage;
import com.lt.hm.wovideo.fragment.RecommendPage;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.interf.updateTagLister;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.FileUtil;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UpdateManager;
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
import com.lt.hm.wovideo.utils.location.CheckPermissionsActivity;
import com.lt.hm.wovideo.utils.location.Utils;
import com.lt.hm.wovideo.widget.materialshowcaseview.MaterialShowcaseView;
import com.lt.hm.wovideo.zxing.ui.MipcaActivityCapture;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class MainPage2 extends BaseActivity implements updateTagLister {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    public final static int SCANNIN_PERSON = 2;
    private final static String FIRST_LAUNCH_KEY = "first launch";
    private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;
    @BindView(R.id.person_center)
    ImageView personCenter;
    @BindView(R.id.qr_scan)
    ImageView qrScan;
    @BindView(R.id.main)
    FrameLayout idMenu;
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
    @BindView(R.id.group)
    RadioGroup group;
    private CheckPermissionsActivity permChecker = null;
    private FragmentManager fragmentManager;

    private ArrayList<String> fragmentTags;

    private void initData() {
        currIndex = 0;
        fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "LiveFragment", "VipFragment", "MineFragment"));
    }

    private void initView() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        chageIndex(0);
                        break;
                    case R.id.foot_bar_im:
                        chageIndex(1);
                        break;
                    case R.id.foot_bar_interest:
                        chageIndex(2);
                        break;
                    case R.id.main_footbar_user:
                        chageIndex(3);
                        break;
                    default:
                        break;
                }
            }
        });
        showFragment();
    }

    public void chageIndex(int index) {
        if (index == 0) {
            commonHeadLayout.setVisibility(View.GONE);
            choiceHeadLayout.setVisibility(View.VISIBLE);
        } else {
            commonHeadLayout.setVisibility(index == 3 ? View.GONE : View.VISIBLE);
            choiceHeadLayout.setVisibility(View.GONE);
        }
        if (index == 1) {
            UIHelper.ToLivePage(this);
            checkGroup(currIndex);
            return;
        }

        currIndex = index;
        showFragment();
    }

    private void checkGroup(int idex) {
        switch (idex) {
            case 0:
                group.check(R.id.foot_bar_home);
                break;
            case 2:
                group.check(R.id.foot_bar_interest);
                break;
            case 3:
                group.check(R.id.main_footbar_user);
                break;
        }
    }

    private void showFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return new NewChoicePage();
            case 1:
                return new LivePageFragment();
            case 2:
                return new RecommendPage();
            case 3:
                //     return new EventsPage();
                return new MineInfo();
            default:
                return null;
        }
    }

    private boolean dialog_showing = false;
    private AMapLocationClientOption locationOption = null;
    private long mExitTime = 0;
    private Handler popHandler = new Handler();
    private Runnable popRun = new Runnable() {
        @Override
        public void run() {
            showPop();
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();

        commonHeadLayout.setVisibility(View.GONE);
        choiceHeadLayout.setVisibility(View.VISIBLE);

        UpdateRecommedMsg.getInstance().addRegisterSucListeners(this);

        checkLoginState();
        readPositionFromAsset();
        initData();
        initView();
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(this)
                .setTarget(choicePersonCenter)
                .setDismissOnTouch(true)
                .setContentText(getString(R.string.register_hint))
                .setMaskColour(getResources().getColor(R.color.mask_color))
                .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("register"); // provide a unique ID used to ensure it is only shown once
        builder.setContentTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.hide();
                Intent intent = new Intent();
                intent.setClass(MainPage2.this, LoginPage.class);
                startActivity(intent);
            }
        });
        builder.show();

        Utils.StartLocation(MainPage2.this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_main2;
    }

    @Override
    protected void onDestroy() {
        UpdateRecommedMsg.getInstance().removeRegisterSucListeners(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstRegister = SharedPrefsUtils.getBooleanPreference(this, "regist", false);
        if (isFirstRegister) {
            SharedPrefsUtils.setBooleanPreference(this, "regist", false);
            UIHelper.ToTagPage(this);
        }
    }

    private boolean isFirstRegister = false;

    private void checkLoginState() {
//        String info = ACache.get(getApplicationContext()).getAsString("userinfo");
        String info = SharedPrefsUtils.getStringPreference(getApplicationContext(), "userinfo");
        boolean isFirstLaunch = SharedPrefsUtils.getBooleanPreference(this, FIRST_LAUNCH_KEY, true);

        if (StringUtils.isNullOrEmpty(info)) {
            if (!isFirstLaunch) {
                popHandler.postDelayed(popRun, 3000);
            }
            SharedPrefsUtils.setBooleanPreference(this, FIRST_LAUNCH_KEY, false);
        } else {
            UserModel model = new Gson().fromJson(info, UserModel.class);
//            String tag = ACache.get(this).getAsString(model.getId() + "free_tag");
//            if (StringUtils.isNullOrEmpty(tag)) {
//                UnLoginHandler.freeDialog(MainPage2.this, model.getId());
//            }
            if (model.getIsVip() == null) {
                UnLoginHandler.freeDialog(MainPage2.this, model);
            } else if (model.getIsVip().equals("0")) {
                UnLoginHandler.freeDialog(MainPage2.this, model);
            }
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
        //检测更新
//		CheckUpdate();
    }

    private void CheckUpdate() {
        UpdateManager.checkUpdate(MainPage2.this, new UpdateManager.DialogClick() {
            @Override
            public void positive() {
                Toast.makeText(getApplicationContext(), "已后台下载", Toast.LENGTH_SHORT).show();
                dialog_showing = false;
            }

            @Override
            public void negitive() {
                dialog_showing = false;
            }

            @Override
            public void onFinish(boolean flag, String tag) {
                if (!tag.equals("1")) {
                    if (flag) {
                        System.exit(0);
                    }
                }
            }

            @Override
            public void isShow() {
                dialog_showing = true;
            }

            @Override
            public void isDismiss() {
                dialog_showing = false;
            }
        });
    }

    @Override
    public void initDatas() {

    }

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
            case SCANNIN_PERSON:
                if (resultCode == PersonalitySet.RESULT_PERSONALITY) {
                  //  restartHome();
                }
                break;
        }
    }

    private void restartHome(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = instantFragment(currIndex);
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
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

    public void showPop() {
// Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.layout_popup_promotion, idMenu, false);


        newView.setAnimation(inFromTop());
        // Set the text in the new row to a random country.
        TextView login = (TextView) newView.findViewById(R.id.msg_text);
        login.setText(getResources().getString(R.string.main_page_toast));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPage2.this, LoginPage.class);
                startActivity(intent);
            }
        });
        ((ImageView) newView.findViewById(R.id.img))
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.img_hd1));

        // Set a click listener for the "X" button in the row that will remove the row.
        newView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                newView.setAnimation(outToTop());
                idMenu.removeView(newView);
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        idMenu.addView(newView);

        Runnable remove = new Runnable() {
            @Override
            public void run() {
                newView.setAnimation(outToTop());
                idMenu.removeView(newView);
            }
        };
        popHandler.postDelayed(remove, 5000);

    }

    private Animation inFromTop() {
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromTop.setDuration(1000);
        inFromTop.setInterpolator(new AccelerateInterpolator());
        return inFromTop;
    }

    private Animation outToTop() {
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        inFromTop.setDuration(1000);
        inFromTop.setInterpolator(new AccelerateInterpolator());
        return inFromTop;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        checkLoginState();
    }

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateNavigateUpTaskStack(builder);
    }

    private void readPositionFromAsset() {
        new Thread(() -> {
            try {
                FileUtil.readAsset(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onUpdateTagLister() {
        restartHome();
    }
}
