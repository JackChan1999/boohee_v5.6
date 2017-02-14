package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.utils.StatisticsDataUtils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: StatisticsDataRequest */
public class r extends SocializeRequest {
    private static final String b = "/app/oper/";
    private static final String c = "StatisticsDataRequest";
    private Context a;

    public r(Context context, SocializeEntity socializeEntity) {
        super(context, "", s.class, socializeEntity, 0, RequestMethod.POST);
        this.a = context;
        this.mEntity = socializeEntity;
    }

    protected String getPath() {
        return b + SocializeUtils.getAppkey(this.a) + "/";
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        Map map2;
        try {
            StatisticsDataUtils.getStatisticsData(this.a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatisticsDataUtils.getStatisticsData(this.a);
        Map statisticsData = this.mEntity.getStatisticsData();
        map.remove(SocializeProtocolConstants.PROTOCOL_KEY_OPID);
        Map hashMap = new HashMap();
        for (SHARE_MEDIA share_media : statisticsData.keySet()) {
            StringBuilder stringBuilder = (StringBuilder) statisticsData.get(share_media);
            if (TextUtils.isEmpty(stringBuilder)) {
                map2 = hashMap;
            } else {
                int i;
                if (share_media == SHARE_MEDIA.GENERIC) {
                    i = 0;
                } else {
                    i = StatisticsDataUtils.getPlatformOperation(share_media);
                }
                if (stringBuilder.substring(stringBuilder.length() - 1, stringBuilder.length())
                        .equals(SocializeConstants.OP_DIVIDER_PLUS)) {
                    stringBuilder = stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                map2 = StatisticsDataUtils.convertStatisticsData(String.valueOf(i), stringBuilder
                        .toString(), hashMap);
            }
            hashMap = map2;
        }
        statisticsData = this.mEntity.getOauthStatisticsData();
        for (String str : statisticsData.keySet()) {
            int intValue = ((Integer) statisticsData.get(str)).intValue();
            if (intValue > 0) {
                hashMap.put(str, intValue + "");
            }
        }
        map2 = this.mEntity.getSharkStatisticsData(this.a);
        if (map2.containsKey("shake")) {
            hashMap.put("shake", map2.get("shake"));
        }
        JSONObject jSONObject = new JSONObject();
        for (String str2 : hashMap.keySet()) {
            try {
                jSONObject.put(str2, Integer.parseInt(hashMap.get(str2).toString()));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        CharSequence adapterSDKVersion = this.mEntity.getAdapterSDKVersion();
        if (!TextUtils.isEmpty(adapterSDKVersion)) {
            try {
                jSONObject.put(this.mEntity.getAdapterSDK(), adapterSDKVersion);
            } catch (JSONException e22) {
                e22.printStackTrace();
            }
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("param", jSONObject.toString());
        } catch (JSONException e222) {
            e222.printStackTrace();
        }
        return packParamsMap(c, jSONObject2.toString());
    }
}
