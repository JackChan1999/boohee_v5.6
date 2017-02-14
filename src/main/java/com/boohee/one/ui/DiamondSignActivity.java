package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ApiUrls;
import com.boohee.api.StatusApi;
import com.boohee.calendar.CheckCalendarAdapter;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.CheckInfo;
import com.boohee.model.CheckRecord;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.AnimCheckBox;
import com.boohee.widgets.AnimCheckBox.OnCheckedChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.common.SocializeConstants;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DiamondSignActivity extends GestureActivity {
    private static final String DIAMOND_RULE_URL = "http://shop.boohee" +
            ".com/store/pages/checkin_rule";
    private CheckCalendarAdapter adapter;
    @InjectView(2131427628)
    GridView     calendarGrid;
    @InjectView(2131427904)
    AnimCheckBox checkBox;
    private CheckInfo checkInfo;
    @InjectView(2131427592)
    Button      exchangeBtn;
    @InjectView(2131427627)
    ViewFlipper flipper;
    @InjectView(2131429256)
    View        leftDate;
    private List<CheckRecord> mRecords = new ArrayList();
    private String record_on;
    @InjectView(2131427599)
    Button repairBtn;
    private String repairDate = "";
    @InjectView(2131429257)
    View     rightDate;
    @InjectView(2131427597)
    TextView tvAllDay;
    @InjectView(2131427594)
    TextView tvContinueDay;
    @InjectView(2131428947)
    TextView tvDate;
    @InjectView(2131427591)
    TextView tvNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b2);
        ButterKnife.inject((Activity) this);
        initDate();
        initListener();
        requestRecords();
        initView();
    }

    protected void onResume() {
        super.onResume();
        requestCheckNumber();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.l, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_diamond_rule:
                BrowserActivity.comeOnBaby(this.activity, getString(R.string.j1), DIAMOND_RULE_URL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(new Date(), "yyyyMM");
        this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                .record_on, "yyyyMM"), "yyyy年M月"));
    }

    private void initListener() {
        this.calendarGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= DiamondSignActivity.this.adapter.startPosition() && position <
                        DiamondSignActivity.this.adapter.endPosition()) {
                    if (DiamondSignActivity.this.adapter.getDate(position).after(new Date())) {
                        DiamondSignActivity.this.repairBtn.setEnabled(false);
                        return;
                    }
                    long dayCount = DateFormatUtils.countDay(DiamondSignActivity.this.adapter
                            .getDateString(position), DateFormatUtils.date2string(new Date(),
                            "yyyy-MM-dd"));
                    if (dayCount <= 0 || dayCount > 7 || DiamondSignActivity.this.adapter
                            .isChecked(position)) {
                        DiamondSignActivity.this.repairBtn.setEnabled(false);
                    } else {
                        DiamondSignActivity.this.repairBtn.setEnabled(true);
                        DiamondSignActivity.this.repairDate = DiamondSignActivity.this.adapter
                                .getDateString(position);
                    }
                    if (dayCount > 0) {
                        DiamondSignActivity.this.adapter.setCurrentPosition(position);
                        DiamondSignActivity.this.adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void initView() {
        this.checkBox.setChecked(OnePreference.getPrefDiamondSignRemind());
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onChange(boolean checked) {
                Helper.showLog("AnimCheckBox", checked + "");
                OnePreference.setPrefDiamondSignRemind(checked);
            }
        });
    }

    private void repair() {
        StatusApi.repair(this.activity, this.repairDate, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                DiamondSignActivity.this.requestCheckNumber();
                DiamondSignActivity.this.requestRecords();
                DiamondSignActivity.this.repairBtn.setEnabled(false);
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
            }

            public void ok(JSONObject object, boolean hasError) {
                super.ok(object, hasError);
            }
        });
    }

    private void requestCheckNumber() {
        StatusApi.getCheckNumber(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                DiamondSignActivity.this.checkInfo = (CheckInfo) FastJsonUtils.fromJson(object,
                        CheckInfo.class);
                if (DiamondSignActivity.this.checkInfo != null) {
                    DiamondSignActivity.this.tvContinueDay.setText(DiamondSignActivity.this
                            .checkInfo.checkin_days + "");
                    DiamondSignActivity.this.tvAllDay.setText(DiamondSignActivity.this.checkInfo
                            .all_checkin_count + "");
                    DiamondSignActivity.this.tvNumber.setText(DiamondSignActivity.this.checkInfo
                            .envious_count + "");
                }
            }
        });
    }

    private void requestRecords() {
        showLoading();
        StatusApi.getCheckRecord(this.record_on, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONArray array) {
                super.ok(array);
                DiamondSignActivity.this.mRecords.clear();
                List<CheckRecord> recordList = FastJsonUtils.parseList(array.toString(),
                        CheckRecord.class);
                Helper.showLog("JsonCallback", SocializeConstants.OP_OPEN_PAREN + recordList.size
                        () + SocializeConstants.OP_CLOSE_PAREN);
                if (recordList != null && recordList.size() > 0) {
                    DiamondSignActivity.this.mRecords.addAll(recordList);
                    Collections.reverse(DiamondSignActivity.this.mRecords);
                }
                if (!TextUtils.isEmpty(DiamondSignActivity.this.record_on)) {
                    DiamondSignActivity.this.adapter = new CheckCalendarAdapter
                            (DiamondSignActivity.this.ctx, DateFormatUtils.string2date
                                    (DiamondSignActivity.this.record_on, "yyyyMM"),
                                    DiamondSignActivity.this.mRecords);
                    DiamondSignActivity.this.calendarGrid.setAdapter(DiamondSignActivity.this
                            .adapter);
                }
            }

            public void onFinish() {
                super.onFinish();
                DiamondSignActivity.this.dismissLoading();
            }
        });
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, DiamondSignActivity.class));
        }
    }

    @OnClick({2131427592, 2131429256, 2131429257, 2131427590, 2131427599})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_check_in:
            case R.id.btn_go_exchange:
                MobclickAgent.onEvent(this.ctx, Event.HOME_DIAMOND);
                MobclickAgent.onEvent(this, Event.HOME_CLICKCHCEKIN);
                BrowserActivity.comeOnBaby(this, getString(R.string.a2h), BooheeClient.build
                        ("status").getDefaultURL(String.format(ApiUrls.DIAMOND_CHECHIN, new
                        Object[]{UserPreference.getToken(this)})));
                return;
            case R.id.btn_repair:
                repair();
                return;
            case R.id.rl_left:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, -1), "yyyyMM");
                this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                        .record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showPrevious();
                this.repairBtn.setEnabled(false);
                requestRecords();
                return;
            case R.id.rl_right:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, 1), "yyyyMM");
                this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                        .record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showNext();
                this.repairBtn.setEnabled(false);
                requestRecords();
                return;
            default:
                return;
        }
    }
}
