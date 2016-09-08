package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/9
 */
public class LiveModles {
    public String  title;
    public List<LiveModel> sinatv;
    private List<LiveModel> localTV;
    private List<LiveModel> cctv;
    private List<LiveModel> otherTv;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LiveModel> getSinatv() {
        return sinatv;
    }

    public void setSinatv(List<LiveModel> sinatv) {
        this.sinatv = sinatv;
    }
    /**
     * 当前类别Item总数。Category也需要占用一个Item
     * @return
     */
    public int getItemCount() {
        return sinatv.size() + 1;
    }
    /**
     *  获取Item内容
     *
     * @param pPosition
     * @return
     */
    public Object getItem(int pPosition) {
        // Category排在第一位
        if (pPosition == 0) {
            return title;
        } else {
            return sinatv.get(pPosition - 1);
        }
    }

    public List<LiveModel> getLocalTV() {
        return localTV;
    }

    public void setLocalTV(List<LiveModel> localTV) {
        this.localTV = localTV;
    }

    public List<LiveModel> getCctv() {
        return cctv;
    }

    public void setCctv(List<LiveModel> cctv) {
        this.cctv = cctv;
    }

    public List<LiveModel> getOtherTv() {
        return otherTv;
    }

    public void setOtherTv(List<LiveModel> otherTv) {
        this.otherTv = otherTv;
    }

    @Override
    public String toString() {
        return "LiveModles{" +
                "title='" + title + '\'' +
                ", sinatv=" + sinatv +
                '}';
    }

}
