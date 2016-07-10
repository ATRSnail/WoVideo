package com.lt.hm.wovideo.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.video.VideoAnthologyAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemGridAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.LikeList;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.VideoURL;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.ShareUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
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
 * @create_date 16/6/7
 */
public class DemandPage extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.anthology_text)
    TextView anthologyText;
    @BindView(R.id.video_update_info)
    LinearLayout videoUpdateInfo;
    @BindView(R.id.anthology_list)
    RecyclerView anthologyList;
    @BindView(R.id.video_bottom_grid)
    RecyclerView videoBottomGrid;
    @BindView(R.id.video_bottom_list)
    RecyclerView videoBottomList;
    @BindView(R.id.video_player)
    WoVideoPlayer woPlayer;
    @BindView(R.id.anthology_all)
    Button anthologyALL;
    @BindView(R.id.img_collect)
    ImageView img_collect;
    VideoItemListAdapter list_adapter;
    VideoItemGridAdapter grid_adapter;
    List<PlayList.PlaysListBean> antholys;
    VideoAnthologyAdapter anthologyAdapter;
    boolean expand_flag = false;
    String vfId;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();
    private int pageSize = 10;
    private WoVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new WoVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            woPlayer.close();
            DemandPage.this.finish();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                woPlayer.setPageType(LiveMediaController.PageType.SHRINK);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                woPlayer.getSuperVideoView().setLayoutParams(params);
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

    @Override
    protected int getLayoutId() {
        return R.layout.layout_demand;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        antholys = new ArrayList<>();
        woPlayer.setVideoPlayCallback(mVideoPlayCallback);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("id")) {
            vfId = bundle.getString("id");
            getVideoDetails(vfId);
            getAnthologyDatas(vfId);
            getFirstURL(vfId);
        }
        getYouLikeDatas();

    }

    /**
     * 猜你喜欢接口调用
     */
    private void getYouLikeDatas() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", pageSize);
        HttpApis.getYouLikeList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<LikeList, RespHeader> resp = new ResponseObj<LikeList, RespHeader>();
                ResponseParser.parse(resp, response, LikeList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    // TODO: 16/6/14 填充 底部数据


                    List<LikeList.LikeListBean> grid_list = new ArrayList<LikeList.LikeListBean>();
//                    List<LikeList.LikeListBean> list_list = new ArrayList<LikeList.LikeListBean>();
//                    for (int i = 0; i < 3; i++) {
//                        grid_list.add(resp.getBody().getLikeList().get(i));
//                    }
//                    for (int i = 0; i < 3; i++) {
//                        resp.getBody().getLikeList().remove(0);
//                    }
                    grid_list.addAll(resp.getBody().getLikeList());
                    initBottomGrid(grid_list);
//                    initBottomList(list_list);
                }
            }
        });
    }

    private void initAnthologys(int length) {
        List<String> anthologys = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            anthologys.add(i + 1 + "");
        }
        anthologyAdapter = new VideoAnthologyAdapter(getApplicationContext(), anthologys);
        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        anthologyList.addItemDecoration(new RecycleViewDivider(getApplicationContext(), LinearLayoutManager.HORIZONTAL));
        anthologyList.setLayoutManager(manager);
        anthologyList.setAdapter(anthologyAdapter);
        anthologyAdapter.notifyDataSetChanged();
        anthologyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                PlayList.PlaysListBean bean = antholys.get(i);
                vfId = bean.getVfId();
                getRealURL(bean.getStandardUrl());
            }
        });
    }

    private void getAnthologyDatas(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        String login_info = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(login_info)) {

        } else {
            UserModel model = new Gson().fromJson(login_info, UserModel.class);
            maps.put("userid", model.getId());
        }
        HttpApis.getVideoListInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<PlayList, RespHeader> resp = new ResponseObj<PlayList, RespHeader>();
                ResponseParser.parse(resp, response, PlayList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    antholys.addAll(resp.getBody().getPlaysList());
                    initAnthologys(resp.getBody().getPlaysList().size());
                }
            }
        });
    }

    private void initBottomGrid(List<LikeList.LikeListBean> grid_list) {
        grid_adapter = new VideoItemGridAdapter(DemandPage.this, grid_list);
        videoBottomGrid.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        videoBottomGrid.setLayoutManager(manager);
        videoBottomGrid.addItemDecoration(new RecycleViewDivider(DemandPage.this, GridLayoutManager.HORIZONTAL));
        videoBottomGrid.setAdapter(grid_adapter);
        grid_adapter.notifyDataSetChanged();
        grid_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                getChangePage(grid_list.get(i).getId());
            }
        });
    }

    private void initBottomList(List<LikeList.LikeListBean> list_list) {
        //init bottom  list
        list_adapter = new VideoItemListAdapter(getApplicationContext(), list_list);
        LinearLayoutManager line_manager = new LinearLayoutManager(this);
        line_manager.setOrientation(LinearLayoutManager.VERTICAL);
        videoBottomList.setHasFixedSize(false);
        videoBottomList.setLayoutManager(line_manager);
        videoBottomList.setAdapter(list_adapter);
        list_adapter.notifyDataSetChanged();
        list_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                getChangePage(list_list.get(i).getId());
            }
        });
    }

    /**
     * 获取视频详情数据
     *
     * @param id
     */
    public void getVideoDetails(String id) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", id);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    VideoDetails.VfinfoBean details = resp.getBody().getVfinfo();
                    videoName.setText(details.getName());
                    videoPlayNumber.setText(details.getHit());
                    if (details.getGxzt().equals("0")) {
                        anthologyText.setText("更新至" + details.getJjs() + "集");
                    } else {
                        anthologyText.setText("完结 共" + details.getJjs() + "集");
                    }
                }
            }
        });
    }

    /**
     * 获取 视频播放地址，并播放
     *
     * @param vfId
     */
    public void getFirstURL(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        String login_info = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(login_info)) {

        } else {
            UserModel model = new Gson().fromJson(login_info, UserModel.class);
            maps.put("userid", model.getId());
        }
        HttpApis.getVideoListInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<PlayList, RespHeader> resp = new ResponseObj<PlayList, RespHeader>();
                ResponseParser.parse(resp, response, PlayList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    PlayList.PlaysListBean details = resp.getBody().getPlaysList().get(0);
                    getRealURL(details.getStandardUrl());
                }
            }
        });
    }

    /**
     * 获取视频播放地址
     *
     * @param url
     */
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
    public void initViews() {
        videoCollect.setOnClickListener(this);
        videoProjection.setVisibility(View.GONE);
        videoShare.setVisibility(View.VISIBLE);
        videoShare.setOnClickListener(this);
        videoCollect.setVisibility(View.VISIBLE);
        videoCollect.setOnClickListener((View v) -> {
            CollectVideo();
        });
        anthologyALL.setOnClickListener((View v) -> {
            if (expand_flag) {
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(GridLayoutManager.HORIZONTAL);
                anthologyList.setLayoutManager(manager);
                expand_flag = false;
            } else {
                GridLayoutManager manager = new GridLayoutManager(this, 8);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                anthologyList.setLayoutManager(manager);
                expand_flag = true;
            }
        });
    }

    /**
     * 收藏 视频接口调用
     */
    private void CollectVideo() {
        HashMap<String, Object> map = new HashMap<>();
        String string = ACache.get(this).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(string)) {
            UserModel model = new Gson().fromJson(string, UserModel.class);
            map.put("userid", model.getId());
            map.put("vfid", vfId);
            HttpApis.collectVideo(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log("collect->" + response);
                    ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                    ResponseParser.parse(resp, response, String.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect_press));
                    }
                }
            });
        } else {
            // TODO: 16/7/8 未登录跳转登录页面
            UnLoginHandler.unLogin(DemandPage.this);
        }
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_share:
                ShareUtils.showShare(this,null,false,videoUrl.getFormatUrl());
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
//            float height = DensityUtil.getWidthInPx(this);
//            float width = DensityUtil.getHeightInPx(this);
            woPlayer.getLayoutParams().height = ScreenUtils.getScreenHeight(this);
            woPlayer.getLayoutParams().width = ScreenUtils.getScreenWidth(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200);
            woPlayer.getLayoutParams().height = (int) height;
            woPlayer.getLayoutParams().width = (int) width;
        }
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            woPlayer.setPageType(LiveMediaController.PageType.SHRINK);
        }
    }

    public void getChangePage(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {

                    if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToMoviePage(DemandPage.this, bundle);
                        DemandPage.this.finish();
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(DemandPage.this, bundle);
                        DemandPage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(DemandPage.this, bundle);
                        DemandPage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(DemandPage.this, bundle);
                        DemandPage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.LIVE.getId()) {
                        UIHelper.ToLivePage(DemandPage.this);
                        DemandPage.this.finish();
                    }
                }
            }
        });
    }

}
