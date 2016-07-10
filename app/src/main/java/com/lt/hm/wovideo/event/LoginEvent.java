package com.lt.hm.wovideo.event;

import com.lt.hm.wovideo.model.UserModel;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class LoginEvent  {
    public UserModel model=null;

    public LoginEvent(UserModel model) {
        this.model = model;
    }
}
