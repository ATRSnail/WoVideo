package com.lt.hm.wovideo.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.comment.CommentAdapter;
import com.lt.hm.wovideo.adapter.video.VideoAnthologyAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemGridAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemListAdapter;
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
 * @create_date 16/6/7
 */
public class DemandPage extends BaseVideoActivity implements View.OnClickListener{

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
    @BindView(R.id.video_comment_list)
    RecyclerView videoCommentList;
    @BindView(R.id.anthology_all)
    Button anthologyALL;
    @BindView(R.id.img_collect)
    ImageView img_collect;
    @BindView(R.id.empty_view)
    TextView empty;
    @BindView(R.id.free_hint)
    TextView free_hint;
    @BindView(R.id.free_label)
    TextView mFreeLabel;

    VideoItemListAdapter list_adapter;
    VideoItemGridAdapter grid_adapter;
    CommentAdapter commentAdapter;
    List<PlayList.PlaysListBean> antholys;
    List<CommentModel.CommentListBean> beans;
    VideoAnthologyAdapter anthologyAdapter;
    boolean expand_flag = false;
    boolean isCollected = false;
    String vfId;
    String per_Id;//单集Id
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();
    @BindView(R.id.et_add_comment)
    EditText etAddComment;
    @BindView(R.id.add_comment)
    LinearLayout addComment;
    private int pageSize = 50;

    private String img_url;
    private String share_title;
    private String share_desc;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoTitle(videoName.getText().toString());
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
//        if (null == woPlayer) return;
//        /***
//         * 根据屏幕方向重新设置播放器的大小
//         */
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().getDecorView().invalidate();
//            woPlayer.getLayoutParams().height = ScreenUtils.getScreenHeight(this);
//            woPlayer.getLayoutParams().width = ScreenUtils.getScreenWidth(this);
//            getWindow().getDecorView().invalidate();
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
//            attrs.flags &= (WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().setAttributes(attrs);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            float width = DensityUtil.getWidthInPx(this);
//            float height = DensityUtil.dip2px(this, 200);
//            woPlayer.getLayoutParams().height = (int) height;
//            woPlayer.getLayoutParams().width = (int) width;
//        }
    }

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
        antholys = new ArrayList<>();
        beans = new ArrayList<>();
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
     * 获取 视频评论列表
     *
     * @param vfId
     */
    private void getCommentList(String vfId) {
        if (beans.size()>0){
            beans.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
        String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(userinfo)) {
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            map.put("userId", model.getId());
            map.put("pageNum", 1);
            map.put("numPerPage", pageSize);
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
                            videoCommentList.setLayoutManager(new LinearLayoutManager(DemandPage.this));
                            videoCommentList.addItemDecoration(new RecycleViewDivider(DemandPage.this, LinearLayoutManager.VERTICAL));
                            videoCommentList.setItemAnimator(new DefaultItemAnimator());
                            videoCommentList.setAdapter(commentAdapter);
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            //暂无评论内容布局添加
                            if (videoCommentList!=null){
                                videoCommentList.setVisibility(View.GONE);
                            }
                            if (empty==null)
                                return;
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
            UnLoginHandler.unLogin(DemandPage.this);
        }

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
                vfId = antholys.get(i).getVfId();
                per_Id = antholys.get(i).getId();

                getRealURL(antholys.get(i).getFluentUrl(), false, antholys.get(i).getId());


//                getRealURL(bean.getStandardUrl());
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
        //移除  超过6条的数据
        if (grid_list.size() > 6) {
            int tmp = grid_list.size() - 6;
            for (int i = 0; i < tmp; i++) {
                grid_list.remove(0);
            }
        }
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
//        videoBottomList.setHasFixedSize(false);
//        videoBottomList.setLayoutManager(line_manager);
//        videoBottomList.setAdapter(list_adapter);
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
                    share_desc = details.getIntroduction();
                    share_title= details.getName();
                    img_url = details.getImg();
                    String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
                    if (!StringUtils.isNullOrEmpty(userinfo)){
                        UserModel model = new Gson().fromJson(userinfo,UserModel.class);
                        String tag = ACache.get(getApplicationContext()).getAsString(model.getId() + "free_tag");
                        if (!StringUtils.isNullOrEmpty(tag)) {
//                            free_hint.setText(" "+"已免流");
                            mFreeLabel.setVisibility(View.VISIBLE);
                        }
                    }
                    videoName.setText(details.getName());

                    videoPlayNumber.setText(details.getHit());
                    if (details.getGxzt().equals("0")) {
                        anthologyText.setText("更新至" + details.getJjs() + "集");
                    } else {
                        anthologyText.setText("完结 共" + details.getJjs() + "集");
                    }

                    videoHistory.setmName(details.getName());
                    videoHistory.setmId(details.getId());
                    videoHistory.setCreate_time(System.currentTimeMillis()+"");
                    videoHistory.setImg_url(details.getImg());

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
                    per_Id = details.getId();
                    getCommentList(per_Id);
                    // TODO: 16/7/11  ADD PLAY URL SELECTOR
                    VideoModel model = new VideoModel();
                    ArrayList<VideoUrl> urls= new ArrayList<VideoUrl>();
                    if (!StringUtils.isNullOrEmpty(details.getFluentUrl())){
                        VideoUrl url= new VideoUrl();
                        url.setFormatName("流畅");
                        url.setFormatUrl(details.getFluentUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getStandardUrl())){
                        VideoUrl url= new VideoUrl();
                        url.setFormatName("标清");
                        url.setFormatUrl(details.getStandardUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getBlueUrl())){
                        VideoUrl url= new VideoUrl();
                        url.setFormatName("蓝光");
                        url.setFormatUrl(details.getBlueUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getHighUrl())){
                        VideoUrl url= new VideoUrl();
                        url.setFormatName("高清");
                        url.setFormatUrl(details.getHighUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getSuperUrl())){
                        VideoUrl url= new VideoUrl();
                        url.setFormatName("超清");
                        url.setFormatUrl(details.getSuperUrl());
                        urls.add(url);
                    }
                    if (!StringUtils.isNullOrEmpty(details.getFkUrl())){
                        VideoUrl url= new VideoUrl();
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

                    if (model.getmVideoUrl().size()>0){
                        getRealURL(model.getmVideoUrl().get(0).getFormatUrl(), false, details.getId());
                        setQualitySwitchText(model.getmVideoUrl().get(0).getFormatName());
                    }
//                    getRealURL(details.getStandardUrl());
                }
            }
        });
    }

    /**
     * 获取视频播放地址
     *
     * @param url
     * @param isQualitySwitch
     * @param videoId
     */
    private void getRealURL(String url, boolean isQualitySwitch, String videoId) {
        if (!isQualitySwitch) {
            TLog.log("Bullet","get real url"+ videoId);
            setVideoId(videoId); // Set Video Id for Bullet Screen usage
            getBullets(); // get Bullet list after set Video Id.
        }
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
                    // Reset player and params.
                    releasePlayer();
                    mPlayerPosition = isQualitySwitch ? mPlayerPosition : 0;
                    // Set Player
                    setIntent(onUrlGot(video));
                    onShown();
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

    @Override
    public void initViews() {
        addComment.setOnClickListener((View v) -> {
            if (TextUtils.isEmpty(etAddComment.getText().toString())) {
                Toast.makeText(getApplicationContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            SendComment(etAddComment.getText().toString());
        });

        videoCollect.setOnClickListener(this);
        videoProjection.setVisibility(View.GONE);
        videoShare.setVisibility(View.VISIBLE);
        videoShare.setOnClickListener(this);
        videoCollect.setVisibility(View.VISIBLE);
        videoCollect.setOnClickListener((View v) -> {
            if (!isCollected){
                CollectVideo();
                isCollected=true;
            }else{
                Toast.makeText(getApplicationContext(),"取消收藏",Toast.LENGTH_SHORT).show();
                isCollected=false;
                img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
            }
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
     * 添加评论
     *
     * @param s
     */
    private void SendComment(String s) {
        TLog.log("comment_send" + per_Id);
        HashMap<String, Object> map = new HashMap<>();
        String string = ACache.get(this).getAsString("userinfo");
        if (!StringUtils.isNullOrEmpty(string)) {
            UserModel model = new Gson().fromJson(string, UserModel.class);
            map.put("userId", model.getId());
            map.put("vfId", per_Id);
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
                        TLog.log("comment_list" + per_Id);
                        getCommentList(per_Id);
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
            UnLoginHandler.unLogin(DemandPage.this);
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
            map.put("vfid", per_Id);
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
                    }else{
                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
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
                ShareUtils.showShare(this, null, true, share_title,share_desc, HttpUtils.appendUrl(img_url));
                break;
        }
    }

    /**
     *跳转页面
     * @param vfId
     */
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

    @Override
    public void onDestroy() {
        videoHistory.setCurrent_positon(getCurrentPosition());
        videoHistory.setFlag("false");
        history.save(videoHistory);
        super.onDestroy();

    }
}
