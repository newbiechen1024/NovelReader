package com.example.newbiechen.ireader.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;
import com.example.newbiechen.ireader.model.gen.BookChapterBeanDao;
import com.example.newbiechen.ireader.model.gen.BookCommentBeanDao;
import com.example.newbiechen.ireader.model.gen.BookHelpfulBeanDao;
import com.example.newbiechen.ireader.model.gen.BookHelpsBeanDao;
import com.example.newbiechen.ireader.model.gen.BookRecordBeanDao;
import com.example.newbiechen.ireader.model.gen.BookReviewBeanDao;
import com.example.newbiechen.ireader.model.gen.CollBookBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoMaster;
import com.example.newbiechen.ireader.model.gen.DownloadTaskBeanDao;
import com.example.newbiechen.ireader.model.gen.ReviewBookBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by newbiechen on 2017/10/9.
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper{
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //更新数据
        MigrationHelper.getInstance().migrate(db,AuthorBeanDao.class,
                BookChapterBeanDao.class,
                BookCommentBeanDao.class,
                BookHelpfulBeanDao.class,
                BookHelpsBeanDao.class,
                BookRecordBeanDao.class,
                BookReviewBeanDao.class,
                CollBookBeanDao.class,
                DownloadTaskBeanDao.class,
                ReviewBookBeanDao.class);
    }
}
