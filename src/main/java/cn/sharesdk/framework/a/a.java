package cn.sharesdk.framework.a;

import android.text.TextUtils;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.network.HTTPPart;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.RawNetworkCallback;
import java.util.ArrayList;

public class a extends NetworkHelper {
    private static a a = null;

    private a() {
    }

    public static a a() {
        if (a == null) {
            a = new a();
        }
        return a;
    }

    private void a(String str, int i) {
        if (!TextUtils.isEmpty(str) && i > 0) {
            ShareSDK.logApiEvent(str, i);
        }
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, String str2, int i) {
        return a(str, (ArrayList) arrayList, (KVPair) kVPair, null, str2, i);
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2, String str2, int i) {
        return a(str, arrayList, kVPair, arrayList2, null, str2, i);
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3, String str2, int i) {
        a(str2, i);
        return super.httpPost(str, arrayList, kVPair, arrayList2, arrayList3);
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, String str2, int i) {
        return a(str, (ArrayList) arrayList, null, null, str2, i);
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3, String str2, int i) {
        a(str2, i);
        return super.httpGet(str, arrayList, arrayList2, arrayList3);
    }

    public void a(String str, ArrayList<KVPair<String>> arrayList, HTTPPart hTTPPart, RawNetworkCallback rawNetworkCallback, String str2, int i) {
        a(str2, i);
        super.rawPost(str, (ArrayList) arrayList, hTTPPart, rawNetworkCallback);
    }

    public String b(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3, String str2, int i) {
        a(str2, i);
        return super.httpPut(str, arrayList, kVPair, arrayList2, arrayList3);
    }

    public String b(String str, ArrayList<KVPair<String>> arrayList, String str2, int i) {
        return a(str, arrayList, null, str2, i);
    }

    public String b(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<?>> arrayList3, String str2, int i) {
        a(str2, i);
        return super.jsonPost(str, arrayList, arrayList2, arrayList3);
    }
}
