package com.lt.hm.wovideo.widget.ViewMiddle;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

    private ListView regionListView;
    private ListView plateListView;
    private ArrayList<String> groups = new ArrayList<>();
    private Map<String, String> groupMap = new HashMap<>();
    private LinkedList<ViewMiddleModel> childrenItem = new LinkedList<>();
    private SparseArray<LinkedList<ViewMiddleModel>> children = new SparseArray<LinkedList<ViewMiddleModel>>();
    private List<LiveModles> menuList;
    private ViewMiddleAdapter plateListViewAdapter;
    private TextAdapter earaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tEaraPosition = 0;
    private int tBlockPosition = 0;
    private ViewMiddleModel showString = new ViewMiddleModel(-1, 0, "不限");
    private Map<Integer, ViewMiddleModel> id_name = new HashMap<>();

    public ViewMiddle(Context context, List<LiveModles> menuList) {
        super(context);
        this.menuList = menuList;
        init(context);
    }

    public void updateShowText(String showArea, String showBlock) {
        if (showArea == null || showBlock == null) {
            return;
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).equals(groupMap.get(showArea))) {
                earaListViewAdapter.setSelectedPosition(i);
                childrenItem.clear();
                if (i < children.size()) {
                    childrenItem.addAll(children.get(i));
                }
                tEaraPosition = i;
                break;
            }
        }
        for (int j = 0; j < childrenItem.size(); j++) {
            if (childrenItem.get(j).getTitle().equals(showBlock.trim())) {
                plateListViewAdapter.setSelectedPosition(j);
                tBlockPosition = j;
                break;
            }
        }
        setDefaultSelect();
    }

    private int parentId;
    private ViewMiddleModel viewMiddleModel;

    private void init(Context context) {
        if ((menuList == null) || (menuList.size() == 0)) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.category_menu, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);

        for (int i = 0; i < menuList.size(); i++) {
            LiveModles categoryMenu = menuList.get(i);
            parentId = categoryMenu.getParentId();
            groups.add(categoryMenu.getTitle());
            groupMap.put(categoryMenu.getTitle(), categoryMenu.getTitle());
            LinkedList<ViewMiddleModel> tItem = new LinkedList<>();
            if (categoryMenu.getSinatv() != null) {
                for (int j = 0; j < categoryMenu.getSinatv().size(); j++) {
                    LiveModel categorySecMenu = categoryMenu.getSinatv().get(j);
                    viewMiddleModel = new ViewMiddleModel(categorySecMenu.getId(), parentId, categorySecMenu.getTvName());
                    tItem.add(viewMiddleModel);
                    id_name.put(categorySecMenu.getId(), viewMiddleModel);
                }
            }
            children.put(i, tItem);
        }

        earaListViewAdapter = new TextAdapter(context, groups, R.color.white, R.color.acount_color);
        earaListViewAdapter.setTextSize(14);
        earaListViewAdapter.setDrawLeft(R.drawable.line);
        earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
        regionListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (position < children.size()) {
                    childrenItem.clear();
                    childrenItem.addAll(children.get(position));
                    plateListViewAdapter.notifyDataSetChanged();
                }
            }
        });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter = new ViewMiddleAdapter(context, childrenItem, R.color.nav_text_selected, R.color.white);
        plateListViewAdapter.setTextSize(14);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter.setOnItemClickListener(new ViewMiddleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {

                showString = childrenItem.get(position);
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(showString.getParent_id(), position);
                }

            }
        });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition);

        setDefaultSelect();
    }

    public void setDefaultSelect() {
        regionListView.setSelection(tEaraPosition);
        plateListView.setSelection(tBlockPosition);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(int parentId, int position);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }
}
