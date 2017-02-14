package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Blocks {
    public List<PartnerBlock> block;

    public static List<Blocks> parseBlocks(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<Blocks>>() {
        }.getType());
    }
}
