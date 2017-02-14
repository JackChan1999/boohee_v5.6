package com.boohee.model.status;

import com.google.gson.Gson;

import java.util.List;

public class DraftBean {
    public AttachMent attachMent;
    public String selectPhotoMsg = "";
    public List<String> selectedPictures;
    public String sendTextMsg  = "";
    public String takePhotoMsg = "";
    public String uri          = "";

    public static DraftBean parse(String jsonStr) {
        return (DraftBean) new Gson().fromJson(jsonStr, DraftBean.class);
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }

    public String getSendTextMsg() {
        return this.sendTextMsg;
    }

    public void setSendTextMsg(String sendTextMsg) {
        this.sendTextMsg = sendTextMsg;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
