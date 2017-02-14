package cn.sharesdk.framework.statistics.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class c {
    private static c c;
    private Context a;
    private SharedPreferences b = this.a.getSharedPreferences("share_sdk_0", 0);

    private c(Context context) {
        this.a = context.getApplicationContext();
    }

    public static c a(Context context) {
        if (c == null) {
            c = new c(context.getApplicationContext());
        }
        return c;
    }

    public long a() {
        return this.b.getLong("service_time", 0);
    }

    public String a(String str) {
        return this.b.getString(str, "");
    }

    public void a(long j) {
        a("device_time", Long.valueOf(j));
    }

    public void a(String str, int i) {
        Editor edit = this.b.edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public void a(String str, Boolean bool) {
        Editor edit = this.b.edit();
        edit.putBoolean(str, bool.booleanValue());
        edit.commit();
    }

    public void a(String str, Long l) {
        Editor edit = this.b.edit();
        edit.putLong(str, l.longValue());
        edit.commit();
    }

    public void a(String str, String str2) {
        Editor edit = this.b.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public void a(boolean z) {
        a("connect_server", Boolean.valueOf(z));
    }

    public long b(String str) {
        return this.b.getLong(str, 0);
    }

    public void b(long j) {
        a("connect_server_time", Long.valueOf(j));
    }

    public void b(String str, String str2) {
        a("buffered_snsconf_" + str, str2);
    }

    public boolean b() {
        return Boolean.parseBoolean(this.b.getString("upload_device_info", "true"));
    }

    public int c(String str) {
        return this.b.getInt(str, 0);
    }

    public boolean c() {
        return Boolean.parseBoolean(this.b.getString("upload_user_info", "true"));
    }

    public int d() {
        String string = this.b.getString("upload_share_content", "0");
        return "true".equals(string) ? 1 : "false".equals(string) ? -1 : 0;
    }

    public void d(String str) {
        a("upload_device_info", str);
    }

    public String e() {
        return a("device_data");
    }

    public void e(String str) {
        a("upload_user_info", str);
    }

    public String f() {
        return a("device_ext_data");
    }

    public void f(String str) {
        a("upload_share_content", str);
    }

    public Long g() {
        return Long.valueOf(b("device_time"));
    }

    public String g(String str) {
        return a("buffered_snsconf_" + str);
    }

    public void h(String str) {
        a("device_data", str);
    }

    public boolean h() {
        return this.b.getBoolean("connect_server", true);
    }

    public Long i() {
        return Long.valueOf(b("connect_server_time"));
    }

    public void i(String str) {
        a("device_ext_data", str);
    }
}
