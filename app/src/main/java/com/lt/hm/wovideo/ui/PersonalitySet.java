package com.lt.hm.wovideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.category.CategoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpCallback;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.model.CateTagModel;
import com.lt.hm.wovideo.model.Category;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.model.TagModel;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.model.response.ResponseChannel;
import com.lt.hm.wovideo.model.response.ResponseTag;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UT;
import com.lt.hm.wovideo.utils.UpdateRecommedMsg;
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
    private List<ChannelModel> seleChannelList = new ArrayList<>();
    private List<ChannelModel> unSelecChannels = new ArrayList<>();

    private List<TagModel> seleTags = new ArrayList<>();
    private List<TagModel> unSeleTags = new ArrayList<>();
    private boolean isTag = false;
    private boolean isEdit = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personality_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            if (intent.getExtras().containsKey("isTag")) {

                isTag = intent.getBooleanExtra("isTag", false);
            }
            if (intent.getExtras().containsKey("isEdit")) {
                isEdit = intent.getBooleanExtra("isEdit", false);
            }
        }
        personTopbar.setLeftIsVisible(true);
        personTopbar.setRightIsVisible(true);
        personTopbar.setRightText(isTag ? isEdit ? "编辑" : "跳过" : "编辑");
        personTopbar.setOnTopbarClickListenter(this);
        changeText.setText(isTag ? "我的标签" : "我的频道");
        unchangeText.setText(isTag ? "未选标签" : "频道栏目");
    }

    @Override
    public void initViews() {

    }

    private void initCateView() {
        adapter = new CategoryAdapter(isTag, this, seleChannelList, seleTags, this, Category.FIRST_TYPE);
        allAdapter = new CategoryAdapter(isTag, this, unSelecChannels, unSeleTags, this, Category.SECOND_TYPE);
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
        if (isTag) {
            getTagInfos();
        } else {
            getClassInfos();
        }
    }

    /**
     * 获取标签
     */
    private void getTagInfos() {
        HashMap<String, Object> map = new HashMap<>();
        UserModel userModel = UserMgr.getUseInfo();
        if (userModel != null)
            map.put("userid", userModel.getId());
        HttpApis.getIndividuationTag(map, HttpApis.http_thr, new HttpCallback<>(ResponseTag.class, this));
    }

    /**
     * 从网络获取频道
     */
    private void getClassInfos() {
        HashMap<String, Object> map = new HashMap<>();
        UserModel userModel = UserMgr.getUseInfo();
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
        UserModel userModel = UserMgr.getUseInfo();
        if (userModel != null)
            map.put("userid", userModel.getId());
        map.put("channel", channel);
        HttpApis.updateChannel(map, HttpApis.http_two, new HttpCallback<>(String.class, this));
    }

    /**
     * 保存个性化tag
     */
    private void updateTag(String channel) {
        TLog.error("channel--->" + channel);
        HashMap<String, Object> map = new HashMap<>();
        UserModel userModel = UserMgr.getUseInfo();
        if (userModel != null)
            map.put("userid", userModel.getId());
        map.put("tag", channel);
        HttpApis.updateTag(map, HttpApis.http_two, new HttpCallback<>(String.class, this));
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        if (isTag && adapter == null) {
            finish();
            return;
        }

        if (adapter.isCanDel) {
            if (isTag) {
                updateTag(splitStr());
            } else {
                updateChannel(splitStr());
            }
        }
        if (isTag) {
            this.finish();
        } else {
            adapter.toggleCanDelete();
            setEditorText(adapter.isCanDel);
        }

    }

    private String splitStr() {
        StringBuilder stringBuffer = new StringBuilder(1000);
        if (isTag) {
            for (int i = 0, length = seleTags.size(); i < length; i++) {
                stringBuffer.append(seleTags.get(i).getCode());
                if (i != seleTags.size() - 1) {
                    stringBuffer.append("|");
                }
            }
        } else {
            for (int i = 0, length = seleChannelList.size(); i < length; i++) {
                stringBuffer.append(seleChannelList.get(i).getFunCode());
                if (i != seleChannelList.size() - 1) {
                    stringBuffer.append("|");
                }
            }
        }
        return stringBuffer.toString();
    }

    private ChannelModel channelModel;
    private TagModel tagModel;

    @Override
    public void OnItemClick(int type, int pos) {
        btnAddItem(type, pos);
    }

    @Override
    public void OnItemLongClick() {
        if (adapter.isCanDel) return;//已处于删除状态,长按无效
        setEditorText(true);
        adapter.toggleCanDelete();
    }

    private void setEditorText(boolean editor) {
        personTopbar.setRightText(editor ? "完成" : isTag ? "跳过" : "编辑");
    }

    /**
     * 添加某项
     *
     * @param type
     * @param pos
     */
    public void btnAddItem(int type, int pos) {
        if (isTag) {
            if (type == Category.FIRST_TYPE) {
                tagModel = seleTags.get(pos);
                unSeleTags.add(0, tagModel);
                seleTags.remove(tagModel);
            } else {
                setEditorText(true);
                adapter.isCanDel = true;
                tagModel = unSeleTags.get(pos);
                seleTags.add(tagModel);
                unSeleTags.remove(tagModel);
            }
        } else {
            if (type == Category.FIRST_TYPE) {
                channelModel = seleChannelList.get(pos);
                unSelecChannels.add(0, channelModel);
                seleChannelList.remove(channelModel);
            } else {
                setEditorText(true);
                adapter.isCanDel = true;
                channelModel = unSelecChannels.get(pos);
                seleChannelList.add(channelModel);
                unSelecChannels.remove(channelModel);
            }
        }

        adapter.notifyDataSetChanged();
        allAdapter.notifyDataSetChanged();
    }


    @Override
    public <T> void onSuccess(T value, int flag) {
        super.onSuccess(value, flag);
        switch (flag) {

            case HttpApis.http_one:
                ResponseChannel channelRe = (ResponseChannel) value;
                seleChannelList = channelRe.getBody().getSelectedChannels();
                unSelecChannels = channelRe.getBody().getNotSelectedChannels();
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
                            if (!isTag)
                                UpdateRecommedMsg.getInstance().downloadListeners.get(0).onUpdateTagLister();

                            this.finish();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UT.showNormal("保存失败");
                break;
            case HttpApis.http_thr:
                ResponseTag responseTag = (ResponseTag) value;
                seleTags = responseTag.getBody().getSelectedDicList();
                unSeleTags = responseTag.getBody().getNotSelectedDicList();
                initCateView();
                break;

        }
    }


}
