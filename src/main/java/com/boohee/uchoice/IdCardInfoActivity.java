package com.boohee.uchoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.AuthFailureError;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.RequestManager;
import com.boohee.one.http.client.BaseJsonRequest;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.http.client.MultipartRequest;
import com.boohee.one.http.client.MultipartRequest.DataPart;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.PhotoPickerHelper;
import com.boohee.utils.TextUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

public class IdCardInfoActivity extends GestureActivity {
    private int REQUEST_CODE_PICTURE1 = 1;
    private int REQUEST_CODE_PICTURE2 = 2;
    @InjectView(2131427833)
    LinearLayout activityPostIdCard;
    DisplayImageOptions defaultOptions = new Builder().cacheInMemory(false).cacheOnDisk(false)
            .build();
    @InjectView(2131427835)
    EditText editIdcard;
    @InjectView(2131427834)
    EditText editName;
    private String idCardNum;
    @InjectView(2131427836)
    ImageView ivCard1;
    @InjectView(2131427837)
    ImageView ivCard2;
    private String name;
    String path1;
    String path2;

    @OnClick({2131427836, 2131427837})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_card1:
                PhotoPickerHelper.show(this.activity, this.REQUEST_CODE_PICTURE1);
                return;
            case R.id.iv_card2:
                PhotoPickerHelper.show(this.activity, this.REQUEST_CODE_PICTURE2);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ci);
        ButterKnife.inject((Activity) this);
        getIdCardInfo();
    }

    private void getIdCardInfo() {
        showLoading();
        BooheeClient.build(BooheeClient.ONE).get("/api/v1/id_cards/me", new JsonCallback(this.ctx) {
            public void onFinish() {
                super.onFinish();
                IdCardInfoActivity.this.dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    JSONObject idCardInfo = object.optJSONObject("id_card");
                    IdCardInfoActivity.this.name = idCardInfo.optString("real_name");
                    IdCardInfoActivity.this.idCardNum = idCardInfo.optString("id_no");
                    IdCardInfoActivity.this.path1 = idCardInfo.optString("photo_a") + "?token=" +
                            UserPreference.getToken(IdCardInfoActivity.this.ctx);
                    IdCardInfoActivity.this.path2 = idCardInfo.optString("photo_b") + "?token=" +
                            UserPreference.getToken(IdCardInfoActivity.this.ctx);
                    IdCardInfoActivity.this.refreshView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this.ctx);
    }

    private void refreshView() {
        this.editName.setText(this.name);
        this.editIdcard.setText(this.idCardNum);
        ImageLoader.getInstance().displayImage(this.path1, this.ivCard1, this.defaultOptions);
        ImageLoader.getInstance().displayImage(this.path2, this.ivCard2, this.defaultOptions);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "确定").setShowAsAction(2);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            postIdCard();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postIdCard() {
        this.name = this.editName.getText().toString().trim();
        this.idCardNum = this.editIdcard.getText().toString().trim();
        if (TextUtil.isEmpty(this.name, this.idCardNum, this.path1, this.path2)) {
            Helper.showToast((CharSequence) "请填写完整信息后重试");
            return;
        }
        showLoading();
        String fullURL = BooheeClient.build(BooheeClient.ONE).getDefaultURL("/api/v1/id_cards");
        RequestManager.addRequest(new MultipartRequest(fullURL, BaseJsonRequest.addDefaultParams
                (fullURL, getParams()), new JsonCallback(this.ctx) {
            public void fail(String message) {
                super.fail(message);
            }

            public void onFinish() {
                super.onFinish();
                IdCardInfoActivity.this.dismissLoading();
            }

            public void ok(String response) {
                IdCardInfoActivity.this.setResult(-1);
                IdCardInfoActivity.this.finish();
            }
        }) {
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap();
                params.put("photo_url_a", new DataPart("photo_a.jpg", BitmapUtil
                        .getFileDataFromDrawable(IdCardInfoActivity.this.getBaseContext(),
                                IdCardInfoActivity.this.ivCard1.getDrawable()), "image/jpeg"));
                params.put("photo_url_b", new DataPart("photo_b.jpg", BitmapUtil
                        .getFileDataFromDrawable(IdCardInfoActivity.this.getBaseContext(),
                                IdCardInfoActivity.this.ivCard2.getDrawable()), "image/jpeg"));
                return params;
            }
        }, this.ctx);
    }

    private JsonParams getParams() {
        JsonParams params = new JsonParams();
        params.put("real_name", this.name);
        params.put("id_no", this.idCardNum);
        return params;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            ArrayList<String> tempList = data.getStringArrayListExtra(MultiImageSelectorActivity
                    .EXTRA_RESULT);
            if (tempList != null && tempList.size() > 0) {
                String path = (String) tempList.get(0);
                if (requestCode == this.REQUEST_CODE_PICTURE1) {
                    this.path1 = "file://" + path;
                } else if (requestCode == this.REQUEST_CODE_PICTURE2) {
                    this.path2 = "file://" + path;
                }
                refreshView();
            }
        }
    }
}
