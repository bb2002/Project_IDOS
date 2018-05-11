package kr.saintdev.idos.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.recorder.RecordObject;
import kr.saintdev.idos.views.activitys.AudioRecordActivity;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class AudioRecordAdapter extends BaseAdapter {
    private ArrayList<RecordObject> items = null;

    public AudioRecordAdapter(ArrayList<RecordObject> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RecordObject getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            Context c = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_record_item, viewGroup, false);
        }

        TextView fileName = view.findViewById(R.id.record_item_name);
        TextView lengthView = view.findViewById(R.id.record_item_length);

        RecordObject recObj = items.get(i);
        fileName.setText(recObj.getRecordName());
        int length = recObj.getLength();


        int h, m, s;
        h = length / 3600;
        length %= 3600;
        m = length / 60;
        length %= 60;
        s = length;

        String format = String.format("%02d:%02d:%02d", h, m, s);
        lengthView.setText(format);

        return view;
    }
}
