package com.lt.hm.wovideo.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.video.BrefIntroAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemGridAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
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
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.video.LiveMediaController;
import com.lt.hm.wovideo.video.WoVideoPlayer;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.widget.CustomListView;
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
public class MoviePage extends BaseActivity {
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
    @BindView(R.id.video_bref_intros)
    CustomListView videoBrefIntros;

    BrefIntroAdapter biAdapter;
    String[] names = new String[]{"导演", "主演"};
    String[] values = new String[]{"王京", "周润发，刘德华"};
    @BindView(R.id.movie_bref_img)
    ImageView movieBrefImg;
    @BindView(R.id.movie_bref_purch)
    ImageView movieBrefPurch;
    @BindView(R.id.video_bottom_grid)
    RecyclerView videoBottomGrid;
    @BindView(R.id.bref_txt1)
    TextView brefTxt1;
    @BindView(R.id.bref_txt2)
    TextView brefTxt2;
    @BindView(R.id.bref_expand)
    ImageView brefExpand;
    @BindView(R.id.video_player)
    WoVideoPlayer woPlayer;

    VideoModel video = new VideoModel();
    VideoUrl videoUrl= new VideoUrl();
    VideoItemGridAdapter grid_adapter;
    boolean flag = false;
    private WoVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new WoVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            woPlayer.close();
            MoviePage.this.finish();
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


    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        hideSomething();
        woPlayer.setVideoPlayCallback(mVideoPlayCallback);
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("id")) {
            String id = bundle.getString("id");
            getVideoDetails(id);
            getFirstURL(id);
        }
        getYouLikeDatas(10);

    }

    /**
     * 在正式项目中需要 解禁
     */
    private void hideSomething() {
        videoCollect.setVisibility(View.GONE);
        videoShare.setVisibility(View.GONE);
        videoProjection.setVisibility(View.GONE);
        brefTxt1.setVisibility(View.GONE);
        brefTxt2.setVisibility(View.GONE);
        brefExpand.setVisibility(View.GONE);
        movieBrefPurch.setVisibility(View.GONE);
    }

    /**
     * 获取视频详情数据
     * @param id
     */
    public void getVideoDetails(String id) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", id);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    VideoDetails.VfinfoBean details= resp.getBody().getVfinfo();
                    values= new String[]{details.getDirector(),details.getStars()};
                    biAdapter = new BrefIntroAdapter(MoviePage.this, names, values);
                    videoBrefIntros.setAdapter(biAdapter);
                    videoName.setText(details.getName());
                    videoPlayNumber.setText(details.getHit());
                    Glide.with(MoviePage.this).load(HttpUtils.appendUrl(details.getImg())).crossFade().into(movieBrefImg);
                }
            }
        });
    }

    /**
     * 获取 视频播放地址，并播放
     * @param vfId
     */
    public void getFirstURL(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        String login_info  = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(login_info)){

        }else{
            UserModel model= new Gson().fromJson(login_info,UserModel.class);
            maps.put("userid",model.getId());
        }
        HttpApis.getVideoListInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("first-url"+response);
                ResponseObj<PlayList, RespHeader> resp = new ResponseObj<PlayList, RespHeader>();
                ResponseParser.parse(resp, response, PlayList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    PlayList.PlaysListBean details= resp.getBody().getPlaysList().get(0);
//                    getRealURL(details.getStandardUrl());
                    getRealURL(details.getHighUrl());
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
        String userinfo= ACache.get(getApplicationContext()).getAsString("userinfo");


        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("videoSourceURL", url);
        if (StringUtils.isNullOrEmpty(userinfo)) {
            UserModel model= new Gson().fromJson(userinfo,UserModel.class);
            maps.put("cellphone", model.getPhoneNo());
        }
        maps.put("freetag", 1);
        HttpApis.getVideoRealURL(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("real_url:"+response);
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
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width= ScreenUtils.getScreenWidth(this)/3;
        movieBrefImg.setLayoutParams(params);
        brefExpand.setOnClickListener((View v) -> {
            if (flag) {
                brefTxt2.setVisibility(View.GONE);
                brefExpand.setImageResource(R.drawable.icon_expand);
                flag = false;
            } else {
                brefTxt2.setVisibility(View.VISIBLE);
                brefExpand.setImageResource(R.drawable.icon_zoom);
                flag = true;
            }
        });
    }

    @Override
    public void initDatas() {
    }

    private void getYouLikeDatas(int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", size);
        HttpApis.getYouLikeList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("youlike"+response);
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
//                    list_list= resp.getBody().getLikeList();
                    grid_list.addAll(resp.getBody().getLikeList());
                    initBottomGrid(grid_list);
//                    initBottomList(list_list);
                }
            }
        });
    }

    private void initBottomGrid(List<LikeList.LikeListBean> grid_list) {
        grid_adapter = new VideoItemGridAdapter(MoviePage.this, grid_list);
        videoBottomGrid.setHasFixedSize(true);
        GridLayoutManager manager= new GridLayoutManager(this,3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        videoBottomGrid.setLayoutManager(manager);
        videoBottomGrid.addItemDecoration(new RecycleViewDivider(MoviePage.this,GridLayoutManager.HORIZONTAL));
        videoBottomGrid.setAdapter(grid_adapter);
        grid_adapter.notifyDataSetChanged();
        grid_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                getChangePage(grid_list.get(i).getId());
            }
        });
    }


    public void getChangePage(String vfId){
        HashMap<String,Object> maps = new HashMap<>();
        maps.put("vfid",vfId);
        maps.put("typeid",VideoType.MOVIE);
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("getchange"+response);
                ResponseObj<VideoDetails,RespHeader> resp= new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp,response,VideoDetails.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){

                    if (resp.getBody().getVfinfo().getTypeId()== VideoType.MOVIE.getId()){
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToMoviePage(MoviePage.this,bundle);
                        MoviePage.this.finish();

                    }else if (resp.getBody().getVfinfo().getTypeId()==VideoType.TELEPLAY.getId()){
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(MoviePage.this,bundle);
                        MoviePage.this.finish();

                    }else if (resp.getBody().getVfinfo().getTypeId()==VideoType.SPORTS.getId()){
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(MoviePage.this,bundle);
                        MoviePage.this.finish();

                    }
                    else if (resp.getBody().getVfinfo().getTypeId()==VideoType.VARIATY.getId()){
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(MoviePage.this,bundle);
                        MoviePage.this.finish();

                    }
                    else if (resp.getBody().getVfinfo().getTypeId()==VideoType.LIVE.getId()){
                        UIHelper.ToLivePage(MoviePage.this);
                        MoviePage.this.finish();

                    }
                }
            }
        });
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
            getWindow().getDecorView().invalidate();
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

}
