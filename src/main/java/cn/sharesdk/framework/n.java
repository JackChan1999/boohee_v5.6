package cn.sharesdk.framework;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

class n extends TextView {
    final /* synthetic */ ImageView a;
    final /* synthetic */ TitleLayout b;

    n(TitleLayout titleLayout, Context context, ImageView imageView) {
        this.b = titleLayout;
        this.a = imageView;
        super(context);
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        this.a.setVisibility(i);
    }
}
