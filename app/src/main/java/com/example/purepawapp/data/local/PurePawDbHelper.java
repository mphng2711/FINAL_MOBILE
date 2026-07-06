package com.example.purepawapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.purepawapp.util.SqliteConfig;

public class PurePawDbHelper extends SQLiteOpenHelper {

    public PurePawDbHelper(Context context) {
        super(context, SqliteConfig.DATABASE_NAME, null, SqliteConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(ProductCacheEntry.CREATE_TABLE);
        db.execSQL(CategoryCacheEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductCacheEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryCacheEntry.TABLE_NAME);
        onCreate(db);
    }
}
