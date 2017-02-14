package com.umeng.socialize.controller.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.umeng.socialize.bean.LIKESTATUS;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMFriend;
import com.umeng.socialize.bean.UMToken;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.AuthService;
import com.umeng.socialize.controller.CommentService;
import com.umeng.socialize.controller.LikeService;
import com.umeng.socialize.controller.ShareService;
import com.umeng.socialize.controller.UserCenterService;
import com.umeng.socialize.controller.d;
import com.umeng.socialize.controller.d.a;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.GetPlatformKeyResponse;
import com.umeng.socialize.net.b;
import com.umeng.socialize.net.base.SocializeClient;
import com.umeng.socialize.net.e;
import com.umeng.socialize.net.h;
import com.umeng.socialize.net.i;
import com.umeng.socialize.net.k;
import com.umeng.socialize.net.l;
import com.umeng.socialize.net.m;
import com.umeng.socialize.net.n;
import com.umeng.socialize.net.r;
import com.umeng.socialize.net.s;
import com.umeng.socialize.net.t;
import com.umeng.socialize.net.u;
import com.umeng.socialize.net.v;
import com.umeng.socialize.net.w;
import com.umeng.socialize.net.x;
import com.umeng.socialize.net.y;
import com.umeng.socialize.utils.Log;

public class BaseController {
    private static       int    g = -1;
    private static final String h = "installed";
    private static final String i = "umsocial_uid";
    protected SocializeEntity a;
    protected ShareService      b = ((ShareService) d.a(this.a, a.SHARE, new Object[0]));
    protected CommentService    c = ((CommentService) d.a(this.a, a.COMMENT, new Object[0]));
    protected LikeService       d = ((LikeService) d.a(this.a, a.LIKE, new Object[0]));
    protected AuthService       e = ((AuthService) d.a(this.a, a.AUTH, new Object[0]));
    protected UserCenterService f = ((UserCenterService) d.a(this.a, a.USER_CENTER, this.e));

    public BaseController(SocializeEntity socializeEntity) {
        this.a = socializeEntity;
    }

    public SocializeEntity getEntity() {
        return this.a;
    }

    protected boolean a(Context context) {
        if (!this.a.mInitialized) {
            actionBarInit(context);
        }
        if (this.a.mInitialized) {
            return true;
        }
        return false;
    }

    public int actionBarInit(Context context) {
        boolean z = true;
        if (g == -1) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SocializeConstants
                    .SOCIAL_PREFERENCE_NAME, 0);
            synchronized (sharedPreferences) {
                g = sharedPreferences.getInt(h, 1);
            }
        }
        if (TextUtils.isEmpty(SocializeConstants.UID)) {
            SocializeConstants.UID = context.getSharedPreferences(SocializeConstants
                    .SOCIAL_PREFERENCE_NAME, 0).getString(i, "");
            Log.i("com.umeng.socialize", "set  field UID from preference.");
        }
        b bVar = (b) new SocializeClient().execute(new com.umeng.socialize.net.a(context, this.a,
                g == 0 ? 0 : 1));
        if (bVar == null) {
            return StatusCode.ST_CODE_SDK_NORESPONSE;
        }
        Editor edit;
        if (g == 1) {
            edit = context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_NAME, 0)
                    .edit();
            synchronized (edit) {
                edit.putInt(h, 0);
                edit.commit();
                g = 0;
            }
        }
        if (bVar.mStCode == 200) {
            if (TextUtils.isEmpty(SocializeConstants.UID) || !SocializeConstants.UID.equals(bVar
                    .h)) {
                Log.i("com.umeng.socialize", "update UID src=" + SocializeConstants.UID + " " +
                        "dest=" + bVar.h);
                SocializeConstants.UID = bVar.h;
                edit = context.getSharedPreferences(SocializeConstants.SOCIAL_PREFERENCE_NAME, 0)
                        .edit();
                synchronized (edit) {
                    edit.putString(i, SocializeConstants.UID);
                    edit.commit();
                }
            }
            synchronized (this.a) {
                this.a.setCommentCount(bVar.b);
                this.a.mEntityKey = bVar.e;
                this.a.mSessionID = bVar.d;
                if (bVar.f != 0) {
                    z = false;
                }
                this.a.setNew(z);
                this.a.setIlikey(bVar.g == 0 ? LIKESTATUS.UNLIKE : LIKESTATUS.LIKE);
                this.a.setLikeCount(bVar.c);
                this.a.setPv(bVar.a);
                this.a.setShareCount(bVar.j);
                this.a.mInitialized = true;
            }
        }
        return bVar.mStCode;
    }

    public l getFriends(Context context, SHARE_MEDIA share_media, String str) throws
            SocializeException {
        l lVar = (l) new SocializeClient().execute(new k(context, this.a, share_media, str));
        if (lVar == null) {
            throw new SocializeException((int) StatusCode.ST_CODE_SDK_NORESPONSE, "Response is " +
                    "null...");
        } else if (lVar.mStCode != 200) {
            throw new SocializeException(lVar.mStCode, lVar.mMsg);
        } else {
            if (lVar.a != null) {
                for (UMFriend usid : lVar.a) {
                    usid.setUsid(str);
                }
            }
            return lVar;
        }
    }

    public i getUserInfo(Context context) throws SocializeException {
        i iVar = (i) new SocializeClient().execute(new h(context, this.a));
        if (iVar == null) {
            throw new SocializeException((int) StatusCode.ST_CODE_SDK_NORESPONSE, "Response is " +
                    "null...");
        } else if (iVar.mStCode == 200) {
            return iVar;
        } else {
            throw new SocializeException(iVar.mStCode, iVar.mMsg);
        }
    }

    public MultiStatus follow(Context context, SNSPair sNSPair, String... strArr) {
        if (sNSPair == null || TextUtils.isEmpty(sNSPair.mUsid) || sNSPair.mPaltform == null ||
                strArr == null || strArr.length == 0) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_SHARE_PARAMS_ERROR);
        }
        n nVar = (n) new SocializeClient().execute(new m(context, this.a, sNSPair, strArr));
        if (nVar == null) {
            return new MultiStatus(StatusCode.ST_CODE_SDK_NORESPONSE);
        }
        MultiStatus multiStatus = new MultiStatus(nVar.mStCode);
        multiStatus.setInfoCode(nVar.a);
        return multiStatus;
    }

    public y getPlatformInfo(Context context, SNSPair sNSPair) {
        return (y) new SocializeClient().execute(new x(context, this.a, sNSPair));
    }

    public GetPlatformKeyResponse getPlatformKeys(Context context) {
        return (GetPlatformKeyResponse) new SocializeClient().execute(new e(context, this.a));
    }

    public int uploadPlatformToken(Context context, UMToken uMToken) {
        if (this.e instanceof a) {
            return ((a) this.e).a(context, uMToken);
        }
        return StatusCode.ST_CODE_SDK_SHARE_PARAMS_ERROR;
    }

    public String uploadImage(Context context, UMediaObject uMediaObject, String str) {
        w wVar = (w) new SocializeClient().execute(new v(context, this.a, uMediaObject, str));
        if (wVar != null) {
            return wVar.a;
        }
        return "";
    }

    public int uploadStatisticsData(Context context) {
        s sVar = (s) new SocializeClient().execute(new r(context, this.a));
        if (sVar != null) {
            return sVar.mStCode;
        }
        return StatusCode.ST_CODE_SDK_UNKNOW;
    }

    public int uploadKeySecret(Context context) {
        u uVar = (u) new SocializeClient().execute(new t(context, this.a));
        if (uVar != null) {
            return uVar.mStCode;
        }
        return StatusCode.ST_CODE_SDK_UNKNOW;
    }
}
