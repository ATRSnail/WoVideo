package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/16.
 */
public class TagListModel {
    private List<TagModel> selectedDicList;
    private List<TagModel> notSelectedDicList;

    public List<TagModel> getNotSelectedDicList() {
        return notSelectedDicList;
    }

    public void setNotSelectedDicList(List<TagModel> notSelectedDicList) {
        this.notSelectedDicList = notSelectedDicList;
    }

    public List<TagModel> getSelectedDicList() {
        return selectedDicList;
    }

    public void setSelectedDicList(List<TagModel> selectedDicList) {
        this.selectedDicList = selectedDicList;
    }
}
