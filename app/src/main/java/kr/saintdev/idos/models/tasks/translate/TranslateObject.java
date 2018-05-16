package kr.saintdev.idos.models.tasks.translate;

import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-16
 */

public class TranslateObject {
    private String sentence = null;
    private JSONObject resultObj = null;

    private boolean isErrorOccurred = false;
    private String errorMessage = null;

    public TranslateObject(String json) {
        try {
            this.resultObj = new JSONObject(json);

            if(this.resultObj.isNull("message")) {
                // 번역 오류
                isErrorOccurred = true;
                this.errorMessage = this.resultObj.getString("errorMessage");
            } else {
                // 번역 성공
                JSONObject message = this.resultObj.getJSONObject("message");
                JSONObject result = message.getJSONObject("result");

                this.sentence = result.getString("translatedText");
                this.sentence = URLDecoder.decode(this.sentence, "UTF-8");
            }
        } catch(Exception jex) {
            this.isErrorOccurred = true;
            errorMessage = jex.getMessage();
        }
    }

    public String getSentence() {
        return sentence;
    }

    public boolean isErrorOccurred() {
        return isErrorOccurred;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

