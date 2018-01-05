package com.example.newbiechen.ireader.model.local.update;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.example.newbiechen.ireader.model.gen.BookChapterBeanDao;
import com.example.newbiechen.ireader.model.gen.CollBookBeanDao;
import com.example.newbiechen.ireader.utils.MD5Utils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by newbiechen on 2018/1/5.
 * 由于 BookChapterBean 做了一次表的大更改，所以需要自定义更新。
 * 作用：将数据库2.0 升级到 3.0
 */

public class Update2Helper {
    private static final String TAG = "BookChapterHelper";
    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";

    private static final String DIVIDER = ",";
    private static final String QUOTE = "'%s'";

    private static Update2Helper instance;

    public static Update2Helper getInstance() {
        if (instance == null) {
            instance = new Update2Helper();
        }
        return instance;
    }

    public void update(Database db) {
        updateCollBook(db);
        updateBookChapter(db);
    }

    private void updateBookChapter(Database db) {
        Class<? extends AbstractDao<?, ?>> bookChapterClass = BookChapterBeanDao.class;

        generateTempTables(db, bookChapterClass);
        deleteOriginalTables(db, bookChapterClass);
        createOrignalTables(db, bookChapterClass);
        restoreData(db, bookChapterClass);
    }

    private void updateCollBook(Database db) {
        Class<? extends AbstractDao<?, ?>> collBookClass = CollBookBeanDao.class;

        // 遍历查找本地文件，然后修改本地文件的数据
        DaoConfig daoConfig = new DaoConfig(db, collBookClass);
        String tableName = daoConfig.tablename;

        Cursor cursor = db.rawQuery("select _ID,IS_LOCAL from " + tableName, null);
        String id = null;
        String cover = null;
        String isLocal = null;

        StringBuilder updateSb = new StringBuilder();
        while (cursor.moveToNext()) {
            cover = cursor.getString(0);
            id = MD5Utils.strToMd5By16(cover);
            isLocal = cursor.getString(1);

            //如果是本地文件
            if (isLocal.equals("1")) {
                // 数据更新
                updateSb.append("UPDATE " + tableName + " SET ");
                updateSb.append("_ID=").append(String.format(QUOTE, id)).append(DIVIDER);
                updateSb.append("COVER=").append(String.format(QUOTE, cover)).append(" ");
                updateSb.append("WHERE _ID=").append(String.format(QUOTE,cover)).append(";");

                db.execSQL(updateSb.toString());
                updateSb.delete(0, updateSb.length());
            }
        }
    }

    /**
     * 生成临时列表
     *
     * @param db
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        // 解析 GreenDao，获取 table 名
        DaoConfig daoConfig = new DaoConfig(db, bookChapterClass);
        String tableName = daoConfig.tablename;

        // 创建临时 table 名。
        String tempTableName = daoConfig.tablename.concat("_TEMP");
        ArrayList<String> properties = new ArrayList<>();

        StringBuilder createTableStringBuilder = new StringBuilder();
        createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

        // 新增的三个字段
        String ID = "ID";
        String START = "START";
        String END = "end";


        // 新建的 id 主键字段
        createTableStringBuilder.append(ID + " ").append("TEXT ").append("PRIMARY KEY");
        properties.add(ID);

        // 获取符合新表的旧字段。
        for (int j = 0; j < daoConfig.properties.length; j++) {
            String columnName = daoConfig.properties[j].columnName;
            if (getColumns(db, tableName).contains(columnName)) {
                properties.add(columnName);

                String type = null;

                try {
                    type = getTypeByClass(daoConfig.properties[j].type);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                createTableStringBuilder.append(DIVIDER).append(columnName).append(" ").append(type);
            }
        }

        // 新建的 START，和 END 字段。
        createTableStringBuilder.append(DIVIDER).append(START).append(" ").append("INTEGER");
        createTableStringBuilder.append(DIVIDER).append(END).append(" ").append("INTEGER");

        properties.add(START);
        properties.add(END);

        createTableStringBuilder.append(");");
        // 创建临时数据表
        db.execSQL(createTableStringBuilder.toString());

        StringBuilder insertTableStringBuilder = new StringBuilder();

        // 将 link 字段的文件的内容转换成 Id
        Cursor cursor = db.rawQuery("select * from " + daoConfig.tablename, null);

        String id = null;
        String link = null;
        String title = null;
        String taskName = null;
        String unreadble = null;
        String bookId = null;

        while (cursor.moveToNext()) {
            link = cursor.getString(0);
            id = MD5Utils.strToMd5By16(link);
            title = cursor.getString(1);
            taskName = cursor.getString(2);
            unreadble = cursor.getString(4);
            bookId = cursor.getString(3);

            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") VALUES (");
            insertTableStringBuilder.append(String.format(QUOTE, id)).append(DIVIDER);
            insertTableStringBuilder.append(String.format(QUOTE, link)).append(DIVIDER);
            insertTableStringBuilder.append(String.format(QUOTE, title)).append(DIVIDER);
            insertTableStringBuilder.append(String.format(QUOTE, taskName)).append(DIVIDER);
            insertTableStringBuilder.append(unreadble).append(DIVIDER);
            insertTableStringBuilder.append(String.format(QUOTE, bookId)).append(DIVIDER);
            insertTableStringBuilder.append("0").append(DIVIDER);
            insertTableStringBuilder.append("0").append(");");

            db.execSQL(insertTableStringBuilder.toString());

            insertTableStringBuilder.delete(0, insertTableStringBuilder.length());
        }
    }


    /**
     * 通过反射，删除要更新的表
     */
    private void deleteOriginalTables(Database db,Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        try {
            Method method = bookChapterClass.getMethod("dropTable", Database.class, boolean.class);
            method.invoke(null, db, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射，重新创建要更新的表
     */
    private void createOrignalTables(Database db,Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        try {
            Method method = bookChapterClass.getMethod("createTable", Database.class, boolean.class);
            method.invoke(null, db, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储新的数据库表 以及数据
     *
     * @param db
     */
    private void restoreData(Database db,Class<? extends AbstractDao<?, ?>> bookChapterClass) {
        DaoConfig daoConfig = new DaoConfig(db, bookChapterClass);
        String tableName = daoConfig.tablename;
        String tempTableName = daoConfig.tablename.concat("_TEMP");
        ArrayList<String> properties = new ArrayList();

        for (int j = 0; j < daoConfig.properties.length; j++) {
            String columnName = daoConfig.properties[j].columnName;
            if (getColumns(db, tableName).contains(columnName)) {
                properties.add(columnName);
            }
        }

        StringBuilder insertTableStringBuilder = new StringBuilder();

        insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
        insertTableStringBuilder.append(TextUtils.join(",", properties));
        insertTableStringBuilder.append(") SELECT ");
        insertTableStringBuilder.append(TextUtils.join(",", properties));
        insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

        Log.d(TAG, "restoreData: " + insertTableStringBuilder.toString());

        StringBuilder dropTableStringBuilder = new StringBuilder();
        dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
        db.execSQL(insertTableStringBuilder.toString());
        db.execSQL(dropTableStringBuilder.toString());
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception = new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        exception.printStackTrace();
        throw exception;
    }

    private List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}
