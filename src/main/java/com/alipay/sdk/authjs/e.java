package com.alipay.sdk.authjs;

import android.widget.Toast;
import com.alipay.sdk.authjs.a.a;
import com.android.volley.DefaultRetryPolicy;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.utils.Utils;
import java.util.Timer;
import org.json.JSONException;
import org.json.JSONObject;

final class e implements Runnable {
    final /* synthetic */ a a;
    final /* synthetic */ d b;

    e(d dVar, a aVar) {
        this.b = dVar;
        this.a = aVar;
    }

    public final void run() {
        d dVar = this.b;
        a aVar = this.a;
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
        a aVar2 = a.NONE_ERROR;
        if (aVar2 != a.NONE_ERROR) {
            try {
                this.b.a(this.a.i, aVar2);
            } catch (JSONException e) {
            }
        }
    }
}
