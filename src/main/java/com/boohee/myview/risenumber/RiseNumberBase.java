package com.boohee.myview.risenumber;

import com.boohee.myview.risenumber.RiseNumberTextView.EndListener;

public interface RiseNumberBase {
    RiseNumberTextView setDuration(long j);

    void setOnEnd(EndListener endListener);

    void start();

    RiseNumberTextView withNumber(float f);

    RiseNumberTextView withNumber(int i);
}
