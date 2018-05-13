package kr.saintdev.idos.views.fragments.convert;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.converter.ConvertedObject;
import kr.saintdev.idos.models.lib.converter.ConverterDB;
import kr.saintdev.idos.views.activitys.AudioRecordActivity;
import kr.saintdev.idos.views.activitys.ConvertActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.windows.dialogs.InputTextDialog;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-12
 */

public class STTFragment extends SuperFragment {
    private TextView sttResultView = null;
    private Button startButton = null;
    private Button saveButton = null;
    private String resultText = "";
    private InputTextDialog textDialog = null;

    private ConvertActivity control = null;
    private ConverterDB dbManager = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_convert_stt, container, false);
        this.control = (ConvertActivity) getActivity();

        this.sttResultView = v.findViewById(R.id.convert_stt_view);
        this.startButton = v.findViewById(R.id.convert_stt_start);
        this.saveButton = v.findViewById(R.id.convert_stt_save);
        this.dbManager = new ConverterDB(control);
        this.textDialog = new InputTextDialog(control, null);
        this.textDialog.setTitle("제목 입력");
        this.textDialog.setOnDismissListener(new OnDialogDismissHandler());

        OnButtonClickHandler handler = new OnButtonClickHandler();
        this.startButton.setOnClickListener(handler);
        this.saveButton.setOnClickListener(handler);

        return v;
    }

    class OnButtonClickHandler implements View.OnClickListener {
        private Intent recoIntent = null;
        private SpeechRecognizer recognizer = null;

        public OnButtonClickHandler() {
            recoIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recoIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, control.getPackageName());
            recoIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.convert_stt_start) {
                // stt 로 문장을 입력함
                this.recognizer = SpeechRecognizer.createSpeechRecognizer(control);
                this.recognizer.setRecognitionListener(new OnRecognitionListener());
                this.recognizer.startListening(recoIntent);
                startButton.setEnabled(false);
            } else {
                // 이 문장을 저장합니다.
                if (resultText.length() == 0) {
                    Toast.makeText(control, "입력된 문장이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    textDialog.setCancelable(false);
                    textDialog.show();
                    this.recognizer.destroy();
                    startButton.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    class OnDialogDismissHandler implements Dialog.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            String title = textDialog.getData();

            ConvertedObject obj = new ConvertedObject(0, title, resultText, null);
            dbManager.addConvertedText(obj);

            control.finish();
            Toast.makeText(control, "저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    class OnRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            Toast.makeText(control, "Error : " + i, Toast.LENGTH_SHORT).show();
            startButton.setEnabled(true);
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            if(mResult.size() != 0) {
                String msg = mResult.get(0);
                resultText += msg + "\n";
                sttResultView.setText(resultText);
            } else {
                Toast.makeText(control, "인식된 단어를 없습니다.", Toast.LENGTH_SHORT).show();
            }

            startButton.setEnabled(true);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
}
