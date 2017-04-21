package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-20.
 */

public class BookHelpsBean {
    /**
     * _id : 58f7590223c128231d6fc3ec
     * author : {"_id":"575558500082adf41f499536","avatar":"/avatar/19/40/1940310d1277e583b07369dce21ca701","nickname":"秋名山丶公子","activityAvatar":"/activities/20170120/1.jpg","type":"commentator","lv":8,"gender":"female"}
     * title : 【读书会-送给喜欢末世的你】末世流精选小说（第三期）
     * likeCount : 19
     * haveImage : false
     * state : normal
     * updated : 2017-04-20T05:45:19.976Z
     * created : 2017-04-19T12:33:06.285Z
     * commentCount : 46
     */
    private String _id;
    private AuthorBean author;
    private String title;
    private int likeCount;
    private boolean haveImage;
    private String state;
    private String updated;
    private String created;
    private int commentCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isHaveImage() {
        return haveImage;
    }

    public void setHaveImage(boolean haveImage) {
        this.haveImage = haveImage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}