package com.zxinsight.common.util;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class l {
    public static <T> boolean a(T t) {
        if (t == null) {
            return true;
        }
        if (t instanceof List) {
            if (((List) t).size() == 0) {
                return true;
            }
        } else if (t instanceof Map) {
            if (((Map) t).size() == 0) {
                return true;
            }
        } else if (t instanceof JSONObject) {
            if (((JSONObject) t).length() == 0) {
                return true;
            }
        } else if (t instanceof Object[]) {
            if (((Object[]) t).length == 0) {
                return true;
            }
        } else if (t instanceof String) {
            int length = ((String) t).length();
            if (length == 0) {
                return true;
            }
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(((String) t).charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static <T> boolean b(T t) {
        return !a(t);
    }
}
