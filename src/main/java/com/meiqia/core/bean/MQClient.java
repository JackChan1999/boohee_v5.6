package com.meiqia.core.bean;

public class MQClient {
    private String AESKey;
    private String bindUserId;
    private String browserId;
    private String enterpriseId;
    private String trackId;
    private String visitId;
    private String visitPageId;

    public String getAESKey() {
        return this.AESKey;
    }

    public String getBindUserId() {
        return this.bindUserId;
    }

    public String getBrowserId() {
        return this.browserId;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
    }

    public String getTrackId() {
        return this.trackId;
    }

    public String getVisitId() {
        return this.visitId;
    }

    public String getVisitPageId() {
        return this.visitPageId;
    }

    public void setAESKey(String str) {
        this.AESKey = str;
    }

    public void setBindUserId(String str) {
        this.bindUserId = str;
    }

    public void setBrowserId(String str) {
        this.browserId = str;
    }

    public void setEnterpriseId(String str) {
        this.enterpriseId = str;
    }

    public void setTrackId(String str) {
        this.trackId = str;
    }

    public void setVisitId(String str) {
        this.visitId = str;
    }

    public void setVisitPageId(String str) {
        this.visitPageId = str;
    }
}
