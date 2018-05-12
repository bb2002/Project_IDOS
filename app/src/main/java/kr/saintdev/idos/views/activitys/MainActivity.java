package kr.saintdev.idos.views.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import kr.saintdev.idos.R;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class MainActivity extends AppCompatActivity {
    MainActivity me = null;

    ImageButton[] appOptions = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.me = this;
        this.appOptions = new ImageButton[] {
                findViewById(R.id.main_option_rec),
                findViewById(R.id.main_option_play),
                findViewById(R.id.main_option_stt),
                findViewById(R.id.main_option_tts),
                findViewById(R.id.main_option_translate),
                findViewById(R.id.main_option_settings)
        };

        OnButtonClickHandler handler = new OnButtonClickHandler();
        for(ImageButton b : appOptions) {
            b.setOnClickListener(handler);
        }
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.main_option_rec:
                    Intent record = new Intent(me, AudioRecordActivity.class);
                    record.putExtra("index", 0);
                    startActivity(record);
                    break;
                case R.id.main_option_play:
                    Intent recordPlay = new Intent(me, AudioRecordActivity.class);
                    recordPlay.putExtra("index", 1);
                    startActivity(recordPlay);
                    break;
                case R.id.main_option_stt:
                    Intent sttConvert = new Intent(me, ConvertActivity.class);
                    sttConvert.putExtra("index", 0);
                    startActivity(sttConvert);
                    break;
                case R.id.main_option_tts:
                    Intent ttsConvert = new Intent(me, ConvertActivity.class);
                    ttsConvert.putExtra("index", 1);
                    startActivity(ttsConvert);
                    break;
                case R.id.main_option_translate:
                    break;
                case R.id.main_option_settings:
                    break;
            }
        }
    }
}
