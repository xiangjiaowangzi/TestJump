package com.example.myapplication.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.myapplication.LogUtils;


/**
 * Created by LiuB on 2017/10/27.
 */

public class DogProvider extends ContentProvider {

    public static final Uri URI = Uri.parse("content://xjr.de.dog/dog");

    private Context mContext;
    Doghelper mDbHelper = null;
    SQLiteDatabase db = null;

    public static final int DOG = 1;

    /**
     * 标识
     */
    public static final String AUTOHORITY = "xjr.de.dog";

    /**
     * UriMatcher类使用:在ContentProvider 中注册URI
     */
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 初始化
        // 若URI资源路径 = content://xjr.de.dog/dog ，则返回注册码DOG
        mMatcher.addURI(AUTOHORITY, "dog", DOG);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new Doghelper(mContext);
        db = mDbHelper.getWritableDatabase(); // 写入
        // 初始化(先清空表,再各加入一个记录)
        db.execSQL("delete from dog");
        db.execSQL("insert into dog values(1,'3岁',\"金毛\");");
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        LogUtils.log("insert : " + uri);
        String tableName = getTableName(uri);
        db.insert(tableName, null, values);
        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
//        LogUtils.log("query : " + uri);
        String tableName = getTableName(uri);
        Cursor cursor = db.query(tableName,null,
                null,null,null,null,null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    public String getTableName(Uri uri) {
        switch (mMatcher.match(uri)) {
            case DOG:
                return Doghelper.TABLE_DOG;
        }
        return "";
    }
}
