package com.boohee.one.update;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.boohee.one.R;

import java.io.File;

public class UpdateDialogActivity extends Activity {
    public static final String FILE        = "file";
    public static final String UPDATE_INFO = "update_info";
    private String     filePath;
    private UpdateInfo info;
    private TextView   tvCancel;
    private TextView   tvContent;
    private TextView   tvStatus;
    private TextView   tvUpdate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. do);
        initParams();
        initView();
    }

    private void initParams() {
        this.info = (UpdateInfo) getIntent().getParcelableExtra("update_info");
        this.filePath = getIntent().getStringExtra("file");
        if (this.info == null) {
            finish();
        }
    }

    private void initView() {
        if (this.info != null) {
            this.tvStatus = (TextView) findViewById(R.id.tv_status);
            this.tvContent = (TextView) findViewById(R.id.tv_content);
            this.tvUpdate = (TextView) findViewById(R.id.tv_update);
            this.tvCancel = (TextView) findViewById(R.id.tv_cancel);
            this.tvContent.setText(this.info.update_log);
            this.tvCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    UpdateDialogActivity.this.finish();
                }
            });
            if (TextUtils.isEmpty(this.filePath)) {
                this.tvUpdate.setText("更新");
                this.tvStatus.setText(String.format("新版本 v%s(%.1fM)", new Object[]{this.info
                        .new_version, Float.valueOf(((float) this.info.target_size) / 1024.0f)}));
                this.tvUpdate.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        UpdateService.intentDownload(UpdateDialogActivity.this,
                                UpdateDialogActivity.this.info);
                        UpdateDialogActivity.this.finish();
                    }
                });
                return;
            }
            this.tvUpdate.setText("安装");
            this.tvStatus.setText(String.format("新版本 v%s 已下载，是否安装？", new Object[]{this.info
                    .new_version}));
            this.tvUpdate.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    UpdateUtil.installApk(UpdateDialogActivity.this, new File
                            (UpdateDialogActivity.this.filePath));
                    UpdateDialogActivity.this.finish();
                }
            });
        }
    }
}
