package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-20.
 */

public class DiscussionBean {
    /**
     * _id : 58f805798c8c193a414c6853
     * title : 女朋友生气的奇葩理由我知道这里是女生区可我觉得好玩还是很想转
     * author : {"_id":"553136ba70feaa764a096f6f","avatar":"/avatar/26/eb/26ebf8ede76d7f52cd377960bd66383b","nickname":"九歌","activityAvatar":"","type":"normal","lv":8,"gender":"female"}
     * type : normal
     * likeCount : 6
     * block : girl
     * haveImage : false
     * state : normal
     * updated : 2017-04-20T05:34:17.931Z
     * created : 2017-04-20T00:48:57.085Z
     * commentCount : 35
     * voteCount : 0
     */

    private String _id;
    private String title;
    private AuthorBean author;
    private String type;
    private int likeCount;
    private String block;
    private boolean haveImage;
    private String state;
    private String updated;
    private String created;
    private int commentCount;
    private int voteCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}