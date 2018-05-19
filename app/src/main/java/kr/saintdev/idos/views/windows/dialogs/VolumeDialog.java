package kr.saintdev.idos.views.windows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.SeekBar;

import kr.saintdev.idos.R;

import static android.content.Context.AUDIO_SERVICE;

public class VolumeDialog extends Dialog {
    AudioManager am = null;
    SeekBar controlBar = null;

    public VolumeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);

        this.am = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
        this.controlBar = findViewById(R.id.settings_volume);

        this.controlBar.setMax(this.am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));  // 미디어의 최대 볼륨 크기
        this.controlBar.setProgress(this.am.getStreamVolume(AudioManager.STREAM_MUSIC));// 현재 볼륨 크기
        this.controlBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
