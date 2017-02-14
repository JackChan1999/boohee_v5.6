package com.meiqia.meiqiasdk.model;

public class PhotoMessage extends BaseMessage {
    private String localPath;
    private String url;

    public PhotoMessage() {
        setItemViewType(0);
        setContentType("photo");
    }

    public PhotoMessage(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
