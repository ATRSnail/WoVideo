package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.TextViewCount;
import com.lt.hm.wovideo.utils.UIHelper;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/6
 */
public class StartAdPage extends BaseActivity {

    @BindView(R.id.ad_img)
    ImageView adImg;
//    @BindView(R.id.round_progress)
//    RoundProgressBar roundProgress;
    @BindView(R.id.count_time_text)
    TextView countTimeText;
    private boolean isSkip = false;
    private int displaySecond = 3;
//    private int mTotalProgress;
//    private int mCurrentProgress;

//    class ProgressRunable implements Runnable {
//        @Override
//        public void run() {
//            while (mCurrentProgress <= mTotalProgress + 1) {
//                try {
//                    mCurrentProgress += 1;
////                    roundProgress.setProgress(mCurrentProgress);
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    ;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_start_ad;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        Glide.with(StartAdPage.this)
//                .load("http://img.club.pchome.net/kdsarticle/2013/11small/21/fd548da909d64a988da20fa0ec124ef3_1000x750.jpg")
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().crossFade()
//                .into(adImg);
//        mTotalProgress = displaySecond;
//        mCurrentProgress = 0;
        countTimeText.setClickable(true);
//        new Thread(new ProgressRunable()).start();
        TextViewCount count = new TextViewCount(4000,1000,StartAdPage.this,countTimeText);
        count.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSkip) {
                    changePage();
                }
            }
        }, displaySecond * 1000);

//       Glide.with(this).load("http://img.club.pchome.net/kdsarticle/2013/11small/21/fd548da909d64a988da20fa0ec124ef3_1000x750.jpg").asBitmap();

    }

    private void changePage() {
        UIHelper.ToMain2(StartAdPage.this);
        StartAdPage.this.finish();
    }

    @Override
    public void initViews() {
//        roundProgress.setOnClickListener((View v) -> {
//            isSkip = true;
//            changePage();
//        });
        countTimeText.setOnClickListener((View v)->{
            isSkip = true;
            changePage();
        });
    }


    @Override
    public void initDatas() {

    }

}
