package com.umeng.socialize.net;

import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ProfileResponse */
public class i extends SocializeReseponse {
    public SocializeUser a;

    public i(JSONObject jSONObject) {
        super(jSONObject);
    }

    public void parseJsonObject() {
        JSONObject jSONObject = this.mJsonData;
        if (jSONObject == null) {
            Log.e(SocializeReseponse.TAG, "data json is null....");
            return;
        }
        try {
            String string;
            String obj;
            String optString;
            String optString2;
            String optString3;
            this.a = new SocializeUser();
            if (jSONObject.has("default")) {
                string = jSONObject.getString("default");
                this.a.mDefaultPlatform = SHARE_MEDIA.convertToEmun(string);
            }
            try {
                if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_ACCOUNTS)) {
                    List arrayList = new ArrayList();
                    JSONObject jSONObject2 = jSONObject.getJSONObject(SocializeProtocolConstants
                            .PROTOCOL_KEY_ACCOUNTS);
                    Iterator keys = jSONObject2.keys();
                    while (keys.hasNext()) {
                        obj = keys.next().toString();
                        JSONObject jSONObject3 = jSONObject2.getJSONObject(obj);
                        optString = jSONObject3.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_USER_NAME2, "");
                        String optString4 = jSONObject3.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_USER_ICON2, "");
                        optString2 = jSONObject3.optString("usid", "");
                        int optInt = jSONObject3.optInt(SocializeProtocolConstants
                                .PROTOCOL_KEY_GENDER, 0);
                        String optString5 = jSONObject3.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_PROFILE_URL, "");
                        String optString6 = jSONObject3.optString("birthday", "");
                        optString3 = jSONObject3.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_EXTEND_ARGS, "");
                        SnsAccount snsAccount = new SnsAccount(optString, Gender.convertToEmun
                                (String.valueOf(optInt)), optString4, optString2);
                        snsAccount.setPlatform(obj);
                        snsAccount.setProfileUrl(optString5);
                        arrayList.add(snsAccount);
                        snsAccount.setBirthday(optString6);
                        snsAccount.setExtendArgs(optString3);
                    }
                    this.a.mAccounts = arrayList;
                }
            } catch (JSONException e) {
                Log.i(TAG, "No snsAccout oauth....");
            }
            try {
                if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_LOGINACC)) {
                    JSONObject jSONObject4 = jSONObject.getJSONObject(SocializeProtocolConstants
                            .PROTOCOL_KEY_LOGINACC);
                    Iterator keys2 = jSONObject4.keys();
                    if (keys2.hasNext()) {
                        String obj2 = keys2.next().toString();
                        jSONObject4 = jSONObject4.getJSONObject(obj2);
                        String optString7 = jSONObject4.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_USER_NAME2, "");
                        obj = jSONObject4.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_USER_ICON2, "");
                        optString3 = jSONObject4.optString("usid", "");
                        optString = jSONObject4.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_PROFILE_URL, "");
                        int optInt2 = jSONObject4.optInt(SocializeProtocolConstants
                                .PROTOCOL_KEY_GENDER, 0);
                        optString2 = jSONObject4.optString("birthday", "");
                        string = jSONObject4.optString(SocializeProtocolConstants
                                .PROTOCOL_KEY_EXTEND_ARGS, "");
                        SnsAccount snsAccount2 = new SnsAccount(optString7, Gender.convertToEmun
                                (String.valueOf(optInt2)), obj, optString3);
                        snsAccount2.setPlatform(obj2);
                        snsAccount2.setProfileUrl(optString);
                        this.a.mLoginAccount = snsAccount2;
                        snsAccount2.setBirthday(optString2);
                        snsAccount2.setExtendArgs(string);
                    }
                }
            } catch (JSONException e2) {
                Log.i(TAG, "No loginAccount ....");
            }
            if (this.a.mLoginAccount == null) {
                try {
                    if (jSONObject.has(SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME2)) {
                        SocializeConstants.GUIDENAME = jSONObject.optString
                                (SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME2, "");
                    }
                } catch (Exception e3) {
                    Log.i(TAG, "No loginAccount ....");
                }
            }
        } catch (Exception e4) {
            Log.e(TAG, "Parse json error[ " + jSONObject.toString() + " ]", e4);
        }
    }
}
