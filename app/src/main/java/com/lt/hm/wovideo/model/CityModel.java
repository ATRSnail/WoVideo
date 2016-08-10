package com.lt.hm.wovideo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityModel extends MultiItemEntity {
    public static final int TITLE = 1;
    public static final int CITY_LIST = 2;


    private String head;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}