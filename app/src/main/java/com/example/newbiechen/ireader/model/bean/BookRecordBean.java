package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-5-20.
 */
@Entity
public class BookRecordBean{
    //所属的书的id
    @Id
    private String bookId;
    //阅读到了第几章
    private int chapter;
    //起始的字节位置
    private int start;
    //结束的字节位置
    private int end;

    @Generated(hash = 426456590)
    public BookRecordBean(String bookId, int chapter, int start, int end) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.start = start;
        this.end = end;
    }

    @Generated(hash = 398068002)
    public BookRecordBean() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
