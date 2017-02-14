package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.food.adapter.UploadAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.UploadFood;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UploadStateActivity extends GestureActivity {
    public static final String KEY_UPLOAD_FOOD = "key_upload_food";
    @InjectView(2131427984)
    View aliasItem;
    @InjectView(2131427982)
    View brandItem;
    private UploadFood mUploadFood;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dq);
        ButterKnife.inject((Activity) this);
        initView();
    }

    private void initView() {
        this.mUploadFood = (UploadFood) FastJsonUtils.fromJson(getIntent().getStringExtra
                (KEY_UPLOAD_FOOD), UploadFood.class);
        initItem(R.id.view_item_code, R.string.abi, this.mUploadFood.barcode);
        if (!(this.mUploadFood.brand == null || TextUtils.isEmpty(this.mUploadFood.brand))) {
            this.brandItem.setVisibility(0);
            initItem(R.id.view_item_brand, R.string.ab3, this.mUploadFood.brand);
        }
        initItem(R.id.view_item_name, R.string.abk, this.mUploadFood.food_name);
        if (!(this.mUploadFood.alias == null || TextUtils.isEmpty(this.mUploadFood.alias))) {
            this.aliasItem.setVisibility(0);
            initItem(R.id.view_item_alias, R.string.ab2, this.mUploadFood.alias);
        }
        initItem(R.id.view_item_time, R.string.abn, this.mUploadFood.upload_date);
        initItem(R.id.view_item_state, R.string.abl, UploadAdapter.getStateString(this, this
                .mUploadFood.state));
        if (TextUtils.isEmpty(this.mUploadFood.message) || "null".equals(this.mUploadFood
                .message)) {
            findViewById(R.id.view_item_state_message).setVisibility(8);
        } else {
            initItem(R.id.view_item_state_message, R.string.abm, this.mUploadFood.message);
        }
        ImageLoader.getInstance().displayImage(this.mUploadFood.front_img_url, (ImageView)
                findViewById(R.id.iv_front), ImageLoaderOptions.global((int) R.drawable.aa2));
        ImageLoader.getInstance().displayImage(this.mUploadFood.back_img_url, (ImageView)
                findViewById(R.id.iv_back), ImageLoaderOptions.global((int) R.drawable.aa2));
    }

    private void initItem(int itemViewRes, int nameRes, String value) {
        View itemView = findViewById(itemViewRes);
        ((TextView) itemView.findViewById(R.id.tv_name)).setText(nameRes);
        ((TextView) itemView.findViewById(R.id.tv_value)).setText(value);
    }

    public static void comeOnBaby(Context context, UploadFood uploadFood) {
        if (uploadFood != null) {
            String dataStr = FastJsonUtils.toJson(uploadFood);
            if (!TextUtils.isEmpty(dataStr)) {
                Intent intent = new Intent(context, UploadStateActivity.class);
                intent.putExtra(KEY_UPLOAD_FOOD, dataStr);
                context.startActivity(intent);
            }
        }
    }
}
