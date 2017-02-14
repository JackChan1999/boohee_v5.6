package com.tencent.tinker.loader;

import android.content.Intent;

import com.tencent.tinker.loader.shareutil.ShareBsDiffPatchInfo;
import com.tencent.tinker.loader.shareutil.ShareIntentUtil;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TinkerSoLoader {
    protected static final String SO_MEAT_FILE = "assets/so_meta.txt";
    protected static final String SO_PATH      = "lib";
    private static final   String TAG          = "Tinker.TinkerSoLoader";

    public static boolean checkComplete(String directory, ShareSecurityCheck securityCheck,
                                        Intent intentResult) {
        String meta = (String) securityCheck.getMetaContentMap().get("assets/so_meta.txt");
        if (meta == null) {
            return true;
        }
        ArrayList<ShareBsDiffPatchInfo> libraryList = new ArrayList();
        ShareBsDiffPatchInfo.parseDiffPatchInfo(meta, libraryList);
        if (libraryList.isEmpty()) {
            return true;
        }
        String libraryPath = directory + "/" + "lib" + "/";
        HashMap<String, String> libs = new HashMap();
        Iterator it = libraryList.iterator();
        while (it.hasNext()) {
            ShareBsDiffPatchInfo info = (ShareBsDiffPatchInfo) it.next();
            if (ShareBsDiffPatchInfo.checkDiffPatchInfo(info)) {
                libs.put(info.path + "/" + info.name, info.md5);
            } else {
                intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_PACKAGE_PATCH_CHECK, -4);
                ShareIntentUtil.setIntentReturnCode(intentResult, -9);
                return false;
            }
        }
        File libraryDir = new File(libraryPath);
        if (libraryDir.exists() && libraryDir.isDirectory()) {
            for (String relative : libs.keySet()) {
                File libFile = new File(libraryPath + relative);
                if (!libFile.exists()) {
                    ShareIntentUtil.setIntentReturnCode(intentResult, -17);
                    intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_MISSING_LIB_PATH, libFile
                            .getAbsolutePath());
                    return false;
                }
            }
            intentResult.putExtra(ShareIntentUtil.INTENT_PATCH_LIBS_PATH, libs);
            return true;
        }
        ShareIntentUtil.setIntentReturnCode(intentResult, -16);
        return false;
    }
}
