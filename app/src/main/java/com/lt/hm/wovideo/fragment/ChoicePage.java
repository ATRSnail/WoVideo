package com.lt.hm.wovideo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.home.Bottom_ListAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.BannerList;
import com.lt.hm.wovideo.model.RecomList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.model.Videos;
import com.lt.hm.wovideo.utils.ScreenUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.utils.imageloader.ImageLoader;
import com.lt.hm.wovideo.utils.imageloader.ImageLoaderUtil;
import com.lt.hm.wovideo.widget.RoundImageView;
import com.lt.hm.wovideo.widget.indicatorView.AutoPlayManager;
import com.lt.hm.wovideo.widget.indicatorView.ImageIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
@Deprecated
public class ChoicePage extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	@BindView(R.id.img_indicator)
	ImageIndicatorView imgIndicator;
	@BindView(R.id.id_viewpager)
	ViewPager idViewpager;
	@BindView(R.id.home_recycler)
	RecyclerView homeRecycler;
	@BindView(R.id.float_button)
	FloatingActionButton floatButton;
	@BindView(R.id.refresh_choice)
	SwipeRefreshLayout refreshLayout;
	@BindView(R.id.h_img_container)
	LinearLayout h_img_container;
	@BindView(R.id.choice_h_scroll)
	HorizontalScrollView choice_h_scroll;
	Unbinder unbinder;
	Bottom_ListAdapter bottom_adapter;
	List<Videos> b_list;
	List<BannerList.Banner> banner_list;
	List<RecomList.Videos> h_list;
	AutoPlayManager autoPlayManager;
	View view;
	private PagerAdapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.layout_choice, container, false);
			unbinder = ButterKnife.bind(this, view);
			initView(view);
			initData();
		}
		return view;
	}

	public void getVideoDetails(String vfId) {
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("vfid", vfId);
		// TODO: 16/6/26 获取app typeId 并填充
		String typeID = null;
		maps.put("typeid", typeID);
//		HttpApis.getVideoInfo(maps, new StringCallback() {
//			@Override
//			public void onError(Call call, Exception e, int id) {
//				TLog.log("error:" + e.getMessage());
//			}
//
//			@Override
//			public void onResponse(String response, int id) {
//				TLog.log(response);
//				ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
//				ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
//				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
//
//					if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
//						// TODO: 16/6/14 跳转电影页面
//						Bundle bundle = new Bundle();
//						bundle.putString("id", vfId);
//						bundle.putInt("typeId", VideoType.MOVIE.getId());
//						UIHelper.ToMoviePage(getActivity(), bundle);
//					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
//						// TODO: 16/6/14 跳转电视剧页面
//						Bundle bundle = new Bundle();
//						bundle.putString("id", vfId);
//						bundle.putInt("typeId", VideoType.TELEPLAY.getId());
//						UIHelper.ToDemandPage(getActivity(), bundle);
//
//					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
//						// TODO: 16/6/14 跳转 体育播放页面
//						Bundle bundle = new Bundle();
//						bundle.putString("id", vfId);
//						bundle.putInt("typeId", VideoType.SPORTS.getId());
//						UIHelper.ToDemandPage(getActivity(), bundle);
//					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
//						// TODO: 16/6/14 跳转综艺界面
//						Bundle bundle = new Bundle();
//						bundle.putString("id", vfId);
//						bundle.putInt("typeId", VideoType.VARIATY.getId());
//						UIHelper.ToDemandPage(getActivity(), bundle);
//					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.LIVE.getId()) {
//						UIHelper.ToLivePage(getActivity());
//					}
//				}
//			}
//		});
	}

	/**
	 * 获取 推荐页面的Banner图
	 */
	private void getBannerDatas() {
		banner_list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("isVip", 0);
		HttpApis.getBanners(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error:" + e.getMessage());

			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log(response);
				ResponseObj<BannerList, RespHeader> resp = new ResponseObj<BannerList, RespHeader>();
				ResponseParser.parse(resp, response, BannerList.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					TLog.log(resp.toString());
					List<BannerList.Banner> mList = resp.getBody().getBannerList();
					banner_list.addAll(mList);
					imgIndicator.setupLayoutByURL(mList);
					imgIndicator.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
					imgIndicator.show();
					loopIndicatorView();
				} else {
					TLog.log(resp.getHead().getRspMsg());
				}

			}
		});
	}

	private void getHotRecommDatas() {
		HashMap<String, Object> maps = new HashMap<>();
		HttpApis.getHotRecomm(maps, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error:" + e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log(response);
				ResponseObj<RecomList, RespHeader> resp = new ResponseObj<RecomList, RespHeader>();
				ResponseParser.parse(resp, response, RecomList.class, RespHeader.class);
				TLog.log(resp.toString());
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					List<RecomList.Videos> mList = resp.getBody().getRecList();
					List<RecomList.Videos> list_viewpager = new ArrayList<RecomList.Videos>();
					h_list.addAll(list_viewpager);
					List<RecomList.Videos> list_recommend = new ArrayList<RecomList.Videos>();
					for (int i = 0; i < mList.size(); i++) {
						if (mList.get(i).getType().equals("0")) {
							list_viewpager.add(mList.get(i));
						} else if (mList.get(i).getType().equals("1")) {
							list_recommend.add(mList.get(i));
						}
					}
//                    initidViewpager(list_viewpager);
					initHorientalScrollView(list_viewpager);
					initRecyclerView(list_recommend);
				}
			}
		});
	}

	private void loopIndicatorView() {
		autoPlayManager = new AutoPlayManager(imgIndicator);
		autoPlayManager.setBroadcastEnable(true);
		autoPlayManager.setBroadcastTimeIntevel(3 * 1000, 5 * 1000);//set first play time and interval
		autoPlayManager.loop();
	}

	private void initHorientalScrollView(List<RecomList.Videos> mList) {
//        h_img_container
		for (int i = 0; i < mList.size(); i++) {
			RoundImageView view = new RoundImageView(getActivity());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getApplicationContext()) * 2 / 5, LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(15, 0, 15, 0);
			view.setBorderRadius(5);
			view.setType(RoundImageView.TYPE_ROUND);
			view.setLayoutParams(params);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				view.setElevation(1000f);
				view.setTranslationZ(200f);
			}
//                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(getActivity()).load(HttpUtils.appendUrl(mList.get(position).getImg())).centerCrop().crossFade().into(view);
			ImageLoaderUtil.getInstance().loadImage(getActivity(), new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(mList.get(i).getImg())).build());
			final int finalPosition = i;
			view.setOnClickListener((View v) -> {
				getVideoDetails(mList.get(finalPosition).getVfId());
			});
			h_img_container.addView(view);
		}
	}

	private void initRecyclerView(List<RecomList.Videos> list_recommend) {
		for (int i = 0; i < list_recommend.size(); i++) {
			list_recommend.get(i).setTag("h");
		}

		bottom_adapter = new Bottom_ListAdapter(getActivity().getApplicationContext(), R.layout.layout_home_item, list_recommend);
		homeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		homeRecycler.setHasFixedSize(false);
		homeRecycler.setAdapter(bottom_adapter);
		bottom_adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
		bottom_adapter.notifyDataSetChanged();
		bottom_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, int i) {
				getVideoDetails(list_recommend.get(i).getVfId());
			}
		});
	}

	/**
	 * 弃用
	 *
	 * @param mList
	 */
	private void initidViewpager(List<RecomList.Videos> mList) {
		idViewpager.setPageMargin(30);
		idViewpager.setOffscreenPageLimit(mList.size());
		idViewpager.setSoundEffectsEnabled(true);
		idViewpager.setAdapter(mAdapter = new PagerAdapter() {
			@Override
			public int getCount() {
				return 50;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
//                ImageView view = new ImageView(getActivity().getApplicationContext());
//                XCRoundRectImageView view = new XCRoundRectImageView(getActivity().getApplicationContext());
				position %= mList.size();
				LinearLayout layout = new LinearLayout(getActivity());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    layout.setBackground(getResources().getDrawable(R.drawable.layout_circle_corner));
//                }
				RoundImageView view = new RoundImageView(getActivity());
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(getApplicationContext()) * 2 / 5, ViewGroup.LayoutParams.MATCH_PARENT);
				view.setBorderRadius(5);
				view.setType(RoundImageView.TYPE_ROUND);
				view.setLayoutParams(params);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					view.setElevation(1000f);
					view.setTranslationZ(200f);
				}
//                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(getActivity()).load(HttpUtils.appendUrl(mList.get(position).getImg())).centerCrop().crossFade().into(view);
//                Glide.with(getActivity()).load(HttpUtils.appendUrl(mList.get(position).getImg())).centerCrop().crossFade().into(view);
				ImageLoaderUtil.getInstance().loadImage(getActivity(), new ImageLoader.Builder().imgView(view).placeHolder(R.drawable.default_vertical).url(HttpUtils.appendUrl(mList.get(position).getImg())).build());

//                GlideUtils.getmInstance().LoadContextCircleBitmap(getActivity(),HttpUtils.appendUrl(mList.get(position).getImg()),view);
				final int finalPosition = position;
				view.setOnClickListener((View v) -> {
					getVideoDetails(mList.get(finalPosition).getVfId());
				});
				layout.addView(view);
				view.setAdjustViewBounds(true);
				container.addView(layout);
				return layout;

			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}

			@Override
			public void finishUpdate(ViewGroup container) {
				int position = idViewpager.getCurrentItem();
				TLog.log("finish update before, position=" + position);
				if (position == 0) {
					position = mList.size();
					idViewpager.setCurrentItem(position, false);
				} else if (position == 10000 - 1) {
					position = mList.size() - 1;
					idViewpager.setCurrentItem(position, false);
				}
				TLog.log("finish update after, position=" + position);
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Parcelable saveState() {
				return super.saveState();
			}

			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {
				super.restoreState(state, loader);
			}
		});
		idViewpager.setCurrentItem(mList.size() / 3);
		idViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
			}
		});
//        idViewpager.setPageTransformer(true, new ScaleInTransformer());
//        mAdapter.notifyDataSetChanged();
	}

	private void enableDisableSwipeRefresh(boolean b) {

		if (refreshLayout != null) {
			refreshLayout.setEnabled(b);
		}
	}

	@Override
	public void onRefresh() {
		initData();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (refreshLayout.isRefreshing()) {
					refreshLayout.setRefreshing(false);
				}
			}
		}, 3000);
	}

	@Override
	public void onResume() {
		super.onResume();
		loopIndicatorView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (unbinder != null) {
			unbinder.unbind();
		}
	}

	@Override
	public void initView(View view) {
		super.initView(view);
//        int width = ScreenUtils.getScreenWidth(getActivity());
//        ViewPager.LayoutParams params = new ViewPager.LayoutParams(width/3, ViewPager.LayoutParams.MATCH_PARENT);
//        idViewpager.setLayoutParams(params);
		h_list = new ArrayList<>();
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setColorSchemeResources(
						android.R.color.holo_blue_bright, android.R.color.holo_green_light,
						android.R.color.holo_orange_light, android.R.color.holo_red_light);

		floatButton.setOnClickListener((View v) -> {
			Toast.makeText(getApplicationContext(), "签到", Toast.LENGTH_SHORT).show();
		});

//        homeSearchImg.setOnClickListener((View v) -> {
//            UIHelper.ToSearchPage(getActivity());
//        });

		imgIndicator.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener() {
			@Override
			public void OnItemClick(View view, int position) {
				BannerList.Banner banner = banner_list.get(position);
				if (banner.getType().equals("0")) {
					getVideoDetails(banner.getOutid());
				} else {
					// TODO: 16/6/26 跳转Web活动页
				}
			}
		});
		imgIndicator.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						refreshLayout.setEnabled(false);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						refreshLayout.setEnabled(true);
						break;
				}
				return false;
			}
		});
		choice_h_scroll.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						refreshLayout.setEnabled(false);
						break;
					case MotionEvent.ACTION_UP:
						refreshLayout.setEnabled(true);
						break;
					case MotionEvent.ACTION_SCROLL:
						refreshLayout.setEnabled(false);
						break;
					case MotionEvent.ACTION_CANCEL:
						refreshLayout.setEnabled(true);
						break;
				}
				return false;
			}
		});


	}

	@Override
	public void initData() {
		super.initData();
		getBannerDatas();
		getHotRecommDatas();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.person_center:
				break;
		}
	}

}


