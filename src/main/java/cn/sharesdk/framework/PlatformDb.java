package cn.sharesdk.framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.boohee.status.UserTimelineActivity;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PlatformDb {
    private static final String DB_NAME = "cn_sharesdk_weibodb";
    private SharedPreferences db;
    private String platformNname;
    private int platformVersion;

    public PlatformDb(Context context, String str, int i) {
        this.db = context.getSharedPreferences("cn_sharesdk_weibodb_" + str + "_" + i, 0);
        this.platformNname = str;
        this.platformVersion = i;
    }

    public String exportData() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.putAll(this.db.getAll());
            return new Hashon().fromHashMap(hashMap);
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public String get(String str) {
        return this.db.getString(str, "");
    }

    public long getExpiresIn() {
        long j = 0;
        try {
            return this.db.getLong("expiresIn", 0);
        } catch (Throwable th) {
            return j;
        }
    }

    public long getExpiresTime() {
        return this.db.getLong("expiresTime", 0) + (getExpiresIn() * 1000);
    }

    public String getPlatformNname() {
        return this.platformNname;
    }

    public int getPlatformVersion() {
        return this.platformVersion;
    }

    public String getToken() {
        return this.db.getString("token", "");
    }

    public String getTokenSecret() {
        return this.db.getString("secret", "");
    }

    public String getUserGender() {
        String string = this.db.getString(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "2");
        return "0".equals(string) ? "m" : "1".equals(string) ? "f" : null;
    }

    public String getUserIcon() {
        return this.db.getString(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, "");
    }

    public String getUserId() {
        return this.db.getString("weibo", "");
    }

    public String getUserName() {
        return this.db.getString(UserTimelineActivity.NICK_NAME, "");
    }

    public void importData(String str) {
        try {
            HashMap fromJson = new Hashon().fromJson(str);
            if (fromJson != null) {
                Editor edit = this.db.edit();
                for (Entry entry : fromJson.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof Boolean) {
                        edit.putBoolean((String) entry.getKey(), ((Boolean) value).booleanValue());
                    } else if (value instanceof Float) {
                        edit.putFloat((String) entry.getKey(), ((Float) value).floatValue());
                    } else if (value instanceof Integer) {
                        edit.putInt((String) entry.getKey(), ((Integer) value).intValue());
                    } else if (value instanceof Long) {
                        edit.putLong((String) entry.getKey(), ((Long) value).longValue());
                    } else {
                        edit.putString((String) entry.getKey(), String.valueOf(value));
                    }
                }
                edit.commit();
            }
        } catch (Throwable th) {
            Ln.w(th);
        }
    }

    public boolean isValid() {
        String token = getToken();
        return (token == null || token.length() <= 0) ? false : getExpiresIn() == 0 || getExpiresTime() > System.currentTimeMillis();
    }

    public void put(String str, String str2) {
        Editor edit = this.db.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public void putExpiresIn(long j) {
        Editor edit = this.db.edit();
        edit.putLong("expiresIn", j);
        edit.putLong("expiresTime", System.currentTimeMillis());
        edit.commit();
    }

    public void putToken(String str) {
        Editor edit = this.db.edit();
        edit.putString("token", str);
        edit.commit();
    }

    public void putTokenSecret(String str) {
        Editor edit = this.db.edit();
        edit.putString("secret", str);
        edit.commit();
    }

    public void putUserId(String str) {
        Editor edit = this.db.edit();
        edit.putString("weibo", str);
        edit.commit();
    }

    public void removeAccount() {
        ArrayList arrayList = new ArrayList();
        for (Entry key : this.db.getAll().entrySet()) {
            arrayList.add(key.getKey());
        }
        Editor edit = this.db.edit();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            edit.remove((String) it.next());
        }
        edit.commit();
    }
}
