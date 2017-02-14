package com.umeng.socialize.bean;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public abstract class SnsPlatform {
    private String  a             = "Default Analytic Descriptor";
    public  boolean isDirectShare = false;
    public SnsAccount                 mAccount;
    public boolean                    mBind;
    public OnSnsPlatformClickListener mClickListener;
    public int                        mGrayIcon;
    public int                        mIcon;
    public int                        mIndex;
    public String                     mKeyword;
    public boolean                    mOauth;
    public SHARE_MEDIA                mPlatform;
    public String                     mShowWord;
    public String                     mUsid;

    public SnsPlatform(String str) {
        this.mKeyword = str;
        this.mPlatform = SHARE_MEDIA.convertToEmun(str);
    }

    public String getEntityDescriptor() {
        return this.a;
    }

    public void setEntityDescriptor(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.a = str;
        }
    }

    public void performClick(Context context, SocializeEntity socializeEntity, SnsPostListener snsPostListener) {
        if (this.mClickListener != null) {
            this.mClickListener.onClick(context, socializeEntity, snsPostListener);
        }
    }
}
