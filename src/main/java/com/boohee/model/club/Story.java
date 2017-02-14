package com.boohee.model.club;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Story {
    public String pic;
    public String title;
    public String url;

    public static List<Story> parseStory(String src) {
        List<Story> result = null;
        try {
            result = JSON.parseArray(src, Story.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
