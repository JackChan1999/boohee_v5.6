package uk.co.senab.photoview;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

class PhotoViewAttacher$1 extends SimpleOnGestureListener {
    final /* synthetic */ PhotoViewAttacher this$0;

    PhotoViewAttacher$1(PhotoViewAttacher photoViewAttacher) {
        this.this$0 = photoViewAttacher;
    }

    public void onLongPress(MotionEvent e) {
        if (PhotoViewAttacher.access$000(this.this$0) != null) {
            PhotoViewAttacher.access$000(this.this$0).onLongClick(this.this$0.getImageView());
        }
    }
}
