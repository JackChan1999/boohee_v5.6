package com.tencent.tinker.lib.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;

public abstract class AbstractResultService extends IntentService {
    private static final String                                 RESULT_EXTRA       = "result_extra";
    private static final String                                 TAG                = "Tinker" +
            ".AbstractResultService";
    private static       Class<? extends AbstractResultService> resultServiceClass = null;

    public abstract void onPatchResult(PatchResult patchResult);

    public AbstractResultService() {
        super(AbstractResultService.class.getSimpleName());
    }

    public static void runResultService(Context context, PatchResult result) {
        if (resultServiceClass == null) {
            throw new TinkerRuntimeException("resultServiceClass is null.");
        }
        Intent intent = new Intent(context, resultServiceClass);
        intent.putExtra(RESULT_EXTRA, result);
        context.startService(intent);
    }

    public static void setResultServiceClass(Class<? extends AbstractResultService> serviceClass) {
        resultServiceClass = serviceClass;
        try {
            Class.forName(serviceClass.getName());
        } catch (ClassNotFoundException e) {
        }
    }

    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            TinkerLog.e(TAG, "AbstractResultService received a null intent, ignoring.", new
                    Object[0]);
        } else {
            onPatchResult((PatchResult) ShareIntentUtil.getSerializableExtra(intent, RESULT_EXTRA));
        }
    }
}
