package com.umeng.socialize.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiStatus {
    private int                       a;
    private String                    b;
    private Map<SHARE_MEDIA, Integer> c;
    private Map<String, Integer>      d;

    public MultiStatus(int i) {
        this(i, "");
    }

    public MultiStatus(int i, String str) {
        this.b = "";
        this.a = i;
        this.b = str;
        this.c = new HashMap();
        this.d = new HashMap();
    }

    public int getStCode() {
        return this.a;
    }

    public void setStCode(int i) {
        this.a = i;
    }

    public String getRespMsg() {
        return this.b;
    }

    public void setRespMsg(String str) {
        this.b = str;
    }

    public void setPlatformCode(Map<SHARE_MEDIA, Integer> map) {
        this.c.putAll(map);
    }

    public void setInfoCode(Map<String, Integer> map) {
        this.d.putAll(map);
    }

    public Map<SHARE_MEDIA, Integer> getPlatformCode() {
        return this.c;
    }

    public int getPlatformStatus(SHARE_MEDIA share_media) {
        if (this.c.containsKey(share_media)) {
            return ((Integer) this.c.get(share_media)).intValue();
        }
        return StatusCode.ST_CODE_SDK_UNKNOW;
    }

    public int getChildCode(String str) {
        if (this.d.containsKey(str)) {
            return ((Integer) this.d.get(str)).intValue();
        }
        SHARE_MEDIA convertToEmun = SHARE_MEDIA.convertToEmun(str);
        if (convertToEmun == null || !this.c.containsKey(convertToEmun)) {
            return StatusCode.ST_CODE_SDK_NORESPONSE;
        }
        return ((Integer) this.c.get(convertToEmun)).intValue();
    }

    public Map<String, Integer> getAllChildren() {
        Map<String, Integer> hashMap = new HashMap();
        hashMap.putAll(this.d);
        Set<SHARE_MEDIA> keySet = this.c.keySet();
        if (keySet != null) {
            for (SHARE_MEDIA share_media : keySet) {
                hashMap.put(share_media.toString(), (Integer) this.c.get(share_media));
            }
        }
        return hashMap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("status=" + this.a + "{");
        if (!(this.c == null || this.c.keySet() == null)) {
            for (SHARE_MEDIA share_media : this.c.keySet()) {
                stringBuilder.append("[" + share_media.toString() + "=" + ((Integer) this.c.get
                        (share_media)) + "]");
            }
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }
}
