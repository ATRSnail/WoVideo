package com.lt.hm.wovideo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.video.LiveTVListAdapter;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LiveTVList;
import com.lt.hm.wovideo.model.response.ResponseLiveList;
import com.lt.hm.wovideo.model.response.ResponseVideoRealUrl;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;
import com.lt.hm.wovideo.widget.LivePlaysPopw;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.PipListviwPopuWindow;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.victor.loading.rotate.RotateLoading;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;

import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_ID_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_TYPE_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.PROVIDER_EXTRA;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class LivePage extends BaseActivity implements SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener, VideoPipAdapter.ItemClickCallBack {
    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_play_number)
    TextView videoPlayNumber;
    @BindView(R.id.video_collect)
    TextView videoCollect;
    @BindView(R.id.video_live)
    TextView liveVideo;
    @BindView(R.id.video_Pip)
    PercentLinearLayout videoPip;
    @BindView(R.id.video_share)
    TextView videoShare;
    @BindView(R.id.video_projection)
    TextView videoProjection;
    @BindView(R.id.video_like)
    TextView videoLike;
    @BindView(R.id.live_bref_img)
    ImageView liveBrefImg;
    @BindView(R.id.live_bref_txt1)
    TextView liveBrefTxt1;
    @BindView(R.id.live_bref_txt2)
    TextView liveBrefTxt2;
    @BindView(R.id.live_expand)
    ImageView liveExpand;
    boolean ex_flag = false;
    @BindView(R.id.live_program_list)
    RecyclerView liveProgramList;
    @BindView(R.id.live_video_bottom)
    PercentRelativeLayout live_video_bottom;
    @BindView(R.id.live_video_bref)
    LinearLayout live_video_bref;
    @BindView(R.id.free_hint)
    TextView free_hint;
    @BindView(R.id.free_label)
    TextView mFreeLabel;
    int newIndex = 0;
    int oldIndex = -1;
    private boolean first_open = false;

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
    List<LiveModel> sinaList;
    List<LiveModel> localList;
    List<LiveModel> cctvList;
    List<LiveModel> otherList;
    LiveTVListAdapter adapter;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();

    // Video thing
    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";
    //画中画urls
    public static final String PIP_URLS = "pip_urls";
    private ScreenSwitchUtils screenSwitchUtils;

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
    private RotateLoading mRotateLoading;
    private SurfaceView mSurfaceView;

    private AVPlayer mPlayer;
    private boolean mPlayerNeedsPrepare;

    private long mPlayerPosition;
    private boolean mEnableBackgroundAduio = false;

    private Uri mContentUri;
    private int mContentType;
    private String mContentId;
    private String mProvider;

    private String img_url;
    private String share_title;
    private String share_desc;
    private AudioCapabilitiesReceiver mAudioCapabilitiesReceiver;

    @Override
    public void pipBtnCallBack(ArrayList<String> str) {
        if (str != null && str.size() > 0) {
            mListPopupWindow.dismiss();
            Intent intent = new Intent(this, PipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PIP_URLS, str);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(this, "请至少选择一个", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statueHight = ScreenUtils.getStatusHeight(getApplicationContext());

            WindowManager manager = this.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            width = outMetrics.widthPixels;
            height = outMetrics.heightPixels;

            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            setSystemUiVisibility();
        }

        View root = findViewById(R.id.video_root);
        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());
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

        mRotateLoading = (RotateLoading) findViewById(R.id.loading);
        mShutterView = findViewById(R.id.shutter);
        mVideoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

        mMediaController = new AVController(this);
        mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        mMediaController.setGestureListener(this);
        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
        mAudioCapabilitiesReceiver.register();

        // DanmakuView

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

    }

    private void setSystemUiVisibility() {
        //hit status bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //       | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        //       | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private Intent onUrlGot() {
        Intent mpdIntent = new Intent(this, NewMoviePage.class)
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
        if (screenSwitchUtils != null) {
            screenSwitchUtils.start(this);
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
        if (screenSwitchUtils != null) {
            screenSwitchUtils.stop();
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

    private int width, height;
    private int statueHight;//5.0以上的状态栏高度

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_root));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );
            mMediaController.setTitle(videoName.getText().toString());
            mMediaController.setSwitchVisibility(View.INVISIBLE);
            mVideoFrame.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mVideoFrame.setAspectRatio((float) height / (width + statueHight));
            } else {
                mVideoFrame.requestLayout();
            }
            mVideoFrame.requestLayout();
            //show status bar
            //         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //hide status bar
            //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        }
        setSystemUiVisibility();
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
            mMediaController.setScreenSwitchUtils(screenSwitchUtils);
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

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == AVPlayer.STATE_READY || playbackState == AVPlayer.STATE_ENDED) {
            mRotateLoading.stop();
        } else {
            mRotateLoading.start();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        mShutterView.setVisibility(View.GONE);
        mVideoFrame.setAspectRatio(
                height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
    }

    @Override
    public void onCues(List<Cue> cues) {

    }

    @Override
    public void onId3Metadata(List<Id3Frame> id3Frames) {

    }

    // AudioCapabilitiesReceiver.Listener methods

    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        if (mPlayer == null) {
            return;
        }
        boolean backgrounded = mPlayer.getBackgrounded();
        boolean playWhenReady = mPlayer.getPlayWhenReady();
        releasePlayer();
        preparePlayer(playWhenReady);
        mPlayer.setBackgrounded(backgrounded);
    }

    private void toggleControlsVisibility() {
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
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
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
     * @param uri           The {@link Uri} of the media.
     * @param fileExtension An overriding file extension.
     * @return The inferred type.
     */
    private static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension
                : uri.getLastPathSegment();
        return Util.inferContentType(lastPathSegment);
    }

    // Old thing
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
        btns = new Button[]{liveBtnCctv, liveBtnSina, liveBtnLocal, liveBtnOthertv};
        liveProgramList.setLayoutManager(new LinearLayoutManager(this));
        liveProgramList.addItemDecoration(new RecycleViewDivider(LivePage.this, LinearLayoutManager.VERTICAL, R.drawable.custom_list_divider));
//        liveProgramList.addItemDecoration(new RecycleViewDivider(LivePage.this,LinearLayoutManager.VERTICAL,getResources().getDimensionPixelOffset(3),R.color.gray_lightest));
//        liveProgramList.addItemDecoration(new SpaceItemDecoration(10));
//        liveProgramList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        hideSomething();

    }

    private void hideSomething() {
        live_video_bref.setVisibility(View.GONE);
        videoCollect.setVisibility(View.GONE);
        videoShare.setVisibility(View.GONE);
        videoProjection.setVisibility(View.GONE);
        videoPlayNumber.setVisibility(View.GONE);
        videoLike.setVisibility(View.GONE);
        videoPip.setVisibility(View.VISIBLE);

    }


    PipListviwPopuWindow mListPopupWindow;
    private List<LiveModles> liveModlesList;
    private final String[] str = {"央视", "卫士", "地方台", "专业"};

    private List<LiveModles> getPipDatas() {
        liveModlesList = new ArrayList<>();
        if (cctvList != null) {
            LiveModles modles = new LiveModles();
            modles.title = str[0];
            modles.setSinatv(cctvList);
            liveModlesList.add(modles);
        }
        if (sinaList != null) {
            LiveModles modles1 = new LiveModles();
            modles1.title = str[1];
            modles1.setSinatv(sinaList);
            liveModlesList.add(modles1);
        }


        if (localList != null) {
            LiveModles modles2 = new LiveModles();
            modles2.title = str[2];
            modles2.setSinatv(localList);
            liveModlesList.add(modles2);
        }


        if (otherList != null) {
            LiveModles modles3 = new LiveModles();
            modles3.title = str[3];
            modles3.setSinatv(otherList);
            liveModlesList.add(modles3);
        }


        return liveModlesList;
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

//        videoShare.setOnClickListener((View v)->{
//            ShareUtils.showShare(this, null, true, share_title,share_desc, HttpUtils.appendUrl(img_url));
//
//        });

        liveBtnCctv.setOnClickListener((View v) -> {
            changeState(btns[0]);
            if (cctvList.size() > 0) {
                initListViews(cctvList);
            }
        });

        liveBtnSina.setOnClickListener((View v) -> {
            changeState(btns[1]);
            if (sinaList.size() > 0) {
                initListViews(sinaList);
            }
        });

        liveBtnLocal.setOnClickListener((View v) -> {
            changeState(btns[2]);
            if (localList.size() > 0) {
                initListViews(localList);
            }
        });

        liveBtnOthertv.setOnClickListener((View v) -> {
            changeState(btns[3]);
            if (otherList.size() > 0) {
                initListViews(otherList);
            }
        });
        //显示视频画中画弹框
        videoPip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != liveModlesList && liveModlesList.size() > 0) {
                    mListPopupWindow = new PipListviwPopuWindow(LivePage.this, liveModlesList, LivePage.this);
                    mListPopupWindow.showAsDropDown(v);

                }
            }
        });

        liveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LivePlaysPopw(LivePage.this).showAsDropDown(v);
            }
        });

    }

    private void changeState(Button btn) {
        for (int i = 0; i < btns.length; i++) {
            Button tmp = btns[i];
            if (btn != tmp) {
                tmp.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
                tmp.setTextColor(getResources().getColor(R.color.font_black));
            } else {
                tmp.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
                tmp.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    private String intentPlayUrl = "";
    private String intentPlayName = "";
    private String intentProperty = "";//电视台属性:0:央视;1:卫视2地方台;3:其他
    private String currentPlayName = "";
    public static final String PLAY_URL = "play_url";
    public static final String PLAY_NAME = "play_name";
    public static final String PLAY_PROPERTY = "property";

    @Override
    public void initDatas() {
        if (getIntent() != null) {
            intentPlayUrl = getIntent().getStringExtra(PLAY_URL);
            intentPlayName = getIntent().getStringExtra(PLAY_NAME);
            intentProperty = getIntent().getStringExtra(PLAY_PROPERTY);
        }
        TLog.error("intentProperty ---->" + intentProperty);
        getLiveList();
    }

    private void getLiveList() {
        NetUtils.getLiveList(this);
    }

    private void initListViews(List<LiveModel> liveTvList) {
        if (liveProgramList == null) return;
        adapter = new LiveTVListAdapter(getApplicationContext(), liveTvList);
        liveProgramList.setAdapter(adapter);

        if (!first_open) {
            currentPlayName = TextUtils.isEmpty(intentPlayUrl) ? liveTvList.get(0).getTvName() : intentPlayName;
            getRealURL(TextUtils.isEmpty(intentPlayUrl) ? liveTvList.get(0).getUrl() : intentPlayUrl);
            videoName.setText(currentPlayName);
            if (!TextUtils.isEmpty(intentProperty) && Integer.valueOf(intentProperty) < 4) {
                TLog.error("intentproperty--->" + intentProperty);
                changeState(btns[Integer.valueOf(intentProperty)]);
            } else {
                changeState(btns[0]);
            }
            first_open = true;
        }
        adapter.updateShowText(liveTvList, currentPlayName);
//        videoName.setText(liveTvList.get(0).getTvName());

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                String url = liveTvList.get(i).getUrl();
                if (UserMgr.isLogin()) {
                    String tag = ACache.get(getApplicationContext()).getAsString(UserMgr.getUseId() + "free_tag");
                    if (!StringUtils.isNullOrEmpty(tag)) {
//                        free_hint.setText(" "+"已免流");
                        mFreeLabel.setVisibility(View.VISIBLE);
                    }
                }
                currentPlayName = liveTvList.get(i).getTvName();
                videoName.setText(currentPlayName);
                adapter.updateShowText(liveTvList, currentPlayName);
//                videoName.setText(liveTvList.get(i).getTvName());
                getRealURL(url);
            }
        });
        //初始化画中画弹框
        liveModlesList = getPipDatas();

    }

    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url) {
        NetUtils.getVideoRealURL(url,UserMgr.getUsePhone(),this);
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag){
            case HttpApis.http_video_real_url:
                ResponseVideoRealUrl va = (ResponseVideoRealUrl) value;
                videoUrl.setFormatUrl(va.getBody().getUrl());
                video.setmPlayUrl(videoUrl);
                // Reset player and params.
                mFreeLabel.setVisibility(UserMgr.isVip() ? View.VISIBLE : View.GONE);
                releasePlayer();
                // Set Player
                setIntent(onUrlGot());
                onShown();
                break;
            case HttpApis.http_video_lives_url:
                ResponseLiveList vaList = (ResponseLiveList) value;
                sinaList.addAll(vaList.getBody().getSinatv());
                localList.addAll(vaList.getBody().getLocalTV());
                cctvList.addAll(vaList.getBody().getCctv());
                otherList.addAll(vaList.getBody().getOtherTv());

                if (TextUtils.isEmpty(intentProperty) || Integer.valueOf(intentProperty) == 0) {
                    initListViews(cctvList);
                } else if (Integer.valueOf(intentProperty) == 1) {
                    initListViews(sinaList);
                } else if (Integer.valueOf(intentProperty) == 2) {
                    initListViews(localList);
                } else if (Integer.valueOf(intentProperty) == 3) {
                    initListViews(otherList);
                }
                break;
        }
    }
}
