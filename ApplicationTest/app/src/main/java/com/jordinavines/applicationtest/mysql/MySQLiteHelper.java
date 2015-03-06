package com.jordinavines.applicationtest.mysql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jordinavines.applicationtest.dao.UserDao;

/**
 * Created by jordinavines on 06/03/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "users_db.db";
    private static final int DATABASE_VERSION = 1;




    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(UserDao.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + UserDao.TABLE_USERS);
        onCreate(db);
    }

}
