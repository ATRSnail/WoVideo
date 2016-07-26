package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/26
 */
public class ValidateComment {
    private String sensitiveWord;
    private String isPass;

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    @Override
    public String toString() {
        return "ValidateComment{" +
                "sensitiveWord='" + sensitiveWord + '\'' +
                ", isPass='" + isPass + '\'' +
                '}';
    }
}
