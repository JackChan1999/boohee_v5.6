package com.boohee.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boohee.model.Sport;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;

import java.util.List;

public class SearchSportViewAdapter extends BaseAdapter {
    private float   lastestWeight;
    private Context mContext;
    private List<Sport> sports = null;

    final class ViewHolder {
        public TextView calory;
        public TextView name;

        ViewHolder() {
        }
    }

    public SearchSportViewAdapter(Context context, List<Sport> sports) {
        this.mContext = context;
        this.sports = sports;
        this.lastestWeight = getLastestWeight();
    }

    private float getLastestWeight() {
        return new WeightRecordDao(this.mContext).getLastestWeight();
    }

    public int getCount() {
        if (this.sports == null) {
            return 0;
        }
        return this.sports.size();
    }

    public Object getItem(int position) {
        return this.sports.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.m1, null);
            holder.name = (TextView) convertView.findViewById(R.id.lName);
            holder.calory = (TextView) convertView.findViewById(R.id.lCalory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(((Sport) this.sports.get(position)).name);
        holder.calory.setText(((Sport) this.sports.get(position)).calcCalory(this.lastestWeight)
                + " 千卡/60分钟");
        return convertView;
    }
}
