package kr.saintdev.idos.views.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.fragments.convert.STTFragment;
import kr.saintdev.idos.views.fragments.convert.TTSFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-12
 */

public class ConvertActivity extends AppCompatActivity {
    SuperFragment nowView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int idx = intent.getIntExtra("index", 0);
        switchFragment(idx);
    }

    public void switchFragment(int idx) {
        SuperFragment view;
        if(idx == 0) {
            // 0 은 STT 화면으로 이동시킵니다.
            view = new STTFragment();
        } else {
            // 1 은 녹음된 파일을 재생합니다.
            view = new TTSFragment();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.convert_container, view);
        ft.commitAllowingStateLoss();

        this.nowView = view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length == 0 || grantResults.length == 0) return;

        nowView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
