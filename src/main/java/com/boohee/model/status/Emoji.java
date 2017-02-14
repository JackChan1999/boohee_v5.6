package com.boohee.model.status;

import android.content.Context;

public class Emoji {
    public static final String DRAWABLE_PREFIX = "emoji_0x";
    public  int     imageId;
    private Context mContext;
    public  String  unified_code;

    public Emoji(Context context, String code) {
        this.unified_code = code;
        this.mContext = context;
        this.imageId = getImageId(code);
    }

    private int getImageId(String code) {
        return this.mContext.getResources().getIdentifier(DRAWABLE_PREFIX + code, "drawable",
                this.mContext.getPackageName());
    }

    public String getCode() {
        return "[e]" + this.unified_code + "[/e]";
    }
}
