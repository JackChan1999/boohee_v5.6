package com.boohee.one.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.boohee.one.R;

import java.util.List;
import java.util.Map;

public class HelpInfoAdapter extends BaseExpandableListAdapter {
    private LayoutInflater            li;
    private Map<String, List<String>> mChildDatas;
    private List<String>              mHeaderDatas;

    public HelpInfoAdapter(Context context, List<String> headerDatas, Map<String, List<String>>
            childDatas) {
        this.li = LayoutInflater.from(context);
        this.mHeaderDatas = headerDatas;
        this.mChildDatas = childDatas;
    }

    public int getGroupCount() {
        return this.mHeaderDatas.size();
    }

    public int getChildrenCount(int groupPosition) {
        return ((List) this.mChildDatas.get(this.mHeaderDatas.get(groupPosition))).size();
    }

    public Object getGroup(int groupPosition) {
        return this.mHeaderDatas.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ((List) this.mChildDatas.get(this.mHeaderDatas.get(groupPosition))).get
                (childPosition);
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        TextView headerText;
        if (convertView == null) {
            convertView = this.li.inflate(R.layout.bk, null);
            headerText = (TextView) convertView.findViewById(R.id.help_info_list_group);
            convertView.setTag(headerText);
        } else {
            headerText = (TextView) convertView.getTag();
        }
        headerText.setText((CharSequence) this.mHeaderDatas.get(groupPosition));
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        TextView childTextView;
        if (convertView == null) {
            convertView = this.li.inflate(R.layout.bj, null);
            childTextView = (TextView) convertView.findViewById(R.id.help_info_list_child_item);
            convertView.setTag(childTextView);
        } else {
            childTextView = (TextView) convertView.getTag();
        }
        childTextView.setText((CharSequence) ((List) this.mChildDatas.get(this.mHeaderDatas.get
                (groupPosition))).get(childPosition));
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
