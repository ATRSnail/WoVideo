package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/10
 */
public class CommentModel {

    /**
     * phoneNo : 18801454755
     * id : 38
     * utime : 1468036540000
     * vfId : 2
     * userId : gwh0QEna7eBWJAD9TUMEqqq4q8bBwDpj
     * state : 0
     * comment : 这是评论啊aaaa11
     * ctime : 1468036540000
     */

    private List<CommentListBean> commentList;

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean {
        private String phoneNo;
        private int id;
        private long utime;
        private String vfId;
        private String userId;
        private String state;
        private String comment;
        private long ctime;

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getUtime() {
            return utime;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }

        public String getVfId() {
            return vfId;
        }

        public void setVfId(String vfId) {
            this.vfId = vfId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }
    }
}
