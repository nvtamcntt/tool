package com.pipongteam.autodata.provider;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pipongteam.autodata.entity.Info;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(ContentValues contentValues) {
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public Info getItemByCondition(String point, String security) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper._ID, DatabaseHelper.POINT_NUMBER, DatabaseHelper.SECURITY_NUMBER, DatabaseHelper.IS_REGISTED},
                "security_number like " + "'%" + security + "%'" + " and point_number like " + "'%" + point + "%'", null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Info info = new Info(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SECURITY_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.POINT_NUMBER)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_REGISTED)));
                return info;
            }
        }
        return null;
    }

    public Info getItemByID(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.POINT_NUMBER, DatabaseHelper.SECURITY_NUMBER, DatabaseHelper.IS_REGISTED},
                "_id = " + id, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Info info = new Info(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SECURITY_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.POINT_NUMBER)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_REGISTED)));
                return info;
            }
        }
        return null;
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.POINT_NUMBER, DatabaseHelper.SECURITY_NUMBER, DatabaseHelper.IS_REGISTED};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, ContentValues contentValues) {
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}