package com.boohee.model;

public class Showcase {
    public String category_label;
    public String default_photo_height;
    public String default_photo_url;
    public String default_photo_width;
    public String exhibit;
    public String exhibit_type;
    public int    id;
    public String page_title;

    public enum ExhibitType {
        category_label,
        topic_label,
        goods,
        page
    }
}
