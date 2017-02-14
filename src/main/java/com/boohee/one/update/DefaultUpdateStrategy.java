package com.boohee.one.update;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.boohee.utils.FileUtil;
import com.boohee.utils.HttpUtils;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DefaultUpdateStrategy implements UpdateStrategy {
    public void onUpdate(final Context context, final UpdateInfo info) {
        if (info != null && !TextUtils.isEmpty(info.apk_url) && !TextUtils.isEmpty(info.new_md5)) {
            Observable.just(info).observeOn(Schedulers.io()).map(new Func1<UpdateInfo, String>() {
                public String call(UpdateInfo updateInfo) {
                    return DefaultUpdateStrategy.this.getDownloadFinishPath(context, info);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                public void call(String path) {
                    Intent intent = new Intent(context, UpdateDialogActivity.class);
                    intent.putExtra("update_info", info);
                    if (!TextUtils.isEmpty(path) || HttpUtils.isWifiConnection(context)) {
                        intent.putExtra("file", path);
                        intent.addFlags(335544320);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @WorkerThread
    private String getDownloadFinishPath(Context context, UpdateInfo info) {
        File dir = UpdateUtil.getUpdateDir(context);
        if (dir == null || !dir.isDirectory()) {
            return null;
        }
        File apk = new File(dir, info.new_md5 + ShareConstants.PATCH_SUFFIX);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.getAbsolutePath().equals(apk.getAbsolutePath())) {
                    f.delete();
                }
            }
        }
        if (apk.exists() && FileUtil.checkMD5(info.new_md5, apk)) {
            return apk.getAbsolutePath();
        }
        return null;
    }
}
