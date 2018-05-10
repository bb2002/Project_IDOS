package kr.saintdev.idos.views.fragments.record;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.activitys.RecordActivity;
import kr.saintdev.idos.views.dialogs.main.DialogManager;
import kr.saintdev.idos.views.dialogs.main.InputTextDialog;
import kr.saintdev.idos.views.fragments.SuperFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-10
 */

public class RecordVoiceFragment extends SuperFragment {
    RecordActivity control = null;

    ImageButton switchButton = null;
    TextView statusView = null;
    OnSwitchButtonClickHandler handler = null;
    DialogManager dm = null;
    InputTextDialog nameWriteDialog = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_record_record, container, false);
        this.control = (RecordActivity) getActivity();

        this.switchButton = v.findViewById(R.id.record_switch);
        this.statusView = v.findViewById(R.id.record_status);
        this.dm = new DialogManager(control);

        Calendar nowDate = Calendar.getInstance();
        this.nameWriteDialog = new InputTextDialog(control,
                nowDate.get(Calendar.YEAR) + "_" + (nowDate.get(Calendar.MONTH)+1) + "_" + nowDate.get(Calendar.DAY_OF_MONTH) +
        nowDate.get(Calendar.HOUR_OF_DAY) + "_" +  nowDate.get(Calendar.MINUTE) + nowDate.get(Calendar.SECOND));
        this.nameWriteDialog.setCancelable(false);
        this.nameWriteDialog.setTitle("파일 이름");

        this.handler = new OnSwitchButtonClickHandler();
        this.switchButton.setOnClickListener(handler);
        this.nameWriteDialog.setOnDismissListener(handler);

        // 권한을 확인합니다.
        String[] needPermissions = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(ContextCompat.checkSelfPermission(control, needPermissions[0]) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(control, needPermissions[1]) == PackageManager.PERMISSION_DENIED) {
            // 권한을 요청합니다.
            ActivityCompat.requestPermissions(control, needPermissions, 0x0);
        }

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode != 0x0) return;

        if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            // 권한이 거부됨.
            dm.setTitle("권한 오류");
            dm.setDescription("일부 권한이 제한되었습니다.\n녹음기를 사용 할 수 없습니다.");
            dm.show();
        }
    }

    class OnSwitchButtonClickHandler implements View.OnClickListener, Dialog.OnDismissListener {
        boolean isRecording = false;
        File recordFile = null;
        MediaRecorder recorder = null;

        @Override
        public void onClick(View v) {
            if(isRecording) {
                stopRecord();   // 녹음을 중지합니다.
                statusView.setText("녹음이 완료되었습니다.");

                nameWriteDialog.show();
                isRecording = false;
            } else {
                startRecord(); // 녹음을 시작합니다.
                statusView.setText("녹음중 입니다.");
                isRecording = true;

                switchButton.setImageResource(R.drawable.ic_ok);
            }
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            String name = nameWriteDialog.getData();

            Toast.makeText(getContext(), recordFile.getAbsolutePath()+"/"+name, Toast.LENGTH_SHORT).show();
        }

        private void startRecord() {
            this.recorder = new MediaRecorder();
            this.recordFile = Environment.getExternalStorageDirectory();
            String path = recordFile.getAbsolutePath() + "/" + System.currentTimeMillis() + ".3gp";

            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(path);
                recorder.prepare();
                recorder.start();
            } catch(IOException iex) {
                dm.setTitle("Fatal error");
                dm.setDescription("An error occurred\n" + iex.getMessage());
                dm.show();

                iex.printStackTrace();
            }
        }

        private void stopRecord() {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
}
