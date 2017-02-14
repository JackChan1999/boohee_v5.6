package com.zxinsight.common.util;

import java.io.Closeable;
import java.io.IOException;

public class g {
    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
