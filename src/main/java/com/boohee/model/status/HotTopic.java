package com.boohee.model.status;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class HotTopic {
    public String desc;
    public String pic_url;
    public String title;
    public String url;

    public static HotTopic parseSelf(String object) {
        if (object == null) {
            return null;
        }
        return (HotTopic) JSON.parseObject(object, HotTopic.class);
    }

    public static List<HotTopic> parseList(String object) {
        if (TextUtils.isEmpty(object)) {
            return null;
        }
        return JSON.parseArray(object, HotTopic.class);
    }
}
