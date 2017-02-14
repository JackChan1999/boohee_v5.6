package com.tencent.tinker.lib.service;

import java.io.Serializable;

public class PatchResult implements Serializable {
    public String    baseTinkerID;
    public long      costTime;
    public Throwable e;
    public boolean   isSuccess;
    public boolean   isUpgradePatch;
    public String    patchTinkerID;
    public String    patchVersion;
    public String    rawPatchFilePath;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nPatchResult: \n");
        sb.append("isUpgradePatch:" + this.isUpgradePatch + "\n");
        sb.append("isSuccess:" + this.isSuccess + "\n");
        sb.append("rawPatchFilePath:" + this.rawPatchFilePath + "\n");
        sb.append("costTime:" + this.costTime + "\n");
        if (this.patchVersion != null) {
            sb.append("patchVersion:" + this.patchVersion + "\n");
        }
        if (this.patchTinkerID != null) {
            sb.append("patchTinkerID:" + this.patchTinkerID + "\n");
        }
        if (this.baseTinkerID != null) {
            sb.append("baseTinkerID:" + this.baseTinkerID + "\n");
        }
        if (this.e != null) {
            sb.append("Throwable:" + this.e.getMessage() + "\n");
        }
        return sb.toString();
    }
}
