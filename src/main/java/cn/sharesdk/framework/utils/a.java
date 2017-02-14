package cn.sharesdk.framework.utils;

import android.util.Base64;
import com.mob.tools.network.KVPair;
import com.qiniu.android.common.Constants;
import com.qiniu.android.http.Client;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class a {
    private b a = new b();
    private b b = new b("-._~", false);

    public enum a {
        HMAC_SHA1,
        PLAINTEXT
    }

    public static class b {
        public String a;
        public String b;
        public String c;
        public String d;
        public String e;
    }

    private ArrayList<KVPair<String>> a(long j, String str) {
        ArrayList<KVPair<String>> arrayList = new ArrayList();
        arrayList.add(new KVPair("oauth_consumer_key", this.a.a));
        arrayList.add(new KVPair("oauth_signature_method", str));
        arrayList.add(new KVPair("oauth_timestamp", String.valueOf(j / 1000)));
        arrayList.add(new KVPair("oauth_nonce", String.valueOf(j)));
        arrayList.add(new KVPair("oauth_version", "1.0"));
        String str2 = this.a.c;
        if (str2 != null && str2.length() > 0) {
            arrayList.add(new KVPair("oauth_token", str2));
        }
        return arrayList;
    }

    private ArrayList<KVPair<String>> a(long j, ArrayList<KVPair<String>> arrayList, String str) {
        Iterator it;
        int i = 0;
        HashMap hashMap = new HashMap();
        if (arrayList != null) {
            it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair kVPair = (KVPair) it.next();
                hashMap.put(a(kVPair.name), a((String) kVPair.value));
            }
        }
        ArrayList a = a(j, str);
        if (a != null) {
            it = a.iterator();
            while (it.hasNext()) {
                kVPair = (KVPair) it.next();
                hashMap.put(a(kVPair.name), a((String) kVPair.value));
            }
        }
        String[] strArr = new String[hashMap.size()];
        int i2 = 0;
        for (Entry key : hashMap.entrySet()) {
            strArr[i2] = (String) key.getKey();
            i2++;
        }
        Arrays.sort(strArr);
        ArrayList<KVPair<String>> arrayList2 = new ArrayList();
        i2 = strArr.length;
        while (i < i2) {
            String str2 = strArr[i];
            arrayList2.add(new KVPair(str2, hashMap.get(str2)));
            i++;
        }
        return arrayList2;
    }

    private ArrayList<KVPair<String>> a(String str, String str2, ArrayList<KVPair<String>> arrayList, a aVar) {
        Object trim;
        String str3 = null;
        long currentTimeMillis = System.currentTimeMillis();
        switch (aVar) {
            case HMAC_SHA1:
                str3 = "HMAC-SHA1";
                Key secretKeySpec = new SecretKeySpec((a(this.a.b) + '&' + a(this.a.d)).getBytes(Constants.UTF_8), "HMAC-SHA1");
                Mac instance = Mac.getInstance("HMAC-SHA1");
                instance.init(secretKeySpec);
                trim = new String(Base64.encode(instance.doFinal((str2 + '&' + a(str.toLowerCase()) + '&' + a(b(a(currentTimeMillis, (ArrayList) arrayList, str3)))).getBytes(Constants.UTF_8)), 0)).trim();
                break;
            case PLAINTEXT:
                str3 = "PLAINTEXT";
                trim = a(this.a.b) + '&' + a(this.a.d);
                break;
            default:
                trim = null;
                break;
        }
        ArrayList<KVPair<String>> a = a(currentTimeMillis, str3);
        a.add(new KVPair("oauth_signature", trim));
        return a;
    }

    private String b(ArrayList<KVPair<String>> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            KVPair kVPair = (KVPair) it.next();
            if (i > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(kVPair.name).append('=').append((String) kVPair.value);
            i++;
        }
        return stringBuilder.toString();
    }

    public b a() {
        return this.a;
    }

    public String a(String str) {
        return str == null ? "" : this.b.escape(str);
    }

    public ArrayList<KVPair<String>> a(String str, ArrayList<KVPair<String>> arrayList) {
        return a(str, (ArrayList) arrayList, a.HMAC_SHA1);
    }

    public ArrayList<KVPair<String>> a(String str, ArrayList<KVPair<String>> arrayList, a aVar) {
        return a(str, com.tencent.connect.common.Constants.HTTP_POST, arrayList, aVar);
    }

    public ArrayList<KVPair<String>> a(ArrayList<KVPair<String>> arrayList) {
        StringBuilder stringBuilder = new StringBuilder("OAuth ");
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            KVPair kVPair = (KVPair) it.next();
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(kVPair.name).append("=\"").append(a((String) kVPair.value)).append(com.alipay.sdk.sys.a.e);
            i++;
        }
        ArrayList<KVPair<String>> arrayList2 = new ArrayList();
        arrayList2.add(new KVPair("Authorization", stringBuilder.toString()));
        arrayList2.add(new KVPair("Content-Type", Client.FormMime));
        return arrayList2;
    }

    public void a(String str, String str2) {
        this.a.c = str;
        this.a.d = str2;
    }

    public void a(String str, String str2, String str3) {
        this.a.a = str;
        this.a.b = str2;
        this.a.e = str3;
    }

    public ArrayList<KVPair<String>> b(String str, ArrayList<KVPair<String>> arrayList) {
        return b(str, arrayList, a.HMAC_SHA1);
    }

    public ArrayList<KVPair<String>> b(String str, ArrayList<KVPair<String>> arrayList, a aVar) {
        return a(str, "GET", arrayList, aVar);
    }

    public ArrayList<KVPair<String>> c(String str, ArrayList<KVPair<String>> arrayList, a aVar) {
        return a(str, "PUT", arrayList, aVar);
    }
}
