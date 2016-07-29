package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class UserModel{

    /**
     * phoneNo : 18513179404
     * id : NNzZ7YelI8EZh2KuIWE8R31FL2FS1zLl
     * utime : null
     * sex : 1
     * headImg :
     * isVip : 0
     * nickName : 18513179404
     * email :
     * state : 1
     * passWord : e10adc3949ba59abbe56e057f20f883e
     * ctime : 1465735611000
     * introduction :
     */

    private String phoneNo;//手机号
    private String id;
    private Object utime;
    private String sex;//性别
    private String headImg;//头像地址
    private String isVip;//是否是VIP
    private String nickName;//昵称
    private String email;//电子邮件
    private String state;
    private String passWord;//密码
    private long ctime;//创建时间
    private String introduction;//简介
    private String etime;

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getUtime() {
        return utime;
    }

    public void setUtime(Object utime) {
        this.utime = utime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }



    @Override
    public String toString() {
        return "UserModel{" +
                "phoneNo='" + phoneNo + '\'' +
                ", id='" + id + '\'' +
                ", utime=" + utime +
                ", sex='" + sex + '\'' +
                ", headImg='" + headImg + '\'' +
                ", isVip='" + isVip + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", state='" + state + '\'' +
                ", passWord='" + passWord + '\'' +
                ", ctime=" + ctime +
                ", introduction='" + introduction + '\'' +
                ", etime='" + etime + '\'' +
                '}';
    }
}
