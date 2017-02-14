package com.boohee.one.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import boohee.lib.share.ShareManager;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

import java.io.File;

public class InviteFriendsActivity extends GestureActivity implements OnClickListener {
    private final String shareDesc = "薄荷已经帮助近600万用户成功减去32,000,000斤！加入薄荷，和我一起减肥吧！";
    private final String shareMsg  = "薄荷已经帮助近600万用户成功减去32,000,000斤！加入薄荷，和我一起减肥吧！http://a.app.qq" +
            ".com/o/simple.jsp?pkgname=com.boohee.one&g_f=991653";
    private final String shareUrl  = "http://a.app.qq.com/o/simple.jsp?pkgname=com.boohee" +
            ".one&g_f=991653";

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.hd);
        setTitle("邀请伙伴");
        addListener();
        init();
    }

    private void addListener() {
        findViewById(R.id.txtMsg).setOnClickListener(this);
        findViewById(R.id.sinaWeibo).setOnClickListener(this);
        findViewById(R.id.weichatFriends).setOnClickListener(this);
        findViewById(R.id.circle).setOnClickListener(this);
    }

    private void init() {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtMsg:
                Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:"));
                intent.putExtra("sms_body", "薄荷已经帮助近600万用户成功减去32,000,000斤！加入薄荷，和我一起减肥吧！http://a" +
                        ".app.qq.com/o/simple.jsp?pkgname=com.boohee.one&g_f=991653");
                startActivity(intent);
                return;
            case R.id.sinaWeibo:
            case R.id.weichatFriends:
            case R.id.circle:
                ShareManager.share(this, "邀请伙伴", "薄荷已经帮助近600万用户成功减去32,000," +
                        "000斤！加入薄荷，和我一起减肥吧！http://a.app.qq.com/o/simple.jsp?pkgname=com.boohee" +
                        ".one&g_f=991653");
                return;
            default:
                return;
        }
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent("android.intent.action.SEND");
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain");
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
            }
        }
        intent.putExtra("android.intent.extra.SUBJECT", msgTitle);
        intent.putExtra("android.intent.extra.TEXT", msgText);
        intent.setFlags(268435456);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
}
