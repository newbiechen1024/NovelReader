package com.example.newbiechen.ireader.model.gen.convert;

import com.example.newbiechen.ireader.model.bean.BookHelpfulBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by newbiechen on 17-4-26.
 */

public class BookHelpfulConvert implements PropertyConverter<BookHelpfulBean,Long> {
    @Override
    public BookHelpfulBean convertToEntityProperty(Long databaseValue) {
        return null;
    }

    @Override
    public Long convertToDatabaseValue(BookHelpfulBean entityProperty) {
        return null;
    }
}
