package com.facebook.rebound.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.boohee.widgets.PathListView;
import com.facebook.rebound.OrigamiValueConverter;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringConfigRegistry;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.tencent.tinker.android.dx.instruction.Opcodes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpringConfiguratorView extends FrameLayout {
    private static final DecimalFormat DECIMAL_FORMAT  = new DecimalFormat("#.#");
    private static final float         MAX_FRICTION    = 50.0f;
    private static final int           MAX_SEEKBAR_VAL = 100000;
    private static final float         MAX_TENSION     = 200.0f;
    private static final float         MIN_FRICTION    = 0.0f;
    private static final float         MIN_TENSION     = 0.0f;
    private       TextView             mFrictionLabel;
    private       SeekBar              mFrictionSeekBar;
    private final float                mRevealPx;
    private final Spring               mRevealerSpring;
    private       SpringConfig         mSelectedSpringConfig;
    private final List<SpringConfig>   mSpringConfigs;
    private       Spinner              mSpringSelectorSpinner;
    private final float                mStashPx;
    private       TextView             mTensionLabel;
    private       SeekBar              mTensionSeekBar;
    private final int                  mTextColor;
    private final SpinnerAdapter       spinnerAdapter;
    private final SpringConfigRegistry springConfigRegistry;

    private class OnNubTouchListener implements OnTouchListener {
        private OnNubTouchListener() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                SpringConfiguratorView.this.togglePosition();
            }
            return true;
        }
    }

    private class RevealerSpringListener implements SpringListener {
        private RevealerSpringListener() {
        }

        public void onSpringUpdate(Spring spring) {
            float val = (float) spring.getCurrentValue();
            float minTranslate = SpringConfiguratorView.this.mRevealPx;
            SpringConfiguratorView.this.setTranslationY((val * (SpringConfiguratorView.this
                    .mStashPx - minTranslate)) + minTranslate);
        }

        public void onSpringAtRest(Spring spring) {
        }

        public void onSpringActivate(Spring spring) {
        }

        public void onSpringEndStateChange(Spring spring) {
        }
    }

    private class SeekbarListener implements OnSeekBarChangeListener {
        private SeekbarListener() {
        }

        public void onProgressChanged(SeekBar seekBar, int val, boolean b) {
            if (seekBar == SpringConfiguratorView.this.mTensionSeekBar) {
                float scaledTension = ((((float) val) * SpringConfiguratorView.MAX_TENSION) /
                        100000.0f) + 0.0f;
                SpringConfiguratorView.this.mSelectedSpringConfig.tension = OrigamiValueConverter
                        .tensionFromOrigamiValue((double) scaledTension);
                SpringConfiguratorView.this.mTensionLabel.setText("T:" + SpringConfiguratorView
                        .DECIMAL_FORMAT.format((double) scaledTension));
            }
            if (seekBar == SpringConfiguratorView.this.mFrictionSeekBar) {
                float scaledFriction = ((((float) val) * 50.0f) / 100000.0f) + 0.0f;
                SpringConfiguratorView.this.mSelectedSpringConfig.friction =
                        OrigamiValueConverter.frictionFromOrigamiValue((double) scaledFriction);
                SpringConfiguratorView.this.mFrictionLabel.setText("F:" + SpringConfiguratorView
                        .DECIMAL_FORMAT.format((double) scaledFriction));
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private class SpinnerAdapter extends BaseAdapter {
        private final Context mContext;
        private final List<String> mStrings = new ArrayList();

        public SpinnerAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return this.mStrings.size();
        }

        public Object getItem(int position) {
            return this.mStrings.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public void add(String string) {
            this.mStrings.add(string);
            notifyDataSetChanged();
        }

        public void clear() {
            this.mStrings.clear();
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = new TextView(this.mContext);
                textView.setLayoutParams(new LayoutParams(-1, -1));
                int twelvePx = Util.dpToPx(12.0f, SpringConfiguratorView.this.getResources());
                textView.setPadding(twelvePx, twelvePx, twelvePx, twelvePx);
                textView.setTextColor(SpringConfiguratorView.this.mTextColor);
            } else {
                textView = (TextView) convertView;
            }
            textView.setText((CharSequence) this.mStrings.get(position));
            return textView;
        }
    }

    private class SpringSelectedListener implements OnItemSelectedListener {
        private SpringSelectedListener() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            SpringConfiguratorView.this.mSelectedSpringConfig = (SpringConfig)
                    SpringConfiguratorView.this.mSpringConfigs.get(i);
            SpringConfiguratorView.this.updateSeekBarsForSpringConfig(SpringConfiguratorView.this
                    .mSelectedSpringConfig);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public SpringConfiguratorView(Context context) {
        this(context, null);
    }

    public SpringConfiguratorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(11)
    public SpringConfiguratorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSpringConfigs = new ArrayList();
        this.mTextColor = Color.argb(255, Opcodes.SHR_INT_LIT8, Opcodes.SHR_INT_LIT8, Opcodes
                .SHR_INT_LIT8);
        SpringSystem springSystem = SpringSystem.create();
        this.springConfigRegistry = SpringConfigRegistry.getInstance();
        this.spinnerAdapter = new SpinnerAdapter(context);
        Resources resources = getResources();
        this.mRevealPx = (float) Util.dpToPx(40.0f, resources);
        this.mStashPx = (float) Util.dpToPx(280.0f, resources);
        this.mRevealerSpring = springSystem.createSpring();
        this.mRevealerSpring.setCurrentValue(PathListView.NO_ZOOM).setEndValue(PathListView
                .NO_ZOOM).addListener(new RevealerSpringListener());
        addView(generateHierarchy(context));
        SeekbarListener seekbarListener = new SeekbarListener();
        this.mTensionSeekBar.setMax(MAX_SEEKBAR_VAL);
        this.mTensionSeekBar.setOnSeekBarChangeListener(seekbarListener);
        this.mFrictionSeekBar.setMax(MAX_SEEKBAR_VAL);
        this.mFrictionSeekBar.setOnSeekBarChangeListener(seekbarListener);
        this.mSpringSelectorSpinner.setAdapter(this.spinnerAdapter);
        this.mSpringSelectorSpinner.setOnItemSelectedListener(new SpringSelectedListener());
        refreshSpringConfigurations();
        setTranslationY(this.mStashPx);
    }

    private View generateHierarchy(Context context) {
        Resources resources = getResources();
        int fivePx = Util.dpToPx(5.0f, resources);
        int tenPx = Util.dpToPx(10.0f, resources);
        int twentyPx = Util.dpToPx(20.0f, resources);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(0, -2, 1.0f);
        tableLayoutParams.setMargins(0, 0, fivePx, 0);
        FrameLayout root = new FrameLayout(context);
        root.setLayoutParams(Util.createLayoutParams(-1, Util.dpToPx(300.0f, resources)));
        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams params = Util.createMatchParams();
        params.setMargins(0, twentyPx, 0, 0);
        container.setLayoutParams(params);
        container.setBackgroundColor(Color.argb(100, 0, 0, 0));
        root.addView(container);
        this.mSpringSelectorSpinner = new Spinner(context, 0);
        params = Util.createMatchWrapParams();
        params.gravity = 48;
        params.setMargins(tenPx, tenPx, tenPx, 0);
        this.mSpringSelectorSpinner.setLayoutParams(params);
        container.addView(this.mSpringSelectorSpinner);
        LinearLayout linearLayout = new LinearLayout(context);
        params = Util.createMatchWrapParams();
        params.setMargins(0, 0, 0, Util.dpToPx(80.0f, resources));
        params.gravity = 80;
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(1);
        container.addView(linearLayout);
        LinearLayout seekWrapper = new LinearLayout(context);
        params = Util.createMatchWrapParams();
        params.setMargins(tenPx, tenPx, tenPx, twentyPx);
        seekWrapper.setPadding(tenPx, tenPx, tenPx, tenPx);
        seekWrapper.setLayoutParams(params);
        seekWrapper.setOrientation(0);
        linearLayout.addView(seekWrapper);
        this.mTensionSeekBar = new SeekBar(context);
        this.mTensionSeekBar.setLayoutParams(tableLayoutParams);
        seekWrapper.addView(this.mTensionSeekBar);
        this.mTensionLabel = new TextView(getContext());
        this.mTensionLabel.setTextColor(this.mTextColor);
        params = Util.createLayoutParams(Util.dpToPx(50.0f, resources), -1);
        this.mTensionLabel.setGravity(19);
        this.mTensionLabel.setLayoutParams(params);
        this.mTensionLabel.setMaxLines(1);
        seekWrapper.addView(this.mTensionLabel);
        seekWrapper = new LinearLayout(context);
        params = Util.createMatchWrapParams();
        params.setMargins(tenPx, tenPx, tenPx, twentyPx);
        seekWrapper.setPadding(tenPx, tenPx, tenPx, tenPx);
        seekWrapper.setLayoutParams(params);
        seekWrapper.setOrientation(0);
        linearLayout.addView(seekWrapper);
        this.mFrictionSeekBar = new SeekBar(context);
        this.mFrictionSeekBar.setLayoutParams(tableLayoutParams);
        seekWrapper.addView(this.mFrictionSeekBar);
        this.mFrictionLabel = new TextView(getContext());
        this.mFrictionLabel.setTextColor(this.mTextColor);
        params = Util.createLayoutParams(Util.dpToPx(50.0f, resources), -1);
        this.mFrictionLabel.setGravity(19);
        this.mFrictionLabel.setLayoutParams(params);
        this.mFrictionLabel.setMaxLines(1);
        seekWrapper.addView(this.mFrictionLabel);
        View nub = new View(context);
        params = Util.createLayoutParams(Util.dpToPx(60.0f, resources), Util.dpToPx(40.0f,
                resources));
        params.gravity = 49;
        nub.setLayoutParams(params);
        SpringConfiguratorView springConfiguratorView = this;
        nub.setOnTouchListener(new OnNubTouchListener());
        nub.setBackgroundColor(Color.argb(255, 0, 164, 209));
        root.addView(nub);
        return root;
    }

    public void destroy() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        this.mRevealerSpring.destroy();
    }

    public void refreshSpringConfigurations() {
        Map<SpringConfig, String> springConfigMap = this.springConfigRegistry.getAllSpringConfig();
        this.spinnerAdapter.clear();
        this.mSpringConfigs.clear();
        for (Entry<SpringConfig, String> entry : springConfigMap.entrySet()) {
            if (entry.getKey() != SpringConfig.defaultConfig) {
                this.mSpringConfigs.add(entry.getKey());
                this.spinnerAdapter.add((String) entry.getValue());
            }
        }
        this.mSpringConfigs.add(SpringConfig.defaultConfig);
        this.spinnerAdapter.add((String) springConfigMap.get(SpringConfig.defaultConfig));
        this.spinnerAdapter.notifyDataSetChanged();
        if (this.mSpringConfigs.size() > 0) {
            this.mSpringSelectorSpinner.setSelection(0);
        }
    }

    private void updateSeekBarsForSpringConfig(SpringConfig springConfig) {
        int scaledTension = Math.round(((((float) OrigamiValueConverter.origamiValueFromTension
                (springConfig.tension)) - 0.0f) * 100000.0f) / MAX_TENSION);
        int scaledFriction = Math.round(((((float) OrigamiValueConverter.origamiValueFromFriction
                (springConfig.friction)) - 0.0f) * 100000.0f) / 50.0f);
        this.mTensionSeekBar.setProgress(scaledTension);
        this.mFrictionSeekBar.setProgress(scaledFriction);
    }

    private void togglePosition() {
        double d = PathListView.NO_ZOOM;
        double currentValue = this.mRevealerSpring.getEndValue();
        Spring spring = this.mRevealerSpring;
        if (currentValue == PathListView.NO_ZOOM) {
            d = 0.0d;
        }
        spring.setEndValue(d);
    }
}
