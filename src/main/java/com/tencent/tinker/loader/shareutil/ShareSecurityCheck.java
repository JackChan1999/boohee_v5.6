package com.tencent.tinker.loader.shareutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.tencent.tinker.loader.TinkerRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ShareSecurityCheck {
    private static final String    TAG        = "ShareSecurityCheck";
    private static       PublicKey mPublicKey = null;
    private final Context mContext;
    private final HashMap<String, String> metaContentMap = new HashMap();
    private HashMap<String, String> packageProperties;

    public ShareSecurityCheck(Context context) {
        this.mContext = context;
        if (mPublicKey == null) {
            init(this.mContext);
        }
    }

    public HashMap<String, String> getMetaContentMap() {
        return this.metaContentMap;
    }

    public String getTinkerID() {
        if (this.packageProperties != null) {
            return (String) this.packageProperties.get(ShareConstants.TINKER_ID);
        }
        return null;
    }

    public String getNewTinkerID() {
        if (this.packageProperties != null) {
            return (String) this.packageProperties.get(ShareConstants.NEW_TINKER_ID);
        }
        return null;
    }

    public HashMap<String, String> getPackagePropertiesIfPresent() {
        if (this.packageProperties != null) {
            return this.packageProperties;
        }
        String property = (String) this.metaContentMap.get(ShareConstants.PACKAGE_META_FILE);
        if (property == null) {
            return null;
        }
        for (String line : property.split("\n")) {
            if (!(line == null || line.length() <= 0 || line.startsWith("#"))) {
                String[] kv = line.split("=", 2);
                if (kv != null && kv.length >= 2) {
                    if (this.packageProperties == null) {
                        this.packageProperties = new HashMap();
                    }
                    this.packageProperties.put(kv[0].trim(), kv[1].trim());
                }
            }
        }
        return this.packageProperties;
    }

    public boolean verifyPatchMetaSignature(File path) {
        Exception e;
        Throwable th;
        if (path == null || !path.isFile() || !path.exists() || path.length() == 0) {
            return false;
        }
        JarFile jarFile = null;
        try {
            JarFile jarFile2 = new JarFile(path);
            try {
                Enumeration<JarEntry> entries = jarFile2.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (jarEntry != null) {
                        String name = jarEntry.getName();
                        if (!name.startsWith("META-INF/") && name.endsWith(ShareConstants
                                .META_SUFFIX)) {
                            this.metaContentMap.put(name, SharePatchFileUtil.loadDigestes
                                    (jarFile2, jarEntry));
                            Certificate[] certs = jarEntry.getCertificates();
                            if (certs == null) {
                                if (jarFile2 == null) {
                                    return false;
                                }
                                try {
                                    jarFile2.close();
                                    return false;
                                } catch (IOException e2) {
                                    Log.e(TAG, path.getAbsolutePath(), e2);
                                    return false;
                                }
                            } else if (!check(path, certs)) {
                                if (jarFile2 == null) {
                                    return false;
                                }
                                try {
                                    jarFile2.close();
                                    return false;
                                } catch (IOException e22) {
                                    Log.e(TAG, path.getAbsolutePath(), e22);
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (jarFile2 != null) {
                    try {
                        jarFile2.close();
                    } catch (IOException e222) {
                        Log.e(TAG, path.getAbsolutePath(), e222);
                    }
                }
                return true;
            } catch (Exception e3) {
                e = e3;
                jarFile = jarFile2;
            } catch (Throwable th2) {
                th = th2;
                jarFile = jarFile2;
            }
        } catch (Exception e4) {
            e = e4;
            try {
                throw new TinkerRuntimeException(String.format("ShareSecurityCheck file %s, size " +
                        "%d verifyPatchMetaSignature fail", new Object[]{path.getAbsolutePath(),
                        Long.valueOf(path.length())}), e);
            } catch (Throwable th3) {
                th = th3;
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException e2222) {
                        Log.e(TAG, path.getAbsolutePath(), e2222);
                    }
                }
                throw th;
            }
        }
    }

    private boolean check(File path, Certificate[] certs) {
        if (certs.length > 0) {
            int i = certs.length - 1;
            while (i >= 0) {
                try {
                    certs[i].verify(mPublicKey);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, path.getAbsolutePath(), e);
                    i--;
                }
            }
        }
        return false;
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    private void init(Context context) {
        Exception e;
        Throwable th;
        ByteArrayInputStream stream = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), 64);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream stream2 = new ByteArrayInputStream(packageInfo.signatures[0]
                    .toByteArray());
            try {
                mPublicKey = ((X509Certificate) certFactory.generateCertificate(stream2))
                        .getPublicKey();
                SharePatchFileUtil.closeQuietly(stream2);
            } catch (Exception e2) {
                e = e2;
                stream = stream2;
                try {
                    throw new TinkerRuntimeException("ShareSecurityCheck init public key fail", e);
                } catch (Throwable th2) {
                    th = th2;
                    SharePatchFileUtil.closeQuietly(stream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                stream = stream2;
                SharePatchFileUtil.closeQuietly(stream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            throw new TinkerRuntimeException("ShareSecurityCheck init public key fail", e);
        }
    }
}
