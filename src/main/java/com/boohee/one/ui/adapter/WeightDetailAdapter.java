package com.boohee.one.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.mine.WeightRecord;
import com.boohee.model.scale.BmrIndex;
import com.boohee.model.scale.BodyfatIndex;
import com.boohee.model.scale.BoneIndex;
import com.boohee.model.scale.FakeIndex;
import com.boohee.model.scale.ProteinIndex;
import com.boohee.model.scale.ScaleIndex;
import com.boohee.model.scale.SkeletalMuscleIndex;
import com.boohee.model.scale.SubfatIndex;
import com.boohee.model.scale.VisfatIndex;
import com.boohee.model.scale.WaterIndex;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.record.ScaleIndexActivity;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class WeightDetailAdapter extends BaseAdapter {
    private final List<ScaleIndex> EMPTY_LIST = new ArrayList();
    private Context            context;
    private List<ScaleIndex>   list;
    private BaseDialogFragment mFragment;
    private WeightRecord       mRecord;
    private User               mUser;
    private float[]            outerR;

    public WeightDetailAdapter(Context context, BaseDialogFragment fragment) {
        this.context = context;
        this.mFragment = fragment;
        this.mUser = new UserDao(context).queryWithToken(UserPreference.getToken(context));
        this.EMPTY_LIST.add(new FakeIndex("脂肪率", 0.0f, "%"));
        this.EMPTY_LIST.add(new FakeIndex("内脏脂肪", 0.0f, "级"));
        this.EMPTY_LIST.add(new FakeIndex("皮下脂肪", 0.0f, "%"));
        this.EMPTY_LIST.add(new FakeIndex("基础代谢", 0.0f, "千卡"));
        this.EMPTY_LIST.add(new FakeIndex("体水分", 0.0f, "%"));
        this.EMPTY_LIST.add(new FakeIndex("骨骼肌率", 0.0f, "%"));
        this.EMPTY_LIST.add(new FakeIndex("骨量", 0.0f, "kg"));
        this.EMPTY_LIST.add(new FakeIndex("蛋白质", 0.0f, "%"));
        float radius = (float) DensityUtil.dip2px(context, 10.0f);
        this.outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
    }

    public void setData(WeightRecord record) {
        this.mRecord = record;
        if (record != null) {
            this.list = new ArrayList();
            int sex = NumberUtils.safeParseInt(this.mUser.sex_type);
            int age = this.mUser.getAge();
            float weight = NumberUtils.safeParseFloat(record.weight);
            this.list.add(new BodyfatIndex(NumberUtils.safeParseFloat(record.bodyfat), sex));
            this.list.add(new VisfatIndex(NumberUtils.safeParseFloat(record.visfat)));
            this.list.add(new SubfatIndex(NumberUtils.safeParseFloat(record.subfat), sex));
            this.list.add(new BmrIndex(NumberUtils.safeParseFloat(record.bmr), sex, age));
            this.list.add(new WaterIndex(NumberUtils.safeParseFloat(record.water), sex));
            this.list.add(new SkeletalMuscleIndex(NumberUtils.safeParseFloat(record.muscle), sex));
            this.list.add(new BoneIndex(NumberUtils.safeParseFloat(record.bone), sex, weight));
            this.list.add(new ProteinIndex(NumberUtils.safeParseFloat(record.protein), sex));
            notifyDataSetChanged();
        } else if (this.list != this.EMPTY_LIST) {
            this.list = this.EMPTY_LIST;
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return this.list == null ? 0 : this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.jm, parent, false);
        }
        final ScaleIndex itemData = (ScaleIndex) getItem(position);
        TextView amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView level = (TextView) convertView.findViewById(R.id.tv_level);
        ((TextView) convertView.findViewById(R.id.tv_name)).setText(itemData.getName());
        amount.setText(itemData.getValueWithUnit());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ViewCompat.MEASURED_SIZE_MASK);
        drawable.setCornerRadius(this.outerR[0]);
        drawable.setStroke(1, itemData.getColor());
        level.setBackgroundDrawable(drawable);
        level.setTextColor(itemData.getColor());
        level.setText(itemData.getLevelName());
        if (itemData instanceof FakeIndex) {
            convertView.setOnClickListener(null);
        } else {
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WeightDetailAdapter.this.mFragment.getDialog().getWindow()
                            .setWindowAnimations(R.style.df);
                    ScaleIndexActivity.startActivity(WeightDetailAdapter.this.context,
                            WeightDetailAdapter.this.mRecord, itemData.getName());
                }
            });
        }
        return convertView;
    }
}
