package com.alipay.sdk.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class c {
    public static byte[] a(byte[] bArr) throws IOException {
        Throwable th;
        ByteArrayInputStream byteArrayInputStream;
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPOutputStream gZIPOutputStream;
        try {
            OutputStream byteArrayOutputStream2;
            ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
            try {
                byteArrayOutputStream2 = new ByteArrayOutputStream();
                try {
                    gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream2);
                } catch (Throwable th2) {
                    th = th2;
                    gZIPOutputStream = null;
                    OutputStream outputStream = byteArrayOutputStream2;
                    byteArrayInputStream = byteArrayInputStream2;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e) {
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (gZIPOutputStream != null) {
                        try {
                            gZIPOutputStream.close();
                        } catch (Exception e3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                gZIPOutputStream = null;
                byteArrayInputStream = byteArrayInputStream2;
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (gZIPOutputStream != null) {
                    gZIPOutputStream.close();
                }
                throw th;
            }
            try {
                byte[] bArr2 = new byte[4096];
                while (true) {
                    int read = byteArrayInputStream2.read(bArr2);
                    if (read == -1) {
                        break;
                    }
                    gZIPOutputStream.write(bArr2, 0, read);
                }
                gZIPOutputStream.flush();
                gZIPOutputStream.finish();
                bArr2 = byteArrayOutputStream2.toByteArray();
                try {
                    byteArrayInputStream2.close();
                } catch (Exception e4) {
                }
                try {
                    byteArrayOutputStream2.close();
                } catch (Exception e5) {
                }
                try {
                    gZIPOutputStream.close();
                } catch (Exception e6) {
                }
                return bArr2;
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = byteArrayOutputStream2;
                byteArrayInputStream = byteArrayInputStream2;
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (gZIPOutputStream != null) {
                    gZIPOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            gZIPOutputStream = null;
            byteArrayInputStream = null;
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (gZIPOutputStream != null) {
                gZIPOutputStream.close();
            }
            throw th;
        }
    }

    public static byte[] b(byte[] bArr) throws IOException {
        Throwable th;
        ByteArrayInputStream byteArrayInputStream;
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPInputStream gZIPInputStream;
        try {
            InputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
            try {
                gZIPInputStream = new GZIPInputStream(byteArrayInputStream2);
                try {
                    byte[] bArr2 = new byte[4096];
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    while (true) {
                        try {
                            int read = gZIPInputStream.read(bArr2, 0, bArr2.length);
                            if (read == -1) {
                                break;
                            }
                            byteArrayOutputStream2.write(bArr2, 0, read);
                        } catch (Throwable th2) {
                            th = th2;
                            byteArrayOutputStream = byteArrayOutputStream2;
                            byteArrayInputStream = byteArrayInputStream2;
                        }
                    }
                    byteArrayOutputStream2.flush();
                    bArr2 = byteArrayOutputStream2.toByteArray();
                    try {
                        byteArrayOutputStream2.close();
                    } catch (Exception e) {
                    }
                    try {
                        gZIPInputStream.close();
                    } catch (Exception e2) {
                    }
                    try {
                        byteArrayInputStream2.close();
                    } catch (Exception e3) {
                    }
                    return bArr2;
                } catch (Throwable th3) {
                    th = th3;
                    inputStream = byteArrayInputStream2;
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e4) {
                    }
                    try {
                        gZIPInputStream.close();
                    } catch (Exception e5) {
                    }
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e6) {
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                gZIPInputStream = null;
                inputStream = byteArrayInputStream2;
                byteArrayOutputStream.close();
                gZIPInputStream.close();
                byteArrayInputStream.close();
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            gZIPInputStream = null;
            byteArrayInputStream = null;
            byteArrayOutputStream.close();
            gZIPInputStream.close();
            byteArrayInputStream.close();
            throw th;
        }
    }
}
