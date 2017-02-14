package me.dm7.barcodescanner.core;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public abstract class BarcodeScannerView extends FrameLayout implements PreviewCallback {
    private Camera mCamera;
    private Rect mFramingRectInPreview;
    private CameraPreview mPreview;
    private IViewFinder mViewFinderView;

    public BarcodeScannerView(Context context) {
        super(context);
        setupLayout();
    }

    public BarcodeScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupLayout();
    }

    public final void setupLayout() {
        this.mPreview = new CameraPreview(getContext());
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setGravity(17);
        relativeLayout.setBackgroundColor(-16777216);
        relativeLayout.addView(this.mPreview);
        addView(relativeLayout);
        this.mViewFinderView = createViewFinderView(getContext());
        if (this.mViewFinderView instanceof View) {
            addView((View) this.mViewFinderView);
            return;
        }
        throw new IllegalArgumentException("IViewFinder object returned by 'createViewFinderView()' should be instance of android.view.View");
    }

    protected IViewFinder createViewFinderView(Context context) {
        return new ViewFinderView(context);
    }

    public void startCamera(int cameraId) {
        startCamera(CameraUtils.getCameraInstance(cameraId));
    }

    public void startCamera(Camera camera) {
        this.mCamera = camera;
        if (this.mCamera != null) {
            this.mViewFinderView.setupViewFinder();
            this.mPreview.setCamera(this.mCamera, this);
            this.mPreview.initCameraPreview();
            return;
        }
        Toast.makeText(getContext(), "请在应用设置中开启相机权限哦~", 1).show();
    }

    public void startCamera() {
        startCamera(CameraUtils.getCameraInstance());
    }

    public void stopCamera() {
        if (this.mCamera != null) {
            this.mPreview.stopCameraPreview();
            this.mPreview.setCamera(null, null);
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    public synchronized Rect getFramingRectInPreview(int previewWidth, int previewHeight) {
        Rect rect;
        if (this.mFramingRectInPreview == null) {
            Rect framingRect = this.mViewFinderView.getFramingRect();
            int viewFinderViewWidth = this.mViewFinderView.getWidth();
            int viewFinderViewHeight = this.mViewFinderView.getHeight();
            if (framingRect == null || viewFinderViewWidth == 0 || viewFinderViewHeight == 0) {
                rect = null;
            } else {
                Rect rect2 = new Rect(framingRect);
                rect2.left = (rect2.left * previewWidth) / viewFinderViewWidth;
                rect2.right = (rect2.right * previewWidth) / viewFinderViewWidth;
                rect2.top = (rect2.top * previewHeight) / viewFinderViewHeight;
                rect2.bottom = (rect2.bottom * previewHeight) / viewFinderViewHeight;
                this.mFramingRectInPreview = rect2;
            }
        }
        rect = this.mFramingRectInPreview;
        return rect;
    }

    public void setFlash(boolean flag) {
        if (this.mCamera != null && CameraUtils.isFlashSupported(this.mCamera)) {
            Parameters parameters = this.mCamera.getParameters();
            if (flag) {
                if (!parameters.getFlashMode().equals("torch")) {
                    parameters.setFlashMode("torch");
                } else {
                    return;
                }
            } else if (!parameters.getFlashMode().equals("off")) {
                parameters.setFlashMode("off");
            } else {
                return;
            }
            this.mCamera.setParameters(parameters);
        }
    }

    public boolean getFlash() {
        if (this.mCamera != null && CameraUtils.isFlashSupported(this.mCamera) && this.mCamera.getParameters().getFlashMode().equals("torch")) {
            return true;
        }
        return false;
    }

    public void toggleFlash() {
        if (this.mCamera != null && CameraUtils.isFlashSupported(this.mCamera)) {
            Parameters parameters = this.mCamera.getParameters();
            if (parameters.getFlashMode().equals("torch")) {
                parameters.setFlashMode("off");
            } else {
                parameters.setFlashMode("torch");
            }
            this.mCamera.setParameters(parameters);
        }
    }

    public void setAutoFocus(boolean state) {
        if (this.mPreview != null) {
            this.mPreview.setAutoFocus(state);
        }
    }
}
