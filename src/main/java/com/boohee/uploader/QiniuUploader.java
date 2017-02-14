package com.boohee.uploader;

import android.text.TextUtils;

import com.boohee.cipher.BooheeCipher;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.utils.BitmapUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.xiaomi.account.openauth.utils.Network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class QiniuUploader {
    private static final List<UploadHandler> mHandlerList = new ArrayList();
    private static UploadManager mUploadManager;

    public static void upload(Prefix prefix, UploadHandler handler, String... paths) {
        if (paths != null && paths.length != 0) {
            List pathList = new ArrayList();
            for (Object add : paths) {
                pathList.add(add);
            }
            upload(prefix, handler, pathList);
        }
    }

    public static void upload(final Prefix prefix, final UploadHandler handler, List<String>
            pathList) {
        if (handler != null) {
            mHandlerList.add(handler);
            if (pathList != null && pathList.size() != 0) {
                if (mUploadManager == null) {
                    mUploadManager = QiniuConfig.getUploadManager();
                }
                Observable.just(pathList).map(new Func1<List<String>, List<QiniuModel>>() {
                    public List<QiniuModel> call(List<String> strings) {
                        List<QiniuModel> modelList = new ArrayList();
                        JSONArray keyArray = new JSONArray();
                        for (String path : strings) {
                            QiniuModel model = new QiniuModel();
                            model.path = path;
                            model.key = QiniuConfig.createFileName(prefix);
                            modelList.add(model);
                            keyArray.put(model.key);
                        }
                        try {
                            JSONObject object = new JSONObject(new OkHttpClient().newCall(new
                                    Builder().url(QiniuConfig.getURL()).addHeader("app_device",
                                    "Android").addHeader(Network.USER_AGENT, "Android/OkHttp")
                                    .post(RequestBody.create(MediaType.parse("application/json"),
                                            BooheeCipher.qiniuRequestBody(keyArray.toString())))
                                    .build()).execute().body().string());
                            if (object.has("errors")) {
                                return null;
                            }
                            JSONArray tokenArray = object.getJSONArray("upload_tokens");
                            for (int i = 0; i < tokenArray.length(); i++) {
                                JSONObject tokenObject = tokenArray.getJSONObject(i);
                                String key = tokenObject.optString("key");
                                String uploadToken = tokenObject.getString("upload_token");
                                if (TextUtils.isEmpty(uploadToken)) {
                                    return null;
                                }
                                for (QiniuModel model2 : modelList) {
                                    if (model2.key.equals(key)) {
                                        model2.token = uploadToken;
                                        break;
                                    }
                                }
                            }
                            return modelList;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).map(new Func1<List<QiniuModel>, List<QiniuModel>>() {
                    public List<QiniuModel> call(List<QiniuModel> modelList) {
                        if (modelList == null) {
                            return null;
                        }
                        try {
                            CountDownLatch countDownLatch = new CountDownLatch(modelList.size());
                            for (QiniuModel model : modelList) {
                                QiniuUploader.uploadToQiniu(model, countDownLatch, handler);
                            }
                            countDownLatch.await();
                            return modelList;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<QiniuModel>>() {
                    private List<QiniuModel> mModelList = null;

                    public void onCompleted() {
                        handler.onFinish();
                        String failMessage = "Upload fail!!!";
                        if (this.mModelList == null || this.mModelList.size() == 0) {
                            handler.onError(failMessage);
                            return;
                        }
                        for (QiniuModel model : this.mModelList) {
                            if (TextUtils.isEmpty(model.hash)) {
                                handler.onError(failMessage);
                                return;
                            }
                        }
                        handler.onSuccess(this.mModelList);
                    }

                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.onFinish();
                        handler.onError(e.getMessage());
                    }

                    public void onNext(List<QiniuModel> modelList) {
                        this.mModelList = modelList;
                    }
                });
            }
        }
    }

    private static void uploadToQiniu(final QiniuModel model, final CountDownLatch
            countDownLatch, final UploadHandler handler) {
        try {
            UploadOptions options = new UploadOptions(null, "image/jpeg", false, new
                    UpProgressHandler() {
                public void progress(String key, double percent) {
                    handler.onProgress(model.path, percent);
                }
            }, new UpCancellationSignal() {
                public boolean isCancelled() {
                    return handler.isCancled();
                }
            });
            UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
                public void complete(String key, ResponseInfo response, JSONObject result) {
                    if (response.isOK()) {
                        model.hash = result.optString("hash");
                    }
                    countDownLatch.countDown();
                }
            };
            if (new File(model.path).length() > 512000) {
                mUploadManager.put(BitmapUtils.compressBitmap(model.path, 512000, QiniuConfig
                        .MAX_WIDTH, 800), model.key, model.token, upCompletionHandler, options);
                return;
            }
            mUploadManager.put(model.path, model.key, model.token, upCompletionHandler, options);
        } catch (Exception e) {
            e.printStackTrace();
            countDownLatch.countDown();
        }
    }

    public static void canncelAll() {
        if (mHandlerList != null && mHandlerList.size() > 0) {
            for (UploadHandler handler : mHandlerList) {
                handler.cancel();
            }
            mHandlerList.clear();
        }
    }
}
