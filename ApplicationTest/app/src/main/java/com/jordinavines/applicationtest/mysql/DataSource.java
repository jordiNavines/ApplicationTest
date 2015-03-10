package com.jordinavines.applicationtest.mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.jordinavines.applicationtest.dao.UserDao;
import com.jordinavines.applicationtest.dao.UserRelationDao;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.LogUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jordinavines on 06/03/2015.
 */
public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;


    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addUser(Context context, User user) {
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_ID_USER, user.getId());
        values.put(UserDao.COLUMN_ABOUT, user.getAbout());
        values.put(UserDao.COLUMN_AVATAR, user.getAvatar());
        values.put(UserDao.COLUMN_ADDRESS, user.getAddress());
        values.put(UserDao.COLUMN_DEPARTMENT, user.getDepartment());
        values.put(UserDao.COLUMN_DOB, user.getDob());
        values.put(UserDao.COLUMN_EMAIL, user.getEmail());
        values.put(UserDao.COLUMN_PHONE, user.getPhone());
        values.put(UserDao.COLUMN_NAME, user.getName().getFirst()+"::"+user.getName().getLast());
        values.put(UserDao.COLUMN_SUBORDINATES, user.getSubordinates().toString());

        Uri todoUri =  context.getContentResolver().insert(UserContentProvider.CONTENT_URI_USER, values);
        LogUtils.LOGD("user DB", "user added "+todoUri);
    }

    public void addSubordinates(Context context, int idBoss, int idSub) {
        ContentValues values = new ContentValues();
        values.put(UserRelationDao.COLUMN_ID_USER_BOSS, idBoss);
        values.put(UserRelationDao.COLUMN_ID_USER_SUBORDINATES, idSub);

        Uri todoUri =  context.getContentResolver().insert(UserContentProvider.CONTENT_URI_RELATIONS, values);
        LogUtils.LOGD("user DB", "user relation added " + todoUri);
    }

    public ArrayList<User> getAllUsers(Context context) {
        ArrayList<User> users = new ArrayList<User>();

        Uri userUri = Uri.parse(String.valueOf(UserContentProvider.CONTENT_URI_USER));
        Cursor cursor = context.getContentResolver().query(userUri, UserDao.allColumns, null, null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = UserDao.cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public User getUser(Context context, int id) {
        User user = null;
        LogUtils.LOGD("-->", "id"+id);
        Uri userUri = Uri.parse(UserContentProvider.CONTENT_URI_USER + "/" + id);
        Cursor cursor = context.getContentResolver().query(userUri, UserDao.allColumns, null, null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = UserDao.cursorToUser(cursor);
            LogUtils.LOGD("-->", user.toString());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return user;
    }

    public ArrayList<User> getSubordiantes(int id){
        ArrayList<User> users= new ArrayList<User>();

        final String MY_QUERY = "SELECT "+UserDao.allColumns_string+" FROM "+UserDao.TABLE_USERS+" user INNER JOIN "+
                UserRelationDao.TABLE_USERS_RELATIONS+" relations ON user."+UserDao.COLUMN_ID_USER+"=relations."+UserRelationDao.COLUMN_ID_USER_SUBORDINATES+" WHERE relations."+UserRelationDao.COLUMN_ID_USER_BOSS+"=?";

        Cursor cursor = database.rawQuery(MY_QUERY, new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = UserDao.cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public ArrayList<User> getBosses(int id){
        ArrayList<User> users= new ArrayList<User>();

        final String MY_QUERY = "SELECT "+UserDao.allColumns_string+" FROM "+UserDao.TABLE_USERS+" user INNER JOIN "+
                UserRelationDao.TABLE_USERS_RELATIONS+" relations ON user."+UserDao.COLUMN_ID_USER+"=relations."+UserRelationDao.COLUMN_ID_USER_BOSS+" WHERE relations."+UserRelationDao.COLUMN_ID_USER_SUBORDINATES+"=?";

        Cursor cursor = database.rawQuery(MY_QUERY, new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = UserDao.cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public boolean isUsersEmpty(Context context){
        Uri userUri = Uri.parse(String.valueOf(UserContentProvider.CONTENT_URI_USER));
        Cursor cursor = context.getContentResolver().query(userUri, UserDao.allColumns, null, null,
                null);

        boolean isEmpty= true;
        if (cursor!=null && cursor.getCount()>0){
            isEmpty= false;
        }else{
            isEmpty= true;
        }

        // make sure to close the cursor
        cursor.close();

        return isEmpty;
    }
}

