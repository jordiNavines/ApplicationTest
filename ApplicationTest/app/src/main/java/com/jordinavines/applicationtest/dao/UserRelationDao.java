package com.jordinavines.applicationtest.dao;

/**
 * Created by jordinavines on 05/03/2015.
 */
public class UserRelationDao {

    public static final String TABLE_USERS_RELATIONS = "relation_users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_USER_BOSS = "id_user_boss";
    public static final String COLUMN_ID_USER_SUBORDINATES = "id_user_subordinate";


    // Database creation sql statement
    public static final String DATABASE_CREATE = "create table "
            + TABLE_USERS_RELATIONS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ID_USER_BOSS + " integer, "
            + COLUMN_ID_USER_SUBORDINATES + " integer);";

    public static final String[] allColumns = {COLUMN_ID_USER_BOSS, COLUMN_ID_USER_SUBORDINATES};


}
