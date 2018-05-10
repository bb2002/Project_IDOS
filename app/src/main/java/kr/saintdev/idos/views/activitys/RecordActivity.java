package kr.saintdev.idos.views.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.fragments.record.RecordPlayFragment;
import kr.saintdev.idos.views.fragments.record.RecordVoiceFragment;

public class RecordActivity extends AppCompatActivity {
    SuperFragment nowView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        int idx = intent.getIntExtra("index", 0);
        switchFragment(idx);
    }

    public void switchFragment(int idx) {
        SuperFragment view = null;

        if(idx == 1) {
            view = new RecordVoiceFragment();
        } else {
            view = new RecordPlayFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.record_container, view);
        ft.commit();

        this.nowView = view;
    }

    public void setActionBarTitle(@Nullable  String title) {
        ActionBar bar = getSupportActionBar();

        if(title == null) {
            bar.hide();
        } else {
            bar.show();
            bar.setTitle(title);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nowView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
