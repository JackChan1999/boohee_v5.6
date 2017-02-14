package com.boohee.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.util.List;

import org.json.JSONObject;

public class FastJsonUtils {
    public static <T> T fromJson(JSONObject object, Class<T> clazz) {
        T t = null;
        if (object != null) {
            try {
                t = JSON.parseObject(object.toString(), (Class) clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static <T> T fromJson(JSONObject object, String key, Class<T> clazz) {
        if (object.has(key)) {
            return fromJson(object.optJSONObject(key), (Class) clazz);
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return JSON.parseObject(jsonStr, (Class) clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> clazz) {
        try {
            return JSON.parseObject(jsonStr, (TypeReference) clazz, new Feature[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> clazz) {
        try {
            return JSON.parseArray(jsonStr, (Class) clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
