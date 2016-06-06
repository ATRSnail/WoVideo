package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/5
 */
public class Videos {
    private String name;
    private String type;
    private String desc;
    private String img;

    @Override
    public String toString() {
        return "Videos{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
