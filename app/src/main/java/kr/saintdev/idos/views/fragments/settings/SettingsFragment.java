package kr.saintdev.idos.views.fragments.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.windows.dialogs.VolumeDialog;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);

        Preference volSetting = (Preference) findPreference("set_volume");
        volSetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                VolumeDialog dialog = new VolumeDialog(getActivity());
                dialog.show();

                return true;
            }
        });
    }
}
