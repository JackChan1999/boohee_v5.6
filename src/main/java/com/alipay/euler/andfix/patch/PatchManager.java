package com.alipay.euler.andfix.patch;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.alipay.euler.andfix.AndFixManager;
import com.alipay.euler.andfix.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class PatchManager {
    private static final String DIR = "apatch";
    private static final String SP_NAME = "_andfix_";
    private static final String SP_VERSION = "version";
    private static final String SUFFIX = ".apatch";
    private static final String TAG = "PatchManager";
    private final AndFixManager mAndFixManager = new AndFixManager(this.mContext);
    private final Context mContext;
    private final Map<String, ClassLoader> mLoaders = new ConcurrentHashMap();
    private final File mPatchDir = new File(this.mContext.getFilesDir(), DIR);
    private final SortedSet<Patch> mPatchs = new ConcurrentSkipListSet();

    public PatchManager(Context context) {
        this.mContext = context;
    }

    public void init(String appVersion) {
        if (!this.mPatchDir.exists() && !this.mPatchDir.mkdirs()) {
            Log.e(TAG, "patch dir create error.");
        } else if (this.mPatchDir.isDirectory()) {
            SharedPreferences sp = this.mContext.getSharedPreferences(SP_NAME, 0);
            String ver = sp.getString(SP_VERSION, null);
            if (ver == null || !ver.equalsIgnoreCase(appVersion)) {
                cleanPatch();
                sp.edit().putString(SP_VERSION, appVersion).commit();
                return;
            }
            initPatchs();
        } else {
            this.mPatchDir.delete();
        }
    }

    private void initPatchs() {
        for (File file : this.mPatchDir.listFiles()) {
            addPatch(file);
        }
    }

    private Patch addPatch(File file) {
        IOException e;
        Patch patch = null;
        if (!file.getName().endsWith(SUFFIX)) {
            return null;
        }
        try {
            Patch patch2 = new Patch(file);
            try {
                this.mPatchs.add(patch2);
                return patch2;
            } catch (IOException e2) {
                e = e2;
                patch = patch2;
                Log.e(TAG, "addPatch", e);
                return patch;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, "addPatch", e);
            return patch;
        }
    }

    private void cleanPatch() {
        for (File file : this.mPatchDir.listFiles()) {
            this.mAndFixManager.removeOptFile(file);
            if (!FileUtil.deleteFile(file)) {
                Log.e(TAG, file.getName() + " delete error.");
            }
        }
    }

    public void addPatch(String path) throws IOException {
        this.mLoaders.put("*", this.mContext.getClassLoader());
        File src = new File(path);
        File dest = new File(this.mPatchDir, src.getName());
        if (src.exists()) {
            if (!dest.exists()) {
                Log.d(TAG, "patch [" + path + "] has not be loaded.");
                FileUtil.copyFile(src, dest);
            }
            Patch patch = addPatch(dest);
            if (patch != null) {
                loadPatch(patch);
                return;
            }
            return;
        }
        throw new FileNotFoundException(path);
    }

    public void removeAllPatch() {
        cleanPatch();
        this.mContext.getSharedPreferences(SP_NAME, 0).edit().clear().commit();
    }

    public void loadPatch(String patchName, ClassLoader classLoader) {
        this.mLoaders.put(patchName, classLoader);
        for (Patch patch : this.mPatchs) {
            if (patch.getPatchNames().contains(patchName)) {
                this.mAndFixManager.fix(patch.getFile(), classLoader, patch.getClasses(patchName));
            }
        }
    }

    public void loadPatch() {
        this.mLoaders.put("*", this.mContext.getClassLoader());
        for (Patch patch : this.mPatchs) {
            for (String patchName : patch.getPatchNames()) {
                this.mAndFixManager.fix(patch.getFile(), this.mContext.getClassLoader(), patch.getClasses(patchName));
            }
        }
    }

    private void loadPatch(Patch patch) {
        for (String patchName : patch.getPatchNames()) {
            ClassLoader cl;
            if (this.mLoaders.containsKey("*")) {
                cl = this.mContext.getClassLoader();
            } else {
                cl = (ClassLoader) this.mLoaders.get(patchName);
            }
            if (cl != null) {
                this.mAndFixManager.fix(patch.getFile(), cl, patch.getClasses(patchName));
            }
        }
    }
}
