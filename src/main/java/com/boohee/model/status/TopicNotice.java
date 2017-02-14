package com.boohee.model.status;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class TopicNotice {
    public String category;
    public String icon_url;
    public int    id;
    public String title;
    public int    topic_id;
    public String url;

    public static List<TopicNotice> parseList(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return JSON.parseArray(str, TopicNotice.class);
    }
}
