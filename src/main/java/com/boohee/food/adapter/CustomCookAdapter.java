package com.boohee.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.CustomCookItem;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CustomCookAdapter extends SimpleBaseAdapter<CustomCookItem> {
    private boolean       isEdit;
    private List<Boolean> mDataSelect;

    public CustomCookAdapter(Context context, List<CustomCookItem> data, List<Boolean> dataSelect) {
        super(context, data);
        this.mDataSelect = dataSelect;
    }

    public int getItemResource() {
        return R.layout.hu;
    }

    public View getItemView(final int position, View convertView, ViewHolder holder) {
        CustomCookItem cook = (CustomCookItem) getItem(position);
        if (cook != null) {
            ImageView civIcon = (CircleImageView) holder.getView(R.id.civ_icon);
            if (!TextUtils.isEmpty(cook.photo)) {
                String imgUrl;
                if (cook.photo.contains(TimeLinePatterns.WEB_SCHEME)) {
                    imgUrl = cook.photo;
                } else {
                    imgUrl = TimeLinePatterns.WEB_SCHEME + cook.photo;
                }
                ImageLoader.getInstance().displayImage(imgUrl, civIcon, ImageLoaderOptions.global
                        ((int) R.drawable.aa2));
            }
            ((TextView) holder.getView(R.id.tv_name)).setText(cook.name);
            ((TextView) holder.getView(R.id.tv_calory)).setText(String.format("%.1f 千卡/份", new
                    Object[]{Float.valueOf(cook.calory)}));
            CheckBox cbSelect = (CheckBox) holder.getView(R.id.cb_select);
            if (this.isEdit) {
                cbSelect.setVisibility(0);
                cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (position < CustomCookAdapter.this.mDataSelect.size()) {
                            CustomCookAdapter.this.mDataSelect.set(position, Boolean.valueOf
                                    (isChecked));
                        }
                    }
                });
            } else {
                cbSelect.setVisibility(8);
            }
            cbSelect.setChecked(((Boolean) this.mDataSelect.get(position)).booleanValue());
        }
        return convertView;
    }

    public void setEdit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }
}
