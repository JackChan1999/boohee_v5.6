package com.boohee.one.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.model.FilterSyncBean;
import com.boohee.model.FilterSyncFigureBean;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter;
import com.boohee.one.ui.adapter.HorizontalIconListAdapter.DataSet;
import com.boohee.utils.Keyboard;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.HorizontalListView;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

public class FilterFigureFragment extends Fragment {
    private static final int[]    FIGURE_DATAS     = new int[]{50, 160, 50, 90, 60, 20, 40, 30};
    private static final int[]    FIGURE_RES_IDS   = new int[]{R.drawable.wl, R.drawable.ow, R
            .drawable.wf, R.drawable.ns, R.drawable.oz, R.drawable.mu, R.drawable.w5, R.drawable
            .nk};
    public static final  String[] FIGURE_RES_TEXTS = new String[]{"体重", "身高", "腰围", "胸围", "臀围",
            "手臂围", "大腿围", "小腿围"};
    private HorizontalIconListAdapter adapter;
    private View                      currentFigureView;
    private View[] figureViewArray = new View[FIGURE_RES_IDS.length];
    private HorizontalListView iconListView;
    private Context            mContext;
    private Uri                mUri;
    private FrameLayout        parentLayout;
    private ImageView          preImage;
    private FilterSyncFigureBean[] syncBeans = new FilterSyncFigureBean[FIGURE_RES_IDS.length];

    private class ItemClickListener implements OnItemClickListener {
        private ItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
            if (FilterFigureFragment.this.figureViewArray[position] != null) {
                FilterFigureFragment.this.currentFigureView.clearFocus();
                FilterFigureFragment.this.hideButton(FilterFigureFragment.this.currentFigureView);
                FilterFigureFragment.this.showButton(FilterFigureFragment.this
                        .figureViewArray[position]);
                FilterFigureFragment.this.currentFigureView = FilterFigureFragment.this
                        .figureViewArray[position];
                return;
            }
            FilterFigureFragment.this.syncBeans[position] = new FilterSyncFigureBean();
            FilterFigureFragment.this.parentLayout.addView(FilterFigureFragment.this
                    .createFigureView(position));
            saveTagName();
        }

        private void saveTagName() {
            UserPreference.getInstance(FilterFigureFragment.this.mContext).putString
                    (ImageFilterActivity.KEY_POST_TAG, "秀身材");
        }
    }

    private class TouchListener implements OnTouchListener {
        private int lastX        = 0;
        private int lastY        = 0;
        private int screenHeight = 0;
        private int screenWidth  = 0;

        public TouchListener(Context mContext) {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            this.screenWidth = dm.widthPixels;
            this.screenHeight = dm.heightPixels;
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.lastX = (int) event.getRawX();
                    this.lastY = (int) event.getRawY();
                    break;
                case 2:
                    int dx = ((int) event.getRawX()) - this.lastX;
                    int dy = ((int) event.getRawY()) - this.lastY;
                    int left = v.getLeft() + dx;
                    int top = v.getTop() + dy;
                    int right = v.getRight() + dx;
                    int bottom = v.getBottom() + dy;
                    this.lastX = (int) event.getRawX();
                    this.lastY = (int) event.getRawY();
                    if (left < 0) {
                        right = v.getWidth();
                    }
                    if (right > this.screenWidth) {
                        right = this.screenWidth;
                        left = right - v.getWidth();
                    }
                    if (top < 0) {
                        bottom = v.getHeight();
                    }
                    if (bottom > this.screenHeight) {
                        bottom = this.screenHeight;
                        top = bottom - v.getHeight();
                    }
                    v.layout(left, top, right, bottom);
                    v.invalidate();
                    break;
            }
            return false;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fq, container, false);
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
    }

    public View getView() {
        hideButton(this.currentFigureView);
        this.currentFigureView = null;
        Keyboard.closeAll(this.mContext);
        return this.parentLayout;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.parentLayout.removeAllViews();
        for (int i = 0; i < this.figureViewArray.length; i++) {
            this.figureViewArray[i] = null;
        }
        this.currentFigureView = null;
    }

    private void findView(View view) {
        this.parentLayout = (FrameLayout) view.findViewById(R.id.filter_parent);
        this.preImage = (ImageView) view.findViewById(R.id.filterImageView);
        this.iconListView = (HorizontalListView) view.findViewById(R.id.filterIconListView);
    }

    private void initListView() {
        this.adapter = new HorizontalIconListAdapter(this.mContext, new DataSet(FIGURE_RES_IDS,
                FIGURE_RES_TEXTS));
        this.iconListView.setAdapter(this.adapter);
        this.iconListView.setOnItemClickListener(new ItemClickListener());
    }

    private View createFigureView(final int pos) {
        final View figureView = View.inflate(this.mContext, R.layout.p4, null);
        figureView.setTag(Integer.valueOf(pos));
        TextView rightText = (TextView) figureView.findViewById(R.id.upRightText);
        final EditText middleEdit = (EditText) figureView.findViewById(R.id.upMiddleText);
        ((TextView) figureView.findViewById(R.id.upLeftText)).setText(FIGURE_RES_TEXTS[pos]);
        middleEdit.setText(FIGURE_DATAS[pos] + "");
        middleEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (FilterFigureFragment.this.syncBeans[pos] != null) {
                    if (s == null || TextUtils.isEmpty(s.toString())) {
                        FilterFigureFragment.this.syncBeans[pos].setValue(0.0f);
                        middleEdit.setText("0");
                        return;
                    }
                    FilterFigureFragment.this.syncBeans[pos].setValue(Float.parseFloat(s.toString
                            ()));
                }
            }
        });
        if (pos == 0) {
            rightText.setText("kg");
            this.syncBeans[pos].setUnit("kg");
        } else {
            this.syncBeans[pos].setUnit(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_COUNT);
        }
        this.syncBeans[pos].setName(FIGURE_RES_TEXTS[pos]);
        this.syncBeans[pos].setValue((float) FIGURE_DATAS[pos]);
        figureView.setOnTouchListener(new TouchListener(this.mContext));
        figureView.setLayoutParams(new LayoutParams(-2, -2));
        figureView.setClickable(true);
        this.figureViewArray[pos] = figureView;
        Button editBtn = (Button) figureView.findViewById(R.id.editBtn);
        ((Button) figureView.findViewById(R.id.removeBtn)).setOnClickListener(new OnClickListener
                () {
            public void onClick(View v) {
                figureView.setVisibility(8);
                int pos = ((Integer) figureView.getTag()).intValue();
                FilterFigureFragment.this.figureViewArray[pos] = null;
                FilterFigureFragment.this.parentLayout.removeView(figureView);
                FilterFigureFragment.this.syncBeans[pos] = null;
            }
        });
        editBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                middleEdit.selectAll();
                middleEdit.requestFocus();
                Keyboard.open(FilterFigureFragment.this.mContext, middleEdit);
            }
        });
        figureView.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (FilterFigureFragment.this.currentFigureView != figureView) {
                    FilterFigureFragment.this.hideButton(FilterFigureFragment.this
                            .currentFigureView);
                    FilterFigureFragment.this.showButton(figureView);
                    FilterFigureFragment.this.currentFigureView = figureView;
                }
            }
        });
        if (this.currentFigureView != null) {
            hideButton(this.currentFigureView);
        }
        this.currentFigureView = figureView;
        return figureView;
    }

    private void hideButton(View figureView) {
        if (figureView != null) {
            figureView.findViewById(R.id.removeBtn).setVisibility(4);
            figureView.findViewById(R.id.editBtn).setVisibility(4);
            ((EditText) figureView.findViewById(R.id.upMiddleText)).setCursorVisible(false);
        }
    }

    private void showButton(View figureView) {
        figureView.findViewById(R.id.removeBtn).setVisibility(0);
        figureView.findViewById(R.id.editBtn).setVisibility(0);
    }

    public void saveSyncData(FilterSyncBean syncData) {
        boolean isNeedSave = false;
        for (FilterSyncFigureBean bean : this.syncBeans) {
            if (bean != null && bean.getValue() > 1.0f) {
                isNeedSave = true;
                break;
            }
        }
        if (isNeedSave) {
            syncData.setFigures(this.syncBeans);
            return;
        }
        UserPreference.getInstance(this.mContext).remove(ImageFilterActivity.KEY_POST_TAG);
        syncData.setFigures(null);
    }
}
