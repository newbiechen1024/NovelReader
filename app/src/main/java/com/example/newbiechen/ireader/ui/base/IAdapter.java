package com.example.newbiechen.ireader.ui.base;

/**
 * Created by newbiechen on 17-3-21.
 */

public interface IAdapter<T> {

    public void onBind(T value, int pos);
}
