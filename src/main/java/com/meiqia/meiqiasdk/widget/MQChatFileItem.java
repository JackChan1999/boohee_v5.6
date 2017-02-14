package com.meiqia.meiqiasdk.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.callback.FileStateCallback;
import com.meiqia.meiqiasdk.callback.OnDownloadFileCallback;
import com.meiqia.meiqiasdk.model.FileMessage;
import com.meiqia.meiqiasdk.util.ErrorCode;
import com.meiqia.meiqiasdk.util.MQChatAdapter;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQTimeUtils;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.io.File;
import java.text.DecimalFormat;

import org.json.JSONObject;

public class MQChatFileItem extends MQBaseCustomCompositeView implements OnTouchListener {
    private boolean             isCancel;
    private MQChatAdapter       mAdapter;
    private FileMessage         mFileMessage;
    private FileStateCallback   mFileStateCallback;
    private CircularProgressBar mProgressBar;
    private View                mRightIv;
    private TextView            mSubTitleTv;
    private TextView            mTitleTv;
    private View                root;

    public MQChatFileItem(Context context) {
        super(context);
    }

    public MQChatFileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MQChatFileItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int getLayoutId() {
        return R.layout.mq_item_file_layout;
    }

    protected void initView() {
        this.root = findViewById(R.id.root);
        this.mProgressBar = (CircularProgressBar) findViewById(R.id.progressbar);
        this.mTitleTv = (TextView) findViewById(R.id.mq_file_title_tv);
        this.mSubTitleTv = (TextView) findViewById(R.id.mq_file_sub_title_tv);
        this.mRightIv = findViewById(R.id.mq_right_iv);
    }

    protected void setListener() {
        this.root.setOnClickListener(this);
        this.mRightIv.setOnClickListener(this);
        this.mProgressBar.setOnTouchListener(this);
    }

    protected int[] getAttrs() {
        return new int[0];
    }

    protected void initAttr(int attr, TypedArray typedArray) {
    }

    protected void processLogic() {
    }

    public void setFileStateCallback(FileStateCallback fileStateCallback) {
        this.mFileStateCallback = fileStateCallback;
    }

    public void onClick(View view) {
        if (this.mFileMessage != null) {
            int id = view.getId();
            if (id == R.id.mq_right_iv) {
                this.root.performClick();
            } else if (id == R.id.progressbar) {
                cancelDownloading();
            } else if (id == R.id.root) {
                switch (this.mFileMessage.getFileState()) {
                    case 0:
                        openFile();
                        return;
                    case 2:
                        this.isCancel = false;
                        this.mFileMessage.setFileState(1);
                        downloadingState();
                        MQConfig.getController(getContext()).downloadFile(this.mFileMessage, new
                                OnDownloadFileCallback() {
                            public void onSuccess(File file) {
                                if (!MQChatFileItem.this.isCancel) {
                                    MQChatFileItem.this.mFileMessage.setFileState(0);
                                    MQChatFileItem.this.mAdapter.notifyDataSetChanged();
                                }
                            }

                            public void onProgress(int progress) {
                                MQChatFileItem.this.mFileMessage.setProgress(progress);
                                MQChatFileItem.this.mAdapter.notifyDataSetChanged();
                            }

                            public void onFailure(int code, String message) {
                                if (code != ErrorCode.DOWNLOAD_IS_CANCEL) {
                                    MQChatFileItem.this.mFileMessage.setFileState(3);
                                    MQChatFileItem.this.downloadFailedState();
                                    MQChatFileItem.this.cancelDownloading();
                                    if (MQChatFileItem.this.mFileStateCallback != null) {
                                        MQChatFileItem.this.mFileStateCallback
                                                .onFileMessageDownloadFailure(MQChatFileItem.this
                                                        .mFileMessage, code, message);
                                    }
                                }
                            }
                        });
                        return;
                    case 3:
                        this.mFileMessage.setFileState(2);
                        this.root.performClick();
                        return;
                    case 4:
                        if (this.mFileStateCallback != null) {
                            this.mFileStateCallback.onFileMessageExpired(this.mFileMessage);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void initFileItem(MQChatAdapter adapter, FileMessage fileMessage) {
        this.mAdapter = adapter;
        this.mFileMessage = fileMessage;
        downloadInitState();
    }

    private void openFile() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(MQUtils.getFileMessageFilePath(this
                .mFileMessage))), getExtraStringValue("type"));
        intent.addFlags(268435456);
        try {
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.mq_no_app_open_file, 0).show();
        }
    }

    private void cancelDownloading() {
        this.isCancel = true;
        this.mFileMessage.setFileState(2);
        MQConfig.getController(getContext()).cancelDownload(this.mFileMessage.getUrl());
        MQUtils.delFile(MQUtils.getFileMessageFilePath(this.mFileMessage));
        this.mAdapter.notifyDataSetChanged();
    }

    public void setProgress(int progress) {
        this.mProgressBar.setProgress((float) progress);
    }

    public void downloadInitState() {
        this.mProgressBar.setProgress(0.0f);
        this.mProgressBar.setVisibility(8);
        displayFileInfo();
    }

    public void downloadSuccessState() {
        displayFileInfo();
        this.mProgressBar.setVisibility(8);
        setProgress(100);
        this.mRightIv.setVisibility(8);
    }

    public void downloadFailedState() {
        this.mProgressBar.setVisibility(8);
    }

    public void downloadingState() {
        this.mSubTitleTv.setText(String.format("%s%s", new Object[]{getSubTitlePrefix(),
                getResources().getString(R.string.mq_downloading)}));
        this.mRightIv.setVisibility(8);
        this.mProgressBar.setVisibility(0);
    }

    private void displayFileInfo() {
        String endStr;
        this.mTitleTv.setText(getExtraStringValue("filename"));
        if (MQUtils.isFileExist(MQUtils.getFileMessageFilePath(this.mFileMessage))) {
            endStr = getResources().getString(R.string.mq_download_complete);
            this.mRightIv.setVisibility(8);
        } else {
            long diffTime = MQTimeUtils.parseTimeToLong(getExtraStringValue("expire_at")) -
                    System.currentTimeMillis();
            if (diffTime <= 0) {
                endStr = getResources().getString(R.string.mq_expired);
                this.mRightIv.setVisibility(8);
                this.mFileMessage.setFileState(4);
            } else {
                String leaveHoursStr = new DecimalFormat("#.0").format((double) (((float)
                        diffTime) / 3600000.0f));
                endStr = getContext().getString(R.string.mq_expire_after, new
                        Object[]{leaveHoursStr});
                this.mRightIv.setVisibility(0);
            }
        }
        this.mSubTitleTv.setText(getSubTitlePrefix() + endStr);
        this.mProgressBar.setVisibility(8);
    }

    private String getExtraStringValue(String key) {
        String value = "";
        try {
            value = new JSONObject(this.mFileMessage.getExtra()).optString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private String getSubTitlePrefix() {
        return Formatter.formatShortFileSize(getContext(), getExtraLongValue("size")) + " · ";
    }

    private long getExtraLongValue(String key) {
        long value = 0;
        try {
            value = new JSONObject(this.mFileMessage.getExtra()).optLong(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            cancelDownloading();
        }
        return false;
    }
}
