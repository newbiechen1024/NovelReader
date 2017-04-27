package com.example.newbiechen.ireader.model.bean;

import com.example.newbiechen.ireader.model.gen.convert.AuthorConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-4-20.
 */
@Entity
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
    @Id
    private String _id;
    @Convert(converter = AuthorConvert.class,columnType = String.class)
    private AuthorBean author;
    private String title;
    private int likeCount;
    private boolean haveImage;
    @Index
    private String state;
    private String updated;
    private String created;
    private int commentCount;

    @Generated(hash = 788615014)
    public BookHelpsBean(String _id, AuthorBean author, String title, int likeCount, boolean haveImage, String state, String updated, String created, int commentCount) {
        this._id = _id;
        this.author = author;
        this.title = title;
        this.likeCount = likeCount;
        this.haveImage = haveImage;
        this.state = state;
        this.updated = updated;
        this.created = created;
        this.commentCount = commentCount;
    }

    @Generated(hash = 1556001284)
    public BookHelpsBean() {
    }

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

    public boolean getHaveImage() {
        return this.haveImage;
    }
}