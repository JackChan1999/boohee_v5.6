package cn.sharesdk.sina.weibo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.statistics.b.f.a;
import com.alipay.sdk.cons.b;
import com.boohee.status.UserTimelineActivity;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.io.File;
import java.util.HashMap;

public class SinaWeibo extends Platform {
    public static final String NAME = SinaWeibo.class.getSimpleName();
    private String a;
    private String b;
    private String c;
    private boolean d;

    public static class ShareParams extends cn.sharesdk.framework.Platform.ShareParams {
        @Deprecated
        public String imageUrl;
        @Deprecated
        public float latitude;
        @Deprecated
        public float longitude;
    }

    public SinaWeibo(Context context) {
        super(context);
    }

    private void a(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        if (shareParams.getImageData() == null && TextUtils.isEmpty(shareParams.getImagePath()) && !TextUtils.isEmpty(shareParams.getImageUrl())) {
            try {
                File file = new File(BitmapHelper.downloadBitmap(getContext(), shareParams.getImageUrl()));
                if (file.exists()) {
                    shareParams.setImagePath(file.getAbsolutePath());
                }
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
        Intent intent = new Intent();
        intent.putExtra("platformID", 1);
        intent.putExtra("action", 2);
        a aVar = new a(this, this.a, this.c);
        aVar.a(this.listener, shareParams, this.d);
        aVar.show(getContext(), intent);
    }

    private void a(String[] strArr) {
        a aVar = new a(this, this.a, this.c);
        aVar.a(new d(this), strArr, isSSODisable());
        Intent intent = new Intent();
        intent.putExtra("platformID", 1);
        intent.putExtra("action", 1);
        aVar.show(getContext(), intent);
    }

    private void b(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        String string;
        i a;
        String imagePath;
        String imageUrl;
        HashMap a2;
        CharSequence text = shareParams.getText();
        if (TextUtils.isEmpty(text)) {
            int stringRes = R.getStringRes(getContext(), "weibo_upload_content");
            if (stringRes > 0) {
                string = getContext().getString(stringRes);
                a = i.a((Platform) this);
                string = getShortLintk(string, false);
                imagePath = shareParams.getImagePath();
                imageUrl = shareParams.getImageUrl();
                if (this.d || !a.a()) {
                    a2 = a.a(string, imageUrl, imagePath, shareParams.getLongitude(), shareParams.getLatitude());
                    if (a2 != null) {
                        if (this.listener != null) {
                            this.listener.onError(this, 9, new Throwable());
                        }
                    } else if (a2.containsKey("error_code") || ((Integer) a2.get("error_code")).intValue() == 0) {
                        a2.put("ShareParams", shareParams);
                        if (this.listener != null) {
                            this.listener.onComplete(this, 9, a2);
                        }
                    } else if (this.listener != null) {
                        this.listener.onError(this, 9, new Throwable(new Hashon().fromHashMap(a2)));
                        return;
                    } else {
                        return;
                    }
                }
                try {
                    a.a(string, shareParams, this.listener);
                    return;
                } catch (Throwable th) {
                    this.listener.onError(this, 9, th);
                    return;
                }
            }
        }
        CharSequence charSequence = text;
        a = i.a((Platform) this);
        string = getShortLintk(string, false);
        imagePath = shareParams.getImagePath();
        imageUrl = shareParams.getImageUrl();
        if (this.d) {
        }
        try {
            a2 = a.a(string, imageUrl, imagePath, shareParams.getLongitude(), shareParams.getLatitude());
            if (a2 != null) {
                if (a2.containsKey("error_code")) {
                }
                a2.put("ShareParams", shareParams);
                if (this.listener != null) {
                    this.listener.onComplete(this, 9, a2);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 9, new Throwable());
            }
        } catch (Throwable th2) {
            this.listener.onError(this, 9, th2);
        }
    }

    private void b(String[] strArr) {
        i a = i.a((Platform) this);
        a.a(this.a, this.b);
        a.a(this.c);
        a.a(strArr);
        a.a(new e(this, a), isSSODisable());
    }

    protected boolean checkAuthorize(int i, Object obj) {
        boolean z;
        try {
            for (ActivityInfo activityInfo : getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 1).activities) {
                if ("cn.sharesdk.sina.weibo.SinaActivity".equals(activityInfo.name)) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (z) {
                Class.forName("com.sina.weibo.sdk.auth.AuthInfo");
                z = true;
                i a = i.a((Platform) this);
                if (z && i == 9 && a.a()) {
                    return true;
                }
                if (i == 9 && this.d && a.a()) {
                    return true;
                }
                if (isAuthValid()) {
                    a.a(this.a, this.b);
                    a.b(this.db.getToken());
                    return true;
                }
                innerAuthorize(i, obj);
                return false;
            }
            throw new Throwable("cn.sharesdk.sina.weibo.SinaActivity is not registered");
        } catch (Throwable th) {
            z = false;
        }
    }

    protected void doAuthorize(String[] strArr) {
        Object obj = 1;
        try {
            for (ActivityInfo activityInfo : getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 1).activities) {
                if ("cn.sharesdk.sina.weibo.SinaActivity".equals(activityInfo.name)) {
                    break;
                }
            }
            obj = null;
            if (obj == null) {
                throw new Throwable("cn.sharesdk.sina.weibo.SinaActivity is not registered");
            }
            Class.forName("com.sina.weibo.sdk.auth.AuthInfo");
            a(strArr);
        } catch (Throwable th) {
            b(strArr);
        }
    }

    protected void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        try {
            HashMap a = i.a((Platform) this).a(str, str2, (HashMap) hashMap, (HashMap) hashMap2);
            if (a == null || a.size() <= 0) {
                if (this.listener != null) {
                    this.listener.onError(this, i, new Throwable());
                }
            } else if (!a.containsKey("error_code") || ((Integer) a.get("error_code")).intValue() == 0) {
                if (this.listener != null) {
                    this.listener.onComplete(this, i, a);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
            }
        } catch (Throwable th) {
            this.listener.onError(this, i, th);
        }
    }

    protected void doShare(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        Object obj = 1;
        try {
            for (ActivityInfo activityInfo : getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 1).activities) {
                if ("cn.sharesdk.sina.weibo.SinaActivity".equals(activityInfo.name)) {
                    break;
                }
            }
            obj = null;
            if (obj == null) {
                throw new Throwable("cn.sharesdk.sina.weibo.SinaActivity is not registered");
            }
            Class.forName("com.sina.weibo.sdk.api.share.WeiboShareSDK");
            a(shareParams);
        } catch (Throwable th) {
            b(shareParams);
        }
    }

    protected a filterShareContent(cn.sharesdk.framework.Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        a aVar = new a();
        aVar.b = shareParams.getText();
        if (hashMap != null) {
            aVar.a = String.valueOf(hashMap.get("id"));
            aVar.d.add(String.valueOf(hashMap.get("original_pic")));
            aVar.g = hashMap;
        }
        return aVar;
    }

    protected void follow(String str) {
        try {
            HashMap d = i.a((Platform) this).d(str);
            if (d == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 6, new Throwable());
                }
            } else if (!d.containsKey("error_code") || ((Integer) d.get("error_code")).intValue() == 0) {
                if (this.listener != null) {
                    this.listener.onComplete(this, 6, d);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 6, new Throwable(new Hashon().fromHashMap(d)));
            }
        } catch (Throwable th) {
            this.listener.onError(this, 6, th);
        }
    }

    protected void getFriendList(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            Object userId = this.db.getUserId();
        }
        if (TextUtils.isEmpty(userId)) {
            userId = this.db.get(UserTimelineActivity.NICK_NAME);
        }
        if (!TextUtils.isEmpty(userId)) {
            try {
                HashMap b = i.a((Platform) this).b(i, i2, userId);
                if (b == null) {
                    if (this.listener != null) {
                        this.listener.onError(this, 2, new Throwable());
                    }
                } else if (!b.containsKey("error_code") || ((Integer) b.get("error_code")).intValue() == 0) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, 2, b);
                    }
                } else if (this.listener != null) {
                    this.listener.onError(this, 2, new Throwable(new Hashon().fromHashMap(b)));
                }
            } catch (Throwable th) {
                this.listener.onError(this, 2, th);
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 2, new RuntimeException("Both weibo id and screen_name are null"));
        }
    }

    public String getName() {
        return NAME;
    }

    protected int getPlatformId() {
        return 1;
    }

    public int getVersion() {
        return 1;
    }

    protected void initDevInfo(String str) {
        this.a = getDevinfo("AppKey");
        this.b = getDevinfo("AppSecret");
        this.c = getDevinfo("RedirectUrl");
        this.d = "true".equals(getDevinfo("ShareByAppClient"));
    }

    public boolean isClientValid() {
        return i.a((Platform) this).a();
    }

    protected void setNetworkDevinfo() {
        this.a = getNetworkDevinfo(b.h, "AppKey");
        this.b = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
        this.c = getNetworkDevinfo("redirect_uri", "RedirectUrl");
    }

    protected void timeline(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get(UserTimelineActivity.NICK_NAME);
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                HashMap a = i.a((Platform) this).a(i, i2, str);
                if (a == null) {
                    if (this.listener != null) {
                        this.listener.onError(this, 7, new Throwable());
                    }
                } else if (!a.containsKey("error_code") || ((Integer) a.get("error_code")).intValue() == 0) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, 7, a);
                    }
                } else if (this.listener != null) {
                    this.listener.onError(this, 7, new Throwable(new Hashon().fromHashMap(a)));
                }
            } catch (Throwable th) {
                this.listener.onError(this, 7, th);
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 7, new RuntimeException("Both weibo id and screen_name are null"));
        }
    }

    protected void userInfor(String str) {
        Object obj = 1;
        Object obj2 = null;
        if (TextUtils.isEmpty(str)) {
            Object userId = this.db.getUserId();
            obj2 = 1;
        }
        if (TextUtils.isEmpty(userId)) {
            userId = this.db.get(UserTimelineActivity.NICK_NAME);
        } else {
            obj = obj2;
        }
        if (!TextUtils.isEmpty(userId)) {
            try {
                HashMap c = i.a((Platform) this).c(userId);
                if (c == null) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable());
                    }
                } else if (!c.containsKey("error_code") || ((Integer) c.get("error_code")).intValue() == 0) {
                    if (obj != null) {
                        this.db.putUserId(String.valueOf(c.get("id")));
                        this.db.put(UserTimelineActivity.NICK_NAME, String.valueOf(c.get("screen_name")));
                        this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, String.valueOf(c.get("avatar_hd")));
                        if (String.valueOf(c.get("verified")).equals("true")) {
                            this.db.put("secretType", "1");
                        } else {
                            this.db.put("secretType", "0");
                        }
                        this.db.put("secret", String.valueOf(c.get("verified_reason")));
                        String valueOf = String.valueOf(c.get(SocializeProtocolConstants.PROTOCOL_KEY_GENDER));
                        if (valueOf.equals("m")) {
                            this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "0");
                        } else if (valueOf.equals("f")) {
                            this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "1");
                        } else {
                            this.db.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "2");
                        }
                        this.db.put("snsUserUrl", "http://weibo.com/" + String.valueOf(c.get(SocializeProtocolConstants.PROTOCOL_KEY_PROFILE_URL)));
                        this.db.put("resume", String.valueOf(c.get("description")));
                        this.db.put("followerCount", String.valueOf(c.get("followers_count")));
                        this.db.put("favouriteCount", String.valueOf(c.get("friends_count")));
                        this.db.put("shareCount", String.valueOf(c.get("statuses_count")));
                        this.db.put("snsregat", String.valueOf(R.dateToLong(String.valueOf(c.get("created_at")))));
                    }
                    if (this.listener != null) {
                        this.listener.onComplete(this, 8, c);
                    }
                } else if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(c)));
                }
            } catch (Throwable th) {
                this.listener.onError(this, 8, th);
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 8, new RuntimeException("Both weibo id and screen_name are null"));
        }
    }
}
