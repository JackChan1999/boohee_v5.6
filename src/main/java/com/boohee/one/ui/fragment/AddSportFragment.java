package com.boohee.one.ui.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.model.RecordSport;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.AddFinishAnimEvent;
import com.boohee.one.event.SportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.booheee.view.keyboard.OnSportResultListener;
import com.booheee.view.keyboard.SportKeyboard;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class AddSportFragment extends BaseDialogFragment {
    public static final int TYPE_ADD  = 0;
    public static final int TYPE_EDIT = 1;
    @InjectView(2131428170)
    ImageView iv_sport;
    private int         mIndex;
    private RecordSport mRecordSport;
    private int mType = 0;
    @InjectView(2131428171)
    SportKeyboard sport_keyboard;
    @InjectView(2131428152)
    TextView      txt_calory;
    @InjectView(2131428154)
    TextView      txt_delete;
    @InjectView(2131428151)
    TextView      txt_name;
    @InjectView(2131429207)
    TextView      txt_title;

    public static AddSportFragment newInstance(int type, int index, RecordSport recordSport) {
        AddSportFragment addSportFragment = new AddSportFragment();
        addSportFragment.mType = type;
        addSportFragment.mIndex = index;
        addSportFragment.mRecordSport = recordSport;
        return addSportFragment;
    }

    public static AddSportFragment newInstance(int type, RecordSport recordSport) {
        AddSportFragment addSportFragment = new AddSportFragment();
        addSportFragment.mType = type;
        addSportFragment.mRecordSport = recordSport;
        return addSportFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fd, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject((Object) this, view);
        initTitle();
        this.sport_keyboard.setOnResultListener(new OnSportResultListener() {
            public void onValue(float duration, float calory) {
                AddSportFragment.this.mRecordSport.duration = duration;
                AddSportFragment.this.mRecordSport.calory = calory;
            }
        });
        this.sport_keyboard.init(this.mRecordSport.duration, this.mRecordSport.mets,
                getlatestWeight());
    }

    private float getlatestWeight() {
        if (OnePreference.getLatestWeight() > 0.0f) {
            return OnePreference.getLatestWeight();
        }
        return 55.0f;
    }

    private void initTitle() {
        if (this.mType == 0) {
            this.txt_title.setText("添加运动");
            this.txt_delete.setVisibility(8);
        } else if (this.mType == 1) {
            this.txt_title.setText("修改运动");
            this.txt_delete.setVisibility(0);
        }
        ImageLoader.getInstance().displayImage(this.mRecordSport.thumb_img_url, this.iv_sport,
                ImageLoaderOptions.global((int) R.drawable.aa5));
        this.txt_name.setText(this.mRecordSport.activity_name);
        this.mRecordSport.calory = (float) this.mRecordSport.calcCalory(getlatestWeight());
        this.txt_calory.setText(this.mRecordSport.calcCalory(getlatestWeight()) + "");
    }

    @OnClick({2131429414, 2131429415, 2131428154})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_delete:
                new Builder(getActivity()).setMessage("确定要删除吗？").setPositiveButton("删除", new
                        OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddSportFragment.this.deleteActivity();
                    }
                }).setNegativeButton("取消", null).show();
                return;
            case R.id.txt_cancel:
                dismissAllowingStateLoss();
                return;
            case R.id.txt_commit:
                if (this.mRecordSport == null || this.mRecordSport.duration > 0.0f) {
                    MobclickAgent.onEvent(getActivity(), Event.TOOL_FOODANDSPORT_SPORT);
                    if (this.mType == 0) {
                        createActivity();
                        return;
                    } else {
                        updateActivity();
                        return;
                    }
                }
                Helper.showToast((CharSequence) "输入不能为零");
                return;
            default:
                return;
        }
    }

    private void createActivity() {
        if (this.mRecordSport != null) {
            RecordSport record = new SportRecordDao(getActivity()).add(this.mRecordSport);
            dismissAllowingStateLoss();
            if (record != null) {
                EventBus.getDefault().post(new SportEvent().setRecordSport(record).setEditType(1));
            }
            if (this.mRecordSport != null) {
                EventBus.getDefault().post(new AddFinishAnimEvent().setThumb_image_name(this
                        .mRecordSport.thumb_img_url));
            }
            MobclickAgent.onEvent(getActivity(), Event.tool_addSportRecordOK);
            MobclickAgent.onEvent(getActivity(), Event.tool_recordOK);
            if (this.changeListener != null) {
                this.changeListener.onFinish();
            }
        }
    }

    private void updateActivity() {
        if (this.mRecordSport != null) {
            new SportRecordDao(getActivity()).update(this.mRecordSport);
            if (this.mRecordSport != null) {
                EventBus.getDefault().post(new SportEvent().setRecordSport(this.mRecordSport)
                        .setEditType(1));
                EventBus.getDefault().post(new AddFinishAnimEvent().setThumb_image_name(this
                        .mRecordSport.thumb_img_url));
            }
            dismissAllowingStateLoss();
        }
    }

    private void deleteActivity() {
        if (HttpUtils.isNetworkAvailable(getActivity())) {
            showLoading();
            RecordApi.deleteActivity(this.mRecordSport.id, getActivity(), new JsonCallback
                    (getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    EventBus.getDefault().post(new SportEvent().setIndex(AddSportFragment.this
                            .mIndex).setEditType(3));
                    AddSportFragment.this.dismissAllowingStateLoss();
                }

                public void onFinish() {
                    super.onFinish();
                    AddSportFragment.this.dismissLoading();
                }
            });
            return;
        }
        new SportRecordDao(getActivity()).delete(this.mRecordSport);
        dismissAllowingStateLoss();
        EventBus.getDefault().post(new SportEvent().setIndex(this.mIndex).setEditType(3));
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
