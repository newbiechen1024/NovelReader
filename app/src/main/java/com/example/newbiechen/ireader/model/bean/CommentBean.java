package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-29.
 */

public class CommentBean {
    /**
     * _id : 57fd69356b613e9d1e69febb
     * content : 2000年
     * author : {"_id":"57b6794f138527405e83382c","avatar":"/avatar/bc/3f/bc3f0b58815e497b00dabb7a14476891","nickname":"孤独患者","activityAvatar":"","type":"normal","lv":6,"gender":"female"}
     * floor : 7150
     * likeCount : 0
     * created : 2016-10-11T22:35:33.303Z
     * replyTo : {"_id":"57caec937a142c2277757f2d","floor":7038,"author":{"_id":"576a96dd4cb19fa249303369","nickname":"刘"}}
     */

    private String _id;
    private String content;
    private AuthorBean author;
    private int floor;
    private int likeCount;
    private String created;
    private ReplyToBean replyTo;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ReplyToBean getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ReplyToBean replyTo) {
        this.replyTo = replyTo;
    }
}