package com.baidu.location;

import android.os.Message;
import android.os.Messenger;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

class ak {
    private static ak goto;
    private boolean a;
    private String byte;
    private String case;
    private long char;
    private String do;
    private String else;
    private long for;
    private long if;
    private String int;
    public String new;
    private a try;

    class a extends s {
        String dA;
        Messenger dB;
        boolean dC;
        final /* synthetic */ ak dz;

        public a(ak akVar) {
            this.dz = akVar;
            this.dC = false;
            this.dA = null;
            this.dB = null;
            this.cT = new ArrayList();
        }

        void T() {
            this.cR = c.try();
            if (this.dz.case == null) {
                this.dz.case = Jni.i("none");
            }
            this.cT.add(new BasicNameValuePair("erpt[0]", this.dz.case));
            if (this.dz.do == null) {
                this.dz.do = "none";
            }
            this.cT.add(new BasicNameValuePair("erpt[1]", Jni.i(this.dz.do)));
            if (this.dA == null) {
                this.dA = "none";
            }
            this.cT.add(new BasicNameValuePair("erpt[2]", Jni.i(this.dA)));
            StringBuffer stringBuffer = new StringBuffer(512);
            stringBuffer.append("&t1=");
            stringBuffer.append(this.dz.if);
            stringBuffer.append("&t2=");
            stringBuffer.append(this.dz.char);
            String aY = x.a4().aY();
            if (aY != null) {
                stringBuffer.append(aY);
            }
            this.cT.add(new BasicNameValuePair("erpt[3]", Jni.i(stringBuffer.toString())));
            this.dz.case = null;
            this.dz.do = null;
            this.dz.char = 0;
        }

        void do(boolean z) {
            if (this.cT != null) {
                this.cT.clear();
            }
            try {
                this.dB.send(z ? Message.obtain(null, 205) : Message.obtain(null, 204));
            } catch (Exception e) {
            }
            this.dC = false;
        }

        public void try(Message message) {
            this.dB = message.replyTo;
            if (this.dC) {
                try {
                    this.dB.send(Message.obtain(null, 204));
                    return;
                } catch (Exception e) {
                    return;
                }
            }
            this.dC = true;
            this.dA = message.getData().getString("errInfo");
            N();
        }
    }

    private ak() {
        this.int = null;
        this.byte = null;
        this.else = null;
        this.new = null;
        this.a = false;
        this.case = null;
        this.do = null;
        this.try = null;
        this.char = 0;
        this.for = 0;
        this.if = 0;
        this.try = new a(this);
    }

    public static ak a() {
        if (goto == null) {
            goto = new ak();
        }
        return goto;
    }

    public void a(Message message) {
        if (this.case == null || this.do == null) {
            this.case = this.int;
            this.do = this.byte;
        }
        this.try.try(message);
    }

    public void a(String str) {
        this.else = str;
        this.a = true;
        this.for = System.currentTimeMillis();
    }

    public void do() {
        this.case = this.int;
        this.do = this.byte;
        this.char = System.currentTimeMillis();
    }

    public void if() {
        this.case = null;
        this.do = null;
        this.char = 0;
    }

    public void if(String str) {
        if (this.a) {
            this.int = this.else;
            this.a = false;
            this.if = this.for;
        }
        this.byte = str;
    }
}
