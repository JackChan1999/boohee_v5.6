package me.nereo.multi_image_selector;

import java.io.File;

public interface MultiImageSelectorFragment$Callback {
    void onCameraShot(File file);

    void onImageSelected(String str);

    void onImageUnselected(String str);

    void onSingleImageSelected(String str);
}
