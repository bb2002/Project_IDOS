package kr.saintdev.idos.views.fragments.recorder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcPlayerView;
import com.example.jean.jcplayer.JcStatus;

import java.io.File;
import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.recorder.RecordObject;
import kr.saintdev.idos.models.lib.recorder.RecorderDB;
import kr.saintdev.idos.views.activitys.AudioRecordActivity;
import kr.saintdev.idos.views.adapters.AudioRecordAdapter;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.windows.dialogs.InputTextDialog;

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

    JcPlayerView audioPlayer = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_recorder_play, container, false);
        this.control = (AudioRecordActivity) getActivity();

        this.recordFiles = v.findViewById(R.id.recorder_play_list);
        this.recDB = new RecorderDB(control);
        this.recordFiles.setOnItemClickListener(new OnItemClickHandler());
        this.recordFiles.setOnItemLongClickListener(new OnItemLongClickHandler());

        // Audio player 를 가져옵니다.
        this.audioPlayer = v.findViewById(R.id.recorder_player);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        reload();
    }

    private void reload() {
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

            final JcAudio jcAudio = JcAudio.createFromFilePath(recObj.getRecordName(), recObj.getFilePath().getAbsolutePath());
            audioPlayer.playAudio(jcAudio);
            audioPlayer.registerStatusListener(new JcPlayerView.JcPlayerViewStatusListener() {
                @Override
                public void onPausedStatus(JcStatus jcStatus) {

                }

                @Override
                public void onContinueAudioStatus(JcStatus jcStatus) {

                }

                @Override
                public void onPlayingStatus(JcStatus jcStatus) {

                }

                @Override
                public void onTimeChangedStatus(JcStatus jcStatus) {

                }

                @Override
                public void onCompletedAudioStatus(JcStatus jcStatus) {
                    // 파일 재생이 완료되었다.
                    audioPlayer.removeAudio(jcAudio);
                    audioPlayer.kill();
                }

                @Override
                public void onPreparedAudioStatus(JcStatus jcStatus) {

                }
            });
        }
    }

    class OnItemLongClickHandler implements AdapterView.OnItemLongClickListener, Dialog.OnDismissListener {
        private RecordObject targetObject = null;
        private InputTextDialog dialog = null;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            String[] items = {"수정", "삭제", "작업 없음"};

            AlertDialog.Builder builder = new AlertDialog.Builder(control);
            builder.setTitle("메뉴 선택");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    targetObject = adapter.getItem(position);

                    switch (which) {
                        case 0:     // 수정 버튼 클릭 시
                            update(targetObject.getRecordName());
                            break;
                        case 1:     // 삭제 버튼 클릭 시
                            recDB.removeRecordObj(targetObject);
                            reload();
                            break;
                        case 2:     // 취소 버튼 클릭 시
                            break;
                    }
                }
            });

            builder.show();
            return true;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            String title = this.dialog.getData();

            if(title != null) {
                recDB.updateRecordObj(targetObject, title);
            }
            reload();

        }

        private void update(String defValue) {
            this.dialog = new InputTextDialog(control, defValue);
            this.dialog.show();
            this.dialog.setOnDismissListener(this);
        }
    }
}
