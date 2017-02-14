package com.meizu.flyme.reflect;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import java.lang.reflect.Method;

public class ActionBarProxy extends Proxy {
    private static Class<?> sClass = ActionBar.class;
    private static Method sSetActionBarViewCollapsableMethod;
    private static Method sSetActionModeHeaderHiddenMethod;
    private static Method sSetBackButtonDrawableMethod;
    private static Method sSetOverFlowButtonDrawableMethod;
    private static Method sSetTabsShowAtBottom;

    public static boolean hasSmartBar() {
        try {
            return ((Boolean) Class.forName("android.os.Build").getMethod("hasSmartBar", new
                    Class[0]).invoke(null, new Object[0])).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean SetBackButtonDrawable(ActionBar actionbar, Drawable backIcon) {
        sSetBackButtonDrawableMethod = Proxy.getMethod(sSetBackButtonDrawableMethod, sClass,
                "setBackButtonDrawable", Drawable.class);
        return Proxy.invoke(sSetBackButtonDrawableMethod, actionbar, backIcon);
    }

    public static boolean SetOverFlowButtonDrawable(ActionBar actionbar, Drawable drawable) {
        sSetOverFlowButtonDrawableMethod = Proxy.getMethod(sSetOverFlowButtonDrawableMethod,
                sClass, "setOverFlowButtonDrawable", Drawable.class);
        return Proxy.invoke(sSetOverFlowButtonDrawableMethod, actionbar, drawable);
    }

    public static boolean setActionModeHeaderHidden(ActionBar bar, boolean hide) {
        sSetActionModeHeaderHiddenMethod = Proxy.getMethod(sSetActionModeHeaderHiddenMethod,
                sClass, "setActionModeHeaderHidden", Boolean.TYPE);
        return Proxy.invoke(sSetActionModeHeaderHiddenMethod, bar, Boolean.valueOf(hide));
    }

    public static boolean setActionBarViewCollapsable(ActionBar bar, boolean collapsable) {
        sSetActionBarViewCollapsableMethod = Proxy.getMethod(sSetActionBarViewCollapsableMethod,
                sClass, "setActionBarViewCollapsable", Boolean.TYPE);
        return Proxy.invoke(sSetActionBarViewCollapsableMethod, bar, Boolean.valueOf(collapsable));
    }

    public static boolean setActionBarTabsShowAtBottom(ActionBar actionbar, boolean showAtBottom) {
        sSetTabsShowAtBottom = Proxy.getMethod(sSetTabsShowAtBottom, sClass,
                "setTabsShowAtBottom", Boolean.TYPE);
        return Proxy.invoke(sSetTabsShowAtBottom, actionbar, Boolean.valueOf(showAtBottom));
    }

    public static int getActionBarHeight(Context context, ActionBar actionbar) {
        if (actionbar == null) {
            return 0;
        }
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(16843499, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources()
                    .getDisplayMetrics());
        }
        return actionbar.getHeight();
    }

    public static int getSmartBarHeight(Context context, ActionBar actionbar) {
        if (actionbar != null) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                return context.getResources().getDimensionPixelSize(Integer.parseInt(c.getField
                        ("mz_action_button_min_height").get(c.newInstance()).toString()));
            } catch (Exception e) {
                e.printStackTrace();
                actionbar.getHeight();
            }
        }
        return 0;
    }
}
