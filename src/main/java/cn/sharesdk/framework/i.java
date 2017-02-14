package cn.sharesdk.framework;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import cn.sharesdk.framework.statistics.a;
import cn.sharesdk.framework.statistics.b;
import cn.sharesdk.framework.statistics.b.c;
import cn.sharesdk.framework.statistics.b.d;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import dalvik.system.DexFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class i {
    private static boolean a = true;

    private ArrayList<Platform> a(PackageInfo packageInfo, Context context) {
        ArrayList<Platform> arrayList = new ArrayList();
        try {
            DexFile dexFile = new DexFile(packageInfo.applicationInfo.sourceDir);
            Enumeration entries = dexFile.entries();
            dexFile.close();
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
        while (entries != null && entries.hasMoreElements()) {
            String str = (String) entries.nextElement();
            if (str.startsWith("cn.sharesdk") && !str.contains("$")) {
                try {
                    Class cls = Class.forName(str);
                    if (!(cls == null || !Platform.class.isAssignableFrom(cls) || CustomPlatform.class.isAssignableFrom(cls))) {
                        Constructor constructor = cls.getConstructor(new Class[]{Context.class});
                        constructor.setAccessible(true);
                        arrayList.add((Platform) constructor.newInstance(new Object[]{context}));
                    }
                } catch (Throwable th2) {
                    Ln.e(th2);
                }
            }
        }
        return arrayList;
    }

    private PackageInfo c(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            for (PackageInfo packageInfo : packageManager.getInstalledPackages(8192)) {
                if (packageName.equals(packageInfo.packageName)) {
                    return packageInfo;
                }
            }
            return null;
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private ArrayList<Platform> d(Context context) {
        String[] strArr = new String[]{"cn.sharesdk.douban.Douban", "cn.sharesdk.evernote.Evernote", "cn.sharesdk.facebook.Facebook", "cn.sharesdk.renren.Renren", "cn.sharesdk.sina.weibo.SinaWeibo", "cn.sharesdk.sohu.suishenkan.SohuSuishenkan", "cn.sharesdk.kaixin.KaiXin", "cn.sharesdk.linkedin.LinkedIn", "cn.sharesdk.system.email.Email", "cn.sharesdk.system.text.ShortMessage", "cn.sharesdk.tencent.qq.QQ", "cn.sharesdk.tencent.qzone.QZone", "cn.sharesdk.tencent.weibo.TencentWeibo", "cn.sharesdk.twitter.Twitter", "cn.sharesdk.wechat.friends.Wechat", "cn.sharesdk.wechat.moments.WechatMoments", "cn.sharesdk.wechat.favorite.WechatFavorite", "cn.sharesdk.youdao.YouDao", "cn.sharesdk.google.GooglePlus", "cn.sharesdk.foursquare.FourSquare", "cn.sharesdk.pinterest.Pinterest", "cn.sharesdk.flickr.Flickr", "cn.sharesdk.tumblr.Tumblr", "cn.sharesdk.dropbox.Dropbox", "cn.sharesdk.vkontakte.VKontakte", "cn.sharesdk.instagram.Instagram", "cn.sharesdk.yixin.friends.Yixin", "cn.sharesdk.yixin.moments.YixinMoments", "cn.sharesdk.mingdao.Mingdao", "cn.sharesdk.line.Line", "cn.sharesdk.kakao.story.KakaoStory", "cn.sharesdk.kakao.talk.KakaoTalk", "cn.sharesdk.system.bluetooth.Bluetooth", "cn.sharesdk.whatsapp.WhatsApp", "cn.sharesdk.pocket.Pocket", "cn.sharesdk.instapaper.Instapaper", "cn.sharesdk.facebookmessenger.FacebookMessenger", "cn.sharesdk.laiwang.friends.Laiwang", "cn.sharesdk.laiwang.moments.LaiwangMoments"};
        ArrayList<Platform> arrayList = new ArrayList();
        for (String cls : strArr) {
            try {
                Constructor constructor = Class.forName(cls).getConstructor(new Class[]{Context.class});
                constructor.setAccessible(true);
                arrayList.add((Platform) constructor.newInstance(new Object[]{context}));
            } catch (Throwable th) {
                Ln.e(th);
            }
        }
        return arrayList;
    }

    public String a() {
        return "2.6.0";
    }

    public String a(int i, String str, HashMap<Integer, HashMap<String, Object>> hashMap) {
        HashMap hashMap2 = (HashMap) hashMap.get(Integer.valueOf(i));
        if (hashMap2 == null) {
            return null;
        }
        Object obj = hashMap2.get(str);
        return obj == null ? null : String.valueOf(obj);
    }

    public String a(Context context, Bitmap bitmap) {
        return a.a(context).a(bitmap);
    }

    public String a(Context context, String str) {
        return a.a(context).e(str);
    }

    public String a(Context context, String str, String str2, boolean z, int i, String str3) {
        return a.a(context).a(str2, str, i, z, str3);
    }

    public ArrayList<Platform> a(Context context) {
        ArrayList d;
        if (a) {
            d = d(context);
        } else {
            PackageInfo c = c(context);
            if (c == null) {
                return null;
            }
            d = a(c, context);
        }
        a(d);
        return d;
    }

    public void a(int i, int i2, HashMap<Integer, HashMap<String, Object>> hashMap) {
        hashMap.put(Integer.valueOf(i2), (HashMap) hashMap.get(Integer.valueOf(i)));
    }

    public void a(int i, Platform platform) {
        c dVar = new d();
        switch (i) {
            case 1:
                dVar.a = "SHARESDK_ENTER_SHAREMENU";
                break;
            case 2:
                dVar.a = "SHARESDK_CANCEL_SHAREMENU";
                break;
            case 3:
                dVar.a = "SHARESDK_EDIT_SHARE";
                break;
            case 4:
                dVar.a = "SHARESDK_FAILED_SHARE";
                break;
            case 5:
                dVar.a = "SHARESDK_CANCEL_SHARE";
                break;
        }
        if (platform != null) {
            dVar.b = platform.getPlatformId();
        }
        b a = b.a(null);
        if (a != null) {
            a.a(dVar);
        }
    }

    public void a(Context context, String str, Handler handler, boolean z, int i) {
        b a = b.a(context);
        a.a(str);
        a.a(handler);
        a.a(z);
        a.a(i);
        a.startThread();
    }

    public void a(String str, int i) {
        b a = b.a(null);
        if (a != null) {
            c aVar = new cn.sharesdk.framework.statistics.b.a();
            aVar.b = str;
            aVar.a = i;
            a.a(aVar);
        }
    }

    public void a(ArrayList<Platform> arrayList) {
        if (arrayList != null) {
            Collections.sort(arrayList, new j(this));
        }
    }

    public boolean a(HashMap<String, Object> hashMap, HashMap<Integer, HashMap<String, Object>> hashMap2) {
        if (hashMap == null || hashMap.size() <= 0) {
            return false;
        }
        ArrayList arrayList = (ArrayList) hashMap.get("fakelist");
        if (arrayList == null) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap hashMap3 = (HashMap) it.next();
            if (hashMap3 != null) {
                int parseInt;
                try {
                    parseInt = R.parseInt(String.valueOf(hashMap3.get("snsplat")));
                } catch (Throwable th) {
                    Ln.e(th);
                    parseInt = -1;
                }
                if (parseInt != -1) {
                    hashMap2.put(Integer.valueOf(parseInt), hashMap3);
                }
            }
        }
        return true;
    }

    public int b() {
        return 51;
    }

    public void b(Context context) {
        b.a(context).stopThread();
    }
}
