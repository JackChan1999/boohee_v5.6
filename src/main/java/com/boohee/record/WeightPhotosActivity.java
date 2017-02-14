package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.model.WeightPhoto;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.DeleteWeightPhoto;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ResolutionUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class WeightPhotosActivity extends GestureActivity {
    public static final int MAX_SELECT_COUNT = 2;
    private WeightPhotoAdapter mAdapter;
    private List<WeightPhoto> mDataList = new ArrayList();
    private int               mPage     = 1;
    @InjectView(2131428021)
    PullToRefreshGridView pullToRefreshGrid;
    @InjectView(2131428023)
    TextView              tv_alert;
    @InjectView(2131428022)
    TextView              tv_make_compare;
    private String url = "/api/v2/photos/list.json?page=%d";
    @InjectView(2131427546)
    RelativeLayout view_operate;

    private class WeightPhotoAdapter extends SimpleBaseAdapter<WeightPhoto> implements
            OnCheckedChangeListener, OnClickListener {
        private boolean              isSelect;
        private WeightPhotosActivity mActivity;
        private List<WeightPhoto>    mDataList;
        private Set<WeightPhoto> mSelect = new HashSet();
        private int mWidth;

        public WeightPhotoAdapter(WeightPhotosActivity activity, List<WeightPhoto> data) {
            super(activity, data);
            this.mActivity = activity;
            this.mDataList = data;
            this.mWidth = (ResolutionUtils.getScreenWidth(this.context) - ViewUtils.dip2px(this
                    .context, 48.0f)) / 2;
        }

        public int getItemResource() {
            return R.layout.jn;
        }

        public View getItemView(int position, View convertView, ViewHolder holder) {
            WeightPhoto photo = (WeightPhoto) getItem(position);
            CheckBox cbSelect = (CheckBox) holder.getView(R.id.cb_select);
            cbSelect.setTag(photo);
            cbSelect.setChecked(this.mSelect.contains(photo));
            cbSelect.setOnCheckedChangeListener(this);
            cbSelect.setVisibility(this.isSelect ? 0 : 8);
            View content = holder.getView(R.id.view_content);
            content.setTag(photo);
            content.setOnClickListener(this);
            ImageView imageView = (ImageView) holder.getView(R.id.iv_photo);
            TextView tvWeight = (TextView) holder.getView(R.id.tv_weight);
            TextView tvTime = (TextView) holder.getView(R.id.tv_time);
            LayoutParams layoutParams = imageView.getLayoutParams();
            LayoutParams layoutParams2 = imageView.getLayoutParams();
            int i = this.mWidth;
            layoutParams2.height = i;
            layoutParams.width = i;
            this.imageLoader.displayImage(photo.thumb_photo_url, imageView);
            tvWeight.setText(String.valueOf(photo.weight) + "公斤");
            tvTime.setText(DateHelper.formatString(photo.record_on, "yyyy年M月d日"));
            return convertView;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            WeightPhoto photo = (WeightPhoto) buttonView.getTag();
            if (!isChecked || !this.mSelect.contains(photo)) {
                if (isChecked) {
                    int size = this.mSelect.size();
                    WeightPhotosActivity weightPhotosActivity = this.mActivity;
                    if (size >= 2) {
                        Helper.showToast((CharSequence) "最多选择两张图片");
                        buttonView.setChecked(false);
                        return;
                    }
                }
                if (isChecked) {
                    this.mSelect.add(photo);
                } else {
                    this.mSelect.remove(photo);
                }
                this.mActivity.setSelectText();
            }
        }

        public void onClick(View v) {
            if (!WheelUtils.isFastDoubleClick()) {
                WeightPhoto photo = (WeightPhoto) v.getTag();
                if (photo != null) {
                    NewWeightGalleryActivity.comeOn(WeightPhotosActivity.this.ctx, this
                            .mDataList, this.mDataList.indexOf(photo));
                }
            }
        }

        public void setMode(boolean select) {
            this.isSelect = select;
            notifyDataSetChanged();
        }

        public Set<WeightPhoto> getSelect() {
            return this.mSelect;
        }
    }

    @OnClick({2131428022, 2131427963, 2131428024})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                this.tv_make_compare.setVisibility(0);
                this.view_operate.setVisibility(8);
                this.mAdapter.setMode(false);
                return;
            case R.id.tv_make_compare:
                this.tv_make_compare.setVisibility(8);
                this.view_operate.setVisibility(0);
                this.mAdapter.setMode(true);
                return;
            case R.id.tv_submit:
                Set<WeightPhoto> select = this.mAdapter.getSelect();
                if (select.size() == 2) {
                    Iterator<WeightPhoto> iterator = select.iterator();
                    WeightPhoto photoBefore = (WeightPhoto) iterator.next();
                    WeightPhoto photoNow = (WeightPhoto) iterator.next();
                    if (DateFormatUtils.countDay(photoBefore.record_on, photoNow.record_on) > 0) {
                        WeightCompareActivity.comeOnBaby(this, photoBefore, photoNow);
                        return;
                    } else {
                        WeightCompareActivity.comeOnBaby(this, photoNow, photoBefore);
                        return;
                    }
                }
                Helper.showToast((CharSequence) "请选择两张图片");
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dz);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        initView();
        loadData(false);
    }

    private void initView() {
        this.mAdapter = new WeightPhotoAdapter(this, this.mDataList);
        this.pullToRefreshGrid.setOnRefreshListener(new OnRefreshListener2<GridView>() {
            public void onPullDownToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
                WeightPhotosActivity.this.loadData(false);
            }

            public void onPullUpToRefresh(PullToRefreshBase<GridView> pullToRefreshBase) {
                WeightPhotosActivity.this.loadData(true);
            }
        });
        this.pullToRefreshGrid.setAdapter(this.mAdapter);
    }

    private void loadData(boolean isLoadMore) {
        if (!isLoadMore) {
            this.mPage = 1;
            this.mDataList.clear();
            this.mAdapter.notifyDataSetChanged();
        }
        BooheeClient.build("record").get(String.format(this.url, new Object[]{Integer.valueOf
                (this.mPage)}), new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                String photosStr = object.optString(WeightRecordDao.PHOTOS);
                if (!TextUtils.isEmpty(photosStr)) {
                    List<WeightPhoto> photoList = FastJsonUtils.parseList(photosStr, WeightPhoto
                            .class);
                    if (photoList == null || photoList.size() <= 0) {
                        Helper.showToast((CharSequence) "没有了!");
                    } else {
                        WeightPhotosActivity.this.mDataList.addAll(photoList);
                        WeightPhotosActivity.this.mAdapter.notifyDataSetChanged();
                        WeightPhotosActivity.this.mPage = WeightPhotosActivity.this.mPage + 1;
                    }
                    if (WeightPhotosActivity.this.mDataList.size() == 0) {
                        WeightPhotosActivity.this.finish();
                    }
                }
            }

            public void onFinish() {
                WeightPhotosActivity.this.pullToRefreshGrid.onRefreshComplete();
            }
        }, this.ctx);
    }

    public void setSelectText() {
        Set<WeightPhoto> select = this.mAdapter.getSelect();
        this.tv_alert.setText(String.format("已选择 %d/%d 张", new Object[]{Integer.valueOf(select
                .size()), Integer.valueOf(2)}));
    }

    public void onEventMainThread(DeleteWeightPhoto event) {
        loadData(false);
    }

    public static void comeOn(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, WeightPhotosActivity.class));
        }
    }
}
