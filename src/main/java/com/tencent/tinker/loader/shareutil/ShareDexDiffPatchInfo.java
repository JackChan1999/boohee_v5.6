package com.tencent.tinker.loader.shareutil;

import com.tencent.tinker.loader.TinkerRuntimeException;

import java.util.ArrayList;

public class ShareDexDiffPatchInfo {
    public final String  destMd5InArt;
    public final String  destMd5InDvm;
    public final String  dexDiffMd5;
    public final String  dexMode;
    public final boolean isJarMode;
    public final String  oldDexCrC;
    public final String  path;
    public final String  rawName;
    public final String  realName;

    public ShareDexDiffPatchInfo(String name, String path, String destMd5InDvm, String
            destMd5InArt, String dexDiffMd5, String oldDexCrc, String dexMode) {
        this.rawName = name;
        this.path = path;
        this.destMd5InDvm = destMd5InDvm;
        this.destMd5InArt = destMd5InArt;
        this.dexDiffMd5 = dexDiffMd5;
        this.oldDexCrC = oldDexCrc;
        this.dexMode = dexMode;
        if (dexMode.equals(ShareConstants.DEXMODE_JAR)) {
            this.isJarMode = true;
            if (SharePatchFileUtil.isRawDexFile(name)) {
                this.realName = name + ShareConstants.JAR_SUFFIX;
            } else {
                this.realName = name;
            }
        } else if (dexMode.equals(ShareConstants.DEXMODE_RAW)) {
            this.isJarMode = false;
            this.realName = name;
        } else {
            throw new TinkerRuntimeException("can't recognize dex mode:" + dexMode);
        }
    }

    public static void parseDexDiffPatchInfo(String meta, ArrayList<ShareDexDiffPatchInfo>
            dexList) {
        if (meta != null && meta.length() != 0) {
            for (String line : meta.split("\n")) {
                if (line != null && line.length() > 0) {
                    String[] kv = line.split(",", 7);
                    if (kv != null && kv.length >= 7) {
                        dexList.add(new ShareDexDiffPatchInfo(kv[0].trim(), kv[1].trim(), kv[2]
                                .trim(), kv[3].trim(), kv[4].trim(), kv[5].trim(), kv[6].trim()));
                    }
                }
            }
        }
    }

    public static boolean checkDexDiffPatchInfo(ShareDexDiffPatchInfo info) {
        if (info == null) {
            return false;
        }
        String name = info.rawName;
        String md5 = ShareTinkerInternals.isVmArt() ? info.destMd5InArt : info.destMd5InDvm;
        if (name == null || name.length() <= 0 || md5 == null || md5.length() != 32) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.rawName);
        sb.append(",");
        sb.append(this.path);
        sb.append(",");
        sb.append(this.destMd5InDvm);
        sb.append(",");
        sb.append(this.destMd5InArt);
        sb.append(",");
        sb.append(this.oldDexCrC);
        sb.append(",");
        sb.append(this.dexDiffMd5);
        sb.append(",");
        sb.append(this.dexMode);
        return sb.toString();
    }
}
