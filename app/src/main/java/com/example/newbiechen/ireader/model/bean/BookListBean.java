package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-1.
 */

public class BookListBean {
    /**
     * _id : 58fecdb4cb21d9452f6d174d
     * title : 自用书单 系统 穿越 无敌
     * author : 暖栀
     * desc : 好看的话 各位老板点下收藏。
     * gender : male
     * collectorCount : 604
     * cover : /agent/http://media.tadu.com/2015/01/30/8/6/e/9/86e966ebb99d42d6b48f26603eec8db3_250_200.jpg
     * bookCount : 295
     */

    private String _id;
    private String title;
    private String author;
    private String desc;
    private String gender;
    private int collectorCount;
    private String cover;
    private int bookCount;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCollectorCount() {
        return collectorCount;
    }

    public void setCollectorCount(int collectorCount) {
        this.collectorCount = collectorCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}
