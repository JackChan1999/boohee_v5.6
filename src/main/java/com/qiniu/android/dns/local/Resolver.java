package com.qiniu.android.dns.local;

import com.loopj.android.http.AsyncHttpClient;
import com.qiniu.android.dns.DnsException;
import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.Record;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public final class Resolver implements IResolver {
    private static final Random random = new Random();
    final InetAddress address;

    public Resolver(InetAddress address) {
        this.address = address;
    }

    public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
        int id;
        synchronized (random) {
            id = random.nextInt() & 255;
        }
        byte[] answer = udpCommunicate(DnsMessage.buildQuery(domain.domain, id));
        if (answer != null) {
            return DnsMessage.parseResponse(answer, id, domain.domain);
        }
        throw new DnsException(domain.domain, "cant get answer");
    }

    private byte[] udpCommunicate(byte[] question) throws IOException {
        Throwable th;
        DatagramSocket socket = null;
        try {
            DatagramSocket socket2 = new DatagramSocket();
            try {
                DatagramPacket packet = new DatagramPacket(question, question.length, this
                        .address, 53);
                socket2.setSoTimeout(10000);
                socket2.send(packet);
                packet = new DatagramPacket(new byte[AsyncHttpClient
                        .DEFAULT_RETRY_SLEEP_TIME_MILLIS], AsyncHttpClient
                        .DEFAULT_RETRY_SLEEP_TIME_MILLIS);
                socket2.receive(packet);
                byte[] data = packet.getData();
                if (socket2 != null) {
                    socket2.close();
                }
                return data;
            } catch (Throwable th2) {
                th = th2;
                socket = socket2;
                if (socket != null) {
                    socket.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (socket != null) {
                socket.close();
            }
            throw th;
        }
    }
}
