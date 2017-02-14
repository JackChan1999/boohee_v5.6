package android.support.v7.widget;

import android.content.Context;

interface CardViewImpl {
    float getElevation(CardViewDelegate cardViewDelegate);

    float getMaxElevation(CardViewDelegate cardViewDelegate);

    float getMinHeight(CardViewDelegate cardViewDelegate);

    float getMinWidth(CardViewDelegate cardViewDelegate);

    float getRadius(CardViewDelegate cardViewDelegate);

    void initStatic();

    void initialize(CardViewDelegate cardViewDelegate, Context context, int i, float f, float f2, float f3);

    void onCompatPaddingChanged(CardViewDelegate cardViewDelegate);

    void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate);

    void setBackgroundColor(CardViewDelegate cardViewDelegate, int i);

    void setElevation(CardViewDelegate cardViewDelegate, float f);

    void setMaxElevation(CardViewDelegate cardViewDelegate, float f);

    void setRadius(CardViewDelegate cardViewDelegate, float f);

    void updatePadding(CardViewDelegate cardViewDelegate);
}
