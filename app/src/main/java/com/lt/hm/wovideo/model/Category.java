package com.lt.hm.wovideo.model;

/**
 * Created by Administrator on 2016/8/5.
 */
public class Category {
    public static final int FIRST_TYPE = 0;
    public static final int SECOND_TYPE = 1;

    private String categoryName;
    private int type;

    public Category(String categoryName, int type) {
        this.categoryName = categoryName;
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getType() {
        return type;
    }
}
