package com.boohee.status;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;
import com.boohee.widgets.LightAlertDialog;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

public class ReportActivity extends GestureActivity {
    public static final String EXTRA_ID   = "extra_id";
    public static final String EXTRA_TYPE = "extra_type";
    static final        String TAG        = ReportActivity.class.getSimpleName();
    String category = "广告";
    int id;
    private ArrayAdapter<String> mAdapter;
    private List<String> mData = Arrays.asList(new String[]{"广告", "色情", "欺诈", "骚扰", "侮辱", "侵权",
            "其他"});
    private ListView mList;
    String type = ReportType.User.toString();

    public enum ReportType {
        User,
        Post,
        Comment
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.cp);
        setTitle(R.string.av);
        getData();
        findViews();
    }

    private void getData() {
        this.id = getIntExtra(EXTRA_ID);
        this.type = getStringExtra(EXTRA_TYPE);
    }

    private void findViews() {
        this.mList = (ListView) findViewById(R.id.contentList);
        this.mAdapter = new ArrayAdapter(this, 17367055, this.mData);
        this.mList.setAdapter(this.mAdapter);
        this.mList.setItemChecked(0, true);
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long
                    arg3) {
                ReportActivity.this.category = (String) ReportActivity.this.mAdapter.getItem
                        (position);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.y8).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                sendReport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendReport() {
        if (TextUtils.isEmpty(this.category)) {
            Helper.showToast((CharSequence) "请选择举报理由");
            return;
        }
        if (TextUtils.isEmpty(this.type)) {
            this.type = ReportType.User.toString();
        }
        StatusApi.postReport(this.activity, this.id, this.type, this.category, new JsonCallback
                (this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object.has("id")) {
                    Helper.showToast((CharSequence) "举报成功");
                    ReportActivity.this.finish();
                }
            }
        });
    }

    public void onClick(View view) {
        LightAlertDialog.create((Context) this, "确认举报么？", "确定要举报？").setPositiveButton(
                (CharSequence) "确定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ReportActivity.this.sendReport();
            }
        }).setNegativeButton(null).show();
    }
}
