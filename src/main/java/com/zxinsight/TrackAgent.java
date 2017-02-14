package com.zxinsight;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.text.TextUtils;

import com.alipay.security.mobile.module.deviceinfo.constant.DeviceInfoConstant;
import com.baidu.location.a.a;
import com.boohee.model.ModelName;
import com.zxinsight.analytics.domain.UserProfile;
import com.zxinsight.analytics.domain.trackEvent.ActionViewEvent;
import com.zxinsight.analytics.domain.trackEvent.AppLaunchEvent;
import com.zxinsight.analytics.domain.trackEvent.ClickEvent;
import com.zxinsight.analytics.domain.trackEvent.CustomEvent;
import com.zxinsight.analytics.domain.trackEvent.ErrorEvent;
import com.zxinsight.analytics.domain.trackEvent.ImpressionEvent;
import com.zxinsight.analytics.domain.trackEvent.MLinkEvent;
import com.zxinsight.analytics.domain.trackEvent.PageTrackingEvent;
import com.zxinsight.analytics.domain.trackEvent.SocialEvent;
import com.zxinsight.common.util.DeviceInfoUtils;
import com.zxinsight.common.util.TimeMap;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.h;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.n;
import com.zxinsight.common.util.o;
import com.zxinsight.common.util.q;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONObject;

public class TrackAgent {
    private static final    TimeMap     a = new TimeMap();
    private static          String      b = null;
    private static volatile TrackAgent  c = null;
    private                 m           d = m.a();
    private                 String      e = "";
    private                 Set<String> f = new HashSet();
    private                 String      g = "root";
    private                 String      h = "root";

    private TrackAgent() {
    }

    public static synchronized TrackAgent currentEvent() {
        TrackAgent trackAgent;
        synchronized (TrackAgent.class) {
            if (c == null) {
                c = new TrackAgent();
            }
            trackAgent = c;
        }
        return trackAgent;
    }

    public void onResume(Context context) {
        onResume(context, null);
    }

    public void onResume(Context context, String str) {
        if (!m.a().M()) {
            resume(context, str);
        }
    }

    void resume(Context context, String str) {
        MWConfiguration.initContext(context);
        onStart();
        isGenNewSessionID();
        if (!m.a().t()) {
            if (l.a(str)) {
                str = context.getClass().getName();
            }
            onPageStart(str);
        }
    }

    public void onPause(Context context) {
        onPause(context, null);
    }

    public void onPause(Context context, String str) {
        if (!m.a().M()) {
            pause(context, str);
        }
    }

    void pause(Context context, String str) {
        MWConfiguration.initContext(context);
        this.d.g();
        if (!m.a().t()) {
            if (l.a(str)) {
                str = context.getClass().getName();
            }
            onPageEnd(str);
        }
        new q(context).postDelayed(new z(this), 300);
    }

    public void onPageStart(String str) {
        String b = o.b();
        this.h = str;
        a.put("pageTrackingStartTime", b);
    }

    public void onPageEnd(String str) {
        if (l.b(a.get("pageTrackingStartTime"))) {
            PageTrackingEvent pageTrackingEvent = new PageTrackingEvent();
            pageTrackingEvent.st = (String) a.get("pageTrackingStartTime");
            a.remove("pageTrackingStartTime");
            pageTrackingEvent.et = o.b();
            if (l.b(str)) {
                str = this.h;
            }
            pageTrackingEvent.p = str;
            pageTrackingEvent.pp = this.g;
            pageTrackingEvent.save();
            this.g = str;
        }
    }

    public void customEvent(String str) {
        customEvent(str, null);
    }

    public void customEvent(String str, Map<String, String> map) {
        if (!TextUtils.isEmpty(str)) {
            if (map == null) {
                map = new HashMap();
            }
            if (map.containsKey("_k")) {
                c.a("custom key '_k' is reserved word ! please change it");
            }
            CustomEvent customEvent = new CustomEvent();
            map.put("_k", str);
            customEvent.st = o.b();
            customEvent.ps = map;
            customEvent.save();
        }
    }

    public void customEventStart(String str) {
        a.put(getCustomTimeKey("actionCustomStartTime", str), o.b());
    }

    public void customEventEnd(String str, Map<String, String> map) {
        if (!l.a(str) && !l.a(map) && l.b(getCustomTimeKey("actionCustomStartTime", str))) {
            CustomEvent customEvent = new CustomEvent();
            map.put("_k", str);
            customEvent.st = (String) a.get(getCustomTimeKey("actionCustomStartTime", str));
            customEvent.et = o.b();
            customEvent.ps = map;
            customEvent.save();
            a.remove(getCustomTimeKey("actionCustomStartTime", (String) map.get("mw_customId")));
        }
    }

    public void setLocation(Double d, Double d2) {
        m.a().a(a.int,String.valueOf(d));
        m.a().a(a.char,String.valueOf(d2));
    }

    public void setCityCode(int i) {
        setCityCode(String.valueOf(i), null);
    }

    public void setCityCode(String str) {
        setCityCode(str, null);
    }

    public void setCityCode(int i, UpdateMarketingListener updateMarketingListener) {
        setCityCode(String.valueOf(i), updateMarketingListener);
    }

    public void setCityCode(String str, UpdateMarketingListener updateMarketingListener) {
        if (!m.a().z().equals(str)) {
            m.a().k(str);
            MarketingHelper.currentMarketing(MWConfiguration.getContext()).update
                    (updateMarketingListener);
        }
    }

    public void reportError(String str) {
        String str2;
        if (str.contains("Caused by:")) {
            String[] split = str.substring(str.indexOf("Caused by:")).split("\n\t");
            if (split.length >= 1) {
                str2 = split[0];
                errorEvent(str2, str);
                if (m.a().u()) {
                    a.a().e();
                }
            }
        }
        str2 = str;
        errorEvent(str2, str);
        if (m.a().u()) {
            a.a().e();
        }
    }

    public void reportError(Throwable th) {
        String errorInfo = getErrorInfo(th);
        String localizedMessage = th.getLocalizedMessage();
        if (l.a(localizedMessage)) {
            localizedMessage = th.toString();
        }
        errorEvent(localizedMessage, errorInfo);
        if (m.a().u()) {
            a.a().e();
        }
    }

    public void onKillProcess() {
        launchEvent();
        a.a().e();
    }

    public void setUserProfile(UserProfile userProfile) {
        this.d.a(userProfile);
        if (!(userProfile == null || userProfile.toString().equals(this.d.e()))) {
            a.a().a(userProfile.toString());
        }
        this.d.b(userProfile);
    }

    public void setUserProfile(String str) {
        setUserProfile(new UserProfile(str));
    }

    public void cancelUserProfile() {
        this.d.b(null);
    }

    void onMLinkClick(String str, JSONObject jSONObject) {
        if (!TextUtils.isEmpty(str)) {
            MLinkEvent mLinkEvent = new MLinkEvent();
            mLinkEvent.l = str;
            mLinkEvent.st = o.b();
            mLinkEvent.ak = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                    .getActivityKey(str);
            if (h.c(jSONObject)) {
                try {
                    mLinkEvent.ps = h.a(jSONObject);
                } catch (Exception e) {
                }
            }
            mLinkEvent.p = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                    .getMLinkKey(str) + ",," + o.c() + ",,";
            mLinkEvent.pp = o.c();
            mLinkEvent.save();
        }
    }

    void onMLinkViewOrInstall(Map map, String str) {
        if (!"mw_dump_key".equals(m.a().I())) {
            MLinkEvent mLinkEvent = new MLinkEvent();
            mLinkEvent.a = str;
            mLinkEvent.st = o.b();
            if (l.b(map)) {
                mLinkEvent.ps = map;
            } else {
                mLinkEvent.ps = new HashMap();
            }
            if (l.b(m.a().G())) {
                mLinkEvent.ak = m.a().G();
            }
            if (l.b(m.a().H())) {
                mLinkEvent.l = m.a().H();
            }
            if (l.b(m.a().I())) {
                mLinkEvent.pp = m.a().I();
            }
            if (l.b(m.a().L())) {
                mLinkEvent.p = m.a().L();
            }
            if (l.b(m.a().J())) {
                mLinkEvent.ts = m.a().J();
            }
            mLinkEvent.save();
        }
    }

    void onActionClick(String str) {
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.st = o.b();
        clickEvent.ak = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                .getActivityKey(str);
        clickEvent.save();
    }

    private void onActionViewResume(String str) {
        a.put(str + "action_view_time_start", o.b());
    }

    private void onActionViewPause(String str, String str2) {
        ActionViewEvent actionViewEvent = new ActionViewEvent();
        actionViewEvent.st = (String) a.get(str + "action_view_time_start");
        a.remove(str + "action_view_time_start");
        actionViewEvent.et = str2;
        actionViewEvent.t = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                .getTitle(str);
        actionViewEvent.ak = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                .getActivityKey(str);
        actionViewEvent.save();
    }

    private void onStart() {
        if (l.a(a.get("app_launch_start_time"))) {
            a.put("app_launch_start_time", o.b());
        }
    }

    private boolean isGenNewSessionID() {
        boolean z = true;
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        try {
            if (b == null) {
                genSession();
                return z;
            } else if (System.currentTimeMillis() - this.d.h() > DeviceInfoConstant
                    .REQUEST_LOCATE_INTERVAL) {
                genSession();
                reentrantLock.unlock();
                return true;
            } else {
                reentrantLock.unlock();
                return false;
            }
        } catch (Exception e) {
            z = e;
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    private String genSession() {
        MarketingHelper.currentMarketing(MWConfiguration.getContext()).updateMarketing();
        String c = o.c();
        if (l.a(c)) {
            c = System.currentTimeMillis() + DeviceInfoUtils.c(MWConfiguration.getContext());
        } else {
            c = c + System.currentTimeMillis() + DeviceInfoUtils.c(MWConfiguration.getContext());
        }
        c = n.a(c);
        this.d.g();
        this.d.f(c);
        b = c;
        return c;
    }

    private boolean isAppExit() {
        if (MWConfiguration.getContext() == null) {
            return true;
        }
        String packageName = MWConfiguration.getContext().getPackageName();
        if (o.a(MWConfiguration.getContext(), "android.permission.GET_TASKS")) {
            ActivityManager activityManager = (ActivityManager) MWConfiguration.getContext()
                    .getSystemService(ModelName.ACTIVITY);
            if (activityManager != null && l.b(activityManager.getRunningTasks(1)) &&
                    activityManager.getRunningTasks(1).get(0) != null && ((RunningTaskInfo)
                    activityManager.getRunningTasks(1).get(0)).topActivity != null) {
                return !packageName.equals(((RunningTaskInfo) activityManager.getRunningTasks(1)
                        .get(0)).topActivity.getPackageName());
            } else if (isAppOnForeground()) {
                return false;
            } else {
                return true;
            }
        } else if (isAppOnForeground()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isAppOnForeground() {
        String packageName = MWConfiguration.getContext().getPackageName();
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) MWConfiguration
                .getContext().getSystemService(ModelName.ACTIVITY)).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.processName.equals(packageName) && runningAppProcessInfo
                    .importance == 100) {
                return true;
            }
        }
        return false;
    }

    private void onTerminate() {
        launchEvent();
        a.a().b();
    }

    synchronized void launchEvent() {
        if (l.b(a.get("app_launch_start_time"))) {
            AppLaunchEvent appLaunchEvent = new AppLaunchEvent();
            appLaunchEvent.st = (String) a.get("app_launch_start_time");
            a.remove("app_launch_start_time");
            appLaunchEvent.et = o.b();
            appLaunchEvent.save();
        }
    }

    private String getCustomTimeKey(String str, String str2) {
        return str + str2;
    }

    private void errorEvent(String str, String str2) {
        ErrorEvent errorEvent = new ErrorEvent();
        errorEvent.l = str;
        errorEvent.ts = str2;
        errorEvent.a = "e";
        errorEvent.st = o.b();
        errorEvent.save();
    }

    private String getErrorInfo(Throwable th) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    void onSocialShare(String str, String str2) {
        SocialEvent socialEvent = new SocialEvent();
        socialEvent.nw = DeviceInfoUtils.b(MWConfiguration.getContext());
        socialEvent.ak = MarketingHelper.currentMarketing(MWConfiguration.getContext())
                .getActivityKey(str);
        socialEvent.sa = str2;
        socialEvent.st = o.b();
        socialEvent.save();
    }

    void onImpression(String str) {
        if (!l.a(str)) {
            String f = m.a().f();
            if (!this.e.equals(f) || !this.f.contains(str)) {
                this.e = f;
                this.f.add(str);
                ImpressionEvent impressionEvent = new ImpressionEvent();
                impressionEvent.ak = MarketingHelper.currentMarketing(MWConfiguration.getContext
                        ()).getActivityKey(str);
                impressionEvent.st = o.b();
                impressionEvent.save();
            }
        }
    }

    void onWebviewResume(Context context, String str) {
        onActionViewResume(str);
        onResume(context, null);
    }

    void onWebviewPause(Context context, String str) {
        onPause(context, null);
        onActionViewPause(str, o.b());
    }
}
