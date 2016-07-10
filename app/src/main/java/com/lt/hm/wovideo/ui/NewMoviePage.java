package com.lt.hm.wovideo.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * @create_date 16/6/29
 */
public class NewMoviePage extends BaseActivity {
    @BindView(R.id.video_player)
    WoVideoPlayer videoPlayer;
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
    @BindView(R.id.movie_bref_img)
    ImageView movieBrefImg;
    @BindView(R.id.video_bref_intros)
    CustomListView videoBrefIntros;
    @BindView(R.id.movie_bref_purch)
    ImageView movieBrefPurch;
    @BindView(R.id.bref_txt1)
    TextView brefTxt1;
    @BindView(R.id.bref_txt2)
    TextView brefTxt2;
    @BindView(R.id.bref_expand)
    ImageView brefExpand;
    @BindView(R.id.video_bottom_grid)
    RecyclerView videoBottomGrid;

    VideoModel video = new VideoModel();
    VideoUrl videoUrl= new VideoUrl();
    VideoItemGridAdapter grid_adapter;
    BrefIntroAdapter biAdapter;
    String[] names = new String[]{"导演", "主演"};
    String[] values = new String[]{"王京", "周润发，刘德华"};
    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        hideSomething();
        videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("id")) {
            String id = bundle.getString("id");
            getVideoDetails(id);
            getFirstURL(id);
        }
        getYouLikeDatas(10);

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
                    grid_list.addAll(resp.getBody().getLikeList());
                    initBottomGrid(grid_list);
                }
            }
        });
    }

    private void initBottomGrid(List<LikeList.LikeListBean> grid_list) {
        grid_adapter = new VideoItemGridAdapter(NewMoviePage.this, grid_list);
        videoBottomGrid.setHasFixedSize(true);
        GridLayoutManager manager= new GridLayoutManager(this,3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        videoBottomGrid.setLayoutManager(manager);
        videoBottomGrid.addItemDecoration(new RecycleViewDivider(NewMoviePage.this,GridLayoutManager.HORIZONTAL));
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
        maps.put("typeid", VideoType.MOVIE);
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
                        UIHelper.ToMoviePage(NewMoviePage.this,bundle);
                        NewMoviePage.this.finish();

                    }else if (resp.getBody().getVfinfo().getTypeId()==VideoType.TELEPLAY.getId()){
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this,bundle);
                        NewMoviePage.this.finish();

                    }else if (resp.getBody().getVfinfo().getTypeId()==VideoType.SPORTS.getId()){
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this,bundle);
                        NewMoviePage.this.finish();

                    }
                    else if (resp.getBody().getVfinfo().getTypeId()==VideoType.VARIATY.getId()){
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle =  new Bundle();
                        bundle.putString("id",vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this,bundle);
                        NewMoviePage.this.finish();

                    }
                    else if (resp.getBody().getVfinfo().getTypeId()==VideoType.LIVE.getId()){
                        UIHelper.ToLivePage(NewMoviePage.this);
                        NewMoviePage.this.finish();

                    }
                }
            }
        });
    }

    private WoVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new WoVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoPlayer.close();
            NewMoviePage.this.finish();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                videoPlayer.setPageType(LiveMediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                videoPlayer.setPageType(LiveMediaController.PageType.EXPAND);
                videoPlayer.alwaysShowController();
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
                TLog.log("video-details"+response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    VideoDetails.VfinfoBean details= resp.getBody().getVfinfo();
                    values= new String[]{details.getDirector(),details.getStars()};
                    biAdapter = new BrefIntroAdapter(NewMoviePage.this, names, values);
                    videoBrefIntros.setAdapter(biAdapter);
                    videoName.setText(details.getName());
                    videoPlayNumber.setText(details.getHit());
                    Glide.with(NewMoviePage.this).load(HttpUtils.appendUrl(details.getImg())).centerCrop().crossFade().into(movieBrefImg);
                }
            }
        });
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

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

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
                    TLog.log("first_url"+details.getStandardUrl());
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
        maps.put("freetag", 1);
        HttpApis.getVideoRealURL(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoURL, RespHeader> resp = new ResponseObj<VideoURL, RespHeader>();
                ResponseParser.parse(resp, response, VideoURL.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    if (videoUrl!=null && video!=null && videoPlayer!=null){
                        videoUrl.setFormatUrl(resp.getBody().getUrl());
                        video.setmPlayUrl(videoUrl);
                        videoPlayer.loadAndPlay(videoUrl, 0);
                    }
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            videoPlayer.setPageType(LiveMediaController.PageType.SHRINK);
        }
    }

}
