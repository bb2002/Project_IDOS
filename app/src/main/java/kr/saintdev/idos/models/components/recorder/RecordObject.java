package kr.saintdev.idos.models.components.recorder;

import java.io.File;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-11
 */

public class RecordObject {
    private String recordName = null;   // 사용자에게 표시될 이름
    private File filePath = null;       // 실제 음성이 저장된 위치
    private int length = 0;             // 음성 파일의 길이
    private String created = null;      // 생성일

    public RecordObject(String recordName, File filePath, int length) {
        this.recordName = recordName;
        this.filePath = filePath;
        this.length = length;
    }

    public String getRecordName() {
        return recordName;
    }

    public File getFilePath() {
        return filePath;
    }

    public int getLength() {
        return length;
    }

    public String getCreated() {
        return created;
    }
}
