package com.boohee.one.pedometer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.StepsPreference;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.pedometer.manager.AbstractStepManager;
import com.boohee.one.pedometer.manager.StepListener;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.utility.Event;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class StepCounterActivity extends SwipeBackActivity implements StepListener {
    public static int            calory;
    private       ObjectAnimator breathUpAnim;
    private       StepModel      currentStep;
    private       float          fat;
    private       boolean        isError;
    private boolean isFirst = true;
    @InjectView(2131427943)
    ImageView    ivComplete;
    @InjectView(2131427942)
    ImageView    ivPointer;
    @InjectView(2131427941)
    ImageView    ivUmcomplete;
    @InjectView(2131427647)
    LinearLayout llContent;
    private Menu                menu;
    private ValueAnimator       progressAnim;
    private StepRecyclerAdapter recyclerAdapter;
    @InjectView(2131427637)
    RecyclerView   recyclerView;
    @InjectView(2131427940)
    RelativeLayout rlStep;
    private AbstractStepManager stepManager;
    @InjectView(2131427461)
    TextView tvCalory;
    @InjectView(2131427944)
    TextView tvCurrentStep;
    @InjectView(2131427672)
    TextView tvError;
    @InjectView(2131427945)
    TextView tvTargetStep;

    private class BitmapAsync extends AsyncTask<Void, Void, Bitmap> {
        ImageView iv_content;
        TextView  tv_title;
        View      view_share_summary;

        private BitmapAsync() {
        }

        protected void onPreExecute() {
            Helper.showToast((CharSequence) "正在分享，请稍等...");
            this.view_share_summary = LayoutInflater.from(StepCounterActivity.this.activity)
                    .inflate(R.layout.qq, null);
            this.iv_content = (ImageView) this.view_share_summary.findViewById(R.id.iv_content);
            this.tv_title = (TextView) this.view_share_summary.findViewById(R.id.tv_date);
            this.tv_title.setText(DateHelper.today() + "步数记录");
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap bitmap2;
                this.iv_content.setImageBitmap(bitmap);
                Bitmap bitmap_share = BitmapUtil.loadBitmapFromView(this.view_share_summary);
                Context access$500 = StepCounterActivity.this.activity;
                if (bitmap_share == null) {
                    bitmap2 = bitmap;
                } else {
                    bitmap2 = bitmap_share;
                }
                if (!TextUtils.isEmpty(FileUtil.getPNGImagePath(access$500, bitmap2,
                        "SHARE_4_STEP"))) {
                    ShareManager.shareLocalImage(StepCounterActivity.this.activity, filePath);
                }
                if (!(bitmap_share == null || bitmap_share.isRecycled())) {
                    bitmap_share.recycle();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }

        protected Bitmap doInBackground(Void... params) {
            return BitmapUtil.getBitmapByView(StepCounterActivity.this.llContent);
        }
    }

    public static class StepRecyclerAdapter extends Adapter<ViewHolder> {
        Context context;
        List<Fruit> fruits = new ArrayList();

        class Fruit {
            public int    calory;
            public int    icon;
            public String name;
            public String unit;

            public Fruit(String name, String unit, int calory, int icon) {
                this.unit = unit;
                this.name = name;
                this.calory = calory;
                this.icon = icon;
            }
        }

        static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            @InjectView(2131428247)
            ImageView    ivStep;
            @InjectView(2131428565)
            LinearLayout llItem;
            @InjectView(2131427461)
            TextView     tvCalory;
            @InjectView(2131428566)
            TextView     tvFruitCount;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.inject((Object) this, view);
            }
        }

        public StepRecyclerAdapter(Context context) {
            this.context = context;
            this.fruits.add(new Fruit("香蕉", "根", 59, R.drawable.aal));
            this.fruits.add(new Fruit("苹果", "个", 150, R.drawable.aak));
            this.fruits.add(new Fruit("酸奶", "盒", 180, R.drawable.aav));
            this.fruits.add(new Fruit("米饭", "碗", 232, R.drawable.aau));
            this.fruits.add(new Fruit("啤酒", "瓶", 32, R.drawable.aam));
            this.fruits.add(new Fruit("玉米", "根", 203, R.drawable.aap));
            this.fruits.add(new Fruit("花生", "粒", 2, R.drawable.aat));
            this.fruits.add(new Fruit("棒棒糖", "个", 54, R.drawable.aas));
            this.fruits.add(new Fruit("冰淇淋", "个", 35, R.drawable.aar));
            this.fruits.add(new Fruit("胡萝卜", "根", 41, R.drawable.aan));
            this.fruits.add(new Fruit("可口可乐", "罐", 149, R.drawable.aao));
            this.fruits.add(new Fruit("薯条", "小份", 241, R.drawable.aaq));
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.j_,
                    parent, false));
        }

        @TargetApi(16)
        public void onBindViewHolder(ViewHolder holder, int position) {
            Fruit fruit = (Fruit) this.fruits.get(position);
            holder.ivStep.setBackground(ContextCompat.getDrawable(this.context, fruit.icon));
            holder.tvCalory.setText(StepCounterActivity.calory + " 千卡");
            if (StepCounterActivity.calory == 0) {
                holder.tvFruitCount.setText(0 + fruit.unit + fruit.name);
                return;
            }
            float count = ArithmeticUtil.div((float) StepCounterActivity.calory, (float) fruit
                    .calory, 1);
            if (((double) count) < 0.1d) {
                holder.tvFruitCount.setText(0 + fruit.unit + fruit.name);
            } else {
                holder.tvFruitCount.setText(count + fruit.unit + fruit.name);
            }
        }

        public int getItemCount() {
            return this.fruits.size();
        }
    }

    @OnClick({2131427940})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_step:
                MobclickAgent.onEvent(this.ctx, Event.STEPS_DETAIL);
                startActivity(new Intent(this.ctx, StepHistoryActivity.class));
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dc);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        init();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StepCounterActivity.this.setPermission();
            }
        }, 500);
    }

    public void onEventMainThread(BandTypeEvent event) {
        this.stepManager = StepManagerFactory.getInstance().changeStepManager(this, event, this
                .stepManager);
        this.stepManager.setListener(this);
        this.stepManager.getCurrentStepAsyncs();
    }

    private void setPermission() {
        if (this.stepManager.isPedometer() && StepsPreference.isStepOpen() && !StepCounterUtil
                .checkNotificationPermission(this.ctx)) {
            LightAlertDialog dialog = LightAlertDialog.create(this.ctx, (int) R.string.a74, (int)
                    R.string.a73);
            dialog.setCancelable(false);
            dialog.setPositiveButton((CharSequence) "去设置", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    StepCounterUtil.goNLPermission(StepCounterActivity.this.ctx);
                    Helper.showToast((int) R.string.a75);
                    StepsPreference.putIsFirst(true);
                }
            });
            dialog.setNegativeButton((CharSequence) "取消", null);
            dialog.show();
        }
    }

    private void initView() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.ctx, 0, false));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerAdapter = new StepRecyclerAdapter(this.ctx);
        this.recyclerView.setAdapter(this.recyclerAdapter);
    }

    private void init() {
        this.stepManager = StepManagerFactory.getInstance().createStepManager(this);
        this.stepManager.setListener(this);
        initView();
        this.stepManager.getCurrentStepAsyncs();
    }

    public void onGetCurrentStep(StepModel stepModel, boolean Error) {
        if (stepModel != null) {
            this.currentStep = stepModel;
            if (this.isFirst) {
                calory = StepCounterUtil.getCalory(this.currentStep.step);
                startAnim();
            }
            this.isError = Error;
            refreshView();
        }
    }

    private void refreshView() {
        if (this.isError) {
            this.tvError.setVisibility(0);
            return;
        }
        this.tvError.setVisibility(8);
        this.tvCurrentStep.setText(this.currentStep.step + "");
        calory = StepCounterUtil.getCalory(this.currentStep.step);
        this.fat = StepCounterUtil.getFat(calory);
        this.tvCalory.setText(String.format(getString(R.string.a6w), new Object[]{Integer.valueOf
                (calory), Float.valueOf(this.fat)}));
        this.recyclerAdapter.notifyDataSetChanged();
    }

    protected void onResume() {
        super.onResume();
        if (this.currentStep != null) {
            Log.d("build", VERSION.SDK_INT + "");
            this.tvTargetStep.setText("目标 " + StepsPreference.getStepsTarget());
            if (!this.isFirst) {
                startAnim();
            }
            this.isFirst = false;
        }
    }

    private void startAnim() {
        this.progressAnim = ValueAnimator.ofInt(new int[]{0, this.currentStep.step});
        this.progressAnim.setDuration(1500);
        this.progressAnim.setInterpolator(new LinearInterpolator());
        this.progressAnim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                StepCounterActivity.this.ivPointer.setRotation((float) ((((Integer) animation
                        .getAnimatedValue()).intValue() * 360) / StepsPreference.getStepsTarget()));
            }
        });
        this.progressAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                StepCounterActivity.this.isComplete();
            }
        });
        this.progressAnim.start();
    }

    private void isComplete() {
        if (this.currentStep.step > StepsPreference.getStepsTarget()) {
            this.ivPointer.setVisibility(8);
            this.ivComplete.setVisibility(0);
            this.ivUmcomplete.setVisibility(8);
            startCompleteBreath();
            return;
        }
        this.ivPointer.setVisibility(0);
        this.ivUmcomplete.setVisibility(0);
        this.ivComplete.setVisibility(8);
    }

    private void startCompleteBreath() {
        this.breathUpAnim = ObjectAnimator.ofFloat(this.ivComplete, "alpha", new float[]{1.0f, 0
                .2f});
        this.breathUpAnim.setDuration(2000);
        this.breathUpAnim.setRepeatCount(-1);
        this.breathUpAnim.setInterpolator(new DecelerateInterpolator());
        this.breathUpAnim.setRepeatMode(2);
        this.breathUpAnim.start();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.u, menu);
        this.menu = menu;
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        prepareMenu();
        return true;
    }

    private void prepareMenu() {
        if (this.menu != null && this.menu.size() > 0) {
            this.menu.findItem(R.id.action_step_set).setVisible(this.stepManager.isPedometer());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_share_step:
                MobclickAgent.onEvent(this.ctx, Event.STEPS_SHARE);
                share();
                return true;
            case R.id.action_step_set:
                startActivity(new Intent(this.ctx, StepSettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        MobclickAgent.onEvent(this.activity, Event.RECORD_WEIGHT_STATUS_SHARE);
        new BitmapAsync().execute(new Void[0]);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.stepManager.onDestroy();
    }
}
