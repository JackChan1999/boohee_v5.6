package com.boohee.one.sport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sport.model.CourseRecord;
import com.boohee.one.sport.model.CourseRecordItem;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.RcvAdapterWrapper;
import kale.adapter.item.AdapterItem;

import org.json.JSONObject;

public class SportStatisticsActivity extends GestureActivity {
    private static final String             KEY_COURSE_ID  = "key_course_id";
    private static final String             KEY_IS_HISTORY = "key_is_history";
    private static final int                ROW_COUNT      = 7;
    private static final List<CourseRecord> mDataList      = new ArrayList();
    private boolean isHistory;
    @InjectView(2131428431)
    ImageView ivBg;
    @InjectView(2131427745)
    ImageView ivFinish;
    private CommonRcvAdapter<CourseRecord> mAdapter = new CommonRcvAdapter<CourseRecord>
            (mDataList) {
        @NonNull
        public AdapterItem createItem(Object o) {
            return new CourseRecordItem();
        }
    };
    RecyclerView mRecyclerView;
    LinearLayout mViewContent;
    private RcvAdapterWrapper mWrapper;
    @InjectView(2131428918)
    TextView tvCourseProgress;
    @InjectView(2131428919)
    TextView tvDuration;
    @InjectView(2131428920)
    TextView tvKcal;
    @InjectView(2131428917)
    TextView tvPeriod;

    class BitmapAsync extends AsyncTask<Void, Void, Bitmap> {
        ImageView iv_content;
        View      view_share_summary;

        BitmapAsync() {
        }

        protected void onPreExecute() {
            Helper.showToast((CharSequence) "正在分享，请稍等...");
            this.view_share_summary = LayoutInflater.from(SportStatisticsActivity.this.activity)
                    .inflate(R.layout.qq, null);
            this.view_share_summary.findViewById(R.id.tv_date).setVisibility(8);
            this.iv_content = (ImageView) this.view_share_summary.findViewById(R.id.iv_content);
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap bitmap2;
                this.iv_content.setImageBitmap(bitmap);
                Bitmap bitmap_share = BitmapUtil.loadBitmapFromView(this.view_share_summary);
                Context access$400 = SportStatisticsActivity.this.activity;
                if (bitmap_share == null) {
                    bitmap2 = bitmap;
                } else {
                    bitmap2 = bitmap_share;
                }
                if (!TextUtils.isEmpty(FileUtil.getPNGImagePath(access$400, bitmap2,
                        "SHARE_4_LINECHART"))) {
                    ShareManager.shareLocalImage(SportStatisticsActivity.this.activity, filePath);
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
            return BitmapUtil.getBitmapByView(SportStatisticsActivity.this.mViewContent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_);
        this.mViewContent = (LinearLayout) findViewById(R.id.view_content);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        int courseID = getIntent().getIntExtra(KEY_COURSE_ID, 0);
        this.isHistory = getIntent().getBooleanExtra(KEY_IS_HISTORY, false);
        initView();
        requestData(courseID);
    }

    private void initView() {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        this.mWrapper = new RcvAdapterWrapper(this.mAdapter, this.mRecyclerView.getLayoutManager());
        View header = LayoutInflater.from(this).inflate(R.layout.mr, null);
        ButterKnife.inject((Object) this, header);
        this.mWrapper.setHeaderView(header);
        this.mRecyclerView.setAdapter(this.mWrapper);
    }

    private void requestData(int courseId) {
        showLoading();
        SportV3Api.requestCourseRecord(this.isHistory, courseId, new JsonCallback(this) {
            public void ok(JSONObject object) {
                SportStatisticsActivity.this.imageLoader.displayImage(object.optString("pic_url")
                        , SportStatisticsActivity.this.ivBg, ImageLoaderOptions.randomColor());
                String startDate = object.optString("start_date");
                String endDate = object.optString("end_date");
                if (!(TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate))) {
                    startDate = DateFormatUtils.string2String(startDate, "yyyy/MM/dd");
                    endDate = DateFormatUtils.string2String(endDate, "yyyy/MM/dd");
                    SportStatisticsActivity.this.tvPeriod.setText(String.format("%s ~ %s", new
                            Object[]{startDate, endDate}));
                }
                int total = object.optInt("total");
                int finish = object.optInt("finished");
                int duration = object.optInt(SportRecordDao.DURATION);
                int calory = object.optInt("calory");
                SportStatisticsActivity.this.tvCourseProgress.setText(String.format("%d/%d", new
                        Object[]{Integer.valueOf(finish), Integer.valueOf(total)}));
                SportStatisticsActivity.this.tvDuration.setText(String.valueOf(duration));
                SportStatisticsActivity.this.tvKcal.setText(String.valueOf(calory));
                SportStatisticsActivity.this.ivFinish.setVisibility(total == finish ? 0 : 8);
                SportStatisticsActivity.mDataList.clear();
                for (int i = 0; i < total; i++) {
                    CourseRecord record = new CourseRecord();
                    record.no = i;
                    SportStatisticsActivity.mDataList.add(i, record);
                }
                try {
                    List<CourseRecord> recordList = FastJsonUtils.parseList(object.optString
                            ("records"), CourseRecord.class);
                    if (recordList != null && recordList.size() > 0) {
                        for (CourseRecord record2 : recordList) {
                            ((CourseRecord) SportStatisticsActivity.mDataList.get(record2.no - 1)
                            ).date = record2.date;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SportStatisticsActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                SportStatisticsActivity.this.dismissLoading();
            }
        }, this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.t, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        MobclickAgent.onEvent(this.activity, Event.BINGO_SHARECOURSESTAT);
        new BitmapAsync().execute(new Void[0]);
    }

    public static void comeOnBaby(Context context, int courseID, boolean isHistory) {
        if (context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SportStatisticsActivity.class);
            intent.putExtra(KEY_COURSE_ID, courseID);
            intent.putExtra(KEY_IS_HISTORY, isHistory);
            context.startActivity(intent);
        }
    }
}
