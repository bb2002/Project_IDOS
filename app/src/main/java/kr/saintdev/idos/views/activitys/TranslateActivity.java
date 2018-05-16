package kr.saintdev.idos.views.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.fragments.translate.TranExecuteFragment;
import kr.saintdev.idos.views.fragments.translate.TranItemSelectFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-16
 */

public class TranslateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        showTranslatePage(null);
    }

    public void showTranslatePage(String data) {
        SuperFragment view;

        if(data == null) {
            // 첫 화면으로 이동합니다.
            view = new TranItemSelectFragment();
        } else {
            // 번역 화면으로 이동합니다.
            TranExecuteFragment v = new TranExecuteFragment();
            v.setData(data);
            view = v;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.translate_container, view);
        ft.commitAllowingStateLoss();
    }
}
