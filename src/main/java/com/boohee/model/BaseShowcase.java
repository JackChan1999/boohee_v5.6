package com.boohee.model;

import java.util.List;

public class BaseShowcase {
    public List<Object> list;
    public String       title;
    public String       type;

    public enum TYPE_SHOWCASE {
        homepage_showcase,
        homepage_goods
    }
}
