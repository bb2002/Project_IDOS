package kr.saintdev.idos.views.fragments.convert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

import kr.saintdev.idos.R;
import kr.saintdev.idos.models.lib.converter.ConvertedObject;
import kr.saintdev.idos.models.lib.converter.ConverterDB;
import kr.saintdev.idos.models.tasks.BackgroundWork;
import kr.saintdev.idos.models.tasks.OnBackgroundWorkListener;
import kr.saintdev.idos.models.tasks.tts.TTS;
import kr.saintdev.idos.models.tasks.tts.TTSObject;
import kr.saintdev.idos.views.activitys.ConvertActivity;
import kr.saintdev.idos.views.activitys.MainActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;
import kr.saintdev.idos.views.windows.dialogs.DialogManager;
import kr.saintdev.idos.views.windows.dialogs.InputTextDialog;
import kr.saintdev.idos.views.windows.dialogs.clicklistener.OnYesClickListener;
import kr.saintdev.idos.views.windows.progress.ProgressManager;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-12
 */

public class TTSFragment extends SuperFragment {
    ConvertActivity control = null;

    ListView ttsList = null;
    ConverterDB dbManager = null;
    ArrayList<ConvertedObject> convertedObjects = null;
    ProgressManager pm = null;
    DialogManager dm = null;

    MediaPlayer player = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_convert_tts, container, false);
        this.control = (ConvertActivity) getActivity();

        this.ttsList = v.findViewById(R.id.convert_tts_list);
        this.ttsList.setOnItemClickListener(new OnItemClickHandler());
        this.ttsList.setOnItemLongClickListener(new OnItemLongClickHandler());

        this.dbManager = new ConverterDB(control);
        this.pm = new ProgressManager(control);
        this.dm = new DialogManager(control);
        this.player = new MediaPlayer();

        this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.player.setOnCompletionListener(new OnPlayCompleteHandler());
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
        reload();
    }

    private void reload() {
        this.convertedObjects = dbManager.getConvertedText();

        ArrayAdapter adapter = new ArrayAdapter(control, android.R.layout.simple_list_item_1);
        for(ConvertedObject o : this.convertedObjects) {
            adapter.add(o.getTitle());
        }

        ttsList.setAdapter(adapter);
    }



    @Override
    public void onStop() {
        super.onStop();

        if(this.player != null) {
            try {
                this.player.stop();
                this.player.release();
            } catch (Exception ex){}
        }
    }

    class OnItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
            String[] items = new String[]{ "남자", "여자" };

            AlertDialog.Builder builder = new AlertDialog.Builder(control);
            builder.setTitle("보컬로이드 선택");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ConvertedObject convObj = convertedObjects.get(i);
                    pm.enable();

                    TTS tts = new TTS(convObj.getData(), convObj.getTitle(), 0x0, new OnBackgroundWorkHandler());
                    tts.setVocal(which);
                    tts.execute();
                }
            });

            builder.show();
        }
    }

    class OnItemLongClickHandler implements AdapterView.OnItemLongClickListener, Dialog.OnDismissListener {
        private int targetItem = 0;
        private InputTextDialog dialog = null;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            String[] items = {"수정", "삭제", "작업 없음"};

            this.targetItem = position;

            AlertDialog.Builder builder = new AlertDialog.Builder(control);
            builder.setTitle("메뉴 선택");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:     // 수정 버튼 클릭 시
                            ConvertedObject convObj = convertedObjects.get(position);
                            update(convObj.getTitle());
                            break;
                        case 1:     // 삭제 버튼 클릭 시
                            remove(position);
                            break;
                        case 2:     // 취소 버튼 클릭 시
                            break;
                    }
                }
            });

            builder.show();
            return true;
        }

        private void update(String defaultVal) {
            this.dialog = new InputTextDialog(control, defaultVal);
            this.dialog.show();
            this.dialog.setOnDismissListener(this);
        }

        private void remove(int pos) {
            ConvertedObject convObj = convertedObjects.get(pos);
            dbManager.removeItem(convObj.getId());
            reload();
        }

        @Override
        public void onDismiss(DialogInterface a) {
            // 데이터를 업데이트 한다.
            ConvertedObject convObj = convertedObjects.get(targetItem);

            String title = dialog.getData();
            if(title != null) {
                dbManager.updateTitle(convObj.getId(), title);
                reload();
            }
        }
    }

    class OnBackgroundWorkHandler implements OnBackgroundWorkListener {
        @Override
        public void onSuccess(int requestCode, BackgroundWork worker) {
            pm.disable();

            if(requestCode == 0x0) {
                // TTS 랜더링에 성공했습니다.
                TTSObject ttsObj = (TTSObject) worker.getResult();

                try {
                    player.reset();
                    player.setDataSource(ttsObj.getMp3File().getAbsolutePath());
                    player.prepare();
                    player.start();

                    ttsList.setEnabled(false);
                } catch(Exception ex) {
                    dm.setTitle("Player error");
                    dm.setDescription("An error occurred.\n" + ex.getMessage());
                    dm.show();

                    ex.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int requestCode, Exception ex) {
            pm.disable();

            dm.setTitle("TTS API Error");
            dm.setDescription("An error occurred.\n" + ex.getMessage());
            dm.show();
        }
    }

    class OnPlayCompleteHandler implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            ttsList.setEnabled(true);
        }
    }
}
