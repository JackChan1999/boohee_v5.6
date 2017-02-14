package android.support.v7.widget;

import android.content.Context;
import android.view.View;

class CardViewApi21 implements CardViewImpl {
    CardViewApi21() {
    }

    public void initialize(CardViewDelegate cardView, Context context, int backgroundColor, float radius, float elevation, float maxElevation) {
        cardView.setBackgroundDrawable(new RoundRectDrawable(backgroundColor, radius));
        View view = (View) cardView;
        view.setClipToOutline(true);
        view.setElevation(elevation);
        setMaxElevation(cardView, maxElevation);
    }

    public void setRadius(CardViewDelegate cardView, float radius) {
        ((RoundRectDrawable) cardView.getBackground()).setRadius(radius);
    }

    public void initStatic() {
    }

    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        ((RoundRectDrawable) cardView.getBackground()).setPadding(maxElevation, cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    public float getMaxElevation(CardViewDelegate cardView) {
        return ((RoundRectDrawable) cardView.getBackground()).getPadding();
    }

    public float getMinWidth(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    public float getMinHeight(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    public float getRadius(CardViewDelegate cardView) {
        return ((RoundRectDrawable) cardView.getBackground()).getRadius();
    }

    public void setElevation(CardViewDelegate cardView, float elevation) {
        ((View) cardView).setElevation(elevation);
    }

    public float getElevation(CardViewDelegate cardView) {
        return ((View) cardView).getElevation();
    }

    public void updatePadding(CardViewDelegate cardView) {
        if (cardView.getUseCompatPadding()) {
            float elevation = getMaxElevation(cardView);
            float radius = getRadius(cardView);
            int hPadding = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateHorizontalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
            int vPadding = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateVerticalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
            cardView.setShadowPadding(hPadding, vPadding, hPadding, vPadding);
            return;
        }
        cardView.setShadowPadding(0, 0, 0, 0);
    }

    public void onCompatPaddingChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    public void setBackgroundColor(CardViewDelegate cardView, int color) {
        ((RoundRectDrawable) cardView.getBackground()).setColor(color);
    }
}
