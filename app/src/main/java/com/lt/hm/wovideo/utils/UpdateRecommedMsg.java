package com.lt.hm.wovideo.utils;

import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.interf.updateTagLister;

import java.util.ArrayList;

/**
 * Created by xuchunhui on 16/8/17.
 */
public class UpdateRecommedMsg {

    public ArrayList<updateTagLister> downloadListeners = new ArrayList<updateTagLister>();
    private static UpdateRecommedMsg instance;

    public static UpdateRecommedMsg getInstance() {
        if (instance == null) {
            synchronized (UpdateRecommedMsg.class) {
                if (instance == null) {
                    instance = new UpdateRecommedMsg();
                }
            }
        }
        return instance;
    }

    public void addRegisterSucListeners(updateTagLister listener) {
        this.downloadListeners.add(listener);
    }

    public void removeRegisterSucListeners(updateTagLister listener) {
        this.downloadListeners.remove(listener);
    }
}
