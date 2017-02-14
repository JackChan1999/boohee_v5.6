package com.zxinsight.share.d;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.zxinsight.CustomStyle;
import com.zxinsight.common.util.k;
import com.zxinsight.share.domain.BMPlatform;
import com.zxinsight.share.domain.b;

import java.util.ArrayList;
import java.util.List;

public class a extends Dialog implements OnItemClickListener {
    protected Context               a;
    private   com.zxinsight.share.a b;
    private   b                     c;
    private   String                d;
    private ArrayList<String> e = new ArrayList();

    public a(Context context, com.zxinsight.share.a aVar, b bVar, String str) {
        super(context);
        this.a = context;
        this.b = aVar;
        this.c = bVar;
        this.d = str;
        List<BMPlatform> openedShare = BMPlatform.getOpenedShare();
        if (openedShare != null && openedShare.size() > 0) {
            for (BMPlatform ordinal : openedShare) {
                switch (b.a[ordinal.ordinal()]) {
                    case 1:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_WXSESSION));
                        break;
                    case 2:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_WXTIMELINE));
                        break;
                    case 3:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_TENCENTWEIBO));
                        break;
                    case 4:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_EMAIL));
                        break;
                    case 5:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_MESSAGE));
                        break;
                    case 6:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_COPYLINK));
                        break;
                    case 7:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_SINAWEIBO));
                        break;
                    case 8:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_RENN));
                        break;
                    case 9:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_QQ));
                        break;
                    case 10:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_QZONE));
                        break;
                    case 11:
                        this.e.add(BMPlatform.getPlatformName(BMPlatform.PLATFORM_MORE_SHARE));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        Window window = getWindow();
        window.setGravity(80);
        window.setBackgroundDrawable(new BitmapDrawable());
        setContentView(a());
    }

    private LinearLayout a() {
        LinearLayout linearLayout = new LinearLayout(this.a);
        linearLayout.setOrientation(1);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, 25, 0, 25);
        linearLayout.setLayoutParams(layoutParams);
        Drawable a = com.zxinsight.common.util.a.a(15.0f, CustomStyle.getSocialPopUpBg(this.d));
        View gridView = new GridView(this.a);
        gridView.setSelector(new BitmapDrawable());
        int size = this.e.size();
        if (size < 3) {
            gridView.setNumColumns(size);
        } else {
            gridView.setNumColumns(3);
        }
        gridView.setBackgroundDrawable(a);
        gridView.setLayoutParams(layoutParams);
        gridView.setPadding(0, 40, 0, 40);
        gridView.setAdapter(new com.zxinsight.share.a.a(this.a, this.e, this.d));
        gridView.setOnItemClickListener(this);
        linearLayout.addView(gridView);
        return linearLayout;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (k.a(this.a)) {
            this.b.a(BMPlatform.getBMPlatformByName((String) this.e.get(i)), this.c);
            dismiss();
        }
    }
}
