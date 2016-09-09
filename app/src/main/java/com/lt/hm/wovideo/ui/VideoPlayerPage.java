//package com.lt.hm.wovideo.ui;
//
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.google.gson.Gson;
//import com.lt.hm.wovideo.R;
//import com.lt.hm.wovideo.base.BaseVideoActivity;
//import com.lt.hm.wovideo.http.HttpApis;
//import com.lt.hm.wovideo.http.RespHeader;
//import com.lt.hm.wovideo.http.ResponseCode;
//import com.lt.hm.wovideo.http.ResponseObj;
//import com.lt.hm.wovideo.http.parser.ResponseParser;
//import com.lt.hm.wovideo.model.UserModel;
//import com.lt.hm.wovideo.model.VideoURL;
//import com.lt.hm.wovideo.utils.SharedPrefsUtils;
//import com.lt.hm.wovideo.utils.StringUtils;
//import com.lt.hm.wovideo.utils.TLog;
//import com.lt.hm.wovideo.video.model.VideoModel;
//import com.lt.hm.wovideo.video.model.VideoUrl;
//import com.zhy.http.okhttp.callback.StringCallback;
//
//import java.util.HashMap;
//
//import okhttp3.Call;
//
///**
// * @author leonardo
// * @version 1.0
// * @create_date 8/4/16
// */
//@Deprecated
//public class VideoPlayerPage extends BaseVideoActivity {
//	VideoModel video = new VideoModel();
//	VideoUrl videoUrl = new VideoUrl();
//	private String titleName;
//
////	@BindView(R.id.video_frame)
////	AspectRatioFrameLayout mVideoFrame;
//
//	@Override
//	protected int getLayoutId() {
//		return R.layout.layout_video_player;
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//			mMediaController.hide();
//			mMediaController.setSwitchVisibility(View.INVISIBLE);
//			mMediaController.setBulletVisible(View.INVISIBLE);
//			mMediaController.setmSendBulletVisible(View.INVISIBLE);
//			mMediaController.setmFullscreenVisible(View.INVISIBLE);
//			mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_root));
//			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//							ViewGroup.LayoutParams.MATCH_PARENT,
//							ViewGroup.LayoutParams.MATCH_PARENT,
//							Gravity.CENTER
//			);
//			//TODO set title
////            mMediaController.setTitle(videoName.getText().toString());
//			TLog.log("test_config");
//			mMediaController.setLayoutParams(lp);
////			mVideoFrame.setLayoutParams(lp);
////			mVideoFrame.requestLayout();
//			mMediaController.setBulletScreen(true);
//			if (!StringUtils.isNullOrEmpty(titleName)){
//				setVideoTitle(titleName);
//			}
//		}
//	}
//
//	@Override
//	protected void init(Bundle savedInstanceState) {
//		super.init(savedInstanceState);
//		Bundle bundle= getIntent().getExtras();
//		if (bundle.containsKey("tv_name")){
//			titleName = bundle.getString("tv_name");
//		}
//		if (bundle.containsKey("tv_url")){
//			getRealURL(bundle.getString("tv_url"));
//		}
//	}
//
//	@Override
//	public void initViews() {
//		super.initViews();
//	}
//
//	@Override
//	public void initDatas() {
//		super.initDatas();
//	}
//
//	/**
//	 * 获取视频播放地址
//	 *
//	 * @param url
//	 */
//	private void getRealURL(String url) {
//		HashMap<String, Object> maps = new HashMap<String, Object>();
//		maps.put("videoSourceURL", url);
//		String userinfo = SharedPrefsUtils.getStringPreference(getApplicationContext(), "userinfo");
//		if (!StringUtils.isNullOrEmpty(userinfo)) {
//			UserModel model = new Gson().fromJson(userinfo, UserModel.class);
//			maps.put("cellphone", model.getPhoneNo());
//			if (model.getIsVip() != null && model.getIsVip().equals("1")) {
//				maps.put("freetag", "1");
//			} else {
//				maps.put("freetag", "0");
//			}
//		} else {
//			maps.put("cellphone", StringUtils.generateOnlyID());
//			maps.put("freetag", "0");
//		}
//
//		HttpApis.getVideoRealURL(maps, new StringCallback() {
//			@Override
//			public void onError(Call call, Exception e, int id) {
//				TLog.log("error:" + e.getMessage());
//			}
//
//			@Override
//			public void onResponse(String response, int id) {
//				TLog.log(response);
//				ResponseObj<VideoURL, RespHeader> resp = new ResponseObj<VideoURL, RespHeader>();
//				ResponseParser.parse(resp, response, VideoURL.class, RespHeader.class);
//				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
//					if (videoUrl != null && video != null) {
//						videoUrl.setFormatUrl(resp.getBody().getUrl());
//						video.setmPlayUrl(videoUrl);
//						// Reset player and params.
//						releasePlayer();
//						// Set play URL and play it
//						setIntent(onUrlGot(video));
//						onShown();
//
//
//						mMediaController.hide();
//						mMediaController.setAnchorView((FrameLayout) findViewById(R.id.video_root));
//						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//										ViewGroup.LayoutParams.MATCH_PARENT,
//										ViewGroup.LayoutParams.MATCH_PARENT,
//										Gravity.CENTER
//						);
//						mMediaController.setLayoutParams(lp);
//						mMediaController.setSwitchVisibility(View.INVISIBLE);
//						mMediaController.setBulletVisible(View.INVISIBLE);
//						mMediaController.setmSendBulletVisible(View.INVISIBLE);
//						mMediaController.setmFullscreenVisible(View.INVISIBLE);
//						if (!StringUtils.isNullOrEmpty(titleName)){
//							setVideoTitle(titleName);
//						}
//					}
//				} else {
//					TLog.log(resp.getHead().getRspMsg());
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//	}
//}
