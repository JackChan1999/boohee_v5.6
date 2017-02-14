package com.boohee.one.update;

import android.content.Context;
import android.support.annotation.NonNull;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Config;
import com.boohee.utils.FastJsonUtils;

public class UpdateAgent {
    public static final String UPDATE_URL = "/app_update.json";

    public static void update(Context context) {
        update(context, new DefaultUpdateStrategy());
    }

    public static void update(final Context context, @NonNull final UpdateStrategy strategy) {
        JsonParams params = new JsonParams();
        params.put("version_code", Config.getVersionCode());
        BooheeClient.build(BooheeClient.ONE).get(UPDATE_URL, params, new JsonCallback(context) {
            public void ok(String response) {
                super.ok(response);
                UpdateInfo info = (UpdateInfo) FastJsonUtils.fromJson(response, UpdateInfo.class);
                if (info != null && info.update) {
                    strategy.onUpdate(context, info);
                }
            }
        }, context);
    }
}
