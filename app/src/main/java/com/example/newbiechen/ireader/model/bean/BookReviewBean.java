package com.example.newbiechen.ireader.model.bean;

import com.example.newbiechen.ireader.model.gen.convert.BookConvert;
import com.example.newbiechen.ireader.model.gen.convert.BookHelpfulConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-4-21.
 * 书籍类别讨论
 */
@Entity
public class BookReviewBean {
    /**
     * _id : 58f8f3efedaa9fe3624a87bb
     * title : 为你写一个中肯的书评，我的访客
     * book : {"_id":"530f3912651881e60d04deb3","cover":"/agent/http://img.17k.com/images/bookcover/2014/3769/18/753884-1399818238000.jpg","title":"我的26岁女房客","site":"zhuishuvip","type":"dsyn","latelyFollower":null,"retentionRatio":null}
     * helpful : {"total":1,"no":5,"yes":6}
     * likeCount : 0
     * haveImage : false
     * state : distillate
     * updated : 2017-04-21T08:20:15.991Z
     * created : 2017-04-20T17:46:23.366Z
     */
    @Id
    private String _id;
    private String title;
    @Convert(converter = BookConvert.class,columnType = String.class)
    private BookBean book;
    @Convert(converter = BookHelpfulConvert.class,columnType = Long.class)
    private BookHelpfulBean helpful;

    private int likeCount;
    private boolean haveImage;
    @Index
    private String state;
    private String updated;
    private String created;

    @Generated(hash = 55680806)
    public BookReviewBean(String _id, String title, BookBean book, BookHelpfulBean helpful, int likeCount, boolean haveImage, String state, String updated, String created) {
        this._id = _id;
        this.title = title;
        this.book = book;
        this.helpful = helpful;
        this.likeCount = likeCount;
        this.haveImage = haveImage;
        this.state = state;
        this.updated = updated;
        this.created = created;
    }

    @Generated(hash = 356564766)
    public BookReviewBean() {
    }

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

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    public BookHelpfulBean getHelpful() {
        return helpful;
    }

    public void setHelpful(BookHelpfulBean helpful) {
        this.helpful = helpful;
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

    public boolean getHaveImage() {
        return this.haveImage;
    }




}