package com.zxinsight;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.zxinsight.common.util.e;

import java.io.File;

class r implements OnClickListener {
    final /* synthetic */ ProgressWebView a;

    r(ProgressWebView progressWebView) {
        this.a = progressWebView;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                this.a.a(this.a.g);
                break;
            case 1:
                this.a.b();
                break;
        }
        this.a.c = e.a(this.a.g) + File.separator + "compress.jpg";
    }
}
