package com.tencent.tinker.bsdiff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BSUtil {
    public static final int BUFFER_SIZE = 8192;
    public static final int HEADER_SIZE = 32;

    public static final boolean readFromStream(InputStream in, byte[] buf, int offset, int len)
            throws IOException {
        int totalBytesRead = 0;
        while (totalBytesRead < len) {
            int bytesRead = in.read(buf, offset + totalBytesRead, len - totalBytesRead);
            if (bytesRead < 0) {
                return false;
            }
            totalBytesRead += bytesRead;
        }
        return true;
    }

    public static byte[] inputStreamToByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        while (true) {
            int count = in.read(data, 0, 8192);
            if (count == -1) {
                return outStream.toByteArray();
            }
            outStream.write(data, 0, count);
        }
    }
}
