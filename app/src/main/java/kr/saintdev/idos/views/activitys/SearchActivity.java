package kr.saintdev.idos.views.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.database.DBHelper;
import kr.saintdev.idos.models.lib.converter.ConvertedObject;
import kr.saintdev.idos.models.lib.converter.ConverterDB;
import kr.saintdev.idos.models.lib.recorder.RecordObject;
import kr.saintdev.idos.models.lib.recorder.RecorderDB;

public class SearchActivity extends AppCompatActivity {
    ListView recordFileList = null;
    ListView sttFileList = null;
    TextView searchItem = null;

    String word = null;

    DBHelper dbHelper = null;
    ArrayList<ConvertedObject> searchResultSTT = new ArrayList<>();
    ArrayList<RecordObject> searchResultRecord = new ArrayList<>();

    ArrayAdapter sttListAdapter = null;
    ArrayAdapter recordListAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.recordFileList = findViewById(R.id.search_record_list);
        this.sttFileList = findViewById(R.id.search_stt_list);
        this.searchItem = findViewById(R.id.search_wordview);
        this.dbHelper = new DBHelper(this);
        this.dbHelper.open();

        Intent intent = getIntent();
        this.word = intent.getStringExtra("q");
        this.searchItem.setText(this.word + " 에 대한 검색 결과");

        this.sttListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        this.recordListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        this.recordFileList.setAdapter(this.recordListAdapter);
        this.sttFileList.setAdapter(this.sttListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 검색 쿼리를 실행합니다.
        String recordSQL = "SELECT * FROM `idos_record_db` WHERE record_name LIKE '%"+this.word+"%'";
        String ttsSQL = "SELECT * FROM `idos_convert_db` WHERE convert_title LIKE '%"+this.word+"%'";

        Cursor cs = this.dbHelper.sendReadableQuery(recordSQL);
        while(cs.moveToNext()) {        // 음성 녹음 된 파일을 찾아옵니다.
            String filePath = cs.getString(1);
            File realPath = new File(cs.getString(2));
            int length = cs.getInt(3);

            RecordObject tmp = new RecordObject(filePath, realPath, length);
            searchResultRecord.add(tmp);
        }
        cs.close();

        cs = this.dbHelper.sendReadableQuery(ttsSQL);
        while(cs.moveToNext()) {        // STT 를 통해 변환된 텍스트 파일을 검색합니다.
            int id = cs.getInt(0);
            String title = cs.getString(1);
            String data = cs.getString(2);
            String created = cs.getString(3);

            ConvertedObject obj = new ConvertedObject(id, title, data, created);
            searchResultSTT.add(obj);
        }

        // Adapter 데이터를 정의합니다.
        this.recordListAdapter.clear();
        this.sttListAdapter.clear();

        for(RecordObject rec : searchResultRecord) this.recordListAdapter.add(rec.getRecordName());
        for(ConvertedObject cv : searchResultSTT) this.sttListAdapter.add(cv.getTitle());
        this.recordListAdapter.notifyDataSetChanged();
        this.sttListAdapter.notifyDataSetChanged();
    }
}
