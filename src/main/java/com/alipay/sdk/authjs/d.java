package com.alipay.sdk.authjs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.alipay.sdk.authjs.a.a;
import com.android.volley.DefaultRetryPolicy;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.utils.Utils;
import java.util.Timer;
import org.json.JSONException;
import org.json.JSONObject;

public final class d {
    c a;
    Context b;

    private static /* synthetic */ a a(d dVar, a aVar) {
        if (aVar != null && "toast".equals(aVar.k)) {
            JSONObject jSONObject = aVar.m;
            CharSequence optString = jSONObject.optString(Utils.RESPONSE_CONTENT);
            int optInt = jSONObject.optInt(SportRecordDao.DURATION);
            int i = 1;
            if (optInt < DefaultRetryPolicy.DEFAULT_TIMEOUT_MS) {
                i = 0;
            }
            Toast.makeText(dVar.b, optString, i).show();
            new Timer().schedule(new f(dVar, aVar), (long) i);
        }
        return a.NONE_ERROR;
    }

    public d(Context context, c cVar) {
        this.b = context;
        this.a = cVar;
    }

    private void a(String str) {
        String str2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("clientId");
            try {
                if (!TextUtils.isEmpty(string)) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("param");
                    if (jSONObject2 instanceof JSONObject) {
                        jSONObject2 = jSONObject2;
                    } else {
                        jSONObject2 = null;
                    }
                    String string2 = jSONObject.getString(a.g);
                    String string3 = jSONObject.getString(a.d);
                    a aVar = new a("call");
                    aVar.j = string3;
                    aVar.k = string2;
                    aVar.m = jSONObject2;
                    aVar.i = string;
                    a(aVar);
                }
            } catch (Exception e) {
                str2 = string;
                if (!TextUtils.isEmpty(str2)) {
                    try {
                        a(str2, a.RUNTIME_ERROR);
                    } catch (JSONException e2) {
                    }
                }
            }
        } catch (Exception e3) {
            str2 = null;
            if (!TextUtils.isEmpty(str2)) {
                a(str2, a.RUNTIME_ERROR);
            }
        }
    }

    public final void a(a aVar) throws JSONException {
        if (aVar != null) {
            if (TextUtils.isEmpty(aVar.k)) {
                a(aVar.i, a.INVALID_PARAMETER);
                return;
            }
            Runnable eVar = new e(this, aVar);
            if ((Looper.getMainLooper() == Looper.myLooper() ? 1 : null) != null) {
                eVar.run();
            } else {
                new Handler(Looper.getMainLooper()).post(eVar);
            }
        }
    }

    public final void a(String str, a aVar) throws JSONException {
        if (!TextUtils.isEmpty(str)) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("error", aVar.ordinal());
            a aVar2 = new a(a.c);
            aVar2.m = jSONObject;
            aVar2.i = str;
            this.a.a(aVar2);
        }
    }

    private static void a(Runnable runnable) {
        if (runnable != null) {
            if ((Looper.getMainLooper() == Looper.myLooper() ? 1 : null) != null) {
                runnable.run();
            } else {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }
    }

    private a b(a aVar) {
        if (aVar != null && "toast".equals(aVar.k)) {
            JSONObject jSONObject = aVar.m;
            CharSequence optString = jSONObject.optString(Utils.RESPONSE_CONTENT);
            int optInt = jSONObject.optInt(SportRecordDao.DURATION);
            int i = 1;
            if (optInt < DefaultRetryPolicy.DEFAULT_TIMEOUT_MS) {
                i = 0;
            }
            Toast.makeText(this.b, optString, i).show();
            new Timer().schedule(new f(this, aVar), (long) i);
        }
        return a.NONE_ERROR;
    }

    private void c(a aVar) {
        JSONObject jSONObject = aVar.m;
        CharSequence optString = jSONObject.optString(Utils.RESPONSE_CONTENT);
        int optInt = jSONObject.optInt(SportRecordDao.DURATION);
        int i = 1;
        if (optInt < DefaultRetryPolicy.DEFAULT_TIMEOUT_MS) {
            i = 0;
        }
        Toast.makeText(this.b, optString, i).show();
        new Timer().schedule(new f(this, aVar), (long) i);
    }
}
