package uk.co.senab.photoview;

import android.content.Context;
import android.graphics.RectF;
import android.widget.ImageView;
import uk.co.senab.photoview.log.LogManager;
import uk.co.senab.photoview.scrollerproxy.ScrollerProxy;

class PhotoViewAttacher$FlingRunnable implements Runnable {
    private int mCurrentX;
    private int mCurrentY;
    private final ScrollerProxy mScroller;
    final /* synthetic */ PhotoViewAttacher this$0;

    public PhotoViewAttacher$FlingRunnable(PhotoViewAttacher photoViewAttacher, Context context) {
        this.this$0 = photoViewAttacher;
        this.mScroller = ScrollerProxy.getScroller(context);
    }

    public void cancelFling() {
        if (PhotoViewAttacher.access$300()) {
            LogManager.getLogger().d("PhotoViewAttacher", "Cancel Fling");
        }
        this.mScroller.forceFinished(true);
    }

    public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
        RectF rect = this.this$0.getDisplayRect();
        if (rect != null) {
            int minX;
            int maxX;
            int minY;
            int maxY;
            int startX = Math.round(-rect.left);
            if (((float) viewWidth) < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - ((float) viewWidth));
            } else {
                maxX = startX;
                minX = startX;
            }
            int startY = Math.round(-rect.top);
            if (((float) viewHeight) < rect.height()) {
                minY = 0;
                maxY = Math.round(rect.height() - ((float) viewHeight));
            } else {
                maxY = startY;
                minY = startY;
            }
            this.mCurrentX = startX;
            this.mCurrentY = startY;
            if (PhotoViewAttacher.access$300()) {
                LogManager.getLogger().d("PhotoViewAttacher", "fling. StartX:" + startX + " StartY:" + startY + " MaxX:" + maxX + " MaxY:" + maxY);
            }
            if (startX != maxX || startY != maxY) {
                this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
            }
        }
    }

    public void run() {
        if (!this.mScroller.isFinished()) {
            ImageView imageView = this.this$0.getImageView();
            if (imageView != null && this.mScroller.computeScrollOffset()) {
                int newX = this.mScroller.getCurrX();
                int newY = this.mScroller.getCurrY();
                if (PhotoViewAttacher.access$300()) {
                    LogManager.getLogger().d("PhotoViewAttacher", "fling run(). CurrentX:" + this.mCurrentX + " CurrentY:" + this.mCurrentY + " NewX:" + newX + " NewY:" + newY);
                }
                PhotoViewAttacher.access$100(this.this$0).postTranslate((float) (this.mCurrentX - newX), (float) (this.mCurrentY - newY));
                PhotoViewAttacher.access$400(this.this$0, this.this$0.getDrawMatrix());
                this.mCurrentX = newX;
                this.mCurrentY = newY;
                Compat.postOnAnimation(imageView, this);
            }
        }
    }
}
