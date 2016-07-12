package com.lt.hm.wovideo.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.comment.CommentAdapter;
import com.lt.hm.wovideo.adapter.video.VideoAnthologyAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemGridAdapter;
import com.lt.hm.wovideo.adapter.video.VideoItemListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
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
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.danmaku.util.IOUtils;
import okhttp3.Call;

import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_ID_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.CONTENT_TYPE_EXTRA;
import static com.lt.hm.wovideo.video.NewVideoPage.PROVIDER_EXTRA;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/7
 */
public class DemandPage extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener {

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

    private EventLogger mEventLogger;
    private AVController mMediaController;
    private AspectRatioFrameLayout mVideoFrame;
    private View mShutterView;
    private SurfaceView mSurfaceView;

    private AVPlayer mPlayer;
    private boolean mPlayerNeedsPrepare;
    private String img_url;
    private String share_title;
    private String share_desc;
    private long mPlayerPosition;
    private boolean mEnableBackgroundAduio = false;

    private Uri mContentUri;
    private int mContentType;
    private String mContentId;
    private String mProvider;

    private AudioCapabilitiesReceiver mAudioCapabilitiesReceiver;
    private BaseDanmakuParser mParser;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        private Drawable mDrawable;

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if(drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable);
                            danmaku.text = spannable;
                            if(mDanmakuView != null) {
                                mDanmakuView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
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
        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);

        mVideoFrame = (AspectRatioFrameLayout) findViewById(R.id.video_frame);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

//        mMediaController = new KeyCompatibleMediaController(this);
        mMediaController = new KeyCompatibleMediaController(this, mDanmakuView);

        mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_frame));
        mMediaController.setGestureListener(this);

        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
        mAudioCapabilitiesReceiver.register();


        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
//            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
//                @Override
//                public void onDanmakuClick(BaseDanmaku latest) {
//                    Log.d("DFM", "onDanmakuClick text:" + latest.text);
//                }
//
//                @Override
//                public void onDanmakuClick(IDanmakus danmakus) {
//                    Log.d("DFM", "onDanmakuClick danmakus size:" + danmakus.size());
//                }
//            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
//            ((View) mDanmakuView).setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    mMediaController.setVisibility(View.VISIBLE);
//                }
//            });
            ((View) mDanmakuView).setOnTouchListener(new View.OnTouchListener() {
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
        }


    }


    private BaseDanmakuParser createParser(InputStream stream) {
        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

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
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
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
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            onHidden();
        }
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
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
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
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
            mMediaController.setTitle(videoName.getText().toString());

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

    // Internal methods

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.blockingClearSurface();
        }
    }

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

    // AVPlayer.Listener implementation

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayerPosition = mPlayer.getCurrentPosition();
            mPlayer.release();
            mPlayer = null;
            mEventLogger.endSession();
            mEventLogger = null;
        }
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {

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

    // AudioCapabilitiesReceiver.Listener methods

    @Override
    public void onId3Metadata(List<Id3Frame> id3Frames) {

    }

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

    // Permission request listener method

    private void showControls() {
        mMediaController.show(0);
    }

    // Permission management methods

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
                PlayList.PlaysListBean bean = antholys.get(i);
                vfId = bean.getVfId();
                per_Id = bean.getId();



                getRealURL(bean.getFluentUrl(), false);
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
                            free_hint.setText(" "+"已免流");
                        }
                    }
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

                    mMediaController.setVideoModel(model);
                    mMediaController.setListener(new AVController.OnQualitySelected() {
                        @Override
                        public void onQualitySelect(String key, String value) {
                            getRealURL(value, true);
                        }
                    });
                    if (model.getmVideoUrl().size()>0){
                        getRealURL(model.getmVideoUrl().get(0).getFormatUrl(), false);
                        mMediaController.setmQualitySwitch(model.getmVideoUrl().get(0).getFormatName());
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
     */
    private void getRealURL(String url, boolean isQualitySwitch) {
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
                    setIntent(onUrlGot());
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

    private static final class KeyCompatibleMediaController extends AVController {

        private MediaPlayerControl playerControl;
        private IDanmakuView mDanmakuView;

        public KeyCompatibleMediaController(Context context,IDanmakuView danmakuView) {
            super(context);
            mDanmakuView = danmakuView;

        }

        @Override
        public void setMediaPlayer(MediaPlayerControl playerControl) {
            super.setMediaPlayer(playerControl);
            this.playerControl = playerControl;
        }
        @Override
        public void toggleBulletScreen(boolean isShow) {
            super.toggleBulletScreen(isShow);
            if (isShow) {
                mDanmakuView.show();
            } else {
                mDanmakuView.hide();
            }
        }

        @Override
        public void show() {
            super.show();
        }

        @Override
        public void setTitle(String titleName) {
            super.setTitle(titleName);
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

}
