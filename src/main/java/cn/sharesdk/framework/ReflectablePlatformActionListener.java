package cn.sharesdk.framework;

import android.os.Handler.Callback;
import android.os.Message;
import com.mob.tools.utils.UIHandler;
import java.util.HashMap;

public class ReflectablePlatformActionListener implements PlatformActionListener {
    private int a;
    private Callback b;
    private int c;
    private Callback d;
    private int e;
    private Callback f;

    public void onCancel(Platform platform, int i) {
        if (this.f != null) {
            Message message = new Message();
            message.what = this.e;
            message.obj = new Object[]{platform, Integer.valueOf(i)};
            UIHandler.sendMessage(message, this.f);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (this.b != null) {
            Message message = new Message();
            message.what = this.a;
            message.obj = new Object[]{platform, Integer.valueOf(i), hashMap};
            UIHandler.sendMessage(message, this.b);
        }
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.d != null) {
            Message message = new Message();
            message.what = this.c;
            message.obj = new Object[]{platform, Integer.valueOf(i), th};
            UIHandler.sendMessage(message, this.d);
        }
    }

    public void setOnCancelCallback(int i, Callback callback) {
        this.e = i;
        this.f = callback;
    }

    public void setOnCompleteCallback(int i, Callback callback) {
        this.a = i;
        this.b = callback;
    }

    public void setOnErrorCallback(int i, Callback callback) {
        this.c = i;
        this.d = callback;
    }
}
