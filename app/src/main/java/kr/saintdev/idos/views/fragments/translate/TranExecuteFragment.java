package kr.saintdev.idos.views.fragments.translate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

        this.translateEditor.setText(data);

        // 번역기를 실행합니다.
        refresh();

        return v;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void refresh() {
        Translater worker = new Translater(data, "ko", "en", 0x0, new OnBackgroundWorkHandler());
        worker.execute();
    }

    class OnBackgroundWorkHandler implements OnBackgroundWorkListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            TranslateObject obj = (TranslateObject) worker.getResult();

            if(obj == null) {
                dm.setTitle("오류");
                dm.setDescription("번역에 실패했습니다!");
                dm.show();
            } else {
                resultView.setText(obj.getSentence());
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {

        }
    }
}
