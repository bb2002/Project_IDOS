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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.components.recorder.RecordObject;
import kr.saintdev.idos.models.components.recorder.RecorderDB;
import kr.saintdev.idos.models.components.recorder.RecorderManager;
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
    DialogManager dm = null;
    InputTextDialog nameWriteDialog = null;

    RecorderManager recoManager = null;
    String recordFilePath = null;   // 녹음된 파일의 경로
    int recordDuration = 0;         // 녹음된 파일의 길이

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

        this.switchButton.setOnClickListener(new OnButtonClickHandler());
        this.nameWriteDialog.setOnDismissListener(new OnDialogDismissHandler());
        this.recoManager = new RecorderManager(control);

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RecorderManager.PERMISSION_REQUEST) {
            if(!recoManager.checkPermission()) {
                dm.setTitle("권한 오류");
                dm.setDescription("일부 권한이 제한되었습니다.\n녹음기를 사용 할 수 없습니다.");
                dm.show();
            }
        }
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("IDOS", "되라고 좀좀");

            if(recoManager.isRecording()) {
                // 녹음중이라면, 녹음을 중지합니다.
                recoManager.stopRecord();

                statusView.setText("녹음이 완료되었습니다.");
                switchButton.setImageResource(R.drawable.ic_ok);
                switchButton.setEnabled(false);

                // 이름 입력창을 띄웁니다.
                nameWriteDialog.show();
            } else {
                if(recoManager.startRecord()) {
                    statusView.setText("녹음 중 입니다.");
                } else {
                    statusView.setText("녹음기를 실행 할 수 없습니다.");
                }
            }
        }
    }

    class OnDialogDismissHandler implements Dialog.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            String name = nameWriteDialog.getData();

            RecordObject recoObj = new RecordObject(name, new File(recordFilePath), recordDuration);
            RecorderDB recoDB = recoManager.getRecorderDB();
            recoDB.addRecordObject(recoObj);

            recoManager.release();
            Toast.makeText(getContext(), name + " 으로 저장되었습니다.", Toast.LENGTH_SHORT).show();

            control.finish();
        }
    }
}
