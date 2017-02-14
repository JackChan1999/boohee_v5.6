package com.xiaomi.channel.commonutils.misc;

import com.boohee.utils.BlackTech;

public class a {
    public static final boolean a = "@SHIP.TO.2A2FE0D7@".contains("2A2FE0D7");
    public static final boolean b;
    public static final boolean c = "LOGABLE".equalsIgnoreCase("@SHIP.TO.2A2FE0D7@");
    public static final boolean d = "@SHIP.TO.2A2FE0D7@".contains("YY");
    public static       boolean e = "@SHIP.TO.2A2FE0D7@".equalsIgnoreCase("TEST");
    public static final boolean f = "BETA".equalsIgnoreCase("@SHIP.TO.2A2FE0D7@");
    public static final boolean g;
    private static      int     h;

    static {
        boolean z = false;
        boolean z2 = a || "DEBUG".equalsIgnoreCase("@SHIP.TO.2A2FE0D7@");
        b = z2;
        if ("@SHIP.TO.2A2FE0D7@" != null && "@SHIP.TO.2A2FE0D7@".startsWith(BlackTech.API_ENV_RC)) {
            z = true;
        }
        g = z;
        h = 1;
        if ("@SHIP.TO.2A2FE0D7@".equalsIgnoreCase("SANDBOX")) {
            h = 2;
        } else if ("@SHIP.TO.2A2FE0D7@".equalsIgnoreCase("ONEBOX")) {
            h = 3;
        } else {
            h = 1;
        }
    }

    public static void a(int i) {
        h = i;
    }

    public static boolean a() {
        return h == 2;
    }

    public static boolean b() {
        return h == 3;
    }

    public static int c() {
        return h;
    }
}
