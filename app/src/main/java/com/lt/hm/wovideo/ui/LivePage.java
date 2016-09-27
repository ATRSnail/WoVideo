package com.lt.hm.wovideo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
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
import com.lt.hm.wovideo.adapter.video.LiveTVListAdapter;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.NetUtils;
import com.lt.hm.wovideo.interf.OnMediaOtherListener;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LiveTVList;
import com.lt.hm.wovideo.model.response.ResponseLiveList;
import com.lt.hm.wovideo.model.response.ResponseVideoRealUrl;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.video.sensor.ScreenSwitchUtils;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.PipListviwPopuWindow;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.lt.hm.wovideo.widget.ViewMiddle.ViewMiddle;
import com.victor.loading.rotate.RotateLoading;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.lt.hm.wovideo.base.BaseVideoActivity.CONTENT_ID_EXTRA;
import static com.lt.hm.wovideo.base.BaseVideoActivity.CONTENT_TYPE_EXTRA;
import static com.lt.hm.wovideo.base.BaseVideoActivity.PROVIDER_EXTRA;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class LivePage extends BaseActivity implements SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener, VideoPipAdapter.ItemClickCallBack, OnMediaOtherListener, PopupWindow.OnDismissListener, ViewMiddle.OnSelectListener {
    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_play_number)
    TextView videoPlayNumber;
    @BindView(R.id.video_collect)
    TextView videoCollect;
    @BindView(R.id.video_Pip)
    PercentLinearLayout videoPip;
    @BindView(R.id.video_share)
    TextView videoShare;
    @BindView(R.id.video_projection)
    TextView videoProjection;
    @BindView(R.id.video_like)
    TextView videoLike;
    @BindView(R.id.live_program_list)
    RecyclerView liveProgramList;
    @BindView(R.id.live_video_bottom)
    PercentRelativeLayout live_video_bottom;
    @BindView(R.id.free_hint)
    TextView free_hint;
    @BindView(R.id.free_label)
    TextView mFreeLabel;
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
    private Context mContext;

    // Video thing
    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";
    //画中画urls
    public static final String PIP_URLS = "pip_urls";
    private ScreenSwitchUtils screenSwitchUtils;

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
        }

        View root = findViewById(R.id.video_root);
        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());

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

        mMediaController = new AVController(this,screenSwitchUtils);
        mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        mMediaController.setLiveUi();
        mMediaController.setGestureListener(this);
        mMediaController.setOnMediaOtherListener(this);
        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
        mAudioCapabilitiesReceiver.register();

    }

    private Intent onUrlGot() {
        return new Intent(this, NewMoviePage.class)
                .setData(Uri.parse(video.getmPlayUrl().getFormatUrl()))
                .putExtra(CONTENT_ID_EXTRA, video.getmVideoName())
                .putExtra(CONTENT_TYPE_EXTRA, Util.TYPE_HLS)
                .putExtra(PROVIDER_EXTRA, "");
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
            mMediaController.setLiveUi();
            mVideoFrame.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mVideoFrame.setAspectRatio((float) height / (width + statueHight));
            } else {
                mVideoFrame.requestLayout();
            }
            mVideoFrame.requestLayout();
            ScreenUtils.setSystemUiVisibility(this,true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
            mMediaController.setLiveUi();
            dimissChangeChannelPop();
            ScreenUtils.setSystemUiVisibility(this,false);
        }

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
        mContext = this;
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
        videoCollect.setVisibility(View.GONE);
        videoShare.setVisibility(View.GONE);
        videoProjection.setVisibility(View.GONE);
        videoPlayNumber.setVisibility(View.GONE);
        videoLike.setVisibility(View.GONE);
        videoPip.setVisibility(View.VISIBLE);
        free_hint.setVisibility(View.GONE);
    }


    PipListviwPopuWindow mListPopupWindow;
    private List<LiveModles> liveModlesList;
    private final String[] str = {"央视", "卫士", "地方台", "专业"};

    private List<LiveModles> getPipDatas() {
        liveModlesList = new ArrayList<>();
        LiveModles modles;
        if (cctvList != null) {
            modles = new LiveModles();
            modles.title = str[0];
            modles.setSinatv(cctvList);
            modles.setParentId(0);
            liveModlesList.add(modles);
        }
        if (sinaList != null) {
            modles = new LiveModles();
            modles.title = str[1];
            modles.setSinatv(sinaList);
            modles.setParentId(1);
            liveModlesList.add(modles);
        }


        if (localList != null) {
            modles = new LiveModles();
            modles.title = str[2];
            modles.setSinatv(localList);
            modles.setParentId(2);
            liveModlesList.add(modles);
        }


        if (otherList != null) {
            modles = new LiveModles();
            modles.title = str[3];
            modles.setSinatv(otherList);
            modles.setParentId(3);
            liveModlesList.add(modles);
        }


        return liveModlesList;
    }

    @Override
    public void initViews() {

        liveBtnCctv.setOnClickListener((View v) -> {
            touchProperty(0);
        });

        liveBtnSina.setOnClickListener((View v) -> {
            touchProperty(1);
        });

        liveBtnLocal.setOnClickListener((View v) -> {
            touchProperty(2);
        });

        liveBtnOthertv.setOnClickListener((View v) -> {
            touchProperty(3);
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
    }

    /**
     * 选择频道分类
     *
     * @param index
     */
    private void touchProperty(int index) {
        currentProperty = index;
        initCurrentList(index);
        changeState(btns[currentProperty]);
        if (currentList.size() > 0) {
            initListViews();
        }
    }

    private void initCurrentList(int index) {
        switch (index) {
            case 0:
                currentList = cctvList;
                break;
            case 1:
                currentList = sinaList;
                break;
            case 2:
                currentList = localList;
                break;
            case 3:
                currentList = otherList;
                break;
        }
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
    private int currentProperty;
    private List<LiveModel> currentList;
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


    private void initListViews() {
        if (liveProgramList == null || currentList == null || currentList.size() <= 0) return;

        if (!first_open) {
            currentPlayName = TextUtils.isEmpty(intentPlayUrl) ? currentList.get(0).getTvName() : intentPlayName;
            getRealURL(TextUtils.isEmpty(intentPlayUrl) ? currentList.get(0).getUrl() : intentPlayUrl);
            videoName.setText(currentPlayName);
            if (!TextUtils.isEmpty(intentProperty) && Integer.valueOf(intentProperty) < 4) {
                TLog.error("intentproperty--->" + intentProperty);
                currentProperty = Integer.valueOf(intentProperty);
            } else {
                currentProperty = 0;
            }
            changeState(btns[currentProperty]);
            first_open = true;
        }

        if (adapter == null){
            adapter = new LiveTVListAdapter(getApplicationContext(), currentList);
            liveProgramList.setAdapter(adapter);
        }else {
            adapter.updateShowText(currentList, currentPlayName);
        }

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                String url = currentList.get(i).getUrl();
                resetPlayInfo(currentList, i);
                initVaule();
                getRealURL(url);
            }
        });
        //初始化画中画弹框
        liveModlesList = getPipDatas();
    }

    private void resetPlayInfo(List<LiveModel> liveTvList, int position) {
        currentPlayName = liveTvList.get(position).getTvName();
        videoName.setText(currentPlayName);
        adapter.updateShowText(liveTvList, currentPlayName);
    }

    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url) {
        NetUtils.getVideoRealURL(url, UserMgr.getUsePhone(), this);
    }

    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {
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
                    currentList = cctvList;
                } else if (Integer.valueOf(intentProperty) == 1) {
                    currentList = sinaList;
                } else if (Integer.valueOf(intentProperty) == 2) {
                    currentList = localList;
                } else if (Integer.valueOf(intentProperty) == 3) {
                    currentList = otherList;
                }
                initListViews();
                break;
        }
    }

    @Override
    public void onChooseChannel(View v) {
        if (changeChannelPop == null) {
            initPop();
        }
        mMediaController.hide();
        changeChannelPop.showAtLocation(v, Gravity.RIGHT, 0, -25);
    }

    @Override
    public void onChooseMore(View v) {

    }

    @Override
    public void onShowQuality(View v) {

    }

    @Override
    public void onQualitySelect(String key, String value) {

    }

    @Override
    public void onAnthologyItemClick(int position) {

    }

    private ViewMiddle viewMiddle;
    private PopupWindow changeChannelPop;

    private void initPop() {
        // TODO Auto-generated method stub
        viewMiddle = new ViewMiddle(mContext, liveModlesList);
        viewMiddle.setOnSelectListener(this);
        changeChannelPop = new PopupWindow();
        changeChannelPop.setContentView(viewMiddle);
        changeChannelPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        changeChannelPop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        changeChannelPop.setFocusable(true);
        changeChannelPop.setOnDismissListener(this);
        changeChannelPop.setAnimationStyle(R.style.AnimationRightFade);
        changeChannelPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    /**
     * 动态改变选台popu选项
     */
    private void initVaule() {
        TLog.error("currentProperty--->"+currentProperty+"----"+currentPlayName);
        if (viewMiddle != null)
            viewMiddle.updateShowText(str[currentProperty], currentPlayName);
    }

    @Override
    public void onDismiss() {
      mMediaController.show();
    }

    @Override
    public void getValue(int parentId, int position) {
        initCurrentList(parentId);
        changeState(btns[parentId]);
        currentProperty = parentId;
        resetPlayInfo(currentList, position);
        mMediaController.setTitle(currentPlayName);
        getRealURL(currentList.get(position).getUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (changeChannelPop != null && changeChannelPop.isShowing()) {
                changeChannelPop.dismiss();
            } else {
                if (mMediaController != null) {
                    mMediaController.doBackClick();
                }
            }
        }
        return false;
    }

    private void dimissChangeChannelPop(){
        if (changeChannelPop != null && changeChannelPop.isShowing()) {
            changeChannelPop.dismiss();
        }
    }
}
