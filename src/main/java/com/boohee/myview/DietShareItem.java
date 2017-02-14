package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;

import com.balysv.materialripple.MaterialRippleLayout;
import com.boohee.one.R;

import java.util.HashMap;
import java.util.Map;

public abstract class DietShareItem extends MaterialRippleLayout {
    public static final    String               GOOD       = "good,合理";
    public static final    String               LESS       = "less,偏低";
    public static final    String               MUCH       = "much,偏高";
    protected static final Map<String, Integer> mIndicator = new HashMap();

    public @interface Status {
    }

    public abstract void setIndicateText(@Status String str);

    public DietShareItem(Context context) {
        this(context, null);
    }

    public DietShareItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietShareItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicator.put(GOOD, Integer.valueOf(R.drawable.pv));
        mIndicator.put(LESS, Integer.valueOf(R.drawable.pw));
        mIndicator.put(MUCH, Integer.valueOf(R.drawable.px));
    }

    public void setIndicateTextWithString(String indicate) {
        setIndicateText(changeStringToStatus(indicate));
    }

    private static String changeStringToStatus(String indicate) {
        if (LESS.split(",")[0].equals(indicate)) {
            return LESS;
        }
        if (MUCH.split(",")[0].equals(indicate)) {
            return MUCH;
        }
        return GOOD;
    }
}
