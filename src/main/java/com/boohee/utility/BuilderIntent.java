package com.boohee.utility;

import android.content.Context;
import android.content.Intent;

public class BuilderIntent extends Intent {
    private final Context context;

    public BuilderIntent(Context ctx, Class<?> cls) {
        super(ctx, cls);
        this.context = ctx;
    }

    public BuilderIntent putExtra(String name, String value) {
        super.putExtra(name, value);
        return this;
    }

    public BuilderIntent putExtra(String name, int value) {
        super.putExtra(name, value);
        return this;
    }

    public BuilderIntent putExtra(String name, boolean value) {
        super.putExtra(name, value);
        return this;
    }

    public void startActivity() {
        this.context.startActivity(this);
    }
}
