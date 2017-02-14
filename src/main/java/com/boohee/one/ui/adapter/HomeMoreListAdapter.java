package com.boohee.one.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.Event;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class HomeMoreListAdapter extends SimpleBaseAdapter<Event> {
    public HomeMoreListAdapter(Context context, List<Event> data) {
        super(context, data);
    }

    public int getItemResource() {
        return R.layout.k7;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        if (this.data.size() != 0) {
            Event event = (Event) this.data.get(position);
            ImageLoader.getInstance().displayImage(event.pic_url, (ImageView) holder.getView(R.id
                    .main_group_iv));
            ((TextView) holder.getView(R.id.main_group_item_name_tv)).setText(event.title);
            ((TextView) holder.getView(R.id.main_group_item_title_tv)).setText(event.desc);
            if (position == this.data.size() - 1) {
                View bottomLine = holder.getView(R.id.v_bottom_line);
                if (bottomLine != null) {
                    bottomLine.setVisibility(0);
                }
            }
        }
        return convertView;
    }
}
