package cn.sharesdk.framework;

import android.content.Context;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.statistics.b.f.a;
import java.util.HashMap;

public abstract class CustomPlatform extends Platform {
    public CustomPlatform(Context context) {
        super(context);
    }

    protected abstract boolean checkAuthorize(int i, Object obj);

    protected void doAuthorize(String[] strArr) {
    }

    protected void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
    }

    protected void doShare(ShareParams shareParams) {
    }

    protected final a filterShareContent(ShareParams shareParams, HashMap<String, Object> hashMap) {
        return null;
    }

    protected void follow(String str) {
    }

    protected int getCustomPlatformId() {
        return 1;
    }

    protected void getFriendList(int i, int i2, String str) {
    }

    public abstract String getName();

    protected final int getPlatformId() {
        return -Math.abs(getCustomPlatformId());
    }

    public int getVersion() {
        return 0;
    }

    protected final void initDevInfo(String str) {
    }

    protected final void setNetworkDevinfo() {
    }

    protected void timeline(int i, int i2, String str) {
    }

    protected void userInfor(String str) {
    }
}
