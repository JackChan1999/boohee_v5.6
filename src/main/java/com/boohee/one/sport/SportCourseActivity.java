package com.boohee.one.sport;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sport.model.CourseInfo;
import com.boohee.one.sport.model.CourseItemInfo;
import com.boohee.one.sport.model.FinishedCourseDay;
import com.boohee.one.sport.view.SportProgress;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SportCourseActivity extends GestureActivity {
    private static final String  KEY_COURSE_ID = "key_course_id";
    private              boolean isFirst       = true;
    @InjectView(2131427881)
    ImageView iv_header_bg;
    private SportFragmentAdapter mAdapter;
    private FileCache            mCache;
    private int                  mCourseID;
    private List<CourseInfo> mDataList = new ArrayList();
    private String mIntroduceURL;
    private int    mTabIndex;
    @InjectView(2131427888)
    TabLayout     tablayout;
    @InjectView(2131427885)
    TextView      tv_course_count;
    @InjectView(2131427884)
    TextView      tv_course_finished;
    @InjectView(2131427886)
    SportProgress viewCourseProgress;
    @InjectView(2131427887)
    View          view_course_finish;
    @InjectView(2131427463)
    ViewPager     viewpager;

    private class SportFragmentAdapter extends FragmentPagerAdapter {
        private List<CourseInfo> mCourseInfoList;
        private List<SportCourseFragment> mFragmentList = new ArrayList();

        public SportFragmentAdapter(FragmentManager fm, List<CourseInfo> courseInfoList) {
            super(fm);
            this.mCourseInfoList = courseInfoList;
            for (CourseInfo course : this.mCourseInfoList) {
                this.mFragmentList.add(SportCourseFragment.newInstance(course.items));
            }
        }

        public void notifyDataChange(List<CourseInfo> courseInfoList) {
            if (courseInfoList.size() == this.mFragmentList.size()) {
                for (int i = 0; i < courseInfoList.size(); i++) {
                    ((SportCourseFragment) this.mFragmentList.get(i)).notifyDataChange((
                            (CourseInfo) courseInfoList.get(i)).items);
                }
            }
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mCourseInfoList.size();
        }

        public CharSequence getPageTitle(int position) {
            return ((CourseInfo) this.mCourseInfoList.get(position)).tab;
        }
    }

    @OnClick({2131427882, 2131427887})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_course_record:
                MobclickAgent.onEvent(this.activity, Event.BINGO_VIEWCOURSESTAT);
                SportStatisticsActivity.comeOnBaby(this, this.mCourseID, false);
                return;
            case R.id.view_course_finish:
                SportCourseChangeActivity.comeOnBaby(this);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d3);
        ButterKnife.inject((Activity) this);
        this.mCache = FileCache.get((Context) this);
        this.mCourseID = getIntent().getIntExtra(KEY_COURSE_ID, 0);
    }

    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void initView() {
        if (this.mAdapter == null) {
            this.mAdapter = new SportFragmentAdapter(getSupportFragmentManager(), this.mDataList);
        }
        this.viewpager.setAdapter(this.mAdapter);
        this.tablayout.setupWithViewPager(this.viewpager);
        this.tablayout.setTabMode(0);
        this.mAdapter.notifyDataChange(this.mDataList);
        this.viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                SportCourseActivity.this.mTabIndex = position;
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        this.viewpager.setCurrentItem(this.mTabIndex);
    }

    private void requestData() {
        if (this.isFirst) {
            showLoading();
        }
        SportV3Api.requestCourse(this.mCourseID, new JsonCallback(this) {
            public void ok(JSONObject object) {
                if (!object.has("error")) {
                    SportCourseActivity.this.mCache.put(CacheKey.SPORT_COURSE_V3, object);
                    SportCourseActivity.this.handleData(object);
                } else if (SportCourseActivity.this.isFirst) {
                    if (object.optInt("error") == 0) {
                        SportCourseActivity.this.changeCourseDialog();
                    }
                    Helper.showToast(object.optString("message"));
                }
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
                SportCourseActivity.this.handleData(SportCourseActivity.this.mCache
                        .getAsJSONObject(CacheKey.SPORT_COURSE_V3));
            }

            public void onFinish() {
                if (SportCourseActivity.this.isFirst) {
                    SportCourseActivity.this.dismissLoading();
                }
                SportCourseActivity.this.isFirst = false;
            }
        }, this);
    }

    private void changeCourseDialog() {
        LightAlertDialog dialog = LightAlertDialog.create(this.ctx, (int) R.string.abu)
                .setNegativeButton((int) R.string.abt, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportCourseActivity.this.finish();
            }
        }).setPositiveButton((int) R.string.acb, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SportCourseChangeActivity.comeOnBaby(SportCourseActivity.this);
                SportCourseActivity.this.finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void handleData(JSONObject object) {
        if (object != null) {
            this.mIntroduceURL = object.optString("link_url");
            this.mCourseID = object.optInt("sports_course_id");
            setTitle(object.optString("name"));
            JSONObject data = object.optJSONObject("sports_days");
            int total = data.optInt("total");
            int finished = data.optInt("finished");
            this.tv_course_count.setText(String.valueOf(total));
            this.tv_course_finished.setText(String.valueOf(finished));
            this.view_course_finish.setVisibility(total == finished ? 0 : 8);
            this.viewCourseProgress.setMax(total);
            this.viewCourseProgress.setProgress(finished);
            this.imageLoader.displayImage(data.optString("pic_url"), this.iv_header_bg,
                    ImageLoaderOptions.randomColor());
            List<CourseInfo> courseInfos = FastJsonUtils.parseList(data.optString("sports_days"),
                    CourseInfo.class);
            if (courseInfos != null && courseInfos.size() != 0) {
                this.mDataList.clear();
                this.mDataList.addAll(courseInfos);
                int todayID = data.optInt("today_id");
                int index = 1;
                List<FinishedCourseDay> finishedDayList = FastJsonUtils.parseList(data.optString
                        ("finish_days"), FinishedCourseDay.class);
                for (CourseInfo course : this.mDataList) {
                    for (CourseItemInfo item : course.items) {
                        int index2 = index + 1;
                        item.index = index;
                        item.download = DownloadHelper.getInstance().hasDownload(item.video_url);
                        if (item.id == todayID) {
                            item.today = true;
                            if (this.mTabIndex == 0) {
                                this.mTabIndex = this.mDataList.indexOf(course);
                            }
                        } else {
                            item.today = false;
                        }
                        if (finishedDayList != null && finishedDayList.size() > 0) {
                            for (FinishedCourseDay day : finishedDayList) {
                                if (item.id == day.id) {
                                    item.date = day.date;
                                }
                            }
                        }
                        index = index2;
                    }
                }
                initView();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.r, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_download_manager:
                DownloadManageActivity.startActivity(this);
                return true;
            case R.id.action_course_history:
                MobclickAgent.onEvent(this.activity, Event.BINGO_VIEWCOURSEHISTORY);
                SportCourseHistoryActivity.comeOnBaby(this);
                return true;
            case R.id.action_change_course:
                MobclickAgent.onEvent(this.activity, Event.BINGO_CLICKCHANGECOURSE);
                SportCourseChangeActivity.comeOnBaby(this);
                return true;
            case R.id.action_change_reset:
                resetCourseDialog();
                return true;
            case R.id.action_course_introduce:
                MobclickAgent.onEvent(this.activity, Event.BINGO_VIEWCOURSEDOC);
                if (TextUtils.isEmpty(this.mIntroduceURL)) {
                    return true;
                }
                BrowserActivity.comeOnBaby(this, "", this.mIntroduceURL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetCourseDialog() {
        LightAlertDialog.create(this.ctx, (int) R.string.abz).setNegativeButton((int) R.string
                .abt, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton((int) R.string.acb, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MobclickAgent.onEvent(SportCourseActivity.this.activity, Event
                        .BINGO_CLICKRESETCOURSE);
                SportCourseActivity.this.resetCourse(SportCourseActivity.this.mCourseID);
            }
        }).show();
    }

    private void resetCourse(int courseId) {
        showLoading();
        SportV3Api.resetCourse(courseId, new JsonCallback(this) {
            public void ok(JSONObject object) {
                SportCourseActivity.this.requestData();
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                SportCourseActivity.this.dismissLoading();
            }
        }, this);
    }

    public static void comeOnBaby(Context context) {
        comeOnBaby(context, 0);
    }

    public static void comeOnBaby(Context context, int courseID) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SportCourseActivity.class);
            intent.addFlags(67108864);
            intent.putExtra(KEY_COURSE_ID, courseID);
            context.startActivity(intent);
        }
    }
}
