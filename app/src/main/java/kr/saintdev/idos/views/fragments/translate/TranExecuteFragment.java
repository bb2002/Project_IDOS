package kr.saintdev.idos.views.fragments.translate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.tasks.BackgroundWork;
import kr.saintdev.idos.models.tasks.OnBackgroundWorkListener;
import kr.saintdev.idos.models.tasks.translate.TranslateObject;
import kr.saintdev.idos.models.tasks.translate.Translater;
import kr.saintdev.idos.models.tasks.tts.TTSObject;
import kr.saintdev.idos.views.activitys.TranslateActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.windows.dialogs.DialogManager;
import kr.saintdev.idos.views.windows.dialogs.clicklistener.OnYesClickListener;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-16
 */

public class TranExecuteFragment extends SuperFragment {
    String data = null;
    TranslateActivity control = null;

    EditText translateEditor = null;    // 번역기 수정 창
    TextView resultView = null;         // 번역 결과 뷰어
    Spinner inputLanguageSpinner = null;        // Input 언어
    Spinner outputLanguageSpinner = null;       // Output 언어

    String[] languageViews = null;      // 언어 선택 (뷰)
    String[] languageCodes = null;      // 언어 선택 (코드)
    ArrayAdapter languageAdapter = null;    // input 언어 아답터

    DialogManager dm = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_tran_execute, container, false);
        this.control = (TranslateActivity) getActivity();

        this.translateEditor = v.findViewById(R.id.tran_text_editor);
        this.resultView = v.findViewById(R.id.tran_result_view);
        this.dm = new DialogManager(control);
        this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, "OK");
        this.inputLanguageSpinner = v.findViewById(R.id.tran_input_language);
        this.outputLanguageSpinner = v.findViewById(R.id.tran_output_language);

        this.translateEditor.setText(data);
        this.translateEditor.addTextChangedListener(new OnTextChangeHandler());

        this.languageCodes = getResources().getStringArray(R.array.translate_language_code);
        this.languageViews = getResources().getStringArray(R.array.translate_language_view);
        this.languageAdapter =
                ArrayAdapter.createFromResource(control, R.array.translate_language_view, android.R.layout.simple_list_item_1);

        this.outputLanguageSpinner.setAdapter(this.languageAdapter);
        this.inputLanguageSpinner.setAdapter(this.languageAdapter);

        this.inputLanguageSpinner.setSelection(0);      // 기본값 : 한국어
        this.outputLanguageSpinner.setSelection(1);     // 기본값 : 영어

        OnSpinnerChangeHandler spinnerHandler = new OnSpinnerChangeHandler();
        this.inputLanguageSpinner.setOnItemSelectedListener(spinnerHandler);
        this.outputLanguageSpinner.setOnItemSelectedListener(spinnerHandler);

        // 번역기를 실행합니다.
        refresh();

        return v;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void refresh() {
        resultView.setText("번역 중 입니다...");

        String inputSrc = languageCodes[inputLanguageSpinner.getSelectedItemPosition()];
        String outputSrc = languageCodes[outputLanguageSpinner.getSelectedItemPosition()];

        Translater worker = new Translater(data, inputSrc, outputSrc, 0x0, new OnBackgroundWorkHandler());
        worker.execute();
    }

    @Override
    public void onBackPressed() {
        control.showTranslatePage(null);
    }

    class OnBackgroundWorkHandler implements OnBackgroundWorkListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            TranslateObject obj = (TranslateObject) worker.getResult();

            if(obj == null) {
                dm.setTitle("오류");
                dm.setDescription("번역에 실패했습니다!");
                dm.show();

                resultView.setText("다른 언어를 선택하거나 문장 길이를 줄여보세요.");
            } else {
                resultView.setText(obj.getSentence());
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {

        }
    }

    class OnSpinnerChangeHandler implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refresh();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class OnTextChangeHandler implements TextWatcher {
        Timer timer = null;
        RefreshHandler handler = null;

        public OnTextChangeHandler() {
            this.handler = new RefreshHandler();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(timer == null || timer.getState() == Thread.State.TERMINATED) {
                timer = new Timer(this.handler);
                timer.start();
            } else {
                timer.reset();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        class RefreshHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                refresh();
            }
        }

        class Timer extends Thread {
            int time = 3;
            Handler handler = null;

            public Timer(Handler handler) {
                this.handler = handler;
            }

            public void reset() {
                time = 3;
            }

            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        time --;
                    } catch (Exception ex) {
                        return;
                    }

                    if (time == 0) {
                        handler.sendEmptyMessage(0);
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
