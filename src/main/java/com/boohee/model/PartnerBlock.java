package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PartnerBlock {
    public static final String TYPE_POINT  = "point";
    public static final String TYPE_RIBBON = "ribbon";
    public static final String TYPE_TEXT   = "text";
    public String  data;
    public String  data_type;
    public String  desc;
    public int     id;
    public boolean is_new;
    public String  link_to;
    public String  pic_url;
    public String  title;

    public static List<PartnerBlock> parseBlocks(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<PartnerBlock>>() {
        }.getType());
    }
}
