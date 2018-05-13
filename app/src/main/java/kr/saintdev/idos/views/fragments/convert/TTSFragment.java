package kr.saintdev.idos.views.fragments.convert;

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
import kr.saintdev.idos.views.activitys.ConvertActivity;
import kr.saintdev.idos.views.activitys.MainActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-12
 */

public class TTSFragment extends SuperFragment {
    ConvertActivity control = null;

    ListView ttsList = null;
    ConverterDB dbManager = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_convert_tts, container, false);
        this.control = (ConvertActivity) getActivity();

        this.ttsList = v.findViewById(R.id.convert_tts_list);
        this.ttsList.setOnItemClickListener(new OnItemClickHandler());
        this.dbManager = new ConverterDB(control);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<ConvertedObject> convertedObjects = dbManager.getConvertedText();

        ArrayAdapter adapter = new ArrayAdapter(control, android.R.layout.simple_list_item_1);
        for(ConvertedObject o : convertedObjects) {
            adapter.add(o.getTitle());
        }

        ttsList.setAdapter(adapter);
    }

    class OnItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }
}
