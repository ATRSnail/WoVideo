package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.category.CategoryAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.interf.OnCateItemListener;
import com.lt.hm.wovideo.model.Category;
import com.lt.hm.wovideo.widget.CustomTopbar;
import com.lt.hm.wovideo.widget.SecondTopbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PersonalitySet extends BaseActivity implements SecondTopbar.myTopbarClicklistenter, OnCateItemListener {


    @BindView(R.id.person_topbar)
    SecondTopbar personTopbar;
    @BindView(R.id.recycler_personal_mine)
    RecyclerView mineCateRecycler;
    @BindView(R.id.recycler_personal_all)
    RecyclerView allCateRecycler;

    private CategoryAdapter adapter;
    private CategoryAdapter allAdapter;
    private List<Category> list = new ArrayList<>();
    private List<Category> allList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personality_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        personTopbar.setLeftIsVisible(true);
        personTopbar.setRightIsVisible(true);
        personTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initViews() {
        adapter = new CategoryAdapter(this, list, this,Category.FIRST_TYPE);
        allAdapter = new CategoryAdapter(this, allList, this,Category.SECOND_TYPE);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 3);
        mineCateRecycler.setLayoutManager(mLayoutManager);
        allCateRecycler.setLayoutManager(mLayoutManager2);
        mineCateRecycler.setAdapter(adapter);
        allCateRecycler.setAdapter(allAdapter);
    }

    @Override
    public void initDatas() {
        list.add(new Category("我的频道", 0));
        list.add(new Category("奶油", 1));
        list.add(new Category("威化", 1));
        list.add(new Category("凤梨", 1));
        list.add(new Category("烧饼", 1));
        allList.add(new Category("频道栏目", 0));
        allList.add(new Category("饼干", 1));
        allList.add(new Category("饼干", 1));
        allList.add(new Category("饼干", 1));
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
      adapter.toggleCanDelete();
    }

    public void btnAddItem(int type) {
        if (type == Category.FIRST_TYPE) {
            allList.add(0, new Category("奶油", 1));
            allAdapter.notifyDataSetChanged();
            return;
        }
        list.add(0, new Category("奶油", 1));
        adapter.notifyDataSetChanged();
    }

    public void btnRemoveItem(int type, int pos) {
        if (type == Category.FIRST_TYPE){
            if (!list.isEmpty()) {
                list.remove(pos);
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
    public void OnItemClick(int type, int pos) {
        btnRemoveItem(type, pos);
        btnAddItem(type);
        Toast.makeText(this, "---" + pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemLongClick() {
        adapter.toggleCanDelete();
    }

}
