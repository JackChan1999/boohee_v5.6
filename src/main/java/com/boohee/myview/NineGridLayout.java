package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.boohee.model.status.Photo;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.ViewUtils;

import java.util.List;

import uk.co.senab.photoview.IPhotoView;

public class NineGridLayout extends ViewGroup {
    private final int                  ITEM_GAP;
    private       int                  columns;
    private       Context              context;
    private       int                  defaultHeight;
    private       int                  defaultWidth;
    private       int                  gap;
    private       boolean              isSmallScreen;
    private       List                 listData;
    private       OnItemClickListerner onItemClickListerner;
    private       int                  rows;
    int singleHeight;
    int singleWidth;
    private int totalWidth;

    public interface OnItemClickListerner {
        void onItemClick(View view, int i);
    }

    public NineGridLayout(Context context) {
        this(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        boolean z = false;
        super(context, attrs);
        this.ITEM_GAP = 3;
        this.singleWidth = 0;
        this.singleHeight = 0;
        this.isSmallScreen = false;
        this.context = context;
        this.gap = ViewUtils.dip2px(context, IPhotoView.DEFAULT_MAX_SCALE);
        if (DensityUtil.getDensity(context) < 2.0f) {
            z = true;
        }
        this.isSmallScreen = z;
        int dip2px = ViewUtils.dip2px(context, 140.0f);
        this.defaultHeight = dip2px;
        this.defaultWidth = dip2px;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.totalWidth = (sizeWidth - getPaddingLeft()) - getPaddingRight();
        if (this.listData != null && this.listData.size() > 0) {
            if (this.listData.size() != 1) {
                this.singleWidth = (this.totalWidth - (this.gap * 2)) / 3;
                this.singleHeight = this.singleWidth;
            } else if (this.isSmallScreen) {
                this.singleWidth = this.defaultWidth;
                this.singleHeight = this.defaultHeight;
            } else {
                this.singleWidth = ((Photo) this.listData.get(0)).preview_width;
                this.singleHeight = ((Photo) this.listData.get(0)).preview_height;
            }
            measureChildren(MeasureSpec.makeMeasureSpec(this.singleWidth, 1073741824),
                    MeasureSpec.makeMeasureSpec(this.singleHeight, 1073741824));
            int measureWidth = (this.singleWidth * this.columns) + (this.gap * (this.columns - 1));
            setMeasuredDimension(sizeWidth, (this.singleHeight * this.rows) + (this.gap * (this
                    .rows - 1)));
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    private void layoutChildrenView() {
        int childrenCount = this.listData.size();
        for (int i = 0; i < childrenCount; i++) {
            int[] position = findPosition(i);
            int left = ((this.singleWidth + this.gap) * position[1]) + getPaddingLeft();
            int top = ((this.singleHeight + this.gap) * position[0]) + getPaddingTop();
            int right = left + this.singleWidth;
            int bottom = top + this.singleHeight;
            NineGridImageView childrenView = (NineGridImageView) getChildAt(i);
            if (childrenCount == 1) {
                childrenView.setScaleType(ScaleType.FIT_CENTER);
            } else {
                childrenView.setScaleType(ScaleType.CENTER_CROP);
            }
            Photo photo = (Photo) this.listData.get(i);
            if (photo != null) {
                childrenView.setImageUrl(this.isSmallScreen ? photo.small_url : photo.middle_url);
            }
            final int itemPosition = i;
            childrenView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (NineGridLayout.this.onItemClickListerner != null) {
                        NineGridLayout.this.onItemClickListerner.onItemClick(v, itemPosition);
                    }
                }
            });
            childrenView.layout(left, top, right, bottom);
        }
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if ((this.columns * i) + j == childNum) {
                    position[0] = i;
                    position[1] = j;
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return this.gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setImagesData(List<Photo> lists) {
        if (lists != null && !lists.isEmpty()) {
            generateChildrenLayout(lists.size());
            int i;
            if (this.listData == null) {
                for (i = 0; i < lists.size(); i++) {
                    addView(generateImageView(i), generateDefaultLayoutParams());
                }
            } else {
                int oldViewCount = this.listData.size();
                int newViewCount = lists.size();
                if (oldViewCount > newViewCount) {
                    removeViews(newViewCount - 1, oldViewCount - newViewCount);
                } else if (oldViewCount < newViewCount) {
                    for (i = 0; i < newViewCount - oldViewCount; i++) {
                        addView(generateImageView(i), generateDefaultLayoutParams());
                    }
                }
            }
            this.listData = lists;
            requestLayout();
        }
    }

    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            this.rows = 1;
            this.columns = length;
        } else if (length <= 6) {
            this.rows = 2;
            this.columns = 3;
            if (length == 4) {
                this.columns = 2;
            }
        } else {
            this.rows = 3;
            this.columns = 3;
        }
    }

    private View generateImageView(int position) {
        NineGridImageView iv = new NineGridImageView(getContext());
        iv.setScaleType(ScaleType.CENTER_CROP);
        return iv;
    }

    public void setOnItemClickListerner(OnItemClickListerner onItemClickListerner) {
        this.onItemClickListerner = onItemClickListerner;
    }
}
