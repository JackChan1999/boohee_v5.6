package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.mine.WeightRecord;
import com.boohee.model.scale.BmiIndex;
import com.boohee.model.scale.BmrIndex;
import com.boohee.model.scale.BodyfatIndex;
import com.boohee.model.scale.BoneIndex;
import com.boohee.model.scale.ProteinIndex;
import com.boohee.model.scale.ScaleIndex;
import com.boohee.model.scale.SkeletalMuscleIndex;
import com.boohee.model.scale.SubfatIndex;
import com.boohee.model.scale.VisfatIndex;
import com.boohee.model.scale.WaterIndex;
import com.boohee.model.scale.WeightIndex;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.ScaleIndexView;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.ui.BaseActivity;
import com.boohee.utils.ArithmeticUtil;
import com.boohee.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ScaleIndexActivity extends BaseActivity {
    public static final String INDEX_NAME = "index_name";
    public static final String RECORD     = "record";
    private List<ScaleIndex> list;
    private String           mIndexName;
    private WeightRecord     mRecord;
    @InjectView(2131427859)
    RecyclerView recyclerView;

    private class IndexItem implements AdapterItem<ScaleIndex> {
        private ScaleIndexView indexView;
        private TextView       tvDes;
        private TextView       tvName;

        private IndexItem() {
        }

        public int getLayoutResId() {
            return R.layout.iq;
        }

        public void bindViews(View view) {
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvDes = (TextView) view.findViewById(R.id.tv_des);
            this.indexView = (ScaleIndexView) view.findViewById(R.id.index);
        }

        public void setViews() {
        }

        public void handleData(ScaleIndex scaleIndex, int i) {
            this.tvName.setText(scaleIndex.getName());
            this.indexView.setIndex(scaleIndex);
            this.tvDes.setText(MyApplication.getContext().getString(scaleIndex.getDes()));
        }
    }

    public static void startActivity(Context context, WeightRecord record, String tag) {
        Intent i = new Intent(context, ScaleIndexActivity.class);
        i.putExtra("record", record);
        i.putExtra(INDEX_NAME, tag);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cr);
        ButterKnife.inject((Activity) this);
        initParams();
        initViews();
    }

    private void initParams() {
        this.mRecord = (WeightRecord) getIntent().getSerializableExtra("record");
        this.mIndexName = getIntent().getStringExtra(INDEX_NAME);
        User mUser = new UserDao(this).queryWithToken(UserPreference.getToken(this));
        int sex = NumberUtils.safeParseInt(mUser.sex_type);
        int age = mUser.getAge();
        int height = (int) mUser.height;
        this.list = new ArrayList();
        float weight = NumberUtils.safeParseFloat(this.mRecord.weight);
        float bmi = (float) ArithmeticUtil.roundOff((double) ((10000.0f * weight) / ((float)
                (height * height))), 1);
        this.list.add(new WeightIndex(height, weight));
        this.list.add(new BmiIndex(bmi));
        this.list.add(new BodyfatIndex(NumberUtils.safeParseFloat(this.mRecord.bodyfat), sex));
        this.list.add(new VisfatIndex(NumberUtils.safeParseFloat(this.mRecord.visfat)));
        this.list.add(new SubfatIndex(NumberUtils.safeParseFloat(this.mRecord.subfat), sex));
        this.list.add(new BmrIndex(NumberUtils.safeParseFloat(this.mRecord.bmr), sex, age));
        this.list.add(new WaterIndex(NumberUtils.safeParseFloat(this.mRecord.water), sex));
        this.list.add(new SkeletalMuscleIndex(NumberUtils.safeParseFloat(this.mRecord.muscle),
                sex));
        this.list.add(new BoneIndex(NumberUtils.safeParseFloat(this.mRecord.bone), sex, weight));
        this.list.add(new ProteinIndex(NumberUtils.safeParseFloat(this.mRecord.protein), sex));
    }

    private void initViews() {
        this.recyclerView.setAdapter(new CommonRcvAdapter<ScaleIndex>(this.list) {
            @NonNull
            public AdapterItem createItem(Object o) {
                return new IndexItem();
            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int index = 0;
        for (int i = 0; i < this.list.size(); i++) {
            if (((ScaleIndex) this.list.get(i)).getName().equals(this.mIndexName)) {
                index = i;
            }
        }
        this.recyclerView.scrollToPosition(index);
    }
}
