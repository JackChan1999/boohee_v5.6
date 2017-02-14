package com.umeng.socialize.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.bean.UMLocation;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.location.DefaultLocationProvider;
import com.umeng.socialize.location.GetLocationTask;
import com.umeng.socialize.location.SocializeLocationManager;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.media.UMediaObject.MediaType;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.view.wigets.KeyboardListenRelativeLayout;

import java.lang.ref.WeakReference;
import java.util.Set;

public class ShareActivity extends Activity {
    public static final  String FOLLOW_FILE_NAME = "umeng_follow";
    private static final String b                = ShareActivity.class.getName();
    private static final int    c                = 140;
    private int A;
    private boolean B = false;
    private Dialog       C;
    private UMediaObject D;
    private SocializeConfig E = SocializeConfig.getSocializeConfig();
    private Set<String>     F = null;
    private GetLocationTask G = null;
    protected ImageView                    a;
    private   Button                       d;
    private   Button                       e;
    private   EditText                     f;
    private   ImageButton                  g;
    private   ImageButton                  h;
    private   View                         i;
    private   View                         j;
    private   View                         k;
    private   TextView                     l;
    private   RelativeLayout               m;
    private   CheckBox                     n;
    private   KeyboardListenRelativeLayout o;
    private   SnsPostListener              p;
    private   SocializeEntity              q;
    private   ProgressDialog               r;
    private   ProgressBar                  s;
    private   Context                      t;
    private   boolean                      u;
    private   UMSocialService              v;
    private   String                       w;
    private   SHARE_MEDIA                  x;
    private   DefaultLocationProvider      y;
    private   Location                     z;

    static class a implements SnsPostListener {
        WeakReference<ShareActivity> a = null;

        public a(ShareActivity shareActivity) {
            this.a = new WeakReference(shareActivity);
        }

        public void onStart() {
            ShareActivity shareActivity = (ShareActivity) this.a.get();
            if (shareActivity != null && shareActivity.isFinishing()) {
                shareActivity.a();
                SocializeUtils.safeCloseDialog(shareActivity.r);
            }
        }

        public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
            if (i == 200) {
                socializeEntity.incrementSc();
            }
        }
    }

    protected void onCreate(Bundle bundle) {
        this.B = SocializeUtils.isFloatWindowStyle(this);
        if (!this.B) {
            setTheme(ResContainer.getResourceId(this, ResType.STYLE, "Theme.UMDefault"));
        }
        super.onCreate(bundle);
        this.t = this;
        setContentView(ResContainer.getResourceId(this, ResType.LAYOUT,
                "umeng_socialize_post_share"));
        LayoutParams attributes = getWindow().getAttributes();
        attributes.softInputMode = 16;
        if (this.B) {
            int[] floatWindowSize = SocializeUtils.getFloatWindowSize(this.t);
            attributes.width = floatWindowSize[0];
            attributes.height = floatWindowSize[1];
        }
        getWindow().setAttributes(attributes);
        this.o = (KeyboardListenRelativeLayout) findViewById(ResContainer.getResourceId(this,
                ResType.ID, "umeng_socialize_share_root"));
        this.o.setOnKeyboardStateChangedListener(new t(this));
        this.w = getIntent().getStringExtra(SocializeProtocolConstants.PROTOCOL_KEY_DESCRIPTOR);
        this.x = SHARE_MEDIA.convertToEmun(getIntent().getStringExtra("sns"));
        if (this.x == null) {
            Toast.makeText(this, "出错啦！", 0).show();
            a();
        }
        if (TextUtils.isEmpty(this.w)) {
            Log.e(b, "####No EntityPool key..............");
            a();
        }
        this.v = UMServiceFactory.getUMSocialService(this.w);
        this.q = this.v.getEntity();
        this.D = this.q.getMedia();
        if (this.D instanceof SimpleShareContent) {
            if (this.D instanceof BaseShareContent) {
                this.D = ((BaseShareContent) this.D).getShareMedia();
            } else {
                this.D = ((SimpleShareContent) this.D).getShareImage();
            }
        }
        this.q.addStatisticsData(this, this.x, 15);
        if (this.x == SHARE_MEDIA.QQ) {
            this.D = this.E.getSsoHandler(SHARE_MEDIA.QQ.getReqCode()).mShareMedia;
        }
    }

    protected void onResume() {
        d();
        if (this.E.isDefaultShareLocation()) {
            c();
            l();
        }
        this.f.requestFocus();
        super.onResume();
    }

    private void c() {
        Log.d(b, "initLocationProvider.....");
        this.y = new DefaultLocationProvider();
        SocializeLocationManager socializeLocationManager = new SocializeLocationManager();
        socializeLocationManager.init(this);
        this.y.setLocationManager(socializeLocationManager);
        this.y.init(this);
        this.h.setImageResource(ResContainer.getResourceId(this, ResType.DRAWABLE,
                "umeng_socialize_location_off"));
    }

    private void d() {
        this.f = (EditText) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_edittext"));
        Object shareContent = this.q.getShareContent();
        if (this.x == SHARE_MEDIA.QQ) {
            shareContent = this.E.getSsoHandler(SHARE_MEDIA.QQ.getReqCode()).mShareContent;
        }
        if (!TextUtils.isEmpty(shareContent)) {
            this.f.setText(shareContent);
            this.f.setSelection(shareContent.length());
        }
        this.h = (ImageButton) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_location_ic"));
        this.i = findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_location_progressbar"));
        this.h.setOnClickListener(new ab(this));
        if (this.B) {
            q();
            this.k = s();
            if (this.k != null) {
                this.k.setVisibility(8);
                this.o.addView(this.k, -1, -1);
            }
        }
        this.g = (ImageButton) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_at"));
        if (p()) {
            this.C = r();
            if (this.C != null) {
                this.C.setOwnerActivity(this);
            }
        } else {
            this.g.setVisibility(8);
        }
        if (this.B && this.k == null) {
            this.g.setVisibility(8);
        }
        this.g.setOnClickListener(new ac(this));
        this.l = (TextView) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_word_num"));
        this.u = h();
        this.d = (Button) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_title_bar_leftBt"));
        this.d.setOnClickListener(new ad(this));
        this.e = (Button) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_title_bar_rightBt"));
        TextView textView = (TextView) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_title_bar_middleTv"));
        CharSequence charSequence = "分享到" + SocialSNSHelper.getShowWord(this, this.x);
        if (this.x == SHARE_MEDIA.QQ) {
            charSequence = "分享到QQ";
        }
        textView.setText(charSequence);
        this.a = (ImageView) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_previewImg"));
        this.j = findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_previewImg_remove"));
        this.j.setOnClickListener(new ae(this));
        this.s = (ProgressBar) findViewById(ResContainer.getResourceId(this, ResType.ID,
                "umeng_socialize_share_previewImg_progressbar"));
        e();
        this.m = (RelativeLayout) findViewById(ResContainer.getResourceId(this.t, ResType.ID,
                "umeng_socialize_follow_layout"));
        if (this.v != null) {
            this.F = this.E.getFollowFids(this.x);
        }
        if (!n()) {
            this.m.setVisibility(8);
        }
        this.n = (CheckBox) findViewById(ResContainer.getResourceId(this.t, ResType.ID,
                "umeng_socialize_follow_check"));
        this.r = new ProgressDialog(this.t);
        this.r.setProgressStyle(0);
        this.r.setMessage("发送中...");
        this.r.setCancelable(false);
        this.f.addTextChangedListener(new af(this));
        this.p = new a(this);
        this.e.setOnClickListener(new ag(this));
    }

    private void e() {
        if (this.D != null) {
            MediaType mediaType = this.D.getMediaType();
            if (mediaType == MediaType.MUSIC || mediaType == MediaType.VEDIO) {
                String str = "umeng_socialize_share_music";
                if (mediaType == MediaType.VEDIO) {
                    str = "umeng_socialize_share_video";
                }
                this.a.setImageResource(ResContainer.getResourceId(this.t, ResType.DRAWABLE, str));
                this.a.setVisibility(0);
                this.j.setVisibility(0);
            } else if (mediaType == MediaType.IMAGE) {
                this.a.setImageDrawable(null);
                int resourceId = ResContainer.getResourceId(this.t, ResType.DRAWABLE,
                        "umeng_socialize_share_pic");
                UMImage uMImage = (UMImage) this.D;
                this.s.setVisibility(0);
                this.a.setVisibility(4);
                new ah(this, uMImage, resourceId).execute();
            }
        }
    }

    private void a(int i, Bitmap bitmap) {
        try {
            this.a.setImageBitmap(bitmap);
        } catch (Exception e) {
            this.a.setImageResource(i);
        }
        this.a.setVisibility(0);
        this.j.setVisibility(0);
    }

    private void f() {
        UMShareMsg uMShareMsg = new UMShareMsg();
        uMShareMsg.mText = this.f.getText().toString();
        uMShareMsg.setMediaData(this.D);
        uMShareMsg.mLocation = UMLocation.build(this.z);
        this.v.getEntity().setShareMsg(uMShareMsg);
        this.v.directShare(this.t, this.x, this.p);
        o();
    }

    private void g() {
        this.r.setMessage("载入中,请稍候...");
        this.r.show();
        UMSsoHandler ssoHandler = this.v.getConfig().getSsoHandler(HandlerRequestCode
                .QQ_REQUEST_CODE);
        if (ssoHandler != null) {
            ssoHandler.mShareContent = this.f.getText().toString();
            ssoHandler.shareTo();
            return;
        }
        Log.d(b, "请先调用mController.getConfig().supportQQPlatform(getActivity(), \"你的app id\");" +
                "支持QQ平台");
    }

    protected void a() {
        if (this.A == -3) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow
                    (getWindow().peekDecorView().getWindowToken(), 0);
            new Handler().postDelayed(new ai(this), 500);
            return;
        }
        finish();
    }

    private boolean h() {
        int countContentLength = 140 - SocializeUtils.countContentLength(this.f.getText()
                .toString());
        Log.d(b, "onTextChanged " + countContentLength + "   " + SocializeUtils
                .countContentLength(this.f.getText().toString()));
        this.l.setText("" + countContentLength);
        if (countContentLength >= 0) {
            return false;
        }
        return true;
    }

    protected void onStop() {
        if (this.r != null && this.r.isShowing()) {
            this.r.dismiss();
        }
        super.onStop();
    }

    protected void onDestroy() {
        if (this.y != null) {
            this.y.destroy();
        }
        if (this.G != null) {
            this.G.cancel(true);
        }
        super.onDestroy();
    }

    private void i() {
        if (this.z != null) {
            new Builder(this).setMessage("是否删除位置信息？").setCancelable(false).setPositiveButton("是",
                    new v(this)).setNegativeButton("否", new u(this)).create().show();
        } else {
            l();
        }
    }

    private void j() {
        if (this.a.getDrawable() != null) {
            String str = "";
            if (this.D != null) {
                switch (aa.a[this.D.getMediaType().ordinal()]) {
                    case 1:
                        str = "音乐";
                        break;
                    case 2:
                        str = "图片";
                        break;
                    case 3:
                        str = "视频";
                        break;
                }
            }
            new Builder(this).setMessage("你确定删除" + str + "吗？").setCancelable(false)
                    .setPositiveButton("确定", new x(this)).setNegativeButton("取消", new w(this))
                    .create().show();
        }
    }

    private void k() {
        UMSsoHandler ssoHandler = this.E.getSsoHandler(SHARE_MEDIA.QQ.getReqCode());
        if (ssoHandler != null) {
            ssoHandler.mShareMedia = null;
        }
    }

    private void l() {
        if (this.y == null) {
            c();
        }
        if (!(this.G == null || this.G.getStatus() == Status.FINISHED)) {
            this.G.cancel(true);
        }
        this.G = new y(this, this.y);
        this.G.execute(new Void[0]);
    }

    private void a(boolean z) {
        if (z) {
            this.h.setVisibility(8);
            this.i.setVisibility(0);
        } else if (this.z == null) {
            this.h.setImageResource(ResContainer.getResourceId(this, ResType.DRAWABLE,
                    "umeng_socialize_location_off"));
            this.h.setVisibility(0);
            this.i.setVisibility(8);
        } else {
            this.h.setImageResource(ResContainer.getResourceId(this, ResType.DRAWABLE,
                    "umeng_socialize_location_on"));
            this.h.setVisibility(0);
            this.i.setVisibility(8);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!SocializeConstants.BACKKEY_COMPLETE_CLOSE || keyEvent.getKeyCode() != 4) {
            return super.dispatchKeyEvent(keyEvent);
        }
        if (this.r.isShowing()) {
            this.r.dismiss();
        }
        new Handler().postDelayed(new z(this), 400);
        SocializeEntity entity = this.v.getEntity();
        for (SnsPostListener onComplete : (SnsPostListener[]) this.v.getConfig().getListener
                (SnsPostListener.class)) {
            onComplete.onComplete(this.x, StatusCode.ST_CODE_ERROR_CANCEL, entity);
        }
        if (this.v != null) {
            this.v.getConfig().cleanListeners();
        }
        return true;
    }

    public void inputAt(SpannableString spannableString) {
        this.f.getText().insert(this.f.getSelectionStart(), spannableString);
    }

    private void m() {
        Editor edit = getSharedPreferences(FOLLOW_FILE_NAME, 0).edit();
        edit.putBoolean(this.x.toString(), false);
        edit.commit();
    }

    private boolean n() {
        if (this.F == null || this.F.size() <= 0) {
            return false;
        }
        if (this.x == SHARE_MEDIA.SINA || this.x == SHARE_MEDIA.TENCENT) {
            return getSharedPreferences(FOLLOW_FILE_NAME, 0).getBoolean(this.x.toString(), true);
        }
        return false;
    }

    private void o() {
        if (this.m.getVisibility() == 0 && this.n.isChecked() && this.F != null && this.F.size()
                > 0) {
            String[] strArr = new String[this.F.size()];
            this.F.toArray(strArr);
            this.v.follow(this.t, this.x, null, strArr);
            m();
        }
    }

    private boolean p() {
        if (this.x == SHARE_MEDIA.QZONE || this.x == SHARE_MEDIA.QQ) {
            return false;
        }
        return true;
    }

    private void q() {
        try {
            Class cls = Class.forName("com.umeng.socialize.view.FriendSelView");
            cls.getDeclaredField("SHOWSILDEBAR").set(cls, Boolean.FALSE);
        } catch (Exception e) {
            e.printStackTrace();
            u();
        }
    }

    private Dialog r() {
        try {
            return (Dialog) Class.forName("com.umeng.socialize.view.ShareAtDialogV2")
                    .getConstructor(new Class[]{ShareActivity.class, SHARE_MEDIA.class, String
                            .class}).newInstance(new Object[]{this, this.x, this.w});
        } catch (Exception e) {
            e.printStackTrace();
            u();
            return null;
        }
    }

    private View s() {
        try {
            return (View) Class.forName("com.umeng.socialize.view.FriendSelView").getConstructor
                    (new Class[]{Context.class}).newInstance(new Object[]{this});
        } catch (Exception e) {
            e.printStackTrace();
            u();
            return null;
        }
    }

    private boolean t() {
        boolean z = false;
        try {
            if (this.k == null) {
                this.k = s();
            }
            if (this.k != null) {
                z = View.class.getDeclaredField("mInitialized").getBoolean(this.k);
            }
        } catch (Exception e) {
            e.printStackTrace();
            u();
        }
        return z;
    }

    private void a(Object obj, String str) {
        if (this.k != null) {
            Class[] clsArr;
            Object[] objArr;
            if ("init".equals(str)) {
                clsArr = new Class[]{Activity.class, SHARE_MEDIA.class, String.class};
                objArr = new Object[]{this, this.x, this.w};
            } else if ("onShow".equals(str)) {
                clsArr = new Class[0];
                objArr = new Object[0];
            } else {
                return;
            }
            try {
                this.k.getClass().getMethod(str, clsArr).invoke(this.k, objArr);
            } catch (Exception e) {
                e.printStackTrace();
                u();
            }
        }
    }

    private void u() {
        Log.w(b, "如果需要使用‘@好友’功能，请添加相应的jar文件；否则忽略此信息");
    }
}
