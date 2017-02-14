package me.dm7.barcodescanner.core;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import java.util.List;

public class CameraUtils {
    public static Camera getCameraInstance() {
        return getCameraInstance(-1);
    }

    public static Camera getCameraInstance(int cameraId) {
        if (cameraId != -1) {
            return Camera.open(cameraId);
        }
        try {
            return Camera.open();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isFlashSupported(Camera camera) {
        if (camera == null) {
            return false;
        }
        Parameters parameters = camera.getParameters();
        if (parameters.getFlashMode() == null) {
            return false;
        }
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || (supportedFlashModes.size() == 1 && ((String) supportedFlashModes.get(0)).equals("off"))) {
            return false;
        }
        return true;
    }
}
