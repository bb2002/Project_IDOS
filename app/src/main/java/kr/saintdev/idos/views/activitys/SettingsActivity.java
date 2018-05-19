package kr.saintdev.idos.views.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.fragments.settings.SettingsFragment;
import kr.saintdev.idos.views.windows.dialogs.VolumeDialog;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
