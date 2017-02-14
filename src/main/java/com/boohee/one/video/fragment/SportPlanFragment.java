package com.boohee.one.video.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.one.video.adapter.CourseRecyclerAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.DailyCourse;
import com.boohee.one.video.entity.SportPlanCourse;
import com.boohee.one.video.ui.NewLessonDetailActivity;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.LightAlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class SportPlanFragment extends BaseFragment {
    public static final String COURSE_STATUS_DOING         = "doing";
    public static final String COURSE_STATUS_FINISH        = "finish";
    public static final String COURSE_STATUS_NORMAL        = "normal";
    public static final String REFRESH_SPORT_PLAN_FRAGMENT = "REFRESH_SPORT_PLAN_FRAGMENT";
    String alert = null;
    private List<SportPlanCourse> courseList = new ArrayList();
    private FileCache fileCache;
    @InjectView(2131428230)
    LinearLayout layoutContainer;
    private LayoutInflater layoutInflater;
    @InjectView(2131427340)
    PullToRefreshScrollView scrollview;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.go, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.layoutInflater = LayoutInflater.from(getActivity());
        this.fileCache = FileCache.get(getActivity());
        initView();
        loadData();
    }

    private void initView() {
        this.scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                SportPlanFragment.this.loadData();
            }
        });
    }

    private void loadData() {
        showLoading();
        SportApi.getSportPlan(getActivity(), new JsonCallback(getActivity()) {
            public void onFinish() {
                super.onFinish();
                SportPlanFragment.this.dismissLoading();
                SportPlanFragment.this.scrollview.onRefreshComplete();
            }

            public void fail(String message) {
                super.fail(message);
                SportPlanFragment.this.handleData(SportPlanFragment.this.fileCache
                        .getAsJSONObject("sport_lesson"));
            }

            public void ok(JSONObject object) {
                super.ok(object);
                SportPlanFragment.this.fileCache.put("sport_lesson", object);
                SportPlanFragment.this.handleData(object);
            }
        });
    }

    private void handleData(JSONObject object) {
        try {
            this.courseList.clear();
            this.courseList.addAll(FastJsonUtils.parseList(object.optString("courses"),
                    SportPlanCourse.class));
            refreshView();
            this.alert = object.optString("alert");
            showResetAlert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResetAlert() {
        if (!TextUtils.isEmpty(this.alert)) {
            final LightAlertDialog dialog = LightAlertDialog.create(getActivity());
            dialog.setMessage(this.alert);
            dialog.setPositiveButton((CharSequence) "好的，我不要半途而废", new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SportPlanFragment.this.loadData();
                    EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void refreshView() {
        this.layoutContainer.removeAllViews();
        for (int i = 0; i < this.courseList.size(); i++) {
            View view = null;
            SportPlanCourse course = (SportPlanCourse) this.courseList.get(i);
            TextView tvLevel;
            RecyclerView recyclerView;
            if (TextUtils.equals(course.level_status, COURSE_STATUS_FINISH)) {
                view = this.layoutInflater.inflate(R.layout.hq, this.layoutContainer, false);
                tvLevel = (TextView) view.findViewById(R.id.tv_level);
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                setTvLevel(tvLevel, i);
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pf, 0);
                initRecyclerView(recyclerView, course);
            } else if (TextUtils.equals(course.level_status, COURSE_STATUS_DOING)) {
                view = this.layoutInflater.inflate(R.layout.hp, this.layoutContainer, false);
                tvLevel = (TextView) view.findViewById(R.id.tv_level);
                ImageView ivBg = (ImageView) view.findViewById(R.id.iv_bg);
                ImageView ivStatus = (ImageView) view.findViewById(R.id.iv_status);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                TextView tvDes = (TextView) view.findViewById(R.id.tv_des);
                TextView tvBtn = (TextView) view.findViewById(R.id.tv_btn);
                TextView tvStory = (TextView) view.findViewById(R.id.tv_story);
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                ViewUtils.setViewScaleHeight(getActivity(), ivBg, 2, 1);
                initRecyclerView(recyclerView, course);
                setTvLevel(tvLevel, i);
                if (course.dailys != null && course.dailys.size() != 0) {
                    String date = DateHelper.format(new Date());
                    for (int index = 0; index < course.dailys.size(); index++) {
                        final DailyCourse dailyCourse = (DailyCourse) course.dailys.get(index);
                        if (TextUtils.equals(date, dailyCourse.date)) {
                            this.imageLoader.displayImage(dailyCourse.pic_url, ivBg);
                            if (TextUtils.equals(dailyCourse.status, "complete")) {
                                ivStatus.setVisibility(0);
                                tvBtn.setVisibility(8);
                                tvStory.setVisibility(8);
                                tvTitle.setText("已完成");
                                tvDes.setText(dailyCourse.name);
                                ivBg.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        if (!WheelUtils.isFastDoubleClick() && !SportPlanFragment
                                                .this.isDetached()) {
                                            if (dailyCourse.section_id > 0) {
                                                NewLessonDetailActivity.comeOn(SportPlanFragment
                                                        .this.getActivity(), dailyCourse
                                                        .section_id);
                                            } else {
                                                BrowserActivity.comeOnBaby(SportPlanFragment.this
                                                        .getActivity(), null, dailyCourse
                                                        .rest_link);
                                            }
                                        }
                                    }
                                });
                            } else if (TextUtils.equals(dailyCourse.status, "rest")) {
                                ivStatus.setVisibility(8);
                                tvBtn.setVisibility(8);
                                tvStory.setVisibility(0);
                                tvTitle.setText("今天休息一天");
                                tvDes.setText("明天将开启第二阶段的训练");
                                ivBg.setImageResource(R.drawable.qc);
                                ivBg.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        if (!WheelUtils.isFastDoubleClick() && !SportPlanFragment
                                                .this.isDetached()) {
                                            BrowserActivity.comeOnBaby(SportPlanFragment.this
                                                    .getActivity(), null, dailyCourse.rest_link);
                                        }
                                    }
                                });
                            } else if (TextUtils.equals(dailyCourse.status, "pre")) {
                                ivStatus.setVisibility(8);
                                tvBtn.setVisibility(0);
                                tvStory.setVisibility(8);
                                tvTitle.setText(dailyCourse.name);
                                ivBg.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        if (!WheelUtils.isFastDoubleClick() && !SportPlanFragment
                                                .this.isRemoved()) {
                                            NewLessonDetailActivity.comeOn(SportPlanFragment.this
                                                    .getActivity(), dailyCourse.section_id);
                                        }
                                    }
                                });
                            }
                            recyclerView.scrollToPosition(index);
                        }
                    }
                } else {
                    return;
                }
            } else if (TextUtils.equals(course.level_status, "normal")) {
                view = this.layoutInflater.inflate(R.layout.hr, this.layoutContainer, false);
                ImageView ivLocked = (ImageView) view.findViewById(R.id.iv_locked);
                setTvLevel((TextView) view.findViewById(R.id.tv_level), i);
                if (i == 1) {
                    ivLocked.setImageResource(R.drawable.n3);
                } else if (i == 2) {
                    ivLocked.setImageResource(R.drawable.n4);
                }
            }
            if (view != null) {
                this.layoutContainer.addView(view);
            }
        }
        try {
            course = (SportPlanCourse) this.courseList.get(this.courseList.size() - 1);
            if (TextUtils.equals(course.level_status, COURSE_STATUS_FINISH) && ((DailyCourse)
                    course.dailys.get(course.dailys.size() - 1)).status.equalsIgnoreCase
                    ("complete")) {
                final LightAlertDialog dialog = LightAlertDialog.create(getActivity(), "恭喜你完成训练");
                dialog.setMessage("如果你对这轮训练感到困难，请点击重新训练;如果感到轻松，请点击下一难度训练");
                dialog.setNegativeButton((CharSequence) "重新训练", (OnClickListener) new
                        OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SportApi.postLessonAgain(SportPlanFragment.this.getActivity(), new
                                JsonCallback(SportPlanFragment.this.getActivity()) {
                            public void ok(JSONObject object) {
                                super.ok(object);
                                SportPlanFragment.this.loadData();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.setPositiveButton((CharSequence) "下一难度训练", (OnClickListener) new
                        OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SportApi.postLessonNext(SportPlanFragment.this.getActivity(), new
                                JsonCallback(SportPlanFragment.this.getActivity()) {
                            public void ok(JSONObject object) {
                                super.ok(object);
                                SportPlanFragment.this.loadData();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTvLevel(TextView tvLevel, int i) {
        if (i == 0) {
            tvLevel.setText("第一阶段");
        } else if (i == 1) {
            tvLevel.setText("第二阶段");
        } else if (i == 2) {
            tvLevel.setText("第三阶段");
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, SportPlanCourse course) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setAdapter(new CourseRecyclerAdapter(getActivity(), course.dailys));
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
