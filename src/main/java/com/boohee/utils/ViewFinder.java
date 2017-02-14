package com.boohee.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewFinder {
    private final FindWrapper wrapper;

    private interface FindWrapper {
        View findViewById(int i);

        Resources getResources();
    }

    private static class ViewWrapper implements FindWrapper {
        private final View view;

        ViewWrapper(View view) {
            this.view = view;
        }

        public View findViewById(int id) {
            return this.view.findViewById(id);
        }

        public Resources getResources() {
            return this.view.getResources();
        }
    }

    private static class WindowWrapper implements FindWrapper {
        private final Window window;

        WindowWrapper(Window window) {
            this.window = window;
        }

        public View findViewById(int id) {
            return this.window.findViewById(id);
        }

        public Resources getResources() {
            return this.window.getContext().getResources();
        }
    }

    public ViewFinder(View view) {
        this.wrapper = new ViewWrapper(view);
    }

    public ViewFinder(Window window) {
        this.wrapper = new WindowWrapper(window);
    }

    public ViewFinder(Activity activity) {
        this(activity.getWindow());
    }

    public <V extends View> V find(int id) {
        return this.wrapper.findViewById(id);
    }

    public ImageView imageView(int id) {
        return (ImageView) find(id);
    }

    public CompoundButton compoundButton(int id) {
        return (CompoundButton) find(id);
    }

    public TextView textView(int id) {
        return (TextView) find(id);
    }

    public TextView setText(int id, CharSequence content) {
        TextView text = (TextView) find(id);
        text.setText(content);
        return text;
    }

    public TextView setText(int id, int content) {
        return setText(id, this.wrapper.getResources().getString(content));
    }

    public View onClick(int id, OnClickListener listener) {
        View clickable = find(id);
        clickable.setOnClickListener(listener);
        return clickable;
    }

    public View onClick(int id, final Runnable runnable) {
        return onClick(id, new OnClickListener() {
            public void onClick(View v) {
                runnable.run();
            }
        });
    }

    public void onClick(OnClickListener listener, int... ids) {
        for (int id : ids) {
            find(id).setOnClickListener(listener);
        }
    }

    public void onClick(final Runnable runnable, int... ids) {
        onClick(new OnClickListener() {
            public void onClick(View v) {
                runnable.run();
            }
        }, ids);
    }

    public ImageView setDrawable(int id, int drawable) {
        ImageView image = imageView(id);
        image.setImageDrawable(image.getResources().getDrawable(drawable));
        return image;
    }

    public CompoundButton onCheck(int id, OnCheckedChangeListener listener) {
        CompoundButton checkable = (CompoundButton) find(id);
        checkable.setOnCheckedChangeListener(listener);
        return checkable;
    }

    public CompoundButton onCheck(int id, final Runnable runnable) {
        return onCheck(id, new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                runnable.run();
            }
        });
    }

    public void onCheck(OnCheckedChangeListener listener, int... ids) {
        for (int id : ids) {
            compoundButton(id).setOnCheckedChangeListener(listener);
        }
    }

    public void onCheck(final Runnable runnable, int... ids) {
        onCheck(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                runnable.run();
            }
        }, ids);
    }
}
