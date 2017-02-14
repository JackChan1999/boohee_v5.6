package com.umeng.socialize.weixin.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.boohee.model.status.Post;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.common.UMAsyncTask;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

class UMWXHandler$LoadResourceTask extends UMAsyncTask<WXMediaMessage> {
    private               ProgressDialog mProgressDialog;
    final /* synthetic */ UMWXHandler    this$0;

    private UMWXHandler$LoadResourceTask(UMWXHandler uMWXHandler) {
        this.this$0 = uMWXHandler;
    }

    private UMWXHandler$LoadResourceTask(UMWXHandler uMWXHandler, Context context) {
        this.this$0 = uMWXHandler;
        if (context instanceof Activity) {
            buildDialog(context);
        }
    }

    protected void onPreExecute() {
        SocializeUtils.safeShowDialog(this.mProgressDialog);
        super.onPreExecute();
    }

    protected WXMediaMessage doInBackground() {
        WXMediaMessage msg = null;
        if (UMWXHandler.access$000(this.this$0).equals("emoji")) {
            msg = UMWXHandler.access$100(this.this$0);
        } else if (UMWXHandler.access$000(this.this$0).equals(Post.IMAGE_TYPE)) {
            msg = UMWXHandler.access$200(this.this$0);
        } else if (UMWXHandler.access$000(this.this$0).equals("music")) {
            msg = UMWXHandler.access$300(this.this$0);
        } else if (UMWXHandler.access$000(this.this$0).equals("text")) {
            msg = UMWXHandler.access$400(this.this$0);
        } else if (UMWXHandler.access$000(this.this$0).equals("text_image")) {
            msg = UMWXHandler.access$500(this.this$0);
        } else if (UMWXHandler.access$000(this.this$0).equals("video")) {
            msg = UMWXHandler.access$600(this.this$0);
        }
        byte[] datas = msg.thumbData;
        if (datas != null && datas.length > 32768) {
            Log.d("UMWXHandler", "原始缩略图大小 : " + (msg.thumbData.length / 1024) + " KB.");
            UMWXHandler.access$700(this.this$0).sendEmptyMessage(1);
            msg.thumbData = UMWXHandler.access$800(this.this$0, datas, 32768);
            Log.d("UMWXHandler", "压缩之后缩略图大小 : " + (msg.thumbData.length / 1024) + " KB.");
        }
        return msg;
    }

    protected void onPostExecute(WXMediaMessage msg) {
        SocializeUtils.safeCloseDialog(this.mProgressDialog);
        if (TextUtils.isEmpty(msg.title) || msg.title.getBytes().length < 512) {
            UMWXHandler.access$902(this.this$0, "分享到" + this.this$0.mCustomPlatform.mShowWord);
        } else {
            msg.title = new String(msg.title.getBytes(), 0, 512);
        }
        if (!TextUtils.isEmpty(msg.description) && msg.description.getBytes().length >= 1024) {
            msg.description = new String(msg.description.getBytes(), 0, 1024);
        }
        UMWXHandler.access$1002(this.this$0, msg);
        boolean result = false;
        if (UMWXHandler.access$000(this.this$0) == Post.IMAGE_TYPE && msg.thumbData == null) {
            Log.e("UMWXHandler", "share image doesn't exist");
        } else {
            result = this.this$0.shareTo();
        }
        UMWXHandler.access$1102(this.this$0, msg.description);
        this.this$0.sendReport(result);
        UMWXHandler.access$1200().setShareType(ShareType.NORMAL);
        this.this$0.mExtraData.clear();
        this.this$0.mExtraData.put(SocializeConstants.FIELD_WX_APPID, UMWXHandler.access$1300
                (this.this$0));
        this.this$0.mExtraData.put(SocializeConstants.FIELD_WX_SECRET, UMWXHandler.access$1400
                (this.this$0));
        super.onPostExecute(msg);
    }

    private void buildDialog(Context context) {
        int theme = ResContainer.getResourceId(UMWXHandler.access$1500(this.this$0), ResType
                .STYLE, "Theme.UMDialog");
        int waiting = ResContainer.getResourceId(UMWXHandler.access$1600(this.this$0), ResType
                .STRING, "umeng_socialize_text_waitting");
        int waitingWX = ResContainer.getResourceId(UMWXHandler.access$1700(this.this$0), ResType
                .STRING, "umeng_socialize_text_waitting_weixin");
        int waitingCircle = ResContainer.getResourceId(UMWXHandler.access$1800(this.this$0),
                ResType.STRING, "umeng_socialize_text_waitting_weixin_circle");
        this.mProgressDialog = new ProgressDialog(context, theme);
        String message = UMWXHandler.access$1900(this.this$0).getString(waiting);
        if (SHARE_MEDIA.WEIXIN.toString().equals(SocializeConfig.getSelectedPlatfrom().toString()
        )) {
            message = UMWXHandler.access$2000(this.this$0).getString(waitingWX);
        } else if (SHARE_MEDIA.WEIXIN_CIRCLE.toString().equals(SocializeConfig
                .getSelectedPlatfrom().toString())) {
            message = UMWXHandler.access$2100(this.this$0).getString(waitingCircle);
        }
        this.mProgressDialog.setMessage(message);
    }
}
