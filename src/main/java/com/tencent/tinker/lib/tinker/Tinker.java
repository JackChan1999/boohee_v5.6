package com.tencent.tinker.lib.tinker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.PlaybackStateCompat;

import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.RepairPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.service.AbstractResultService;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

public class Tinker {
    private static final String  TAG       = "Tinker.Tinker";
    private static       boolean installed = false;
    private static Tinker        sInstance;
    final          Context       context;
    final          boolean       isMainProcess;
    final          boolean       isPatchProcess;
    final          PatchListener listener;
    final          LoadReporter  loadReporter;
    private        boolean       loaded;
    final          File          patchDirectory;
    final          File          patchInfoFile;
    final          PatchReporter patchReporter;
    int              tinkerFlags;
    TinkerLoadResult tinkerLoadResult;
    final boolean tinkerLoadVerifyFlag;

    public static class Builder {
        private final Context       context;
        private       PatchListener listener;
        private       LoadReporter  loadReporter;
        private final boolean       mainProcess;
        private       File          patchDirectory;
        private       File          patchInfoFile;
        private final boolean       patchProcess;
        private       PatchReporter patchReporter;
        private int status = -1;
        private Boolean tinkerLoadVerifyFlag;

        public Builder(Context context) {
            if (context == null) {
                throw new TinkerRuntimeException("Context must not be null.");
            }
            this.context = context;
            this.mainProcess = ShareTinkerInternals.isInMainProcess(context);
            this.patchProcess = TinkerServiceInternals.isInTinkerPatchServiceProcess(context);
            this.patchDirectory = SharePatchFileUtil.getPatchDirectory(context);
            if (this.patchDirectory == null) {
                TinkerLog.e(Tinker.TAG, "patchDirectory is null!", new Object[0]);
                return;
            }
            this.patchInfoFile = SharePatchFileUtil.getPatchInfoFile(this.patchDirectory
                    .getAbsolutePath());
            TinkerLog.w(Tinker.TAG, "tinker patch directory: %s", this.patchDirectory);
        }

        public Builder tinkerFlags(int tinkerFlags) {
            if (this.status != -1) {
                throw new TinkerRuntimeException("tinkerFlag is already set.");
            }
            this.status = tinkerFlags;
            return this;
        }

        public Builder tinkerLoadVerifyFlag(Boolean verifyMd5WhenLoad) {
            if (verifyMd5WhenLoad == null) {
                throw new TinkerRuntimeException("tinkerLoadVerifyFlag must not be null.");
            } else if (this.tinkerLoadVerifyFlag != null) {
                throw new TinkerRuntimeException("tinkerLoadVerifyFlag is already set.");
            } else {
                this.tinkerLoadVerifyFlag = verifyMd5WhenLoad;
                return this;
            }
        }

        public Builder loadReport(LoadReporter loadReporter) {
            if (loadReporter == null) {
                throw new TinkerRuntimeException("loadReporter must not be null.");
            } else if (this.loadReporter != null) {
                throw new TinkerRuntimeException("loadReporter is already set.");
            } else {
                this.loadReporter = loadReporter;
                return this;
            }
        }

        public Builder patchReporter(PatchReporter patchReporter) {
            if (patchReporter == null) {
                throw new TinkerRuntimeException("patchReporter must not be null.");
            } else if (this.patchReporter != null) {
                throw new TinkerRuntimeException("patchReporter is already set.");
            } else {
                this.patchReporter = patchReporter;
                return this;
            }
        }

        public Builder listener(PatchListener listener) {
            if (listener == null) {
                throw new TinkerRuntimeException("listener must not be null.");
            } else if (this.listener != null) {
                throw new TinkerRuntimeException("listener is already set.");
            } else {
                this.listener = listener;
                return this;
            }
        }

        public Tinker build() {
            if (this.status == -1) {
                this.status = 7;
            }
            if (this.loadReporter == null) {
                this.loadReporter = new DefaultLoadReporter(this.context);
            }
            if (this.patchReporter == null) {
                this.patchReporter = new DefaultPatchReporter(this.context);
            }
            if (this.listener == null) {
                this.listener = new DefaultPatchListener(this.context);
            }
            if (this.tinkerLoadVerifyFlag == null) {
                this.tinkerLoadVerifyFlag = Boolean.valueOf(false);
            }
            return new Tinker(this.context, this.status, this.loadReporter, this.patchReporter,
                    this.listener, this.patchDirectory, this.patchInfoFile, this.mainProcess,
                    this.patchProcess, this.tinkerLoadVerifyFlag.booleanValue());
        }
    }

    private Tinker(Context context, int tinkerFlags, LoadReporter loadReporter, PatchReporter
            patchReporter, PatchListener listener, File patchDirectory, File patchInfoFile,
                   boolean isInMainProc, boolean isPatchProcess, boolean tinkerLoadVerifyFlag) {
        this.loaded = false;
        this.context = context;
        this.listener = listener;
        this.loadReporter = loadReporter;
        this.patchReporter = patchReporter;
        this.tinkerFlags = tinkerFlags;
        this.patchDirectory = patchDirectory;
        this.patchInfoFile = patchInfoFile;
        this.isMainProcess = isInMainProc;
        this.tinkerLoadVerifyFlag = tinkerLoadVerifyFlag;
        this.isPatchProcess = isPatchProcess;
    }

    public static Tinker with(Context context) {
        if (installed) {
            if (sInstance == null) {
                synchronized (Tinker.class) {
                    if (sInstance == null) {
                        sInstance = new Builder(context).build();
                    }
                }
            }
            return sInstance;
        }
        throw new TinkerRuntimeException("you must install tinker before get tinker sInstance");
    }

    public static void create(Tinker tinker) {
        if (sInstance != null) {
            throw new TinkerRuntimeException("Tinker instance is already set.");
        }
        sInstance = tinker;
    }

    public void install(Intent intentResult, Class<? extends AbstractResultService> serviceClass,
                        AbstractPatch upgradePatch, AbstractPatch repairPatch) {
        installed = true;
        AbstractResultService.setResultServiceClass(serviceClass);
        TinkerPatchService.setPatchProcessor(upgradePatch, repairPatch);
        if (!isTinkerEnabled()) {
            TinkerLog.e(TAG, "tinker is disabled", new Object[0]);
        } else if (intentResult == null) {
            throw new TinkerRuntimeException("intentResult must not be null.");
        } else {
            this.tinkerLoadResult = new TinkerLoadResult();
            this.tinkerLoadResult.parseTinkerResult(getContext(), intentResult);
            this.loadReporter.onLoadResult(this.patchDirectory, this.tinkerLoadResult.loadCode,
                    this.tinkerLoadResult.costTime);
            if (!this.loaded) {
                TinkerLog.w(TAG, "tinker load fail!", new Object[0]);
            }
        }
    }

    public void setPatchServiceNotificationId(int id) {
        TinkerPatchService.setTinkerNotificationId(id);
    }

    public TinkerLoadResult getTinkerLoadResultIfPresent() {
        return this.tinkerLoadResult;
    }

    public void install(Intent intentResult) {
        install(intentResult, DefaultTinkerResultService.class, new UpgradePatch(), new
                RepairPatch());
    }

    public Context getContext() {
        return this.context;
    }

    public boolean isMainProcess() {
        return this.isMainProcess;
    }

    public boolean isPatchProcess() {
        return this.isPatchProcess;
    }

    public void setTinkerDisable() {
        this.tinkerFlags = 0;
    }

    public LoadReporter getLoadReporter() {
        return this.loadReporter;
    }

    public PatchReporter getPatchReporter() {
        return this.patchReporter;
    }

    public boolean isTinkerEnabled() {
        return ShareTinkerInternals.isTinkerEnabled(this.tinkerFlags);
    }

    public boolean isTinkerLoaded() {
        return this.loaded;
    }

    public void setTinkerLoaded(boolean isLoaded) {
        this.loaded = isLoaded;
    }

    public boolean isTinkerInstalled() {
        return installed;
    }

    public boolean isTinkerLoadVerify() {
        return this.tinkerLoadVerifyFlag;
    }

    public boolean isEnabledForDex() {
        return ShareTinkerInternals.isTinkerEnabledForDex(this.tinkerFlags);
    }

    public boolean isEnabledForNativeLib() {
        return ShareTinkerInternals.isTinkerEnabledForNativeLib(this.tinkerFlags);
    }

    public boolean isEnabledForResource() {
        return ShareTinkerInternals.isTinkerEnabledForResource(this.tinkerFlags);
    }

    public File getPatchDirectory() {
        return this.patchDirectory;
    }

    public File getPatchInfoFile() {
        return this.patchInfoFile;
    }

    public PatchListener getPatchListener() {
        return this.listener;
    }

    public void cleanPatch() {
        if (this.patchDirectory != null) {
            if (isTinkerLoaded()) {
                TinkerLog.e(TAG, "it is not safety to clean patch when tinker is loaded, you " +
                        "should kill all your process after clean!", new Object[0]);
            }
            SharePatchFileUtil.deleteDir(this.patchDirectory);
        }
    }

    public void cleanPatchByVersion(String versionName) {
        if (this.patchDirectory != null && versionName != null) {
            SharePatchFileUtil.deleteDir(this.patchDirectory.getAbsolutePath() + "/" + versionName);
        }
    }

    public long getTinkerRomSpace() {
        if (this.patchDirectory == null) {
            return 0;
        }
        return SharePatchFileUtil.getFileOrDirectorySize(this.patchDirectory) /
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    public void cleanPatchByVersion(File patchFile) {
        if (this.patchDirectory != null && patchFile != null && patchFile.exists()) {
            cleanPatchByVersion(SharePatchFileUtil.getPatchVersionDirectory(SharePatchFileUtil.getMD5(patchFile)));
        }
    }
}
