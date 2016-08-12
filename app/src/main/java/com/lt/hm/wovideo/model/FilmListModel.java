package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class FilmListModel {
    private List<FilmMode> typeList;

    public List<FilmMode> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<FilmMode> typeList) {
        this.typeList = typeList;
    }

    @Override
    public String toString() {
        return "FilmListModel{" +
                "typeList=" + typeList +
                '}';
    }
}
