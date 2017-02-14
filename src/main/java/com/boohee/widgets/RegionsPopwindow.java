package com.boohee.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.boohee.one.R;
import com.boohee.utility.App;
import com.boohee.utils.FileUtil;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ViewFinder;

import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class RegionsPopwindow implements OnClickListener, OnWheelChangedListener {
    static final String TAG = RegionsPopwindow.class.getName();
    private static RegionsPopwindow regionsPopwindow;
    private        View             contentView;
    private        ViewFinder       finder;
    private        Animation        inAnim;
    private boolean isAdd = false;
    private WheelView mArea;
    private Map<String, String[]> mAreaDatasMap  = new HashMap();
    private Map<String, String[]> mCitisDatasMap = new HashMap();
    private WheelView mCity;
    private Context   mContext;
    private String    mCurrentCityName;
    private String mCurrentDistrictName = "";
    private String                 mCurrentProviceName;
    private JSONObject             mJsonObj;
    private WheelView              mProvince;
    private String[]               mProvinceDatas;
    private View                   mask;
    private LinearLayout           popLayout;
    private PopupWindow            popWindow;
    private onRegionChangeListener regionChangeListener;

    public interface onRegionChangeListener {
        void onChange(String str, String str2, String str3);
    }

    public static RegionsPopwindow getInstance() {
        if (regionsPopwindow == null) {
            regionsPopwindow = new RegionsPopwindow();
        }
        return regionsPopwindow;
    }

    private void createPopWindow(Context context, String provice, String city, String area) {
        if (this.mContext != context) {
            this.mContext = context;
            this.mCurrentProviceName = provice;
            this.mCurrentCityName = city;
            this.mCurrentDistrictName = area;
            boolean z = TextUtils.isEmpty(this.mCurrentProviceName) || TextUtils.isEmpty(this
                    .mCurrentCityName) || TextUtils.isEmpty(this.mCurrentDistrictName);
            this.isAdd = z;
            this.popWindow = new PopupWindow(createContentView(), -1, -2, true);
            this.popWindow.setBackgroundDrawable(new BitmapDrawable());
            this.popWindow.setFocusable(true);
            this.popWindow.setTouchable(true);
            this.popWindow.setOutsideTouchable(true);
            this.inAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.s);
        }
    }

    private void initJsonData() {
        if (!TextUtil.isEmpty(FileUtil.readStrFromAPP(App.FILE_PATH, App.REGION_NAME))) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(FileUtil.readStrFromAPP(App.FILE_PATH, App
                        .REGION_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.mJsonObj = jsonObject.optJSONObject("data");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initDatas() {
        /*
        r19 = this;
        r0 = r19;
        r0 = r0.mJsonObj;
        r17 = r0;
        if (r17 != 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r0 = r19;
        r0 = r0.mJsonObj;	 Catch:{ JSONException -> 0x00ad }
        r17 = r0;
        r18 = "provinces";
        r9 = r17.getJSONArray(r18);	 Catch:{ JSONException -> 0x00ad }
        r17 = r9.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        r0 = new java.lang.String[r0];	 Catch:{ JSONException -> 0x00ad }
        r17 = r0;
        r0 = r17;
        r1 = r19;
        r1.mProvinceDatas = r0;	 Catch:{ JSONException -> 0x00ad }
        r6 = 0;
    L_0x0027:
        r17 = r9.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        if (r6 >= r0) goto L_0x00b1;
    L_0x002f:
        r12 = r9.getJSONObject(r6);	 Catch:{ JSONException -> 0x00ad }
        r17 = "name";
        r0 = r17;
        r16 = r12.getString(r0);	 Catch:{ JSONException -> 0x00ad }
        r0 = r19;
        r0 = r0.mProvinceDatas;	 Catch:{ JSONException -> 0x00ad }
        r17 = r0;
        r17[r6] = r16;	 Catch:{ JSONException -> 0x00ad }
        r11 = 0;
        r17 = "cities";
        r0 = r17;
        r11 = r12.getJSONArray(r0);	 Catch:{ Exception -> 0x0099 }
        r17 = r11.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        r15 = new java.lang.String[r0];	 Catch:{ JSONException -> 0x00ad }
        r7 = 0;
    L_0x0057:
        r17 = r11.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        if (r7 >= r0) goto L_0x00bb;
    L_0x005f:
        r10 = r11.getJSONObject(r7);	 Catch:{ JSONException -> 0x00ad }
        r17 = "name";
        r0 = r17;
        r3 = r10.getString(r0);	 Catch:{ JSONException -> 0x00ad }
        r15[r7] = r3;	 Catch:{ JSONException -> 0x00ad }
        r8 = 0;
        r17 = "districts";
        r0 = r17;
        r8 = r10.getJSONArray(r0);	 Catch:{ Exception -> 0x009d }
        r17 = r8.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        r14 = new java.lang.String[r0];	 Catch:{ JSONException -> 0x00ad }
        r13 = 0;
    L_0x0081:
        r17 = r8.length();	 Catch:{ JSONException -> 0x00ad }
        r0 = r17;
        if (r13 >= r0) goto L_0x00a1;
    L_0x0089:
        r17 = r8.getJSONObject(r13);	 Catch:{ JSONException -> 0x00ad }
        r18 = "name";
        r2 = r17.getString(r18);	 Catch:{ JSONException -> 0x00ad }
        r14[r13] = r2;	 Catch:{ JSONException -> 0x00ad }
        r13 = r13 + 1;
        goto L_0x0081;
    L_0x0099:
        r5 = move-exception;
    L_0x009a:
        r6 = r6 + 1;
        goto L_0x0027;
    L_0x009d:
        r4 = move-exception;
    L_0x009e:
        r7 = r7 + 1;
        goto L_0x0057;
    L_0x00a1:
        r0 = r19;
        r0 = r0.mAreaDatasMap;	 Catch:{ JSONException -> 0x00ad }
        r17 = r0;
        r0 = r17;
        r0.put(r3, r14);	 Catch:{ JSONException -> 0x00ad }
        goto L_0x009e;
    L_0x00ad:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x00b1:
        r17 = 0;
        r0 = r17;
        r1 = r19;
        r1.mJsonObj = r0;
        goto L_0x0008;
    L_0x00bb:
        r0 = r19;
        r0 = r0.mCitisDatasMap;	 Catch:{ JSONException -> 0x00ad }
        r17 = r0;
        r0 = r17;
        r1 = r16;
        r0.put(r1, r15);	 Catch:{ JSONException -> 0x00ad }
        goto L_0x009a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.widgets" +
                ".RegionsPopwindow.initDatas():void");
    }

    private View createContentView() {
        this.contentView = View.inflate(this.mContext, R.layout.jq, null);
        this.finder = new ViewFinder(this.contentView);
        initJsonData();
        findView();
        initDatas();
        locationToRegions();
        return this.contentView;
    }

    private void findView() {
        this.popLayout = (LinearLayout) this.finder.find(R.id.popLayout);
        this.mask = this.finder.find(R.id.mask);
        this.mProvince = (WheelView) this.finder.find(R.id.regions_privince);
        this.mCity = (WheelView) this.finder.find(R.id.regions_city);
        this.mArea = (WheelView) this.finder.find(R.id.regions_area);
        this.mProvince.setVisibleItems(3);
        this.mCity.setVisibleItems(3);
        this.mArea.setVisibleItems(3);
        this.mask.setOnClickListener(this);
    }

    private void locationToRegions() {
        if (this.mProvinceDatas != null && this.mProvinceDatas.length > 0) {
            this.mProvince.setViewAdapter(new ArrayWheelAdapter(this.mContext, this
                    .mProvinceDatas));
            this.mCurrentProviceName = this.mProvinceDatas[0];
            String[] cities = (String[]) this.mCitisDatasMap.get(this.mCurrentProviceName);
            this.mCurrentCityName = cities[0];
            this.mCity.setViewAdapter(new ArrayWheelAdapter(this.mContext, cities));
            this.mCity.setCurrentItem(0);
            String[] areas = (String[]) this.mAreaDatasMap.get(this.mCurrentCityName);
            this.mCurrentDistrictName = areas[0];
            this.mArea.setViewAdapter(new ArrayWheelAdapter(this.mContext, areas));
            this.mArea.setCurrentItem(0);
            if (this.isAdd && this.regionChangeListener != null) {
                this.regionChangeListener.onChange(this.mCurrentProviceName, this
                        .mCurrentCityName, this.mCurrentDistrictName);
            }
            this.mProvince.addChangingListener(this);
            this.mCity.addChangingListener(this);
            this.mArea.addChangingListener(this);
        }
    }

    private void updateCities() {
        this.mCurrentProviceName = this.mProvinceDatas[this.mProvince.getCurrentItem()];
        String[] cities = (String[]) this.mCitisDatasMap.get(this.mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        this.mCity.setViewAdapter(new ArrayWheelAdapter(this.mContext, cities));
        this.mCity.setCurrentItem(0);
        updateAreas();
    }

    private void updateAreas() {
        this.mCurrentCityName = ((String[]) this.mCitisDatasMap.get(this.mCurrentProviceName))
                [this.mCity.getCurrentItem()];
        String[] areas = (String[]) this.mAreaDatasMap.get(this.mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        this.mArea.setViewAdapter(new ArrayWheelAdapter(this.mContext, areas));
        this.mArea.setCurrentItem(0);
        if (areas != null && areas.length > 0) {
            this.mCurrentDistrictName = areas[0];
        }
    }

    private void show(Context context) {
        if (this.popWindow != null && !this.popWindow.isShowing()) {
            this.popWindow.showAtLocation(new View(context), 48, 0, 0);
            this.popLayout.startAnimation(this.inAnim);
        }
    }

    public synchronized boolean isShowing() {
        boolean z;
        z = this.popWindow != null && this.popWindow.isShowing();
        return z;
    }

    public synchronized void dismiss() {
        if (this.popWindow != null && this.popWindow.isShowing()) {
            this.popWindow.dismiss();
        }
    }

    public synchronized void showRegionsPopWindow(Context context, String provice, String city,
                                                  String area) {
        createPopWindow(context, provice, city, area);
        show(context);
    }

    public void setRegionChangeListener(onRegionChangeListener regionChangeListener) {
        this.regionChangeListener = regionChangeListener;
    }

    public void onClick(View v) {
        dismiss();
    }

    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == this.mProvince) {
            updateCities();
        } else if (wheel == this.mCity) {
            updateAreas();
        } else if (wheel == this.mArea) {
            this.mCurrentDistrictName = ((String[]) this.mAreaDatasMap.get(this.mCurrentCityName)
            )[newValue];
        }
        if (this.regionChangeListener != null) {
            this.regionChangeListener.onChange(this.mCurrentProviceName, this.mCurrentCityName, this.mCurrentDistrictName);
        }
    }
}
