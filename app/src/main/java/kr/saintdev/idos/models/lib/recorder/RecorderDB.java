package kr.saintdev.idos.models.lib.recorder;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.util.ArrayList;

import kr.saintdev.idos.models.database.DBHelper;


/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class RecorderDB {
    private Context context = null;
    private DBHelper dbHelper = null;

    public RecorderDB(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.dbHelper.open();
    }

    public void addRecordObject(RecordObject recObj) {
        SQLiteStatement pst =
                dbHelper.getWriteDB().compileStatement("INSERT INTO `idos_record_db` (record_name, record_path, record_length) VALUES(?,?,?)");
        pst.bindString(1, recObj.getRecordName());
        pst.bindString(2, recObj.getFilePath().getPath());
        pst.bindLong(3, recObj.getLength());
        pst.execute();
    }

    public ArrayList<RecordObject> getRecordObject() {
        String sql = "SELECT * FROM idos_record_db ORDER BY _id DESC";
        Cursor cs = dbHelper.sendReadableQuery(sql);

        ArrayList<RecordObject> arrayObj = new ArrayList<>();

        while(cs.moveToNext()) {
            String filePath = cs.getString(1);
            File realPath = new File(cs.getString(2));
            int length = cs.getInt(3);

            RecordObject tmp = new RecordObject(filePath, realPath, length);
            arrayObj.add(tmp);
        }

        return arrayObj;
    }
}
