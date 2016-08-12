package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class ChannelListModel {
    private List<ChannelModel> selectedChannels;
    private List<ChannelModel> notSelectedChannels;

    public List<ChannelModel> getNotSelectedChannels() {
        return notSelectedChannels;
    }

    public void setNotSelectedChannels(List<ChannelModel> notSelectedChannels) {
        this.notSelectedChannels = notSelectedChannels;
    }

    public List<ChannelModel> getSelectedChannels() {
        return selectedChannels;
    }

    public void setSelectedChannels(List<ChannelModel> selectedChannels) {
        this.selectedChannels = selectedChannels;
    }

    @Override
    public String toString() {
        return "ChannelListModel{" +
                "selectedChannels=" + selectedChannels +
                ", notSelectedChannels=" + notSelectedChannels +
                '}';
    }
}
