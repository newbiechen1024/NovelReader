package com.example.newbiechen.ireader.model.flag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by newbiechen on 17-4-24.
 */

public enum  BookSelection {

    DISTILLATE(BookDistillate.values()),
    SORT_TYPE(BookSort.values()),
    BOOK_TYPE(BookType.values());

    private BookConvert [] converts;
    BookSelection(BookConvert ...converts){
        this.converts = converts;
    }

    public List<String> getTypeParams(){
        List<String> params = new ArrayList<>();
        for (BookConvert convert : converts){
            params.add(convert.getTypeName());
        }
        return params;
    }

    public List<String> getNetParams(){
        List<String> params = new ArrayList<>();
        for (BookConvert convert : converts){
            params.add(convert.getNetName());
        }
        return params;
    }
}
