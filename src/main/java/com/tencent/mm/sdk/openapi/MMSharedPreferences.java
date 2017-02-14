package com.tencent.mm.sdk.openapi;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;

import com.tencent.mm.sdk.b.a;
import com.tencent.mm.sdk.c.a.b;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class MMSharedPreferences implements SharedPreferences {
    private final String[] columns = new String[]{"_id", "key", "type", "value"};
    private final ContentResolver cr;
    private       REditor                 editor = null;
    private final HashMap<String, Object> values = new HashMap();

    private static class REditor implements Editor {
        private boolean clear = false;
        private ContentResolver cr;
        private Set<String>         remove = new HashSet();
        private Map<String, Object> values = new HashMap();

        public REditor(ContentResolver contentResolver) {
            this.cr = contentResolver;
        }

        public void apply() {
        }

        public Editor clear() {
            this.clear = true;
            return this;
        }

        public boolean commit() {
            ContentValues contentValues = new ContentValues();
            if (this.clear) {
                this.cr.delete(b.CONTENT_URI, null, null);
                this.clear = false;
            }
            for (String str : this.remove) {
                this.cr.delete(b.CONTENT_URI, "key = ?", new String[]{str});
            }
            for (Entry value : this.values.entrySet()) {
                int i;
                boolean z;
                Object value2 = value.getValue();
                if (value2 == null) {
                    a.a("MicroMsg.SDK.PluginProvider.Resolver", "unresolve failed, null value");
                    i = 0;
                } else if (value2 instanceof Integer) {
                    i = 1;
                } else if (value2 instanceof Long) {
                    i = 2;
                } else if (value2 instanceof String) {
                    i = 3;
                } else if (value2 instanceof Boolean) {
                    i = 4;
                } else if (value2 instanceof Float) {
                    i = 5;
                } else if (value2 instanceof Double) {
                    i = 6;
                } else {
                    a.a("MicroMsg.SDK.PluginProvider.Resolver", "unresolve failed, unknown type="
                            + value2.getClass().toString());
                    i = 0;
                }
                if (i == 0) {
                    z = false;
                } else {
                    contentValues.put("type", Integer.valueOf(i));
                    contentValues.put("value", value2.toString());
                    z = true;
                }
                if (z) {
                    this.cr.update(b.CONTENT_URI, contentValues, "key = ?", new String[]{(String)
                            value.getKey()});
                }
            }
            return true;
        }

        public Editor putBoolean(String str, boolean z) {
            this.values.put(str, Boolean.valueOf(z));
            this.remove.remove(str);
            return this;
        }

        public Editor putFloat(String str, float f) {
            this.values.put(str, Float.valueOf(f));
            this.remove.remove(str);
            return this;
        }

        public Editor putInt(String str, int i) {
            this.values.put(str, Integer.valueOf(i));
            this.remove.remove(str);
            return this;
        }

        public Editor putLong(String str, long j) {
            this.values.put(str, Long.valueOf(j));
            this.remove.remove(str);
            return this;
        }

        public Editor putString(String str, String str2) {
            this.values.put(str, str2);
            this.remove.remove(str);
            return this;
        }

        public Editor putStringSet(String str, Set<String> set) {
            return null;
        }

        public Editor remove(String str) {
            this.remove.add(str);
            return this;
        }
    }

    public MMSharedPreferences(Context context) {
        this.cr = context.getContentResolver();
    }

    private Object getValue(String str) {
        try {
            Cursor query = this.cr.query(b.CONTENT_URI, this.columns, "key = ?", new
                    String[]{str}, null);
            if (query == null) {
                return null;
            }
            Object a = query.moveToFirst() ? com.tencent.mm.sdk.c.a.a.a(query.getInt(query
                    .getColumnIndex("type")), query.getString(query.getColumnIndex("value"))) :
                    null;
            query.close();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean contains(String str) {
        return getValue(str) != null;
    }

    public Editor edit() {
        if (this.editor == null) {
            this.editor = new REditor(this.cr);
        }
        return this.editor;
    }

    public Map<String, ?> getAll() {
        try {
            Cursor query = this.cr.query(b.CONTENT_URI, this.columns, null, null, null);
            if (query == null) {
                return null;
            }
            int columnIndex = query.getColumnIndex("key");
            int columnIndex2 = query.getColumnIndex("type");
            int columnIndex3 = query.getColumnIndex("value");
            while (query.moveToNext()) {
                this.values.put(query.getString(columnIndex), com.tencent.mm.sdk.c.a.a.a(query
                        .getInt(columnIndex2), query.getString(columnIndex3)));
            }
            query.close();
            return this.values;
        } catch (Exception e) {
            e.printStackTrace();
            return this.values;
        }
    }

    public boolean getBoolean(String str, boolean z) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Boolean)) ? z : ((Boolean) value)
                .booleanValue();
    }

    public float getFloat(String str, float f) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Float)) ? f : ((Float) value).floatValue();
    }

    public int getInt(String str, int i) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Integer)) ? i : ((Integer) value).intValue();
    }

    public long getLong(String str, long j) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Long)) ? j : ((Long) value).longValue();
    }

    public String getString(String str, String str2) {
        Object value = getValue(str);
        return (value == null || !(value instanceof String)) ? str2 : (String) value;
    }

    public Set<String> getStringSet(String str, Set<String> set) {
        return null;
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener
                                                                 onSharedPreferenceChangeListener) {
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener
                                                                   onSharedPreferenceChangeListener) {
    }
}
