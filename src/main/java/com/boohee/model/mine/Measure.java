package com.boohee.model.mine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class Measure extends BaseRecord {
    private static final long serialVersionUID = -3522980763547257404L;
    public String code;
    public float  value;

    public enum MeasureType {
        WEIGHT("体重", "weight", 0),
        WAIST("腰围", "waist", 1),
        BRASS("胸围", "brass", 2),
        HIP("臀围", "hip", 3),
        ARM("手臂围", "arm", 4),
        THIGH("大腿围", "thigh", 5),
        SHANK("小腿围", "shank", 6);

        private int    code;
        private String name;
        private String type;

        private MeasureType(String name, String type, int code) {
            this.name = name;
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return this.type;
        }

        public String getName() {
            return this.name;
        }

        public int getCode() {
            return this.code;
        }
    }

    public static List<BaseRecord> parseLists(JSONArray array) {
        if (array == null) {
            return null;
        }
        return (List) new Gson().fromJson(array.toString(), new TypeToken<List<Measure>>() {
        }.getType());
    }

    public static List<String> getMeasureList() {
        List<String> list = new ArrayList();
        list.add(MeasureType.WAIST.getName());
        list.add(MeasureType.BRASS.getName());
        list.add(MeasureType.HIP.getName());
        list.add(MeasureType.ARM.getName());
        list.add(MeasureType.THIGH.getName());
        list.add(MeasureType.SHANK.getName());
        return list;
    }
}
