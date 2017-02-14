package com.boohee.one.update;

import android.content.Context;
import android.support.annotation.NonNull;

public interface UpdateStrategy {
    void onUpdate(Context context, @NonNull UpdateInfo updateInfo);
}
