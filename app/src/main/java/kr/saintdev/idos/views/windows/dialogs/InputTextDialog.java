package kr.saintdev.idos.views.windows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    public InputTextDialog(@NonNull Context context, String defaultVal) {
        super(context);
        this.defaultVal = defaultVal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_inputtext);

        this.editor = findViewById(R.id.dialog_input_editor);
        this.okButton = findViewById(R.id.dialog_input_ok);

        if(this.defaultVal != null) {
            this.editor.setText(this.defaultVal);
        }

        this.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editor.getText().length() == 0) {
                    Toast.makeText(getContext(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                }
            }
        });
    }

    public String getData() {
        return this.editor.getText().toString();
    }
}