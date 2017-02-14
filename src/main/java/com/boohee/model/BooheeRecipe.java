package com.boohee.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

public class BooheeRecipe extends ModelBase {
    public Data   data;
    public String type;

    public class Data {
        public String    ext;
        public Materials materials;
    }

    public static BooheeRecipe parse(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        BooheeRecipe result = null;
        try {
            return (BooheeRecipe) JSON.parseObject(json, BooheeRecipe.class);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
