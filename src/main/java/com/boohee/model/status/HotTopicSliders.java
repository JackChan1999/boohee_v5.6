package com.boohee.model.status;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class HotTopicSliders {
    public String pic_url;
    public String title;
    public String url;

    public static HotTopicSliders parseSelf(String object) {
        if (object == null) {
            return null;
        }
        return (HotTopicSliders) JSON.parseObject(object, HotTopicSliders.class);
    }

    public static List<HotTopicSliders> parseList(String object) {
        if (TextUtils.isEmpty(object)) {
            return null;
        }
        return JSON.parseArray(object, HotTopicSliders.class);
    }
}
