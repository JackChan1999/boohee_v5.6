package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.status.AttachMent;
import com.boohee.one.R;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.Lesson;

public class LessonFinishActivity extends BaseNoToolbarActivity {
    public static final String IS_FINISH  = "IS_FINISH";
    public static final String KEY_LESSON = "KEY_LESSON";
    private AttachMent attachMent;
    @InjectView(2131427756)
    TextView     btnGoSportPlan;
    @InjectView(2131427757)
    TextView     btnPostStatus;
    @InjectView(2131427752)
    ImageView    btnQuestion;
    @InjectView(2131427753)
    LinearLayout diamondLayout;
    private boolean isFinish;
    private boolean isSpecialTrain;
    @InjectView(2131427745)
    ImageView ivFinish;
    private Lesson lesson;
    @InjectView(2131427748)
    ProgressBar  lessonProgress;
    @InjectView(2131427746)
    LinearLayout progressLayout;
    @InjectView(2131427754)
    TextView     tvDiamond;
    @InjectView(2131427747)
    TextView     tvProgress;
    @InjectView(2131427749)
    TextView     tvTodayCost;
    @InjectView(2131427751)
    TextView     tvTomorrow;
    @InjectView(2131427755)
    TextView     tvUnfinshCount;

    public static void comeOn(Context context, Lesson lesson, boolean isFinish) {
        Intent intent = new Intent(context, LessonFinishActivity.class);
        intent.putExtra(IS_FINISH, isFinish);
        intent.putExtra("KEY_LESSON", lesson);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.bw);
        ButterKnife.inject((Activity) this);
        initData();
        refreshView();
    }

    private void refreshView() {
        if (this.lesson != null) {
            if (this.isFinish) {
                this.btnPostStatus.setVisibility(0);
                this.ivFinish.setBackgroundResource(R.drawable.q9);
                this.tvDiamond.setText(String.valueOf(this.lesson.envious_count));
            } else {
                this.btnPostStatus.setVisibility(8);
                this.diamondLayout.setVisibility(8);
                this.ivFinish.setBackgroundResource(R.drawable.q_);
                this.tvDiamond.setText(String.valueOf(0));
                this.tvUnfinshCount.setText("完成计划才有可能达成目标哦");
                this.lessonProgress.setProgressDrawable(ContextCompat.getDrawable(this.ctx, R
                        .drawable.h1));
                SportApi.postGiveUpLesson(this.ctx, this.lesson.id, null);
            }
            this.tvTodayCost.setText(String.valueOf(this.lesson.today_calorie));
            this.tvTomorrow.setText(String.valueOf(this.lesson.basic_calorie));
            if (this.lesson.user_progress == null) {
                this.isSpecialTrain = true;
                this.progressLayout.setVisibility(8);
                return;
            }
            this.progressLayout.setVisibility(0);
            int progress = (int) ((((float) this.lesson.user_progress.finish_section_count) * 100
            .0f) / ((float) this.lesson.user_progress.total_section_count));
            this.lessonProgress.setProgress(progress);
            this.tvProgress.setText(progress + "%");
        }
    }

    private void initData() {
        if (getIntent() != null) {
            this.lesson = (Lesson) getIntent().getParcelableExtra("KEY_LESSON");
            this.isFinish = getIntent().getBooleanExtra(IS_FINISH, false);
        }
    }

    @OnClick({2131427756, 2131427757, 2131427752})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_question:
                BrowserActivity.comeOnBaby(this.ctx, null, SportApi.URL_QUESTION);
                return;
            case R.id.btn_go_sport_plan:
                finish();
                return;
            case R.id.btn_post_status:
                this.attachMent = new AttachMent();
                String text = "";
                if (this.isSpecialTrain) {
                    text = "已完成" + this.lesson.name + "\n" + String.format("已消耗%d千卡", new
                            Object[]{Integer.valueOf(this.lesson.today_calorie)});
                } else {
                    String str;
                    StringBuilder append = new StringBuilder().append("已完成");
                    if (TextUtils.isEmpty(this.lesson.difficulty)) {
                        str = "";
                    } else {
                        str = this.lesson.difficulty;
                    }
                    text = append.append(str).append("<第").append(this.lesson.level).append
                            ("阶段>").append(this.lesson.name).append("\n").append(String.format
                            ("已消耗%d千卡，已坚持%d天", new Object[]{Integer.valueOf(this.lesson
                                    .today_calorie), Integer.valueOf(this.lesson.user_progress
                                    .continue_days)})).toString();
                }
                this.attachMent.title = text;
                this.attachMent.type = "show";
                this.attachMent.pic = this.lesson.level_pic;
                StatusPostTextActivity.comeWithAttachmentAndExtraText(this.ctx, "#薄荷健身#", this
                        .attachMent);
                finish();
                return;
            default:
                return;
        }
    }
}
