package com.example.newbiechen.ireader.event;

import com.example.newbiechen.ireader.model.bean.CollBookBean;

/**
 * Created by newbiechen on 17-5-27.
 */

public class DeleteTaskEvent {
    public CollBookBean collBook;

    public DeleteTaskEvent(CollBookBean collBook){
        this.collBook = collBook;
    }
}
