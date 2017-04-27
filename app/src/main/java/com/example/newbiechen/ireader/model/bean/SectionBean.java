package com.example.newbiechen.ireader.model.bean;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.example.newbiechen.ireader.App;

/**
 * Created by newbiechen on 17-4-16.
 */

public class SectionBean {
    private String name;
    private int drawableId;

    public SectionBean(String name, @DrawableRes int drawableId){
        this.name = name;
        this.drawableId = drawableId;
    }

    public SectionBean(@StringRes int strRes, @DrawableRes int drawableId){
        this.name = App.getContext()
                .getString(strRes);

        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
