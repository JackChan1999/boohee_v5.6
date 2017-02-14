package com.qiniu.android.common;

import com.boohee.utility.TimeLinePatterns;

public final class Zone {
    public static final Zone zone0 = createZone("upload.qiniu.com", "up.qiniu.com", "183.136.139" +
            ".10", "115.231.182.136");
    public static final Zone zone1 = createZone("upload-z1.qiniu.com", "up-z1.qiniu.com", "106.38" +
            ".227.27", "106.38.227.28");
    public final ServiceAddress up;
    public final ServiceAddress upBackup;

    public Zone(ServiceAddress up, ServiceAddress upBackup) {
        this.up = up;
        this.upBackup = upBackup;
    }

    private static Zone createZone(String upHost, String upHostBackup, String upIp, String upIp2) {
        String[] upIps = new String[]{upIp, upIp2};
        return new Zone(new ServiceAddress(TimeLinePatterns.WEB_SCHEME + upHost, upIps), new
                ServiceAddress(TimeLinePatterns.WEB_SCHEME + upHostBackup, upIps));
    }
}
