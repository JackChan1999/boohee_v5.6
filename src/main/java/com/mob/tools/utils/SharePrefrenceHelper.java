package com.mob.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SharePrefrenceHelper {
    private static SharePrefrenceHelper instance;
    private        Context              context;
    private        SharedPreferences    prefrence;

    private SharePrefrenceHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static SharePrefrenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefrenceHelper(context);
        }
        return instance;
    }

    public Object get(String str) {
        try {
            Object string = getString(str);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream
                    (Base64.decode(string, 2)));
            string = objectInputStream.readObject();
            objectInputStream.close();
            return string;
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public boolean getBoolean(String str) {
        return this.prefrence.getBoolean(str, false);
    }

    public float getFloat(String str) {
        return this.prefrence.getFloat(str, 0.0f);
    }

    public int getInt(String str) {
        return this.prefrence.getInt(str, 0);
    }

    public long getLong(String str) {
        return this.prefrence.getLong(str, 0);
    }

    public String getString(String str) {
        return this.prefrence.getString(str, "");
    }

    public void open(String str) {
        open(str, 0);
    }

    public void open(String str, int i) {
        this.prefrence = this.context.getSharedPreferences(str + "_" + i, 0);
    }

    public void put(String str, Object obj) {
        if (obj != null) {
            try {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream
                        (byteArrayOutputStream);
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                objectOutputStream.close();
                putString(str, Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2));
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
    }

    public void putBoolean(String str, Boolean bool) {
        Editor edit = this.prefrence.edit();
        edit.putBoolean(str, bool.booleanValue());
        edit.commit();
    }

    public void putFloat(String str, Float f) {
        Editor edit = this.prefrence.edit();
        edit.putFloat(str, f.floatValue());
        edit.commit();
    }

    public void putInt(String str, Integer num) {
        Editor edit = this.prefrence.edit();
        edit.putInt(str, num.intValue());
        edit.commit();
    }

    public void putLong(String str, Long l) {
        Editor edit = this.prefrence.edit();
        edit.putLong(str, l.longValue());
        edit.commit();
    }

    public void putString(String str, String str2) {
        Editor edit = this.prefrence.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public void remove(String str) {
        Editor edit = this.prefrence.edit();
        edit.remove(str);
        edit.commit();
    }
}
