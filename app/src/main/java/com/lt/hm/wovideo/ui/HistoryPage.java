package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.history.VideoHistoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.db.HistoryDataBase;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.VideoDetails;
import com.lt.hm.wovideo.model.VideoHistory;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.NoDoubleItemClickListener;
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
public class HistoryPage extends BaseActivity implements CustomTopbar.myTopbarClicklistenter {
    @BindView(R.id.history_topbar)
    CustomTopbar historyTopbar;
    @BindView(R.id.history_list)
    ListView history_list;
    @BindView(R.id.select_all)
    Button selectAll;
    @BindView(R.id.delete)
    Button delete;
    int checkNum;//selected count
    HistoryDataBase dataBase;
    @BindView(R.id.history_bottom_container)
    LinearLayout historyBottomContainer;
    private VideoHistoryAdapter adapter;
    private List<VideoHistory> list;
    private boolean top_flag = false;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        historyTopbar.setRightIsVisible(false);
        dataBase = new HistoryDataBase(getApplicationContext());
        historyTopbar.setRightText("编辑");
        historyTopbar.setLeftIsVisible(true);
        historyTopbar.setOnTopbarClickListenter(this);
        list = new ArrayList<>();
        adapter = new VideoHistoryAdapter(list, getApplicationContext());
        history_list.setAdapter(adapter);
    }

    @Override
    public void initViews() {
        selectAll.setOnClickListener((View v) -> {
            // 遍历list的长度，将MyAdapter中的map值全部设为true
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setFlag("true");
            }
            // 数量设为list的长度
            checkNum = list.size();
            // 刷新listview和TextView的显示
            dataChanged();
        });
        delete.setOnClickListener((View v) -> {
            Iterator<VideoHistory> iterator = list.iterator();
            while (iterator.hasNext()) {
                VideoHistory temp = iterator.next();
                if (temp.getFlag().equals("true")) {
                    iterator.remove();
                    dataBase.delete(temp.getmId());
                }
            }


            checkNum = 0;
            // 通知列表数据修改
            dataChanged();
        });

        history_list.setOnItemClickListener(new NoDoubleItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (top_flag){
                    // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                    VideoHistoryAdapter.ViewHolder holder = (VideoHistoryAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.itemCheckBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    // 调整选定条目
                    if (holder.itemCheckBox.isChecked() == true) {
                        list.get(position).setFlag("true");
                        checkNum++;
                    } else {
                        list.get(position).setFlag("false");
                        checkNum--;
                    }
                }else{

//                VideoHistory bean = list.get(position);
//                int typeId = Integer.parseInt(bean.getmId());
                    getVideoDetails(list.get(position));
                }
            }
        });
    }

    @Override
    public void initDatas() {
        list.addAll(dataBase.query(""));
        TLog.log("sql_history"+list.toString());
        if (list != null && list.size() > 0) {
            dataChanged();
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
            historyTopbar.setRightText("取消");
            historyBottomContainer.setVisibility(View.VISIBLE);
            showCheckView("false");
            top_flag = true;
        } else {
            historyTopbar.setRightText("编辑");
            historyBottomContainer.setVisibility(View.GONE);
            showCheckView("");
            top_flag = false;
        }
    }

    private void showCheckView(String tmp) {
        if (StringUtils.isNullOrEmpty(tmp)){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setFlag("");
            }
        }else{
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setFlag("false");
            }
        }

        dataChanged();
    }

    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 获取视频详情数据
     *
     * @param bean
     */
    public void getVideoDetails(VideoHistory bean) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("vfid", bean.getmId());
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
                    getChangePage(resp.getBody().getVfinfo().getId(),bean);
                }
            }
        });
    }

    /**
     *跳转页面
     * @param vfId
     * @param bean
     */
    public void getChangePage(String vfId, VideoHistory bean) {
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
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putLong("cur_position",bean.getCurrent_positon());
                        UIHelper.ToMoviePage(HistoryPage.this, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", vfId);
                        bundle.putLong("cur_position",bean.getCurrent_positon());
                        UIHelper.ToDemandPage(HistoryPage.this, bundle);

                    }
                }
            }
        });
    }


}
