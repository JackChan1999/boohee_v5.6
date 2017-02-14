package com.umeng.socialize.view.abs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;

public class SocialPopupDialog extends Dialog {
    private RelativeLayout a;
    private View           b;
    private SwitchListener c;

    public static abstract class a {
        private Context   a;
        private View      b;
        private ViewGroup c;
        private ViewGroup d;
        private View      e;
        private int[]     f;

        public abstract void a(View view);

        public a(Context context) {
            this.a = context;
            this.b = View.inflate(context, ResContainer.getResourceId(context, ResType.LAYOUT,
                    "umeng_socialize_full_alert_dialog"), null);
            this.c = (ViewGroup) this.b.findViewById(ResContainer.getResourceId(context, ResType
                    .ID, "umeng_socialize_first_area"));
            this.d = (ViewGroup) this.b.findViewById(ResContainer.getResourceId(context, ResType
                    .ID, "umeng_socialize_second_area"));
            this.e = this.b.findViewById(ResContainer.getResourceId(context, ResType.ID,
                    "umeng_socialize_title"));
            a(this.e);
        }

        public a a(String str) {
            TextView textView = (TextView) this.b.findViewById(ResContainer.getResourceId(this.a,
                    ResType.ID, "umeng_socialize_first_area_title"));
            if (TextUtils.isEmpty(str)) {
                textView.setVisibility(8);
            } else {
                textView.setText(str);
            }
            return this;
        }

        public a a(int i) {
            this.c.setVisibility(i);
            ((TextView) this.b.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                    "umeng_socialize_first_area_title"))).setVisibility(i);
            return this;
        }

        public a a(int i, int i2) {
            this.f = new int[]{i, i2};
            return this;
        }

        public a b(String str) {
            TextView textView = (TextView) this.b.findViewById(ResContainer.getResourceId(this.a,
                    ResType.ID, "umeng_socialize_second_area_title"));
            if (TextUtils.isEmpty(str)) {
                textView.setVisibility(8);
            } else {
                textView.setText(str);
            }
            return this;
        }

        public a b(int i) {
            this.d.setVisibility(i);
            ((TextView) this.b.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                    "umeng_socialize_second_area_title"))).setVisibility(i);
            return this;
        }

        public a a(View view, LayoutParams layoutParams) {
            if (layoutParams == null) {
                this.c.addView(view);
            } else {
                this.c.addView(view, layoutParams);
            }
            return this;
        }

        public a b(View view, LayoutParams layoutParams) {
            if (layoutParams == null) {
                this.d.addView(view);
            } else {
                this.d.addView(view, layoutParams);
            }
            return this;
        }

        public SocialPopupDialog a() {
            return new SocialPopupDialog(this.a, this.b, this.f);
        }
    }

    public interface SwitchListener {
        void a();

        void b();
    }

    public static class b {
        private View      a;
        private ImageView b;
        private TextView  c;

        public b(Context context) {
            this.a = View.inflate(context, ResContainer.getResourceId(context, ResType.LAYOUT,
                    "umeng_socialize_full_alert_dialog_item"), null);
            this.b = (ImageView) this.a.findViewById(ResContainer.getResourceId(context, ResType
                    .ID, "umeng_socialize_full_alert_dialog_item_icon"));
            this.c = (TextView) this.a.findViewById(ResContainer.getResourceId(context, ResType
                    .ID, "umeng_socialize_full_alert_dialog_item_text"));
        }

        public b a(int i) {
            this.b.setImageResource(i);
            return this;
        }

        public b a(String str) {
            this.c.setText(str);
            return this;
        }

        public b a(OnClickListener onClickListener) {
            this.a.setOnClickListener(onClickListener);
            return this;
        }

        public View a() {
            return this.a;
        }
    }

    public SocialPopupDialog(Context context, View view, int[] iArr) {
        int resourceId;
        super(context, ResContainer.getResourceId(context, ResType.STYLE,
                "umeng_socialize_popup_dialog"));
        this.a = new RelativeLayout(context);
        this.a.setBackgroundDrawable(null);
        this.a.setLayoutParams(new LayoutParams(-1, -1));
        view.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.a.addView(view);
        this.b = View.inflate(context, ResContainer.getResourceId(context, ResType.LAYOUT,
                "umeng_socialize_full_curtain"), null);
        this.a.addView(this.b, new RelativeLayout.LayoutParams(-1, -1));
        this.b.setClickable(false);
        this.b.setOnTouchListener(new a(this));
        setContentView(this.a);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (iArr == null || iArr.length < 2) {
            attributes.height = -1;
            attributes.width = -1;
            resourceId = ResContainer.getResourceId(getContext(), ResType.STYLE,
                    "umeng_socialize_dialog_animations");
        } else {
            attributes.width = iArr[0];
            attributes.height = iArr[1];
            resourceId = ResContainer.getResourceId(getContext(), ResType.STYLE,
                    "umeng_socialize_dialog_anim_fade");
        }
        getWindow().getAttributes().windowAnimations = resourceId;
        getWindow().setAttributes(attributes);
        c();
    }

    public SwitchListener a() {
        return this.c;
    }

    public void a(SwitchListener switchListener) {
        this.c = switchListener;
    }

    public void dismiss() {
        if (this.c != null) {
            this.c.b();
        }
        super.dismiss();
    }

    public void show() {
        if (this.c != null) {
            this.c.a();
        }
        super.show();
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 4 || this.b.getVisibility() != 0) {
            return super.dispatchKeyEvent(keyEvent);
        }
        this.b.setVisibility(8);
        return true;
    }

    public void b() {
        this.b.setVisibility(0);
    }

    public void c() {
        this.b.setVisibility(4);
    }
}
