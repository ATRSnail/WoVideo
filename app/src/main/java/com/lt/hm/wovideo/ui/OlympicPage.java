package com.lt.hm.wovideo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.vip.VipItemAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.DialogHelp;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.IPhoneDialog;
import com.lt.hm.wovideo.widget.VerticalSwipeRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/4/16
 */
public class OlympicPage extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
	private static final String ORDERED_STATUS = "1";
	Unbinder unbinder;
	@BindView(R.id.order_five_flow)
	ImageButton orderFiveFlow;
	@BindView(R.id.olympic_list)
	RecyclerView olympicList;
	@BindView(R.id.olympic_swipe)
	VerticalSwipeRefreshLayout olympicSwipe;
	@BindView(R.id.oly_cctv1)
	ImageButton olyCctv1;
	@BindView(R.id.oly_cctv2)
	ImageButton olyCctv2;
	@BindView(R.id.oly_cctv5)
	ImageButton olyCctv5;
	@BindView(R.id.oly_cctv5_plus)
	ImageButton olyCctv5Plus;
	VipItemAdapter oly_adapter;
	private int pageNum = 1;
	private int pageSize = 30;
	private List<VideoList.TypeListBean> oly_list;
	private int currentCount;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.layout_olympic, container, false);
		unbinder = ButterKnife.bind(this, view);
		oly_list = new ArrayList<>();

		initView(view);
		initData();
		return view;
	}

	private void checkStatus() {
		String userinfo = SharedPrefsUtils.getStringPreference(getApplicationContext(), "userinfo");
		if (!StringUtils.isNullOrEmpty(userinfo)) {
			UserModel model = new Gson().fromJson(userinfo, UserModel.class);
			HashMap<String, Object> maps = new HashMap<>();
			maps.put("userid", model.getId());
			HttpApis.getUsers(maps, new StringCallback() {
				@Override
				public void onError(Call call, Exception e, int id) {
					TLog.log("error:" + e.getMessage());
				}

				@Override
				public void onResponse(String response, int id) {
					TLog.log("userinfo_update" + response);
					ResponseObj<UserModel, RespHeader> resp = new ResponseObj<UserModel, RespHeader>();
					ResponseParser.loginParse(resp, response, UserModel.class, RespHeader.class);
					if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
						UserModel model = resp.getBody();
						String json = new Gson().toJson(model);
						cacheUserInfo(json);
						if (model.getAy() != null && model.getAy().equals(ORDERED_STATUS)) {
							orderFiveFlow.setImageDrawable(getResources().getDrawable(R.drawable.icon_o_cancel_order));
						} else {
							orderFiveFlow.setImageDrawable(getResources().getDrawable(R.drawable.icon_o_order));
						}
					} else {
						Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			UnLoginHandler.unLogin(getActivity());
		}
	}

	private void getOlyList() {
		// TODO: 8/4/16  获取 Olympic List  数据
		HashMap<String, Object> map = new HashMap<>();
		map.put("typeid", 4);
		map.put("pageNum", pageNum);
		map.put("numPerPage", pageSize);
		map.put("isvip", "0");
		map.put("lx", "16");
		HttpApis.getListByType(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error"+e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("olympic_list" + response);
				ResponseObj<VideoList, RespHeader> resp = new ResponseObj<VideoList, RespHeader>();
				ResponseParser.parse(resp, response, VideoList.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					if (resp.getBody().getTypeList().size() > 0) {
						oly_list.addAll(resp.getBody().getTypeList());
						oly_adapter = new VipItemAdapter(getApplicationContext(), oly_list);
						olympicList.setAdapter(oly_adapter);
						olympicList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
						oly_adapter.openLoadAnimation();
//		oly_adapter.setOnLoadMoreListener(this);
						oly_adapter.openLoadMore(pageSize, true);
//						oly_adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//							@Override
//							public void onLoadMoreRequested() {
//								olympicList.post(new Runnable() {
//									@Override
//									public void run() {
////						if (currentCount >= TOTAL_COUNTER) {
////							oly_adapter.notifyDataSetChanged();
////					} else {
////						oly_adapter.notifyDataSetChanged();
//										oly_adapter.notifyDataChangedAfterLoadMore(oly_list,true);
//										currentCount = oly_adapter.getItemCount();
////					}
//									}
//								});
//							}
//						});
						oly_adapter.setOnLoadMoreListener(OlympicPage.this);
						oly_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
							@Override
							public void onItemClick(View view, int i) {
								getVideoDetails(resp.getBody().getTypeList().get(i).getVfinfo_id());
							}
						});
					} else {
						// TODO: 8/4/16 empty view show 
					}
				} else {
					// TODO: 8/4/16 getdata error do something
				}
			}
		});
	}

	/**
	 * 跳转 页面
	 * @param vfId
	 */
	public void getVideoDetails(String vfId) {
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("vfid", vfId);
		HttpApis.getVideoInfo(maps, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error:" + e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("result:::" + response);
				ResponseObj<VideoDetails, RespHeader> resp = new ResponseObj<VideoDetails, RespHeader>();
				ResponseParser.parse(resp, response, VideoDetails.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {

					if (resp.getBody().getVfinfo().getTypeId() == VideoType.MOVIE.getId()) {
						// TODO: 16/6/14 跳转电影页面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId",VideoType.MOVIE.getId());
						UIHelper.ToMoviePage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
						// TODO: 16/6/14 跳转电视剧页面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId",VideoType.TELEPLAY.getId());

						UIHelper.ToDemandPage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
						// TODO: 16/6/14 跳转 体育播放页面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId",VideoType.SPORTS.getId());
						UIHelper.ToDemandPage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
						// TODO: 16/6/14 跳转综艺界面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId",VideoType.VARIATY.getId());
						UIHelper.ToDemandPage(getActivity(), bundle);
					}
				}
			}
		});
	}


	private void cacheUserInfo(String json) {
		SharedPrefsUtils.setStringPreference(getApplicationContext(), "userinfo", json);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (unbinder != null) {
			unbinder.unbind();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_olympic;
	}

	@Override
	public void initView(View view) {
		super.initView(view);
		olympicSwipe.setOnRefreshListener(this);
		olympicSwipe.setColorSchemeResources(
						android.R.color.holo_blue_bright, android.R.color.holo_green_light,
						android.R.color.holo_orange_light, android.R.color.holo_red_light);
		olyCctv1.setOnClickListener(this);
		olyCctv2.setOnClickListener(this);
		olyCctv5.setOnClickListener(this);
		olyCctv5Plus.setOnClickListener(this);




	}

	@Override
	public void initData() {
		super.initData();
		checkStatus();
		getOlyList();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Bundle bundle= new Bundle();
		Intent intent = new Intent(getApplicationContext(),VideoPlayerPage.class);
		switch (v.getId()) {
			case R.id.oly_cctv1:
				// TODO: 8/4/16 change the page live horiental page cctv1_url
				//http://111.206.133.39:9910/live/live_cctv1/index.m3u8
				bundle.putString("tv_name","CCTV1");
				bundle.putString("tv_url","http://111.206.133.39:9910/live/live_cctv1/index.m3u8");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.oly_cctv2:
				// TODO: 8/4/16 change the page live horiental page cctv2_url
				//http://111.206.133.39:9910/live/live_cctv2/index.m3u8
				bundle.putString("tv_name","CCTV2");
				bundle.putString("tv_url","http://111.206.133.39:9910/live/live_cctv2/index.m3u8");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.oly_cctv5:
				// TODO: 8/4/16 change the page live horiental page cctv5_url
//				http://111.206.133.39:9910/live/live_cctv5/index.m3u8
				bundle.putString("tv_name","CCTV5");
				bundle.putString("tv_url","http://111.206.133.39:9910/live/live_cctv5/index.m3u8");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.oly_cctv5_plus:
				// TODO: 8/4/16 change the page live horiental page cctv5_plus_url
//				http://111.206.133.39:9910/live/live_cctv5p/index.m3u8
				bundle.putString("tv_name","CCTV5+");
				bundle.putString("tv_url","http://111.206.133.39:9910/live/live_cctv5p/index.m3u8");
				intent.putExtras(bundle);
				startActivity(intent);
				break;

		}
	}

	@OnClick(R.id.order_five_flow)
	void orderFlow(View view) {
		String userinfo = SharedPrefsUtils.getStringPreference(getApplicationContext(), "userinfo");
		if (!StringUtils.isNullOrEmpty(userinfo)) {
			UserModel model = new Gson().fromJson(userinfo, UserModel.class);
			if (model.getAy()!=null && model.getAy().equals("1")) {
				IPhoneDialog dialog = DialogHelp.showDialog(getActivity(), "退订沃为你加油5元包月产品,下月初生效", "取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}, "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CancelOrder(model, getApplicationContext());
						dialog.dismiss();
					}
				});
				dialog.show();
			} else {
				IPhoneDialog dialog = DialogHelp.showDialog(getActivity(), "购买沃为你加油5元包月产品所产生的费用将从您的话费中扣除,并且下月自动续费", "取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}, "确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						purchOrder(model, getApplicationContext());
						dialog.dismiss();
					}
				});
				dialog.show();
			}

		} else {
			UnLoginHandler.unLogin(getActivity());
		}
	}

	private void CancelOrder(UserModel model, Context mContext) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("cellphone", model.getPhoneNo());
		map.put("spid", "954");
		HttpApis.cancleOrder(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log(e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("cancel_olympic_order" + response);
				ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
				ResponseParser.parse(resp, response, String.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					CancelHMOrder(model);
				} else {

				}
			}
		});
	}

	/**
	 * 支付
	 *
	 * @param model
	 */
	private void purchOrder(UserModel model, Context mContext) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("cellphone", model.getPhoneNo());
		map.put("spid", "954");
		map.put("ordertype", 0);
		HttpApis.purchOrder(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error:" + e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
				ResponseParser.parse(resp, response, String.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					finishOrder(model, mContext);
				} else {
					Toast.makeText(mContext, resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void CancelHMOrder(UserModel model) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userid", model.getId());
		HttpApis.receiptCancelOrder(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log(e.getMessage());
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("cancel_olympic_order_hm", response);
				ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
				ResponseParser.parse(resp, response, String.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					Toast.makeText(getActivity(), "退订成功", Toast.LENGTH_SHORT).show();
					orderFiveFlow.setImageDrawable(getResources().getDrawable(R.drawable.icon_o_order));
					checkStatus();
				} else {
					Toast.makeText(getActivity(), "退订失败", Toast.LENGTH_SHORT).show();
					orderFiveFlow.setImageDrawable(getResources().getDrawable(R.drawable.icon_o_cancel_order));
					checkStatus();
				}
			}
		});
	}

	private void finishOrder(UserModel model, Context mContext) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userid", model.getId());
		HttpApis.receiptOrder(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log(e.getMessage());
				Toast.makeText(mContext, "订购失败，请稍后再试", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String response, int id) {
				TLog.log("olympic_finish_order" + response);
				ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
				ResponseParser.parse(resp, response, String.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					Toast.makeText(mContext, "订购成功", Toast.LENGTH_SHORT).show();
					orderFiveFlow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_o_cancel_order));
					model.setAy("1");
					SharedPrefsUtils.setStringPreference(getApplicationContext(),"userinfo",new Gson().toJson(model));
				} else {
					model.setAy("0");
					SharedPrefsUtils.setStringPreference(getApplicationContext(),"userinfo",new Gson().toJson(model));
					Toast.makeText(mContext, "订购失败", Toast.LENGTH_SHORT).show();
					orderFiveFlow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_o_order));
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				olympicSwipe.setRefreshing(false);
			}
		}, 3000);
	}

	@Override
	public void onLoadMoreRequested() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				++pageNum;

			}
		},3000);
	}
}
