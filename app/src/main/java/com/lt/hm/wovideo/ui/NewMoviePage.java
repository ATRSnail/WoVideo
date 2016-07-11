package com.lt.hm.wovideo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;
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
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.widget.CustomListView;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_ID_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_TYPE_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.PROVIDER_EXTRA;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/29
 */
public class NewMoviePage extends BaseActivity implements SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener {
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

    // Video thing
    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";

    private static final int MENU_GROUP_TRACKS = 1;
    private static final int ID_OFFSET = 2;

    private static final CookieManager defaultCookieManager;

    static {
        defaultCookieManager = new CookieManager();
        defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private EventLogger mEventLogger;
    private AVController mMediaController;
    private AspectRatioFrameLayout mVideoFrame;
    private View mShutterView;
    private SurfaceView mSurfaceView;

    private AVPlayer mPlayer;
    private boolean mPlayerNeedsPrepare;

    private long mPlayerPosition;
    private boolean mEnableBackgroundAduio = false;

    private Uri mContentUri;
    private int mContentType;
    private String mContentId;
    private String mProvider;

    private AudioCapabilitiesReceiver mAudioCapabilitiesReceiver;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = findViewById(R.id.video_root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //toggleControlsVisibility();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                return false;
            }
        });
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        || keyCode == KeyEvent.KEYCODE_ESCAPE
                        || keyCode == KeyEvent.KEYCODE_MENU) {
                    return false;
                }
                return mMediaController.dispatchKeyEvent(event);
            }
        });

        mShutterView = findViewById(R.id.shutter);

        mVideoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

        mMediaController = new KeyCompatibleMediaController(this);
        mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        mMediaController.setGestureListener(this);

        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
        mAudioCapabilitiesReceiver.register();

    }

    private Intent onUrlGot() {
        Intent mpdIntent = new Intent(this, MoviePage.class)
                .setData(Uri.parse(video.getmPlayUrl().getFormatUrl()))
                .putExtra(CONTENT_ID_EXTRA, video.getmVideoName())
                .putExtra(CONTENT_TYPE_EXTRA, Util.TYPE_HLS)
                .putExtra(PROVIDER_EXTRA, "");
        return mpdIntent;
    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        mPlayerPosition = 0;
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            onShown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            onShown();
        }
    }

    private void onShown() {
        try {
            Intent intent = getIntent();
            mContentUri = intent.getData();
            mContentType = intent.getIntExtra(CONTENT_TYPE_EXTRA,
                    inferContentType(mContentUri, intent.getStringExtra(CONTENT_EXT_EXTRA)));
            mContentId = intent.getStringExtra(CONTENT_ID_EXTRA);
            mProvider = intent.getStringExtra(PROVIDER_EXTRA);
            if (mPlayer == null) {
                if (!maybeRequestPermission()) {
                    preparePlayer(true);
                }
            } else {
                mPlayer.setBackgrounded(false);
            }
            mShutterView.setVisibility(View.INVISIBLE);
        } catch (NullPointerException ex) {
            //TODO deal with this if needed
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            onHidden();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            onHidden();
        }
    }

    private void onHidden() {
        if (!mEnableBackgroundAduio) {
            releasePlayer();
        } else {
            mPlayer.setBackgrounded(true);
        }
        mShutterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioCapabilitiesReceiver.unregister();
        releasePlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_root));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );
            mVideoFrame.setLayoutParams(lp);
            mVideoFrame.requestLayout();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
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

    // Surface Life cycle
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing...
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.blockingClearSurface();
        }
    }

    // Internal methods

    private AVPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(this, "AVPlayer");
        switch (mContentType) {
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(this, userAgent, mContentUri.toString());
            default:
                throw new IllegalStateException("Unsupported type:" + mContentType);
        }
    }

    private void preparePlayer(boolean playWhenReady) {
        if (mPlayer == null) {
            mPlayer = new AVPlayer(getRendererBuilder(), this);
            mPlayer.addListener(this);
            mPlayer.setCaptionListener(this);
            mPlayer.setMetadataListener(this);
            mPlayer.seekTo(mPlayerPosition);
            mPlayerNeedsPrepare = true;
            mMediaController.setMediaPlayer(mPlayer.getPlayerControl());
            mMediaController.setEnabled(true);
            mEventLogger = new EventLogger();
            mEventLogger.startSession();
            mPlayer.addListener(mEventLogger);
            mPlayer.setInfoListener(mEventLogger);
            mPlayer.setInternalErrorListener(mEventLogger);
        }
        if (mPlayerNeedsPrepare) {
            mPlayer.prepare();
            mPlayerNeedsPrepare = false;
        }
        mPlayer.setSurface(mSurfaceView.getHolder().getSurface());
        mPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayerPosition = mPlayer.getCurrentPosition();
            mPlayer.release();
            mPlayer = null;
            mEventLogger.endSession();
            mEventLogger = null;
        }
    }

    // AVPlayer.Listener implementation

    @Override public void onStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override public void onError(Exception e) {

    }

    @Override public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                             float pixelWidthHeightRatio) {
        mShutterView.setVisibility(View.GONE);
        mVideoFrame.setAspectRatio(
                height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
    }

    @Override public void onCues(List<Cue> cues) {

    }

    @Override public void onId3Metadata(List<Id3Frame> id3Frames) {

    }

    // AudioCapabilitiesReceiver.Listener methods

    @Override public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        if (mPlayer == null) {
            return;
        }
        boolean backgrounded = mPlayer.getBackgrounded();
        boolean playWhenReady = mPlayer.getPlayWhenReady();
        releasePlayer();
        preparePlayer(playWhenReady);
        mPlayer.setBackgrounded(backgrounded);
    }

    private void toggleControlsVisibility()  {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            showControls();
        }
    }
    private void showControls() {
        mMediaController.show(0);
    }

    // Permission request listener method

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            preparePlayer(true);
        } else {
            Toast.makeText(getApplicationContext(), R.string.storage_permission_denied,
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Permission management methods

    /**
     * Checks whether it is necessary to ask for permission to read storage. If necessary, it also
     * requests permission.
     *
     * @return true if a permission request is made. False if it is not necessary.
     */
    @TargetApi(23)
    private boolean maybeRequestPermission() {
        if (requiresPermission(mContentUri)) {
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(23)
    private boolean requiresPermission(Uri uri) {
        return Util.SDK_INT >= 23
                && Util.isLocalFileUri(uri)
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Makes a best guess to infer the type from a media {@link Uri} and an optional overriding file
     * extension.
     *
     * @param uri The {@link Uri} of the media.
     * @param fileExtension An overriding file extension.
     * @return The inferred type.
     */
    private static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension
                : uri.getLastPathSegment();
        return Util.inferContentType(lastPathSegment);
    }

    private static final class KeyCompatibleMediaController extends AVController {

        private AVController.MediaPlayerControl playerControl;

        public KeyCompatibleMediaController(Context context) {
            super(context);
        }

        @Override
        public void setMediaPlayer(AVController.MediaPlayerControl playerControl) {
            super.setMediaPlayer(playerControl);
            this.playerControl = playerControl;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (playerControl.canSeekForward() && (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() + 15000); // milliseconds
                    show();
                }
                return true;
            } else if (playerControl.canSeekBackward() && (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                    || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() - 5000); // milliseconds
                    show();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_movie;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideSomething();
//        videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
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
//                    TLog.log("first_url"+details.getStandardUrl());
                    getRealURL(details.getFluentUrl());
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
                    if (videoUrl!=null && video!=null && mVideoFrame !=null){
                        videoUrl.setFormatUrl(resp.getBody().getUrl());
                        video.setmPlayUrl(videoUrl);
                        // Reset player and params.
                        releasePlayer();
                        mPlayerPosition = 0;
                        // Set play URL and play it
                        setIntent(onUrlGot());
                        onShown();
                    }
                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }

}
