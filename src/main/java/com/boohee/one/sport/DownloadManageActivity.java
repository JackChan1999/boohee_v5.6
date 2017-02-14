package com.boohee.one.sport;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.event.DownloadEvent;
import com.boohee.one.sport.DownloadAdapter.DownloadVH;
import com.boohee.one.sport.model.DownloadRecord;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.List;

public class DownloadManageActivity extends GestureActivity {
    @InjectView(2131427636)
    CheckBox cbSelectAll;
    private DownloadAdapter mAdapter;
    private MenuItem        menuEdit;
    @InjectView(2131427637)
    RecyclerView recyclerView;
    @InjectView(2131427635)
    View         selectLine;
    @InjectView(2131427638)
    TextView     tvDelete;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DownloadManageActivity.class));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b7);
        ButterKnife.inject((Activity) this);
        MobclickAgent.onEvent(this, Event.bingo_viewDownloadList);
        initViews();
    }

    private void initViews() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<DownloadRecord> recordList = DownloadHelper.getInstance().getRecordsList();
        this.mAdapter = new DownloadAdapter(this, recordList);
        this.recyclerView.setAdapter(this.mAdapter);
        if (recordList.size() == 0) {
            showEmpty();
            return;
        }
        this.tvDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DownloadManageActivity.this.showDeleteDialog();
            }
        });
        this.cbSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DownloadManageActivity.this.mAdapter.selectAll();
                } else if (!DownloadAdapter.CANCEL_SELECT_ALL.equals(buttonView.getTag())) {
                    DownloadManageActivity.this.mAdapter.selectNone();
                }
                buttonView.setTag(null);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.mAdapter.updateAllRecord();
    }

    private void showEmpty() {
        ViewStub stub = (ViewStub) findViewById(R.id.empty_layout);
        if (stub != null) {
            stub.setVisibility(0);
        }
        this.recyclerView.setVisibility(8);
        updateMenu();
    }

    private void showDeleteDialog() {
        if (this.mAdapter.getSelectVideos().size() == 0) {
            Helper.showToast((CharSequence) "请选择要删除的视频");
        } else {
            new Builder(this).setTitle((CharSequence) "清除视频缓存").setMessage((CharSequence)
                    "清除视频缓存后，已下载的运动视频需要重新下载").setPositiveButton((CharSequence) "确定清除", new
                    DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DownloadHelper.getInstance().deleteDownload(DownloadManageActivity.this
                            .mAdapter.getSelectVideos());
                    DownloadManageActivity.this.mAdapter.setList(DownloadHelper.getInstance()
                            .getRecordsList());
                    DownloadManageActivity.this.changeEditMode(DownloadManageActivity.this
                            .menuEdit);
                    if (DownloadHelper.getInstance().getRecordsList().size() == 0) {
                        DownloadManageActivity.this.showEmpty();
                    }
                }
            }).setNegativeButton((CharSequence) "取消", null).show();
        }
    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(DownloadEvent event) {
        int position = this.mAdapter.updateRecord(event.record);
        if (position >= 0) {
            this.mAdapter.updateDownloadStatus((DownloadVH) this.recyclerView
                    .findViewHolderForAdapterPosition(position), event.record);
        }
    }

    private void updateMenu() {
        if (this.menuEdit != null && DownloadHelper.getInstance().getRecordsList().size() == 0) {
            this.menuEdit.setVisible(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuEdit = menu.add(0, 1, 1, R.string.jk);
        this.menuEdit.setShowAsAction(2);
        updateMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case 1:
                changeEditMode(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeEditMode(MenuItem item) {
        if (this.tvDelete.getVisibility() == 0) {
            this.mAdapter.setShowSelect(false);
            this.tvDelete.setVisibility(8);
            this.selectLine.setVisibility(4);
            item.setTitle(getString(R.string.jk));
        } else if (this.tvDelete.getVisibility() == 8) {
            this.mAdapter.setShowSelect(true);
            this.tvDelete.setVisibility(0);
            this.selectLine.setVisibility(0);
            item.setTitle(getString(R.string.jl));
        }
    }
}
