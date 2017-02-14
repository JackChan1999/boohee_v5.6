package com.boohee.model;

import java.util.Date;

public class SearchHistory extends ModelBase {
    public Date   created_at;
    public String name;

    public SearchHistory(String name, Date created_at) {
        this.name = name;
        this.created_at = created_at;
    }
}
