package com.boohee.model;

public class QiniuPhoto {
    public static final String TYPE_BOOHEE = "Photo::Boohee";
    public static final String TYPE_QINIU  = "Photo::Qiniu";
    public              String _type       = TYPE_QINIU;
    public int    origin_height;
    public int    origin_width;
    public int    preview_height;
    public int    preview_width;
    public String qiniu_hash;
    public String qiniu_key;

    public QiniuPhoto(String key, int origin_width, int origin_height, int preview_width, int
            preview_height) {
        this.qiniu_key = key;
        this.origin_width = origin_width;
        this.origin_height = origin_height;
        this.preview_width = preview_width;
        this.preview_height = preview_height;
    }

    public QiniuPhoto(String key, String hash, int origin_width, int origin_height) {
        this.qiniu_key = key;
        this.qiniu_hash = hash;
        this.origin_width = origin_width;
        this.origin_height = origin_height;
    }

    public QiniuPhoto(String key, String hash) {
        this.qiniu_key = key;
        this.qiniu_hash = hash;
    }

    public String get_type() {
        return this._type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }
}
