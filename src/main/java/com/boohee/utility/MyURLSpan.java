package com.boohee.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.StoryDetailActivity;
import com.boohee.status.TopicActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utils.Helper;
import com.boohee.utils.SDcard;
import com.boohee.utils.TextUtil;

public class MyURLSpan extends ClickableSpan implements ParcelableSpan {
    static final String TAG = MyURLSpan.class.getSimpleName();
    private final String mURL;

    public MyURLSpan(String url) {
        this.mURL = url;
    }

    public MyURLSpan(Parcel src) {
        this.mURL = src.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mURL);
    }

    public int getSpanTypeId() {
        return 11;
    }

    public int getSpanTypeIdInternal() {
        return getSpanTypeId();
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        writeToParcel(dest, flags);
    }

    public String getURL() {
        return this.mURL;
    }

    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Helper.showLog(TAG, uri.getScheme());
        String url;
        if (uri.getScheme().startsWith("http")) {
            url = uri.toString();
            if (url.contains("http://status.boohee.com/") && url.contains("story") && url
                    .contains("id=")) {
                StoryDetailActivity.comeOn(context, url, TextUtil.checkId(url), null);
            } else {
                BrowserActivity.comeOnBaby(context, null, url);
            }
        } else if (uri.getScheme().startsWith("com.boohee.one.topic")) {
            url = uri.toString();
            intent = new Intent(context, TopicActivity.class);
            intent.putExtra(TopicActivity.EXTRA_TOPIC, getTopic(url));
            context.startActivity(intent);
        } else if (uri.getScheme().startsWith("com.boohee.one")) {
            url = uri.toString();
            intent = new Intent(context, UserTimelineActivity.class);
            intent.putExtra(UserTimelineActivity.NICK_NAME, getUserName(url));
            context.startActivity(intent);
        } else if (uri.getScheme().startsWith(SDcard.BOOHEE_DIR)) {
            BooheeScheme.handleUrl(context, uri.toString());
        } else {
            intent = new Intent("android.intent.action.VIEW", uri);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(intent);
        }
    }

    private String getUserName(String url) {
        return url.substring("com.boohee.one://@".length());
    }

    private String getTopic(String url) {
        return url.substring(TimeLinePatterns.TOPIC_SCHEME.length()).replaceAll("#", "");
    }

    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    public void onLongClick(View widget) {
    }
}
