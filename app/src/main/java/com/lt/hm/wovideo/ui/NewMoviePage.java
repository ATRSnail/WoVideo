package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.comment.CommentAdapter;
import com.lt.hm.wovideo.adapter.video.BrefIntroAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemGridAdapter;
import com.lt.hm.wovideo.base.BaseVideoActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.CommentModel;
import com.lt.hm.wovideo.model.LikeList;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.VideoURL;
import com.lt.hm.wovideo.utils.ShareUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class NewMoviePage extends BaseVideoActivity {

    //    @BindView(R.id.video_player)
//    WoVideoPlayer videoPlayer;
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
    @BindView(R.id.bref_txt_short)
//    TextView brefTxt1;
            TextView bref_txt_short;
    @BindView(R.id.bref_txt_long)
    TextView bref_txt_long;
    @BindView(R.id.bref_expand)
    ImageView brefExpand;
    @BindView(R.id.video_bottom_grid)
    RecyclerView videoBottomGrid;
    @BindView(R.id.free_hint)
    TextView free_hint;
    @BindView(R.id.free_label)
    TextView mFreeLabel;
    CommentAdapter commentAdapter;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();
    VideoItemGridAdapter grid_adapter;
    BrefIntroAdapter biAdapter;
    String[] names = new String[]{"导演", "主演", "类型", "地区", "年份", "来源"};
    String[] values = new String[]{"王京", "周润发，刘德华"};
    List<CommentModel.CommentListBean> beans;
    @BindView(R.id.video_comment_list)
    RecyclerView videoCommentList;
    @BindView(R.id.empty_view)
    TextView empty;
    @BindView(R.id.et_add_comment)
    EditText etAddComment;
    @BindView(R.id.add_comment)
    LinearLayout addComment;
    @BindView(R.id.img_collect)
    ImageView img_collect;
    boolean text_flag = false;
    private boolean isCollected;

    private String mQualityName;
    private String img_url;
    private String share_title;
    private String share_desc;
    private String vfId;
    private String collect_tag;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoTitle(videoName.getText().toString());
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        beans = new ArrayList<>();
        hideSomething();
//        videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("id")) {
            vfId = bundle.getString("id");
            getVideoDetails(vfId);
            getFirstURL(vfId);
            getCommentList(vfId);

        }

        getYouLikeDatas(10);

    }


    private void getCommentList(String vfId) {
        if (beans.size() > 0) {
            beans.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(userinfo)) {
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            map.put("userId", model.getId());
            map.put("pageNum", 1);
            map.put("numPerPage", 50);
            map.put("vfId", vfId);
            HttpApis.commentList(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    ResponseObj<CommentModel, RespHeader> resp = new ResponseObj<CommentModel, RespHeader>();
                    ResponseParser.parse(resp, response, CommentModel.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        if (!StringUtils.isNullOrEmpty(resp.getBody().getCommentList()) && resp.getBody().getCommentList().size() > 0) {
                            empty.setVisibility(View.GONE);
                            videoCommentList.setVisibility(View.VISIBLE);
                            beans.addAll(resp.getBody().getCommentList());
                            commentAdapter = new CommentAdapter(getApplicationContext(), beans);
                            videoCommentList.setLayoutManager(new LinearLayoutManager(NewMoviePage.this));
                            videoCommentList.addItemDecoration(new RecycleViewDivider(NewMoviePage.this, LinearLayoutManager.VERTICAL));
                            videoCommentList.setItemAnimator(new DefaultItemAnimator());
                            videoCommentList.setAdapter(commentAdapter);
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            //暂无评论内容布局添加
                            if (videoCommentList != null) {
                                videoCommentList.setVisibility(View.GONE);
                            }
                            empty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //评论内容获取失败布局添加
                        videoCommentList.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        empty.setText("获取评论失败,请稍后重试");
                    }
                }
            });
        } else {
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
    }

    private void getYouLikeDatas(int size) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("numPerPage", size);
        HttpApis.getYouLikeList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("youlike" + response);
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
        if (grid_list.size() > 6) {
            int tmp = grid_list.size() - 6;
            for (int i = 0; i < tmp; i++) {
                grid_list.remove(0);
            }
        }
        grid_adapter = new VideoItemGridAdapter(NewMoviePage.this, grid_list);
        videoBottomGrid.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        videoBottomGrid.setLayoutManager(manager);
        videoBottomGrid.addItemDecoration(new RecycleViewDivider(NewMoviePage.this, GridLayoutManager.HORIZONTAL));
        videoBottomGrid.setAdapter(grid_adapter);
        grid_adapter.notifyDataSetChanged();
        grid_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                getChangePage(grid_list.get(i).getId());
            }
        });
    }

    public void getChangePage(String vfId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", vfId);
        maps.put("typeid", VideoType.MOVIE.getId());
        HttpApis.getVideoInfo(maps, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("getchange" + response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {

                    if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToMoviePage(NewMoviePage.this, bundle);
                        NewMoviePage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this, bundle);
                        NewMoviePage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this, bundle);
                        NewMoviePage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(NewMoviePage.this, bundle);
                        NewMoviePage.this.finish();

                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.LIVE.getId()) {
                        UIHelper.ToLivePage(NewMoviePage.this);
                        NewMoviePage.this.finish();

                    }
                }
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
                TLog.log("video-details" + response);
                ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
                ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    VideoDetails.VfinfoBean details = resp.getBody().getVfinfo();
                    img_url = details.getImg();
                    share_title = details.getName();
                    share_desc = details.getIntroduction();
                    videoHistory.setmName(details.getName());
                    videoHistory.setmId(details.getId());
                    videoHistory.setCreate_time(System.currentTimeMillis() + "");
                    videoHistory.setImg_url(details.getImg());
                    values = new String[]{details.getDirector(), details.getStars(), details.getLx(), details.getDq(), details.getNd(), details.getCpname()};
                    biAdapter = new BrefIntroAdapter(NewMoviePage.this, names, values);
                    videoBrefIntros.setAdapter(biAdapter);
                    String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
                    if (!StringUtils.isNullOrEmpty(userinfo)) {
                        UserModel model = new Gson().fromJson(userinfo, UserModel.class);
                        String tag = ACache.get(getApplicationContext()).getAsString(model.getId() + "free_tag");
                        if (!StringUtils.isNullOrEmpty(tag)) {
//                            free_hint.setText(" "+"已免流");
                            mFreeLabel.setVisibility(View.VISIBLE);
                        }
                    }

                    videoName.setText(details.getName());
                    bref_txt_short.setText(details.getIntroduction());
                    bref_txt_long.setText(details.getIntroduction());
                    videoPlayNumber.setText(details.getHit());
                    Glide.with(NewMoviePage.this).load(HttpUtils.appendUrl(details.getImg())).placeholder(R.drawable.default_vertical).centerCrop().crossFade().into(movieBrefImg);


                }
            }
        });
    }


    /**
     * 在正式项目中需要 解禁
     */
    private void hideSomething() {
        videoCollect.setVisibility(View.VISIBLE);
        videoShare.setVisibility(View.VISIBLE);
        videoProjection.setVisibility(View.GONE);
        bref_txt_short.setVisibility(View.VISIBLE);
        brefExpand.setVisibility(View.VISIBLE);
        movieBrefPurch.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        videoShare.setOnClickListener((View v) -> {
            ShareUtils.showShare(this, null, true, share_title, share_desc, HttpUtils.appendUrl(img_url));

        });
        addComment.setOnClickListener((View v) -> {
            if (TextUtils.isEmpty(etAddComment.getText().toString())) {
                Toast.makeText(getApplicationContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            SendComment(etAddComment.getText().toString());
        });

        videoCollect.setOnClickListener((View v) -> {
            if (!isCollected) {
                CollectVideo();
                isCollected = true;
            } else {
                CancelCollect();
                isCollected = false;
            }
        });


        brefExpand.setOnClickListener((View v) -> {
            if (!text_flag) {
                text_flag = true;
//                bref_txt_short.setMaxHeight(100);
                bref_txt_short.setVisibility(View.GONE);
                bref_txt_long.setVisibility(View.VISIBLE);
                brefExpand.setImageDrawable(getResources().getDrawable(R.drawable.icon_zoom));
            } else {
                text_flag = false;
                brefExpand.setImageDrawable(getResources().getDrawable(R.drawable.icon_expand));
                bref_txt_short.setVisibility(View.VISIBLE);
                bref_txt_long.setVisibility(View.GONE);
            }
        });
    }

    private void CancelCollect() {

        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(userinfo)){
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userid", model.getId());
            map.put("vfids", collect_tag);
            HttpApis.cancelCollect(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log("cancel_result" + response);
                    ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                    ResponseParser.parse(resp, response, String.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cancel_collect_success), Toast.LENGTH_SHORT).show();
                    } else {
                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect_press));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cancel_collect_success), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
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
            map.put("vfid", collect_tag);
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
                    } else {
                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
                    }
                }
            });
        } else {
            // TODO: 16/7/8 未登录跳转登录页面
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
    }

    @Override
    public void initDatas() {

    }

    /**
     * 添加评论
     *
     * @param s
     */
    private void SendComment(String s) {
        HashMap<String, Object> map = new HashMap<>();
        String string = ACache.get(this).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(string)) {
            UserModel model = new Gson().fromJson(string, UserModel.class);
            map.put("userId", model.getId());
            map.put("vfId", vfId);
            map.put("comment", s);
            HttpApis.pushComment(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                    ResponseParser.parse(resp, response, String.class, RespHeader.class);
                    etAddComment.setText("");
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
                        TLog.log("comment_list" + vfId);
                        getCommentList(vfId);
                    } else {
                        if (StringUtils.isNullOrEmpty(resp.getHead().getRspMsg())) {
                            Toast.makeText(getApplicationContext(), "评论失败", Toast.LENGTH_SHORT).show();
                        } else {
                            TLog.log(resp.getHead().getRspMsg());
                            Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        } else {
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
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
                TLog.log("first-url" + response);
                ResponseObj<PlayList, RespHeader> resp = new ResponseObj<PlayList, RespHeader>();
                ResponseParser.parse(resp, response, PlayList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    PlayList.PlaysListBean details = resp.getBody().getPlaysList().get(0);
                    if (StringUtils.isNullOrEmpty(login_info)){
                        img_collect.setImageResource( R.drawable.icon_collect);
                    }else{
                        if (details.getSc() != null && details.getSc().equals("1")){
                            img_collect.setImageResource( R.drawable.icon_collect_press);
                            isCollected = true;
                        }else{
                            img_collect.setImageResource( R.drawable.icon_collect);
                            isCollected = false;
                        }
                    }
                    collect_tag = details.getId();
                    VideoModel model = new VideoModel();
                    ArrayList<VideoUrl> urls = new ArrayList<VideoUrl>();
                    if (!StringUtils.isNullOrEmpty(details.getFluentUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("流畅");
                        url.setFormatUrl(details.getFluentUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getStandardUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("标清");
                        url.setFormatUrl(details.getStandardUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getBlueUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("蓝光");
                        url.setFormatUrl(details.getBlueUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getHighUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("高清");
                        url.setFormatUrl(details.getHighUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getSuperUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("超清");
                        url.setFormatUrl(details.getSuperUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getFkUrl())) {
                        VideoUrl url = new VideoUrl();
                        url.setFormatName("4K");
                        url.setFormatUrl(details.getFkUrl());
                        urls.add(url);
                    }
                    model.setmVideoUrl(urls);

                    setVideoModel(model);
                    setQualityListener(new AVController.OnQualitySelected() {
                        @Override
                        public void onQualitySelect(String key, String value) {
                            getRealURL(value, true, "");
                        }
                    });
                    if (model.getmVideoUrl().size() > 0) {
                        getRealURL(model.getmVideoUrl().get(0).getFormatUrl(), false, details.getId());
                        mQualityName = model.getmVideoUrl().get(0).getFormatName();
                    }
//                    getRealURL(details.getFluentUrl());
//                    getRealURL(details.getStandardUrl());
                }
            }
        });
    }

    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url, boolean isQualitySwitch, String videoId) {
        if (!isQualitySwitch) {
            TLog.log("Bullet","get real url"+ videoId);
            setVideoId(videoId); // Set Video Id for Bullet Screen usage
            getBullets(); // get Bullet list after set Video Id.
        }
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("videoSourceURL", url);
//        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
//        if (StringUtils.isNullOrEmpty(userinfo)){
//            return;
//        }
        //FIXME: hardcode cellphone freetag
        maps.put("cellphone", "18513179404");
        maps.put("freetag", 1);
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
                    if (videoUrl != null && video != null ) {
                        videoUrl.setFormatUrl(resp.getBody().getUrl());
                        video.setmPlayUrl(videoUrl);
                        // Reset player and params.
                        releasePlayer();
                        mPlayerPosition = isQualitySwitch ? mPlayerPosition : 0;
                        if (getIntent().getExtras().containsKey("cur_position")){
                            long cur_position = getIntent().getExtras().getLong("cur_position");
                            mPlayerPosition= cur_position;
                            seekTo(mPlayerPosition);
                        }
                        // Set play URL and play it
                        setIntent(onUrlGot(video));
                        onShown();
                    }
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        videoHistory.setCurrent_positon(mPlayerPosition);
        videoHistory.setFlag("false");
        history.save(videoHistory);
        super.onDestroy();
    }
}
