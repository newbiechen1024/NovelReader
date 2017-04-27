package com.example.newbiechen.ireader.model.gen.convert;

import com.example.newbiechen.ireader.model.bean.BookBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by newbiechen on 17-4-26.
 */

public class BookConvert implements PropertyConverter<BookBean,String> {
    @Override
    public BookBean convertToEntityProperty(String databaseValue) {
        return null;
    }

    @Override
    public String convertToDatabaseValue(BookBean entityProperty) {
        return null;
    }
}
