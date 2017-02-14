package com.huewu.pla.lib.internal;

import android.database.DataSetObserver;
import android.os.Parcelable;

import com.huewu.pla.lib.DebugUtil;

class PLA_AdapterView$AdapterDataSetObserver extends DataSetObserver {
    private Parcelable mInstanceState = null;
    final /* synthetic */ PLA_AdapterView this$0;

    PLA_AdapterView$AdapterDataSetObserver(PLA_AdapterView this$0) {
        this.this$0 = this$0;
    }

    public void onChanged() {
        DebugUtil.LogDebug("data changed by onChanged()");
        this.this$0.mDataChanged = true;
        this.this$0.mOldItemCount = this.this$0.mItemCount;
        this.this$0.mItemCount = this.this$0.getAdapter().getCount();
        if (!this.this$0.getAdapter().hasStableIds() || this.mInstanceState == null || this
                .this$0.mOldItemCount != 0 || this.this$0.mItemCount <= 0) {
            this.this$0.rememberSyncState();
        } else {
            PLA_AdapterView.access$000(this.this$0, this.mInstanceState);
            this.mInstanceState = null;
        }
        this.this$0.requestLayout();
    }

    public void onInvalidated() {
        DebugUtil.LogDebug("data changed by onInvalidated()");
        this.this$0.mDataChanged = true;
        if (this.this$0.getAdapter().hasStableIds()) {
            this.mInstanceState = PLA_AdapterView.access$100(this.this$0);
        }
        this.this$0.mOldItemCount = this.this$0.mItemCount;
        this.this$0.mItemCount = 0;
        this.this$0.mSelectedPosition = -1;
        this.this$0.mSelectedRowId = Long.MIN_VALUE;
        this.this$0.mNeedSync = false;
        this.this$0.requestLayout();
    }

    public void clearSavedState() {
        this.mInstanceState = null;
    }
}
