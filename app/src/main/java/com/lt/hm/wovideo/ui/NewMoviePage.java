package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.comment.CommentAdapter;
import com.lt.hm.wovideo.adapter.home.LikeListAdapter;
import com.lt.hm.wovideo.adapter.video.BrefIntroAdapter;
import com.lt.hm.wovideo.adapter.video.EpisodeAdapter;
import com.lt.hm.wovideo.base.BaseVideoActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.interf.OnMediaOtherListener;
import com.lt.hm.wovideo.interf.onFunctionListener;
import com.lt.hm.wovideo.model.CommentModel;
import com.lt.hm.wovideo.model.LikeModel;
import com.lt.hm.wovideo.model.PlaysListBean;
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
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.widget.Anthology.AnthologyLinear;
import com.lt.hm.wovideo.widget.Anthology.FunctionLinear;
import com.lt.hm.wovideo.widget.Anthology.QualityLinear;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.lt.hm.wovideo.widget.SpacesItemDecoration;
import com.lt.hm.wovideo.widget.SpacesVideoItemDecoration;
import com.lt.hm.wovideo.widget.TextViewExpandableAnimation;
import com.lt.hm.wovideo.widget.multiselector.MultiSelector;
import com.lt.hm.wovideo.widget.multiselector.SingleSelector;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

//import cn.handsight.android.handsightsdk.fragment.HsBaseFragment;
//import cn.handsight.android.handsightsdk.fragment.HsPortraitFragment;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class NewMoviePage extends BaseVideoActivity implements OnMediaOtherListener
        , PopupWindow.OnDismissListener
        , onFunctionListener
//        implements HsBaseFragment.PlayerListener,HsBaseFragment.EventListener
{

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
    TextViewExpandableAnimation bref_txt_short;
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
    String[] values = new String[]{};
    List<CommentModel> beans;
    @BindView(R.id.video_comment_list)
    RecyclerView videoCommentList;
    @BindView(R.id.empty_view)
    TextView empty;
    @BindView(R.id.et_add_comment)
    EditText etAddComment;
    @BindView(R.id.add_comment)
    LinearLayout addComment;

//    HsPortraitFragment motiveFragment;


    @BindView(R.id.ly_demand_anthology)
    View demandSelectView;
    @BindView(R.id.anthology_list)
    RecyclerView anthologyList;
    @BindView(R.id.anthology_text)
    TextView anthologyText;
    @BindView(R.id.anthology_all)
    Button anthologyALL;

    private ArrayList<VideoUrl> videoUrls = new ArrayList<>();

    private int selectedItem = 0;
    private MultiSelector mMultiSelector = new SingleSelector();
    boolean expand_flag = false;
    private LinearLayoutManager manager;

    private boolean isCollected;
    private EpisodeAdapter adapter;

    private String mQualityName;
    private String img_url;
    private String share_title;
    private String share_desc;
    private String vfId;
    private String collect_tag;
    private int typeId = 0;

    private Context mContext;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaController.setOnMediaOtherListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mContext = this;
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
        movieBrefPurch.setVisibility(View.GONE);

        if (typeId == VideoType.SMIML.getId()) {//小视屏
            brefFl.setVisibility(View.GONE);
        }

        if (typeId == VideoType.TELEPLAY.getId()
                || typeId == VideoType.VARIATY.getId()
                || typeId == VideoType.SPORTS.getId()) {//电视剧,综艺,体育
            brefFl.setVisibility(View.GONE);
            demandSelectView.setVisibility(View.VISIBLE);
            isMovie = false;
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

    private List<LikeModel> grid_list = new ArrayList<>();//猜你喜欢

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
        NetUtils.getVideoDetails(id, this);
    }

    private SpacesVideoItemDecoration itemGvDecoration;
    private SpacesVideoItemDecoration itemLiDecoration;
    private String videoId;
    private int totalTime;

    @Override
    public void initViews() {
//        motiveFragment = (HsPortraitFragment) getFragmentManager().findFragmentById(R.id.hs_vertical_fragment);
//        videoId = "1750";
//        totalTime = 477;
//        motiveFragment.setVideo(videoId, totalTime, 0, HsBaseFragment.PLAY_SATART);
        videoProjection.setOnClickListener(v -> onTouClick());

        videoShare.setOnClickListener((View v) ->
                onShareClick()
        );

        videoLike.setOnClickListener(v -> onZanClick());

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
            onCollectClick();
        });

        itemGvDecoration = new SpacesVideoItemDecoration(8, false);
        itemLiDecoration = new SpacesVideoItemDecoration(10, true);
        anthologyALL.setOnClickListener((View v) -> {
            anthologyList.removeItemDecoration(itemLiDecoration);
            anthologyList.removeItemDecoration(itemGvDecoration);
            if (expand_flag) {
                manager = new LinearLayoutManager(this);
                manager.setOrientation(GridLayoutManager.HORIZONTAL);
                anthologyList.addItemDecoration(itemLiDecoration);
                expand_flag = false;
                anthologyList.scrollToPosition(selectedItem - 1 > 0 ? selectedItem - 1 : selectedItem);
            } else {
                manager = new GridLayoutManager(this, 6);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                anthologyList.addItemDecoration(itemGvDecoration);
                expand_flag = true;
            }
            anthologyList.setLayoutManager(manager);
        });
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
            case HttpApis.http_video_detail://详情
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
                antholys.clear();
                antholys.addAll(vaP.getBody().getPlaysList());
                playIntro = antholys.get(0);

                if (typeId == 2 || typeId == 3 || typeId == 4) {
                    initAnthologys();
                }

                isCollected = playIntro.getSc() != null && playIntro.getSc().equals("1");
                setCollectImg(isCollected);

                collect_tag = playIntro.getId();
                free_hint.setText(playIntro.getZan() + "");
                videoAddHit(playIntro.getId());

                initVideoModel(playIntro);

                if (videoUrls.size() > 0) {
                    isQualitySwitch = false;
                    getRealURL(videoUrls.get(0).getFormatUrl(), playIntro.getId());
                    mQualityName = videoUrls.get(0).getFormatName();
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
    private void initVideoModel(PlaysListBean playIntro) {
        videoUrls.clear();
        if (!TextUtils.isEmpty(playIntro.getFluentUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("流畅");
            url.setFormatUrl(playIntro.getFluentUrl());
            videoUrls.add(url);
        }
        if (!TextUtils.isEmpty(playIntro.getStandardUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("标清");
            url.setFormatUrl(playIntro.getStandardUrl());
            videoUrls.add(url);
        }
        if (!TextUtils.isEmpty(playIntro.getBlueUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("蓝光");
            url.setFormatUrl(playIntro.getBlueUrl());
            videoUrls.add(url);
        }
        if (!TextUtils.isEmpty(playIntro.getHighUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("高清");
            url.setFormatUrl(playIntro.getHighUrl());
            videoUrls.add(url);
        }
        if (!TextUtils.isEmpty(playIntro.getSuperUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("超清");
            url.setFormatUrl(playIntro.getSuperUrl());
            videoUrls.add(url);
        }
        if (!TextUtils.isEmpty(playIntro.getFkUrl())) {
            VideoUrl url = new VideoUrl();
            url.setFormatName("4K");
            url.setFormatUrl(playIntro.getFkUrl());
            videoUrls.add(url);
        }
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

    private void initAnthologys() {
        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        anthologyList.addItemDecoration(itemLiDecoration);
        anthologyList.setLayoutManager(manager);
        mMultiSelector.setSelectable(true);
        antholys.get(0).setSelect(true);
        adapter = new EpisodeAdapter(antholys);
        anthologyList.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            //改变之前被选中颜色
            antholys.get(selectedItem).setSelect(false);
            adapter.notifyItemChanged(selectedItem);
            selectedItem = i;
            //改变现在被选中颜色
            antholys.get(selectedItem).setSelect(true);
            adapter.notifyItemChanged(selectedItem);
            videoHistory.setEpisode(i + "");
            selectEpisode(i);
            if (anthologyPop != null) {
                anthologyLinear.notityAnthology(selectedItem);
            }
        });
//        if (getIntent().getExtras().containsKey("episode")) {
//            if (!getIntent().getExtras().getString("episode").equals("null")) {
//                int c_episode = Integer.parseInt(getIntent().getExtras().getString("episode"));
//                mMultiSelector.setSelected(c_episode - 1, 0, true);
//                adapter.setSelectedItem(c_episode - 1);
//                selectEpisode(c_episode + "");
//            } else {
//                mMultiSelector.setSelected(0, 0, true);
//                adapter.notifyItemChanged(0);
//                selectEpisode("1");
//            }
//        } else {
//            selectEpisode("1");
//            adapter.notifyItemChanged(0);
//            mMultiSelector.setSelected(0, 0, true);
//        }
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

    private PlaysListBean playIntro;

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
        NetUtils.getVideoRealURL(isAd ? "http://111.206.133.39:9910/video/wovideo//ads/spfb15/spfb15.m3u8" : url, UserMgr.getUsePhone(), this);
    }

    String per_Id;//单集Id
    List<PlaysListBean> antholys = new ArrayList<>();

    private void selectEpisode(int episode) {
        if (antholys.size() > 0) {
            videoHistory.setEpisode(episode + "");
        } else {
            videoHistory.setEpisode("0");
        }
        vfId = antholys.get(episode).getVfId();
        per_Id = antholys.get(episode).getId();
        PlaysListBean details = antholys.get(episode);
        videoHistory.setmId(details.getVfId());
        initVideoModel(antholys.get(episode));
        getRealURL(videoUrls.get(0).getFormatUrl(), details.getId());
    }

    @Override
    public void onDestroy() {
        videoHistory.setCurrent_positon(mPlayerPosition);
        videoHistory.setFlag("false");
        history.save(videoHistory);
        super.onDestroy();
    }

//    @Override
//    public void onLogin() {
//
//    }
//
//    @Override
//    public void onShare(String s, String s1, String s2, int i) {
//
//    }
//
//    @Override
//    public void onPlay(int i) {
//
//    }


    @Override
    public void adOnComplete() {
        super.adOnComplete();
        getFirstURL(vfId);
    }

    @Override
    public void onPassAd() {
        super.onPassAd();
        adOnComplete();
    }

    private AnthologyLinear anthologyLinear;
    private PopupWindow anthologyPop;
    private FunctionLinear functionLinear;
    private PopupWindow functionPop;
    private QualityLinear qualityLinear;
    private PopupWindow qualityPop;

    /**
     * 初始化,赞,分享,投屏,收藏popw
     */
    private void initQualityView() {
        qualityLinear = new QualityLinear(mContext, playIntro, this);
        qualityPop = new PopupWindow();
        qualityPop.setContentView(qualityLinear);
        initPop(qualityPop);
    }

    /**
     * 初始化,赞,分享,投屏,收藏popw
     */
    private void initFunctionView() {
        functionLinear = new FunctionLinear(mContext, this);
        functionPop = new PopupWindow();
        functionPop.setContentView(functionLinear);
        initPop(functionPop);
    }

    /**
     * 初始化选台popw
     */
    private void initAnthologyView() {
        anthologyLinear = new AnthologyLinear(mContext, antholys, this, selectedItem);
        anthologyPop = new PopupWindow();
        anthologyPop.setContentView(anthologyLinear);
        initPop(anthologyPop);
    }

    /**
     * 初始化设置popwindow属性
     */
    private void initPop(PopupWindow popupWindow) {
        // TODO Auto-generated method stub
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(this);
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    public void onChooseChannel(View v) {
        TLog.error("onChoose");
        if (anthologyPop == null) {
            initAnthologyView();
        }
        mMediaController.hide();
        anthologyPop.showAtLocation(v, Gravity.RIGHT | Gravity.CENTER, 0, 0);
    }

    @Override
    public void onChooseMore(View v) {
        if (functionPop == null) {
            initFunctionView();
        }
        mMediaController.hide();
        functionPop.showAtLocation(v, Gravity.RIGHT | Gravity.CENTER, 0, 0);
    }

    @Override
    public void onShowQuality(View v) {
        if (qualityPop == null) {
            initQualityView();
        }
        mMediaController.hide();
        qualityPop.showAtLocation(v, Gravity.RIGHT | Gravity.CENTER, 0, 0);
    }

    @Override
    public void onQualitySelect(String key, String value) {
        setQualitySwitchText(key);
        getRealURL(value, "");
        isQualitySwitch = true;
    }

    @Override
    public void onAnthologyItemClick(int position) {
        //改变之前被选中颜色
        antholys.get(selectedItem).setSelect(false);
        adapter.notifyItemChanged(selectedItem);
        selectedItem = position;
        //改变现在被选中颜色
        antholys.get(selectedItem).setSelect(true);
        adapter.notifyItemChanged(selectedItem);
        videoHistory.setEpisode(position + "");
        selectEpisode(position);
    }

    @Override
    public void onDismiss() {
        mMediaController.show();
    }

    @Override
    public void onZanClick() {
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
    public void onTouClick() {
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
    }

    @Override
    public void onShareClick() {
        ShareUtils.showShare(this, null, true, share_title, share_desc, HttpUtils.appendUrl(img_url));
    }

    @Override
    public void onCollectClick() {
        if (!isCollected) {
            CollectVideo();
            isCollected = true;
        } else {
            CancelCollect();
        }
    }


}
