package com.boohee.one.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.model.FilterSyncBean;
import com.boohee.model.FilterSyncSportBean;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter.DataSet;
import com.boohee.utils.Keyboard;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.HorizontalListView;

import uk.co.senab.photoview.IPhotoView;

public class FilterSportFragment extends Fragment {
    private static final float[]  SPORT_DATAS          = new float[]{3.5f, 7.0f, 7.5f, 7.0f, 8
            .5f, IPhotoView.DEFAULT_MAX_SCALE, 6.0f, 5.0f};
    private static final int[]    SPORT_IDS            = new int[]{21095, 21096, 21097, 21098,
            21099, 21101, 21102};
    private static final int[]    SPORT_RES_IDS        = new int[]{R.drawable.wi, R.drawable.vj,
            R.drawable.w3, R.drawable.nw, R.drawable.t7, R.drawable.n6, R.drawable.n0};
    private static final String[] SPORT_RES_TEXTS      = new String[]{"走路", "跑步", "游泳", "跳操",
            "器械", "自行车", "球类运动"};
    private static final int[]    SPORT_SELECT_RES_IDS = new int[]{R.drawable.wj, R.drawable.vk,
            R.drawable.w4, R.drawable.nx, R.drawable.t8, R.drawable.n7, R.drawable.n1};
    private HorizontalIconListAdapter adapter;
    private TextView                  bottomText;
    private ImageView                 currentImage;
    private int currentPosition = 0;
    private EditText           editText;
    private HorizontalListView iconListView;
    private int lastPosition = 0;
    private Context        mContext;
    private Uri            mUri;
    private View           markView;
    private FrameLayout    parentLayout;
    private ImageView      preImage;
    private UserPreference preference;
    private FilterSyncSportBean syncBean = new FilterSyncSportBean();
    private TextView upLeftText;

    private class ItemClickListener implements OnItemClickListener {
        private ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
            FilterSportFragment.this.currentPosition = position;
            updateBackground(itemView, position);
            FilterSportFragment.this.upLeftText.setText(FilterSportFragment
                    .SPORT_RES_TEXTS[position]);
            FilterSportFragment.this.editText.requestFocus();
            FilterSportFragment.this.calConsumeCalory();
            Keyboard.open(FilterSportFragment.this.mContext, FilterSportFragment.this.editText);
            saveTagName();
            FilterSportFragment.this.syncBean.setTag(FilterSportFragment.this.upLeftText.getText
                    ().toString());
            FilterSportFragment.this.syncBean.setSportId(FilterSportFragment.SPORT_IDS[position]);
        }

        private void updateBackground(View itemView, int currentPos) {
            FilterSportFragment.this.markView.setVisibility(0);
            if (FilterSportFragment.this.currentImage != null) {
                FilterSportFragment.this.currentImage.setBackgroundResource(FilterSportFragment
                        .SPORT_RES_IDS[FilterSportFragment.this.lastPosition]);
            }
            FilterSportFragment.this.currentImage = (ImageView) itemView.findViewById(R.id
                    .charlet_icon);
            FilterSportFragment.this.currentImage.setBackgroundResource(FilterSportFragment
                    .SPORT_SELECT_RES_IDS[currentPos]);
            FilterSportFragment.this.lastPosition = currentPos;
        }

        private void saveTagName() {
            FilterSportFragment.this.preference.putString(ImageFilterActivity.KEY_POST_TAG,
                    FilterSportFragment.this.upLeftText.getText().toString());
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fs, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        addListener();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mContext = getActivity();
        this.mUri = getActivity().getIntent().getData();
        this.preference = UserPreference.getInstance(this.mContext);
        ViewUtils.initImageView(getActivity(), this.mUri, this.preImage);
        initListView();
    }

    public View getView() {
        this.editText.setCursorVisible(false);
        Keyboard.close(this.mContext, this.editText);
        return this.parentLayout;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.parentLayout.removeAllViews();
        this.currentImage = null;
    }

    private void findView(View view) {
        this.parentLayout = (FrameLayout) view.findViewById(R.id.filter_parent);
        this.preImage = (ImageView) view.findViewById(R.id.filterImageView);
        this.iconListView = (HorizontalListView) view.findViewById(R.id.filterIconListView);
        this.markView = view.findViewById(R.id.markView);
        this.upLeftText = (TextView) this.markView.findViewById(R.id.upLeftText);
        this.bottomText = (TextView) this.markView.findViewById(R.id.bottomText);
        this.editText = (EditText) this.markView.findViewById(R.id.upMiddleText);
    }

    private void addListener() {
        this.editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable edt) {
                FilterSportFragment.this.calConsumeCalory();
            }
        });
    }

    private void calConsumeCalory() {
        String timeStr = this.editText.getText().toString();
        if (TextUtils.isEmpty(timeStr)) {
            timeStr = "0";
        }
        int time = Integer.parseInt(timeStr);
        int calory = (int) ((((1.34d * ((double) (SPORT_DATAS[this.currentPosition] - 1.0f))) * (
                (double) new WeightRecordDao(this.mContext).getLastestWeight())) * ((double)
                time)) / 60.0d);
        this.bottomText.setText("消耗" + calory + "千卡");
        this.syncBean.setCalory(calory);
        this.syncBean.setDuration(time);
        this.editText.clearFocus();
    }

    private void initListView() {
        this.adapter = new HorizontalIconListAdapter(this.mContext, new DataSet(SPORT_RES_IDS,
                SPORT_RES_TEXTS));
        this.iconListView.setAdapter(this.adapter);
        this.iconListView.setOnItemClickListener(new ItemClickListener());
    }

    public void saveSyncData(FilterSyncBean syncData) {
        if (this.syncBean != null && this.syncBean.getCalory() > 1) {
            syncData.setSport(this.syncBean);
        }
    }
}
