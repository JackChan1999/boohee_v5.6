package com.zhy.view.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class TagFlowLayout extends FlowLayout implements OnDataChangedListener {
    private static final String KEY_CHOOSE_POS = "key_choose_pos";
    private static final String KEY_DEFAULT    = "key_default";
    private static final String TAG            = "TagFlowLayout";
    private boolean            mAutoSelectEffect;
    private MotionEvent        mMotionEvent;
    private OnSelectListener   mOnSelectListener;
    private OnTagClickListener mOnTagClickListener;
    private int                mSelectedMax;
    private Set<Integer>       mSelectedView;
    private TagAdapter         mTagAdapter;

    public interface OnSelectListener {
        void onSelected(Set<Integer> set);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int i, FlowLayout flowLayout);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAutoSelectEffect = true;
        this.mSelectedMax = -1;
        this.mSelectedView = new HashSet();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        this.mAutoSelectEffect = ta.getBoolean(R.styleable.TagFlowLayout_auto_select_effect, true);
        this.mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
        ta.recycle();
        if (this.mAutoSelectEffect) {
            setClickable(true);
        }
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() != 8 && tagView.getTagView().getVisibility() == 8) {
                tagView.setVisibility(8);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
        if (this.mOnSelectListener != null) {
            setClickable(true);
        }
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.mOnTagClickListener = onTagClickListener;
        if (onTagClickListener != null) {
            setClickable(true);
        }
    }

    public void setAdapter(TagAdapter adapter) {
        this.mTagAdapter = adapter;
        this.mTagAdapter.setOnDataChangedListener(this);
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter adapter = this.mTagAdapter;
        HashSet preCheckedList = this.mTagAdapter.getPreCheckedList();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tagView = adapter.getView(this, i, adapter.getItem(i));
            TagView tagViewContainer = new TagView(getContext());
            tagView.setDuplicateParentStateEnabled(true);
            tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
            if (preCheckedList.contains(Integer.valueOf(i))) {
                tagViewContainer.setChecked(true);
            }
        }
        this.mSelectedView.addAll(preCheckedList);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            this.mMotionEvent = MotionEvent.obtain(event);
        }
        return super.onTouchEvent(event);
    }

    public boolean performClick() {
        if (this.mMotionEvent == null) {
            return super.performClick();
        }
        int x = (int) this.mMotionEvent.getX();
        int y = (int) this.mMotionEvent.getY();
        this.mMotionEvent = null;
        TagView child = findChild(x, y);
        int pos = findPosByView(child);
        if (child != null) {
            doSelect(child, pos);
            if (this.mOnTagClickListener != null) {
                return this.mOnTagClickListener.onTagClick(child.getTagView(), pos, this);
            }
        }
        return super.performClick();
    }

    public void setMaxSelectCount(int count) {
        if (this.mSelectedView.size() > count) {
            Log.w(TAG, "you has already select more than " + count + " views , so it will be " +
                    "clear .");
            this.mSelectedView.clear();
        }
        this.mSelectedMax = count;
    }

    public Set<Integer> getSelectedList() {
        return new HashSet(this.mSelectedView);
    }

    private void doSelect(TagView child, int position) {
        if (this.mAutoSelectEffect) {
            if (child.isChecked()) {
                child.setChecked(false);
                this.mSelectedView.remove(Integer.valueOf(position));
            } else if (this.mSelectedMax == 1 && this.mSelectedView.size() == 1) {
                Integer preIndex = (Integer) this.mSelectedView.iterator().next();
                ((TagView) getChildAt(preIndex.intValue())).setChecked(false);
                child.setChecked(true);
                this.mSelectedView.remove(preIndex);
                this.mSelectedView.add(Integer.valueOf(position));
            } else if (this.mSelectedMax <= 0 || this.mSelectedView.size() < this.mSelectedMax) {
                child.setChecked(true);
                this.mSelectedView.add(Integer.valueOf(position));
            } else {
                return;
            }
            if (this.mOnSelectListener != null) {
                this.mOnSelectListener.onSelected(new HashSet(this.mSelectedView));
            }
        }
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());
        String selectPos = "";
        if (this.mSelectedView.size() > 0) {
            for (Integer intValue : this.mSelectedView) {
                selectPos = selectPos + intValue.intValue() + "|";
            }
            selectPos = selectPos.substring(0, selectPos.length() - 1);
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtils.isEmpty(mSelectPos)) {
                for (String pos : mSelectPos.split("\\|")) {
                    int index = Integer.parseInt(pos);
                    this.mSelectedView.add(Integer.valueOf(index));
                    ((TagView) getChildAt(index)).setChecked(true);
                }
            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private int findPosByView(View child) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            if (getChildAt(i) == child) {
                return i;
            }
        }
        return -1;
    }

    private TagView findChild(int x, int y) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView v = (TagView) getChildAt(i);
            if (v.getVisibility() != 8) {
                Rect outRect = new Rect();
                v.getHitRect(outRect);
                if (outRect.contains(x, y)) {
                    return v;
                }
            }
        }
        return null;
    }

    public void onChanged() {
        changeAdapter();
    }
}
