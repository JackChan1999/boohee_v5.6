package com.umeng.socialize.view.wigets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KeyboardListenRelativeLayout extends RelativeLayout {
    public static final byte    KEYBOARD_STATE_HIDE = (byte) -2;
    public static final byte    KEYBOARD_STATE_INIT = (byte) -1;
    public static final byte    KEYBOARD_STATE_SHOW = (byte) -3;
    private             boolean a                   = false;
    private             boolean b                   = false;
    private int                             c;
    private IOnKeyboardStateChangedListener d;

    public interface IOnKeyboardStateChangedListener {
        void a(int i);
    }

    public KeyboardListenRelativeLayout(Context context) {
        super(context);
    }

    public KeyboardListenRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public KeyboardListenRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener
                                                          iOnKeyboardStateChangedListener) {
        this.d = iOnKeyboardStateChangedListener;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.a) {
            this.c = this.c < i4 ? i4 : this.c;
        } else {
            this.a = true;
            this.c = i4;
            if (this.d != null) {
                this.d.a(-1);
            }
        }
        if (this.a && this.c > i4) {
            this.b = true;
            if (this.d != null) {
                this.d.a(-3);
            }
        }
        if (this.a && this.b && this.c == i4) {
            this.b = false;
            if (this.d != null) {
                this.d.a(-2);
            }
        }
    }
}
