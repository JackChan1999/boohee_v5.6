package me.dm7.barcodescanner.core;

import android.graphics.Rect;

public interface IViewFinder {
    Rect getFramingRect();

    int getHeight();

    int getWidth();

    void setupViewFinder();
}
