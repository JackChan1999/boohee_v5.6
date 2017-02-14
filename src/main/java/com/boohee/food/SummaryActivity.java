package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordPhoto;
import com.boohee.model.RecordSport;
import com.boohee.model.VideoSportRecord;
import com.boohee.one.R;
import com.boohee.utility.Event;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.FileUtil;
import com.boohee.utils.FoodUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class SummaryActivity extends GestureActivity {
    public static final  String KEY_BREAKFAST              = "KEY_BREAKFAST";
    public static final  String KEY_BREAKFAST_PHOTO        = "KEY_BREAKFAST_PHOTO";
    private static final String KEY_DATE                   = "KEY_DATE";
    public static final  String KEY_DINNER                 = "KEY_DINNER";
    public static final  String KEY_DINNER_PHOTO           = "KEY_DINNER_PHOTO";
    public static final  String KEY_LUNCH                  = "KEY_LUNCH";
    public static final  String KEY_LUNCH_PHOTO            = "KEY_LUNCH_PHOTO";
    public static final  String KEY_SNACKS_BREAKFAST       = "KEY_SNACKS_BREAKFAST";
    public static final  String KEY_SNACKS_BREAKFAST_PHOTO = "KEY_SNACKS_BREAKFAST_PHOTO";
    public static final  String KEY_SNACKS_DINNER          = "KEY_SNACKS_DINNER";
    public static final  String KEY_SNACKS_DINNER_PHOTO    = "KEY_SNACKS_DINNER_PHOTO";
    public static final  String KEY_SNACKS_LUNCH           = "KEY_SNACKS_LUNCH";
    public static final  String KEY_SNACKS_LUNCH_PHOTO     = "KEY_SNACKS_LUNCH_PHOTO";
    public static final  String KEY_SPORT                  = "KEY_SPORT";
    public static final  String KEY_VIDEO_SPORT            = "KEY_VIDEO_SPORT";
    public static final  int    TIME_TYPE_BREAKFAST        = 1;
    public static final  int    TIME_TYPE_DINNER           = 3;
    public static final  int    TIME_TYPE_LUNCH            = 2;
    public static final  int    TIME_TYPE_SNACKS_BREAKFAST = 6;
    public static final  int    TIME_TYPE_SNACKS_DINNER    = 8;
    public static final  int    TIME_TYPE_SNACKS_LUNCH     = 7;
    private ArrayList<RecordFood>  breakfastList;
    private ArrayList<RecordPhoto> breakfastPhotoList;
    private ArrayList<RecordFood>  dinnerList;
    private ArrayList<RecordPhoto> dinnerPhotoList;
    @InjectView(2131427575)
    LinearLayout ll_card_breakfast;
    @InjectView(2131427579)
    LinearLayout ll_card_dinner;
    @InjectView(2131427577)
    LinearLayout ll_card_lunch;
    @InjectView(2131427576)
    LinearLayout ll_card_snacks_breakfast;
    @InjectView(2131427580)
    LinearLayout ll_card_snacks_dinner;
    @InjectView(2131427578)
    LinearLayout ll_card_snacks_lunch;
    @InjectView(2131427581)
    LinearLayout ll_card_sport;
    private ArrayList<RecordFood>  lunchList;
    private ArrayList<RecordPhoto> lunchPhotoList;
    private String                 record_on;
    private ArrayList<RecordFood>  snacksBreakfastList;
    private ArrayList<RecordPhoto> snacksBreakfastPhotoList;
    private ArrayList<RecordFood>  snacksDinnerList;
    private ArrayList<RecordPhoto> snacksDinnerPhotoList;
    private ArrayList<RecordFood>  snacksLunchList;
    private ArrayList<RecordPhoto> snacksLunchPhotoList;
    private ArrayList<RecordSport> sportList;
    @InjectView(2131427955)
    ScrollView sv_main;
    private float totalDietCalory;
    private float totalSportCalory;
    @InjectView(2131427614)
    TextView tv_date;
    @InjectView(2131429436)
    TextView tv_diet_calory;
    @InjectView(2131429440)
    TextView tv_sport_calory;
    private ArrayList<VideoSportRecord> videoSportRecordsList;

    public static void start(Context context, String record_on, ArrayList<RecordFood>
            breakfastList, ArrayList<RecordFood> lunchList, ArrayList<RecordFood> dinnerList,
                             ArrayList<RecordFood> snacksBreakfastList, ArrayList<RecordFood>
                                     snacksLunchList, ArrayList<RecordFood> snacksDinnerList,
                             ArrayList<RecordSport> sportList, ArrayList<RecordPhoto>
                                     breakfastPhotoList, ArrayList<RecordPhoto> lunchPhotoList,
                             ArrayList<RecordPhoto> dinnerPhotoList, ArrayList<RecordPhoto>
                                     snacksBreakfastPhotoList, ArrayList<RecordPhoto>
                                     snacksLunchPhotoList, ArrayList<RecordPhoto>
                                     snacksDinnerPhotoList, ArrayList<VideoSportRecord>
                                     videoSportRecords) {
        Intent starter = new Intent(context, SummaryActivity.class);
        starter.putExtra(KEY_DATE, record_on);
        starter.putExtra(KEY_BREAKFAST, breakfastList);
        starter.putExtra(KEY_LUNCH, lunchList);
        starter.putExtra(KEY_DINNER, dinnerList);
        starter.putExtra(KEY_SNACKS_BREAKFAST, snacksBreakfastList);
        starter.putExtra(KEY_SNACKS_LUNCH, snacksLunchList);
        starter.putExtra(KEY_SNACKS_DINNER, snacksDinnerList);
        starter.putExtra(KEY_SPORT, sportList);
        starter.putExtra(KEY_BREAKFAST_PHOTO, breakfastPhotoList);
        starter.putExtra(KEY_LUNCH_PHOTO, lunchPhotoList);
        starter.putExtra(KEY_DINNER_PHOTO, dinnerPhotoList);
        starter.putExtra(KEY_SNACKS_BREAKFAST_PHOTO, snacksBreakfastPhotoList);
        starter.putExtra(KEY_SNACKS_LUNCH_PHOTO, snacksLunchPhotoList);
        starter.putExtra(KEY_SNACKS_DINNER_PHOTO, snacksDinnerPhotoList);
        starter.putExtra(KEY_VIDEO_SPORT, videoSportRecords);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dk);
        ButterKnife.inject((Activity) this);
        handleIntent();
        this.tv_date.setText(this.record_on);
        initDietCart(1, this.breakfastList, this.breakfastPhotoList, this.ll_card_breakfast);
        initDietCart(2, this.lunchList, this.lunchPhotoList, this.ll_card_lunch);
        initDietCart(3, this.dinnerList, this.dinnerPhotoList, this.ll_card_dinner);
        initDietCart(6, this.snacksBreakfastList, this.snacksBreakfastPhotoList, this
                .ll_card_snacks_breakfast);
        initDietCart(7, this.snacksLunchList, this.snacksLunchPhotoList, this.ll_card_snacks_lunch);
        initDietCart(8, this.snacksDinnerList, this.snacksDinnerPhotoList, this
                .ll_card_snacks_dinner);
        initSportCart(this.sportList, this.ll_card_sport);
        this.tv_diet_calory.setText(Math.round(this.totalDietCalory) + "");
        this.tv_sport_calory.setText(Math.round(this.totalSportCalory) + "");
    }

    private void handleIntent() {
        this.record_on = getStringExtra(KEY_DATE);
        this.breakfastList = getIntent().getParcelableArrayListExtra(KEY_BREAKFAST);
        this.lunchList = getIntent().getParcelableArrayListExtra(KEY_LUNCH);
        this.dinnerList = getIntent().getParcelableArrayListExtra(KEY_DINNER);
        this.snacksBreakfastList = getIntent().getParcelableArrayListExtra(KEY_SNACKS_BREAKFAST);
        this.snacksLunchList = getIntent().getParcelableArrayListExtra(KEY_SNACKS_LUNCH);
        this.snacksDinnerList = getIntent().getParcelableArrayListExtra(KEY_SNACKS_DINNER);
        this.sportList = getIntent().getParcelableArrayListExtra(KEY_SPORT);
        this.breakfastPhotoList = getIntent().getParcelableArrayListExtra(KEY_BREAKFAST_PHOTO);
        this.lunchPhotoList = getIntent().getParcelableArrayListExtra(KEY_LUNCH_PHOTO);
        this.dinnerPhotoList = getIntent().getParcelableArrayListExtra(KEY_DINNER_PHOTO);
        this.snacksBreakfastPhotoList = getIntent().getParcelableArrayListExtra
                (KEY_SNACKS_BREAKFAST_PHOTO);
        this.snacksLunchPhotoList = getIntent().getParcelableArrayListExtra(KEY_SNACKS_LUNCH_PHOTO);
        this.snacksDinnerPhotoList = getIntent().getParcelableArrayListExtra
                (KEY_SNACKS_DINNER_PHOTO);
        this.videoSportRecordsList = getIntent().getParcelableArrayListExtra(KEY_VIDEO_SPORT);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.y, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                MobclickAgent.onEvent(this.activity, Event.TOOL_FOODANDSPORT_ABSTRACTSHARE);
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        Bitmap bitmap;
        View view_share_summary = LayoutInflater.from(this).inflate(R.layout.qq, null);
        ImageView iv_content = (ImageView) view_share_summary.findViewById(R.id.iv_content);
        Bitmap bitmap_main = BitmapUtil.convertViewToBitmap(this.sv_main);
        iv_content.setImageBitmap(bitmap_main);
        Bitmap bitmap_share = BitmapUtil.loadBitmapFromView(view_share_summary);
        Context context = this.activity;
        if (bitmap_share == null) {
            bitmap = bitmap_main;
        } else {
            bitmap = bitmap_share;
        }
        if (!TextUtils.isEmpty(FileUtil.getPNGImagePath(context, bitmap, "SHARE_4_LINECHART"))) {
            ShareManager.shareLocalImage(this.activity, filePath);
        }
        if (!(bitmap_share == null || bitmap_share.isRecycled())) {
            bitmap_share.recycle();
        }
        if (bitmap_main != null && !bitmap_main.isRecycled()) {
            bitmap_main.recycle();
        }
    }

    private void initDietCart(int time_type, ArrayList<RecordFood> foodRcordList,
                              ArrayList<RecordPhoto> recordPhotoList, LinearLayout ll_card) {
        if ((foodRcordList == null || foodRcordList.size() <= 0) && (recordPhotoList == null ||
                recordPhotoList.size() <= 0)) {
            ll_card.setVisibility(8);
            return;
        }
        int i;
        ll_card.setVisibility(0);
        TextView tv_des = (TextView) ll_card.findViewById(R.id.tv_des);
        ((TextView) ll_card.findViewById(R.id.tv_time_type)).setText(FoodUtils.getDietName(this
                .activity, time_type));
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < foodRcordList.size(); i++) {
            sb.append(String.format("%1$s%2$s%3$s, ", new Object[]{((RecordFood) foodRcordList
                    .get(i)).food_name, Float.valueOf(((RecordFood) foodRcordList.get(i)).amount)
                    , ((RecordFood) foodRcordList.get(i)).unit_name}));
            this.totalDietCalory += food.calory;
        }
        for (i = 0; i < recordPhotoList.size(); i++) {
            RecordPhoto photo = (RecordPhoto) recordPhotoList.get(i);
            String str = "%1$s, ";
            Object[] objArr = new Object[1];
            objArr[0] = TextUtils.isEmpty(photo.name) ? "拍照记录" : photo.name;
            sb.append(String.format(str, objArr));
            if (photo.status != 1) {
                this.totalDietCalory += photo.calory;
            }
        }
        tv_des.setText(sb.subSequence(0, sb.lastIndexOf(",")).toString());
    }

    private void initSportCart(ArrayList<RecordSport> recordList, LinearLayout ll_card) {
        if ((recordList == null || recordList.size() <= 0) && (this.videoSportRecordsList == null
                || this.videoSportRecordsList.size() <= 0)) {
            ll_card.setVisibility(8);
            return;
        }
        int i;
        ll_card.setVisibility(0);
        TextView tv_des = (TextView) ll_card.findViewById(R.id.tv_des);
        ((TextView) ll_card.findViewById(R.id.tv_time_type)).setText(getString(R.string.a5q));
        StringBuffer sb = new StringBuffer();
        for (i = 0; i < recordList.size(); i++) {
            sb.append(String.format("%1$s%2$.1f%3$s, ", new Object[]{sport.activity_name, Float
                    .valueOf(sport.duration), ((RecordSport) recordList.get(i)).unit_name}));
            this.totalSportCalory += sport.calory;
        }
        for (i = 0; i < this.videoSportRecordsList.size(); i++) {
            sb.append(String.format("%1$s%2$.1f%3$s, ", new Object[]{videoSportRecord
                    .activity_name, Float.valueOf(((float) videoSportRecord.amount) * 1.0f), (
                    (VideoSportRecord) this.videoSportRecordsList.get(i)).unit_name}));
            this.totalSportCalory += (float) videoSportRecord.calory;
        }
        tv_des.setText(sb.subSequence(0, sb.lastIndexOf(",")).toString());
    }

    protected void onDestroy() {
        super.onDestroy();
        this.breakfastList.clear();
        this.lunchList.clear();
        this.dinnerList.clear();
        this.snacksBreakfastList.clear();
        this.snacksLunchList.clear();
        this.snacksDinnerList.clear();
        this.sportList.clear();
        this.breakfastPhotoList.clear();
        this.lunchPhotoList.clear();
        this.dinnerPhotoList.clear();
        this.snacksBreakfastPhotoList.clear();
        this.snacksLunchPhotoList.clear();
        this.snacksDinnerPhotoList.clear();
    }
}
