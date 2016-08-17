package com.lt.hm.wovideo.utils;

import com.lt.hm.wovideo.interf.OnUpdateLocationListener;

import java.util.ArrayList;

/**
 * Created by xuchunhui on 16/8/17.
 */
public class UpdateLocationMsg {

    public ArrayList<OnUpdateLocationListener> downloadListeners = new ArrayList<OnUpdateLocationListener>();
    private static UpdateLocationMsg instance;

    public static UpdateLocationMsg getInstance() {
        if (instance == null) {
            synchronized (UpdateLocationMsg.class) {
                if (instance == null) {
                    instance = new UpdateLocationMsg();
                }
            }
        }
        return instance;
    }

    public void addRegisterSucListeners(OnUpdateLocationListener listener) {
        this.downloadListeners.add(listener);
    }

    public void removeRegisterSucListeners(OnUpdateLocationListener listener) {
        this.downloadListeners.remove(listener);
    }
}
