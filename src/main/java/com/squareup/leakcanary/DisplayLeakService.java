package com.squareup.leakcanary;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build.VERSION;
import android.util.Log;

import com.squareup.leakcanary.internal.DisplayLeakActivity;
import com.squareup.leakcanary.internal.LeakCanaryInternals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DisplayLeakService extends AbstractAnalysisResultService {
    @TargetApi(11)
    protected final void onHeapAnalyzed(HeapDump heapDump, AnalysisResult result) {
        IOException e;
        Throwable th;
        String leakInfo = LeakCanary.leakInfo(this, heapDump, result, true);
        if (leakInfo.length() < 4000) {
            Log.d("LeakCanary", leakInfo);
        } else {
            for (String line : leakInfo.split("\n")) {
                Log.d("LeakCanary", line);
            }
        }
        if (result.failure != null || (result.leakFound && !result.excludedLeak)) {
            int maxStoredLeaks = getResources().getInteger(R.integer
                    .__leak_canary_max_stored_leaks);
            File renamedFile = LeakCanaryInternals.findNextAvailableHprofFile(maxStoredLeaks);
            if (renamedFile == null) {
                Log.e("LeakCanary", "Leak result dropped because we already store " +
                        maxStoredLeaks + " leak traces.");
                afterDefaultHandling(heapDump, result, leakInfo);
                return;
            }
            heapDump = heapDump.renameFile(renamedFile);
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(LeakCanaryInternals.leakResultFile
                        (renamedFile));
                try {
                    String contentTitle;
                    Notification notification;
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos2);
                    objectOutputStream.writeObject(heapDump);
                    objectOutputStream.writeObject(result);
                    if (fos2 != null) {
                        try {
                            fos2.close();
                        } catch (IOException e2) {
                        }
                    }
                    PendingIntent pendingIntent = DisplayLeakActivity.createPendingIntent(this,
                            heapDump.referenceKey);
                    if (result.failure == null) {
                        contentTitle = getString(R.string.__leak_canary_class_has_leaked, new
                                Object[]{LeakCanaryInternals.classSimpleName(result.className)});
                    } else {
                        contentTitle = getString(R.string.__leak_canary_analysis_failed);
                    }
                    String contentText = getString(R.string.__leak_canary_notification_message);
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService("notification");
                    if (VERSION.SDK_INT < 11) {
                        notification = new Notification();
                        notification.icon = R.drawable.__leak_canary_notification;
                        notification.when = System.currentTimeMillis();
                        notification.flags |= 16;
                        notification.setLatestEventInfo(this, contentTitle, contentText,
                                pendingIntent);
                    } else {
                        Builder builder = new Builder(this).setSmallIcon(R.drawable
                                .__leak_canary_notification).setWhen(System.currentTimeMillis())
                                .setContentTitle(contentTitle).setContentText(contentText)
                                .setAutoCancel(true).setContentIntent(pendingIntent);
                        if (VERSION.SDK_INT < 16) {
                            notification = builder.getNotification();
                        } else {
                            notification = builder.build();
                        }
                    }
                    notificationManager.notify(-558907665, notification);
                    afterDefaultHandling(heapDump, result, leakInfo);
                    return;
                } catch (IOException e3) {
                    e = e3;
                    fos = fos2;
                    try {
                        Log.e("LeakCanary", "Could not save leak analysis result to disk", e);
                        afterDefaultHandling(heapDump, result, leakInfo);
                        if (fos != null) {
                            try {
                                fos.close();
                                return;
                            } catch (IOException e4) {
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                Log.e("LeakCanary", "Could not save leak analysis result to disk", e);
                afterDefaultHandling(heapDump, result, leakInfo);
                if (fos != null) {
                    fos.close();
                    return;
                }
                return;
            }
        }
        afterDefaultHandling(heapDump, result, leakInfo);
    }

    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
    }
}
