package com.tencent.tinker.lib.listener;

import android.content.Context;

import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class DefaultPatchListener implements PatchListener {
    protected final Context context;

    public DefaultPatchListener(Context context) {
        this.context = context;
    }

    public int onPatchReceived(String path, boolean isUpgrade) {
        int returnCode = patchCheck(path, isUpgrade);
        if (returnCode == 0) {
            TinkerPatchService.runPatchService(this.context, path, isUpgrade);
        } else {
            Tinker.with(this.context).getLoadReporter().onLoadPatchListenerReceiveFail(new File
                    (path), returnCode, isUpgrade);
        }
        return returnCode;
    }

    protected int patchCheck(String path, boolean isUpgrade) {
        Tinker manager = Tinker.with(this.context);
        if (!manager.isTinkerEnabled() || !ShareTinkerInternals
                .isTinkerEnableWithSharedPreferences(this.context)) {
            return -1;
        }
        File file = new File(path);
        if (!file.isFile() || !file.exists() || file.length() == 0) {
            return -2;
        }
        if (manager.isPatchProcess()) {
            return -4;
        }
        if (TinkerServiceInternals.isTinkerPatchServiceRunning(this.context)) {
            return -3;
        }
        return 0;
    }
}
