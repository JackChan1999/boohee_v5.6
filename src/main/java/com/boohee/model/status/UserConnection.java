package com.boohee.model.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserConnection {
    public String access_token;
    public String avatar_url;
    public String expires_at;
    public String expires_in;
    public int    id;
    public String identity;
    public String nickname;
    public String provider;

    public static UserConnection parseUserConnection(String str) {
        UserConnection connection = null;
        try {
            return (UserConnection) new Gson().fromJson(str, UserConnection.class);
        } catch (Exception e) {
            e.printStackTrace();
            return connection;
        }
    }

    public static ArrayList<UserConnection> parseUserConnections(JSONObject object) {
        ArrayList<UserConnection> connections = null;
        try {
            JSONArray connObj = object.getJSONArray("user_connections");
            return (ArrayList) new Gson().fromJson(connObj.toString(), new
                    TypeToken<ArrayList<UserConnection>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return connections;
        }
    }
}
