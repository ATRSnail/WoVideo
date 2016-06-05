package com.lt.hm.wovideo.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.WindowManager;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.video.LiveMediaController;
import com.lt.hm.wovideo.video.WoVideoPlayer;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;

import butterknife.BindView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.VideoView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/31
 */
public class VideoPage extends BaseActivity{

    @BindView(R.id.video_player)
    WoVideoPlayer videoView;
//    MediaController controller;
    GestureDetector mGesture = null;
    private AudioManager mAudioManager;
    private int mLayout = VideoView.VIDEO_LAYOUT_SCALE;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_video;
    }

    private WoVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new WoVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoView.close();
//            mPlayBtnView.setVisibility(View.VISIBLE);
            VideoPage.this.finish();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                videoView.setPageType(LiveMediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                videoView.setPageType(LiveMediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {
            // TODO: 16/6/3 添加 显示 信息／电视剧 可能需要 自动播放下一集
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!LibsChecker.checkVitamioLibs(this))
            return;
//        videoView.setVideoURI(Uri.parse("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"));
//        videoView.setVideoURI(Uri.parse("http://111.206.133.53:9910/if5ax/live/live_xpt/index.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&tradeid=ce28f8dcb3aae21394064e8a68b6c505&lsttm=20160602210740&enkey=2ce3c35d412af6e20be7385b186fb47f"));
        VideoModel video = new VideoModel();
        VideoUrl videoUrl= new VideoUrl();
//        videoUrl.setFormatUrl("http://111.206.133.53:9910/if5ax/live/live_nsn/index.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&tradeid=ba0f7e78399aafaa0e00a6e61e67ed9c&lsttm=20160603154606&enkey=8e1bcc174918850a01e353eef012f642");
        videoUrl.setFormatUrl("http://111.206.133.53:9910/if5ax/video/wovideo/sp1/sp1.m3u8?vkey=4E36E4353070E6ECC78CADC43354B1DEFE83B110106A2&vid=Z-2oTN5kQ9ad_cJINnfZmA&apptype=web&pid=8031006300&portalid=319&preview=1&spid=21170&spip=111.206.133.39&spport=9910&tag=12&userid=18510509670&userip=61.148.116.190&videoname=abc&tradeid=aa13c96eb5f4a34c13a7252babf0f031&lsttm=20160603192655&enkey=e74bf8741443e61372294e600edd3773");
//        videoUrl.setFormatUrl("http://42.81.5.133/v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4?wshc_tag=0&wsts_tag=57513d2a&wsid_tag=6a02e926&wsiphost=ipdbm");
        video.setmPlayUrl(videoUrl);
        videoView.setVideoPlayCallback(mVideoPlayCallback);
        videoView.loadAndPlay(videoUrl,0);
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
            videoView.setPageType(LiveMediaController.PageType.SHRINK);
        }
    }
    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == videoView) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            videoView.getLayoutParams().height = (int) width;
            videoView.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            videoView.getLayoutParams().height = (int) height;
            videoView.getLayoutParams().width = (int) width;
        }
    }

}
