package kr.saintdev.idos.models.lib.converter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.util.ArrayList;

import kr.saintdev.idos.models.database.DBHelper;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-13
 */

public class ConverterDB {
    private DBHelper dbHelper = null;

    public ConverterDB(Context context) {
        this.dbHelper = new DBHelper(context);
        this.dbHelper.open();
    }


    public void addConvertedText(ConvertedObject convertedObj) {
        SQLiteStatement pst =
                dbHelper.getWriteDB().compileStatement("INSERT INTO `idos_convert_db` (convert_title, convert_text) VALUES(?, ?)");
        pst.bindString(1, convertedObj.getTitle());
        pst.bindString(2, convertedObj.getData());
        pst.execute();
    }

    public ArrayList<ConvertedObject> getConvertedText() {
        String sql = "SELECT * FROM idos_convert_db ORDER BY _id DESC";
        Cursor cs = dbHelper.sendReadableQuery(sql);

        ArrayList<ConvertedObject> arrayObj = new ArrayList<>();

        while(cs.moveToNext()) {
            int id = cs.getInt(0);
            String title = cs.getString(1);
            String data = cs.getString(2);
            String created = cs.getString(3);

            ConvertedObject obj = new ConvertedObject(id, title, data, created);
            arrayObj.add(obj);
        }

        return arrayObj;
    }

    public void updateTitle(int id, String title) {
        SQLiteStatement pst =
                dbHelper.getWriteDB().compileStatement("UPDATE idos_convert_db SET convert_title = ? WHERE _id = ?");
        pst.bindString(1, title);
        pst.bindLong(2, id);
        pst.execute();
    }

    public void removeItem(int id) {
        String sql = "DELETE FROM idos_convert_db WHERE _id = " + id;
        dbHelper.sendWriteableQuery(sql);
    }
}
