package kr.saintdev.idos.models.lib.converter;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @date 2018-05-13
 */

public class ConvertedObject {
    private int id = 0;
    private String data = null;
    private String title = null;
    private String created = null;

    public ConvertedObject(int id, String title, String data, String created) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }
}
