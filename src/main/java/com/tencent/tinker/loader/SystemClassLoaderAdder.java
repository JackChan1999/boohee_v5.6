package com.tencent.tinker.loader;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build.VERSION;
import android.util.Log;

import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.ShareReflectUtil;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipFile;

public class SystemClassLoaderAdder {
    private static final String CHECK_DEX_CLASS = "com.tencent.tinker.loader.TinkerTestDexLoad";
    private static final String CHECK_DEX_FIELD = "isPatch";
    private static final String TAG             = "Tinker.ClassLoaderAdder";

    private static final class V14 {
        private V14() {
        }

        private static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                    File optimizedDirectory) throws IllegalArgumentException,
                IllegalAccessException, NoSuchFieldException, InvocationTargetException,
                NoSuchMethodException {
            Object dexPathList = ShareReflectUtil.findField((Object) loader, "pathList").get
                    (loader);
            ShareReflectUtil.expandFieldArray(dexPathList, "dexElements", makeDexElements
                    (dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory));
        }

        private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File
                optimizedDirectory) throws IllegalAccessException, InvocationTargetException,
                NoSuchMethodException {
            return (Object[]) ShareReflectUtil.findMethod(dexPathList, "makeDexElements",
                    ArrayList.class, File.class).invoke(dexPathList, new Object[]{files,
                    optimizedDirectory});
        }
    }

    private static final class V19 {
        private V19() {
        }

        private static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                    File optimizedDirectory) throws IllegalArgumentException,
                IllegalAccessException, NoSuchFieldException, InvocationTargetException,
                NoSuchMethodException, IOException {
            Object dexPathList = ShareReflectUtil.findField((Object) loader, "pathList").get
                    (loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList();
            ShareReflectUtil.expandFieldArray(dexPathList, "dexElements", makeDexElements
                    (dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory,
                            suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                Iterator it = suppressedExceptions.iterator();
                if (it.hasNext()) {
                    IOException e = (IOException) it.next();
                    Log.w(SystemClassLoaderAdder.TAG, "Exception in makeDexElement", e);
                    throw e;
                }
            }
        }

        private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File
                optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws
                IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Method makeDexElements;
            try {
                makeDexElements = ShareReflectUtil.findMethod(dexPathList, "makeDexElements",
                        ArrayList.class, File.class, ArrayList.class);
            } catch (NoSuchMethodException e) {
                Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: makeDexElements" +
                        "(ArrayList,File,ArrayList) failure");
                try {
                    makeDexElements = ShareReflectUtil.findMethod(dexPathList, "makeDexElements",
                            List.class, File.class, List.class);
                } catch (NoSuchMethodException e1) {
                    Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: makeDexElements" +
                            "(List,File,List) failure");
                    throw e1;
                }
            }
            return (Object[]) makeDexElements.invoke(dexPathList, new Object[]{files,
                    optimizedDirectory, suppressedExceptions});
        }
    }

    private static final class V23 {
        private V23() {
        }

        private static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                    File optimizedDirectory) throws IllegalArgumentException,
                IllegalAccessException, NoSuchFieldException, InvocationTargetException,
                NoSuchMethodException, IOException {
            Object dexPathList = ShareReflectUtil.findField((Object) loader, "pathList").get
                    (loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList();
            ShareReflectUtil.expandFieldArray(dexPathList, "dexElements", makePathElements
                    (dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory,
                            suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                Iterator it = suppressedExceptions.iterator();
                if (it.hasNext()) {
                    IOException e = (IOException) it.next();
                    Log.w(SystemClassLoaderAdder.TAG, "Exception in makePathElement", e);
                    throw e;
                }
            }
        }

        private static Object[] makePathElements(Object dexPathList, ArrayList<File> files, File
                optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws
                IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Method makePathElements;
            try {
                makePathElements = ShareReflectUtil.findMethod(dexPathList, "makePathElements",
                        List.class, File.class, List.class);
            } catch (NoSuchMethodException e) {
                Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: makePathElements(List," +
                        "File,List) failure");
                try {
                    makePathElements = ShareReflectUtil.findMethod(dexPathList,
                            "makePathElements", ArrayList.class, File.class, ArrayList.class);
                } catch (NoSuchMethodException e2) {
                    Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: makeDexElements" +
                            "(ArrayList,File,ArrayList) failure");
                    try {
                        Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: try use v19 " +
                                "instead");
                        return V19.makeDexElements(dexPathList, files, optimizedDirectory,
                                suppressedExceptions);
                    } catch (NoSuchMethodException e22) {
                        Log.e(SystemClassLoaderAdder.TAG, "NoSuchMethodException: makeDexElements" +
                                "(List,File,List) failure");
                        throw e22;
                    }
                }
            }
            return (Object[]) makePathElements.invoke(dexPathList, new Object[]{files,
                    optimizedDirectory, suppressedExceptions});
        }
    }

    private static final class V4 {
        private V4() {
        }

        private static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                    File optimizedDirectory) throws IllegalArgumentException,
                IllegalAccessException, NoSuchFieldException, IOException {
            int extraSize = additionalClassPathEntries.size();
            Field pathField = ShareReflectUtil.findField((Object) loader, "path");
            StringBuilder path = new StringBuilder((String) pathField.get(loader));
            String[] extraPaths = new String[extraSize];
            File[] extraFiles = new File[extraSize];
            ZipFile[] extraZips = new ZipFile[extraSize];
            DexFile[] extraDexs = new DexFile[extraSize];
            ListIterator<File> iterator = additionalClassPathEntries.listIterator();
            while (iterator.hasNext()) {
                File additionalEntry = (File) iterator.next();
                String entryPath = additionalEntry.getAbsolutePath();
                path.append(':').append(entryPath);
                int index = iterator.previousIndex();
                extraPaths[index] = entryPath;
                extraFiles[index] = additionalEntry;
                extraZips[index] = new ZipFile(additionalEntry);
                extraDexs[index] = DexFile.loadDex(entryPath, SharePatchFileUtil.optimizedPathFor
                        (additionalEntry, optimizedDirectory), 0);
            }
            pathField.set(loader, path.toString());
            ShareReflectUtil.expandFieldArray(loader, "mPaths", extraPaths);
            ShareReflectUtil.expandFieldArray(loader, "mFiles", extraFiles);
            ShareReflectUtil.expandFieldArray(loader, "mZips", extraZips);
            try {
                ShareReflectUtil.expandFieldArray(loader, "mDexs", extraDexs);
            } catch (Exception e) {
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void installDexes(Application application, PathClassLoader loader, File
            dexOptDir, List<File> files) throws Throwable {
        if (!files.isEmpty()) {
            ClassLoader classLoader = loader;
            if (VERSION.SDK_INT >= 24) {
                classLoader = AndroidNClassLoader.inject(loader, application);
            }
            if (VERSION.SDK_INT >= 23) {
                V23.install(classLoader, files, dexOptDir);
            } else if (VERSION.SDK_INT >= 19) {
                V19.install(classLoader, files, dexOptDir);
            } else if (VERSION.SDK_INT >= 14) {
                V14.install(classLoader, files, dexOptDir);
            } else {
                V4.install(classLoader, files, dexOptDir);
            }
            if (!checkDexInstall()) {
                throw new TinkerRuntimeException(ShareConstants.CHECK_DEX_INSTALL_FAIL);
            }
        }
    }

    private static boolean checkDexInstall() throws ClassNotFoundException, NoSuchFieldException,
            IllegalAccessException {
        boolean isPatch = ((Boolean) ShareReflectUtil.findField(Class.forName(CHECK_DEX_CLASS),
                CHECK_DEX_FIELD).get(null)).booleanValue();
        Log.w(TAG, "checkDexInstall result:" + isPatch);
        return isPatch;
    }
}
