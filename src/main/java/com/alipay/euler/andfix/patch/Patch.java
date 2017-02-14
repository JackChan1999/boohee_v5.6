package com.alipay.euler.andfix.patch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Patch implements Comparable<Patch> {
    private static final String CLASSES = "-Classes";
    private static final String CREATED_TIME = "Created-Time";
    private static final String ENTRY_NAME = "META-INF/PATCH.MF";
    private static final String PATCH_CLASSES = "Patch-Classes";
    private static final String PATCH_NAME = "Patch-Name";
    private Map<String, List<String>> mClassesMap;
    private final File mFile;
    private String mName;
    private Date mTime;

    public Patch(File file) throws IOException {
        this.mFile = file;
        init();
    }

    private void init() throws IOException {
        Throwable th;
        JarFile jarFile = null;
        InputStream inputStream = null;
        try {
            JarFile jarFile2 = new JarFile(this.mFile);
            try {
                inputStream = jarFile2.getInputStream(jarFile2.getJarEntry(ENTRY_NAME));
                Attributes main = new Manifest(inputStream).getMainAttributes();
                this.mName = main.getValue(PATCH_NAME);
                this.mTime = new Date(main.getValue(CREATED_TIME));
                this.mClassesMap = new HashMap();
                for (Name attrName : main.keySet()) {
                    String name = attrName.toString();
                    if (name.endsWith(CLASSES)) {
                        List<String> strings = Arrays.asList(main.getValue(attrName).split(","));
                        if (name.equalsIgnoreCase(PATCH_CLASSES)) {
                            this.mClassesMap.put(this.mName, strings);
                        } else {
                            this.mClassesMap.put(name.trim().substring(0, name.length() - 8), strings);
                        }
                    }
                }
                if (jarFile2 != null) {
                    jarFile2.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
                jarFile = jarFile2;
            }
        } catch (Throwable th3) {
            th = th3;
            if (jarFile != null) {
                jarFile.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public String getName() {
        return this.mName;
    }

    public File getFile() {
        return this.mFile;
    }

    public Set<String> getPatchNames() {
        return this.mClassesMap.keySet();
    }

    public List<String> getClasses(String patchName) {
        return (List) this.mClassesMap.get(patchName);
    }

    public Date getTime() {
        return this.mTime;
    }

    public int compareTo(Patch another) {
        return this.mTime.compareTo(another.getTime());
    }
}
