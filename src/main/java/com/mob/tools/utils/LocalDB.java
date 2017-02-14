package com.mob.tools.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class LocalDB {
    private File                    dbFile;
    private HashMap<String, Object> map;

    private void commit() {
        if (this.map != null && this.dbFile != null) {
            try {
                if (!this.dbFile.getParentFile().exists()) {
                    this.dbFile.getParentFile().mkdirs();
                }
                synchronized (this.map) {
                    OutputStream fileOutputStream = new FileOutputStream(this.dbFile);
                    if (fileOutputStream.getChannel().tryLock() != null) {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream
                                (fileOutputStream);
                        objectOutputStream.writeObject(this.map);
                        objectOutputStream.flush();
                        objectOutputStream.close();
                    } else {
                        fileOutputStream.close();
                    }
                }
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
    }

    private Object get(String str) {
        return this.map == null ? null : this.map.get(str);
    }

    private void put(String str, Object obj) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        this.map.put(str, obj);
    }

    public boolean getBoolean(String str) {
        Object obj = get(str);
        return obj == null ? false : obj instanceof Boolean ? ((Boolean) obj).booleanValue() :
                false;
    }

    public float getFloat(String str) {
        Object obj = get(str);
        return obj == null ? 0.0f : obj instanceof Integer ? ((Float) obj).floatValue() : 0.0f;
    }

    public int getInt(String str) {
        Object obj = get(str);
        return obj == null ? 0 : obj instanceof Integer ? ((Integer) obj).intValue() : 0;
    }

    public long getLong(String str) {
        Object obj = get(str);
        return obj == null ? 0 : obj instanceof Long ? ((Long) obj).longValue() : 0;
    }

    public Object getObject(String str) {
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

    public String getString(String str) {
        Object obj = get(str);
        return obj == null ? null : obj instanceof String ? (String) obj : String.valueOf(obj);
    }

    public void open(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                this.dbFile = new File(str);
                if (this.dbFile.exists()) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new
                            FileInputStream(this.dbFile));
                    this.map = (HashMap) objectInputStream.readObject();
                    objectInputStream.close();
                }
            }
        } catch (Throwable th) {
            Ln.w(th);
        }
    }

    public void putBoolean(String str, Boolean bool) {
        put(str, bool);
        commit();
    }

    public void putFloat(String str, Float f) {
        put(str, f);
        commit();
    }

    public void putInt(String str, Integer num) {
        put(str, num);
        commit();
    }

    public void putLong(String str, Long l) {
        put(str, l);
        commit();
    }

    public void putObject(String str, Object obj) {
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

    public void putString(String str, String str2) {
        put(str, str2);
        commit();
    }

    public void remove(String str) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        this.map.remove(str);
        commit();
    }
}
