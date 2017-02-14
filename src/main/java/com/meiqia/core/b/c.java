package com.meiqia.core.b;

import butterknife.internal.ButterKnifeProcessor;

import com.alipay.sdk.sys.a;
import com.boohee.modeldao.UserDao;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utils.Utils;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.squareup.okhttp.Response;
import com.umeng.socialize.common.SocialSNSHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    public static MQMessage a(JSONObject jSONObject) {
        MQMessage mQMessage = new MQMessage("text");
        long optLong = jSONObject.optLong("id");
        long optLong2 = jSONObject.optLong("conversation_id");
        String optString = jSONObject.optString("content_type");
        String optString2 = jSONObject.optString(Utils.RESPONSE_CONTENT);
        String optString3 = jSONObject.optString("type");
        long a = i.a(jSONObject.optString("created_on"));
        String optString4 = jSONObject.optString("from_type");
        String optString5 = jSONObject.optString("track_id");
        JSONObject optJSONObject = jSONObject.optJSONObject("agent");
        String str = "";
        String str2 = "";
        String str3 = "";
        if (optJSONObject != null) {
            str = optJSONObject.optString(UserTimelineActivity.NICK_NAME);
            str2 = optJSONObject.optString(UserDao.AVATAR);
            str3 = optJSONObject.optString("token");
        }
        String optString6 = jSONObject.optString("media_url");
        long optLong3 = jSONObject.optLong("enterprise_id");
        mQMessage.setExtra(jSONObject.optString("extra"));
        mQMessage.setEnterprise_id(optLong3);
        mQMessage.setAgent_id(str3);
        mQMessage.setId(optLong);
        mQMessage.setConversation_id(optLong2);
        mQMessage.setContent_type(optString);
        mQMessage.setType(optString3);
        mQMessage.setCreated_on(a);
        mQMessage.setContent(optString2);
        mQMessage.setFrom_type(optString4);
        mQMessage.setTrack_id(optString5);
        mQMessage.setAgent_nickname(str);
        mQMessage.setAvatar(str2);
        mQMessage.setMedia_url(optString6);
        return mQMessage;
    }

    public static String a(String str, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (String str2 : map.keySet()) {
            if (i == 0) {
                stringBuffer.append("?");
            } else {
                stringBuffer.append(a.b);
            }
            stringBuffer.append(str2).append("=").append((String) map.get(str2));
            i++;
        }
        return stringBuffer.toString();
    }

    public static List<MQMessage> a(JSONArray jSONArray) {
        List<MQMessage> arrayList = new ArrayList();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    String optString = optJSONObject.optString("action");
                    if ("message".equals(optString) || "ticket_reply".equals(optString)) {
                        arrayList.add(a(optJSONObject));
                    }
                }
            }
        }
        return arrayList;
    }

    public static JSONArray a(Object obj) {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < length; i++) {
                jSONArray.put(b(Array.get(obj, i)));
            }
            return jSONArray;
        }
        throw new JSONException("Not a primitive data: " + obj.getClass());
    }

    public static JSONArray a(Collection collection) {
        JSONArray jSONArray = new JSONArray();
        if (collection != null) {
            for (Object b : collection) {
                jSONArray.put(b(b));
            }
        }
        return jSONArray;
    }

    public static JSONObject a(Response response) {
        try {
            return new JSONObject(b(response));
        } catch (Exception e) {
            Exception exception = e;
            JSONObject jSONObject = new JSONObject();
            e.a("responseToJsonObj : " + exception.toString());
            return jSONObject;
        }
    }

    public static JSONObject a(Map<?, ?> map) {
        JSONObject jSONObject = new JSONObject();
        for (Entry entry : map.entrySet()) {
            String str = (String) entry.getKey();
            if (str == null) {
                throw new NullPointerException("key == null");
            }
            try {
                jSONObject.put(str, b(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static MQAgent b(JSONObject jSONObject) {
        MQAgent mQAgent = new MQAgent();
        long optLong = jSONObject.optLong("enterprise_id");
        String optString = jSONObject.optString(UserDao.AVATAR);
        String optString2 = jSONObject.optString("cellphone");
        String optString3 = jSONObject.optString(UserTimelineActivity.NICK_NAME);
        String optString4 = jSONObject.optString("public_cellphone");
        String optString5 = jSONObject.optString("public_email");
        String optString6 = jSONObject.optString(SocialSNSHelper.SOCIALIZE_QQ_KEY);
        String optString7 = jSONObject.optString("signature");
        String optString8 = jSONObject.optString("telephone");
        String optString9 = jSONObject.optString("weixin");
        String optString10 = jSONObject.optString("token");
        String optString11 = jSONObject.optString("status");
        boolean optBoolean = jSONObject.optBoolean("is_online");
        mQAgent.setEnterprise_id(optLong);
        mQAgent.setAvatar(optString);
        mQAgent.setCellphone(optString2);
        mQAgent.setNickname(optString3);
        mQAgent.setPublic_cellphone(optString4);
        mQAgent.setPublic_email(optString5);
        mQAgent.setQq(optString6);
        mQAgent.setSignature(optString7);
        mQAgent.setTelephone(optString8);
        mQAgent.setWeixin(optString9);
        mQAgent.setId(optString10);
        mQAgent.setStatus(optString11);
        mQAgent.setIsOnline(optBoolean);
        return mQAgent;
    }

    private static Object b(Object obj) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof JSONArray) || (obj instanceof JSONObject)) {
            return obj;
        }
        try {
            if (obj instanceof Collection) {
                return a((Collection) obj);
            }
            if (obj.getClass().isArray()) {
                return a(obj);
            }
            if (obj instanceof Map) {
                return a((Map) obj);
            }
            if ((obj instanceof Boolean) || (obj instanceof Byte) || (obj instanceof Character)
                    || (obj instanceof Double) || (obj instanceof Float) || (obj instanceof
                    Integer) || (obj instanceof Long) || (obj instanceof Short) || (obj
                    instanceof String)) {
                return obj;
            }
            if (obj.getClass().getPackage().getName().startsWith(ButterKnifeProcessor
                    .JAVA_PREFIX)) {
                return obj.toString();
            }
            return null;
        } catch (Exception e) {
        }
    }

    public static String b(Response response) {
        try {
            return new String(response.body().bytes(), "UTF-8");
        } catch (Exception e) {
            e.a("responseToJsonObj : " + e.toString());
            return null;
        }
    }

    public static MQConversation c(JSONObject jSONObject) {
        MQConversation mQConversation = new MQConversation();
        long optLong = jSONObject.optLong("id");
        int optInt = jSONObject.optInt("assignee");
        long optLong2 = jSONObject.optLong("enterprise_id");
        long a = i.a(jSONObject.optString("created_on"));
        mQConversation.setAssignee(optInt);
        mQConversation.setEnterprise_id(optLong2);
        mQConversation.setCreated_on(a);
        mQConversation.setId(optLong);
        return mQConversation;
    }
}
