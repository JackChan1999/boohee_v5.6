package com.boohee.one.radar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.boohee.one.R;
import com.boohee.utility.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConclusionLayout extends FrameLayout {
    private       int             BOX_LENGTH    = 50;
    private final int             BOX_NUMBER    = 5;
    private       int             FACE_LENGTH   = 15;
    private       int             INTERVAL      = 15;
    private final int[]           RESOURCE      = new int[]{R.drawable.a9x, R.drawable.a9y, R
            .drawable.a9z, R.drawable.a_0, R.drawable.a_1};
    private       List<ImageView> faceImageList = new ArrayList();
    private Context mContext;

    public ConclusionLayout(Context context) {
        super(context);
        init(context);
    }

    public ConclusionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ConclusionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.BOX_LENGTH = DensityUtil.dip2px(context, (float) this.BOX_LENGTH);
        this.INTERVAL = DensityUtil.dip2px(context, (float) this.INTERVAL);
        this.FACE_LENGTH = DensityUtil.dip2px(context, (float) this.FACE_LENGTH);
        for (int i = 0; i < 5; i++) {
            ImageView iv = new ImageView(this.mContext);
            iv.setImageResource(R.drawable.a_b);
            this.faceImageList.add(iv);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        removeAllViews();
        addTag();
        addFace();
    }

    public void setData(Map<String, String> data) {
        ((ImageView) this.faceImageList.get(0)).setImageResource(getImageResource("nutrition",
                data));
        ((ImageView) this.faceImageList.get(1)).setImageResource(getImageResource("spirit", data));
        ((ImageView) this.faceImageList.get(2)).setImageResource(getImageResource("balance", data));
        ((ImageView) this.faceImageList.get(3)).setImageResource(getImageResource("sports", data));
        ((ImageView) this.faceImageList.get(4)).setImageResource(getImageResource("social", data));
    }

    private int getImageResource(String attr, Map<String, String> data) {
        if (data == null || !data.containsKey(attr)) {
            return R.drawable.a_b;
        }
        String status = (String) data.get(attr);
        if ("good".equalsIgnoreCase(status)) {
            return R.drawable.a_8;
        }
        if ("bad".equalsIgnoreCase(status)) {
            return R.drawable.a9v;
        }
        return R.drawable.a_b;
    }

    private void addTag() {
        for (int i = 0; i < 5; i++) {
            ImageView iv = new ImageView(this.mContext);
            LayoutParams params = new LayoutParams(this.BOX_LENGTH, this.BOX_LENGTH);
            params.leftMargin = (this.BOX_LENGTH + this.INTERVAL) * i;
            iv.setLayoutParams(params);
            iv.setImageResource(this.RESOURCE[i]);
            addView(iv);
        }
    }

    public void addFace() {
        for (int i = 0; i < 5; i++) {
            ImageView iv = (ImageView) this.faceImageList.get(i);
            LayoutParams params = new LayoutParams(this.FACE_LENGTH, this.FACE_LENGTH);
            params.leftMargin = (int) ((((float) ((this.BOX_LENGTH + this.INTERVAL) * i)) + ((
                    (float) this.BOX_LENGTH) / 2.0f)) - (((float) this.FACE_LENGTH) / 2.0f));
            params.gravity = 80;
            iv.setLayoutParams(params);
            addView(iv);
        }
    }
}
