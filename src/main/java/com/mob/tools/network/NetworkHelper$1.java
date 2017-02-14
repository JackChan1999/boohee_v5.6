package com.mob.tools.network;

import com.mob.tools.utils.Hashon;
import com.qiniu.android.common.Constants;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

class NetworkHelper$1 implements HttpResponseCallback {
    final /* synthetic */ NetworkHelper this$0;
    final /* synthetic */ HashMap       val$tmpMap;

    NetworkHelper$1(NetworkHelper networkHelper, HashMap hashMap) {
        this.this$0 = networkHelper;
        this.val$tmpMap = hashMap;
    }

    public void onResponse(HttpResponse httpResponse) throws Throwable {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200 || statusCode == 201) {
            this.val$tmpMap.put("resp", EntityUtils.toString(httpResponse.getEntity(), Constants
                    .UTF_8));
            return;
        }
        String entityUtils = EntityUtils.toString(httpResponse.getEntity(), Constants.UTF_8);
        HashMap hashMap = new HashMap();
        hashMap.put("error", entityUtils);
        hashMap.put("status", Integer.valueOf(statusCode));
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }
}
