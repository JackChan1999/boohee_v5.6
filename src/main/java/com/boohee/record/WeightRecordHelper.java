package com.boohee.record;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.model.LocalWeightRecord;
import com.boohee.model.WeightPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.event.LatestWeightEvent;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sync.SyncHelper;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.Event;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeightRecordHelper {
    public static final int REQUEST_IMAGE = 0;
    public static final int REQUEST_SHOW  = 1;
    private Activity context;
    private FileCache mCache = FileCache.get(this.context);
    private BaseDialogFragment mFragment;

    public WeightRecordHelper(BaseDialogFragment mFragment) {
        this.mFragment = mFragment;
        this.context = mFragment.getActivity();
    }

    public void deleteWeight(final String record_on) {
        if (!TextUtils.isEmpty(record_on)) {
            if (HttpUtils.isNetworkAvailable(this.context)) {
                this.mFragment.showLoading();
                RecordApi.deleteRecord(this.context, record_on, new JsonCallback(this.context) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        WeightRecordHelper.this.mFragment.callChangeListener();
                        WeightRecordHelper.this.mFragment.dismiss();
                        WeightRecordHelper.this.deleteLocalWeightRecord(record_on);
                        EventBus.getDefault().post(new RefreshWeightEvent());
                    }

                    public void onFinish() {
                        super.onFinish();
                        WeightRecordHelper.this.mFragment.dismissLoading();
                    }
                });
                return;
            }
            new WeightRecordDao(this.context).delete(record_on);
            this.mFragment.dismiss();
            EventBus.getDefault().post(new RefreshWeightEvent());
        }
    }

    private void deleteLocalWeightRecord(String record_on) {
        JSONObject object = this.mCache.getAsJSONObject("latest_weight");
        if (object != null) {
            LocalWeightRecord weightRecord = (LocalWeightRecord) FastJsonUtils.fromJson(object,
                    LocalWeightRecord.class);
            if (weightRecord != null && record_on.equals(weightRecord.record_on)) {
                this.mCache.remove("latest_weight");
            }
        }
    }

    public void finishRequest(String weight, String record_on) {
        Helper.showToast((CharSequence) "保存成功");
        MobclickAgent.onEvent(this.context, Event.tool_addWeightRecordOK);
        MobclickAgent.onEvent(this.context, Event.tool_recordOK);
        saveWeightRecord(weight, record_on);
        this.mFragment.callChangeListener();
        this.mFragment.dismiss();
    }

    private void saveWeightRecord(String weight, String record_on) {
        LocalWeightRecord localRecord = (LocalWeightRecord) FastJsonUtils.fromJson(this.mCache
                .getAsString("latest_weight"), LocalWeightRecord.class);
        if (localRecord == null || TextUtils.isEmpty(localRecord.record_on) || localRecord
                .record_on.compareTo(record_on) <= 0) {
            this.mCache.put("latest_weight", FastJsonUtils.toJson(new LocalWeightRecord(weight,
                    record_on)));
            OnePreference.setLatestWeight(Float.parseFloat(weight));
            EventBus.getDefault().post(new LatestWeightEvent().setLatestWeight(Float.parseFloat
                    (weight)));
        }
    }

    public void sendRequest(String weight, String record_on, String imagePath) {
        new WeightRecordDao(this.context).add(weight, record_on, null);
        if (!HttpUtils.isNetworkAvailable(this.context)) {
            finishRequest(weight, record_on);
        } else if (TextUtils.isEmpty(imagePath)) {
            SyncHelper.syncWeight(true);
            finishRequest(weight, record_on);
        } else {
            uploadPhoto(weight, record_on, imagePath);
        }
    }

    private void uploadPhoto(final String weight, final String record_on, String imagePath) {
        this.mFragment.showLoading();
        QiniuUploader.upload(Prefix.record, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                new WeightRecordDao(WeightRecordHelper.this.context).add(weight, record_on,
                        WeightRecordHelper.this.generatePhotoInfo(infos));
                SyncHelper.syncWeight(true);
                WeightRecordHelper.this.finishRequest(weight, record_on);
            }

            public void onError(String msg) {
                Helper.showToast((CharSequence) msg);
            }

            public void onFinish() {
                WeightRecordHelper.this.mFragment.dismissLoading();
            }
        }, imagePath);
    }

    public void sendRequest(WeightRecord record, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            this.mFragment.showLoading();
            sendScaleRequest(record, null);
            return;
        }
        uploadPhoto(record, imagePath);
    }

    private void uploadPhoto(final WeightRecord record, String imagePath) {
        this.mFragment.showLoading();
        QiniuUploader.upload(Prefix.record, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                WeightRecordHelper.this.sendScaleRequest(record, WeightRecordHelper.this
                        .generatePhotoInfo(infos));
            }

            public void onError(String msg) {
                WeightRecordHelper.this.mFragment.dismissLoading();
                Helper.showToast((CharSequence) msg);
            }

            public void onFinish() {
            }
        }, imagePath);
    }

    private void sendScaleRequest(final WeightRecord record, JSONArray array) {
        RecordApi.postWeight(record, array, this.context, new JsonCallback(this.context) {
            public void ok(JSONObject object) {
                super.ok(object);
                WeightRecordHelper.this.finishRequest(record.weight, record.record_on);
            }

            public void onFinish() {
                super.onFinish();
                WeightRecordHelper.this.mFragment.dismissLoading();
            }
        });
    }

    public void showTakePhotoDialog() {
        this.mFragment.getDialog().getWindow().setWindowAnimations(R.style.df);
        Intent intent = new Intent(this.context, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", true);
        intent.putExtra("max_select_count", 1);
        intent.putExtra("select_count_mode", 0);
        this.context.startActivityForResult(intent, 0);
    }

    private ArrayList<WeightPhoto> generate(WeightRecord record) {
        WeightPhoto createPhoto = new WeightPhoto();
        com.boohee.model.mine.WeightPhoto photo = (com.boohee.model.mine.WeightPhoto) record
                .photos.get(0);
        createPhoto.id = -1;
        createPhoto.record_on = record.record_on;
        createPhoto.photo_url = photo.photo_url;
        createPhoto.thumb_photo_url = photo.thumb_photo_url;
        createPhoto.weight = ArithmeticUtil.safeParseFloat(record.weight);
        ArrayList<WeightPhoto> list = new ArrayList();
        list.add(createPhoto);
        return list;
    }

    public void goLargeImage(Activity activity, WeightRecord record) {
        Intent i = new Intent(activity, NewWeightGalleryActivity.class);
        i.putParcelableArrayListExtra(NewWeightGalleryActivity.KEY_WEIGHT_PHOTOS, generate(record));
        i.putExtra(NewWeightGalleryActivity.KEY_INDEX, 0);
        activity.startActivityForResult(i, 1);
    }

    private JSONArray generatePhotoInfo(List<QiniuModel> infos) {
        MobclickAgent.onEvent(this.context, Event.tool_weight_addphoto);
        QiniuModel model = (QiniuModel) infos.get(0);
        JSONArray photosArray = new JSONArray();
        try {
            JSONObject obj = new JSONObject();
            obj.put("origin_width", 120);
            obj.put("origin_height", 120);
            obj.put("qiniu_key", model.key);
            obj.put("qiniu_hash", model.hash);
            photosArray.put(obj);
            return photosArray;
        } catch (JSONException e) {
            return null;
        }
    }
}
