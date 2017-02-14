package com.alipay.euler.andfix.security;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import com.boohee.utils.Coder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.security.auth.x500.X500Principal;

public class SecurityChecker {
    private static final String CLASSES_DEX = "classes.dex";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    private static final String SP_MD5 = "-md5";
    private static final String SP_NAME = "_andfix_";
    private static final String TAG = "SecurityChecker";
    private final Context mContext;
    private boolean mDebuggable;
    private PublicKey mPublicKey;

    public SecurityChecker(Context context) {
        this.mContext = context;
        init(this.mContext);
    }

    public boolean verifyOpt(File file) {
        String fingerprint = getFileMD5(file);
        String saved = getFingerprint(file.getName());
        if (fingerprint == null || !TextUtils.equals(fingerprint, saved)) {
            return false;
        }
        return true;
    }

    public void saveOptSig(File file) {
        saveFingerprint(file.getName(), getFileMD5(file));
    }

    public boolean verifyApk(File path) {
        IOException e;
        Throwable th;
        boolean z = false;
        if (this.mDebuggable) {
            Log.d(TAG, "mDebuggable = true");
            return true;
        }
        JarFile jarFile = null;
        try {
            JarFile jarFile2 = new JarFile(path);
            try {
                JarEntry jarEntry = jarFile2.getJarEntry("classes.dex");
                if (jarEntry != null) {
                    loadDigestes(jarFile2, jarEntry);
                    Certificate[] certs = jarEntry.getCertificates();
                    if (certs != null) {
                        z = check(path, certs);
                        if (jarFile2 == null) {
                            return z;
                        }
                        try {
                            jarFile2.close();
                            return z;
                        } catch (IOException e2) {
                            Log.e(TAG, path.getAbsolutePath(), e2);
                            return z;
                        }
                    } else if (jarFile2 == null) {
                        return z;
                    } else {
                        try {
                            jarFile2.close();
                            return z;
                        } catch (IOException e22) {
                            Log.e(TAG, path.getAbsolutePath(), e22);
                            return z;
                        }
                    }
                } else if (jarFile2 == null) {
                    return z;
                } else {
                    try {
                        jarFile2.close();
                        return z;
                    } catch (IOException e222) {
                        Log.e(TAG, path.getAbsolutePath(), e222);
                        return z;
                    }
                }
            } catch (IOException e3) {
                e222 = e3;
                jarFile = jarFile2;
                try {
                    Log.e(TAG, path.getAbsolutePath(), e222);
                    if (jarFile != null) {
                        return z;
                    }
                    try {
                        jarFile.close();
                        return z;
                    } catch (IOException e2222) {
                        Log.e(TAG, path.getAbsolutePath(), e2222);
                        return z;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (jarFile != null) {
                        try {
                            jarFile.close();
                        } catch (IOException e22222) {
                            Log.e(TAG, path.getAbsolutePath(), e22222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                jarFile = jarFile2;
                if (jarFile != null) {
                    jarFile.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e22222 = e4;
            Log.e(TAG, path.getAbsolutePath(), e22222);
            if (jarFile != null) {
                return z;
            }
            jarFile.close();
            return z;
        }
    }

    private void loadDigestes(JarFile jarFile, JarEntry je) throws IOException {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(je);
            while (true) {
                if (is.read(new byte[8192]) <= 0) {
                    break;
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private boolean check(File path, Certificate[] certs) {
        if (certs.length > 0) {
            int i = certs.length - 1;
            while (i >= 0) {
                try {
                    certs[i].verify(this.mPublicKey);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, path.getAbsolutePath(), e);
                    i--;
                }
            }
        }
        return false;
    }

    private String getFileMD5(File file) {
        Exception e;
        Throwable th;
        if (!file.isFile()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        byte[] buffer = new byte[8192];
        try {
            MessageDigest digest = MessageDigest.getInstance(Coder.KEY_MD5);
            FileInputStream in = new FileInputStream(file);
            while (true) {
                try {
                    int len = in.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    digest.update(buffer, 0, len);
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = in;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = in;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    Log.e(TAG, "getFileMD5", e3);
                }
            }
            return new BigInteger(digest.digest()).toString();
        } catch (Exception e4) {
            e = e4;
            try {
                Log.e(TAG, "getFileMD5", e);
                if (fileInputStream == null) {
                    return null;
                }
                try {
                    fileInputStream.close();
                    return null;
                } catch (IOException e32) {
                    Log.e(TAG, "getFileMD5", e32);
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e322) {
                        Log.e(TAG, "getFileMD5", e322);
                    }
                }
                throw th;
            }
        }
    }

    private void saveFingerprint(String fileName, String md5) {
        Editor editor = this.mContext.getSharedPreferences(SP_NAME, 0).edit();
        editor.putString(fileName + SP_MD5, md5);
        editor.commit();
    }

    private String getFingerprint(String fileName) {
        return this.mContext.getSharedPreferences(SP_NAME, 0).getString(fileName + SP_MD5, null);
    }

    private void init(Context context) {
        try {
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toByteArray()));
            this.mDebuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
            this.mPublicKey = cert.getPublicKey();
        } catch (NameNotFoundException e) {
            Log.e(TAG, "init", e);
        } catch (CertificateException e2) {
            Log.e(TAG, "init", e2);
        }
    }
}
