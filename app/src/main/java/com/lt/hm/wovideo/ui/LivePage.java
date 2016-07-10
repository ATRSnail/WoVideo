package com.lt.hm.wovideo.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.video.LiveTVListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LiveTVList;
import com.lt.hm.wovideo.model.VideoURL;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.video.LiveMediaController;
import com.lt.hm.wovideo.video.WoVideoPlayer;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.vov.vitamio.LibsChecker;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class LivePage extends BaseActivity {
    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_play_number)
    TextView videoPlayNumber;
    @BindView(R.id.video_collect)
    PercentLinearLayout videoCollect;
    @BindView(R.id.video_share)
    PercentLinearLayout videoShare;
    @BindView(R.id.video_projection)
    PercentLinearLayout videoProjection;
    @BindView(R.id.live_bref_img)
    ImageView liveBrefImg;
    @BindView(R.id.live_bref_txt1)
    TextView liveBrefTxt1;
    @BindView(R.id.live_bref_txt2)
    TextView liveBrefTxt2;
    @BindView(R.id.live_expand)
    ImageView liveExpand;
    @BindView(R.id.video_player)
    WoVideoPlayer woPlayer;
    boolean ex_flag = false;
    @BindView(R.id.live_program_list)
    RecyclerView liveProgramList;
    @BindView(R.id.live_video_bottom)
    PercentRelativeLayout live_video_bottom;
    @BindView(R.id.live_video_bref)
    LinearLayout live_video_bref;
    int newIndex = 0;
    int oldIndex = -1;


    @BindView(R.id.live_btn_sina)
    Button liveBtnSina;
    @BindView(R.id.live_btn_local)
    Button liveBtnLocal;
    @BindView(R.id.live_btn_cctv)
    Button liveBtnCctv;
    @BindView(R.id.live_btn_othertv)
    Button liveBtnOthertv;
    Button[] btns;

    List<LiveTVList.LiveTvListBean> mList;
    List<LiveModles.LiveModel> sinaList;
    List<LiveModles.LiveModel> localList;
    List<LiveModles.LiveModel> cctvList;
    List<LiveModles.LiveModel> otherList;
    LiveTVListAdapter adapter;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();
    private WoVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new WoVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            woPlayer.close();
            LivePage.this.finish();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                woPlayer.setPageType(LiveMediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                woPlayer.setPageType(LiveMediaController.PageType.EXPAND);
                woPlayer.alwaysShowController();
            }
        }

        @Override
        public void onPlayFinish() {
            // TODO: 16/6/3 添加 显示 信息／电视剧 可能需要 自动播放下一集
        }

        @Override
        public void onLanToPor() {
            resetPageToPortrait();
        }
    };

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            woPlayer.setPageType(LiveMediaController.PageType.SHRINK);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_video_live;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        sinaList = new ArrayList<>();
        localList = new ArrayList<>();
        cctvList = new ArrayList<>();
        otherList = new ArrayList<>();
        btns = new Button[]{liveBtnSina, liveBtnLocal, liveBtnCctv, liveBtnOthertv};
        liveProgramList.setLayoutManager(new LinearLayoutManager(this));
        liveProgramList.addItemDecoration(new RecycleViewDivider(LivePage.this,LinearLayoutManager.VERTICAL));
//        liveProgramList.addItemDecoration(new SpaceItemDecoration(10));
//        liveProgramList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        hideSomething();
        woPlayer.setVideoPlayCallback(mVideoPlayCallback);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
    }

    private void hideSomething() {
        live_video_bref.setVisibility(View.GONE);
        videoCollect.setVisibility(View.GONE);
        videoShare.setVisibility(View.GONE);
        videoProjection.setVisibility(View.GONE);
        videoPlayNumber.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        liveExpand.setOnClickListener((View v) -> {
            if (ex_flag) {
                liveExpand.setImageResource(R.drawable.icon_expand);
                liveBrefTxt2.setVisibility(View.GONE);
                ex_flag = false;
            } else {
                liveExpand.setImageResource(R.drawable.icon_zoom);
                liveBrefTxt2.setVisibility(View.VISIBLE);
                ex_flag = true;
            }
        });

        liveBtnSina.setOnClickListener((View v) -> {
            changeState(btns[0]);
            if (sinaList.size()>0){
                initListViews(sinaList);
            }
        });
        liveBtnLocal.setOnClickListener((View v) -> {
            changeState(btns[1]);
            if (localList.size()>0){
                initListViews(localList);
            }
        });
        liveBtnCctv.setOnClickListener((View v) -> {
            changeState(btns[2]);
            if (cctvList.size()>0){
                initListViews(cctvList);
            }
        });
        liveBtnOthertv.setOnClickListener((View v) -> {
            changeState(btns[3]);
            if (otherList.size()>0){
                initListViews(otherList);
            }
        });
    }

    private void changeState(Button btn) {
        for (int i = 0; i < btns.length; i++) {
            Button tmp= btns[i];
            if (btn!=tmp){
                tmp.setBackgroundColor(getResources().getColor(R.color.white));
                tmp.setTextColor(getResources().getColor(R.color.black));
            }else{
                tmp.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
                tmp.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void initDatas() {
        getLiveList();
    }

    private void getLiveList() {
        HashMap<String, Object> map = new HashMap<>();
        HttpApis.getLiveTvList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
//                ResponseObj<LiveTVList, RespHeader> resp = new ResponseObj<LiveTVList, RespHeader>();
                ResponseObj<LiveModles, RespHeader> resp = new ResponseObj<LiveModles, RespHeader>();
                ResponseParser.parse(resp, response, LiveModles.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    sinaList.addAll(resp.getBody().getSinatv());
                    localList.addAll(resp.getBody().getLocalTV());
                    cctvList.addAll(resp.getBody().getCctv());
                    otherList.addAll(resp.getBody().getOtherTv());

//                    mList.addAll(resp.getBody().getLiveTvList());
                    initListViews(sinaList);
                }
            }
        });
    }

    private void initListViews(List<LiveModles.LiveModel> liveTvList) {
        adapter = new LiveTVListAdapter(getApplicationContext(), liveTvList);
        liveProgramList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getRealURL(liveTvList.get(0).getUrl());
        videoName.setText(liveTvList.get(0).getTvName());

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                String url = liveTvList.get(i).getUrl();
                videoName.setText(liveTvList.get(i).getTvName());
                getRealURL(url);
            }
        });
//        adapter.setListener(new LiveTVListAdapter.ItemOnClick() {
//            @Override
//            public void onClick(BaseViewHolder  holder, int position) {
////                newIndex = position;
////                if (oldIndex==-1){
////                    oldIndex = newIndex;
////                    holder.setText(R.id.live_list_item_current_text,"正在播放");
////                }else{
////                    if (oldIndex!=newIndex){
////                       TextView view= (TextView) holder.convertView.findViewWithTag("测试"+oldIndex);
////                        view.setText(liveTvList.get(oldIndex).getNowPro());
////                    }else{
////                        holder.setText(R.id.live_list_item_current_text,"正在播放");
////                    }
////                }
//                holder.setText(R.id.live_list_item_current_text,"正在播放");
//
//            }
//        });
    }

    private void getRealURL(String url) {
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("videoSourceURL", url);
        maps.put("cellphone", "18513179404");
        maps.put("freetag", "1");
        HttpApis.getVideoRealURL(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoURL, RespHeader> resp = new ResponseObj<VideoURL, RespHeader>();
                ResponseParser.parse(resp, response, VideoURL.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    videoUrl.setFormatUrl(resp.getBody().getUrl());
                    video.setmPlayUrl(videoUrl);
                    woPlayer.loadAndPlay(videoUrl, 0);
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == woPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            woPlayer.getLayoutParams().height = (int) width;
            woPlayer.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            woPlayer.getLayoutParams().height = (int) height;
            woPlayer.getLayoutParams().width = (int) width;
        }
    }
}
