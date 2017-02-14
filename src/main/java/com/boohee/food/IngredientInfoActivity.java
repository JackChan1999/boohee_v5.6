package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alibaba.fastjson.JSON;
import com.boohee.main.GestureActivity;
import com.boohee.model.IngredientInfo;
import com.boohee.one.R;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class IngredientInfoActivity extends GestureActivity {
    private static final String KEY_INGREDIENT_INFO = "key_ingredient_info";
    @InjectView(2131427670)
    ListView lvMain;
    private IngredientInfoAdapter mAdapter;
    private List<IngredientInfo> mDataList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs);
        ButterKnife.inject((Activity) this);
        initView();
        initData();
    }

    private void initView() {
        this.mAdapter = new IngredientInfoAdapter(this, this.mDataList);
        this.lvMain.setAdapter(this.mAdapter);
    }

    private void initData() {
        String tmpDataStr = getIntent().getStringExtra(KEY_INGREDIENT_INFO);
        if (TextUtils.isEmpty(tmpDataStr)) {
            Helper.showLong((CharSequence) "Parameter error!");
            finish();
            return;
        }
        this.mDataList.addAll(JSON.parseArray(tmpDataStr, IngredientInfo.class));
        this.mAdapter.notifyDataSetChanged();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mDataList != null) {
            this.mDataList.clear();
        }
    }

    public static void comeOnBaby(Context context, List<IngredientInfo> dataList) {
        if (dataList == null || dataList.size() == 0) {
            Helper.showLong(context.getString(R.string.kg));
            return;
        }
        Intent intent = new Intent(context, IngredientInfoActivity.class);
        intent.putExtra(KEY_INGREDIENT_INFO, JSON.toJSONString(dataList));
        context.startActivity(intent);
    }
}
