package com.zxinsight.share.a;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxinsight.common.util.o;
import com.zxinsight.share.d.c;

import java.io.IOException;
import java.util.ArrayList;

public class a extends BaseAdapter {
    private Context           a;
    private ArrayList<String> b;
    private String            c;

    public a(Context context, ArrayList<String> arrayList, String str) {
        this.a = context;
        this.b = arrayList;
        this.c = str;
    }

    public int getCount() {
        return this.b.size();
    }

    public Object getItem(int i) {
        return this.b.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new LinearLayout(this.a);
            view.setOrientation(1);
            view.setPadding(0, 40, 0, 40);
            view.setGravity(17);
            View imageView = new ImageView(this.a);
            imageView.setTag("mw_share_icon");
            LayoutParams layoutParams = new LinearLayout.LayoutParams(o.a(this.a, 45.0f), o.a
                    (this.a, 45.0f));
            View textView = new TextView(this.a);
            textView.setTag("mw_share_text");
            textView.setPadding(0, 20, 0, 0);
            textView.setTextSize(13.0f);
            textView.setTextColor(-16777216);
            textView.setTextColor(-16777216);
            LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            view.addView(imageView, layoutParams);
            view.addView(textView, layoutParams2);
        }
        TextView textView2 = (TextView) view.findViewWithTag("mw_share_text");
        try {
            ((ImageView) view.findViewWithTag("mw_share_icon")).setImageBitmap(c.a((String) this
                    .b.get(i), this.a, this.c));
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView2.setText(c.a((String) this.b.get(i)));
        return view;
    }
}
