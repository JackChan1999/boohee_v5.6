package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.boohee.one.R;

public class CornerListView extends ListView {
    public CornerListView(Context context) {
        super(context);
    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                int itemnum = pointToPosition((int) ev.getX(), (int) ev.getY());
                if (itemnum != -1) {
                    if (itemnum != 0) {
                        if (itemnum != getAdapter().getCount() - 1) {
                            setSelector(R.drawable.dp);
                            break;
                        }
                        setSelector(R.drawable. do);
                        break;
                    } else if (itemnum != getAdapter().getCount() - 1) {
                        setSelector(R.drawable.dr);
                        break;
                    } else {
                        setSelector(R.drawable.dq);
                        break;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
