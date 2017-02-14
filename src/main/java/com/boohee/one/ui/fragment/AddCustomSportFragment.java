package com.boohee.one.ui.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.model.RecordSport;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.SportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.booheee.view.keyboard.CustomSportKeyboard;
import com.booheee.view.keyboard.OnCustomSportResultListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import org.json.JSONObject;

public class AddCustomSportFragment extends BaseDialogFragment {
    public static final int TYPE_ADD  = 0;
    public static final int TYPE_EDIT = 1;
    @InjectView(2131428150)
    CircleImageView     civ_title;
    @InjectView(2131428156)
    CustomSportKeyboard custom_sport_keyboard;
    private int         mIndex;
    private RecordSport mRecordSport;
    private int mType = 0;
    @InjectView(2131428152)
    TextView txt_calory;
    @InjectView(2131428154)
    TextView txt_delete;
    @InjectView(2131428151)
    TextView txt_name;
    @InjectView(2131429207)
    TextView txt_title;
    @InjectView(2131428153)
    TextView txt_unit;

    public static AddCustomSportFragment newInstance(int type, int index, RecordSport recordFood) {
        AddCustomSportFragment addSportFragment = new AddCustomSportFragment();
        addSportFragment.mType = type;
        addSportFragment.mIndex = index;
        addSportFragment.mRecordSport = recordFood;
        return addSportFragment;
    }

    public static AddCustomSportFragment newInstance(int type, RecordSport recordFood) {
        AddCustomSportFragment addCustomDietFragment = new AddCustomSportFragment();
        addCustomDietFragment.mType = type;
        addCustomDietFragment.mRecordSport = recordFood;
        return addCustomDietFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.f_, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        initTitle();
        this.custom_sport_keyboard.setOnResultListener(new OnCustomSportResultListener() {
            public void onValue(float amount, float calory) {
                AddCustomSportFragment.this.mRecordSport.duration = amount;
                AddCustomSportFragment.this.mRecordSport.calory = calory;
            }
        });
        this.custom_sport_keyboard.init(this.mRecordSport.duration, this.mRecordSport.calory,
                this.mRecordSport.unit_name);
    }

    private void initTitle() {
        if (this.mType == 0) {
            this.txt_title.setText("添加自定义运动");
            this.txt_delete.setVisibility(8);
        } else if (this.mType == 1) {
            this.txt_title.setText("修改自定义运动");
            this.txt_delete.setVisibility(0);
        }
        if (this.mRecordSport != null) {
            this.txt_name.setText(this.mRecordSport.activity_name);
            this.txt_calory.setText(Math.round(this.mRecordSport.calory) + "");
            this.txt_unit.setText(String.format(" 千卡/%1$.1f%2$s", new Object[]{Float.valueOf(this
                    .mRecordSport.duration), this.mRecordSport.unit_name}));
            ImageLoader.getInstance().displayImage("", this.civ_title, ImageLoaderOptions.global(
                    (int) R.drawable.aa5));
        }
    }

    @OnClick({2131429414, 2131429415, 2131428154})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_delete:
                new Builder(getActivity()).setMessage("确定要删除吗？").setPositiveButton("删除", new
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCustomSportFragment.this.deleteActivity();
                    }
                }).setNegativeButton("取消", null).show();
                return;
            case R.id.txt_cancel:
                dismissAllowingStateLoss();
                return;
            case R.id.txt_commit:
                if (this.mRecordSport != null && this.mRecordSport.duration <= 0.0f) {
                    Helper.showToast((CharSequence) "输入不能为零");
                    return;
                } else if (this.mType == 0) {
                    createActivity();
                    return;
                } else {
                    updateActivity();
                    return;
                }
            default:
                return;
        }
    }

    private void createActivity() {
        showLoading();
        RecordApi.createCustomActivity(this.mRecordSport, getActivity(), new JsonCallback
                (getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomSportFragment.this.dismissAllowingStateLoss();
                RecordSport recordSport = RecordSport.parse(object);
                if (recordSport != null) {
                    EventBus.getDefault().post(new SportEvent().setRecordSport(recordSport)
                            .setEditType(1));
                    EventBus.getDefault().post(new AddFinishAnimEvent());
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomSportFragment.this.dismissLoading();
            }
        });
    }

    private void updateActivity() {
        showLoading();
        RecordApi.updateCustomActivity(this.mRecordSport, getActivity(), new JsonCallback
                (getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomSportFragment.this.dismissAllowingStateLoss();
                if (AddCustomSportFragment.this.mRecordSport != null) {
                    EventBus.getDefault().post(new SportEvent().setRecordSport
                            (AddCustomSportFragment.this.mRecordSport).setEditType(1));
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomSportFragment.this.dismissLoading();
            }
        });
    }

    private void deleteActivity() {
        showLoading();
        RecordApi.deleteActivity(this.mRecordSport.id, getActivity(), new JsonCallback
                (getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                AddCustomSportFragment.this.dismissAllowingStateLoss();
                if (AddCustomSportFragment.this.mRecordSport != null) {
                    EventBus.getDefault().post(new SportEvent().setIndex(AddCustomSportFragment
                            .this.mIndex).setRecordSport(AddCustomSportFragment.this
                            .mRecordSport).setEditType(3));
                }
            }

            public void onFinish() {
                super.onFinish();
                AddCustomSportFragment.this.dismissLoading();
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String record_on) {
    }
}
