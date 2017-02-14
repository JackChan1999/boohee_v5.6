package cn.sharesdk.framework;

import android.content.Context;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.statistics.b.f.a;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import java.util.HashMap;

public abstract class Service {
    private Context a;
    private String b;

    public static abstract class ServiceEvent {
        private final int PLATFORM = 1;
        protected Service service;

        public ServiceEvent(Service service) {
            this.service = service;
        }

        protected HashMap<String, Object> filterShareContent(int i, ShareParams shareParams, HashMap<String, Object> hashMap) {
            a filterShareContent = ShareSDK.getPlatform(ShareSDK.platformIdToName(i)).filterShareContent(shareParams, hashMap);
            HashMap<String, Object> hashMap2 = new HashMap();
            hashMap2.put("shareID", filterShareContent.a);
            hashMap2.put("shareContent", new Hashon().fromJson(filterShareContent.toString()));
            Ln.e("filterShareContent ==>>%s", hashMap2);
            return hashMap2;
        }

        protected HashMap<String, Object> toMap() {
            HashMap<String, Object> hashMap = new HashMap();
            DeviceHelper instance = DeviceHelper.getInstance(this.service.a);
            hashMap.put("deviceid", instance.getDeviceKey());
            hashMap.put("appkey", this.service.getAppKey());
            hashMap.put("apppkg", instance.getPackageName());
            hashMap.put("appver", Integer.valueOf(instance.getAppVersion()));
            hashMap.put("sdkver", Integer.valueOf(this.service.getServiceVersionInt()));
            hashMap.put("plat", Integer.valueOf(1));
            hashMap.put("networktype", instance.getDetailNetworkTypeForStatic());
            hashMap.put("deviceData", instance.getDeviceDataNotAES());
            return hashMap;
        }

        public final String toString() {
            return new Hashon().fromHashMap(toMap());
        }
    }

    void a(Context context) {
        this.a = context;
    }

    void a(String str) {
        this.b = str;
    }

    public String getAppKey() {
        return this.b;
    }

    public Context getContext() {
        return this.a;
    }

    public String getDeviceKey() {
        return DeviceHelper.getInstance(this.a).getDeviceKey();
    }

    protected abstract int getServiceVersionInt();

    public abstract String getServiceVersionName();

    public void onBind() {
    }

    public void onUnbind() {
    }
}
