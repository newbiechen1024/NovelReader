package com.example.newbiechen.ireader.model;

import java.util.function.ToIntBiFunction;

/**
 * Created by newbiechen on 17-4-22.
 */

public enum  BookType {
    ALL("all","全部类型"),
    XHQH("xhqh","玄幻奇幻"),
    WXXX("",""),
    DSYN("",""),
    LSJS("",""),
    YXJJ("",""),
    KHLY("",""),
    CYJK("",""),
    HMZC("","");


    private String name;
    private String value;
    BookType(String name,String value){
        this.name = name;
        this.value = value;
    }
}
