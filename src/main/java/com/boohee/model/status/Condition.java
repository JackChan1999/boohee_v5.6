package com.boohee.model.status;

import com.boohee.model.Goods;
import com.boohee.modeldao.UserDao;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utility.Const;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class Condition {
    public static String AVATAR   = UserDao.AVATAR;
    public static String NICKNAME = UserTimelineActivity.NICK_NAME;
    public static String PASSWORD = Const.PASSWORD;
    public String  field;
    public String  message;
    public boolean satisfied;

    public static ArrayList<Condition> parseConditions(JSONObject res) {
        try {
            return (ArrayList) new Gson().fromJson(res.getString("conditions").toString(), new
                    TypeToken<ArrayList<Goods>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
