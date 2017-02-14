package com.alipay.euler.andfix;

import android.content.Context;
import android.util.Log;
import com.alipay.euler.andfix.annotation.MethodReplace;
import com.alipay.euler.andfix.security.SecurityChecker;
import dalvik.system.DexFile;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AndFixManager {
    private static final String DIR = "apatch_opt";
    private static final String TAG = "AndFixManager";
    private static Map<String, Class<?>> mFixedClass = new ConcurrentHashMap();
    private final Context mContext;
    private File mOptDir;
    private SecurityChecker mSecurityChecker;
    private boolean mSupport = false;

    public AndFixManager(Context context) {
        this.mContext = context;
        this.mSupport = Compat.isSupport();
        if (this.mSupport) {
            this.mSecurityChecker = new SecurityChecker(this.mContext);
            this.mOptDir = new File(this.mContext.getFilesDir(), DIR);
            if (!this.mOptDir.exists() && !this.mOptDir.mkdirs()) {
                this.mSupport = false;
                Log.e(TAG, "opt dir create error.");
            } else if (!this.mOptDir.isDirectory()) {
                this.mOptDir.delete();
                this.mSupport = false;
            }
        }
    }

    public synchronized void removeOptFile(File file) {
        File optfile = new File(this.mOptDir, file.getName());
        if (optfile.exists() && !optfile.delete()) {
            Log.e(TAG, optfile.getName() + " delete error.");
        }
    }

    public synchronized void fix(String patchPath) {
        fix(new File(patchPath), this.mContext.getClassLoader(), null);
    }

    public synchronized void fix(File file, ClassLoader classLoader, List<String> classes) {
        if (this.mSupport) {
            if (this.mSecurityChecker.verifyApk(file)) {
                try {
                    File optfile = new File(this.mOptDir, file.getName());
                    boolean saveFingerprint = true;
                    if (optfile.exists()) {
                        if (this.mSecurityChecker.verifyOpt(optfile)) {
                            saveFingerprint = false;
                        } else if (!optfile.delete()) {
                        }
                    }
                    final DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(), optfile.getAbsolutePath(), 0);
                    if (saveFingerprint) {
                        this.mSecurityChecker.saveOptSig(optfile);
                    }
                    ClassLoader patchClassLoader = new ClassLoader(classLoader) {
                        protected Class<?> findClass(String className) throws ClassNotFoundException {
                            Class<?> clazz = dexFile.loadClass(className, this);
                            if (clazz == null && className.startsWith(BuildConfig.APPLICATION_ID)) {
                                return Class.forName(className);
                            }
                            if (clazz == null) {
                                return Class.forName(className);
                            }
                            if (clazz != null) {
                                return clazz;
                            }
                            throw new ClassNotFoundException(className);
                        }
                    };
                    Enumeration<String> entrys = dexFile.entries();
                    while (entrys.hasMoreElements()) {
                        String entry = (String) entrys.nextElement();
                        if (classes == null || classes.contains(entry)) {
                            Class<?> clazz = dexFile.loadClass(entry, patchClassLoader);
                            if (clazz != null) {
                                fixClass(clazz, classLoader);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "pacth", e);
                }
            }
        }
    }

    private void fixClass(Class<?> clazz, ClassLoader classLoader) {
        for (Method method : clazz.getDeclaredMethods()) {
            MethodReplace methodReplace = (MethodReplace) method.getAnnotation(MethodReplace.class);
            if (methodReplace != null) {
                String clz = methodReplace.clazz();
                String meth = methodReplace.method();
                if (!(isEmpty(clz) || isEmpty(meth))) {
                    replaceMethod(classLoader, clz, meth, method);
                }
            }
        }
    }

    private void replaceMethod(ClassLoader classLoader, String clz, String meth, Method method) {
        try {
            String key = clz + "@" + classLoader.toString();
            Class<?> clazz = (Class) mFixedClass.get(key);
            if (clazz == null) {
                clazz = AndFix.initTargetClass(classLoader.loadClass(clz));
            }
            if (clazz != null) {
                mFixedClass.put(key, clazz);
                AndFix.addReplaceMethod(clazz.getDeclaredMethod(meth, method.getParameterTypes()), method);
            }
        } catch (Exception e) {
            Log.e(TAG, "replaceMethod", e);
        }
    }

    private static boolean isEmpty(String string) {
        return string == null || string.length() <= 0;
    }
}
