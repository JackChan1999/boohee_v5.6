package com.pingplusplus.android;

public class PingppObject {
    public String               currentChannel;
    public PingppDataCollection dataCollection;
    public boolean              isOne;
    public q                    pingppWxHandler;
    public h                    sdkType;
    public String               wxAppId;
    public int                  wxErrCode;

    private PingppObject() {
        this.wxAppId = null;
        this.currentChannel = null;
        this.pingppWxHandler = null;
        this.wxErrCode = -10;
        this.sdkType = h.SDK;
    }

    public static PingppObject getInstance() {
        return j.a;
    }

    public void setOneType() {
        this.sdkType = h.ONE;
    }
}
