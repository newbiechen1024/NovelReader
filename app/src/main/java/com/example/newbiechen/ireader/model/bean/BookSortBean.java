package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;

/**
 * Created by newbiechen on 17-4-23.
 */
public class BookSortBean {
    /**
     * name : 玄幻
     * bookCount : 437252
     */
    private String name;
    private int bookCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}