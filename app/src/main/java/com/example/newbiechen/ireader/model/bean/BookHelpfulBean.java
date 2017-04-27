package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-4-26.
 */
@Entity
public class BookHelpfulBean {
    /**
     * total : 1
     * no : 5
     * yes : 6
     */
    @Id(autoincrement = true)
    private long id;

    private int total;
    private int no;
    private int yes;


    @Generated(hash = 1595070628)
    public BookHelpfulBean(long id, int total, int no, int yes) {
        this.id = id;
        this.total = total;
        this.no = no;
        this.yes = yes;
    }

    @Generated(hash = 534892841)
    public BookHelpfulBean() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }
}