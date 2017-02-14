package com.boohee.food;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordPhoto;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.event.DietEvent;
import com.boohee.one.event.PhotoDietEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.FoodUtils;
import com.boohee.utils.FormulaUtils;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

public class FoodListActivity extends GestureActivity {
    private static final String KEY_RECORD_FOOD_LIST  = "key_record_food_list";
    private static final String KEY_RECORD_PHOTO_LIST = "key_record_photo_list";
    private static final String KEY_TIME_TYPE         = "key_time_type";
    static final         String TAG                   = FoodListActivity.class.getName();
    @InjectView(2131427684)
    LinearLayout ll_food_list;
    private ArrayList<RecordFood>  mRecordFoods;
    private ArrayList<RecordPhoto> mRecordPhotos;
    private int mTimeType = -1;
    private float total;
    @InjectView(2131427683)
    TextView tvCalorySuggest;
    @InjectView(2131427682)
    TextView tvMorePercent;
    @InjectView(2131427685)
    TextView tv_total;

    public static void start(Context context, int timeType, ArrayList<RecordFood> recordFoods,
                             ArrayList<RecordPhoto> recordPhotos) {
        Intent starter = new Intent(context, FoodListActivity.class);
        starter.putParcelableArrayListExtra(KEY_RECORD_FOOD_LIST, recordFoods);
        starter.putParcelableArrayListExtra(KEY_RECORD_PHOTO_LIST, recordPhotos);
        starter.putExtra(KEY_TIME_TYPE, timeType);
        context.startActivity(starter);
    }

    public void onCreate(Bundle saveState) {
        super.onCreate(saveState);
        setContentView(R.layout.bb);
        EventBus.getDefault().register(this);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initView();
        refreshTotal();
    }

    private void handleIntent() {
        this.mRecordFoods = getIntent().getParcelableArrayListExtra(KEY_RECORD_FOOD_LIST);
        this.mRecordPhotos = getIntent().getParcelableArrayListExtra(KEY_RECORD_PHOTO_LIST);
        this.mTimeType = getIntExtra(KEY_TIME_TYPE);
    }

    private void initView() {
        int i;
        View view;
        this.ll_food_list.removeAllViews();
        if (this.mTimeType != -1) {
            setTitle(FoodUtils.getDietName(this.ctx, this.mTimeType));
        }
        if (this.mRecordFoods != null && this.mRecordFoods.size() > 0) {
            for (i = 0; i < this.mRecordFoods.size(); i++) {
                view = getRecordView(i, (RecordFood) this.mRecordFoods.get(i));
                if (view != null) {
                    this.ll_food_list.addView(view);
                }
            }
        }
        if (this.mRecordPhotos != null && this.mRecordPhotos.size() > 0) {
            for (i = 0; i < this.mRecordPhotos.size(); i++) {
                view = getPhotoDietItemView(i, (RecordPhoto) this.mRecordPhotos.get(i));
                if (view != null) {
                    this.ll_food_list.addView(view);
                }
            }
        }
    }

    private View getRecordView(final int index, final RecordFood food) {
        if (food == null) {
            return null;
        }
        View itemView = LayoutInflater.from(this).inflate(R.layout.i1, null);
        RelativeLayout rl_del = (RelativeLayout) itemView.findViewById(R.id.rl_del);
        TextView tv_calory = (TextView) itemView.findViewById(R.id.tv_calory);
        TextView tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        ((TextView) itemView.findViewById(R.id.tv_name)).setText(food.food_name);
        tv_calory.setText(Math.round(food.calory) + "千卡");
        tv_count.setText(food.amount + food.unit_name);
        rl_del.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Builder(FoodListActivity.this.activity).setMessage("确定要删除吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FoodListActivity.this.deleteEating(food.id, index);
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
        return itemView;
    }

    private View getPhotoDietItemView(final int index, final RecordPhoto recordPhoto) {
        View itemView = null;
        if (recordPhoto != null) {
            itemView = LayoutInflater.from(this).inflate(R.layout.io, null);
            RelativeLayout rl_del = (RelativeLayout) itemView.findViewById(R.id.rl_del);
            TextView tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            TextView tv_calory = (TextView) itemView.findViewById(R.id.tv_calory);
            if (TextUtils.isEmpty(recordPhoto.name)) {
                tv_name.setText("拍照记录");
            } else {
                tv_name.setText(recordPhoto.name);
            }
            if (recordPhoto.status == 1) {
                tv_calory.setText("正在估算");
            } else if (recordPhoto.status == 4 || recordPhoto.status == 2) {
                tv_calory.setText(recordPhoto.calory > 0.0f ? Math.round(recordPhoto.calory) +
                        "千卡" : "");
            }
            rl_del.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new Builder(FoodListActivity.this.activity).setMessage("确定要删除吗？")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FoodListActivity.this.deletePhotoEating(recordPhoto, index);
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
        }
        return itemView;
    }

    private void deletePhotoEating(final RecordPhoto recordPhoto, final int index) {
        showLoading();
        RecordApi.deleteDietPhotos(this.activity, recordPhoto.id, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (recordPhoto != null) {
                    EventBus.getDefault().post(new PhotoDietEvent().setTimeType(recordPhoto
                            .time_type).setIndex(index).setEditType(3));
                    FoodListActivity.this.initView();
                    FoodListActivity.this.refreshTotal();
                }
            }

            public void onFinish() {
                super.onFinish();
                FoodListActivity.this.dismissLoading();
            }
        });
    }

    private void deleteEating(int id, final int position) {
        showLoading();
        RecordApi.deleteEating(id, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                FoodListActivity.this.mRecordFoods.remove(position);
                FoodListActivity.this.initView();
                EventBus.getDefault().post(new DietEvent().setTimeType(FoodListActivity.this
                        .mTimeType).setIndex(position).setEditType(3));
                FoodListActivity.this.refreshTotal();
            }

            public void onFinish() {
                super.onFinish();
                FoodListActivity.this.dismissLoading();
            }
        });
    }

    private void refreshTotal() {
        int i;
        int i2;
        this.total = 0.0f;
        if (this.mRecordFoods != null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i & (this.mRecordFoods.size() > 0 ? 1 : 0)) != 0) {
            for (i2 = 0; i2 < this.mRecordFoods.size(); i2++) {
                this.total = ((RecordFood) this.mRecordFoods.get(i2)).calory + this.total;
            }
        }
        if (this.mRecordPhotos != null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i & (this.mRecordPhotos.size() > 0 ? 1 : 0)) != 0) {
            for (i2 = 0; i2 < this.mRecordPhotos.size(); i2++) {
                RecordPhoto recordPhoto = (RecordPhoto) this.mRecordPhotos.get(i2);
                if (recordPhoto.status != 1) {
                    this.total += recordPhoto.calory;
                }
                this.total += recordPhoto.calory;
            }
        }
        this.tv_total.setText(String.format("小计: %d千卡", new Object[]{Integer.valueOf(Math.round
                (this.total))}));
        refreshCaloryView();
    }

    private void refreshCaloryView() {
        if (this.mTimeType == 6 || this.mTimeType == 7 || this.mTimeType == 8) {
            this.tvCalorySuggest.setVisibility(8);
            this.tvMorePercent.setVisibility(8);
            return;
        }
        int[] suggestCalorie = getSuggestCalorie();
        this.tvCalorySuggest.setText(String.format(Locale.getDefault(), "%s建议：%d ~ %d 千卡", new
                Object[]{FoodUtils.getDietName(this.ctx, this.mTimeType), Integer.valueOf
                (suggestCalorie[0]), Integer.valueOf(suggestCalorie[1])}));
        if (this.total <= ((float) suggestCalorie[1]) || suggestCalorie[1] == 0) {
            this.tvMorePercent.setVisibility(8);
            return;
        }
        this.tvMorePercent.setText("多吃了 " + ArithmeticUtil.round(((this.total / ((float) (
                (suggestCalorie[0] + suggestCalorie[1]) / 2))) - 1.0f) * 100.0f, 1) + "%");
    }

    private int[] getSuggestCalorie() {
        return FormulaUtils.calorieLimit((float) new UserDao(this.ctx).queryWithToken
                (UserPreference.getToken(this.ctx)).target_calory, this.mTimeType);
    }

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String record_on) {
    }
}
