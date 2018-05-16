package kr.saintdev.idos.models.tasks.translate;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import kr.saintdev.idos.models.constants.APIConstant;
import kr.saintdev.idos.models.tasks.BackgroundWork;
import kr.saintdev.idos.models.tasks.OnBackgroundWorkListener;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-16
 */

public class Translater extends BackgroundWork<TranslateObject> {
    private String text = null;
    private String input = null;
    private String output = null;

    public Translater(String text, String input, String output, int requestCode, OnBackgroundWorkListener listener) {
        super(requestCode, listener);

        this.text = text;
        this.output = output;
        this.input = input;
    }

    @Override
    protected TranslateObject script() throws Exception {
        // 번역할 문자열을 URLEncode 를 합니다.
        String text = URLEncoder.encode(this.text, "UTF-8");

        URL url = new URL(APIConstant.TRANSLATE_API);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", APIConstant.NCLOUD_ID);
        con.setRequestProperty("X-NCP-APIGW-API-KEY", APIConstant.NCLOUD_SECRET);

        // POST 으로 요청을 보냅니다.
        String postParams = "source="+this.input+"&target="+this.output+"&text=" + text;
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader br = null;
        if(responseCode != 200) { // 요청 실패
            return null;
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        String translatedText = response.toString();
        Log.d("IDOS", "번역됨 : " + translatedText);

        TranslateObject tranObj = new TranslateObject(this.text, translatedText, this.output);
        return tranObj;
    }
}
