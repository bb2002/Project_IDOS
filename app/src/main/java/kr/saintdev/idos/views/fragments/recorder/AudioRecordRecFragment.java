package kr.saintdev.idos.views.fragments.recorder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.recorder.RecordObject;
import kr.saintdev.idos.models.lib.recorder.RecorderDB;
import kr.saintdev.idos.models.lib.recorder.RecorderManager;
import kr.saintdev.idos.views.activitys.AudioRecordActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.windows.dialogs.DialogManager;
import kr.saintdev.idos.views.windows.dialogs.InputTextDialog;
import kr.saintdev.idos.views.windows.dialogs.clicklistener.OnYesClickListener;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class AudioRecordRecFragment extends SuperFragment {
    ImageButton recorderPower = null;
    TextView statusView = null;
    DialogManager dm = null;
    InputTextDialog textDialog = null;

    RecorderManager recManager = null;
    AudioRecordActivity control = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_recorder_rec, container, false);
        this.control = (AudioRecordActivity) getActivity();

        this.recorderPower = v.findViewById(R.id.recorder_rec_switcher);
        this.statusView = v.findViewById(R.id.recorder_res_status);
        this.dm = new DialogManager(control);
        this.textDialog = new InputTextDialog(control, null);
        this.recorderPower.setOnClickListener(new OnButtonClickHandler());
        this.recManager = new RecorderManager(control);

        recManager = new RecorderManager(control);
        if(!recManager.checkPermission()) {
            recManager.obtainPermission(control);
        }


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RecorderManager.PERMISSION_REQUEST) {
            if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                control.finish();
            }
        }
    }

    class OnButtonClickHandler implements View.OnClickListener, Dialog.OnDismissListener {
        int length = 0;

        @Override
        public void onClick(View view) {
            // 녹음기를 켜거나 끕니다.
            if(recManager.isRecording()) {
                length = recManager.stopRecord();
                statusView.setText("녹음이 완료되었습니다.");

                textDialog.show();
                textDialog.setOnDismissListener(this);
            } else {
                recManager.startRecord();
                statusView.setText("녹음 중 입니다 ...");
            }
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            dialogInterface.dismiss();
            String name = textDialog.getData();

            RecordObject recObj = new RecordObject(name, recManager.getSaveFolder().getAbsoluteFile(), length);
            RecorderDB db = recManager.getRecorderDB();
            db.addRecordObject(recObj);

            recManager.release();
            Toast.makeText(control, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            control.finish();
        }
    }
}
