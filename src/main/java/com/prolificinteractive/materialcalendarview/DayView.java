package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.util.List;

@SuppressLint({"ViewConstructor"})
class DayView extends CheckedTextView {
    private CalendarDay date;
    private int         fadeTime;
    private DayFormatter formatter           = DayFormatter.DEFAULT;
    private boolean      isDecoratedDisabled = false;
    private boolean      isInRange           = true;
    private int          selectionColor      = -7829368;
    private Drawable selectionDrawable;
    private boolean showOtherDates = false;

    public DayView(Context context) {
        super(context);
        init();
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayView(Context context, CalendarDay day) {
        super(context);
        init();
        setDay(day);
    }

    private void init() {
        this.fadeTime = getResources().getInteger(17694720);
        setSelectionColor(this.selectionColor);
        setGravity(17);
        if (VERSION.SDK_INT >= 17) {
            setTextAlignment(4);
        }
    }

    public void setDay(CalendarDay date) {
        this.date = date;
        setText(getLabel());
    }

    public void setDayFormatter(DayFormatter formatter) {
        if (formatter == null) {
            formatter = DayFormatter.DEFAULT;
        }
        this.formatter = formatter;
        CharSequence currentLabel = getText();
        Object[] spans = null;
        if (currentLabel instanceof Spanned) {
            spans = ((Spanned) currentLabel).getSpans(0, currentLabel.length(), Object.class);
        }
        SpannableString newLabel = new SpannableString(getLabel());
        if (spans != null) {
            for (Object span : spans) {
                newLabel.setSpan(span, 0, newLabel.length(), 33);
            }
        }
        setText(newLabel);
    }

    @NonNull
    public String getLabel() {
        return this.formatter.format(this.date);
    }

    public void setSelectionColor(int color) {
        this.selectionColor = color;
        regenerateBackground();
    }

    public void setSelectionDrawable(Drawable selectionDrawable) {
        this.selectionDrawable = selectionDrawable;
        invalidate();
    }

    public CalendarDay getDate() {
        return this.date;
    }

    private void setEnabled() {
        boolean z;
        int i = 0;
        if (!this.isInRange || this.isDecoratedDisabled) {
            z = false;
        } else {
            z = true;
        }
        super.setEnabled(z);
        if (!(this.isInRange || this.showOtherDates)) {
            i = 4;
        }
        setVisibility(i);
    }

    protected void setupSelection(boolean showOtherDates, boolean inRange, boolean inMonth) {
        this.showOtherDates = showOtherDates;
        boolean z = inMonth && inRange;
        this.isInRange = z;
        setEnabled();
    }

    private void regenerateBackground() {
        if (this.selectionDrawable != null) {
            setBackgroundDrawable(this.selectionDrawable);
        } else {
            setBackgroundDrawable(generateBackground(this.selectionColor, this.fadeTime));
        }
    }

    private static Drawable generateBackground(int color, int fadeTime) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(fadeTime);
        drawable.addState(new int[]{16842912}, generateCircleDrawable(color));
        if (VERSION.SDK_INT >= 21) {
            drawable.addState(new int[]{16842919}, generateRippleDrawable(color));
        } else {
            drawable.addState(new int[]{16842919}, generateCircleDrawable(color));
        }
        drawable.addState(new int[0], generateCircleDrawable(0));
        return drawable;
    }

    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setShaderFactory(new ShaderFactory() {
            public Shader resize(int width, int height) {
                return new LinearGradient(0.0f, 0.0f, 0.0f, 0.0f, color, color, TileMode.REPEAT);
            }
        });
        return drawable;
    }

    @TargetApi(21)
    private static Drawable generateRippleDrawable(int color) {
        return new RippleDrawable(ColorStateList.valueOf(color), null, generateCircleDrawable(-1));
    }

    void applyFacade(DayViewFacade facade) {
        this.isDecoratedDisabled = facade.areDaysDisabled();
        setEnabled();
        setSelectionDrawable(facade.getSelectionDrawable());
        List<Span> spans = facade.getSpans();
        if (spans.isEmpty()) {
            setText(getLabel());
            return;
        }
        String label = getLabel();
        SpannableString formattedLabel = new SpannableString(getLabel());
        for (Span span : spans) {
            formattedLabel.setSpan(span.span, 0, label.length(), 33);
        }
        setText(formattedLabel);
    }
}
