package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.adapter.history.CollectListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.CollectModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UIHelper;
import com.lt.hm.wovideo.widget.CustomTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class CollectPage extends BaseActivity implements CustomTopbar.myTopbarClicklistenter, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.collect_topbar)
    CustomTopbar collectTopbar;
    @BindView(R.id.collect_list)
    ListView collectList;
    @BindView(R.id.collect_refresh)
    SwipeRefreshLayout collectRefresh;
    //    List<CollectModel> mList;
    List<CollectModel.CollListBean> mList;
    int pageNum = 1;
    int pageSize = 100;
    @BindView(R.id.select_all)
    Button selectAll;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.history_bottom_container)
    LinearLayout historyBottomContainer;
    private CollectListAdapter adapter;
    private boolean top_flag = false;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_collect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        adapter = new CollectListAdapter(mList, getApplicationContext());
        collectList.setAdapter(adapter);
        collectTopbar.setLeftIsVisible(true);
        collectTopbar.setRightIsVisible(true);
        collectTopbar.setRightText("编辑");
        collectRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    @Override
    public void initViews() {
        collectTopbar.setOnTopbarClickListenter(this);
        collectRefresh.setOnRefreshListener(this);
        selectAll.setOnClickListener((View v) -> {
            // 遍历list的长度，将MyAdapter中的map值全部设为true
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("true");
            }
            // 数量设为list的长度
//            checkNum = list.size();
            // 刷新listview和TextView的显示
            dataChanged();
        });
        delete.setOnClickListener((View v) -> {
            Iterator<CollectModel.CollListBean> iterator = mList.iterator();
            List<CollectModel.CollListBean> cancelList = new ArrayList<CollectModel.CollListBean>();
            while (iterator.hasNext()) {
                CollectModel.CollListBean temp = iterator.next();
                if (temp.getFlag().equals("true")) {
                    iterator.remove();
                    cancelList.add(temp);
                }
            }
            cancelCollect(cancelList);

//            checkNum = 0;
            // 通知列表数据修改
            dataChanged();
        });

        collectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                if (top_flag){
                    CollectListAdapter.ViewHolder holder = (CollectListAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.itemCheckBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    // 调整选定条目
                    if (holder.itemCheckBox.isChecked() == true) {
                        mList.get(position).setFlag("true");
//            checkNum++;
                    } else {
                        mList.get(position).setFlag("false");
//            checkNum--;
                    }
                }else{
                    // TODO: 16/7/21 跳转页面
                    CollectModel.CollListBean bean = mList.get(position);
                    int typeId = Integer.parseInt(bean.getTypeid());
//                    if (VideoType.MOVIE.getId()== typeId){
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", bean.getIid());
//                        UIHelper.ToMoviePage(CollectPage.this, bundle);
//                    }else{
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", bean.getIid());
//                        UIHelper.ToDemandPage(CollectPage.this, bundle);
//                    }
                    if (VideoType.MOVIE.getId()== typeId) {
                        // TODO: 16/6/14 跳转电影页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getIid());
                        bundle.putInt("typeId",VideoType.MOVIE.getId());
                        UIHelper.ToMoviePage(CollectPage.this, bundle);
                    } else if (VideoType.TELEPLAY.getId()== typeId) {
                        // TODO: 16/6/14 跳转电视剧页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getIid());
                        bundle.putInt("typeId",VideoType.TELEPLAY.getId());
      //                  UIHelper.ToDemandPage(CollectPage.this, bundle);

                    } else if (VideoType.SPORTS.getId()== typeId) {
                        // TODO: 16/6/14 跳转 体育播放页面
                        Bundle bundle = new Bundle();
                        bundle.putString("id",  bean.getIid());
                        bundle.putInt("typeId",VideoType.TELEPLAY.getId());
       //                 UIHelper.ToDemandPage(CollectPage.this, bundle);
                    } else if (VideoType.VARIATY.getId()== typeId) {
                        // TODO: 16/6/14 跳转综艺界面
                        Bundle bundle = new Bundle();
                        bundle.putString("id", bean.getIid());
                        bundle.putInt("typeId",VideoType.VARIATY.getId());
        //                UIHelper.ToDemandPage(CollectPage.this, bundle);
                    }


                }


            }
        });

    }

    private void cancelCollect(List<CollectModel.CollListBean> cancelList) {
        StringBuffer sb = new StringBuffer();
        if (cancelList.size() > 0) {
            for (CollectModel.CollListBean bean :
                    cancelList) {
                sb.append(bean.getPid()).append(",");
            }
            String userinfo = ACache.get(getApplicationContext()).getAsString("userinfo");
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userid", model.getId());
            map.put("vfids", sb.toString());
            HttpApis.cancelCollect(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log("cancel_result" + response);
                    ResponseObj<String,RespHeader> resp = new ResponseObj<String, RespHeader>();
                    ResponseParser.parse(resp,response,String.class,RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
//                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.cancel_collect_success),Toast.LENGTH_SHORT).show();
                    }else{
//                        img_collect.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect_press));
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.cancel_collect_success),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            TLog.log("no data to cancel collect");
        }


    }

    @Override
    public void initDatas() {
        getCollectList();
    }

    private void getCollectList() {
        String userinfo = SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        TLog.log("collectInfo"+userinfo);
        if (!StringUtils.isNullOrEmpty(userinfo)){
            UserModel model = new Gson().fromJson(userinfo, UserModel.class);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userid", model.getId());
            map.put("pageNum", pageNum);
            map.put("numPerPage", pageSize);
            HttpApis.collectList(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log(e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log("collect" + response);
                    ResponseObj<CollectModel, RespHeader> resp = new ResponseObj<CollectModel, RespHeader>();
                    ResponseParser.parse(resp, response, CollectModel.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        List<CollectModel.CollListBean> models = resp.getBody().getCollList();
                        mList.addAll(models);
                        dataChanged();
                    } else {
                        TLog.log(resp.getHead().getRspMsg());
                    }
                }
            });
        }

    }

    private void dataChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        //顶部文字切换
        if (!top_flag) {
            collectTopbar.setRightText("取消");
            historyBottomContainer.setVisibility(View.VISIBLE);
            showCheckView("false");
            top_flag = true;
        } else {
            collectTopbar.setRightText("编辑");
            historyBottomContainer.setVisibility(View.GONE);
            showCheckView("");
            top_flag = false;
        }

    }

    private void showCheckView(String tmp) {
        if (StringUtils.isNullOrEmpty(tmp)) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("");
            }
        } else {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setFlag("false");
            }
        }
        dataChanged();
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (collectRefresh==null)return;
                collectRefresh.setRefreshing(false);
            }
        }, 3000);
    }

}
