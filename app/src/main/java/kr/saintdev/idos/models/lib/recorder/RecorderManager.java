package kr.saintdev.idos.models.lib.recorder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class RecorderManager {
    private static RecorderManager recoderManager = null;           // 녹음기 메니저
    private static final String[] NEED_PERMISSIONS = new String[]{  // 이 녹음기에서 필요한 권한
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int PERMISSION_REQUEST = 0x100;       // 권한 요청 ID

    private MediaRecorder recorder = null;          // 녹음기
    private File saveFolder = null;                 // 저장 폴더
    private Context context = null;
    private RecorderDB recorderDB = null;           // 녹음 데이터 저장 관리자
    private boolean isRecording = false;            // 현재 녹음중 인가?

    public static RecorderManager getInstance(Context context) {
        if(recoderManager == null) {
            recoderManager = new RecorderManager(context);
        }

        return recoderManager;
    }

    public RecorderManager(Context context) {
        this.recorder = new MediaRecorder();
        this.recorderDB = new RecorderDB(context);
        this.context = context;
    }

    /**
     * Record Permiss 을 확인합니다.
     */
    public boolean checkPermission() {
        boolean isWritePermission =
                ContextCompat.checkSelfPermission(context, NEED_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
        boolean isRecordPermission =
                ContextCompat.checkSelfPermission(context, NEED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED;

        if(isWritePermission && isRecordPermission) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permission 을 obtain 합니다.
     */
    public void obtainPermission(Activity activity) {
        if(checkPermission()) return;
        ActivityCompat.requestPermissions(activity, NEED_PERMISSIONS, PERMISSION_REQUEST);
    }

    private long recordingStart = 0;
    public boolean startRecord() {
        if(!isRecording) {
            String fileName = System.currentTimeMillis() + ".3gp";
            this.saveFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), fileName);

            if (this.recorder == null) {
                this.recorder = new MediaRecorder();
            }

            Log.d("IDOS", this.saveFolder.getAbsolutePath());

            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
                recorder.setOutputFile(this.saveFolder.getAbsolutePath());
                recorder.prepare();
                recorder.start();

                recordingStart = System.currentTimeMillis();
                this.isRecording = true;
                return true;
            } catch (IOException iex) {
                iex.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public int stopRecord() {
        int duration = (int) ((System.currentTimeMillis() - recordingStart) / 1000);
        isRecording = false;
        recorder.stop();

        // 녹음된 길이
        return duration;
    }

    public void release() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
        } catch(Exception ex){}
    }

    public RecorderDB getRecorderDB() {
        return recorderDB;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public File getSaveFolder() {
        return saveFolder;
    }
}
