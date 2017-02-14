package com.mob.tools.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.boohee.utils.Coder;
import com.qiniu.android.common.Constants;
import com.umeng.socialize.common.SocializeConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Data {
    private static final String CHAT_SET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String AES128Decode(String str, byte[] bArr) throws Throwable {
        return (str == null || bArr == null) ? null : new String(AES128Decode(str.getBytes
                ("UTF-8"), bArr), "UTF-8");
    }

    public static byte[] AES128Decode(byte[] bArr, byte[] bArr2) throws Throwable {
        if (bArr == null || bArr2 == null) {
            return null;
        }
        Object obj = new byte[16];
        System.arraycopy(bArr, 0, obj, 0, Math.min(bArr.length, 16));
        Key secretKeySpec = new SecretKeySpec(obj, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/NoPadding", "BC");
        instance.init(2, secretKeySpec);
        byte[] bArr3 = new byte[instance.getOutputSize(bArr2.length)];
        int update = instance.update(bArr2, 0, bArr2.length, bArr3, 0);
        int doFinal = instance.doFinal(bArr3, update) + update;
        return bArr3;
    }

    public static byte[] AES128Encode(String str, String str2) throws Throwable {
        if (str == null || str2 == null) {
            return null;
        }
        Object bytes = str.getBytes("UTF-8");
        Object obj = new byte[16];
        System.arraycopy(bytes, 0, obj, 0, Math.min(bytes.length, 16));
        byte[] bytes2 = str2.getBytes("UTF-8");
        Key secretKeySpec = new SecretKeySpec(obj, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        instance.init(1, secretKeySpec);
        byte[] bArr = new byte[instance.getOutputSize(bytes2.length)];
        instance.doFinal(bArr, instance.update(bytes2, 0, bytes2.length, bArr, 0));
        return bArr;
    }

    public static byte[] AES128Encode(byte[] bArr, String str) throws Throwable {
        if (bArr == null || str == null) {
            return null;
        }
        byte[] bytes = str.getBytes("UTF-8");
        Key secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        instance.init(1, secretKeySpec);
        byte[] bArr2 = new byte[instance.getOutputSize(bytes.length)];
        instance.doFinal(bArr2, instance.update(bytes, 0, bytes.length, bArr2, 0));
        return bArr2;
    }

    public static String Base64AES(String str, String str2) {
        String str3 = null;
        if (!(str == null || str2 == null)) {
            try {
                str3 = Base64.encodeToString(AES128Encode(str2, str), 0);
                if (!TextUtils.isEmpty(str3) && str3.contains("\n")) {
                    str3 = str3.replace("\n", "");
                }
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
        return str3;
    }

    public static String CRC32(byte[] bArr) throws Throwable {
        CRC32 crc32 = new CRC32();
        crc32.update(bArr);
        long value = crc32.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 56))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 48))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 40))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 32))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 24))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 16))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                (value >>> 8))) & 255)}));
        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(((byte) ((int)
                value)) & 255)}));
        while (stringBuilder.charAt(0) == '0') {
            stringBuilder = stringBuilder.deleteCharAt(0);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static String MD5(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            InputStream fileInputStream = new FileInputStream(file);
            byte[] rawMD5 = rawMD5(fileInputStream);
            fileInputStream.close();
            return rawMD5 != null ? HEX.toHex(rawMD5) : null;
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public static String MD5(String str) {
        if (str == null) {
            return null;
        }
        byte[] rawMD5 = rawMD5(str);
        return rawMD5 != null ? HEX.toHex(rawMD5) : null;
    }

    public static String MD5(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] rawMD5 = rawMD5(bArr);
        return rawMD5 != null ? HEX.toHex(rawMD5) : null;
    }

    public static byte[] SHA1(String str) throws Throwable {
        return TextUtils.isEmpty(str) ? null : SHA1(str.getBytes(Constants.UTF_8));
    }

    public static byte[] SHA1(byte[] bArr) throws Throwable {
        MessageDigest instance = MessageDigest.getInstance(Coder.KEY_SHA);
        instance.update(bArr);
        return instance.digest();
    }

    public static String base62(long j) {
        String str = j == 0 ? "0" : "";
        while (j > 0) {
            int i = (int) (j % 62);
            j /= 62;
            str = CHAT_SET.charAt(i) + str;
        }
        return str;
    }

    public static String byteToHex(byte[] bArr) {
        return byteToHex(bArr, 0, bArr.length);
    }

    public static String byteToHex(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bArr == null) {
            return stringBuffer.toString();
        }
        while (i < i2) {
            stringBuffer.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
            i++;
        }
        return stringBuffer.toString();
    }

    public static byte[] rawMD5(InputStream inputStream) {
        byte[] bArr = null;
        if (inputStream != null) {
            try {
                byte[] bArr2 = new byte[1024];
                MessageDigest instance = MessageDigest.getInstance(Coder.KEY_MD5);
                int read = inputStream.read(bArr2);
                while (read != -1) {
                    instance.update(bArr2, 0, read);
                    read = inputStream.read(bArr2);
                }
                bArr = instance.digest();
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
        return bArr;
    }

    public static byte[] rawMD5(String str) {
        byte[] bArr = null;
        if (str != null) {
            try {
                bArr = rawMD5(str.getBytes(Constants.UTF_8));
            } catch (Throwable th) {
                Ln.w(th);
            }
        }
        return bArr;
    }

    public static byte[] rawMD5(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] rawMD5;
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            rawMD5 = rawMD5(byteArrayInputStream);
            byteArrayInputStream.close();
        } catch (Throwable th) {
            Ln.w(th);
            rawMD5 = null;
        }
        return rawMD5;
    }

    public static String urlEncode(String str) {
        try {
            return urlEncode(str, Constants.UTF_8);
        } catch (Throwable th) {
            Ln.w(th);
            return null;
        }
    }

    public static String urlEncode(String str, String str2) throws Throwable {
        String encode = URLEncoder.encode(str, str2);
        return TextUtils.isEmpty(encode) ? encode : encode.replace(SocializeConstants
                .OP_DIVIDER_PLUS, "%20");
    }
}
