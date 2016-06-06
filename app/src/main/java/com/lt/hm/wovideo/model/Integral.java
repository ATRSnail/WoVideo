package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class Integral {
    private String name;
    private String scores;

    @Override
    public String toString() {
        return "Integral{" +
                "name='" + name + '\'' +
                ", scores='" + scores + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }
}
