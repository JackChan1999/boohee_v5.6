package com.zxinsight.common.http;

import com.loopj.android.http.AsyncHttpClient;
import com.tencent.connect.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class z {
    static         c                 a = new aa();
    private static c                 b = a;
    private        HttpURLConnection c = null;
    private ad      d;
    private boolean e;
    private boolean f = true;
    private boolean g = false;
    private URL    h;
    private String i;
    private int j = 8192;

    public z(String str, String str2) {
        try {
            this.h = new URL(str);
            this.i = str2;
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    public static z a(String str) {
        return new z(str, "GET");
    }

    public static z b(String str) {
        return new z(str, Constants.HTTP_POST);
    }

    public z a(int i) {
        a().setReadTimeout(i);
        return this;
    }

    public z b(int i) {
        a().setConnectTimeout(i);
        return this;
    }

    private HttpURLConnection j() {
        try {
            HttpURLConnection a = b.a(this.h);
            a.setRequestMethod(this.i);
            a.setReadTimeout(ac.a);
            a.setConnectTimeout(ac.b);
            return a;
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    public HttpURLConnection a() {
        if (this.c == null) {
            this.c = j();
        }
        return this.c;
    }

    private byte[] b(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                int read = inputStream.read(bArr, 0, bArr.length);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public InputStream b() {
        if (d() < 400) {
            try {
                InputStream inputStream = a().getInputStream();
            } catch (IOException e) {
                throw new RestException(e);
            }
        }
        inputStream = a().getErrorStream();
        if (inputStream == null) {
            try {
                inputStream = a().getInputStream();
            } catch (IOException e2) {
                throw new RestException(e2);
            }
        }
        if (!this.g || !AsyncHttpClient.ENCODING_GZIP.equals(c())) {
            return inputStream;
        }
        try {
            return new GZIPInputStream(inputStream);
        } catch (IOException e22) {
            throw new RestException(e22);
        }
    }

    public String c() {
        return d(AsyncHttpClient.HEADER_CONTENT_ENCODING);
    }

    public int d() {
        try {
            h();
            return a().getResponseCode();
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    public byte[] e() {
        return b(b());
    }

    public z c(String str) {
        return b("Accept", str);
    }

    public z f() {
        return c("application/json");
    }

    protected z a(InputStream inputStream, OutputStream outputStream) {
        return (z) new ab(this, inputStream, this.f, inputStream, outputStream).call();
    }

    protected String a(String str, String str2) {
        if (str == null || str.length() == 0) {
            return null;
        }
        int length = str.length();
        int indexOf = str.indexOf(59) + 1;
        if (indexOf == 0 || indexOf == length) {
            return null;
        }
        int indexOf2 = str.indexOf(59, indexOf);
        if (indexOf2 == -1) {
            indexOf2 = indexOf;
            indexOf = length;
        } else {
            int i = indexOf2;
            indexOf2 = indexOf;
            indexOf = i;
        }
        while (indexOf2 < indexOf) {
            int indexOf3 = str.indexOf(61, indexOf2);
            if (indexOf3 != -1 && indexOf3 < indexOf && str2.equals(str.substring(indexOf2,
                    indexOf3).trim())) {
                String trim = str.substring(indexOf3 + 1, indexOf).trim();
                indexOf3 = trim.length();
                if (indexOf3 != 0) {
                    if (indexOf3 > 2 && '\"' == trim.charAt(0) && '\"' == trim.charAt(indexOf3 -
                            1)) {
                        return trim.substring(1, indexOf3 - 1);
                    }
                    return trim;
                }
            }
            indexOf++;
            indexOf2 = str.indexOf(59, indexOf);
            if (indexOf2 == -1) {
                indexOf2 = length;
            }
            i = indexOf2;
            indexOf2 = indexOf;
            indexOf = i;
        }
        return null;
    }

    public String d(String str) {
        g();
        return a().getHeaderField(str);
    }

    public z b(String str, String str2) {
        a().setRequestProperty(str, str2);
        return this;
    }

    public z a(Map<String, String> map) {
        if (map.size() > 0) {
            for (String str : map.keySet()) {
                a().addRequestProperty(str, (String) map.get(str));
            }
        }
        return this;
    }

    protected z g() {
        try {
            return h();
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    protected z h() {
        if (this.d != null) {
            if (this.e) {
                this.d.a("\r\n--00content0boundary00--\r\n");
            }
            if (this.f) {
                try {
                    this.d.close();
                } catch (IOException e) {
                }
            } else {
                this.d.close();
            }
            this.d = null;
        }
        return this;
    }

    public z e(String str) {
        return a(str.getBytes("UTF-8"));
    }

    public z a(byte[] bArr) {
        return a(new ByteArrayInputStream(bArr));
    }

    public z a(InputStream inputStream) {
        try {
            i();
            a(inputStream, this.d);
            return this;
        } catch (IOException e) {
            throw new RestException(e, RestException.RETRY_CONNECTION);
        } catch (IOException e2) {
            throw new RestException(e2);
        }
    }

    public z c(String str, String str2) {
        if (str2 == null || str2.length() <= 0) {
            return b("Content-Type", str);
        }
        String str3 = "; charset=";
        return b("Content-Type", str + "; charset=" + str2);
    }

    protected z i() {
        if (this.d == null) {
            a().setDoOutput(true);
            a().setUseCaches(false);
            this.d = new ad(a().getOutputStream(), a(a().getRequestProperty("Content-Type"), "charset"), this.j);
        }
        return this;
    }
}
