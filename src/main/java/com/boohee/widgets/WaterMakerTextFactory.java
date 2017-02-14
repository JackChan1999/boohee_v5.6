package com.boohee.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.boohee.one.R;

import java.util.Map;

public class WaterMakerTextFactory {
    public static final String                BOTTOM_TEXT    = "BOTTOM_TEXT";
    public static final String                UP_LEFT_TEXT   = "UP_LEFT_TEXT";
    public static final String                UP_MIDDLE_TEXT = "UP_MIDDLE_TEXT";
    public static final String                UP_RIGHT_TEXT  = "UP_RIGHT_TEXT";
    private static      WaterMakerTextFactory instance       = new WaterMakerTextFactory();

    private WaterMakerTextFactory() {
    }

    public static WaterMakerTextFactory newInstance() {
        return instance;
    }

    public View createEatView(Context mContext, Map<String, String> datas) {
        View eatView = View.inflate(mContext, R.layout.p3, null);
        initView(eatView, datas);
        return eatView;
    }

    public View createSportView(Context mContext, Map<String, String> datas) {
        View sportView = View.inflate(mContext, R.layout.p6, null);
        initView(sportView, datas);
        return sportView;
    }

    public View createFigureView(Context mContext, Map<String, String> datas) {
        View figureView = View.inflate(mContext, R.layout.p4, null);
        initView(figureView, datas);
        return figureView;
    }

    public View createSleepView(Context mContext, Map<String, String> datas) {
        View sleepView = View.inflate(mContext, R.layout.p5, null);
        initView(sleepView, datas);
        return sleepView;
    }

    public void initView(View v, Map<String, String> mParams) {
        v.setClickable(true);
        String leftText = (String) mParams.get(UP_LEFT_TEXT);
        String midText = (String) mParams.get(UP_MIDDLE_TEXT);
        String rightText = (String) mParams.get(UP_RIGHT_TEXT);
        String botText = (String) mParams.get(BOTTOM_TEXT);
        if (!TextUtils.isEmpty(leftText)) {
            TextView mUpLeftText = (TextView) v.findViewById(R.id.upLeftText);
            mUpLeftText.setText(leftText);
            mUpLeftText.setVisibility(0);
        }
        if (!TextUtils.isEmpty(midText)) {
            TextView mUpMiddleText = (TextView) v.findViewById(R.id.upMiddleText);
            mUpMiddleText.setText(midText);
            mUpMiddleText.setVisibility(0);
        }
        if (!TextUtils.isEmpty(rightText)) {
            TextView mUpRightText = (TextView) v.findViewById(R.id.upRightText);
            mUpRightText.setText(rightText);
            mUpRightText.setVisibility(0);
        }
        if (!TextUtils.isEmpty(botText)) {
            TextView mBottomText = (TextView) v.findViewById(R.id.bottomText);
            mBottomText.setText(botText);
            mBottomText.setVisibility(0);
        }
    }
}
