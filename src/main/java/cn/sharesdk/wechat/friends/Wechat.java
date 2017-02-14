package cn.sharesdk.wechat.friends;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.statistics.b.f.a;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatHelper;
import cn.sharesdk.wechat.utils.g;
import cn.sharesdk.wechat.utils.k;
import com.alipay.sdk.packet.d;
import com.boohee.model.status.Post;
import com.boohee.utils.Utils;
import com.mob.tools.utils.Ln;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.HashMap;

public class Wechat extends Platform {
    public static final String NAME = Wechat.class.getSimpleName();
    private String a;
    private String b;
    private boolean c;

    public static class ShareParams extends cn.sharesdk.wechat.utils.WechatHelper.ShareParams {
        public ShareParams() {
            this.scene = 0;
        }
    }

    public Wechat(Context context) {
        super(context);
    }

    protected boolean checkAuthorize(int i, Object obj) {
        WechatHelper a = WechatHelper.a();
        a.a(this.context, this.a);
        if (a.c()) {
            if (i == 9 || isAuthValid()) {
                return true;
            }
            if (TextUtils.isEmpty(getDb().get(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN))) {
                innerAuthorize(i, obj);
            } else {
                innerAuthorize(i, obj);
            }
            return false;
        } else if (this.listener == null) {
            return false;
        } else {
            this.listener.onError(this, i, new WechatClientNotExistException());
            return false;
        }
    }

    protected void doAuthorize(String[] strArr) {
        if (!TextUtils.isEmpty(this.a) && !TextUtils.isEmpty(this.b)) {
            WechatHelper a = WechatHelper.a();
            a.a(this.context, this.a);
            if (a.c()) {
                g gVar = new g(this, 22);
                gVar.a(this.a, this.b);
                k kVar = new k(this);
                kVar.a(gVar);
                kVar.a(new a(this));
                try {
                    a.a(kVar);
                } catch (Throwable th) {
                    if (this.listener != null) {
                        this.listener.onError(this, 1, th);
                    }
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 1, new WechatClientNotExistException());
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 8, new Throwable("The params of appID or appSecret is missing !"));
        }
    }

    protected void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        if (this.listener != null) {
            this.listener.onCancel(this, i);
        }
    }

    protected void doShare(cn.sharesdk.framework.Platform.ShareParams shareParams) {
        shareParams.set("scene", Integer.valueOf(0));
        WechatHelper a = WechatHelper.a();
        a.a(this.context, this.a);
        k kVar = new k(this);
        if (this.c) {
            try {
                a.a(kVar, shareParams, this.listener);
                return;
            } catch (Throwable th) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th);
                    return;
                }
                return;
            }
        }
        kVar.a(shareParams, this.listener);
        try {
            a.b(kVar);
        } catch (Throwable th2) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th2);
            }
        }
    }

    protected a filterShareContent(cn.sharesdk.framework.Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        a aVar = new a();
        String text = shareParams.getText();
        aVar.b = text;
        CharSequence imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        Bitmap imageData = shareParams.getImageData();
        if (!TextUtils.isEmpty(imageUrl)) {
            aVar.d.add(imageUrl);
        } else if (imagePath != null) {
            aVar.e.add(imagePath);
        } else if (imageData != null) {
            aVar.f.add(imageData);
        }
        String url = shareParams.getUrl();
        if (url != null) {
            aVar.c.add(url);
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("title", shareParams.getTitle());
        hashMap2.put("url", url);
        hashMap2.put("extInfo", null);
        hashMap2.put(Utils.RESPONSE_CONTENT, text);
        hashMap2.put(Post.IMAGE_TYPE, aVar.d);
        hashMap2.put("musicFileUrl", url);
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
        return 22;
    }

    public int getVersion() {
        return 1;
    }

    protected void initDevInfo(String str) {
        this.a = getDevinfo(d.f);
        this.b = getDevinfo("AppSecret");
        this.c = "true".equals(getDevinfo("BypassApproval"));
        if (this.a == null || this.a.length() <= 0) {
            this.a = getDevinfo("WechatMoments", d.f);
            this.c = "true".equals(getDevinfo("WechatMoments", "BypassApproval"));
            if (this.a == null || this.a.length() <= 0) {
                this.a = getDevinfo("WechatFavorite", d.f);
                if (this.a != null && this.a.length() > 0) {
                    copyDevinfo("WechatFavorite", NAME);
                    this.a = getDevinfo(d.f);
                    if (ShareSDK.isDebug()) {
                        System.err.println("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.");
                        return;
                    }
                    return;
                }
                return;
            }
            copyDevinfo("WechatMoments", NAME);
            this.a = getDevinfo(d.f);
            this.c = "true".equals(getDevinfo("BypassApproval"));
            if (ShareSDK.isDebug()) {
                System.err.println("Try to use the dev info of WechatMoments, this will cause Id and SortId field are always 0.");
            }
        }
    }

    public boolean isClientValid() {
        WechatHelper a = WechatHelper.a();
        a.a(this.context, this.a);
        return a.c();
    }

    @Deprecated
    public boolean isValid() {
        WechatHelper a = WechatHelper.a();
        a.a(this.context, this.a);
        return a.c() && super.isValid();
    }

    protected void setNetworkDevinfo() {
        this.a = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
        this.b = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
        if (this.a == null || this.a.length() <= 0) {
            this.a = getNetworkDevinfo(23, SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
            if (this.a == null || this.a.length() <= 0) {
                this.a = getNetworkDevinfo(37, SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
                if (this.a != null && this.a.length() > 0) {
                    copyNetworkDevinfo(37, 22);
                    this.a = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
                    if (ShareSDK.isDebug()) {
                        System.err.println("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.");
                    }
                }
            } else {
                copyNetworkDevinfo(23, 22);
                this.a = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_ID, d.f);
                if (ShareSDK.isDebug()) {
                    System.err.println("Try to use the dev info of WechatMoments, this will cause Id and SortId field are always 0.");
                }
            }
        }
        if (this.b == null || this.b.length() <= 0) {
            this.b = getNetworkDevinfo(23, SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
            if (this.b == null || this.b.length() <= 0) {
                this.b = getNetworkDevinfo(37, SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
                if (this.b != null && this.b.length() > 0) {
                    copyNetworkDevinfo(37, 22);
                    this.b = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
                    if (ShareSDK.isDebug()) {
                        System.err.println("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.");
                        return;
                    }
                    return;
                }
                return;
            }
            copyNetworkDevinfo(23, 22);
            this.b = getNetworkDevinfo(SocializeProtocolConstants.PROTOCOL_KEY_APP_KEY, "AppSecret");
            if (ShareSDK.isDebug()) {
                System.err.println("Try to use the dev info of WechatMoments, this will cause Id and SortId field are always 0.");
            }
        }
    }

    protected void timeline(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 7);
        }
    }

    protected void userInfor(String str) {
        if (!TextUtils.isEmpty(this.a) && !TextUtils.isEmpty(this.b)) {
            g gVar = new g(this, 22);
            gVar.a(this.a, this.b);
            try {
                gVar.a(this.listener);
            } catch (Throwable th) {
                Ln.e(th);
                if (this.listener != null) {
                    this.listener.onError(this, 8, th);
                }
            }
        } else if (this.listener != null) {
            this.listener.onError(this, 8, new Throwable("The params of appID or appSecret is missing !"));
        }
    }
}
