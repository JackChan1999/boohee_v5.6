package com.boohee.myview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements OnItemTouchListener {
    private View childView;
    GestureDetector mGestureDetector;
    private RecyclerView touchView;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);

        void onLongClick(View view, int i);
    }

    public RecyclerItemClickListener(Context context, final OnItemClickListener mListener) {
        this.mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent ev) {
                if (!(RecyclerItemClickListener.this.childView == null || mListener == null)) {
                    mListener.onItemClick(RecyclerItemClickListener.this.childView,
                            RecyclerItemClickListener.this.touchView.getChildPosition
                                    (RecyclerItemClickListener.this.childView));
                }
                return true;
            }

            public void onLongPress(MotionEvent ev) {
                if (RecyclerItemClickListener.this.childView != null && mListener != null) {
                    mListener.onLongClick(RecyclerItemClickListener.this.childView,
                            RecyclerItemClickListener.this.touchView.getChildPosition
                                    (RecyclerItemClickListener.this.childView));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        this.mGestureDetector.onTouchEvent(motionEvent);
        this.childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        this.touchView = recyclerView;
        return false;
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
