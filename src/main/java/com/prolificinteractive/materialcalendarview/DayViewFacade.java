package com.prolificinteractive.materialcalendarview;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class DayViewFacade {
    private       Drawable         backgroundDrawable = null;
    private       boolean          daysDisabled       = false;
    private       boolean          isDecorated        = false;
    private       Drawable         selectionDrawable  = null;
    private final LinkedList<Span> spans              = new LinkedList();

    protected static class Span {
        final Object span;

        public Span(Object span) {
            this.span = span;
        }
    }

    public void setBackgroundDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        this.backgroundDrawable = drawable;
        this.isDecorated = true;
    }

    public void setSelectionDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        this.selectionDrawable = drawable;
        this.isDecorated = true;
    }

    public void addSpan(@NonNull Object span) {
        if (this.spans != null) {
            this.spans.add(new Span(span));
            this.isDecorated = true;
        }
    }

    public void setDaysDisabled(boolean daysDisabled) {
        this.daysDisabled = daysDisabled;
        this.isDecorated = true;
    }

    protected void reset() {
        this.backgroundDrawable = null;
        this.selectionDrawable = null;
        this.spans.clear();
        this.isDecorated = false;
        this.daysDisabled = false;
    }

    protected void applyTo(DayViewFacade other) {
        if (this.selectionDrawable != null) {
            other.setSelectionDrawable(this.selectionDrawable);
        }
        if (this.backgroundDrawable != null) {
            other.setBackgroundDrawable(this.backgroundDrawable);
        }
        other.spans.addAll(this.spans);
        other.isDecorated |= this.isDecorated;
        other.daysDisabled = this.daysDisabled;
    }

    protected boolean isDecorated() {
        return this.isDecorated;
    }

    protected Drawable getSelectionDrawable() {
        return this.selectionDrawable;
    }

    protected Drawable getBackgroundDrawable() {
        return this.backgroundDrawable;
    }

    protected List<Span> getSpans() {
        return Collections.unmodifiableList(this.spans);
    }

    public boolean areDaysDisabled() {
        return this.daysDisabled;
    }
}
