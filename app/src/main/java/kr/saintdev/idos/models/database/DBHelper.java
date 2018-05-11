package kr.saintdev.idos.models.database;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 5252b on 2018-05-11.
 */

public class DBHelper extends SQLiteOpenHelper {
    Context context = null;
    SQLiteDatabase readDB = null;
    SQLiteDatabase writeDB = null;

    public DBHelper(Context context) {
        super(context, "project_idos", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 녹음 데이터를 저장하는 테이블을 만든다.
        db.execSQL(SQLQuerys.RECORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void open() {
        // read 와 write 할 수 있는 db 객체를 가져옵니다.
        this.readDB = getReadableDatabase();
        this.writeDB = getWritableDatabase();
    }

    public Cursor sendReadableQuery(String query) {
        return this.readDB.rawQuery(query, null);
    }

    public void sendWriteableQuery(String query) {
        this.writeDB.execSQL(query);
    }

    public Context getContext() {
        return context;
    }

    public SQLiteDatabase getReadDB() {
        return readDB;
    }

    public SQLiteDatabase getWriteDB() {
        return writeDB;
    }

    interface SQLQuerys {
        String RECORDER_TABLE = "CREATE TABLE idos_record_db (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "record_name TEXT NOT NULL," +
                "record_path TEXT NOT NULL," +
                "record_length INTEGER NOT NULL," +
                "created DATETIME DEFAULT (datetime('now', 'localhost'))" +
                ")";

    }
}