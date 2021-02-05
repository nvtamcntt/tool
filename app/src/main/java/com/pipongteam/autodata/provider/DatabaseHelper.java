package com.pipongteam.autodata.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "COUNTRIES";

    // Table columns
    public static final String _ID = "_id";
    public static final String POINT_NUMBER = "point_number";
    public static final String SECURITY_NUMBER = "security_number";
    public static final String IS_REGISTED = "is_registed";
    public static final String DATE_OF_BIRTH_YEAR = "date_of_birth_year";
    public static final String DATE_OF_BIRTH_MOUTH = "date_of_birth_mouth";
    public static final String ZIP_CODE = "zip_code";
    public static final String GENDER = "gender";
    public static final String SHOP = "shop";
    public static final String SHOP_AREA = "shop_area";
    public static final String SHOP_CITY = "shop_city";

    // Database Information
    static final String DB_NAME = "tool.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + POINT_NUMBER + " TEXT NOT NULL, "
            + IS_REGISTED + " INTEGER DEFAULT 0, "
            + GENDER + " INTEGER DEFAULT 0, "
            + ZIP_CODE + " TEXT, "
            + DATE_OF_BIRTH_MOUTH + " INTEGER, "
            + DATE_OF_BIRTH_YEAR + " INTEGER, "
            + SHOP_AREA + " TEXT, "
            + SHOP_CITY + " TEXT, "
            + SHOP + " TEXT, "
            + SECURITY_NUMBER + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}