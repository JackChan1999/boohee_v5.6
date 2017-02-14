package com.boohee.one.tinker.reporter;

import android.app.ActivityManager;
import android.content.Context;

import com.boohee.model.ModelName;
import com.boohee.one.MyApplication;
import com.boohee.one.tinker.BuildInfo;
import com.boohee.one.tinker.Utils;
import com.boohee.utility.Event;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerLoadResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.Properties;

public class SamplePatchListener extends DefaultPatchListener {
    protected static final long   NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = 62914560;
    protected static final long   OLD_PATCH_RESTRICTION_SPACE_SIZE_MIN = 31457280;
    private static final   String TAG                                  = "Tinker" +
            ".SamplePatchListener";
    private final int maxMemory;

    public SamplePatchListener(Context context) {
        super(context);
        this.maxMemory = ((ActivityManager) context.getSystemService(ModelName.ACTIVITY))
                .getMemoryClass();
        TinkerLog.i(TAG, "application maxMemory:" + this.maxMemory, new Object[0]);
    }

    public int onPatchReceived(String path, boolean isUpgrade) {
        MobclickAgent.onEvent(MyApplication.getContext(), Event.tinker_onPatchReceived);
        return super.onPatchReceived(path, isUpgrade);
    }

    public int patchCheck(String path, boolean isUpgrade) {
        File patchFile = new File(path);
        TinkerLog.i(TAG, "receive a patch file: %s, isUpgrade:%b, file size:%d", path, Boolean
                .valueOf(isUpgrade), Long.valueOf(SharePatchFileUtil.getFileOrDirectorySize
                (patchFile)));
        int returnCode = super.patchCheck(path, isUpgrade);
        if (returnCode == 0) {
            if (isUpgrade) {
                returnCode = Utils.checkForPatchRecover(NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN,
                        this.maxMemory);
            } else {
                returnCode = Utils.checkForPatchRecover(OLD_PATCH_RESTRICTION_SPACE_SIZE_MIN,
                        this.maxMemory);
            }
        }
        if (returnCode == 0) {
            String patchMd5 = SharePatchFileUtil.getMD5(patchFile);
            if (this.context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG,
                    4).getInt(patchMd5, 0) >= 3) {
                returnCode = -9;
            } else {
                Tinker tinker = Tinker.with(this.context);
                if (tinker.isTinkerLoaded()) {
                    TinkerLoadResult tinkerLoadResult = tinker.getTinkerLoadResultIfPresent();
                    if (tinkerLoadResult != null && patchMd5.equals(tinkerLoadResult
                            .currentVersion)) {
                        returnCode = -8;
                    }
                }
            }
        }
        if (returnCode == 0) {
            Properties properties = ShareTinkerInternals.fastGetPatchPackageMeta(patchFile);
            if (properties == null) {
                returnCode = -10;
            } else {
                String platform = properties.getProperty("platform");
                TinkerLog.i(TAG, "get platform:" + platform, new Object[0]);
                if (platform == null || !platform.equals(BuildInfo.PLATFORM)) {
                    returnCode = -10;
                }
            }
        }
        SampleTinkerReport.onTryApply(isUpgrade, returnCode == 0);
        return returnCode;
    }
}
