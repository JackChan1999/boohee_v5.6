package com.boohee.myview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.mine.McLatest;
import com.boohee.model.mine.McSummary;
import com.boohee.modeldao.UserDao;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.myview.risenumber.RiseNumberTextView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.GoSportActivity;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FormulaUtils;
import com.boohee.widgets.TextProgressBar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class MineHeadViewFactory {
    public static final  int             HEAD_EAT           = 2;
    public static final  int             HEAD_PEROID        = 4;
    public static final  int             HEAD_WEIGHT        = 1;
    public static final  String          LEFT_CENTER        = "LEFT_CENTER";
    public static final  String          LEFT_TOP           = "LEFT_TOP";
    public static final  String          LEFT_UNIT          = "LEFT_UNIT";
    private static final float           MAX_CALORY         = 2500.0f;
    public static final  String          RIGHT_BOTTOM       = "RIGHT_BOTTOM";
    public static final  String          RIGHT_CENTER       = "RIGHT_CENTER";
    public static final  String          RIGHT_TOP          = "RIGHT_TOP";
    private              TextProgressBar eatTextProgressBar = null;
    private              boolean         isRunEatAnim       = false;
    private              boolean         isRunWeightAnim    = false;
    private              Context         mContext           = null;
    private McLatest  mcLatest;
    private McSummary mcSummary;
    private TextProgressBar peroidTextProgressBar = null;
    private String record_on;
    private Resources resources = null;
    private float totalDietCalory;
    private float totalSportCalory;
    private User  user;
    private TextProgressBar weightTextProgressBar = null;

    public MineHeadViewFactory(Context context) {
        this.mContext = context;
        this.resources = this.mContext.getResources();
    }

    public View createHeadView(int type, String record_on, float dietCalory, float sportCalory) {
        this.totalDietCalory = dietCalory;
        this.totalSportCalory = sportCalory;
        this.record_on = record_on;
        this.user = new UserDao(this.mContext).queryWithToken(UserPreference.getToken(this
                .mContext));
        switch (type) {
            case 1:
                return createWeightHeadView(this.user);
            case 2:
                return createEatHeadView(this.user, dietCalory, sportCalory);
            case 4:
                return createPeroidHeadView(this.user);
            default:
                return null;
        }
    }

    private View createWeightHeadView(User user) {
        View view = View.inflate(this.mContext, R.layout.px, null);
        Map<String, String> params = new HashMap();
        params.put(LEFT_TOP, "已减去");
        float latestWeight = new WeightRecordDao(this.mContext).getLastestWeight();
        if (user != null) {
            if (user.target_weight < 0.0f) {
                user.target_weight = user.begin_weight;
            }
            String str = LEFT_CENTER;
            String str2 = "%.1f";
            Object[] objArr = new Object[1];
            objArr[0] = Float.valueOf(user.begin_weight - latestWeight < 0.0f ? 0.0f : user
                    .begin_weight - latestWeight);
            params.put(str, String.format(str2, objArr));
            params.put(LEFT_UNIT, "kg");
            params.put(RIGHT_TOP, "初始体重：" + String.format("%.1f", new Object[]{Float.valueOf(user
                    .begin_weight)}) + " kg");
            params.put(RIGHT_CENTER, "目标体重：" + String.format("%.1f", new Object[]{Float.valueOf
                    (user.target_weight)}) + " kg");
            params.put(RIGHT_BOTTOM, "BMI：" + String.format("%.1f", new Object[]{Float.valueOf
                    (user.calcBmi(latestWeight))}));
        } else {
            params.put(LEFT_CENTER, "0");
            params.put(LEFT_UNIT, "kg");
            params.put(RIGHT_TOP, "初始体重：0 kg");
            params.put(RIGHT_CENTER, "目标体重：0 kg");
            params.put(RIGHT_BOTTOM, "BMI：--");
        }
        invalidateView(view.findViewById(R.id.view_mine_header_weight), params, 2);
        this.weightTextProgressBar = (TextProgressBar) view.findViewById(R.id
                .view_mine_header_weight_textBar);
        this.weightTextProgressBar.setProgressRate(0.0f);
        this.weightTextProgressBar.setProgressColor(Color.parseColor("#FF00AEF0"));
        this.weightTextProgressBar.setText("0 kg");
        this.weightTextProgressBar.setBgBitmap(((BitmapDrawable) this.resources.getDrawable(R
                .drawable.ma)).getBitmap());
        view.setTag(this.weightTextProgressBar);
        return view;
    }

    private View createEatHeadView(User user, float dietCalory, float sportCalory) {
        View view = View.inflate(this.mContext, R.layout.pt, null);
        View llCalory = view.findViewById(R.id.ll_calory);
        TextView tvEatTitle = (TextView) view.findViewById(R.id.tv_eat_title);
        RiseNumberTextView tvCanEat = (RiseNumberTextView) view.findViewById(R.id.tv_can_eat);
        TextView tvSport = (TextView) view.findViewById(R.id.tv_sport);
        TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
        ((TextView) view.findViewById(R.id.tv_input)).setText(String.valueOf(Math.round
                (dietCalory)));
        tvSport.setText(String.valueOf(Math.round(sportCalory)));
        View llSportHint = view.findViewById(R.id.ll_sport_hint);
        int caloryType = FormulaUtils.calorieType((float) user.target_calory, dietCalory,
                sportCalory);
        int sub = (int) FormulaUtils.needCalorie((float) user.target_calory, dietCalory,
                sportCalory);
        tvCanEat.setIntFormat();
        llCalory.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BooheeScheme.handleUrl(MineHeadViewFactory.this.mContext, "http://shop.boohee" +
                        ".com/store/pages/calories_budget");
            }
        });
        if (sub >= 0) {
            tvEatTitle.setText("还可以吃/千卡");
            tvCanEat.withNumber(sub).start();
        } else {
            tvEatTitle.setText("多吃了/千卡");
            tvCanEat.withNumber(Math.abs(sub)).start();
        }
        if (caloryType <= 0) {
            llSportHint.setVisibility(8);
            if (caloryType < 0) {
                tvStatus.setText("摄入未达标");
            } else {
                tvStatus.setText("摄入适中");
            }
            llCalory.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.hb));
        } else {
            tvStatus.setText("摄入过量");
            llCalory.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.jr));
            if (DateFormatUtils.isToday(this.record_on)) {
                llSportHint.setVisibility(0);
                ((TextView) llSportHint.findViewById(R.id.tv_sport_hint)).setText(getSportHint(
                        (float) Math.abs(sub)));
                final User user2 = user;
                final float f = dietCalory;
                final float f2 = sportCalory;
                llSportHint.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        GoSportActivity.startActivity(MineHeadViewFactory.this.mContext,
                                MineHeadViewFactory.this.record_on, (float) user2.target_calory,
                                f, f2);
                    }
                });
            } else {
                llSportHint.setVisibility(8);
            }
        }
        return view;
    }

    private CharSequence getSportHint(float overCalorie) {
        String[] sportName = new String[]{"快走", "跑步(慢)", "爬楼梯", "跳舞"};
        float[] sportMet = new float[]{5.0f, 6.5f, 6.4f, 6.0f};
        int minute = (int) ((overCalorie / FormulaUtils.calcCalorie(sportMet[(int) (Math.random()
                * ((double) sportName.length))], new WeightRecordDao(this.mContext)
                .getLastestWeight())) * 60.0f);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("多吃了可用运动抵消。如：");
        int start = sb.length();
        sb.append(String.format("%s %d分钟", new Object[]{sportName[choose], Integer.valueOf
                (minute)}));
        sb.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.hb)),
                start, sb.length(), 17);
        return sb;
    }

    private View createPeroidHeadView(User user) {
        final View view = View.inflate(this.mContext, R.layout.pw, null);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id
                .view_mine_header_loading);
        setPeroidData(this.mcLatest, this.mcSummary, view);
        progressBar.setVisibility(0);
        RecordApi.getMcPeriods(this.mContext, new JsonCallback(this.mContext) {
            public void ok(JSONObject object) {
                super.ok(object);
                MineHeadViewFactory.this.mcLatest = McLatest.parseSelf(object.optJSONObject
                        ("mc_latest"));
                MineHeadViewFactory.this.mcSummary = McSummary.parseSelf(object.optJSONObject
                        ("mc_summary"));
                MineHeadViewFactory.this.setPeroidData(MineHeadViewFactory.this.mcLatest,
                        MineHeadViewFactory.this.mcSummary, view);
            }

            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(8);
            }
        });
        this.peroidTextProgressBar = (TextProgressBar) view.findViewById(R.id
                .view_mine_header_peroid_textBar);
        this.peroidTextProgressBar.setBackgroundColor(-7829368);
        this.peroidTextProgressBar.setProgressRate(0.0f);
        view.setTag(this.peroidTextProgressBar);
        return view;
    }

    private void setPeroidData(McLatest mcLatest, McSummary mcSummary, View view) {
        Map<String, String> params = new HashMap();
        params.put(LEFT_TOP, "距离下次还有");
        params.put(LEFT_UNIT, "天");
        if (mcLatest != null) {
            if (mcLatest.mc_index > 0) {
                params.put(LEFT_TOP, "生理期第");
                params.put(LEFT_CENTER, mcLatest.mc_index + "");
            } else {
                params.put(LEFT_CENTER, mcLatest.mc_distance + "");
            }
            if (TextUtils.isEmpty(mcLatest.oviposit_day)) {
                params.put(RIGHT_BOTTOM, "排卵日: --");
            } else {
                Date date = DateHelper.parseString(mcLatest.oviposit_day);
                params.put(RIGHT_BOTTOM, "排卵日: " + DateHelper.getMonth(date) + "月" + DateHelper
                        .getDay(date) + "日");
            }
        } else {
            params.put(LEFT_CENTER, "0");
            params.put(RIGHT_BOTTOM, "排卵日: --");
        }
        if (mcSummary != null) {
            params.put(RIGHT_TOP, "月经周期: " + mcSummary.cycle + "天");
            params.put(RIGHT_CENTER, "行经天数: " + mcSummary.duration + "天 ");
        } else {
            params.put(RIGHT_TOP, "月经周期: 0 天");
            params.put(RIGHT_CENTER, "行经天数: 0天 ");
        }
        invalidateView(view.findViewById(R.id.view_mine_header_peroid), params, 1);
    }

    private void invalidateView(View view, Map<String, String> map, int type) {
    }

    private void invalidateWeightProgressView(final TextProgressBar textBar, final float
            progressRate) {
        if (textBar != null && progressRate >= 0.0f && !isRunWeightAnim()) {
            setRunWeightAnim(true);
            textBar.setProgressColor(Color.parseColor("#FF00AEF0"));
            textBar.setTextColor(-1);
            new Thread() {
                float latestWeight = new WeightRecordDao(MineHeadViewFactory.this.mContext)
                        .getLastestWeight();

                public void run() {
                    float tempPro = 0.0f;
                    textBar.setText(String.format("%.1f", new Object[]{Float.valueOf(this
                            .latestWeight)}) + " kg");
                    while (tempPro < progressRate) {
                        tempPro = (float) (((double) tempPro) + 0.01d);
                        textBar.setProgressRate(tempPro);
                        MineHeadViewFactory.this.delay();
                    }
                    MineHeadViewFactory.this.setRunWeightAnim(false);
                }
            }.start();
        }
    }

    private void invalidateEatProgressView(final TextProgressBar textBar, float totalDietCalory) {
        if (textBar != null && totalDietCalory >= 0.0f) {
            setRunEatAnim(true);
            switch (FormulaUtils.calorieType((float) this.user.target_calory, totalDietCalory,
                    this.totalSportCalory)) {
                case -1:
                    textBar.setProgressColor(Color.parseColor("#ffcc00"));
                    textBar.setBgBitmap(((BitmapDrawable) this.resources.getDrawable(R.drawable
                            .md)).getBitmap());
                    break;
                case 0:
                    textBar.setProgressColor(Color.parseColor("#42D639"));
                    textBar.setBgBitmap(((BitmapDrawable) this.resources.getDrawable(R.drawable
                            .mb)).getBitmap());
                    break;
                case 1:
                    textBar.setProgressColor(this.mContext.getResources().getColor(R.color.he));
                    textBar.setBgBitmap(((BitmapDrawable) this.resources.getDrawable(R.drawable
                            .mc)).getBitmap());
                    break;
            }
            float eatProgressRate = totalDietCalory / (((float) this.user.target_calory) +
                    FormulaUtils.computeSport(this.totalSportCalory));
            if (eatProgressRate > 1.0f) {
                eatProgressRate = 1.0f;
            } else if (eatProgressRate < 0.0f) {
                eatProgressRate = 0.0f;
            }
            final float progressRate = eatProgressRate;
            new Thread() {
                public void run() {
                    float tempPro = 0.0f;
                    while (tempPro < progressRate) {
                        tempPro = (float) (((double) tempPro) + 0.01d);
                        textBar.setProgressRate(tempPro);
                        MineHeadViewFactory.this.delay();
                    }
                    MineHeadViewFactory.this.setRunEatAnim(false);
                }
            }.start();
        }
    }

    public void runAnimation(View view) {
        TextProgressBar textBar = (TextProgressBar) view.getTag();
        if (this.user != null) {
            switch (textBar.getId()) {
                case R.id.view_mine_header_eat_textBar:
                    invalidateEatProgressView(this.eatTextProgressBar, this.totalDietCalory);
                    return;
                case R.id.view_mine_header_weight_textBar:
                    float weightProgressRate;
                    float latestWeight = new WeightRecordDao(this.mContext).getLastestWeight();
                    if (this.user.begin_weight - latestWeight < 0.0f) {
                        weightProgressRate = 0.0f;
                    } else if (this.user.begin_weight - this.user.target_weight <= 0.0f) {
                        weightProgressRate = 0.0f;
                    } else {
                        weightProgressRate = Float.valueOf(this.user.begin_weight - latestWeight)
                                .floatValue() / (this.user.begin_weight - this.user.target_weight);
                    }
                    if (weightProgressRate > 1.0f) {
                        weightProgressRate = 1.0f;
                    } else if (weightProgressRate < 0.0f) {
                        weightProgressRate = 0.0f;
                    }
                    invalidateWeightProgressView(this.weightTextProgressBar, weightProgressRate);
                    return;
                default:
                    return;
            }
        }
    }

    private void delay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setRunWeightAnim(boolean isRunWeightAnim) {
        this.isRunWeightAnim = isRunWeightAnim;
    }

    public synchronized boolean isRunWeightAnim() {
        return this.isRunWeightAnim;
    }

    public synchronized boolean isRunEatAnim() {
        return this.isRunEatAnim;
    }

    public synchronized void setRunEatAnim(boolean isRunEatAnim) {
        this.isRunEatAnim = isRunEatAnim;
    }
}
