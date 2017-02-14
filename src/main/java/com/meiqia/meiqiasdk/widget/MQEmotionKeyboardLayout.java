package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQEmotionUtil;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MQEmotionKeyboardLayout extends MQBaseCustomCompositeView {
    private static final int EMOTION_COLUMN    = 7;
    private static final int EMOTION_PAGE_SIZE = 27;
    private static final int EMOTION_ROW       = 4;
    private Callback             mCallback;
    private ViewPager            mContentVp;
    private ArrayList<GridView>  mGridViewList;
    private ArrayList<ImageView> mIndicatorIvList;
    private LinearLayout         mIndicatorLl;

    public interface Callback {
        void onDelete();

        void onInsert(String str);
    }

    class EmotionAdapter extends BaseAdapter {
        private List<String> mDatas;

        public EmotionAdapter(List<String> datas) {
            this.mDatas = datas;
        }

        public int getCount() {
            return this.mDatas == null ? 0 : this.mDatas.size();
        }

        public String getItem(int position) {
            return (String) this.mDatas.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MQEmotionKeyboardLayout.this.getContext(), R.layout
                        .mq_item_emotion_keyboard, null);
            }
            ImageView iconIv = (ImageView) convertView;
            if (position == getCount() - 1) {
                iconIv.setImageResource(R.drawable.mq_emoji_delete);
                iconIv.setVisibility(0);
            } else {
                String key = (String) this.mDatas.get(position);
                if (TextUtils.isEmpty(key)) {
                    iconIv.setVisibility(4);
                } else {
                    iconIv.setImageResource(MQEmotionUtil.getImgByName(key));
                    iconIv.setVisibility(0);
                }
            }
            return convertView;
        }
    }

    class EmotionPagerAdapter extends PagerAdapter {
        EmotionPagerAdapter() {
        }

        public int getCount() {
            return MQEmotionKeyboardLayout.this.mGridViewList.size();
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) MQEmotionKeyboardLayout.this.mGridViewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView((View) MQEmotionKeyboardLayout.this.mGridViewList.get(position));
            return MQEmotionKeyboardLayout.this.mGridViewList.get(position);
        }
    }

    public MQEmotionKeyboardLayout(Context context) {
        super(context);
    }

    public MQEmotionKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MQEmotionKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getLayoutId() {
        return R.layout.mq_layout_emotion_keyboard;
    }

    protected void initView() {
        this.mContentVp = (ViewPager) getViewById(R.id.vp_emotion_keyboard_content);
        this.mIndicatorLl = (LinearLayout) getViewById(R.id.ll_emotion_keyboard_indicator);
    }

    protected void setListener() {
        this.mContentVp.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                for (int i = 0; i < MQEmotionKeyboardLayout.this.mIndicatorIvList.size(); i++) {
                    ((ImageView) MQEmotionKeyboardLayout.this.mIndicatorIvList.get(i)).setEnabled
                            (false);
                }
                ((ImageView) MQEmotionKeyboardLayout.this.mIndicatorIvList.get(position))
                        .setEnabled(true);
            }
        });
    }

    protected int[] getAttrs() {
        return new int[0];
    }

    protected void initAttr(int attr, TypedArray typedArray) {
    }

    protected void processLogic() {
        this.mIndicatorIvList = new ArrayList();
        this.mGridViewList = new ArrayList();
        int emotionPageCount = ((MQEmotionUtil.sEmotionKeyArr.length - 1) / 27) + 1;
        LayoutParams indicatorIvLp = new LayoutParams(-2, -2);
        int margin = MQUtils.dip2px(getContext(), 5.0f);
        indicatorIvLp.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < emotionPageCount; i++) {
            ImageView indicatorIv = new ImageView(getContext());
            indicatorIv.setLayoutParams(indicatorIvLp);
            indicatorIv.setImageResource(R.drawable.mq_selector_emotion_indicator);
            indicatorIv.setEnabled(false);
            this.mIndicatorIvList.add(indicatorIv);
            this.mIndicatorLl.addView(indicatorIv);
            this.mGridViewList.add(getGridView(i));
        }
        ((ImageView) this.mIndicatorIvList.get(0)).setEnabled(true);
        this.mContentVp.setAdapter(new EmotionPagerAdapter());
    }

    private GridView getGridView(int position) {
        int edge = MQUtils.dip2px(getContext(), 5.0f);
        GridView gridView = new GridView(getContext());
        gridView.setPadding(edge, edge, edge, edge);
        gridView.setNumColumns(7);
        gridView.setVerticalSpacing(edge);
        gridView.setHorizontalSpacing(edge);
        gridView.setOverScrollMode(2);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setVerticalFadingEdgeEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setHorizontalFadingEdgeEnabled(false);
        gridView.setSelector(17170445);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmotionAdapter adapter = (EmotionAdapter) parent.getAdapter();
                if (position == adapter.getCount() - 1) {
                    if (MQEmotionKeyboardLayout.this.mCallback != null) {
                        MQEmotionKeyboardLayout.this.mCallback.onDelete();
                    }
                } else if (MQEmotionKeyboardLayout.this.mCallback != null) {
                    MQEmotionKeyboardLayout.this.mCallback.onInsert(adapter.getItem(position));
                }
            }
        });
        int start = position * 27;
        List<String> tempEmotionList = Arrays.asList(Arrays.copyOfRange(MQEmotionUtil
                .sEmotionKeyArr, start, start + 27));
        List<String> emotionList = new ArrayList();
        emotionList.addAll(tempEmotionList);
        emotionList.add("");
        gridView.setAdapter(new EmotionAdapter(emotionList));
        return gridView;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
