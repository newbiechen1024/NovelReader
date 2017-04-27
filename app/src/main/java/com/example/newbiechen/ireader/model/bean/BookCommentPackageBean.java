package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */
public class BookCommentPackageBean extends BaseBean {

    private List<BookCommentBean> posts;

    public List<BookCommentBean> getPosts() {
        return posts;
    }

    public void setPosts(List<BookCommentBean> posts) {
        this.posts = posts;
    }
}
