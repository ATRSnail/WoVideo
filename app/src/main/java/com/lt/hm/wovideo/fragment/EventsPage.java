package com.lt.hm.wovideo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public class EventsPage extends BaseFragment {
    @BindView(R.id.lv_huodong)
    ListView lvHuodong;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_huodong, container, false);
        unbinder=ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void initData() {
        super.initData();
        lvHuodong.setAdapter(new myAdapter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    private class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=null;
            if (convertView==null){
                holder=new Holder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_huodong,null);
                convertView.setTag(holder);
            }else {
                holder= (Holder) convertView.getTag();
            }
            return convertView;
        }
        private class Holder{

        }
    }
}