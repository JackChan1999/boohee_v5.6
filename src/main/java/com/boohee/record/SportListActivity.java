package com.boohee.record;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.RecordSport;
import com.boohee.one.R;
import com.boohee.one.event.SportEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FoodUtils;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;

import org.json.JSONObject;

public class SportListActivity extends GestureActivity {
    private static final String KEY_SPORT_LIST = "key_sport_list";
    static final         String TAG            = SportListActivity.class.getName();
    @InjectView(2131427684)
    LinearLayout ll_food_list;
    private ArrayList<RecordSport> mRecordSports;
    @InjectView(2131427683)
    TextView tvCalorySuggest;
    @InjectView(2131427682)
    TextView tvMorePercent;
    @InjectView(2131427685)
    TextView tv_total;

    public static void start(Context context, ArrayList<RecordSport> recordFoods) {
        Intent starter = new Intent(context, SportListActivity.class);
        starter.putParcelableArrayListExtra(KEY_SPORT_LIST, recordFoods);
        context.startActivity(starter);
    }

    public void onCreate(Bundle saveState) {
        super.onCreate(saveState);
        setContentView(R.layout.bb);
        ButterKnife.inject((Activity) this);
        setTitle("运动");
        handleIntent();
        initView();
        refreshTotal();
    }

    private void handleIntent() {
        this.mRecordSports = getIntent().getParcelableArrayListExtra(KEY_SPORT_LIST);
    }

    private void initView() {
        this.tvMorePercent.setVisibility(8);
        this.tvCalorySuggest.setVisibility(8);
        this.ll_food_list.removeAllViews();
        if (this.mRecordSports != null && this.mRecordSports.size() > 0) {
            for (int i = 0; i < this.mRecordSports.size(); i++) {
                View view = getRecordView((RecordSport) this.mRecordSports.get(i), i);
                if (view != null) {
                    this.ll_food_list.addView(view);
                }
            }
        }
    }

    private View getRecordView(final RecordSport recordSport, final int index) {
        View itemView = null;
        if (recordSport != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.i1, null);
            RelativeLayout rl_del = (RelativeLayout) itemView.findViewById(R.id.rl_del);
            TextView tv_calory = (TextView) itemView.findViewById(R.id.tv_calory);
            TextView tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            ((TextView) itemView.findViewById(R.id.tv_name)).setText(recordSport.activity_name);
            tv_calory.setText(Math.round(recordSport.calory) + "千卡");
            StringBuilder stringBuilder = new StringBuilder();
            String str = (recordSport.activity_id == 0 && FoodUtils.isKM(recordSport.unit_name))
                    ? recordSport.duration + "" : Math.round(recordSport.duration) + "";
            tv_count.setText(stringBuilder.append(str).append(recordSport.unit_name).toString());
            rl_del.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new Builder(SportListActivity.this.activity).setMessage("确定要删除吗？")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SportListActivity.this.deleteActivity(recordSport.id, index);
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
        }
        return itemView;
    }

    private void refreshTotal() {
        int i;
        float total = 0.0f;
        if (this.mRecordSports != null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i & (this.mRecordSports.size() > 0 ? 1 : 0)) != 0) {
            for (int i2 = 0; i2 < this.mRecordSports.size(); i2++) {
                total += ((RecordSport) this.mRecordSports.get(i2)).calory;
            }
        }
        this.tv_total.setText(String.format("小计: %d千卡", new Object[]{Integer.valueOf(Math.round
                (total))}));
    }

    private void deleteActivity(int activity_id, final int position) {
        showLoading();
        RecordApi.deleteActivity(activity_id, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                SportListActivity.this.mRecordSports.remove(position);
                SportListActivity.this.initView();
                EventBus.getDefault().post(new SportEvent().setIndex(position).setEditType(3));
                SportListActivity.this.refreshTotal();
            }

            public void onFinish() {
                super.onFinish();
                SportListActivity.this.dismissLoading();
            }
        });
    }
}
