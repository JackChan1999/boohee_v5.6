package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public abstract class MQBaseCustomCompositeView extends RelativeLayout implements OnClickListener {
    protected abstract int[] getAttrs();

    protected abstract int getLayoutId();

    protected abstract void initAttr(int i, TypedArray typedArray);

    protected abstract void initView();

    protected abstract void processLogic();

    protected abstract void setListener();

    public MQBaseCustomCompositeView(Context context) {
        this(context, null);
    }

    public MQBaseCustomCompositeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MQBaseCustomCompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, getLayoutId(), this);
        initView();
        setListener();
        initAttrs(context, attrs);
        processLogic();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, getAttrs());
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    public void onClick(View view) {
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return findViewById(id);
    }
}
