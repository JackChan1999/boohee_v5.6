package com.mob.tools.network;

import com.mob.tools.utils.Hashon;
import com.qiniu.android.common.Constants;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

class NetworkHelper$2 implements HttpResponseCallback {
    final /* synthetic */ NetworkHelper this$0;
    final /* synthetic */ HashMap       val$tmpMap;

    NetworkHelper$2(NetworkHelper networkHelper, HashMap hashMap) {
        this.this$0 = networkHelper;
        this.val$tmpMap = hashMap;
    }

    public void onResponse(HttpResponse httpResponse) throws Throwable {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        this.val$tmpMap.put("status", Integer.valueOf(statusCode));
        if (statusCode == 200 || statusCode == 201) {
            this.val$tmpMap.put("resp", EntityUtils.toString(httpResponse.getEntity(), Constants
                    .UTF_8));
            return;
        }
        String entityUtils = EntityUtils.toString(httpResponse.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        this.val$tmpMap.put("resp", new Hashon().fromHashMap(hashMap));
    }
}
