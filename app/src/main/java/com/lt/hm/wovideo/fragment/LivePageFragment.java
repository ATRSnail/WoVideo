package com.lt.hm.wovideo.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.video.LiveTVListAdapter;
import com.lt.hm.wovideo.adapter.video.VideoPipAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.LiveModles;
import com.lt.hm.wovideo.model.LiveTVList;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoURL;
import com.lt.hm.wovideo.ui.MoviePage;
import com.lt.hm.wovideo.ui.PipActivity;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;
import com.lt.hm.wovideo.video.player.AVController;
import com.lt.hm.wovideo.video.player.AVPlayer;
import com.lt.hm.wovideo.video.player.EventLogger;
import com.lt.hm.wovideo.video.player.HlsRendererBuilder;
import com.lt.hm.wovideo.widget.PercentLinearLayout;
import com.lt.hm.wovideo.widget.PipListviwPopuWindow;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.victor.loading.rotate.RotateLoading;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
 * Created by xuchunhui on 16/8/15.
 */
public class LivePageFragment extends BaseFragment implements SurfaceHolder.Callback, AVPlayer.Listener, AVPlayer.CaptionListener, AVPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener,VideoPipAdapter.ItemClickCallBack {

    @BindView(R.id.video_name)
    TextView videoName;
    @BindView(R.id.video_play_number)
    TextView videoPlayNumber;
    @BindView(R.id.video_collect)
    PercentLinearLayout videoCollect;
    @BindView(R.id.video_Pip)
    PercentLinearLayout videoPip;
    @BindView(R.id.video_share)
    PercentLinearLayout videoShare;
    @BindView(R.id.video_projection)
    PercentLinearLayout videoProjection;
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
    List<LiveModles.LiveModel> sinaList;
    List<LiveModles.LiveModel> localList;
    List<LiveModles.LiveModel> cctvList;
    List<LiveModles.LiveModel> otherList;
    LiveTVListAdapter adapter;
    VideoModel video = new VideoModel();
    VideoUrl videoUrl = new VideoUrl();

    // Video thing
    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";
    //画中画urls
    public static final String PIP_URLS = "pip_urls";

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

    // Bullet Screen
    private BaseDanmakuParser mParser;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mContext;
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
                        if (drawable == null) {
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
                            if (mDanmakuView != null) {
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

    Unbinder unbinder;
    View view;

    @Override
    public void pipBtnCallBack(ArrayList<String> str) {
        if (str != null && str.size() > 0) {
            mListPopupWindow.dismiss();
            Intent intent = new Intent(getActivity(), PipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PIP_URLS, str);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "请至少选择一个", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private static class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding = 10;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setColor(0x8125309b);
            canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
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

    private void addDanmaku(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = "这是一条弹幕" + System.nanoTime();
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);

    }

    private void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mMediaController.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_video_live;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(view);
            initData();
        }
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mList = new ArrayList<>();
        sinaList = new ArrayList<>();
        localList = new ArrayList<>();
        cctvList = new ArrayList<>();
        otherList = new ArrayList<>();
        btns = new Button[]{liveBtnSina, liveBtnLocal, liveBtnCctv, liveBtnOthertv};
        liveProgramList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        liveProgramList.addItemDecoration(new RecycleViewDivider(getApplicationContext(), LinearLayoutManager.VERTICAL, R.drawable.custom_list_divider));
//        liveProgramList.addItemDecoration(new RecycleViewDivider(LivePage.this,LinearLayoutManager.VERTICAL,getResources().getDimensionPixelOffset(3),R.color.gray_lightest));
//        liveProgramList.addItemDecoration(new SpaceItemDecoration(10));
//        liveProgramList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        hideSomething();

        View root = view.findViewById(R.id.video_root);
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

        mRotateLoading = (RotateLoading) view.findViewById(R.id.loading);
        mShutterView = view.findViewById(R.id.shutter);
        mDanmakuView = (IDanmakuView) view.findViewById(R.id.sv_danmaku);

        mVideoFrame = (AspectRatioFrameLayout) view.findViewById(R.id.video_frame);

        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

        mMediaController = new KeyCompatibleMediaController(getApplicationContext(), mDanmakuView);
        mMediaController.setAnchorView((FrameLayout) view.findViewById(R.id.video_frame));
        mMediaController.setGestureListener(getApplicationContext());
        CookieHandler currentHanlder = CookieHandler.getDefault();
        if (currentHanlder != defaultCookieManager) {
            CookieHandler.setDefault(defaultCookieManager);
        }

        mAudioCapabilitiesReceiver = new AudioCapabilitiesReceiver(getApplicationContext(), this);
        mAudioCapabilitiesReceiver.register();

        // DanmakuView

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
            mDanmakuView.hide();
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

        liveBtnSina.setOnClickListener((View v) -> {
            changeState(btns[0]);
            if (sinaList.size()>0){
                initListViews(sinaList);
            }
        });
        liveBtnLocal.setOnClickListener((View v) -> {
            changeState(btns[1]);
            if (localList.size()>0){
                initListViews(localList);
            }
        });
        liveBtnCctv.setOnClickListener((View v) -> {
            changeState(btns[2]);
            if (cctvList.size()>0){
                initListViews(cctvList);
            }
        });
        liveBtnOthertv.setOnClickListener((View v) -> {
            changeState(btns[3]);
            if (otherList.size()>0){
                initListViews(otherList);
            }
        });
        //显示视频画中画弹框
        videoPip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                if (null!=liveModlesList&&liveModlesList.size()>0) {
                    mListPopupWindow = new PipListviwPopuWindow(getApplicationContext(), liveModlesList, null);
                    mListPopupWindow.showAsDropDown(v);

                }
            }
        });


    }

    private void changeState(Button btn) {
        for (int i = 0; i < btns.length; i++) {
            Button tmp= btns[i];
            if (btn!=tmp){
                tmp.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
                tmp.setTextColor(getResources().getColor(R.color.font_black));
            }else{
                tmp.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
                tmp.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void initData() {
        getLiveList();
    }

    private void getLiveList() {
        HashMap<String, Object> map = new HashMap<>();
        HttpApis.getLiveTvList(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
//                ResponseObj<LiveTVList, RespHeader> resp = new ResponseObj<LiveTVList, RespHeader>();
                ResponseObj<LiveModles, RespHeader> resp = new ResponseObj<LiveModles, RespHeader>();
                ResponseParser.parse(resp, response, LiveModles.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    sinaList.addAll(resp.getBody().getSinatv());
                    localList.addAll(resp.getBody().getLocalTV());
                    cctvList.addAll(resp.getBody().getCctv());
                    otherList.addAll(resp.getBody().getOtherTv());

//                    mList.addAll(resp.getBody().getLiveTvList());
                    initListViews(cctvList);
                }
            }
        });
    }

    private void initListViews(List<LiveModles.LiveModel> liveTvList) {
        adapter = new LiveTVListAdapter(getApplicationContext(), liveTvList);
        liveProgramList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (!first_open){

            getRealURL(liveTvList.get(0).getUrl());
            videoName.setText(liveTvList.get(0).getTvName());

            first_open=true;
        }

//        videoName.setText(liveTvList.get(0).getTvName());

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {


                String url = liveTvList.get(i).getUrl();
                String userinfo= ACache.get(getApplicationContext()).getAsString("userinfo");
                if (!StringUtils.isNullOrEmpty(userinfo)){
                    UserModel model = new Gson().fromJson(userinfo,UserModel.class);
                    String tag = ACache.get(getApplicationContext()).getAsString(model.getId() + "free_tag");
                    if (!StringUtils.isNullOrEmpty(tag)) {
//                        free_hint.setText(" "+"已免流");
                        mFreeLabel.setVisibility(View.VISIBLE);
                    }
                }
                videoName.setText(liveTvList.get(i).getTvName());

//                videoName.setText(liveTvList.get(i).getTvName());
                getRealURL(url);
            }
        });
        //初始化画中画弹框
        liveModlesList  =getPipDatas();

    }

//    private void getRealURL(String url) {
//        HashMap<String, Object> maps = new HashMap<String, Object>();
//        maps.put("videoSourceURL", url);
//        maps.put("cellphone", "18513179404");
//        maps.put("freetag", "1");
//        HttpApis.getVideoRealURL(maps, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                TLog.log("error:" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                TLog.log(response);
//                ResponseObj<VideoURL, RespHeader> resp = new ResponseObj<VideoURL, RespHeader>();
//                ResponseParser.parse(resp, response, VideoURL.class, RespHeader.class);
//                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
//                    videoUrl.setFormatUrl(resp.getBody().getUrl());
//                    video.setmPlayUrl(videoUrl);
//                    // Reset player and params.
//                    releasePlayer();
//                    mPlayerPosition = 0;
//                    // Set play URL and play it
//                    setIntent(onUrlGot());
//                    onShown();
////                    mDanmakuView.show();
//                } else {
//                    TLog.log(resp.getHead().getRspMsg());
//                }
//            }
//        });
//    }



    /**
     * 获取视频播放地址
     *
     * @param url
     */
    private void getRealURL(String url) {
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("videoSourceURL", url);
        String userinfo = SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        if (!StringUtils.isNullOrEmpty(userinfo)){
            UserModel model = new Gson().fromJson(userinfo,UserModel.class);
            maps.put("cellphone", model.getPhoneNo());
            if (model.getIsVip()!=null && model.getIsVip().equals("1")){
                maps.put("freetag", "1");
            }else{
                maps.put("freetag", "0");
            }

        }else{
            maps.put("cellphone", StringUtils.generateOnlyID());
            maps.put("freetag", "0");
        }
//        maps.put("cellphone", "18513179404");
//        maps.put("freetag", "1");
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
                    // Set Player
                    getActivity().setIntent(onUrlGot());
                    onShown();

                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }
            }
        });
    }


    private Intent onUrlGot() {
        Intent mpdIntent = new Intent(getActivity(), MoviePage.class)
                .setData(Uri.parse(video.getmPlayUrl().getFormatUrl()))
                .putExtra(CONTENT_ID_EXTRA, video.getmVideoName())
                .putExtra(CONTENT_TYPE_EXTRA, Util.TYPE_HLS)
                .putExtra(PROVIDER_EXTRA, "");
        return mpdIntent;
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        releasePlayer();
//        mPlayerPosition = 0;
//        getActivity().setIntent(intent);
//    }

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
            Intent intent = getActivity().getIntent();
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
            mMediaController.setAnchorView((FrameLayout) view.findViewById(R.id.video_root));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );
            mMediaController.setTitle(videoName.getText().toString());
            mMediaController.setSwitchVisibility(View.INVISIBLE);
            mVideoFrame.setLayoutParams(lp);
            mVideoFrame.requestLayout();
            //show status bar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //hide status bar
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mMediaController.hide();
            mMediaController.setAnchorView((FrameLayout) view.findViewById(R.id.video_frame));
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
        String userAgent = Util.getUserAgent(getApplicationContext(), "AVPlayer");
        switch (mContentType) {
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(getApplicationContext(), userAgent, mContentUri.toString());
            default:
                throw new IllegalStateException("Unsupported type:" + mContentType);
        }
    }

    private void preparePlayer(boolean playWhenReady) {
        if (mPlayer == null) {
            mPlayer = new AVPlayer(getRendererBuilder(), getApplicationContext());
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
        if (playbackState == AVPlayer.STATE_READY || playbackState == AVPlayer.STATE_ENDED) {
            mRotateLoading.stop();
        } else {
            mRotateLoading.start();
        }
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
    //        getActivity().finish();
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
                && getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
        private IDanmakuView mDanmakuView;

        public KeyCompatibleMediaController(Context context) {
            super(context);
        }

        public KeyCompatibleMediaController(Context context, IDanmakuView danmakuView) {
            super(context);
            mDanmakuView = danmakuView;
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

    private void hideSomething() {
        live_video_bref.setVisibility(View.GONE);
        videoCollect.setVisibility(View.GONE);
        videoShare.setVisibility(View.GONE);
        videoProjection.setVisibility(View.GONE);
        videoPlayNumber.setVisibility(View.GONE);
        videoPip.setVisibility(View.VISIBLE);

    }


    PipListviwPopuWindow mListPopupWindow;
    private List<LiveModles> liveModlesList;
    private final String [] str={"央视","卫士","地方台","专业"};
    private List<LiveModles> getPipDatas(){
        liveModlesList=new ArrayList<>();
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

}
