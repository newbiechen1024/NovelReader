package com.example.newbiechen.ireader.event;

import com.example.newbiechen.ireader.utils.Constant;

/**
 * Created by newbiechen on 17-4-21.
 */

public class SelectorEvent {

    public String distillate;

    public String type;

    public String sort;

    public SelectorEvent(String distillate,
                         String type,
                         String sort) {
        this.distillate = distillate;
        this.type = type;
        this.sort = sort;
    }

    public SelectorEvent(String sort) {
        this.sort = sort;
    }
}
