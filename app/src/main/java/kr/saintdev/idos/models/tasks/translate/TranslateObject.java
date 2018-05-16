package kr.saintdev.idos.models.tasks.translate;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-16
 */

public class TranslateObject {
    private String realText = null;
    private String translateText = null;
    private String output = null;

    public TranslateObject(String realText, String translateText, String output) {
        this.realText = realText;
        this.translateText = translateText;
        this.output = output;
    }

    public String getRealText() {
        return realText;
    }

    public String getTranslateText() {
        return translateText;
    }

    public String getOutput() {
        return output;
    }
}
