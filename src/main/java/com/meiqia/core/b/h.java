package com.meiqia.core.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class h {
    private SharedPreferences a;
    private Editor            b;

    public h(Context context) {
        this.a = context.getSharedPreferences("Meiqia", 0);
    }

    private void a(String str, boolean z) {
        this.b = this.a.edit();
        this.b.putBoolean(str, z);
        this.b.apply();
    }

    private void b(String str, String str2) {
        this.b = this.a.edit();
        this.b.putString(str, str2);
        this.b.apply();
    }

    private boolean b(String str, boolean z) {
        return this.a.getBoolean(str, z);
    }

    private String c(String str, String str2) {
        return this.a.getString(str, str2);
    }

    public String a() {
        return this.a.getString("meiqia_appkey", null);
    }

    public void a(long j) {
        this.b = this.a.edit();
        this.b.putLong("mq_last_msg_update_time", j);
        this.b.apply();
    }

    public void a(String str) {
        this.b = this.a.edit();
        this.b.putString("meiqia_appkey", str);
        this.b.apply();
    }

    public void a(String str, String str2) {
        b(d.a(str), str2);
    }

    public void a(boolean z) {
        a(m("mq_is_schedulered"), z);
    }

    public long b() {
        return this.a.getLong("mq_last_msg_update_time", System.currentTimeMillis());
    }

    public void b(long j) {
        this.b = this.a.edit();
        this.b.putLong("mq_last_msg_id", j);
        this.b.apply();
    }

    public void b(String str) {
        b(m("mq_track_id"), str);
    }

    public void c(long j) {
        this.b = this.a.edit();
        this.b.putLong(m("mq_conversation_onstop_time"), j);
        this.b.apply();
    }

    public void c(String str) {
        b(m("mq_visit_id"), str);
    }

    public boolean c() {
        return b(m("mq_is_schedulered"), false);
    }

    public String d() {
        return c(m("mq_track_id"), null);
    }

    public void d(String str) {
        b(m("mq_visit_page_id"), str);
    }

    public String e() {
        return c(m("mq_visit_id"), null);
    }

    public void e(String str) {
        b(m("mq_browser_id"), str);
    }

    public String f() {
        return c(m("mq_visit_page_id"), null);
    }

    public void f(String str) {
        b(m("mq_enterprise_id"), str);
    }

    public String g() {
        return c(m("mq_browser_id"), null);
    }

    public void g(String str) {
        b(m("mq_leave_message_templete"), str);
    }

    public String h() {
        return c(m("mq_enterprise_id"), "1");
    }

    public void h(String str) {
        this.b = this.a.edit();
        this.b.putString(m("mq_aes_key"), str);
        this.b.apply();
    }

    public String i() {
        return c(m("mq_leave_message_templete"), "");
    }

    public void i(String str) {
        this.b = this.a.edit();
        this.b.putString(m("mq_dev_infos"), str);
        this.b.apply();
    }

    public String j() {
        return this.a.getString(m("mq_aes_key"), null);
    }

    public void j(String str) {
        this.b = this.a.edit();
        this.b.putString(m("mq_client_infos"), str);
        this.b.apply();
    }

    public String k() {
        return this.a.getString(m("mq_dev_infos"), null);
    }

    public void k(String str) {
        b("mq_user_id", str);
    }

    public String l() {
        return this.a.getString(m("mq_client_infos"), "");
    }

    public void l(String str) {
        b(m("mq_client_avatar_url"), str);
    }

    public String m() {
        return c("mq_user_id", "");
    }

    public String m(String str) {
        return d.a(str + a() + m());
    }

    public String n() {
        return c(m("mq_client_avatar_url"), "");
    }

    public String n(String str) {
        return c(d.a(str), "");
    }

    public long o() {
        return this.a.getLong(m("mq_conversation_onstop_time"), System.currentTimeMillis());
    }
}
