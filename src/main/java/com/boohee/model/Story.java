package com.boohee.model;

public class Story {
    public String image;
    public String slug;
    public String title;
    public String url;

    public Story(String slug, String title, String url, String image) {
        this.slug = slug;
        this.title = title;
        this.url = url;
        this.image = image;
    }
}
