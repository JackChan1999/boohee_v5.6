package com.boohee.utility;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.Layout;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class LongClickableLinkMovementMethod extends ScrollingMovementMethod {
    private static final int    CLICK      = 1;
    private static final int    DOWN       = 3;
    private static       Object FROM_BELOW = new Concrete();
    private static final int    UP         = 2;
    private static LongClickableLinkMovementMethod sInstance;
    private Handler handler       = new Handler();
    private float[] lastEvent     = new float[2];
    private boolean longClickable = true;
    private boolean           mHasPerformedLongPress;
    private CheckForLongPress mPendingCheckForLongPress;
    private boolean           pressed;

    class CheckForLongPress implements Runnable {
        MyURLSpan[] spans;
        View        widget;

        public CheckForLongPress(MyURLSpan[] spans, View widget) {
            this.spans = spans;
            this.widget = widget;
        }

        public void run() {
            if (LongClickableLinkMovementMethod.this.isPressed() &&
                    LongClickableLinkMovementMethod.this.longClickable) {
                this.spans[0].onLongClick(this.widget);
                LongClickableLinkMovementMethod.this.mHasPerformedLongPress = true;
            }
        }
    }

    public void setLongClickable(boolean value) {
        this.longClickable = value;
    }

    @SuppressLint({"NewApi"})
    protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode, int
            movementMetaState, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                if (KeyEvent.metaStateHasNoModifiers(movementMetaState) && event.getAction() == 0
                        && event.getRepeatCount() == 0 && action(1, widget, buffer)) {
                    return true;
                }
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
    }

    protected boolean up(TextView widget, Spannable buffer) {
        if (action(2, widget, buffer)) {
            return true;
        }
        return super.up(widget, buffer);
    }

    protected boolean down(TextView widget, Spannable buffer) {
        if (action(3, widget, buffer)) {
            return true;
        }
        return super.down(widget, buffer);
    }

    protected boolean left(TextView widget, Spannable buffer) {
        if (action(2, widget, buffer)) {
            return true;
        }
        return super.left(widget, buffer);
    }

    protected boolean right(TextView widget, Spannable buffer) {
        if (action(3, widget, buffer)) {
            return true;
        }
        return super.right(widget, buffer);
    }

    private boolean action(int what, TextView widget, Spannable buffer) {
        Layout layout = widget.getLayout();
        int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
        int areatop = widget.getScrollY();
        int areabot = (widget.getHeight() + areatop) - padding;
        int linetop = layout.getLineForVertical(areatop);
        int linebot = layout.getLineForVertical(areabot);
        int first = layout.getLineStart(linetop);
        int last = layout.getLineEnd(linebot);
        MyURLSpan[] candidates = (MyURLSpan[]) buffer.getSpans(first, last, MyURLSpan.class);
        int a = Selection.getSelectionStart(buffer);
        int b = Selection.getSelectionEnd(buffer);
        int selStart = Math.min(a, b);
        int selEnd = Math.max(a, b);
        if (selStart < 0 && buffer.getSpanStart(FROM_BELOW) >= 0) {
            selEnd = buffer.length();
            selStart = selEnd;
        }
        if (selStart > last) {
            selEnd = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            selStart = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }
        if (selEnd < first) {
            selEnd = -1;
            selStart = -1;
        }
        int beststart;
        int bestend;
        int i;
        switch (what) {
            case 1:
                if (selStart != selEnd) {
                    MyURLSpan[] link = (MyURLSpan[]) buffer.getSpans(selStart, selEnd, MyURLSpan
                            .class);
                    if (link.length == 1) {
                        link[0].onClick(widget);
                        break;
                    }
                    return false;
                }
                return false;
            case 2:
                beststart = -1;
                bestend = -1;
                for (i = 0; i < candidates.length; i++) {
                    int end = buffer.getSpanEnd(candidates[i]);
                    if ((end < selEnd || selStart == selEnd) && end > bestend) {
                        beststart = buffer.getSpanStart(candidates[i]);
                        bestend = end;
                    }
                }
                if (beststart >= 0) {
                    Selection.setSelection(buffer, bestend, beststart);
                    return true;
                }
                break;
            case 3:
                beststart = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                bestend = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                for (i = 0; i < candidates.length; i++) {
                    int start = buffer.getSpanStart(candidates[i]);
                    if ((start > selStart || selStart == selEnd) && start < beststart) {
                        beststart = start;
                        bestend = buffer.getSpanEnd(candidates[i]);
                    }
                }
                if (bestend < Integer.MAX_VALUE) {
                    Selection.setSelection(buffer, beststart, bestend);
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == 1 || action == 0) {
            int x = (((int) event.getX()) - widget.getTotalPaddingLeft()) + widget.getScrollX();
            int y = (((int) event.getY()) - widget.getTotalPaddingTop()) + widget.getScrollY();
            Layout layout = widget.getLayout();
            int off = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
            MyURLSpan[] link = (MyURLSpan[]) buffer.getSpans(off, off, MyURLSpan.class);
            if (link.length != 0) {
                if (action == 1) {
                    if (!this.mHasPerformedLongPress) {
                        link[0].onClick(widget);
                    }
                    this.pressed = false;
                    this.lastEvent = new float[2];
                } else if (action == 0) {
                    this.pressed = true;
                    this.lastEvent[0] = event.getX();
                    this.lastEvent[1] = event.getY();
                    checkForLongClick(link, widget);
                }
                return true;
            }
        } else if (action == 2) {
            float[] position = new float[]{event.getX(), event.getY()};
            if (Math.sqrt(Math.hypot((double) Math.abs(this.lastEvent[0] - position[0]), (double)
                    Math.abs(this.lastEvent[1] - position[1]))) > ((double) 6)) {
                this.pressed = false;
            }
        } else if (action == 3) {
            this.pressed = false;
            this.lastEvent = new float[2];
        } else {
            this.pressed = false;
            this.lastEvent = new float[2];
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    private void checkForLongClick(MyURLSpan[] spans, View widget) {
        this.mHasPerformedLongPress = false;
        this.mPendingCheckForLongPress = new CheckForLongPress(spans, widget);
        this.handler.postDelayed(this.mPendingCheckForLongPress, (long) ViewConfiguration
                .getLongPressTimeout());
    }

    public void removeLongClickCallback() {
        if (this.mPendingCheckForLongPress != null) {
            this.handler.removeCallbacks(this.mPendingCheckForLongPress);
            this.mPendingCheckForLongPress = null;
        }
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void initialize(TextView widget, Spannable text) {
        Selection.removeSelection(text);
        text.removeSpan(FROM_BELOW);
    }

    public void onTakeFocus(TextView view, Spannable text, int dir) {
        Selection.removeSelection(text);
        if ((dir & 1) != 0) {
            text.setSpan(FROM_BELOW, 0, 0, 34);
        } else {
            text.removeSpan(FROM_BELOW);
        }
    }

    public static LongClickableLinkMovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new LongClickableLinkMovementMethod();
        }
        return sInstance;
    }
}
