package kr.saintdev.idos.views.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.windows.dialogs.InputTextDialog;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_search:
                openSearchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    InputTextDialog searchDialog = null;
    private void openSearchDialog() {
        if(this.searchDialog == null) {
            this.searchDialog = new InputTextDialog(this, "");
            this.searchDialog.setOnDismissListener(new OnSearchDialogCloseHandler());
            this.searchDialog.setTitle("검색");
        }

        this.searchDialog.show();
    }

    class OnSearchDialogCloseHandler implements Dialog.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            String search = searchDialog.getData();

            if(search != null && search.length() != 0) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                searchActivity.putExtra("q", search);
                startActivity(searchActivity);
            }
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
                    Intent transalte = new Intent(me, TranslateActivity.class);
                    startActivity(transalte);
                    break;
                case R.id.main_option_settings:
                    Intent setting = new Intent(me, SettingsActivity.class);
                    startActivity(setting);
                    break;
            }
        }
    }
}
