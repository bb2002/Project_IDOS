package kr.saintdev.idos.views.fragments.recorder;

import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.recorder.RecordObject;
import kr.saintdev.idos.models.lib.recorder.RecorderDB;
import kr.saintdev.idos.views.activitys.AudioRecordActivity;
import kr.saintdev.idos.views.adapters.AudioRecordAdapter;
import kr.saintdev.idos.views.fragments.SuperFragment;
import nl.changer.audiowife.AudioWife;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class AudioRecordPlayFragment extends SuperFragment {
    ListView recordFiles = null;

    AudioRecordActivity control = null;
    RecorderDB recDB = null;
    AudioRecordAdapter adapter = null;
    RelativeLayout playerLayout = null;

    AudioWife audioPlayer = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_recorder_play, container, false);
        this.control = (AudioRecordActivity) getActivity();

        this.recordFiles = v.findViewById(R.id.recorder_play_list);
        this.recDB = new RecorderDB(control);
        this.recordFiles.setOnItemClickListener(new OnItemClickHandler());
        this.playerLayout = v.findViewById(R.id.recorder_player);

        this.audioPlayer = AudioWife.getInstance();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<RecordObject> items = this.recDB.getRecordObject();
        this.adapter = new AudioRecordAdapter(items);
        this.recordFiles.setAdapter(adapter);
    }

    class OnItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            RecordObject recObj = adapter.getItem(i);
            File file = recObj.getFilePath();

            if(!file.exists()) {
                Toast.makeText(control, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri uri = Uri.fromFile(recObj.getFilePath());

            try {
                audioPlayer.init(control, uri);
                audioPlayer.useDefaultUi(playerLayout, getLayoutInflater());
            } catch(Exception ex) {
                Toast.makeText(control, "오디오 플레이어를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    }
}
