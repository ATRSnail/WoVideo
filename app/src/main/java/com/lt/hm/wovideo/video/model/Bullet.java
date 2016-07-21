package com.lt.hm.wovideo.video.model;

import java.util.Random;

/**
 * Created by KECB on 7/21/16.
 */

public class Bullet {
    private String content;
    private String fontColor;
    private String fontSize;

    public Bullet() {
    }

    public Bullet(String content, String fontColor, String fontSize) {
        this.content = content;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void randomFontColor(int intColor) {
        setFontColor(String.format("#%06X", (0xFFFFFF & intColor)));
    }

    public void randomFontSize() {
        Random random = new Random();
        int min = 15, max = 20;
        setFontSize((random.nextInt(max - min) + min) + "");
    }

}
