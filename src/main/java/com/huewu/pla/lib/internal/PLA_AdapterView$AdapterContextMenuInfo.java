package com.huewu.pla.lib.internal;

import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

public class PLA_AdapterView$AdapterContextMenuInfo implements ContextMenuInfo {
    public long id;
    public int  position;
    public View targetView;

    public PLA_AdapterView$AdapterContextMenuInfo(View targetView, int position, long id) {
        this.targetView = targetView;
        this.position = position;
        this.id = id;
    }
}
