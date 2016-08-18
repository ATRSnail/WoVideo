package com.lt.hm.wovideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.NoDoubleItemClickListener;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xuchunhui on 16/8/16.
 */
public class EventsPageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,SecondTopbar.myTopbarClicklistenter {
    @BindView(R.id.set_topbar)
    SecondTopbar setTopbar;
    @BindView(R.id.lv_huodong)
    ListView lvHuodong;
    @BindView(R.id.event_refresh)
    SwipeRefreshLayout evnetRefresh;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_huodong;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopbar.setRightIsVisible(false);
        setTopbar.setOnTopbarClickListenter(this);

    }

    @Override
    public void initViews() {
        evnetRefresh.setOnRefreshListener(this);
        evnetRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

    }

    @Override
    public void initDatas() {
        lvHuodong.setAdapter(new myAdapter());
        lvHuodong.setOnItemClickListener(new NoDoubleItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventsPageActivity.this, ThirdPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "ku.17wo.cn");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    Unbinder unbinder;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                evnetRefresh.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    private class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1;
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
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_huodong, null);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            return convertView;
        }

        private class Holder {

        }
    }
}
