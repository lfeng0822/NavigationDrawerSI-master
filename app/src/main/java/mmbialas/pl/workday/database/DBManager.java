package mmbialas.pl.workday.database;

/**
 * Created by liangfeng on 2016/3/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        Log.d("DBManager", "DBManager --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     *
     * @param accounts
     */
    public void add(List<Account> accounts) {
        Log.d("DBManager", "DBManager --> add");
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {
            for (Account account : accounts) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?)", new Object[]{account.date,
                        account.info});
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }


    public boolean isAccountExist(Account account) {
        Cursor cursor = null;
        boolean isExist = false;
        int accountId = account._id;
        String sql = "";
        //String sql = select count( *)as c from sqlite_master where type = 'table' and name = '+tabName.trim()+';
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return isExist;
    }


    public void add(Account account) {
        Log.d("DBManager", "DBManager --> add");
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {

            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                    + " VALUES( ?, ?, ?)", new Object[]{account._id, account.date,
                    account.info});
            // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
            // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
            // 使用占位符有效区分了这种情况
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }


    /**
     * update person's age
     *
     * @param person
     */
//    public void updateInfo(Account account)
//    {
//        Log.d("DBManager", "DBManager --> updateAge");
//        ContentValues cv = new ContentValues();
//        cv.put("age", account.info);
//        db.update(DatabaseHelper.TABLE_NAME, cv, "name = ?",
//                new String[] { account.info });
//    }

    /**
     * delete old person
     *
     * @param person
     */
//    public void deleteOldPerson(Account person)
//    {
//        Log.d("DBManager", "DBManager --> deleteOldPerson");
//        db.delete(DatabaseHelper.TABLE_NAME, "age >= ?",
//                new String[] { String.valueOf(person.age) });
//    }


    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<Account> query() {
        Log.d("DBManager", "DBManager --> query");
        ArrayList<Account> persons = new ArrayList<Account>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Account account = new Account();
            account._id = c.getInt(c.getColumnIndex("_id"));
            account.date = c.getString(c.getColumnIndex("date"));
            account.info = c.getString(c.getColumnIndex("info"));
            persons.add(account);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Log.d("DBManager", "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        Log.d("DBManager", "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }

}
