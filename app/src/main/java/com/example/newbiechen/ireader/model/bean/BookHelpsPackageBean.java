package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public class BookHelpsPackageBean extends BaseBean{

    private List<BookHelpsBean> helps;

    public List<BookHelpsBean> getHelps() {
        return helps;
    }

    public void setHelps(List<BookHelpsBean> helps) {
        this.helps = helps;
    }

}
