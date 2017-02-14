package com.boohee.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boohee.model.FoodWithUnit;
import com.boohee.one.R;

import java.util.List;

public class SearchViewAdapter extends BaseAdapter {
    private List<FoodWithUnit> foods = null;
    private Context mContext;

    final class ViewHolder {
        public TextView calory;
        public TextView name;

        ViewHolder() {
        }
    }

    public SearchViewAdapter(Context context, List<FoodWithUnit> foods) {
        this.mContext = context;
        this.foods = foods;
    }

    public int getCount() {
        if (this.foods == null) {
            return 0;
        }
        return this.foods.size();
    }

    public Object getItem(int position) {
        return this.foods.get(position);
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
        holder.name.setText(((FoodWithUnit) this.foods.get(position)).name);
        String format = this.mContext.getResources().getString(R.string.m_);
        holder.calory.setText(String.format(format, new Object[]{Float.valueOf(((FoodWithUnit)
                this.foods.get(position)).calory)}));
        return convertView;
    }
}
