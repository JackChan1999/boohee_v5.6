package com.tencent.tinker.loader.shareutil;

import java.util.ArrayList;

public class ShareBsDiffPatchInfo {
    public String md5;
    public String name;
    public String patchMd5;
    public String path;
    public String rawCrc;

    public ShareBsDiffPatchInfo(String name, String md5, String path, String raw, String patch) {
        this.name = name;
        this.md5 = md5;
        this.rawCrc = raw;
        this.patchMd5 = patch;
        this.path = path;
    }

    public static void parseDiffPatchInfo(String meta, ArrayList<ShareBsDiffPatchInfo> diffList) {
        if (meta != null && meta.length() != 0) {
            for (String line : meta.split("\n")) {
                if (line != null && line.length() > 0) {
                    String[] kv = line.split(",", 5);
                    if (kv != null && kv.length >= 5) {
                        diffList.add(new ShareBsDiffPatchInfo(kv[0].trim(), kv[2].trim(), kv[1]
                                .trim(), kv[3].trim(), kv[4].trim()));
                    }
                }
            }
        }
    }

    public static boolean checkDiffPatchInfo(ShareBsDiffPatchInfo info) {
        if (info == null) {
            return false;
        }
        String name = info.name;
        String md5 = info.md5;
        if (name == null || name.length() <= 0 || md5 == null || md5.length() != 32) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);
        sb.append(",");
        sb.append(this.path);
        sb.append(",");
        sb.append(this.md5);
        sb.append(",");
        sb.append(this.rawCrc);
        sb.append(",");
        sb.append(this.patchMd5);
        return sb.toString();
    }
}
