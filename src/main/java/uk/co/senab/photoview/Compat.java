package uk.co.senab.photoview;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.view.View;

public class Compat {
    private static final int SIXTY_FPS_INTERVAL = 16;

    public static void postOnAnimation(View view, Runnable runnable) {
        if (VERSION.SDK_INT >= 16) {
            postOnAnimationJellyBean(view, runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    @TargetApi(16)
    private static void postOnAnimationJellyBean(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }

    public static int getPointerIndex(int action) {
        if (VERSION.SDK_INT >= 11) {
            return getPointerIndexHoneyComb(action);
        }
        return getPointerIndexEclair(action);
    }

    @TargetApi(5)
    private static int getPointerIndexEclair(int action) {
        return (MotionEventCompat.ACTION_POINTER_INDEX_MASK & action) >> 8;
    }

    @TargetApi(11)
    private static int getPointerIndexHoneyComb(int action) {
        return (MotionEventCompat.ACTION_POINTER_INDEX_MASK & action) >> 8;
    }
}
