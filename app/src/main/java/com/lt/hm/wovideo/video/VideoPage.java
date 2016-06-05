package com.lt.hm.wovideo.video;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/2
 */
public class VideoPage extends BaseActivity  implements View.OnClickListener{

    @BindView(R.id.play_btn)
    View mPlayBtnView;
    @BindView(R.id.video_player_item_1)
    SuperVideoPlayer mSuperVideoPlayer;
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            mSuperVideoPlayer.close();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };



    @Override
    public void onClick(View view) {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        mSuperVideoPlayer.setAutoHideController(false);

        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatName("720P");
        videoUrl1.setFormatUrl("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8");
        VideoUrl videoUrl2 = new VideoUrl();
        videoUrl2.setFormatName("480P");
//        videoUrl2.setFormatUrl("http://111.206.133.53:9910/if5ax/live/live_xpt/index.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&tradeid=ce28f8dcb3aae21394064e8a68b6c505&lsttm=20160602210740&enkey=2ce3c35d412af6e20be7385b186fb47f");
        videoUrl2.setFormatUrl("http://dir.wo186.tv:809/if5ax/live/live_nsn/index.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&spkey=91469ac32df55ab4ca2750329ebbc3b1");
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        arrayList1.add(videoUrl2);
        video.setVideoName("测试视频一");
        video.setVideoUrl(arrayList1);

        Video video2 = new Video();
        VideoUrl videoUrl3 = new VideoUrl();
        videoUrl3.setFormatName("720P");
        videoUrl3.setFormatUrl("http://7xkbzx.com1.z0.glb.clouddn.com/SampleVideo_1080x720_10mb.mp4");
        VideoUrl videoUrl4 = new VideoUrl();
        videoUrl4.setFormatName("480P");
        videoUrl4.setFormatUrl("http://7xkbzx.com1.z0.glb.clouddn.com/SampleVideo_720x480_10mb.mp4");
        ArrayList<VideoUrl> arrayList2 = new ArrayList<>();
        arrayList2.add(videoUrl3);
        arrayList2.add(videoUrl4);
        video2.setVideoName("测试视频二");
        video2.setVideoUrl(arrayList2);

        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);
        videoArrayList.add(video2);

        mSuperVideoPlayer.loadMultipleVideo(videoArrayList,0,0,0);
    }


    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.layout_video_page;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        startDLNAService();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

    private void startDLNAService() {
        // Clear the device container.
        DLNAContainer.getInstance().clear();
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        startService(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        stopService(intent);
    }

}
