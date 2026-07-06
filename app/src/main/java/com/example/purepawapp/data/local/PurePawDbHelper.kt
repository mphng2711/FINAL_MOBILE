package com.example.purepawapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.purepawapp.util.SqliteConfig

class PurePawDbHelper(context: Context) :
    SQLiteOpenHelper(context, SqliteConfig.DATABASE_NAME, null, SqliteConfig.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ProductCacheEntry.CREATE_TABLE)
        db.execSQL(CategoryCacheEntry.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${ProductCacheEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${CategoryCacheEntry.TABLE_NAME}")
        onCreate(db)
    }
}
