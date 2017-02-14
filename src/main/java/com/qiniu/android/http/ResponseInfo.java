package com.qiniu.android.http;

import com.qiniu.android.common.Constants;
import com.qiniu.android.dns.Record;

import java.util.Locale;

public final class ResponseInfo {
    public static final int Cancelled             = -2;
    public static final int CannotConnectToHost   = -1004;
    public static final int InvalidArgument       = -4;
    public static final int InvalidFile           = -3;
    public static final int InvalidToken          = -5;
    public static final int NetworkConnectionLost = -1005;
    public static final int NetworkError          = -1;
    public static final int TimedOut              = -1001;
    public static final int UnknownHost           = -1003;
    public static final int ZeroSizeFile          = -6;
    public final double duration;
    public final String error;
    public final String host;
    public final String id = UserAgent.instance().id;
    public final String ip;
    public final String path;
    public final int    port;
    public final String reqId;
    public final long   sent;
    public final int    statusCode;
    public final long timeStamp = (System.currentTimeMillis() / 1000);
    public final String xlog;
    public final String xvia;

    public ResponseInfo(int statusCode, String reqId, String xlog, String xvia, String host,
                        String path, String ip, int port, double duration, long sent, String
                                error) {
        this.statusCode = statusCode;
        this.reqId = reqId;
        this.xlog = xlog;
        this.xvia = xvia;
        this.host = host;
        this.path = path;
        this.duration = duration;
        this.error = error;
        this.ip = ip;
        this.port = port;
        this.sent = sent;
    }

    public static ResponseInfo zeroSize() {
        return new ResponseInfo(-6, "", "", "", "", "", "", -1, 0.0d, 0, "file or data size is " +
                "zero");
    }

    public static ResponseInfo cancelled() {
        return new ResponseInfo(-2, "", "", "", "", "", "", -1, 0.0d, 0, "cancelled by user");
    }

    public static ResponseInfo invalidArgument(String message) {
        return new ResponseInfo(-4, "", "", "", "", "", "", -1, 0.0d, 0, message);
    }

    public static ResponseInfo invalidToken(String message) {
        return new ResponseInfo(-5, "", "", "", "", "", "", -1, 0.0d, 0, message);
    }

    public static ResponseInfo fileError(Exception e) {
        return new ResponseInfo(-3, "", "", "", "", "", "", -1, 0.0d, 0, e.getMessage());
    }

    public boolean isCancelled() {
        return this.statusCode == -2;
    }

    public boolean isOK() {
        return this.statusCode == 200 && this.error == null && this.reqId != null;
    }

    public boolean isNetworkBroken() {
        return this.statusCode == -1 || this.statusCode == -1003 || this.statusCode == -1004 ||
                this.statusCode == -1001 || this.statusCode == NetworkConnectionLost;
    }

    public boolean isServerError() {
        return (this.statusCode >= 500 && this.statusCode < Record.TTL_MIN_SECONDS && this
                .statusCode != 579) || this.statusCode == 996;
    }

    public boolean needSwitchServer() {
        return isNetworkBroken() || isServerError();
    }

    public boolean needRetry() {
        return !isCancelled() && (needSwitchServer() || this.statusCode == 406 || (this
                .statusCode == 200 && this.error != null));
    }

    public boolean isNotQiniu() {
        return this.statusCode < 500 && this.statusCode >= 200 && this.reqId == null;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "{ver:%s,ResponseInfo:%s,status:%d, reqId:%s, " +
                "xlog:%s, xvia:%s, host:%s, path:%s, ip:%s, port:%d, duration:%f s, time:%d, " +
                "sent:%d,error:%s}", new Object[]{Constants.VERSION, this.id, Integer.valueOf
                (this.statusCode), this.reqId, this.xlog, this.xvia, this.host, this.path, this
                .ip, Integer.valueOf(this.port), Double.valueOf(this.duration), Long.valueOf(this.timeStamp), Long.valueOf(this.sent), this.error});
    }
}
