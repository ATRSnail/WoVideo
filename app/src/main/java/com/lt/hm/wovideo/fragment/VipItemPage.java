package com.lt.hm.wovideo.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.vip.VipItemAdapter;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.VideoList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.lt.hm.wovideo.widget.SpaceItemDecoration;
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
 * @create_date 16/6/6
 */
public class VipItemPage extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	@BindView(R.id.vip_item_list)
	RecyclerView vipItemList;
	Unbinder unbinder;
	@BindView(R.id.refresh_view)
	SwipeRefreshLayout refreshView;
	int pageNum = 1;
	int pageSize = 60;
	List<VideoList.TypeListBean> b_list;
	VipItemAdapter bottom_adapter;
	int mId;
	String isvip;
	boolean first_open = true;
	View view;
	private String lx;
	private String sx;
	private String dq;
	private String nd;
	private int mCurrentCounter, TOTAL_COUNTER;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle.containsKey("id")) {
			String tag = bundle.getString("id");
			mId = Integer.valueOf(tag);
			if (bundle.containsKey("isvip")) {
				isvip = bundle.getString("isvip");
			}
		}
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
		b_list = new ArrayList<>();
//        refreshView.setAutoLoadMore(false);
		refreshView.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
						android.R.color.holo_red_light);
		refreshView.setOnRefreshListener(this);
	}

	@Override
	public void initData() {
		super.initData();
		getListDatas(mId, "1", pageNum);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.layout_vip_item_page, container, false);
			unbinder = ButterKnife.bind(this, view);
			initView(view);
			initData();
		}
		return view;
	}

	private void getListDatas(int id, String tag, int page) {
		first_open = false;
		HashMap<String, Object> map = new HashMap<>();
		map.put("typeid", id);
		map.put("pageNum", page);
		map.put("numPerPage", pageSize);
		map.put("isvip", tag);
		map.put("lx", lx);
		map.put("sx", sx);
		map.put("dq", dq);
		map.put("nd", nd);
		HttpApis.getListByType(map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				TLog.log("error:" + e.getMessage());
			}

			@TargetApi(Build.VERSION_CODES.M)
			@Override
			public void onResponse(String response, int id) {
				TLog.log(response);
				ResponseObj<VideoList, RespHeader> resp = new ResponseObj<VideoList, RespHeader>();
				ResponseParser.parse(resp, response, VideoList.class, RespHeader.class);
				if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
					if (b_list != null && b_list.size() > 0) {
						b_list.clear();
					}
					b_list.addAll(resp.getBody().getTypeList());
					for (int i = 0; i < b_list.size(); i++) {
						b_list.get(i).setDesc(b_list.get(i).getIntroduction());
					}
					if (b_list != null && b_list.size() > 0) {
						bottom_adapter = new VipItemAdapter(getActivity().getApplicationContext(), b_list);
						if ((mId) == VideoType.MOVIE.getId()) {
							GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
							manager.setOrientation(GridLayoutManager.VERTICAL);
							vipItemList.setLayoutManager(manager);
//                        vipItemList.addItemDecoration(new RecycleViewDivider(getActivity(), GridLayoutManager.VERTICAL));
							int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.divider_width);
							vipItemList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
							vipItemList.addItemDecoration(new RecycleViewDivider(getActivity(), GridLayoutManager.HORIZONTAL));
							for (int i = 0; i < b_list.size(); i++) {
								b_list.get(i).setTag("0");
							}
						} else {
							LinearLayoutManager manager = new LinearLayoutManager(getActivity());
							manager.setOrientation(LinearLayoutManager.VERTICAL);
							vipItemList.setLayoutManager(manager);
						}
						TOTAL_COUNTER = b_list.size();
						vipItemList.setHasFixedSize(false);
						vipItemList.setAdapter(bottom_adapter);
						bottom_adapter.notifyDataSetChanged();
//                        bottom_adapter.setOnLoadMoreListener(PAGE_SIZE, new BaseQuickAdapter.RequestLoadMoreListener() {
//                            @Override
//                            public void onLoadMoreRequested() {
//                                if (b_list.size() % 10 != 0) {
//                                    vipItemList.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            bottom_adapter.isNextLoad(false);
//                                        }
//                                    });
//                                } else {
//                                    if (!StringUtils.isNullOrEmpty(mId)) {
//                                        int pageNum = b_list.size() / 10 + 1;
//                                        getListDatas(mId, "1", pageNum);
//                                    }
//                                }
//                            }
//                        });
						bottom_adapter.openLoadAnimation();
//                        bottom_adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//                            @Override
//                            public void onLoadMoreRequested() {
//                                if (!StringUtils.isNullOrEmpty(mId)) {
//                                    if (b_list.size()%10==0){
//                                        int pageNum=b_list.size()/10+1;
//                                        if (StringUtils.isNullOrEmpty(isvip)) {
//                                            getListDatas(mId, "0",pageNum);
//                                        } else {
//                                            getListDatas(mId, "1",pageNum);
//                                        }
//                                    }else {
//                                        if (vipItemList.getScrollState()==RecyclerView.SCROLL_STATE_IDLE)
//                                                 bottom_adapter.isNextLoad(false);
//                                    }
//                                }
//                            }
//                        });
						bottom_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
							@Override
							public void onItemClick(View view, int i) {
								getVideoDetails(resp.getBody().getTypeList().get(i).getVfinfo_id());
							}
						});
					} else {
						bottom_adapter.isNextLoad(false);
					}

				}
			}
		});
	}

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
						bundle.putInt("typeId", VideoType.MOVIE.getId());
						UIHelper.ToMoviePage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
						// TODO: 16/6/14 跳转电视剧页面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId", VideoType.TELEPLAY.getId());

						UIHelper.ToDemandPage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
						// TODO: 16/6/14 跳转 体育播放页面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId", VideoType.SPORTS.getId());
//                        UIHelper.ToMoviePage(getActivity(), bundle);
						UIHelper.ToDemandPage(getActivity(), bundle);
					} else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
						// TODO: 16/6/14 跳转综艺界面
						Bundle bundle = new Bundle();
						bundle.putString("id", vfId);
						bundle.putInt("typeId", VideoType.VARIATY.getId());
						UIHelper.ToDemandPage(getActivity(), bundle);
					}
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		if (!StringUtils.isNullOrEmpty(mId)) {
			pageNum = 1;
			if (b_list.size() > 0) {
				b_list.clear();
			}
			getListDatas(mId, "1", pageNum);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (refreshView != null && refreshView.isRefreshing()) {
					refreshView.setRefreshing(false);
				}
			}
		}, 3000);
	}
}
