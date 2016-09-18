package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.comment.CommentAdapter;
import com.lt.hm.wovideo.adapter.home.LikeListAdapter;
import com.lt.hm.wovideo.adapter.video.BrefIntroAdapter;
import com.lt.hm.wovideo.base.BaseVideoActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.model.CommentModel;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.model.ValidateComment;
import com.lt.hm.wovideo.model.VfinfoModel;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.response.ResponseComment;
import com.lt.hm.wovideo.model.response.ResponseLikeList;
import com.lt.hm.wovideo.model.response.ResponsePlayList;
import com.lt.hm.wovideo.model.response.ResponseValidateComment;
import com.lt.hm.wovideo.model.response.ResponseVfinfo;
import com.lt.hm.wovideo.model.response.ResponseVideoRealUrl;
import com.lt.hm.wovideo.utils.ShareUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.lt.hm.wovideo.widget.SpacesItemDecoration;
import com.lt.hm.wovideo.widget.SpacesVideoItemDecoration;
import com.lt.hm.wovideo.widget.multiselector.MultiSelector;
import com.lt.hm.wovideo.widget.multiselector.SingleSelector;
import com.lt.hm.wovideo.widget.multiselector.SwappingHolder;
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

    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_play_number)
    TextView videoPlayNumber;
    @BindView(R.id.video_collect)
    TextView videoCollect;
    @BindView(R.id.video_share)
    TextView videoShare;
    @BindView(R.id.video_projection)
    TextView videoProjection;
    @BindView(R.id.video_like)
    TextView videoLike;
    @BindView(R.id.movie_bref_img)
    ImageView movieBrefImg;
    @BindView(R.id.video_bref_intros)
    CustomListView videoBrefIntros;
    @BindView(R.id.movie_bref_purch)
    ImageView movieBrefPurch;
    @BindView(R.id.bref_txt_short)
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
    @BindView(R.id.fl_bref)
    PercentRelativeLayout brefFl;
    CommentAdapter commentAdapter;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();
    LikeListAdapter grid_adapter;
    BrefIntroAdapter biAdapter;
    String[] names = new String[]{"导演", "主演", "类型", "地区", "年份", "来源"};
    String[] values = new String[]{"王京", "周润发，刘德华"};
    List<CommentModel> beans;
    @BindView(R.id.video_comment_list)
    RecyclerView videoCommentList;
    @BindView(R.id.empty_view)
    TextView empty;
    @BindView(R.id.et_add_comment)
    EditText etAddComment;
    @BindView(R.id.add_comment)
    LinearLayout addComment;


    @BindView(R.id.ly_demand_anthology)
    View demandSelectView;
    @BindView(R.id.anthology_list)
    RecyclerView anthologyList;
    @BindView(R.id.anthology_text)
    TextView anthologyText;
    @BindView(R.id.anthology_all)
    Button anthologyALL;

    private MultiSelector mMultiSelector = new SingleSelector();
    private long cur_position;
    private String mCurrentEpisode = "1";
    private ArrayList<String> mEpisodes;
    boolean expand_flag = false;
    private LinearLayoutManager manager;

    boolean text_flag = false;
    private boolean isCollected;

    private String mQualityName;
    private String img_url;
    private String share_title;
    private String share_desc;
    private String vfId;
    private String collect_tag;
    private int typeId = 0;

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
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("id")) {
            vfId = bundle.getString("id");
            getVideoDetails(vfId);
            getFirstURL(vfId);
            getCommentList(vfId);
            if (mMediaController != null) {
                mMediaController.setBulletScreen(false);
            }
        }
        if (bundle.containsKey("typeId")) {
            typeId = bundle.getInt("typeId");
        }


        hideSomething();
        getYouLikeDatas(6);

    }

    /**
     * 在正式项目中需要 解禁
     */
    private void hideSomething() {
        videoCollect.setVisibility(View.VISIBLE);
        videoShare.setVisibility(View.VISIBLE);
        videoProjection.setVisibility(View.VISIBLE);
        bref_txt_short.setVisibility(View.VISIBLE);
        brefExpand.setVisibility(View.VISIBLE);
        movieBrefPurch.setVisibility(View.GONE);
        if (typeId == 1 || typeId == 5) {
            if (typeId == VideoType.SMIML.getId()) {
                brefFl.setVisibility(View.GONE);
            }
        }
        if (typeId == 2 || typeId == 3 || typeId == 4) {
            brefFl.setVisibility(View.GONE);
            demandSelectView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 增加访问量
     *
     * @param vfId
     */
    private void videoAddHit(String vfId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("playsId", vfId);
        HttpApis.addVideoHit(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log("addHit" + response);
            }
        });
    }


    /**
     * 获取评论信息
     *
     * @param vfId
     */
    private void getCommentList(String vfId) {
        if (beans.size() > 0) {
            beans.clear();
        }
        if (UserMgr.isLogin()) {
            NetUtils.getCommentList(vfId, this);
        } else {
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
    }

    private List<LikeModel> grid_list = new ArrayList<LikeModel>();//猜你喜欢

    /**
     * 猜你喜欢接口调用
     */
    private void getYouLikeDatas(int size) {
        NetUtils.getYouLikeData(1, size, "", "", typeId == 0 ? "1" : typeId + "", this);
    }


    /**
     * 填充猜你喜欢数据
     *
     * @param grid_list
     */
    private void initBottomGrid(List<LikeModel> grid_list) {
        grid_adapter = new LikeListAdapter(R.layout.layout_new_home_movie_item, grid_list, false);
        videoBottomGrid.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        videoBottomGrid.setLayoutManager(manager);
        videoBottomGrid.addItemDecoration(new SpacesItemDecoration(10, false));
        videoBottomGrid.setAdapter(grid_adapter);
        grid_adapter.notifyDataSetChanged();
        grid_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                UIHelper.ToAllCateVideo(NewMoviePage.this, grid_list.get(i).getTypeId(), grid_list.get(i).getId());
                NewMoviePage.this.finish();
            }
        });
    }


    private void setCollectImg(boolean isCollected) {
        videoCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(isCollected ? R.drawable.icon_collect_press : R.drawable.icon_collect), null, null);
    }


    /**
     * 获取视频详情数据
     *
     * @param id
     */
    private VfinfoModel vfinfoModel;

    public void getVideoDetails(String id) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", id);
        HttpApis.getVideoInfo(maps, HttpApis.http_fiv, new HttpCallback<>(ResponseVfinfo.class, this));
    }

    private RecyclerView.ItemDecoration itemDecoration;

    @Override
    public void initViews() {
        videoProjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                Fragment prev = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                // Create and show the dialog.
//                DeviceListDialogFragment newFragment = DeviceListDialogFragment.newInstance();
//                newFragment.show(ft, DIALOG_FRAGMENT_TAG);
//                newFragment.setButtonClickedListener(NewMoviePage.this);
//
            }
        });

        videoShare.setOnClickListener((View v) -> {
            ShareUtils.showShare(this, null, true, share_title, share_desc, HttpUtils.appendUrl(img_url));
        });

        videoLike.setOnClickListener(v -> clickZan());

        addComment.setOnClickListener((View v) -> {
            if (TextUtils.isEmpty(etAddComment.getText().toString().trim())) {
                UT.showNormal("评论内容不能为空");
                return;
            }

            if (etAddComment.getText().toString().trim().length() > 200) {
                UT.showNormal("评论内容不能超过200字");
                return;
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            checkCommentValide(etAddComment.getText().toString());
        });

        videoCollect.setOnClickListener((View v) -> {
            if (!isCollected) {
                CollectVideo();
                isCollected = true;
            } else {
                CancelCollect();
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

        itemDecoration = new SpacesVideoItemDecoration(10, false);
        anthologyALL.setOnClickListener((View v) -> {
            if (expand_flag) {
                manager = new LinearLayoutManager(this);
                manager.setOrientation(GridLayoutManager.HORIZONTAL);
                expand_flag = false;
            } else {
                manager = new GridLayoutManager(this, 6);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                anthologyList.invalidateItemDecorations();
                anthologyList.removeItemDecoration(itemDecoration);
                anthologyList.addItemDecoration(itemDecoration);
                expand_flag = true;
            }
            anthologyList.setLayoutManager(manager);
            anthologyList.scrollToPosition(currentEpisode - 1 > 0 ? currentEpisode - 1 : currentEpisode);
        });
    }

    /**
     * 点赞
     */
    private void clickZan() {
        if (playIntro == null) {
            UT.showNormal("点赞失败");
            return;
        }
        if (!UserMgr.isLogin()) {
            UT.showNormal("请先登录");
            return;
        }
        NetUtils.clickZan(playIntro.getId(), UserMgr.getUseId(), this);
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {
            case HttpApis.http_zan:
                String val = (String) value;
                if (!TextUtils.isEmpty(val) && val.equals(ResponseCode.Success)) {
                    UT.showNormal("点赞成功");
                } else {
                    UT.showNormal("点赞失败");
                }
                break;
            case HttpApis.http_video_collect:
                String va = (String) value;
                if (!TextUtils.isEmpty(va) && va.equals(ResponseCode.Success)) {
                    UT.showNormal("收藏成功");
                    setCollectImg(true);
                } else {
                    UT.showNormal("收藏失败");
                    setCollectImg(false);
                }
                break;
            case HttpApis.http_valide_comment:
                ResponseValidateComment responseValidateComment = (ResponseValidateComment) value;
                ValidateComment validate = responseValidateComment.getBody();
                if (validate.getIsPass().equals("1")) {
                    //check validate success;
                    SendComment(etAddComment.getText().toString());
                } else {
                    UT.showNormal("言论不当，请斟酌发言");
                }
                break;
            case HttpApis.http_video_uncollect:
                String valColl = (String) value;
                if (!TextUtils.isEmpty(valColl) && valColl.equals(ResponseCode.Success)) {
                    isCollected = false;
                    setCollectImg(false);
                    UT.showNormal(getResources().getString(R.string.cancel_collect_success));
                } else {
                    UT.showNormal(getResources().getString(R.string.cancel_collect_failed));
                }
                break;
            case HttpApis.http_push_comment:
                String vaC = (String) value;
                if (!TextUtils.isEmpty(vaC) && vaC.equals(ResponseCode.Success)) {
                    UT.showNormal("评论成功");
                    getCommentList(vfId);
                } else {
                    UT.showNormal("评论失败");
                }
                break;
            case HttpApis.http_you_like:
                ResponseLikeList re = (ResponseLikeList) value;
                grid_list = re.getBody().getLikeList();
                if (grid_list.size() == 0) return;
                initBottomGrid(grid_list);
                break;
            case HttpApis.http_comments:
                ResponseComment responseComment = (ResponseComment) value;
                beans = responseComment.getBody().getCommentList();
                if (beans == null || beans.size() == 0) return;
                if (empty == null) return;
                empty.setVisibility(View.GONE);
                videoCommentList.setVisibility(View.VISIBLE);
                commentAdapter = new CommentAdapter(getApplicationContext(), beans);
                videoCommentList.setLayoutManager(new LinearLayoutManager(NewMoviePage.this));
                videoCommentList.addItemDecoration(new RecycleViewDivider(NewMoviePage.this, LinearLayoutManager.VERTICAL));
                videoCommentList.setItemAnimator(new DefaultItemAnimator());
                videoCommentList.setAdapter(commentAdapter);
                break;
            case HttpApis.http_fiv://详情
                ResponseVfinfo responseVfinfo = (ResponseVfinfo) value;
                vfinfoModel = responseVfinfo.getBody().getVfinfo();
                img_url = vfinfoModel.getImg();
                share_title = vfinfoModel.getName();
                share_desc = vfinfoModel.getIntroduction();
                mFreeLabel.setVisibility(UserMgr.isVip() ? View.VISIBLE : View.GONE);

                saveVideoHistory(vfinfoModel);
                setCollectImg(vfinfoModel.getSc() != null && vfinfoModel.getSc().equals("1"));

                if (typeId == 1 || typeId == 5) {
                    values = new String[]{vfinfoModel.getDirector(), vfinfoModel.getStars(), vfinfoModel.getLx(), vfinfoModel.getDq(), vfinfoModel.getNd(), vfinfoModel.getCpname()};
                    biAdapter = new BrefIntroAdapter(NewMoviePage.this, names, values);
                    videoBrefIntros.setAdapter(biAdapter);
                    Glide.with(NewMoviePage.this).load(HttpUtils.appendUrl(vfinfoModel.getImg())).placeholder(R.drawable.default_vertical).centerCrop().crossFade().into(movieBrefImg);
                }
                if (typeId == 2 || typeId == 3 || typeId == 4) {
                    if (vfinfoModel.getGxzt().equals("0")) {
                        anthologyText.setText("更新至" + vfinfoModel.getJjs() + "集");
                    } else {
                        anthologyText.setText("完结 共" + vfinfoModel.getJjs() + "集");
                    }
                }

                videoName.setText(share_title);
                bref_txt_short.setText(share_desc);
                bref_txt_long.setText(share_desc);
                videoPlayNumber.setText(vfinfoModel.getHit());
                break;
            case HttpApis.http_video_real_url:
                ResponseVideoRealUrl realUrl = (ResponseVideoRealUrl) value;
                if (videoUrl != null && video != null) {
                    TLog.error("realUrl--->" + realUrl.getBody().getUrl());

                    videoUrl.setFormatUrl(realUrl.getBody().getUrl());
                    video.setmPlayUrl(videoUrl);
                    // Reset player and params.
                    releasePlayer();
                    mPlayerPosition = isQualitySwitch ? mPlayerPosition : 0;
                    if (getIntent().getExtras().containsKey("cur_position")) {
                        long cur_position = getIntent().getExtras().getLong("cur_position");
                        mPlayerPosition = cur_position;
                        seekTo(mPlayerPosition);
                    }
                    // Set play URL and play it
                    setIntent(onUrlGot(video));
                    onShown();
                }
                break;
            case HttpApis.http_video_fake_url:
                ResponsePlayList vaP = (ResponsePlayList) value;
                if (vaP.getBody().getPlaysList().size() < 0 || vaP.getBody().getPlaysList().size() == 0)
                    return;
                playIntro = vaP.getBody().getPlaysList().get(0);

                if (typeId == 2 || typeId == 3 || typeId == 4) {
                    antholys.addAll(vaP.getBody().getPlaysList());
                    initAnthologys(vaP.getBody().getPlaysList().size());
                }

                isCollected = playIntro.getSc() != null && playIntro.getSc().equals("1");
                setCollectImg(isCollected);

                collect_tag = playIntro.getId();
                free_hint.setText(playIntro.getZan() + "");
                videoAddHit(playIntro.getId());

                VideoModel model = initVideoModel();

                setVideoModel(model);
                setQualityListener(new AVController.OnQualitySelected() {
                    @Override
                    public void onQualitySelect(String key, String value) {
                        getRealURL(value, "");
                        isQualitySwitch = true;
                    }
                });
                if (model.getmVideoUrl().size() > 0) {
                    isQualitySwitch = false;
                    getRealURL(model.getmVideoUrl().get(0).getFormatUrl(), playIntro.getId());
                    mQualityName = model.getmVideoUrl().get(0).getFormatName();
                }
                break;
        }
    }

    /**
     * 保存播放历史
     *
     * @param vfinfoModel
     */
    private void saveVideoHistory(VfinfoModel vfinfoModel) {
        videoHistory.setmName(vfinfoModel.getName());
        videoHistory.setmId(vfinfoModel.getId());
        videoHistory.setCreate_time(System.currentTimeMillis() + "");
        videoHistory.setImg_url(vfinfoModel.getImg());
    }

    /**
     * 得到电影播放画质类型
     *
     * @return
     */
    private VideoModel initVideoModel() {
        VideoModel model = new VideoModel();
        ArrayList<VideoUrl> urls = new ArrayList<VideoUrl>();
        if (!StringUtils.isNullOrEmpty(playIntro.getFluentUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("流畅");
            url.setFormatUrl(playIntro.getFluentUrl());
            urls.add(url);
        }
        if (!StringUtils.isNullOrEmpty(playIntro.getStandardUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("标清");
            url.setFormatUrl(playIntro.getStandardUrl());
            urls.add(url);
        }
        if (!StringUtils.isNullOrEmpty(playIntro.getBlueUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("蓝光");
            url.setFormatUrl(playIntro.getBlueUrl());
            urls.add(url);
        }
        if (!StringUtils.isNullOrEmpty(playIntro.getHighUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("高清");
            url.setFormatUrl(playIntro.getHighUrl());
            urls.add(url);
        }
        if (!StringUtils.isNullOrEmpty(playIntro.getSuperUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("超清");
            url.setFormatUrl(playIntro.getSuperUrl());
            urls.add(url);
        }
        if (!StringUtils.isNullOrEmpty(playIntro.getFkUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("4K");
            url.setFormatUrl(playIntro.getFkUrl());
            urls.add(url);
        }
        model.setmVideoUrl(urls);
        return model;
    }


    @Override
    public void onFail(String error, int flag) {
        switch (flag) {
            case HttpApis.http_zan:
            case HttpApis.http_valide_comment:
            case HttpApis.http_video_uncollect:
            case HttpApis.http_video_collect:
            case HttpApis.http_push_comment:
                UT.showNormal(error);
                break;
            case HttpApis.http_comments:
                //评论内容获取失败布局添加
                videoCommentList.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                empty.setText("获取评论失败,请稍后重试");
                break;

        }
    }

    private void initAnthologys(int length) {
        mEpisodes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            mEpisodes.add(i + 1 + "");
        }
        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        anthologyList.addItemDecoration(itemDecoration);
        anthologyList.setLayoutManager(manager);
        mMultiSelector.setSelectable(true);
        EpisodeAdapter adapter = new EpisodeAdapter();
        anthologyList.setAdapter(adapter);
        if (getIntent().getExtras().containsKey("episode")) {
            if (!getIntent().getExtras().getString("episode").equals("null")) {
                int c_episode = Integer.parseInt(getIntent().getExtras().getString("episode"));
                mMultiSelector.setSelected(c_episode - 1, 0, true);
                adapter.setSelectedItem(c_episode - 1);
                selectEpisode(c_episode + "");
            } else {
                mMultiSelector.setSelected(0, 0, true);
                adapter.notifyItemChanged(0);
                selectEpisode("1");
            }
        } else {
            selectEpisode("1");
            adapter.notifyItemChanged(0);
            mMultiSelector.setSelected(0, 0, true);
        }
//        mMultiSelector.setSelected(0,0,true);
    }

    /**
     * 检查言论是否合规
     *
     * @param s
     */
    private void checkCommentValide(String s) {
        NetUtils.checkCommentValide(s, this);
    }

    /**
     * 取消收藏
     */
    private void CancelCollect() {
        if (UserMgr.isLogin()) {
            NetUtils.CancelVideoCollect(collect_tag, UserMgr.getUseId(), this);
        } else {
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
    }

    /**
     * 收藏 视频接口调用
     */
    private void CollectVideo() {
        if (UserMgr.isLogin()) {
            NetUtils.VideoCollect(collect_tag, UserMgr.getUseId(), this);
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
        if (UserMgr.isLogin()) {
            NetUtils.SendComment(s, vfId, UserMgr.getUseId(), this);
        } else {
            UnLoginHandler.unLogin(NewMoviePage.this);
        }
    }

    private PlayList.PlaysListBean playIntro;

    /**
     * 获取 视频播放地址，并播放
     *
     * @param vfId
     */
    public void getFirstURL(String vfId) {
        NetUtils.getVideoFakeUrl(vfId, UserMgr.getUseId(), this);
    }

    private boolean isQualitySwitch;

    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url, String videoId) {
        if (!isQualitySwitch) {
            TLog.log("Bullet", "get real url" + videoId);
            setVideoId(videoId); // Set Video Id for Bullet Screen usage
            getBullets(); // get Bullet list after set Video Id.
        }
        NetUtils.getVideoRealURL(isAd?"http://111.206.133.39:9910/video/wovideo//ads/spfb15/spfb15.m3u8":url, UserMgr.getUsePhone(), this);
    }

    int currentEpisode = 0;
    String per_Id;//单集Id
    List<PlayList.PlaysListBean> antholys = new ArrayList<>();

    private void selectEpisode(String episode) {
        int i = Integer.valueOf(episode) - 1;
        currentEpisode = i;
        if (antholys.size() > 0) {
            videoHistory.setEpisode(episode);
        } else {
            videoHistory.setEpisode("0");
        }
        vfId = antholys.get(i).getVfId();
        per_Id = antholys.get(i).getId();
        PlayList.PlaysListBean details = antholys.get(i);
        videoHistory.setmId(details.getVfId());
        VideoModel model = initVideoModel();
        setVideoModel(model);
        getRealURL(model.getmVideoUrl().get(0).getFormatUrl(), details.getId());
    }

    @Override
    public void onDestroy() {
        videoHistory.setCurrent_positon(mPlayerPosition);
        videoHistory.setFlag("false");
        history.save(videoHistory);
        super.onDestroy();
    }

    private class EpisodeAdapter extends RecyclerView.Adapter<EpisodeHolder> {
        private int selectedItem = 0;

        @Override
        public EpisodeHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_video_episode_item, parent, false);
            EpisodeHolder episodeHolder = new EpisodeHolder(view);
            episodeHolder.itemView.setOnClickListener(v -> {
                notifyItemChanged(selectedItem);
                selectedItem = anthologyList.getChildAdapterPosition(v);
                notifyItemChanged(selectedItem);
                if (!mCurrentEpisode.equals(selectedItem + 1 + "")) {
                    cur_position = 0;
                    mCurrentEpisode = selectedItem + 1 + "";
                    videoHistory.setEpisode(mCurrentEpisode);
                    selectEpisode(mCurrentEpisode);
                }
            });
            return episodeHolder;
        }

        @Override
        public void onBindViewHolder(EpisodeHolder holder, int pos) {
            String episode = mEpisodes.get(pos);
            holder.bindCrime(episode);
            holder.setSelectionModeBackgroundDrawable(null);
            if (selectedItem == pos) {
                holder.mTitleTextView.setBackground(getResources().getDrawable(R.drawable.blue_circle));
                mMultiSelector.setSelected(holder, true);
                holder.mTitleTextView.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.mTitleTextView.setBackground(getResources().getDrawable(R.drawable.grey_circle));
                holder.mTitleTextView.setTextColor(getResources().getColor(R.color.class_font_color));
            }
        }

        @Override
        public int getItemCount() {
            return mEpisodes.size();
        }

        public void setSelectedItem(int pos) {
            notifyItemChanged(pos);
            selectedItem = pos;
        }
    }

    private class EpisodeHolder extends SwappingHolder {
        private final TextView mTitleTextView;
        private String mEposide;

        public EpisodeHolder(View itemView) {
            super(itemView, mMultiSelector);

            mTitleTextView = (TextView) itemView.findViewById(R.id.episode_text);
            itemView.setLongClickable(true);
        }


        public void bindCrime(String episode) {
            mEposide = episode;
            mTitleTextView.setText(episode);
        }

    }

    @Override
    public void adOnComplete() {
        super.adOnComplete();
        getFirstURL(vfId);
    }

    @Override
    public void onTouchAd() {
        super.onTouchAd();
    }

    @Override
    public void onPassAd() {
        super.onPassAd();
        isAd = false;
        getFirstURL(vfId);
    }
}
