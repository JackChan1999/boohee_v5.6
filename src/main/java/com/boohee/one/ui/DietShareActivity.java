package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.baidu.location.aj;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.DietShareEatItem;
import com.boohee.myview.DietShareNutritionItem;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.ResolutionUtils;
import com.umeng.analytics.MobclickAgent;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.IPhotoView;

public class DietShareActivity extends BaseActivity {
    private static final String KEY_RECORD_ON = "key_record_on";
    private static final int[]  mSlogan       = new int[]{R.drawable.py, R.drawable.pz, R
            .drawable.q0, R.drawable.q1};
    @InjectView(2131427619)
    CircleImageView avatar;
    private String  mRecordOn;
    private boolean mShareBoohee;
    private User    mUser;
    @InjectView(2131427618)
    TextView     tvActivity;
    @InjectView(2131427614)
    TextView     tvDate;
    @InjectView(2131427617)
    TextView     tvEating;
    @InjectView(2131427616)
    TextView     tvPlan;
    @InjectView(2131427613)
    ScrollView   viewContent;
    @InjectView(2131427621)
    LinearLayout viewDietEat;
    @InjectView(2131427622)
    LinearLayout viewDietNuturitions;
    @InjectView(2131427615)
    LinearLayout viewMetabolism;
    @InjectView(2131427620)
    ImageView    viewSlogan;

    class BitmapAsync extends AsyncTask<Void, Void, Bitmap> {
        BitmapAsync() {
        }

        protected void onPreExecute() {
            Helper.showToast((CharSequence) "请稍等...");
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                String filePath = FileUtil.getPNGImagePath(DietShareActivity.this.activity,
                        bitmap, "SHARE_4_LINECHART");
                if (!TextUtils.isEmpty(filePath)) {
                    if (DietShareActivity.this.mShareBoohee) {
                        StatusPostTextActivity.comeWithPicture(DietShareActivity.this.activity,
                                filePath);
                    } else {
                        ShareManager.shareLocalImage(DietShareActivity.this.activity, filePath);
                    }
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }

        protected Bitmap doInBackground(Void... params) {
            return BitmapUtil.getBitmapByView(DietShareActivity.this.viewContent);
        }
    }

    @OnClick({2131427623, 2131427624})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share_boohee:
                this.mShareBoohee = true;
                share();
                return;
            case R.id.bt_share_sns:
                this.mShareBoohee = false;
                share();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b5);
        ButterKnife.inject((Activity) this);
        this.mRecordOn = getStringExtra(KEY_RECORD_ON);
        if (TextUtils.isEmpty(this.mRecordOn)) {
            this.mRecordOn = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd");
        }
        initView();
        initData();
    }

    private void initView() {
        this.viewMetabolism.getLayoutParams().height = (int) ((((float) ResolutionUtils
                .getScreenWidth(this)) - (aj.hA * getResources().getDimension(R.dimen.dh))) /
                IPhotoView.DEFAULT_MAX_SCALE);
        this.viewSlogan.setImageResource(getSloganResource());
        this.imageLoader.displayImage(getUserAvatar(), this.avatar, ImageLoaderOptions.avatar());
    }

    public int getSloganResource() {
        return mSlogan[new Random().nextInt(mSlogan.length)];
    }

    private void initData() {
        this.tvDate.setText(this.mRecordOn);
        this.tvPlan.setText(String.valueOf(getBudgetCalory()));
        showLoading();
        BooheeClient.build("record").get(String.format("/api/v2/eatings/share?record_on=%s", new
                Object[]{this.mRecordOn}), new JsonCallback(this) {
            public void ok(JSONObject object) {
                DietShareActivity.this.tvEating.setText(String.valueOf(object.optInt("intake")));
                JSONObject eating = object.optJSONObject("eatings");
                if (eating != null) {
                    DietShareActivity.this.parseEating(eating);
                }
                JSONObject activity = object.optJSONObject("activities");
                if (activity != null) {
                    DietShareActivity.this.parseActivity(activity);
                }
                JSONObject nutrition = object.optJSONObject("nuturitions");
                if (nutrition != null) {
                    DietShareActivity.this.parseNutrition(nutrition);
                }
            }

            public void onFinish() {
                DietShareActivity.this.dismissLoading();
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }
        }, this);
    }

    private void parseEating(JSONObject eating) {
        int i = 0;
        String[] keys = new String[]{"breakfast", "lunch", "dinner", DietShareEatItem.SNACK};
        this.viewDietEat.removeAllViews();
        int length = keys.length;
        while (i < length) {
            String key = keys[i];
            JSONObject object = eating.optJSONObject(key);
            int calory = object.optInt("calory");
            if (calory > 0) {
                String details = object.optString("details");
                DietShareEatItem item = new DietShareEatItem(this);
                item.setIcon(key);
                item.setTitle(String.valueOf(calory));
                item.setUnit("千卡");
                item.setSubTitle(details);
                try {
                    double percentage = object.optDouble("percentage");
                    String description = object.getString("description");
                    if (!(TextUtils.isEmpty(description) || description.equals("null"))) {
                        item.setIndicateTextWithString(description);
                    }
                } catch (JSONException e) {
                    try {
                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                this.viewDietEat.addView(item);
            }
            i++;
        }
    }

    private void parseActivity(JSONObject activity) {
        try {
            int calory = activity.optInt("calory");
            if (calory <= 0) {
                this.tvActivity.setText("0");
                return;
            }
            String details = activity.optString("details");
            DietShareEatItem item = new DietShareEatItem(this);
            item.setIcon("sport");
            item.setTitle(String.valueOf(calory));
            item.setUnit("千卡");
            item.setSubTitle(details);
            this.viewDietEat.addView(item);
            this.tvActivity.setText(String.valueOf(calory));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseNutrition(JSONObject nutrition) {
        String[] keys = new String[]{"carbohydrate,碳水化合物", "fat,脂肪", "protein,蛋白质"};
        this.viewDietNuturitions.removeAllViews();
        for (String keyValue : keys) {
            String key = keyValue.split(",")[0];
            String value = keyValue.split(",")[1];
            JSONObject item = nutrition.optJSONObject(key);
            double weight = item.optDouble("weight");
            double percentage = item.optDouble("percentage");
            DietShareNutritionItem view = new DietShareNutritionItem(this);
            view.setIngredient(value);
            try {
                String result = String.format("%.1f", new Object[]{Double.valueOf(weight)});
                if (result.endsWith(".0")) {
                    result = result.substring(0, result.indexOf(".0"));
                }
                view.setShowContent(String.format("%s克", new Object[]{result}));
            } catch (Exception e) {
                e.printStackTrace();
            }
            int percent = (int) (100.0d * percentage);
            if (percent > 0) {
                try {
                    view.setPercent(percent + "%");
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            view.setPercent("0%");
            try {
                String description = item.getString("description");
                if (!(TextUtils.isEmpty(description) || description.equals("null"))) {
                    view.setIndicateTextWithString(description);
                }
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
            this.viewDietNuturitions.addView(view);
        }
    }

    private int getBudgetCalory() {
        if (this.mUser == null) {
            this.mUser = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
        }
        return this.mUser.target_calory;
    }

    private String getUserAvatar() {
        if (this.mUser == null) {
            this.mUser = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
        }
        return this.mUser.avatar_url;
    }

    private void share() {
        MobclickAgent.onEvent(this.activity, this.mShareBoohee ? Event.DIET_SHARE_BOOHEE : Event
                .DIET_SHARE_SNS);
        new BitmapAsync().execute(new Void[0]);
    }

    public static void comeOnBaby(Context context, String recordOn) {
        if (context != null) {
            if (TextUtils.isEmpty(recordOn)) {
                recordOn = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd");
            }
            MobclickAgent.onEvent(context, Event.DIET_SHARE_CLICK);
            Intent intent = new Intent(context, DietShareActivity.class);
            intent.putExtra(KEY_RECORD_ON, recordOn);
            context.startActivity(intent);
        }
    }
}
