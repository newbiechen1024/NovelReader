package com.example.newbiechen.ireader.model.flag;

/**
 * Created by newbiechen on 2018/1/14.
 */

public enum BookFormat {
    TXT("txt"), PDF("pdf"), EPUB("epub"), NB("nb"), NONE("none");

    private String name;

    private BookFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
