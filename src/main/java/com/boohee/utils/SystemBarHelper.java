package com.boohee.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.boohee.one.R;
import com.meizu.flyme.reflect.StatusBarProxy;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SystemBarHelper {
    private static float DEFAULT_ALPHA = (VERSION.SDK_INT >= 21 ? 0.2f : 0.3f);

    public static void compatWhiteToolBar(Activity activity, View toolbar) {
        if (VERSION.SDK_INT < 19) {
            return;
        }
        if (isMIUI6Later() || isFlyme4Later() || VERSION.SDK_INT >= 23) {
            setStatusBarDarkMode(activity);
            immersiveStatusBar(activity, 0.0f);
            return;
        }
        immersiveStatusBar(activity, 1.0f);
    }

    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor) {
        tintStatusBar(activity, statusBarColor, DEFAULT_ALPHA);
    }

    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor, @FloatRange
            (from = 0.0d, to = 1.0d) float alpha) {
        tintStatusBar(activity.getWindow(), statusBarColor, alpha);
    }

    public static void tintStatusBar(Window window, @ColorInt int statusBarColor) {
        tintStatusBar(window, statusBarColor, DEFAULT_ALPHA);
    }

    public static void tintStatusBar(Window window, @ColorInt int statusBarColor, @FloatRange
            (from = 0.0d, to = 1.0d) float alpha) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(0);
            } else {
                window.addFlags(67108864);
            }
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            View rootView = ((ViewGroup) window.getDecorView().findViewById(16908290)).getChildAt
                    (0);
            if (rootView != null) {
                ViewCompat.setFitsSystemWindows(rootView, true);
            }
            setStatusBar(decorView, statusBarColor, true);
            setTranslucentView(decorView, alpha);
        }
    }

    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout,
                                              @ColorInt int statusBarColor) {
        tintStatusBarForDrawer(activity, drawerLayout, statusBarColor, DEFAULT_ALPHA);
    }

    public static void tintStatusBarForDrawer(Activity activity, DrawerLayout drawerLayout,
                                              @ColorInt int statusBarColor, @FloatRange(from = 0
            .0d, to = 1.0d) float alpha) {
        if (VERSION.SDK_INT >= 19) {
            Window window = activity.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            ViewGroup drawContent = (ViewGroup) drawerLayout.getChildAt(0);
            if (VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(0);
                drawerLayout.setStatusBarBackgroundColor(statusBarColor);
                window.getDecorView().setSystemUiVisibility((window.getDecorView()
                        .getSystemUiVisibility() | 1024) | 256);
            } else {
                window.addFlags(67108864);
            }
            setStatusBar(decorView, statusBarColor, true, true);
            setTranslucentView(decorView, alpha);
            drawerLayout.setFitsSystemWindows(false);
            drawContent.setFitsSystemWindows(true);
            ((ViewGroup) drawerLayout.getChildAt(1)).setFitsSystemWindows(false);
        }
    }

    public static void immersiveStatusBar(Activity activity) {
        immersiveStatusBar(activity, DEFAULT_ALPHA);
    }

    public static void immersiveStatusBar(Activity activity, @FloatRange(from = 0.0d, to = 1.0d)
            float alpha) {
        immersiveStatusBar(activity.getWindow(), alpha);
    }

    public static void immersiveStatusBar(Window window) {
        immersiveStatusBar(window, DEFAULT_ALPHA);
    }

    public static void immersiveStatusBar(Window window, @FloatRange(from = 0.0d, to = 1.0d)
            float alpha) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(0);
                window.getDecorView().setSystemUiVisibility((window.getDecorView()
                        .getSystemUiVisibility() | 1024) | 256);
            } else {
                window.addFlags(67108864);
            }
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            View rootView = ((ViewGroup) window.getDecorView().findViewById(16908290)).getChildAt
                    (0);
            int statusBarHeight = getStatusBarHeight(window.getContext());
            if (rootView != null) {
                LayoutParams lp = (LayoutParams) rootView.getLayoutParams();
                ViewCompat.setFitsSystemWindows(rootView, true);
                lp.topMargin = -statusBarHeight;
                rootView.setLayoutParams(lp);
            }
            setTranslucentView(decorView, alpha);
        }
    }

    public static void setStatusBarDarkMode(Activity activity) {
        setStatusBarDarkMode(activity.getWindow());
    }

    public static void setStatusBarDarkMode(Window window) {
        if (isFlyme4Later()) {
            setStatusBarDarkModeForFlyme4(window, true);
        } else if (isMIUI6Later()) {
            setStatusBarDarkModeForMIUI6(window, true);
        } else if (VERSION.SDK_INT >= 23) {
            setStatusBarDarkModeForM(window);
        }
    }

    @TargetApi(23)
    public static void setStatusBarDarkModeForM(Window window) {
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
                | 8192);
    }

    public static boolean setStatusBarDarkModeForFlyme4(Window window, boolean dark) {
        boolean z;
        if (dark) {
            z = false;
        } else {
            z = true;
        }
        return StatusBarProxy.setStatusBarDarkIcon(window, z);
    }

    public static void setStatusBarDarkModeForMIUI6(Window window, boolean darkmode) {
        int i = 0;
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int darkModeFlag = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt
                    (layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", new Class[]{Integer.TYPE,
                    Integer.TYPE});
            Object[] objArr = new Object[2];
            if (darkmode) {
                i = darkModeFlag;
            }
            objArr[0] = Integer.valueOf(i);
            objArr[1] = Integer.valueOf(darkModeFlag);
            extraFlagField.invoke(window, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setStatusBar(ViewGroup container, @ColorInt int statusBarColor, boolean
            visible, boolean addToFirst) {
        int i = 0;
        if (VERSION.SDK_INT >= 19) {
            View statusBarView = container.findViewById(R.id.statusbar_view);
            if (statusBarView == null) {
                statusBarView = new View(container.getContext());
                statusBarView.setId(R.id.statusbar_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, getStatusBarHeight
                        (container.getContext()));
                if (addToFirst) {
                    container.addView(statusBarView, 0, lp);
                } else {
                    container.addView(statusBarView, lp);
                }
            }
            statusBarView.setBackgroundColor(statusBarColor);
            if (!visible) {
                i = 8;
            }
            statusBarView.setVisibility(i);
        }
    }

    private static void setStatusBar(ViewGroup container, @ColorInt int statusBarColor, boolean
            visible) {
        setStatusBar(container, statusBarColor, visible, false);
    }

    private static void setTranslucentView(ViewGroup container, @FloatRange(from = 0.0d, to = 1
            .0d) float alpha) {
        if (VERSION.SDK_INT >= 19) {
            View translucentView = container.findViewById(R.id.translucent_view);
            if (translucentView == null) {
                translucentView = new View(container.getContext());
                translucentView.setId(R.id.translucent_view);
                container.addView(translucentView, new ViewGroup.LayoutParams(-1,
                        getStatusBarHeight(container.getContext())));
            }
            translucentView.setBackgroundColor(Color.argb((int) (255.0f * alpha), 0, 0, 0));
        }
    }

    public static int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen",
                DeviceInfoConstant.OS_ANDROID);
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    public static boolean isFlyme4Later() {
        return Build.FINGERPRINT.contains("Flyme_OS_4") || VERSION.INCREMENTAL.contains
                ("Flyme_OS_4") || Pattern.compile("Flyme[ OS ]*[4|5]+", 2).matcher(Build.DISPLAY)
                .find();
    }

    public static boolean isMIUI6Later() {
        try {
            if (Integer.parseInt(((String) Class.forName("android.os.SystemProperties").getMethod
                    ("get", new Class[]{String.class}).invoke(null, new Object[]{"ro.miui.ui" +
                    ".version.name"})).replaceAll("[vV]", "")) >= 6) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void setHeightAndPadding(Context context, View view) {
        if (VERSION.SDK_INT >= 19) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight
                    (context), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static void setPadding(Context context, View view) {
        if (VERSION.SDK_INT >= 19) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight
                    (context), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    public static void forceFitsSystemWindows(Activity activity) {
        forceFitsSystemWindows(activity.getWindow());
    }

    public static void forceFitsSystemWindows(Window window) {
        forceFitsSystemWindows((ViewGroup) window.getDecorView().findViewById(16908290));
    }

    public static void forceFitsSystemWindows(ViewGroup viewGroup) {
        if (VERSION.SDK_INT >= 19) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    forceFitsSystemWindows((ViewGroup) view);
                } else if (ViewCompat.getFitsSystemWindows(view)) {
                    ViewCompat.setFitsSystemWindows(view, false);
                }
            }
        }
    }
}
