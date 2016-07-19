package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.vip.VipItemAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoList;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.lt.hm.wovideo.widget.SelectMenuPop;
import com.lt.hm.wovideo.widget.SpaceItemDecoration;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/3
 */
public class NewClassDetailPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter, SwipeRefreshLayout.OnRefreshListener {

    String[] movie_type_names = new String[]{
            "全部", "剧情", "喜剧", "动作", "恐怖", "动画", "武侠", "警匪", "战争", "爱情",
            "科幻", "奇幻", "犯罪", "冒险", "灾难", "伦理", "传记", "家庭", "记录", "惊悚", "历史",
            "悬疑"
    };
    String[] teleplay_type_names = new String[]{
            "全部", "古装", "武侠", "警匪", "战争", "神话", "科幻", "悬疑", "历史", "爱情", "喜剧", "都市", "农村", "奇幻", "其他"
    };
    String[] varity_type_names = new String[]{
            "全部", "情感", "访谈", "真人秀", "选秀", "生活", "时尚", "美食", "纪实", "文化", "曲艺", "演唱会"
    };
    String[] sport_type_names = new String[]{
            "全部", "NBA", "CBA", "中国女篮", "英超", "西甲	", "意甲", "德甲", "欧冠", "中超", "国际足球", "亚冠", "综合", "体育", "网球", "世界杯", "欧洲杯", "奥运会", "竞	速", "格斗", "电子竞技", "排球", "极限", "其他"
    };

//    @BindView(R.id.class_details_topbar)
//    SecondTopbar classDetailsTopbar;

    @BindView(R.id.layout_class_details_head)
    PercentRelativeLayout class_details_head;
    @BindView(R.id.class_details_list)
    RecyclerView classDetailsList;
    @BindView(R.id.details_type_title)
    LinearLayout details_type_title;
    @BindView(R.id.type_container)
    HorizontalScrollView type_container;
    List<VideoList.TypeListBean> b_list;
    VipItemAdapter bottom_adapter;
    int pageNum = 1;
    int pageSize = 20;
    int mId;
    @BindView(R.id.class_refresh_layout)
    SwipeRefreshLayout classRefreshLayout;
    @BindView(R.id.class_details_back)
    ImageView classDetailsBack;
    @BindView(R.id.class_details_spinner)
    Spinner classDetailsSpinner;
    @BindView(R.id.class_details_choose)
    ImageView classDetailsChoose;
    String[] mItems;
    private String lx;
    private String sx;
    private String dq;
    private String nd;
    private boolean first_open = true;
    private boolean spinner_flag = true;
    private boolean search_first = true;
    private boolean shown = false;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_new_class_details;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        mItems = getResources().getStringArray(R.array.video_type);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        classRefreshLayout.setOnRefreshListener(this);
        classRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("key")) {
            String title = bundle.getString("key");
//            classDetailsTopbar.setTvTitle(title);
        }
        if (bundle.containsKey("id")) {
            String id = bundle.getString("id");
            if (!StringUtils.isNullOrEmpty(id)) {
                mId = Integer.parseInt(id);
                classDetailsSpinner.setSelection(mId - 1);
            }
//            classDetailsSpinner.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }

    private void addHeadTypeView() {
        details_type_title.invalidate();
        details_type_title.removeAllViewsInLayout();
        RadioGroup group = new RadioGroup(this);
        group.setOrientation(RadioGroup.HORIZONTAL);
        Map<String, String> values = new LinkedHashMap<>();
        if (mId == VideoType.MOVIE.getId()) {
            for (int i = 0; i < movie_type_names.length; i++) {
                values.put(i + "", movie_type_names[i]);
            }
        } else if (mId == VideoType.TELEPLAY.getId()) {
            for (int i = 0; i < teleplay_type_names.length; i++) {
                values.put(i + "", teleplay_type_names[i]);
            }
        } else if (mId == VideoType.VARIATY.getId()) {
            for (int i = 0; i < varity_type_names.length; i++) {
                values.put(i + "", varity_type_names[i]);
            }
        } else if (mId == VideoType.SPORTS.getId()) {
            for (int i = 0; i < sport_type_names.length; i++) {
                values.put(i + "", sport_type_names[i]);
            }
        }
        if (values.size() > 0) {
            Set<String> keys = values.keySet();
            for (String key_name :
                    keys) {
                RadioButton radiobutton = new RadioButton(this);
                radiobutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.anthology_selector));
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.leftMargin = 20;
                params1.gravity = Gravity.CENTER;
                radiobutton.setLayoutParams(params1);
                radiobutton.setPadding(20, 0, 20, 0);
                radiobutton.setButtonDrawable(null);
                radiobutton.setText(values.get(key_name));
                radiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            SearchChecked("类型", key_name);
                        }
                    }
                });
                group.addView(radiobutton);
                //防止选中两个
                if (radiobutton.getText().toString().equals("全部")) {
                    group.check(radiobutton.getId());
                }
            }
        }
        details_type_title.addView(group);
        details_type_title.invalidate();
    }

    @Override
    public void initViews() {
        b_list = new ArrayList<>();
        classDetailsBack.setOnClickListener((View v) -> {
            this.finish();
        });
        classDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinner_flag) {
                    mId = position + 1;
                    if (b_list != null && b_list.size() > 0) {
                        b_list.clear();
                        getListDatas(mId);
                    }
                } else {
                    spinner_flag = false;
                }
                addHeadTypeView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        classDetailsChoose.setOnClickListener((View v) -> {
            SelectMenuPop pop = new SelectMenuPop(this, mId);
//            pop.showPopupWindow(class_details_head,shown);
            pop.showPopupWindow(type_container, shown);
            pop.setListener(new SelectMenuPop.OnRadioClickListener() {
                @Override
                public void clickListener(String key, String value) {
                    SearchChecked(key, value);
                }
            });
            pop.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        pop.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        });
        addHeadTypeView();

    }

    @Override
    public void initDatas() {
        getListDatas(mId);
    }

    private void getListDatas(int id) {
        if (b_list != null && b_list.size() > 0) {
            b_list.clear();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeid", id);
        map.put("pageNum", pageNum);
        map.put("numPerPage", pageSize);
        map.put("isvip", "0");
        map.put("lx", lx);
        map.put("sx", sx);
        map.put("dq", dq);
        map.put("nd", nd);
        HttpApis.getListByType(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<VideoList, RespHeader> resp = new ResponseObj<VideoList, RespHeader>();
                ResponseParser.parse(resp, response, VideoList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    b_list = resp.getBody().getTypeList();
                    if (b_list == null || b_list.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < b_list.size(); i++) {
                        b_list.get(i).setDesc(b_list.get(i).getIntroduction());
                    }
                    if (b_list != null && b_list.size() > 0) {
                        classDetailsList.setVisibility(View.VISIBLE);
                        bottom_adapter = new VipItemAdapter(getApplicationContext(), b_list);
                        if ((mId) == VideoType.MOVIE.getId()) {
                            GridLayoutManager manager = new GridLayoutManager(NewClassDetailPage.this, 3);
                            manager.setOrientation(GridLayoutManager.VERTICAL);
                            classDetailsList.setLayoutManager(manager);
//                        vipItemList.addItemDecoration(new RecycleViewDivider(getActivity(), GridLayoutManager.VERTICAL));
                            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.divider_width);
                            classDetailsList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
//                            classDetailsList.addItemDecoration(new RecycleViewDivider(NewClassDetailPage.this, GridLayoutManager.VERTICAL));
                            for (int i = 0; i < b_list.size(); i++) {
                                b_list.get(i).setTag("0");
                            }
                        } else {
                            LinearLayoutManager manager = new LinearLayoutManager(NewClassDetailPage.this);
                            manager.setOrientation(LinearLayoutManager.VERTICAL);
                            classDetailsList.setLayoutManager(manager);
                        }
                        classDetailsList.setHasFixedSize(true);
                        classDetailsList.setAdapter(bottom_adapter);
                        bottom_adapter.notifyDataSetChanged();
                        bottom_adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int i) {
                                getVideoDetails(resp.getBody().getTypeList().get(i).getVfinfo_id());
                            }
                        });
                    } else {
                        classDetailsList.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 获取视频详情
     *
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
                        UIHelper.ToMoviePage(NewClassDetailPage.this, bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.TELEPLAY.getId()) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(NewClassDetailPage.this, bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.SPORTS.getId()) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
//                        UIHelper.ToMoviePage(NewClassDetailPage.this, bundle);
                        UIHelper.ToDemandPage(NewClassDetailPage.this, bundle);
                    } else if (resp.getBody().getVfinfo().getTypeId() == VideoType.VARIATY.getId()) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        UIHelper.ToDemandPage(NewClassDetailPage.this, bundle);
                    }
                }
            }
        });
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        SelectMenuPop pop = new SelectMenuPop(this, mId);
//        pop.showPopupWindow(class_details_head,shown);
        pop.showPopupWindow(type_container, shown);
        pop.setListener(new SelectMenuPop.OnRadioClickListener() {
            @Override
            public void clickListener(String key, String value) {
                SearchChecked(key, value);
            }
        });
    }

    private void SearchChecked(String key, String value) {
        if (key.equals("类型")) {
            if (value.equals("0")) {
                lx = "";
            } else {
                lx = value;
            }
        }
        if (key.equals("属性")) {
            if (value.equals("0")) {
                sx = "";
            } else {
                sx = value;
            }
        }
        if (key.equals("地区")) {
            if (value.equals("0")) {
                dq = "";
            } else {
                dq = value;
            }
        }
        if (key.equals("年代")) {
            if (value.equals("0")) {
                nd = "";
            } else {
                nd = value;
            }
        }
        if (b_list != null && b_list.size() > 0) {
            b_list.clear();
        }
        getListDatas(mId);

    }

    @Override
    public void onRefresh() {
        if (b_list != null && b_list.size() > 0) {
            b_list.clear();
        }
        getListDatas(mId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (classRefreshLayout != null && classRefreshLayout.isRefreshing()) {
                    classRefreshLayout.setRefreshing(false);
                }
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        first_open = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        first_open = false;
    }
}
