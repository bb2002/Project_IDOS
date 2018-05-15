package kr.saintdev.idos.models.tasks.tts;

import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import kr.saintdev.idos.models.constants.APIConstant;
import kr.saintdev.idos.models.tasks.BackgroundWork;
import kr.saintdev.idos.models.tasks.OnBackgroundWorkListener;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-05-15
 */

public class TTS extends BackgroundWork<TTSObject> {
    String sentence = null;
    String fileName = null;

    public TTS(String sentence, @Nullable String fileName, int requestCode, OnBackgroundWorkListener listener) {
        super(requestCode, listener);
        this.sentence = sentence;

        if(fileName == null) {
            this.fileName = Long.valueOf(new Date().getTime()).toString();
        } else {
            this.fileName = fileName + System.currentTimeMillis();
        }
        this.fileName += ".mp3";
    }

    @Override
    protected TTSObject script() throws Exception {
        String text = URLEncoder.encode(this.sentence, "UTF-8"); // 13자
        String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", APIConstant.NCLOUD_ID);
        con.setRequestProperty("X-NCP-APIGW-API-KEY", APIConstant.NCLOUD_SECRET);

        // post request
        String postParams = "speaker=mijin&speed=0&text=" + text;
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // TTS 변환에 성공함.
            InputStream is = con.getInputStream();
            int read = 0;
            byte[] bytes = new byte[1024];

            // 임의의 이름으로 저장한다.
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".mp3");

            if(f.createNewFile()){
                // 파일 생성 성공
                // mp3 파일을 받아옵니다.

                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();

                TTSObject ttsObj = new TTSObject(this.sentence, f.getAbsoluteFile());
                return ttsObj;
            } else {
                // 파일 생성 실패
                throw new Exception("Cannot create file!");
            }

        } else {  // 에러 발생
            throw new Exception("Socket error!");
        }
    }
}
