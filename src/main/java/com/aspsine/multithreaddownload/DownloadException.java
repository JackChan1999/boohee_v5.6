package com.aspsine.multithreaddownload;

public class DownloadException extends Exception {
    private int errorCode;
    private String errorMessage;

    public DownloadException(String detailMessage) {
        super(detailMessage);
        this.errorMessage = detailMessage;
    }

    public DownloadException(int errorCode, String detailMessage) {
        this(detailMessage);
        this.errorCode = errorCode;
    }

    public DownloadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessage = detailMessage;
    }

    public DownloadException(int errorCode, String detailMessage, Throwable throwable) {
        this(detailMessage, throwable);
        this.errorCode = errorCode;
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
