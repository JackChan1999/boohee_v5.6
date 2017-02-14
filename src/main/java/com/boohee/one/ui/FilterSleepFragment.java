package com.boohee.one.ui;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.boohee.one.R;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter.DataSet;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.HorizontalListView;

import java.util.Calendar;

public class FilterSleepFragment extends Fragment {
    private static final int[]    SLEEP_RES_IDS        = new int[]{R.drawable.wg, R.drawable.vr};
    private static final String[] SLEEP_RES_TEXTS      = new String[]{"起床", "睡觉"};
    private static final int[]    SLEEP_SELECT_RES_IDS = new int[]{R.drawable.wh, R.drawable.vs};
    private HorizontalIconListAdapter adapter;
    private ImageView                 currentImage;
    private HorizontalListView        iconListView;
    private int lastPosition = 0;
    private Context     mContext;
    private Uri         mUri;
    private View        markView;
    private FrameLayout parentLayout;
    private ImageView   preImage;
    private TextView    textView;
    private TextView    timeText;

    private class ItemClickListener implements OnItemClickListener {
        private ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
            updateBackground(itemView, position);
            FilterSleepFragment.this.textView.setText(FilterSleepFragment
                    .SLEEP_RES_TEXTS[position]);
        }

        private void updateBackground(View itemView, int currentPos) {
            FilterSleepFragment.this.markView.setVisibility(0);
            if (FilterSleepFragment.this.currentImage != null) {
                FilterSleepFragment.this.currentImage.setBackgroundResource(FilterSleepFragment
                        .SLEEP_RES_IDS[FilterSleepFragment.this.lastPosition]);
            }
            FilterSleepFragment.this.currentImage = (ImageView) itemView.findViewById(R.id
                    .charlet_icon);
            FilterSleepFragment.this.currentImage.setBackgroundResource(FilterSleepFragment
                    .SLEEP_SELECT_RES_IDS[currentPos]);
            FilterSleepFragment.this.lastPosition = currentPos;
        }
    }

    private class TimeSetCallback implements OnTimeSetListener {
        private TimeSetCallback() {
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            String min = "";
            String h = "";
            if (minute < 10) {
                min = "0" + minute;
            } else {
                min = minute + "";
            }
            if (hour < 10) {
                h = "0" + hour;
            } else {
                h = "" + hour;
            }
            FilterSleepFragment.this.timeText.setText(h + ":" + min);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fr, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mContext = getActivity();
        this.mUri = getActivity().getIntent().getData();
        ViewUtils.initImageView(getActivity(), this.mUri, this.preImage);
        initListView();
        initTextClock();
    }

    public View getView() {
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
        this.timeText = (TextView) view.findViewById(R.id.upMiddleText);
        this.textView = (TextView) view.findViewById(R.id.upRightText);
    }

    private void initListView() {
        this.adapter = new HorizontalIconListAdapter(this.mContext, new DataSet(SLEEP_RES_IDS,
                SLEEP_RES_TEXTS));
        this.iconListView.setAdapter(this.adapter);
        this.iconListView.setOnItemClickListener(new ItemClickListener());
    }

    private void initTextClock() {
        final Calendar c = Calendar.getInstance();
        this.timeText.setText(c.get(11) + ":" + c.get(12));
        this.timeText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(FilterSleepFragment.this.mContext, new TimeSetCallback(), c.get(11), c.get(12), true).show();
            }
        });
    }
}
