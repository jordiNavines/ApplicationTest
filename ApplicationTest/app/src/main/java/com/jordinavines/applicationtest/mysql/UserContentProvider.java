package com.jordinavines.applicationtest.mysql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.jordinavines.applicationtest.dao.UserDao;
import com.jordinavines.applicationtest.dao.UserRelationDao;

/**
 * Created by jordinavines on 06/03/2015.
 */
public class UserContentProvider extends ContentProvider {

    // database
    private MySQLiteHelper database;

    // used for the UriMacher
    private static final int USERS = 10;
    private static final int USER_ID = 20;
    private static final int RELATIONS = 30;

    private static final String AUTHORITY = "com.jordinavines.applicationtest.contentprovider";

    public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY + "/users" );
    public static final Uri CONTENT_URI_RELATIONS = Uri.parse("content://" + AUTHORITY+ "/relations");

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, "users", USERS);
        sURIMatcher.addURI(AUTHORITY, "users/#", USER_ID);
        sURIMatcher.addURI(AUTHORITY, "relations", RELATIONS);
    }

    @Override
    public boolean onCreate() {
        database = new MySQLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table
        queryBuilder.setTables(UserDao.TABLE_USERS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case USERS:
                break;
            case USER_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(UserDao.COLUMN_ID_USER + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case USERS:
                id = sqlDB.insert(UserDao.TABLE_USERS, null, values);
                break;
            case RELATIONS:
                id = sqlDB.insert(UserRelationDao.TABLE_USERS_RELATIONS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(itemUri, null);
        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case USERS:
                rowsDeleted = sqlDB.delete(UserDao.TABLE_USERS, selection,
                        selectionArgs);
                break;
            case USER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(UserDao.TABLE_USERS,
                            UserDao.COLUMN_ID_USER + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(UserDao.TABLE_USERS,
                            UserDao.COLUMN_ID_USER + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case USERS:
                rowsUpdated = sqlDB.update(UserDao.TABLE_USERS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(UserDao.TABLE_USERS,
                            values,
                            UserDao.COLUMN_ID_USER + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(UserDao.TABLE_USERS,
                            values,
                            UserDao.COLUMN_ID_USER + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }



}
