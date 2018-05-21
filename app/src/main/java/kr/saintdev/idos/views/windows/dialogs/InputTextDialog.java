package kr.saintdev.idos.views.windows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kr.saintdev.idos.R;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-10
 */

public class InputTextDialog extends Dialog {
    String defaultVal = null;

    TextView editor = null;
    Button okButton = null;
    Button onCancel = null;

    String data = null;

    public InputTextDialog(@NonNull Context context, String defaultVal) {
        super(context);

        if(defaultVal == null) {
            Calendar nowDate = Calendar.getInstance();
            this.defaultVal = nowDate.get(Calendar.YEAR) + "_" + (nowDate.get(Calendar.MONTH)+1) + "_" + nowDate.get(Calendar.DAY_OF_MONTH) +
                    nowDate.get(Calendar.HOUR_OF_DAY) + "_" +  nowDate.get(Calendar.MINUTE) + nowDate.get(Calendar.SECOND);
        } else {
            this.defaultVal = defaultVal;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_inputtext);

        this.editor = findViewById(R.id.dialog_input_editor);
        this.okButton = findViewById(R.id.dialog_input_ok);
        this.onCancel = findViewById(R.id.dialog_input_cancel);

        if(this.defaultVal != null) {
            this.editor.setText(this.defaultVal);
        }

        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editor.getText().toString();

                if(text.length() == 0) {
                    data = null;
                } else {
                    data = text;
                }

                dismiss();
            }
        });

        this.onCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = null;
                dismiss();
            }
        });
    }

    public String getData() {
        return this.data;
    }
}