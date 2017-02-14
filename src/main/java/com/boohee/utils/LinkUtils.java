package com.boohee.utils;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkUtils {
    public static final Pattern URL_PATTERN = Pattern.compile("((https?|ftp|boohee)(:\\/\\/[-_" +
            ".!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+))");

    public interface OnClickListener {
        void onClicked();

        void onLinkClicked(String str);
    }

    static class SensibleLinkMovementMethod extends LinkMovementMethod {
        private String  mClickedLink;
        private boolean mLinkClicked;

        SensibleLinkMovementMethod() {
        }

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            if (event.getAction() == 1) {
                this.mLinkClicked = false;
                this.mClickedLink = null;
                int x = (((int) event.getX()) - widget.getTotalPaddingLeft()) + widget.getScrollX();
                int y = (((int) event.getY()) - widget.getTotalPaddingTop()) + widget.getScrollY();
                Layout layout = widget.getLayout();
                int off = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
                ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan
                        .class);
                if (link.length != 0) {
                    SensibleUrlSpan span = link[0];
                    this.mLinkClicked = span.onClickSpan(widget);
                    this.mClickedLink = span.getURL();
                    return this.mLinkClicked;
                }
            }
            super.onTouchEvent(widget, buffer, event);
            return false;
        }

        public boolean isLinkClicked() {
            return this.mLinkClicked;
        }

        public String getClickedLink() {
            return this.mClickedLink;
        }
    }

    static class SensibleUrlSpan extends URLSpan {
        private OnClickListener listener;
        private Pattern         mPattern;

        public SensibleUrlSpan(String url, Pattern pattern) {
            this(url, pattern, null);
        }

        public SensibleUrlSpan(String url, Pattern pattern, OnClickListener listener) {
            super(url);
            this.mPattern = pattern;
            this.listener = listener;
        }

        public boolean onClickSpan(View widget) {
            boolean matched = this.mPattern.matcher(getURL()).matches();
            if (matched) {
                if (this.listener != null) {
                    this.listener.onLinkClicked(getURL());
                } else {
                    super.onClick(widget);
                }
            }
            return matched;
        }
    }

    public static void autoLink(TextView view, OnClickListener listener) {
        autoLink(view, listener, null);
    }

    public static void autoLink(TextView view, final OnClickListener listener, String patternStr) {
        String text = view.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            Pattern pattern;
            Spannable spannable = new SpannableString(text);
            if (TextUtils.isEmpty(patternStr)) {
                pattern = URL_PATTERN;
            } else {
                pattern = Pattern.compile(patternStr);
            }
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                spannable.setSpan(new SensibleUrlSpan(matcher.group(1), pattern, listener),
                        matcher.start(1), matcher.end(1), 33);
            }
            view.setText(spannable, BufferType.SPANNABLE);
            final SensibleLinkMovementMethod method = new SensibleLinkMovementMethod();
            view.setMovementMethod(method);
            if (listener != null) {
                view.setOnClickListener(new android.view.View.OnClickListener() {
                    public void onClick(View v) {
                        if (!method.isLinkClicked()) {
                            listener.onClicked();
                        }
                    }
                });
            }
        }
    }
}
