package com.umeng.analytics.social;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import org.json.JSONObject;

class UMSocialService$a extends AsyncTask<Void, Void, d> {
    String            a;
    String            b;
    UMSocialService$b c;
    UMPlatformData[]  d;

    protected /* synthetic */ Object doInBackground(Object[] objArr) {
        return a((Void[]) objArr);
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((d) obj);
    }

    public UMSocialService$a(String[] strArr, UMSocialService$b uMSocialService$b,
                             UMPlatformData[] uMPlatformDataArr) {
        this.a = strArr[0];
        this.b = strArr[1];
        this.c = uMSocialService$b;
        this.d = uMPlatformDataArr;
    }

    protected void onPreExecute() {
        if (this.c != null) {
            this.c.a();
        }
    }

    protected d a(Void... voidArr) {
        String a;
        if (TextUtils.isEmpty(this.b)) {
            a = c.a(this.a);
        } else {
            a = c.a(this.a, this.b);
        }
        try {
            JSONObject jSONObject = new JSONObject(a);
            int optInt = jSONObject.optInt(SocializeProtocolConstants.PROTOCOL_KEY_ST);
            d dVar = new d(optInt == 0 ? e.t : optInt);
            String optString = jSONObject.optString("msg");
            if (!TextUtils.isEmpty(optString)) {
                dVar.a(optString);
            }
            Object optString2 = jSONObject.optString("data");
            if (TextUtils.isEmpty(optString2)) {
                return dVar;
            }
            dVar.b(optString2);
            return dVar;
        } catch (Exception e) {
            return new d(-99, e);
        }
    }

    protected void a(d dVar) {
        if (this.c != null) {
            this.c.a(dVar, this.d);
        }
    }
}
