package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.Platform;
import java.util.HashMap;

public interface ThemeShareCallback {
    void doShare(HashMap<Platform, HashMap<String, Object>> hashMap);
}
