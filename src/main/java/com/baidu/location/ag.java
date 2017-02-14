package com.baidu.location;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.qiniu.android.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

abstract class ag implements ax, n {
    public static String ee = null;
    private boolean ed = false;
    private boolean ef = true;
    final Handler eg = new b(this);
    public com.baidu.location.t.a eh = null;
    private boolean ei = false;
    private boolean ej = false;
    public b ek = null;

    class a extends s {
        final /* synthetic */ ag dw;
        String dx;
        String dy;

        public a(ag agVar) {
            this.dw = agVar;
            this.dy = null;
            this.dx = null;
            this.cT = new ArrayList();
        }

        void T() {
            this.cR = c.for();
            String i = Jni.i(this.dx);
            if (ab.gE) {
                Log.i(ax.i, i);
            }
            ak.a().a(i);
            this.dx = null;
            if (this.dy == null) {
                this.dy = q.B();
            }
            this.cT.add(new BasicNameValuePair("bloc", i));
            if (this.dy != null) {
                this.cT.add(new BasicNameValuePair("up", this.dy));
            }
            StringBuffer stringBuffer = new StringBuffer(512);
            stringBuffer.append(String.format(Locale.CHINA, "&ki=%s&sn=%s", new Object[]{v.a(f.getServiceContext()), v.if(f.getServiceContext())}));
            String cY = j.cZ().cY();
            if (cY != null) {
                stringBuffer.append(cY);
            }
            if (stringBuffer.length() > 0) {
                this.cT.add(new BasicNameValuePair(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, Jni.i(stringBuffer.toString())));
            }
            this.cT.add(new BasicNameValuePair("trtm", String.format(Locale.CHINA, "%d", new Object[]{Long.valueOf(System.currentTimeMillis())})));
            g.e().h();
        }

        public void d(String str) {
            this.dx = str;
            N();
        }

        void do(boolean z) {
            Message obtainMessage;
            if (!z || this.cS == null) {
                ak.a().if("network exception");
                obtainMessage = this.dw.eg.obtainMessage(63);
                obtainMessage.obj = "HttpStatus error";
                obtainMessage.sendToTarget();
            } else {
                try {
                    Object bDLocation;
                    String entityUtils = EntityUtils.toString(this.cS, Constants.UTF_8);
                    ag.ee = entityUtils;
                    ak.a().if(entityUtils);
                    try {
                        bDLocation = new BDLocation(entityUtils);
                        if (bDLocation.getLocType() == 161) {
                            g.e().try(bDLocation.getTime());
                            bDLocation.byte(t.an().aq());
                            if (af.bw().by()) {
                                bDLocation.setDirection(af.bw().bu());
                            }
                        }
                    } catch (Exception e) {
                        bDLocation = new BDLocation();
                        bDLocation.setLocType(63);
                    }
                    Message obtainMessage2 = this.dw.eg.obtainMessage(21);
                    obtainMessage2.obj = bDLocation;
                    obtainMessage2.sendToTarget();
                    this.dy = null;
                } catch (Exception e2) {
                    obtainMessage = this.dw.eg.obtainMessage(63);
                    obtainMessage.obj = "HttpStatus error";
                    obtainMessage.sendToTarget();
                }
            }
            if (this.cT != null) {
                this.cT.clear();
            }
        }
    }

    public class b extends Handler {
        final /* synthetic */ ag a;

        public b(ag agVar) {
            this.a = agVar;
        }

        public void handleMessage(Message message) {
            if (ab.gv) {
                switch (message.what) {
                    case 21:
                        this.a.byte(message);
                        return;
                    case 62:
                    case 63:
                        this.a.at();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    ag() {
    }

    abstract void at();

    abstract void byte(Message message);

    public String e(String str) {
        String str2;
        if (this.eh == null || !this.eh.do()) {
            this.eh = t.an().ak();
        }
        if (this.eh != null) {
            c.if(ax.i, this.eh.if());
        } else {
            c.if(ax.i, "cellInfo null...");
        }
        if (this.ek == null || !this.ek.for()) {
            this.ek = ar.bW().b1();
        }
        if (this.ek != null) {
            c.if(ax.i, this.ek.else());
        } else {
            c.if(ax.i, "wifi list null");
        }
        Location location = null;
        if (x.a4().aR()) {
            location = x.a4().aS();
        }
        String o = k.p().o();
        if (ar.bU()) {
            str2 = "&cn=32";
        } else {
            str2 = String.format(Locale.CHINA, "&cn=%d", new Object[]{Integer.valueOf(t.an().ap())});
        }
        if (this.ef) {
            this.ef = false;
        } else if (!this.ed) {
            String y = q.y();
            if (y != null) {
                str2 = str2 + y;
            }
            Object b0 = ar.bW().b0();
            if (!TextUtils.isEmpty(b0)) {
                y = b0.replace(":", "");
                str2 = String.format(Locale.CHINA, "%s&mac=%s", new Object[]{str2, y});
                this.ed = true;
            }
        }
        str2 = str2 + o;
        if (str != null) {
            str2 = str + str2;
        }
        return c.if(this.eh, this.ek, location, str2, 0);
    }
}
