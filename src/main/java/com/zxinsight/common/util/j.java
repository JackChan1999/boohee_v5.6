package com.zxinsight.common.util;

import android.content.Context;

public class j {
    public static int a(Context context, String str, String str2) {
        int i = 0;
        try {
            for (Class cls : Class.forName(context.getPackageName() + ".R").getClasses()) {
                if (cls.getName().split("\\$")[1].equals(str.trim())) {
                    break;
                }
            }
            Class cls2 = null;
            if (cls2 != null) {
                i = cls2.getField(str2.trim()).getInt(cls2);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (SecurityException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (NoSuchFieldException e5) {
        }
        return i;
    }
}
