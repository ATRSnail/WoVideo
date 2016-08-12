package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class LocalCityListModel {
    private List<LocalCityModel> citys;

    public List<LocalCityModel> getCitys() {
        return citys;
    }

    public void setCitys(List<LocalCityModel> citys) {
        this.citys = citys;
    }

    @Override
    public String toString() {
        return "LocalCityListModel{" +
                "citys=" + citys +
                '}';
    }
}
