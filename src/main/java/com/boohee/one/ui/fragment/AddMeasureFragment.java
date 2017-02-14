package com.boohee.one.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.model.LocalMeasureRecord;
import com.boohee.model.RecordMeasure;
import com.boohee.model.mine.Measure.MeasureType;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.MeasureEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.Date;

import org.json.JSONObject;

public class AddMeasureFragment extends BaseDialogFragment {
    private static final String URL_MEASURE        = "/api/v2/measures?token=%s&record_on=%s";
    public static final  String URL_MEASURE_DELETE = "/api/v1/measures/%1$s";
    private static final String URL_MEASURE_POST   = "/api/v2/measures";
    private EditText       et_arm;
    private EditText       et_brass;
    private EditText       et_hip;
    private EditText       et_shank;
    private EditText       et_thigh;
    private EditText       et_waist;
    private boolean        isRefresh;
    private RecordMeasure  recordMeasure;
    private String         record_on;
    private RelativeLayout rl_left;
    private RelativeLayout rl_right;
    private TextView       txt_cancel;
    private TextView       txt_commit;
    private TextView       txt_date;
    private TextView       txt_del_arm;
    private TextView       txt_del_brass;
    private TextView       txt_del_hip;
    private TextView       txt_del_shank;
    private TextView       txt_del_thigh;
    private TextView       txt_del_waist;
    private TextView       txt_right;

    private class ClickListener implements OnClickListener {
        private ClickListener() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_left:
                    AddMeasureFragment.this.record_on = DateHelper.format(DateHelper.addDays
                            (DateHelper.parseString(AddMeasureFragment.this.record_on), -1));
                    if (!TextUtils.equals(DateHelper.format(new Date()), AddMeasureFragment.this
                            .record_on)) {
                        AddMeasureFragment.this.txt_right.setBackgroundResource(R.drawable.et);
                    }
                    AddMeasureFragment.this.requestMeasure();
                    return;
                case R.id.rl_right:
                    if (!TextUtils.equals(DateHelper.format(new Date()), AddMeasureFragment.this
                            .record_on)) {
                        AddMeasureFragment.this.record_on = DateHelper.format(DateHelper.addDays
                                (DateHelper.parseString(AddMeasureFragment.this.record_on), 1));
                        if (TextUtils.equals(DateHelper.format(new Date()), AddMeasureFragment
                                .this.record_on)) {
                            AddMeasureFragment.this.txt_right.setBackgroundResource(R.drawable.a1x);
                        }
                        AddMeasureFragment.this.requestMeasure();
                        return;
                    }
                    return;
                case R.id.txt_cancel:
                    if (AddMeasureFragment.this.isRefresh && AddMeasureFragment.this
                            .changeListener != null) {
                        AddMeasureFragment.this.changeListener.onFinish();
                    }
                    AddMeasureFragment.this.dismiss();
                    return;
                case R.id.txt_commit:
                    if (AddMeasureFragment.this.recordMeasure != null && AddMeasureFragment.this
                            .inputValidate(AddMeasureFragment.this.recordMeasure)) {
                        AddMeasureFragment.this.postMeasure();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private class MeasureDelClickListener implements OnClickListener {
        EditText edit_add_record;
        String   record_type;
        TextView txt_add_record_del;

        public MeasureDelClickListener(String record_type, EditText edit_record, TextView txt_del) {
            this.record_type = record_type;
            this.edit_add_record = edit_record;
            this.txt_add_record_del = txt_del;
        }

        public void onClick(View v) {
            this.edit_add_record.setText("");
            AddMeasureFragment.this.deleteMeasure(this.record_type, this.txt_add_record_del);
        }
    }

    private class MeasureTextWatcher implements TextWatcher {
        EditText edit_add_record;
        String   record_type;
        TextView txt_add_record_del;

        public MeasureTextWatcher(String record_type, EditText edit_add_record, TextView
                txt_add_record_del) {
            this.record_type = record_type;
            this.txt_add_record_del = txt_add_record_del;
            this.edit_add_record = edit_add_record;
        }

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtil.isEmpty(s)) {
                this.txt_add_record_del.setVisibility(8);
            } else {
                this.txt_add_record_del.setVisibility(0);
            }
            if (MeasureType.WAIST.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.waist = s.toString().trim();
            } else if (MeasureType.BRASS.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.brass = s.toString().trim();
            } else if (MeasureType.ARM.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.arm = s.toString().trim();
            } else if (MeasureType.HIP.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.hip = s.toString().trim();
            } else if (MeasureType.THIGH.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.thigh = s.toString().trim();
            } else if (MeasureType.SHANK.getType().equals(this.record_type)) {
                AddMeasureFragment.this.recordMeasure.shank = s.toString().trim();
            }
        }
    }

    public static AddMeasureFragment newInstance(String record_date) {
        AddMeasureFragment fragment = new AddMeasureFragment();
        fragment.record_on = record_date;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.recordMeasure == null) {
            this.recordMeasure = new RecordMeasure();
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(1);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fc, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        this.txt_date = (TextView) view.findViewById(R.id.txt_date);
        this.txt_commit = (TextView) view.findViewById(R.id.txt_commit);
        this.txt_right = (TextView) view.findViewById(R.id.txt_right);
        this.txt_del_waist = (TextView) view.findViewById(R.id.txt_del_waist);
        this.txt_del_brass = (TextView) view.findViewById(R.id.txt_del_brass);
        this.txt_del_hip = (TextView) view.findViewById(R.id.txt_del_hip);
        this.txt_del_arm = (TextView) view.findViewById(R.id.txt_del_arm);
        this.txt_del_thigh = (TextView) view.findViewById(R.id.txt_del_thigh);
        this.txt_del_shank = (TextView) view.findViewById(R.id.txt_del_shank);
        this.et_waist = (EditText) view.findViewById(R.id.et_waist);
        this.et_brass = (EditText) view.findViewById(R.id.et_brass);
        this.et_hip = (EditText) view.findViewById(R.id.et_hip);
        this.et_arm = (EditText) view.findViewById(R.id.et_arm);
        this.et_thigh = (EditText) view.findViewById(R.id.et_thigh);
        this.et_shank = (EditText) view.findViewById(R.id.et_shank);
        this.rl_left = (RelativeLayout) view.findViewById(R.id.rl_left);
        this.rl_right = (RelativeLayout) view.findViewById(R.id.rl_right);
        this.txt_cancel.setOnClickListener(new ClickListener());
        this.txt_commit.setOnClickListener(new ClickListener());
        this.rl_left.setOnClickListener(new ClickListener());
        this.rl_right.setOnClickListener(new ClickListener());
        this.txt_del_waist.setOnClickListener(new MeasureDelClickListener(MeasureType.WAIST
                .getType(), this.et_waist, this.txt_del_waist));
        this.txt_del_brass.setOnClickListener(new MeasureDelClickListener(MeasureType.BRASS
                .getType(), this.et_brass, this.txt_del_brass));
        this.txt_del_hip.setOnClickListener(new MeasureDelClickListener(MeasureType.HIP.getType()
                , this.et_hip, this.txt_del_hip));
        this.txt_del_arm.setOnClickListener(new MeasureDelClickListener(MeasureType.ARM.getType()
                , this.et_arm, this.txt_del_arm));
        this.txt_del_thigh.setOnClickListener(new MeasureDelClickListener(MeasureType.THIGH
                .getType(), this.et_thigh, this.txt_del_thigh));
        this.txt_del_shank.setOnClickListener(new MeasureDelClickListener(MeasureType.SHANK
                .getType(), this.et_shank, this.txt_del_shank));
        this.et_waist.addTextChangedListener(new MeasureTextWatcher(MeasureType.WAIST.getType(),
                this.et_waist, this.txt_del_waist));
        this.et_brass.addTextChangedListener(new MeasureTextWatcher(MeasureType.BRASS.getType(),
                this.et_brass, this.txt_del_brass));
        this.et_hip.addTextChangedListener(new MeasureTextWatcher(MeasureType.HIP.getType(), this
                .et_hip, this.txt_del_hip));
        this.et_arm.addTextChangedListener(new MeasureTextWatcher(MeasureType.ARM.getType(), this
                .et_arm, this.txt_del_arm));
        this.et_thigh.addTextChangedListener(new MeasureTextWatcher(MeasureType.THIGH.getType(),
                this.et_thigh, this.txt_del_thigh));
        this.et_shank.addTextChangedListener(new MeasureTextWatcher(MeasureType.SHANK.getType(),
                this.et_shank, this.txt_del_shank));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestMeasure();
    }

    private void requestMeasure() {
        if (!TextUtils.isEmpty(this.record_on)) {
            if (DateFormatUtils.isToday(this.record_on)) {
                this.txt_date.setText("今天");
                this.txt_right.setBackgroundResource(R.drawable.a1x);
            } else {
                this.txt_date.setText(this.record_on);
            }
            showLoading();
            BooheeClient.build("record").get(String.format(URL_MEASURE, new
                    Object[]{UserPreference.getToken(getActivity()), this.record_on}), new
                    JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    AddMeasureFragment.this.recordMeasure = (RecordMeasure) FastJsonUtils
                            .fromJson(object, RecordMeasure.class);
                    AddMeasureFragment.this.refreshMeasure();
                }

                public void onFinish() {
                    super.onFinish();
                    AddMeasureFragment.this.dismissLoading();
                }
            }, getActivity());
        }
    }

    private void refreshMeasure() {
        if (this.recordMeasure == null) {
            this.recordMeasure = new RecordMeasure();
        }
        if (TextUtil.isNull(this.recordMeasure.brass)) {
            this.txt_del_brass.setVisibility(8);
            this.et_brass.setText("");
        } else {
            this.txt_del_brass.setVisibility(0);
            this.et_brass.setText(this.recordMeasure.brass);
        }
        if (TextUtil.isNull(this.recordMeasure.waist)) {
            this.txt_del_waist.setVisibility(8);
            this.et_waist.setText("");
        } else {
            this.txt_del_waist.setVisibility(0);
            this.et_waist.setText(this.recordMeasure.waist);
        }
        if (TextUtil.isNull(this.recordMeasure.hip)) {
            this.txt_del_hip.setVisibility(8);
            this.et_hip.setText("");
        } else {
            this.txt_del_hip.setVisibility(0);
            this.et_hip.setText(this.recordMeasure.hip);
        }
        if (TextUtil.isNull(this.recordMeasure.arm)) {
            this.txt_del_arm.setVisibility(8);
            this.et_arm.setText("");
        } else {
            this.txt_del_arm.setVisibility(0);
            this.et_arm.setText(this.recordMeasure.arm);
        }
        if (TextUtil.isNull(this.recordMeasure.thigh)) {
            this.txt_del_thigh.setVisibility(8);
            this.et_thigh.setText("");
        } else {
            this.txt_del_thigh.setVisibility(0);
            this.et_thigh.setText(this.recordMeasure.thigh);
        }
        if (TextUtil.isNull(this.recordMeasure.shank)) {
            this.txt_del_shank.setVisibility(8);
            this.et_shank.setText("");
            return;
        }
        this.txt_del_shank.setVisibility(0);
        this.et_shank.setText(this.recordMeasure.shank);
    }

    private void deleteMeasure(final String record_type, final TextView txt_add_record_del) {
        if (!TextUtils.isEmpty(this.record_on)) {
            JsonParams jsonParams = new JsonParams();
            jsonParams.put("record_on", this.record_on);
            jsonParams.put("token", UserPreference.getToken(getActivity()));
            BooheeClient.build("record").delete(String.format(URL_MEASURE_DELETE, new
                    Object[]{record_type}), jsonParams, new JsonCallback(getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    AddMeasureFragment.this.isRefresh = true;
                    txt_add_record_del.setVisibility(8);
                    if (MeasureType.WAIST.getType().equals(record_type)) {
                        AddMeasureFragment.this.deleteMeasureRecord(AddMeasureFragment.this
                                .record_on);
                        AddMeasureFragment.this.recordMeasure.waist = "";
                    } else if (MeasureType.BRASS.getType().equals(record_type)) {
                        AddMeasureFragment.this.recordMeasure.brass = "";
                    } else if (MeasureType.ARM.getType().equals(record_type)) {
                        AddMeasureFragment.this.recordMeasure.arm = "";
                    } else if (MeasureType.HIP.getType().equals(record_type)) {
                        AddMeasureFragment.this.recordMeasure.hip = "";
                    } else if (MeasureType.THIGH.getType().equals(record_type)) {
                        AddMeasureFragment.this.recordMeasure.thigh = "";
                    } else if (MeasureType.SHANK.getType().equals(record_type)) {
                        AddMeasureFragment.this.recordMeasure.shank = "";
                    }
                }

                public void onFinish() {
                    super.onFinish();
                }
            }, getActivity());
        }
    }

    private void postMeasure() {
        if (!TextUtils.isEmpty(this.record_on)) {
            JsonParams params = new JsonParams();
            params.put("record_on", this.record_on);
            params.put("token", UserPreference.getToken(getActivity()));
            params.put("waist", this.recordMeasure.waist);
            params.put("brass", this.recordMeasure.brass);
            params.put("hip", this.recordMeasure.hip);
            params.put("arm", this.recordMeasure.arm);
            params.put("thigh", this.recordMeasure.thigh);
            params.put("shank", this.recordMeasure.shank);
            BooheeClient.build("record").post(URL_MEASURE_POST, params, new JsonCallback
                    (getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if ("succeed".equals(object.optString("result"))) {
                        if (AddMeasureFragment.this.changeListener != null) {
                            AddMeasureFragment.this.changeListener.onFinish();
                        }
                        if (!TextUtils.isEmpty(AddMeasureFragment.this.recordMeasure.waist)) {
                            AddMeasureFragment.this.saveMeasureRecord(AddMeasureFragment.this
                                    .recordMeasure.waist, AddMeasureFragment.this.record_on);
                        }
                        EventBus.getDefault().post(new MeasureEvent());
                        AddMeasureFragment.this.dismiss();
                        MobclickAgent.onEvent(AddMeasureFragment.this.getActivity(), Event
                                .tool_addOtherRecordOK);
                        MobclickAgent.onEvent(AddMeasureFragment.this.getActivity(), Event
                                .tool_recordOK);
                        return;
                    }
                    Helper.showToast((CharSequence) "保存失败");
                }

                public void onFinish() {
                    super.onFinish();
                }
            }, getActivity());
        }
    }

    private boolean inputValidate(RecordMeasure recordMeasure) {
        return recordMeasureValidate(recordMeasure.waist, 50, 150, MeasureType.WAIST.getName())
                && recordMeasureValidate(recordMeasure.brass, 50, 150, MeasureType.BRASS.getName
                ()) && recordMeasureValidate(recordMeasure.hip, 50, 150, MeasureType.HIP.getName
                ()) && recordMeasureValidate(recordMeasure.arm, 15, 100, MeasureType.ARM.getName
                ()) && recordMeasureValidate(recordMeasure.thigh, 30, 150, MeasureType.THIGH
                .getName()) && recordMeasureValidate(recordMeasure.shank, 15, 100, MeasureType
                .SHANK.getName());
    }

    private boolean recordMeasureValidate(String value, int min, int max, String name) {
        if (!TextUtils.isEmpty(value)) {
            try {
                float f_value = Float.valueOf(value).floatValue();
                if (f_value < ((float) min) || f_value > ((float) max)) {
                    Helper.showToast(name + "输入不在合理范围");
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private void saveMeasureRecord(String waist, String record_on) {
        if (DateFormatUtils.isToday(record_on)) {
            this.mCache.put(CacheKey.LATEST_GIRTH, FastJsonUtils.toJson(new LocalMeasureRecord
                    (waist, record_on)));
        }
    }

    private void deleteMeasureRecord(String record_on) {
        JSONObject object = this.mCache.getAsJSONObject(CacheKey.LATEST_GIRTH);
        if (object != null) {
            LocalMeasureRecord measureRecord = (LocalMeasureRecord) FastJsonUtils.fromJson
                    (object, LocalMeasureRecord.class);
            if (measureRecord != null && record_on.equals(measureRecord.record_on)) {
                this.mCache.remove("latest_weight");
            }
        }
    }
}
