package com.lt.hm.wovideo.widget.ViewMiddle;

/**
 * @author 作者:xch
 * @version 创建时间：2015-1-22 上午11:09:44 类说明 声音分类下拉选项
 */
public class ViewMiddleModel {

    private int mSoundType; // id
    private int parent_id; // 父类id
    private String title; // 名称

    public ViewMiddleModel(int mSoundType, int parent_id, String title) {
        super();
        this.mSoundType = mSoundType;
        this.parent_id = parent_id;
        this.title = title;
    }

    public int getmSoundType() {
        return mSoundType;
    }

    public void setmSoundType(int mSoundType) {
        this.mSoundType = mSoundType;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ViewMiddleModel [mSoundType=" + mSoundType + ", parent_id=" + parent_id + ", title=" + title + "]";
    }

}
