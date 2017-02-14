package cn.sharesdk.tencent.qzone;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.statistics.b.f.a;
import com.alipay.sdk.packet.d;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utils.SDcard;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.tencent.open.SocialConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class QZone extends Platform {
    public static final String NAME = QZone.class.getSimpleName();
    private String a;
    private boolean b;

    public static class ShareParams extends cn.sharesdk.framework.Platform.ShareParams {
        @Deprecated
        public String comment;
        @Deprecated
        public String imageUrl;
        @Deprecated
        public String site;
        @Deprecated
        public String siteUrl;
        @Deprecated
        public String title;
        @Deprecated
        public String titleUrl;
        @Deprecated
        boolean webShare;
    }

    public QZone(Context context) {
        super(context);
    }

    private void a(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        Object imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        boolean isShareTencentWeibo = shareParams.isShareTencentWeibo();
        try {
            if (TextUtils.isEmpty(imagePath) && !TextUtils.isEmpty(imageUrl)) {
                shareParams.setImagePath(BitmapHelper.downloadBitmap(this.context, imageUrl));
                doShare(shareParams);
            } else if (isAuthValid()) {
                imageUrl = shareParams.getText();
                if (!TextUtils.isEmpty(imageUrl)) {
                    String shortLintk = getShortLintk(imageUrl, false);
                    f a = f.a((Platform) this);
                    HashMap b = isShareTencentWeibo ? a.b(imagePath, shortLintk) : a.a(imagePath, shortLintk);
                    if (b == null && this.listener != null) {
                        this.listener.onError(this, 9, new Throwable("response is empty"));
                    }
                    b.put("ShareParams", shareParams);
                    if (this.listener != null) {
                        this.listener.onComplete(this, 9, b);
                    }
                } else if (this.listener != null) {
                    this.listener.onError(this, 9, new Throwable("share params' value of text is empty!"));
                }
            } else {
                setPlatformActionListener(new b(this, getPlatformActionListener(), shareParams));
                authorize();
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    private void b(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        if (TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
            imageUrl = uploadImageToFileServer(imagePath);
            shareParams.setImageUrl(imageUrl);
        }
        try {
            shareParams.set("webShare", Boolean.valueOf(true));
            String title = shareParams.getTitle();
            String titleUrl = shareParams.getTitleUrl();
            String site = shareParams.getSite();
            String text = shareParams.getText();
            imagePath = getShortLintk(titleUrl + " SSDK_SEP " + text, false);
            String[] split = imagePath.split(" SSDK_SEP ");
            if (split.length >= 2) {
                titleUrl = split[0];
                text = split[1];
            } else if (split.length >= 1) {
                if (imagePath.endsWith(" SSDK_SEP ")) {
                    titleUrl = split[0];
                } else {
                    text = split[0];
                }
            }
            f.a((Platform) this).a(title, titleUrl, text, imageUrl, site, this.b, new c(this, shareParams));
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    protected boolean checkAuthorize(int i, Object obj) {
        if (isAuthValid() || i == 9) {
            f a = f.a((Platform) this);
            a.a(this.a);
            a.b(this.db.getUserId());
            a.c(this.db.getToken());
            return true;
        }
        innerAuthorize(i, obj);
        return false;
    }

    protected void doAuthorize(String[] strArr) {
        f a = f.a((Platform) this);
        a.a(this.a);
        a.a(strArr);
        a.a(new a(this, a), isSSODisable());
    }

    protected void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        HashMap a = f.a((Platform) this).a(str, str2, hashMap, hashMap2);
        if (a == null || a.size() <= 0) {
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable());
            }
        } else if (a.containsKey("ret")) {
            if (((Integer) a.get("ret")).intValue() != 0) {
                if (this.listener != null) {
                    this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
                }
            } else if (this.listener != null) {
                this.listener.onComplete(this, i, a);
            }
        } else if (this.listener != null) {
            this.listener.onError(this, i, new Throwable());
        }
    }

    protected void doShare(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        shareParams.set("webShare", Boolean.valueOf(false));
        CharSequence title = shareParams.getTitle();
        CharSequence titleUrl = shareParams.getTitleUrl();
        CharSequence site = shareParams.getSite();
        CharSequence siteUrl = shareParams.getSiteUrl();
        CharSequence imageUrl = shareParams.getImageUrl();
        CharSequence imagePath = shareParams.getImagePath();
        if (shareParams.isShareTencentWeibo()) {
            a(shareParams);
        } else if (TextUtils.isEmpty(title) && TextUtils.isEmpty(titleUrl) && TextUtils.isEmpty(site) && TextUtils.isEmpty(siteUrl) && (!TextUtils.isEmpty(imagePath) || !TextUtils.isEmpty(imageUrl))) {
            a(shareParams);
        } else {
            b(shareParams);
        }
    }

    protected a filterShareContent(cn.sharesdk.framework.Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        a aVar = new a();
        aVar.b = shareParams.getText();
        String imageUrl;
        if (((Boolean) shareParams.get("webShare", Boolean.class)).booleanValue()) {
            imageUrl = shareParams.getImageUrl();
            if (imageUrl != null) {
                aVar.d.add(imageUrl);
            }
            imageUrl = shareParams.getTitleUrl();
            if (imageUrl != null) {
                aVar.c.add(imageUrl);
            }
            imageUrl = shareParams.getSiteUrl();
            if (imageUrl != null) {
                aVar.c.add(imageUrl);
            }
            if (hashMap != null) {
                aVar.a = String.valueOf(hashMap.get("share_id"));
            }
        } else {
            imageUrl = shareParams.getImagePath();
            if (imageUrl != null) {
                aVar.e.add(imageUrl);
            } else if (hashMap.get("large_url") != null) {
                aVar.d.add(String.valueOf(hashMap.get("large_url")));
            } else if (hashMap.get("small_url") != null) {
                aVar.d.add(String.valueOf(hashMap.get("small_url")));
            }
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("title", shareParams.getTitle());
        hashMap2.put("url", shareParams.getTitleUrl());
        hashMap2.put("site", shareParams.getSite());
        hashMap2.put("fromurl", shareParams.getSiteUrl());
        hashMap2.put("type", Integer.valueOf(4));
        hashMap2.put("comment", shareParams.getComment());
        hashMap2.put("summary", shareParams.getText());
        ArrayList arrayList = new ArrayList();
        arrayList.add(shareParams.getImageUrl());
        hashMap2.put(SDcard.IMAGES_DIR, arrayList);
        hashMap2.put(SocialConstants.PARAM_PLAY_URL, null);
        aVar.g = hashMap2;
        return aVar;
    }

    protected void follow(String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 6);
        }
    }

    protected void getFriendList(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 2);
        }
    }

    public String getName() {
        return NAME;
    }

    public int getPlatformId() {
        return 6;
    }

    public int getVersion() {
        return 2;
    }

    protected void initDevInfo(String str) {
        this.a = getDevinfo(d.f);
        this.b = "true".equals(getDevinfo("ShareByAppClient"));
        if (this.a == null || this.a.length() <= 0) {
            this.a = getDevinfo("QQ", d.f);
            if (this.a != null && this.a.length() > 0) {
                copyDevinfo("QQ", NAME);
                this.a = getDevinfo(d.f);
                this.b = "true".equals(getDevinfo("ShareByAppClient"));
                if (ShareSDK.isDebug()) {
                    System.err.println("Try to use the dev info of QQ, this will cause Id and SortId field are always 0.");
                }
            }
        }
    }

    public boolean isClientValid() {
        f a = f.a((Platform) this);
        a.a(this.a);
        return a.a();
    }

    protected void setNetworkDevinfo() {
        this.a = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
        if (this.a == null || this.a.length() <= 0) {
            this.a = getNetworkDevinfo(24, SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
            if (this.a != null && this.a.length() > 0) {
                copyNetworkDevinfo(24, 6);
                this.a = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
                if (ShareSDK.isDebug()) {
                    System.err.println("Try to use the dev info of QQ, this will cause Id and SortId field are always 0.");
                }
            }
        }
    }

    protected void timeline(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 7);
        }
    }

    protected void userInfor(String str) {
        if (str == null || str.length() < 0) {
            str = this.db.getUserId();
        }
        if (str != null && str.length() >= 0) {
            try {
                HashMap d = f.a((Platform) this).d(str);
                if (d == null || d.size() <= 0) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable());
                    }
                } else if (d.containsKey("ret")) {
                    if (((Integer) d.get("ret")).intValue() == 0) {
                        if (str == this.db.getUserId()) {
                            this.db.put(UserTimelineActivity.NICK_NAME, String.valueOf(d.get(UserTimelineActivity.NICK_NAME)));
                            if (d.containsKey("figureurl_qq_1")) {
                                this.db.put("iconQQ", String.valueOf(d.get("figureurl_qq_1")));
                            } else if (d.containsKey("figureurl_qq_2")) {
                                this.db.put("iconQQ", String.valueOf(d.get("figureurl_qq_2")));
                            }
                            if (d.containsKey("figureurl_2")) {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, String.valueOf(d.get("figureurl_2")));
                            } else if (d.containsKey("figureurl_1")) {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, String.valueOf(d.get("figureurl_1")));
                            } else if (d.containsKey("figureurl")) {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, String.valueOf(d.get("figureurl")));
                            }
                            this.db.put("secretType", String.valueOf(d.get("is_yellow_vip")));
                            if (String.valueOf(d.get("is_yellow_vip")).equals("1")) {
                                this.db.put("snsUserLevel", String.valueOf(d.get("level")));
                            }
                            String valueOf = String.valueOf(d.get(SocializeProtocolConstants.PROTOCOL_KEY_GENDER));
                            if (valueOf.equals("男")) {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "0");
                            } else if (valueOf.equals("女")) {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "1");
                            } else {
                                this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "2");
                            }
                        }
                        if (this.listener != null) {
                            this.listener.onComplete(this, 8, d);
                        }
                    } else if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(d)));
                    }
                } else if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable());
                }
            } catch (Throwable th) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, th);
                }
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 8, new RuntimeException("qq account is null"));
        }
    }
}
