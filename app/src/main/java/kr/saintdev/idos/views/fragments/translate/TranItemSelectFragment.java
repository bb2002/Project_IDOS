package kr.saintdev.idos.views.fragments.translate;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.converter.ConvertedObject;
import kr.saintdev.idos.models.lib.converter.ConverterDB;
import kr.saintdev.idos.models.tasks.tts.TTS;
import kr.saintdev.idos.views.activitys.TranslateActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.fragments.convert.TTSFragment;
import kr.saintdev.idos.views.windows.dialogs.DialogManager;
import kr.saintdev.idos.views.windows.dialogs.clicklistener.OnYesClickListener;
import kr.saintdev.idos.views.windows.progress.ProgressManager;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-16
 */

public class TranItemSelectFragment extends SuperFragment {
    TranslateActivity control = null;

    ListView tranList = null;
    ConverterDB dbManager = null;
    ArrayList<ConvertedObject> convertedObjects = null;
    ProgressManager pm = null;
    DialogManager dm = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_tran_list, container, false);
        this.control = (TranslateActivity) getActivity();

        this.tranList = v.findViewById(R.id.translate_list);
        this.tranList.setOnItemClickListener(new OnItemClickHandler());
        this.dbManager = new ConverterDB(control);
        this.pm = new ProgressManager(control);
        this.dm = new DialogManager(control);

        this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, "OK");
        this.pm.setMessage("Loading ...");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.convertedObjects = dbManager.getConvertedText();

        ArrayAdapter adapter = new ArrayAdapter(control, android.R.layout.simple_list_item_1);
        for(ConvertedObject o : this.convertedObjects) {
            adapter.add(o.getTitle());
        }

        tranList.setAdapter(adapter);
    }


    class OnItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ConvertedObject convObj = convertedObjects.get(i);

            control.showTranslatePage(convObj.getData());
        }
    }
}
