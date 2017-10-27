package com.example.myapplication.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LiuB on 2017/10/27.
 */

public class Doghelper extends SQLiteOpenHelper {

    /**
     * 数据名字
     */
    private static final String DATABASE = "xjr.db";

    /**
     * 表名
     */
    public static final String TABLE_DOG = "dog";

    /**
     * 版本号
     */
    private static final int VERSION = 1;


    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DOG  +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT , year INTEGER , name TEXT)";

    public Doghelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
