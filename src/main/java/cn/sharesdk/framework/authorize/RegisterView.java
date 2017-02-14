package cn.sharesdk.framework.authorize;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;

public class RegisterView extends ResizeLayout {
    private TitleLayout a;
    private RelativeLayout b;
    private WebView c;
    private TextView d;

    public RegisterView(Context context) {
        super(context);
        a(context);
    }

    public RegisterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
    }

    private void a(Context context) {
        setBackgroundColor(-1);
        setOrientation(1);
        int b = b(context);
        this.a = new TitleLayout(context);
        try {
            int bitmapRes = R.getBitmapRes(context, "ssdk_auth_title_back");
            if (bitmapRes > 0) {
                this.a.setBackgroundResource(bitmapRes);
            }
        } catch (Throwable th) {
            Ln.e(th);
        }
        this.a.getBtnRight().setVisibility(8);
        this.a.getTvTitle().setText(R.getStringRes(getContext(), "weibo_oauth_regiseter"));
        addView(this.a);
        View imageView = new ImageView(context);
        imageView.setImageResource(R.getBitmapRes(context, "ssdk_logo"));
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setPadding(0, 0, R.dipToPx(context, 10), 0);
        imageView.setLayoutParams(new LayoutParams(-2, -1));
        imageView.setOnClickListener(new c(this));
        this.a.addView(imageView);
        this.b = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, 0);
        layoutParams.weight = 1.0f;
        this.b.setLayoutParams(layoutParams);
        addView(this.b);
        imageView = new LinearLayout(context);
        imageView.setOrientation(1);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.b.addView(imageView);
        this.d = new TextView(context);
        this.d.setLayoutParams(new LayoutParams(-1, 5));
        this.d.setBackgroundColor(-12929302);
        imageView.addView(this.d);
        this.d.setVisibility(8);
        this.c = new WebView(context);
        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.weight = 1.0f;
        this.c.setLayoutParams(layoutParams2);
        this.c.setWebChromeClient(new d(this, b));
        imageView.addView(this.c);
        this.c.requestFocus();
    }

    private int b(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (!(context instanceof Activity)) {
            return 0;
        }
        WindowManager windowManager = ((Activity) context).getWindowManager();
        if (windowManager == null) {
            return 0;
        }
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public View a() {
        return this.a.getBtnBack();
    }

    public void a(boolean z) {
        this.a.setVisibility(z ? 8 : 0);
    }

    public WebView b() {
        return this.c;
    }

    public TitleLayout c() {
        return this.a;
    }

    public RelativeLayout d() {
        return this.b;
    }
}
