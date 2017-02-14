package com.zxinsight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.boohee.one.http.DnspodFree;
import com.umeng.socialize.common.SocializeConstants;
import com.zxinsight.WebViewActivity.JavaScriptInterface;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.e;
import com.zxinsight.common.util.o;

import java.io.File;

@SuppressLint({"ViewConstructor"})
public class ProgressWebView extends WebView {
    public  String      a;
    public  Uri         b;
    public  String      c;
    private ProgressBar d;
    private float       e;
    private float       f;
    private Activity    g;
    private s           h;
    private String i = getSettings().getUserAgentString();

    public ProgressWebView(Activity activity) {
        super(activity);
        this.g = activity;
        this.d = new ProgressBar(activity, null, 16842872);
        this.d.setLayoutParams(new LayoutParams(-1, o.a(activity, 5.0f)));
        addView(this.d);
        setDownloadListener(new p(this));
        setWebChromeClient(new t(this));
        setWebViewClient(new q(this));
        getSettings().setJavaScriptEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setUserAgentString(a(new StringBuilder(this.i)));
    }

    private String a(StringBuilder stringBuilder) {
        stringBuilder.append(SocializeConstants.OP_OPEN_PAREN);
        stringBuilder.append("MagicWindow;");
        if (!TextUtils.isEmpty(DeviceInfoUtils.c(MWConfiguration.getContext()))) {
            stringBuilder.append("av/");
            stringBuilder.append(DeviceInfoUtils.c(MWConfiguration.getContext()));
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.a())) {
            stringBuilder.append("sv/");
            stringBuilder.append(DeviceInfoUtils.a());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.h())) {
            stringBuilder.append("uid/");
            stringBuilder.append(DeviceInfoUtils.h());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.a())) {
            stringBuilder.append("fp/");
            stringBuilder.append(DeviceInfoUtils.a());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.c(MWConfiguration.getContext()))) {
            stringBuilder.append("d/");
            stringBuilder.append(DeviceInfoUtils.c(MWConfiguration.getContext()));
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.j())) {
            stringBuilder.append("m/");
            stringBuilder.append(DeviceInfoUtils.j());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.i())) {
            stringBuilder.append("mf/");
            stringBuilder.append(DeviceInfoUtils.i());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.h())) {
            stringBuilder.append("b/");
            stringBuilder.append(DeviceInfoUtils.h());
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.g(MWConfiguration.getContext()))) {
            stringBuilder.append("c/");
            stringBuilder.append(DeviceInfoUtils.g(MWConfiguration.getContext()));
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        if (!TextUtils.isEmpty(DeviceInfoUtils.d(MWConfiguration.getContext()))) {
            stringBuilder.append("sr/");
            stringBuilder.append(DeviceInfoUtils.d(MWConfiguration.getContext()));
            stringBuilder.append(DnspodFree.IP_SPLIT);
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(),
                SocializeConstants.OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }

    public void destroy() {
        getSettings().setUserAgentString(this.i);
        super.destroy();
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void addJavascriptInterface(Object obj, String str) {
        if (str.equals("JSInterface") && (obj instanceof JavaScriptInterface)) {
            super.addJavascriptInterface(obj, str);
        }
    }

    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) this.d
                .getLayoutParams();
        layoutParams.x = i;
        layoutParams.y = i2;
        this.d.setLayoutParams(layoutParams);
        super.onScrollChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.f = motionEvent.getY();
                break;
            case 1:
                if (this.e - this.f > 0.0f && Math.abs(this.e - this.f) > 25.0f) {
                    if (this.h != null) {
                        this.h.a(true);
                        break;
                    }
                } else if (this.e - this.f < 0.0f && Math.abs(this.e - this.f) > 25.0f && this.h
                        != null) {
                    this.h.a(false);
                    break;
                }
                break;
            case 2:
                this.e = motionEvent.getY();
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setOnScrollChangedCallback(s sVar) {
        this.h = sVar;
    }

    private void a(Activity activity) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        this.a = e.a(MWConfiguration.getContext()) + System.currentTimeMillis() + ".jpg";
        File file = new File(this.a);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        } else if (file.exists()) {
            file.delete();
        }
        this.b = Uri.fromFile(file);
        intent.putExtra("output", this.b);
        activity.startActivityForResult(intent, 2);
    }

    private void b() {
        e.a(this.c);
        this.g.startActivityForResult(new Intent("android.intent.action.PICK", Media
                .EXTERNAL_CONTENT_URI), 3);
    }

    protected final void a() {
        new Builder(this.g).setItems(new String[]{"camera", "photo"}, new r(this)).show();
    }
}
