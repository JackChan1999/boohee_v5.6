package com.baidu.location;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import com.boohee.utility.TimeLinePatterns;
import com.qiniu.android.common.Constants;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class d extends s {
    private static d c1 = null;
    Handler c2;
    String c3;
    String c4;
    String c5;

    private d() {
        this.c5 = null;
        this.c4 = null;
        this.c3 = null;
        this.c2 = null;
        this.c2 = new Handler();
    }

    private boolean U() {
        return (this.c3 == null || new File(c.goto() + File.separator + this.c3).exists()) ? true : for(TimeLinePatterns.WEB_SCHEME + this.c5 + "/" + this.c3, this.c3);
    }

    private void V() {
        if (this.c4 != null) {
            File file = new File(c.goto() + File.separator + this.c4);
            if (!file.exists() && for(TimeLinePatterns.WEB_SCHEME + this.c5 + "/" + this.c4, this.c4)) {
                File file2 = new File(c.goto() + File.separator + f.replaceFileName);
                if (file2.exists()) {
                    file2.delete();
                }
                try {
                    if(file, file2);
                } catch (Exception e) {
                    file2.delete();
                }
            }
        }
    }

    private Handler W() {
        return this.c2;
    }

    public static d X() {
        if (c1 == null) {
            c1 = new d();
        }
        return c1;
    }

    private void Y() {
        if (this.c5 != null && ar.bU()) {
            new Thread(this) {
                final /* synthetic */ d a;

                {
                    this.a = r1;
                }

                public void run() {
                    if (this.a.U()) {
                        this.a.V();
                    }
                }
            }.start();
        }
    }

    private static boolean for(String str, String str2) {
        File file = new File(c.goto() + File.separator + "tmp");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            httpURLConnection.disconnect();
            fileOutputStream.close();
            if (file.length() < 10240) {
                file.delete();
                return false;
            }
            file.renameTo(new File(c.goto() + File.separator + str2));
            return true;
        } catch (Exception e) {
            file.delete();
            return false;
        }
    }

    public static void if(File file, File file2) throws IOException {
        BufferedOutputStream bufferedOutputStream;
        Throwable th;
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                try {
                    byte[] bArr = new byte[ax.O];
                    while (true) {
                        int read = bufferedInputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        bufferedOutputStream.write(bArr, 0, read);
                    }
                    bufferedOutputStream.flush();
                    if (bufferedInputStream2 != null) {
                        bufferedInputStream2.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedInputStream = bufferedInputStream2;
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedOutputStream = null;
                bufferedInputStream = bufferedInputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedOutputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            throw th;
        }
    }

    void T() {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&sdk=");
        stringBuffer.append(n.V);
        stringBuffer.append("&fw=");
        stringBuffer.append(f.getFrameVersion());
        stringBuffer.append("&suit=");
        stringBuffer.append(2);
        if (az.cn().iP == null) {
            stringBuffer.append("&im=");
            stringBuffer.append(az.cn().iL);
        } else {
            stringBuffer.append("&cu=");
            stringBuffer.append(az.cn().iP);
        }
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&sv=");
        String str = VERSION.RELEASE;
        if (str != null && str.length() > 10) {
            str = str.substring(0, 10);
        }
        stringBuffer.append(str);
        stringBuffer.append("&pack=");
        stringBuffer.append(az.iH);
        this.cR = c.int() + "?&it=" + Jni.l(stringBuffer.toString());
    }

    public void Z() {
        if (System.currentTimeMillis() - e.do().if() > 172800000) {
            W().postDelayed(new Runnable(this) {
                final /* synthetic */ d a;

                {
                    this.a = r1;
                }

                public void run() {
                    if (ar.bU()) {
                        this.a.Q();
                    }
                }
            }, 10000);
        }
    }

    void do(boolean z) {
        if (z) {
            try {
                JSONObject jSONObject = new JSONObject(EntityUtils.toString(this.cS, Constants.UTF_8));
                if ("up".equals(jSONObject.getString(ShareConstants.RES_PATH))) {
                    this.c5 = jSONObject.getString("upath");
                    if (jSONObject.has("u1")) {
                        this.c4 = jSONObject.getString("u1");
                    }
                    if (jSONObject.has("u2")) {
                        this.c3 = jSONObject.getString("u2");
                    }
                    W().post(new Runnable(this) {
                        final /* synthetic */ d a;

                        {
                            this.a = r1;
                        }

                        public void run() {
                            this.a.Y();
                        }
                    });
                }
            } catch (Exception e) {
            }
        }
        e.do().a(System.currentTimeMillis());
    }
}
