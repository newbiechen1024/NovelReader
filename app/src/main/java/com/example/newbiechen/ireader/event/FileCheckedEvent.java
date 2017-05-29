package com.example.newbiechen.ireader.event;

/**
 * Created by newbiechen on 17-5-28.
 */

public class FileCheckedEvent {
    boolean isSelected;

    public FileCheckedEvent(boolean isSelected){
        this.isSelected = isSelected;
    }
}
