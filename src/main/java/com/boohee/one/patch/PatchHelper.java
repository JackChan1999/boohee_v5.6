package com.boohee.one.patch;

import android.content.Context;
import android.text.TextUtils;

import com.aspsine.multithreaddownload.CallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequest;
import com.aspsine.multithreaddownload.DownloadRequest.Builder;
import com.boohee.api.OneApi;
import com.boohee.one.MyApplication;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.tinker.BuildInfo;
import com.boohee.utility.Config;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.utils.SystemUtil;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import org.json.JSONObject;

public class PatchHelper {
    public static final  String PATCH_DIR = "/tinkerPatch/";
    private static final String SUFFIX    = ".patch";
    public static final  String TAG       = "Tinker.PatchHelper";
    private static File   patchDir;
    private static File   patchFile;
    private static String patchUrl;
    private static String patchVersion;

    public static void init(Context context) {
        try {
            if (SystemUtil.getProcessName(context).equalsIgnoreCase("com.boohee.one")) {
                patchDir = new File(context.getExternalFilesDir(null), PATCH_DIR);
                if (!patchDir.exists() && !patchDir.mkdirs()) {
                    return;
                }
                if (patchDir.isDirectory()) {
                    getPatch(context);
                } else {
                    patchDir.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getPatch(final Context context) {
        OneApi.getPatch(context, new JsonCallback(context) {
            public void fail(String message) {
            }

            public void onFinish() {
                super.onFinish();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                PatchHelper.handleData(context, object);
            }
        });
    }

    private static void handleData(Context context, JSONObject object) {
        if (object != null) {
            try {
                patchVersion = object.optString("patch_version");
                patchUrl = object.optString("download_url");
                String hashValue = object.optString("hash_value");
                if (!TextUtils.isEmpty(patchVersion)) {
                    if (TextUtils.equals(patchVersion, BuildInfo.ONE_PATCH_VERSION)) {
                        Helper.showLog(TAG, "patch version : " + patchVersion + " is already be " +
                                "patched! just return!");
                        return;
                    }
                    patchFile = new File(patchDir, getPatchName());
                    if (!patchFile.exists() || patchFile.length() <= 0) {
                        downloadPatch(context);
                        return;
                    }
                    Helper.showLog(TAG, "patch version : " + patchVersion + "is already download!" +
                            " just load it!");
                    TinkerInstaller.onReceiveUpgradePatch(MyApplication.getContext(), patchFile
                            .getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getPatchName() {
        return Config.getVersionName() + "_" + patchVersion + SUFFIX;
    }

    private static void downloadPatch(final Context context) {
        if (!TextUtils.isEmpty(patchUrl)) {
            DownloadRequest request = new Builder().setFolder(patchFile.getParentFile()).setTitle
                    (getPatchName()).setUri(patchUrl).build();
            DownloadManager.getInstance().init(context);
            DownloadManager.getInstance().download(request, getPatchName(), new CallBack() {
                public void onStarted() {
                    Helper.showLog(PatchHelper.TAG, "patch begin download : " + PatchHelper
                            .patchUrl);
                }

                public void onConnecting() {
                    Helper.showLog(PatchHelper.TAG, "patch download onConnecting : " +
                            PatchHelper.patchUrl);
                }

                public void onConnected(long total, boolean isRangeSupport) {
                    Helper.showLog(PatchHelper.TAG, "patch download onConnected : " + PatchHelper
                            .patchUrl);
                }

                public void onProgress(long finished, long total, int progress) {
                }

                public void onCompleted() {
                    Helper.showLog(PatchHelper.TAG, "patch download onCompeted! patch url : " +
                            PatchHelper.patchUrl + " patch path --> " + PatchHelper.patchFile
                            .getAbsolutePath());
                    if (PatchHelper.patchFile != null && PatchHelper.patchFile.length() > 0) {
                        MobclickAgent.onEvent(context, Event.tinker_downloaded);
                        TinkerInstaller.onReceiveUpgradePatch(MyApplication.getContext(),
                                PatchHelper.patchFile.getAbsolutePath());
                    }
                }

                public void onDownloadPaused() {
                    Helper.showLog(PatchHelper.TAG, "patch download onDownloadPaused : " +
                            PatchHelper.patchUrl);
                }

                public void onDownloadCanceled() {
                    Helper.showLog(PatchHelper.TAG, "patch download Canceled! : " + PatchHelper
                            .patchUrl);
                }

                public void onFailed(DownloadException e) {
                    Helper.showLog(PatchHelper.TAG, "patch download Failed ! : " + PatchHelper
                            .patchUrl);
                    if (PatchHelper.patchFile != null && PatchHelper.patchFile.exists()) {
                        PatchHelper.patchFile.delete();
                    }
                }
            });
        }
    }
}
