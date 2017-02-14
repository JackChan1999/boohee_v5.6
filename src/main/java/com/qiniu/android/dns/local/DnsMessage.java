package com.qiniu.android.dns.local;

import com.qiniu.android.dns.DnsException;
import com.qiniu.android.dns.Record;
import com.qiniu.android.dns.util.BitSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

public final class DnsMessage {
    public static byte[] buildQuery(String domain, int id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        DataOutputStream dos = new DataOutputStream(baos);
        BitSet bits = new BitSet();
        bits.set(8);
        try {
            dos.writeShort((short) id);
            dos.writeShort((short) bits.value());
            dos.writeShort(1);
            dos.writeShort(0);
            dos.writeShort(0);
            dos.writeShort(0);
            dos.flush();
            writeQuestion(baos, domain);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static void writeDomain(OutputStream out, String domain) throws IOException {
        for (String s : domain.split("[.。．｡]")) {
            byte[] buffer = IDN.toASCII(s).getBytes();
            out.write(buffer.length);
            out.write(buffer, 0, buffer.length);
        }
        out.write(0);
    }

    private static void writeQuestion(OutputStream out, String domain) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        writeDomain(out, domain);
        dos.writeShort(1);
        dos.writeShort(1);
    }

    public static Record[] parseResponse(byte[] response, int id, String domain) throws
            IOException {
        boolean recursionAvailable = true;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(response));
        int answerId = dis.readUnsignedShort();
        if (answerId != id) {
            throw new DnsException(domain, "the answer id " + answerId + " is not match " + id);
        }
        int header = dis.readUnsignedShort();
        boolean recursionDesired;
        if (((header >> 8) & 1) == 1) {
            recursionDesired = true;
        } else {
            recursionDesired = false;
        }
        if (((header >> 7) & 1) != 1) {
            recursionAvailable = false;
        }
        if (recursionAvailable && recursionDesired) {
            int questionCount = dis.readUnsignedShort();
            int answerCount = dis.readUnsignedShort();
            dis.readUnsignedShort();
            dis.readUnsignedShort();
            readQuestions(dis, response, questionCount);
            return readAnswers(dis, response, answerCount);
        }
        throw new DnsException(domain, "the dns server cant support recursion ");
    }

    private static String readName(DataInputStream dis, byte[] data) throws IOException {
        int c = dis.readUnsignedByte();
        if ((c & 192) == 192) {
            c = ((c & 63) << 8) + dis.readUnsignedByte();
            HashSet<Integer> jumps = new HashSet();
            jumps.add(Integer.valueOf(c));
            return readName(data, c, jumps);
        } else if (c == 0) {
            return "";
        } else {
            byte[] b = new byte[c];
            dis.readFully(b);
            String s = IDN.toUnicode(new String(b));
            String t = readName(dis, data);
            if (t.length() > 0) {
                return s + "." + t;
            }
            return s;
        }
    }

    private static String readName(byte[] data, int offset, HashSet<Integer> jumps) throws
            IOException {
        int c = data[offset] & 255;
        if ((c & 192) == 192) {
            c = ((c & 63) << 8) + (data[offset + 1] & 255);
            if (jumps.contains(Integer.valueOf(c))) {
                throw new DnsException("", "Cyclic offsets detected.");
            }
            jumps.add(Integer.valueOf(c));
            return readName(data, c, jumps);
        } else if (c == 0) {
            return "";
        } else {
            String s = new String(data, offset + 1, c);
            String t = readName(data, (offset + 1) + c, jumps);
            if (t.length() > 0) {
                return s + "." + t;
            }
            return s;
        }
    }

    private static void readQuestions(DataInputStream dis, byte[] data, int count) throws
            IOException {
        int count2 = count;
        while (true) {
            count = count2 - 1;
            if (count2 > 0) {
                readName(dis, data);
                dis.readUnsignedShort();
                dis.readUnsignedShort();
                count2 = count;
            } else {
                return;
            }
        }
    }

    private static Record[] readAnswers(DataInputStream dis, byte[] data, int count) throws
            IOException {
        Record[] ret = new Record[count];
        int offset = 0;
        int count2 = count;
        while (true) {
            count = count2 - 1;
            if (count2 <= 0) {
                return ret;
            }
            int offset2 = offset + 1;
            ret[offset] = readRecord(dis, data);
            offset = offset2;
            count2 = count;
        }
    }

    private static Record readRecord(DataInputStream dis, byte[] data) throws IOException {
        String payload;
        readName(dis, data);
        int type = dis.readUnsignedShort();
        dis.readUnsignedShort();
        long ttl = (((long) dis.readUnsignedShort()) << 16) + ((long) dis.readUnsignedShort());
        int payloadLength = dis.readUnsignedShort();
        switch (type) {
            case 1:
                byte[] ip = new byte[4];
                dis.readFully(ip);
                payload = InetAddress.getByAddress(ip).getHostAddress();
                break;
            case 5:
                payload = readName(dis, data);
                break;
            default:
                payload = null;
                for (int i = 0; i < payloadLength; i++) {
                    dis.readByte();
                }
                break;
        }
        if (payload != null) {
            return new Record(payload, type, (int) ttl, System.currentTimeMillis() / 1000);
        }
        throw new UnknownHostException("no record");
    }
}
