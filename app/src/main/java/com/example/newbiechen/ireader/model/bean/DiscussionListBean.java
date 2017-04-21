package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscussionListBean extends BaseBean {

    private List<DiscussionBean> posts;

    public List<DiscussionBean> getPosts() {
        return posts;
    }

    public void setPosts(List<DiscussionBean> posts) {
        this.posts = posts;
    }
}
