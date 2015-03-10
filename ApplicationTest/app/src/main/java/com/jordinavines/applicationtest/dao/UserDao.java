package com.jordinavines.applicationtest.dao;

import android.database.Cursor;

import com.jordinavines.applicationtest.model.Name;
import com.jordinavines.applicationtest.model.User;

/**
 * Created by jordinavines on 05/03/2015.
 */
public class UserDao {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_USER = "id_user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ABOUT = "about";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_DEPARTMENT = "department";
    public static final String COLUMN_SUBORDINATES = "subordinates";

    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table "
            + TABLE_USERS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ID_USER + " integer, "
            + COLUMN_NAME + " text,"
            + COLUMN_ABOUT + " text,"
            + COLUMN_AVATAR + " text,"
            + COLUMN_ADDRESS + " text,"
            + COLUMN_EMAIL + " text,"
            + COLUMN_PHONE + " text,"
            + COLUMN_DOB + " text,"
            + COLUMN_DEPARTMENT + " text,"
            + COLUMN_SUBORDINATES + " text not null);";

    public static final String[] allColumns = {COLUMN_ID_USER, COLUMN_NAME, COLUMN_ABOUT, COLUMN_AVATAR, COLUMN_ADDRESS, COLUMN_EMAIL,COLUMN_PHONE ,COLUMN_DOB, COLUMN_DEPARTMENT, COLUMN_SUBORDINATES };
    public static final String allColumns_string = COLUMN_ID_USER+","+COLUMN_NAME+","+ COLUMN_ABOUT+","+ COLUMN_AVATAR+","+COLUMN_ADDRESS+","+ COLUMN_EMAIL+","+COLUMN_PHONE+","+COLUMN_DOB+","+COLUMN_DEPARTMENT+","+ COLUMN_SUBORDINATES ;

    public static User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        String[] names = cursor.getString(1).split("::");
        Name _name= new Name();
        _name.setFirst(names[0]);
        _name.setLast(names[1]);
        user.setName(_name);
        user.setAbout(cursor.getString(2));
        user.setAvatar(cursor.getString(3));
        user.setAddress(cursor.getString(4));
        user.setEmail(cursor.getString(5));
        user.setPhone(cursor.getString(6));
        user.setDob(cursor.getString(7));
        user.setDepartment(cursor.getString(8));
        return user;
    }



}
