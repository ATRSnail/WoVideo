package com.lt.hm.wovideo.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.VideoListAdapter;
import com.lt.hm.wovideo.base.BaseLazyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/11/28
 */
public class VideoListFrg extends BaseLazyFragment {


    @BindView(R.id.listview)
    ListView lv;

    View view;
    VideoListAdapter adapterVideoList;

    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;

    @Override
    protected int getLayoutId() {
        return R.layout.frg_video_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this, view);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        return view;
    }

    @Override
    public void onFirstUserVisible() {
        initView(null);
    }

    @Override
    public void initView(View view) {
        adapterVideoList = new VideoListAdapter(getContext());
        lv.setAdapter(adapterVideoList);

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

}
