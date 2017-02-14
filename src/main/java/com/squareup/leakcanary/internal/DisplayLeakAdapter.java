package com.squareup.leakcanary.internal;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.leakcanary.LeakTrace;
import com.squareup.leakcanary.LeakTraceElement;
import com.squareup.leakcanary.LeakTraceElement.Holder;
import com.squareup.leakcanary.R;
import com.squareup.leakcanary.internal.DisplayLeakConnectorView.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class DisplayLeakAdapter extends BaseAdapter {
    private static final int                    NORMAL_ROW = 1;
    private static final int                    TOP_ROW    = 0;
    private              List<LeakTraceElement> elements   = Collections.emptyList();
    private              boolean[]              opened     = new boolean[0];
    private String referenceKey;
    private String referenceName = "";

    DisplayLeakAdapter() {
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout
                        .__leak_canary_ref_top_row, parent, false);
            }
            ((TextView) findById(convertView, R.id.__leak_canary_row_text)).setText(context
                    .getPackageName());
        } else {
            boolean isRoot;
            boolean isLeakingInstance;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout
                        .__leak_canary_ref_row, parent, false);
            }
            TextView textView = (TextView) findById(convertView, R.id.__leak_canary_row_text);
            if (position == 1) {
                isRoot = true;
            } else {
                isRoot = false;
            }
            if (position == getCount() - 1) {
                isLeakingInstance = true;
            } else {
                isLeakingInstance = false;
            }
            String htmlString = elementToHtmlString(getItem(position), isRoot, this
                    .opened[position]);
            if (isLeakingInstance && !this.referenceName.equals("") && this.opened[position]) {
                htmlString = htmlString + " <font color='#919191'>" + this.referenceName +
                        "</font>";
            }
            textView.setText(Html.fromHtml(htmlString));
            DisplayLeakConnectorView connector = (DisplayLeakConnectorView) findById(convertView,
                    R.id.__leak_canary_row_connector);
            if (isRoot) {
                connector.setType(Type.START);
            } else if (isLeakingInstance) {
                connector.setType(Type.END);
            } else {
                connector.setType(Type.NODE);
            }
            ((MoreDetailsView) findById(convertView, R.id.__leak_canary_row_more)).setOpened(this
                    .opened[position]);
        }
        return convertView;
    }

    private String elementToHtmlString(LeakTraceElement element, boolean root, boolean opened) {
        String qualifier;
        String simpleName;
        String htmlString = "";
        if (element.referenceName == null) {
            htmlString = htmlString + "leaks ";
        } else if (!root) {
            htmlString = htmlString + "references ";
        }
        if (element.type == LeakTraceElement.Type.STATIC_FIELD) {
            htmlString = htmlString + "<font color='#c48a47'>static</font> ";
        }
        if (element.holder == Holder.ARRAY || element.holder == Holder.THREAD) {
            htmlString = htmlString + "<font color='#f3cf83'>" + element.holder.name()
                    .toLowerCase() + "</font> ";
        }
        int separator = element.className.lastIndexOf(46);
        if (separator == -1) {
            qualifier = "";
            simpleName = element.className;
        } else {
            qualifier = element.className.substring(0, separator + 1);
            simpleName = element.className.substring(separator + 1);
        }
        if (opened) {
            htmlString = htmlString + "<font color='#919191'>" + qualifier + "</font>";
        }
        htmlString = htmlString + ("<font color='#ffffff'>" + simpleName + "</font>");
        if (element.referenceName != null) {
            htmlString = htmlString + ".<font color='#998bb5'>" + element.referenceName
                    .replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</font>";
        } else {
            htmlString = htmlString + " <font color='#f3cf83'>instance</font>";
        }
        if (!opened || element.extra == null) {
            return htmlString;
        }
        return htmlString + " <font color='#919191'>" + element.extra + "</font>";
    }

    public void update(LeakTrace leakTrace, String referenceKey, String referenceName) {
        if (!referenceKey.equals(this.referenceKey)) {
            this.referenceKey = referenceKey;
            this.referenceName = referenceName;
            this.elements = new ArrayList(leakTrace.elements);
            this.opened = new boolean[(this.elements.size() + 1)];
            notifyDataSetChanged();
        }
    }

    public void toggleRow(int position) {
        this.opened[position] = !this.opened[position];
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.elements.size() + 1;
    }

    public LeakTraceElement getItem(int position) {
        if (getItemViewType(position) == 0) {
            return null;
        }
        return (LeakTraceElement) this.elements.get(position - 1);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    private static <T extends View> T findById(View view, int id) {
        return view.findViewById(id);
    }
}
