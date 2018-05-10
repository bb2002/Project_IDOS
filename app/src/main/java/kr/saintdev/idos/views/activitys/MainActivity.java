package kr.saintdev.idos.views.activitys;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.fragments.SuperFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void switchFragment(SuperFragment view) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_container, view);
        ft.commit();
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
}
