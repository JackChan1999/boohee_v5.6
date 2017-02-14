package cn.sharesdk.onekeyshare;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity.OnShareButtonClickListener;
import com.baidu.location.a.a;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.R;
import com.mob.tools.utils.UIHandler;
import com.zxinsight.share.domain.BMPlatform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class OnekeyShare implements PlatformActionListener, Callback {
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private static final int MSG_TOAST = 1;
    private View bgView;
    private PlatformActionListener callback = this;
    private Context context;
    private ArrayList<CustomerLogo> customers = new ArrayList();
    private ShareContentCustomizeCallback customizeCallback;
    private boolean dialogMode = false;
    private boolean disableSSO;
    private HashMap<String, String> hiddenPlatforms = new HashMap();
    private OnShareButtonClickListener onShareButtonClickListener;
    private HashMap<String, Object> shareParamsMap = new HashMap();
    private boolean silent;
    private OnekeyShareTheme theme;

    public void show(Context context) {
        ShareSDK.initSDK(context);
        this.context = context;
        ShareSDK.logDemoEvent(1, null);
        if (this.shareParamsMap.containsKey("platform")) {
            String name = String.valueOf(this.shareParamsMap.get("platform"));
            Platform platform = ShareSDK.getPlatform(name);
            if (this.silent || ShareCore.isUseClientToShare(name) || (platform instanceof CustomPlatform)) {
                HashMap<Platform, HashMap<String, Object>> shareData = new HashMap();
                shareData.put(ShareSDK.getPlatform(name), this.shareParamsMap);
                share(shareData);
                return;
            }
        }
        try {
            PlatformListFakeActivity platformListFakeActivity;
            if (OnekeyShareTheme.SKYBLUE == this.theme) {
                platformListFakeActivity = (PlatformListFakeActivity) Class.forName("cn.sharesdk.onekeyshare.theme.skyblue.PlatformListPage").newInstance();
            } else {
                platformListFakeActivity = (PlatformListFakeActivity) Class.forName("cn.sharesdk.onekeyshare.theme.classic.PlatformListPage").newInstance();
            }
            platformListFakeActivity.setDialogMode(this.dialogMode);
            platformListFakeActivity.setShareParamsMap(this.shareParamsMap);
            platformListFakeActivity.setSilent(this.silent);
            platformListFakeActivity.setCustomerLogos(this.customers);
            platformListFakeActivity.setBackgroundView(this.bgView);
            platformListFakeActivity.setHiddenPlatforms(this.hiddenPlatforms);
            platformListFakeActivity.setOnShareButtonClickListener(this.onShareButtonClickListener);
            platformListFakeActivity.setThemeShareCallback(new ThemeShareCallback() {
                public void doShare(HashMap<Platform, HashMap<String, Object>> shareData) {
                    OnekeyShare.this.share(shareData);
                }
            });
            if (this.shareParamsMap.containsKey("platform")) {
                platformListFakeActivity.showEditPage(context, ShareSDK.getPlatform(String.valueOf(this.shareParamsMap.get("platform"))));
            } else {
                platformListFakeActivity.show(context, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTheme(OnekeyShareTheme theme) {
        this.theme = theme;
    }

    public void setAddress(String address) {
        this.shareParamsMap.put("address", address);
    }

    public void setTitle(String title) {
        this.shareParamsMap.put("title", title);
    }

    public void setTitleUrl(String titleUrl) {
        this.shareParamsMap.put("titleUrl", titleUrl);
    }

    public void setText(String text) {
        this.shareParamsMap.put("text", text);
    }

    public String getText() {
        return this.shareParamsMap.containsKey("text") ? String.valueOf(this.shareParamsMap.get("text")) : null;
    }

    public void setImagePath(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            this.shareParamsMap.put("imagePath", imagePath);
        }
    }

    public void setImageUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            this.shareParamsMap.put("imageUrl", imageUrl);
        }
    }

    public void setUrl(String url) {
        this.shareParamsMap.put("url", url);
    }

    public void setFilePath(String filePath) {
        this.shareParamsMap.put("filePath", filePath);
    }

    public void setComment(String comment) {
        this.shareParamsMap.put("comment", comment);
    }

    public void setSite(String site) {
        this.shareParamsMap.put("site", site);
    }

    public void setSiteUrl(String siteUrl) {
        this.shareParamsMap.put("siteUrl", siteUrl);
    }

    public void setVenueName(String venueName) {
        this.shareParamsMap.put("venueName", venueName);
    }

    public void setVenueDescription(String venueDescription) {
        this.shareParamsMap.put("venueDescription", venueDescription);
    }

    public void setLatitude(float latitude) {
        this.shareParamsMap.put(a.int, Float.valueOf(latitude));
    }

    public void setLongitude(float longitude) {
        this.shareParamsMap.put(a.char, Float.valueOf(longitude));
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public void setPlatform(String platform) {
        this.shareParamsMap.put("platform", platform);
    }

    public void setInstallUrl(String installurl) {
        this.shareParamsMap.put("installurl", installurl);
    }

    public void setExecuteUrl(String executeurl) {
        this.shareParamsMap.put("executeurl", executeurl);
    }

    public void setMusicUrl(String musicUrl) {
        this.shareParamsMap.put("musicUrl", musicUrl);
    }

    public void setCallback(PlatformActionListener callback) {
        this.callback = callback;
    }

    public PlatformActionListener getCallback() {
        return this.callback;
    }

    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
        this.customizeCallback = callback;
    }

    public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
        return this.customizeCallback;
    }

    public void setCustomerLogo(Bitmap enableLogo, Bitmap disableLogo, String label, OnClickListener ocListener) {
        CustomerLogo cl = new CustomerLogo();
        cl.label = label;
        cl.enableLogo = enableLogo;
        cl.disableLogo = disableLogo;
        cl.listener = ocListener;
        this.customers.add(cl);
    }

    public void disableSSOWhenAuthorize() {
        this.disableSSO = true;
    }

    public void setDialogMode() {
        this.dialogMode = true;
        this.shareParamsMap.put("dialogMode", Boolean.valueOf(this.dialogMode));
    }

    public void addHiddenPlatform(String platform) {
        this.hiddenPlatforms.put(platform, platform);
    }

    public void setViewToShare(View viewToShare) {
        try {
            this.shareParamsMap.put("viewToShare", BitmapHelper.captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setImageArray(String[] imageArray) {
        this.shareParamsMap.put("imageArray", imageArray);
    }

    public void setEditPageBackground(View bgView) {
        this.bgView = bgView;
    }

    public void setOnShareButtonClickListener(OnShareButtonClickListener onShareButtonClickListener) {
        this.onShareButtonClickListener = onShareButtonClickListener;
    }

    public void share(HashMap<Platform, HashMap<String, Object>> shareData) {
        boolean started = false;
        for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
            Platform plat = (Platform) ent.getKey();
            plat.SSOSetting(this.disableSSO);
            String name = plat.getName();
            Message msg;
            if ("KakaoTalk".equals(name) && !plat.isClientValid()) {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "kakaotalk_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            } else if ("KakaoStory".equals(name) && !plat.isClientValid()) {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "kakaostory_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            } else if ("Line".equals(name) && !plat.isClientValid()) {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "line_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            } else if ("WhatsApp".equals(name) && !plat.isClientValid()) {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "whatsapp_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            } else if ("Pinterest".equals(name) && !plat.isClientValid()) {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "pinterest_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            } else if (!"Instagram".equals(name) || plat.isClientValid()) {
                boolean isLaiwang = "Laiwang".equals(name);
                boolean isLaiwangMoments = "LaiwangMoments".equals(name);
                if ((isLaiwang || isLaiwangMoments) && !plat.isClientValid()) {
                    msg = new Message();
                    msg.what = 1;
                    msg.obj = this.context.getString(R.getStringRes(this.context, "laiwang_client_inavailable"));
                    UIHandler.sendMessage(msg, this);
                } else {
                    boolean isYixin = "YixinMoments".equals(name) || "Yixin".equals(name);
                    if (!isYixin || plat.isClientValid()) {
                        HashMap<String, Object> data = (HashMap) ent.getValue();
                        int shareType = 1;
                        String imagePath = String.valueOf(data.get("imagePath"));
                        if (imagePath == null || !new File(imagePath).exists()) {
                            Bitmap viewToShare = (Bitmap) data.get("viewToShare");
                            if (viewToShare == null || viewToShare.isRecycled()) {
                                Object imageUrl = data.get("imageUrl");
                                if (!(imageUrl == null || TextUtils.isEmpty(String.valueOf(imageUrl)))) {
                                    shareType = 2;
                                    if (String.valueOf(imageUrl).endsWith(".gif")) {
                                        shareType = 9;
                                    } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                                        shareType = 4;
                                        if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                                            shareType = 5;
                                        }
                                    }
                                }
                            } else {
                                shareType = 2;
                                if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                                    shareType = 4;
                                    if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                                        shareType = 5;
                                    }
                                }
                            }
                        } else {
                            shareType = 2;
                            if (imagePath.endsWith(".gif")) {
                                shareType = 9;
                            } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                                shareType = 4;
                                if (data.containsKey("musicUrl") && !TextUtils.isEmpty(data.get("musicUrl").toString())) {
                                    shareType = 5;
                                }
                            }
                        }
                        if (BMPlatform.NAME_QZONE.equals(name) && TextUtils.isEmpty(data.get("url").toString()) && data.get("imageUrl") != null) {
                            shareType = 4;
                            data.put("siteUrl", "http://www.boohee.com");
                            data.put("url", "http://www.boohee.com");
                            data.put("titleUrl", "http://www.boohee.com");
                        }
                        if (BMPlatform.NAME_QZONE.equals(name) && TextUtils.isEmpty(data.get("url").toString()) && data.get("imageUrl") == null) {
                            data.put("siteUrl", "http://www.boohee.com");
                            data.put("titleUrl", "http://www.boohee.com");
                        }
                        data.put("shareType", Integer.valueOf(shareType));
                        if (!started) {
                            started = true;
                            int resId = R.getStringRes(this.context, "sharing");
                            if (resId > 0) {
                                showNotification(this.context.getString(resId));
                            }
                        }
                        plat.setPlatformActionListener(this.callback);
                        ShareCore shareCore = new ShareCore();
                        shareCore.setShareContentCustomizeCallback(this.customizeCallback);
                        shareCore.share(plat, data);
                    } else {
                        msg = new Message();
                        msg.what = 1;
                        msg.obj = this.context.getString(R.getStringRes(this.context, "yixin_client_inavailable"));
                        UIHandler.sendMessage(msg, this);
                    }
                }
            } else {
                msg = new Message();
                msg.what = 1;
                msg.obj = this.context.getString(R.getStringRes(this.context, "instagram_client_inavailable"));
                UIHandler.sendMessage(msg, this);
            }
        }
    }

    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
        ShareSDK.logDemoEvent(4, platform);
    }

    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                Toast.makeText(this.context, String.valueOf(msg.obj), 0).show();
                break;
            case 2:
                int resId;
                switch (msg.arg1) {
                    case 1:
                        resId = R.getStringRes(this.context, "share_completed");
                        if (resId > 0) {
                            showNotification(this.context.getString(resId));
                            break;
                        }
                        break;
                    case 2:
                        String expName = msg.obj.getClass().getSimpleName();
                        if (!"WechatClientNotExistException".equals(expName) && !"WechatTimelineNotSupportedException".equals(expName) && !"WechatFavoriteNotSupportedException".equals(expName)) {
                            if (!"GooglePlusClientNotExistException".equals(expName)) {
                                if (!"QQClientNotExistException".equals(expName)) {
                                    if (!"YixinClientNotExistException".equals(expName) && !"YixinTimelineNotSupportedException".equals(expName)) {
                                        if (!"KakaoTalkClientNotExistException".equals(expName)) {
                                            if (!"KakaoStoryClientNotExistException".equals(expName)) {
                                                if (!"WhatsAppClientNotExistException".equals(expName)) {
                                                    resId = R.getStringRes(this.context, "share_failed");
                                                    if (resId > 0) {
                                                        showNotification(this.context.getString(resId));
                                                        break;
                                                    }
                                                }
                                                resId = R.getStringRes(this.context, "whatsapp_client_inavailable");
                                                if (resId > 0) {
                                                    showNotification(this.context.getString(resId));
                                                    break;
                                                }
                                            }
                                            resId = R.getStringRes(this.context, "kakaostory_client_inavailable");
                                            if (resId > 0) {
                                                showNotification(this.context.getString(resId));
                                                break;
                                            }
                                        }
                                        resId = R.getStringRes(this.context, "kakaotalk_client_inavailable");
                                        if (resId > 0) {
                                            showNotification(this.context.getString(resId));
                                            break;
                                        }
                                    }
                                    resId = R.getStringRes(this.context, "yixin_client_inavailable");
                                    if (resId > 0) {
                                        showNotification(this.context.getString(resId));
                                        break;
                                    }
                                }
                                resId = R.getStringRes(this.context, "qq_client_inavailable");
                                if (resId > 0) {
                                    showNotification(this.context.getString(resId));
                                    break;
                                }
                            }
                            resId = R.getStringRes(this.context, "google_plus_client_inavailable");
                            if (resId > 0) {
                                showNotification(this.context.getString(resId));
                                break;
                            }
                        }
                        resId = R.getStringRes(this.context, "wechat_client_inavailable");
                        if (resId > 0) {
                            showNotification(this.context.getString(resId));
                            break;
                        }
                        break;
                    case 3:
                        resId = R.getStringRes(this.context, "share_canceled");
                        if (resId > 0) {
                            showNotification(this.context.getString(resId));
                            break;
                        }
                        break;
                    default:
                        break;
                }
            case 3:
                NotificationManager nm = msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                    break;
                }
                break;
        }
        return false;
    }

    private void showNotification(String text) {
        Toast.makeText(this.context, text, 0).show();
    }

    public void setShareFromQQAuthSupport(boolean shareFromQQLogin) {
        this.shareParamsMap.put("isShareTencentWeibo", Boolean.valueOf(shareFromQQLogin));
    }
}
