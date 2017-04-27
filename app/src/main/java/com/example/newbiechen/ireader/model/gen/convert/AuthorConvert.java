package com.example.newbiechen.ireader.model.gen.convert;

import com.example.newbiechen.ireader.model.bean.AuthorBean;
import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoSession;
import com.example.newbiechen.ireader.model.local.DaoDbHelper;
import com.example.newbiechen.ireader.model.local.LocalRepository;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by newbiechen on 17-4-26.
 */

public class AuthorConvert implements PropertyConverter<AuthorBean,String> {
    private LocalRepository repository = LocalRepository.getInstance();
    @Override
    public AuthorBean convertToEntityProperty(String databaseValue) {
        return repository.getAuthor(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(AuthorBean entityProperty) {
        repository.saveAuthor(entityProperty);
        return entityProperty.get_id();
    }
}
