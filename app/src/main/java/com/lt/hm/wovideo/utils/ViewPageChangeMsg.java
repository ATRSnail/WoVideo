package com.lt.hm.wovideo.utils;

import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.interf.onChangeLister;

import java.util.ArrayList;

/**
 * Created by xuchunhui on 16/8/17.
 */
public class ViewPageChangeMsg {

    public ArrayList<onChangeLister> listers = new ArrayList<onChangeLister>();
    private static ViewPageChangeMsg instance;

    public static ViewPageChangeMsg getInstance() {
        if (instance == null) {
            synchronized (ViewPageChangeMsg.class) {
                if (instance == null) {
                    instance = new ViewPageChangeMsg();
                }
            }
        }
        return instance;
    }

    public void addRegisterSucListeners(onChangeLister listener) {
        this.listers.add(listener);
    }

    public void removeRegisterSucListeners(onChangeLister listener) {
        this.listers.remove(listener);
    }
}
