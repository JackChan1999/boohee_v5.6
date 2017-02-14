package com.qiniu.android.utils;

import com.qiniu.android.storage.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Etag {
    public static String data(byte[] data, int offset, int length) {
        try {
            return stream(new ByteArrayInputStream(data, offset, length), (long) length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String data(byte[] data) {
        return data(data, 0, data.length);
    }

    public static String file(File file) throws IOException {
        return stream(new FileInputStream(file), file.length());
    }

    public static String file(String filePath) throws IOException {
        return file(new File(filePath));
    }

    public static String stream(InputStream in, long len) throws IOException {
        if (len == 0) {
            return "Fto5o-5ea0sNMlW_75VgGJCv2AcJ";
        }
        byte[] buffer = new byte[65536];
        byte[][] blocks = new byte[(((int) ((4194304 + len) - 1)) / Configuration.BLOCK_SIZE)][];
        for (int i = 0; i < blocks.length; i++) {
            long read;
            long left = len - (4194304 * ((long) i));
            if (left > 4194304) {
                read = 4194304;
            } else {
                read = left;
            }
            blocks[i] = oneBlock(buffer, in, (int) read);
        }
        return resultEncode(blocks);
    }

    private static byte[] oneBlock(byte[] buffer, InputStream in, int len) throws IOException {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("sha-1");
            int buffSize = buffer.length;
            while (len != 0) {
                int next;
                if (buffSize > len) {
                    next = len;
                } else {
                    next = buffSize;
                }
                in.read(buffer, 0, next);
                sha1.update(buffer, 0, next);
                len -= next;
            }
            return sha1.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String resultEncode(byte[][] sha1s) {
        byte head = (byte) 22;
        byte[] finalHash = sha1s[0];
        int len = finalHash.length;
        byte[] ret = new byte[(len + 1)];
        if (sha1s.length != 1) {
            head = (byte) -106;
            try {
                MessageDigest sha1 = MessageDigest.getInstance("sha-1");
                for (byte[] s : sha1s) {
                    sha1.update(s);
                }
                finalHash = sha1.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
        ret[0] = head;
        System.arraycopy(finalHash, 0, ret, 1, len);
        return UrlSafeBase64.encodeToString(ret);
    }
}
