package com.lt.hm.wovideo.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.category.CategoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.model.Category;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseChannel;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UserMgr;
import com.lt.hm.wovideo.widget.CustomTopbar;
import com.lt.hm.wovideo.widget.DividerDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class PersonalitySet extends BaseActivity implements CustomTopbar.myTopbarClicklistenter, OnCateItemListener {

    private static final int TOTAL_LINE = 5;
    public static final int RESULT_PERSONALITY = 34;

    @BindView(R.id.person_topbar)
    CustomTopbar personTopbar;
    @BindView(R.id.recycler_personal_mine)
    RecyclerView mineCateRecycler;
    @BindView(R.id.recycler_personal_all)
    RecyclerView allCateRecycler;
    @BindView(R.id.text_change)
    TextView changeText;
    @BindView(R.id.text_unchange)
    TextView unchangeText;

    private CategoryAdapter adapter;
    private CategoryAdapter allAdapter;
    private List<ChannelModel> allList = new ArrayList<>();


    private List<ChannelModel> channels = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personality_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        personTopbar.setLeftIsVisible(true);
        personTopbar.setRightIsVisible(true);
        personTopbar.setRightText("编辑");
        personTopbar.setOnTopbarClickListenter(this);
        changeText.setText("我的频道");
        unchangeText.setText("频道栏目");
    }

    @Override
    public void initViews() {

    }

    private void initCateView() {
        adapter = new CategoryAdapter(this, channels, this, Category.FIRST_TYPE);
        allAdapter = new CategoryAdapter(this, allList, this, Category.SECOND_TYPE);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, TOTAL_LINE);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, TOTAL_LINE);
        DividerDecoration decoration = new DividerDecoration(getApplicationContext());
        mineCateRecycler.addItemDecoration(decoration);
        allCateRecycler.addItemDecoration(decoration);
        mineCateRecycler.setLayoutManager(mLayoutManager);
        allCateRecycler.setLayoutManager(mLayoutManager2);
        mineCateRecycler.setAdapter(adapter);
        allCateRecycler.setAdapter(allAdapter);
    }

    @Override
    public void initDatas() {

        getClassInfos();
    }

    /**
     * 从网络获取标签
     */
    private void getClassInfos() {
        HashMap<String, Object> map = new HashMap<>();
        UserModel userModel = UserMgr.getUseInfo(getApplicationContext());
        if (userModel != null)
            map.put("userid", userModel.getId());
        HttpApis.getIndividuationChannel(map, HttpApis.http_one, new HttpCallback<>(ResponseChannel.class, this));
    }

    /**
     * 保存个性化频道
     */
    private void updateChannel(String channel) {
        TLog.error("channel--->" + channel);
        HashMap<String, Object> map = new HashMap<>();
        UserModel userModel = UserMgr.getUseInfo(getApplicationContext());
        if (userModel != null)
            map.put("userid", userModel.getId());
        map.put("channel", channel);
        HttpApis.updateChannel(map, HttpApis.http_two, new HttpCallback<>(String.class, this));
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        if (adapter.isCanDel) {

            updateChannel(splitStr(channels));
        }
        adapter.toggleCanDelete();
        setEditorText(adapter.isCanDel);
    }

    private String splitStr(List<ChannelModel> channelModels) {
        StringBuffer stringBuffer = new StringBuffer(1000);
        for (int i = 0, length = channelModels.size(); i < length; i++) {
            stringBuffer.append(channelModels.get(i).getFunCode());
            if (i != channelModels.size() - 1) {
                stringBuffer.append("|");
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public void OnItemClick(int type, int pos) {
        btnAddItem(type, pos);
        btnRemoveItem(type, pos);
        Toast.makeText(this, "---" + pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemLongClick() {
        if (adapter.isCanDel) return;//已处于删除状态,长按无效
        setEditorText(true);
        adapter.toggleCanDelete();
    }

    private void setEditorText(boolean editor) {
        personTopbar.setRightText(editor ? "完成" : "编辑");
    }

    /**
     * 添加某项
     *
     * @param type
     * @param pos
     */
    public void btnAddItem(int type, int pos) {
        if (type == Category.FIRST_TYPE) {
            allList.add(0, channels.get(pos));
            allAdapter.notifyDataSetChanged();
            return;
        }
        channels.add(allList.get(pos));
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除某项
     *
     * @param type
     * @param pos
     */
    public void btnRemoveItem(int type, int pos) {
        if (type == Category.FIRST_TYPE) {
            if (!channels.isEmpty()) {
                channels.remove(pos);
            }
            adapter.notifyItemRemoved(pos);
            return;
        }
        if (!allList.isEmpty()) {
            allList.remove(pos);
        }
        allAdapter.notifyItemRemoved(pos);
    }


    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {

            case HttpApis.http_one:
                ResponseChannel channelRe = (ResponseChannel) value;
                channels = channelRe.getBody().getSelectedChannels();
                allList = channelRe.getBody().getNotSelectedChannels();
                initCateView();
                break;
            case HttpApis.http_two:
                try {
                    JSONObject obj = new JSONObject((String) value);
                    if (obj.has("head")) {
                        JSONObject obj_head = obj.getJSONObject("head");
                        RespHeader header = new Gson().fromJson(obj_head.toString(), RespHeader.class);
                        if (header.getRspCode().equals(ResponseCode.Success)) {
                            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_PERSONALITY);
                            this.finish();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
