package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-29.
 */

public class DetailBean<T> {
    private T detail;
    private List<CommentBean> bestComments;
    private List<CommentBean> comments;

    public DetailBean(T details, List<CommentBean> bestComments, List<CommentBean> comments) {
        this.detail = details;
        this.bestComments = bestComments;
        this.comments = comments;
    }

    public T getDetail() {
        return detail;
    }

    public List<CommentBean> getBestComments() {
        return bestComments;
    }

    public List<CommentBean> getComments() {
        return comments;
    }
}
