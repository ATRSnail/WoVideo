package com.lt.hm.wovideo.model;

import java.util.ArrayList;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/8/29
 */
public class CommentListModel {
    private ArrayList<CommentModel> commentList;

    public ArrayList<CommentModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<CommentModel> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "CommentListModel{" +
                "commentList=" + commentList +
                '}';
    }
}
