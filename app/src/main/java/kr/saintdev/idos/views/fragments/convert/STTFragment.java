package kr.saintdev.idos.views.fragments.convert;

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
import kr.saintdev.idos.views.activitys.AudioRecordActivity;
import kr.saintdev.idos.views.activitys.ConvertActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-12
 */

public class STTFragment extends SuperFragment {
    private TextView sttResultView = null;
    private Button startButton = null;
    private String resultText = null;

    private ConvertActivity control = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_convert_stt, container, false);
        this.control = (ConvertActivity) getActivity();

        this.sttResultView = v.findViewById(R.id.convert_stt_view);
        this.startButton = v.findViewById(R.id.convert_stt_start);
        this.startButton.setOnClickListener(new OnButtonClickHandler());

        return v;
    }

    class OnButtonClickHandler implements View.OnClickListener {
        private Intent recoIntent = null;
        private SpeechRecognizer recognizer = null;

        public OnButtonClickHandler() {
            recoIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recoIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, control.getPackageName());
            recoIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP");
        }

        @Override
        public void onClick(View view) {
            this.recognizer = SpeechRecognizer.createSpeechRecognizer(control);
            this.recognizer.setRecognitionListener(new OnRecognitionListener());
            this.recognizer.startListening(recoIntent);
            startButton.setEnabled(false);
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

        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            if(mResult.size() != 0) {
                String msg = mResult.get(0);
                resultText += "\n" + msg;
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
