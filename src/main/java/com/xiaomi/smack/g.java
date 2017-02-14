package com.xiaomi.smack;

import com.umeng.socialize.common.SocializeConstants;
import com.xiaomi.smack.packet.b;
import com.xiaomi.smack.packet.b.a;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.util.c;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

class g {
    private Thread        a;
    private l             b;
    private XmlPullParser c;
    private boolean       d;

    protected g(l lVar) {
        this.b = lVar;
        a();
    }

    private void a(d dVar) {
        if (dVar != null) {
            for (a a : this.b.e.values()) {
                a.a(dVar);
            }
        }
    }

    private void e() {
        this.c = XmlPullParserFactory.newInstance().newPullParser();
        this.c.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        this.c.setInput(this.b.h);
    }

    private void f() {
        try {
            e();
            int eventType = this.c.getEventType();
            String str = "";
            do {
                this.b.o();
                if (eventType == 2) {
                    String name = this.c.getName();
                    if (this.c.getName().equals("message")) {
                        a(c.a(this.c));
                        str = name;
                    } else if (this.c.getName().equals("iq")) {
                        a(c.a(this.c, this.b));
                        str = name;
                    } else if (this.c.getName().equals("presence")) {
                        a(c.b(this.c));
                        str = name;
                    } else if (this.c.getName().equals("stream")) {
                        str = "";
                        for (int i = 0; i < this.c.getAttributeCount(); i++) {
                            if (this.c.getAttributeName(i).equals("from")) {
                                this.b.l.a(this.c.getAttributeValue(i));
                            } else if (this.c.getAttributeName(i).equals("challenge")) {
                                str = this.c.getAttributeValue(i);
                            } else if ("ps".equals(this.c.getAttributeName(i))) {
                                String attributeValue = this.c.getAttributeValue(i);
                                d bVar = new b();
                                bVar.l("0");
                                bVar.k("0");
                                bVar.a("ps", attributeValue);
                                bVar.a(a.b);
                                a(bVar);
                            }
                        }
                        this.b.a(str);
                        str = name;
                    } else if (this.c.getName().equals("error")) {
                        throw new p(c.d(this.c));
                    } else {
                        if (this.c.getName().equals("warning")) {
                            this.c.next();
                            if (this.c.getName().equals("multi-login")) {
                                a(6, null);
                                str = name;
                            }
                        } else if (this.c.getName().equals("bind")) {
                            a(c.c(this.c));
                            str = name;
                        }
                        str = name;
                    }
                } else if (eventType == 3 && this.c.getName().equals("stream")) {
                    a(13, null);
                }
                eventType = this.c.next();
                if (this.d) {
                    break;
                }
            } while (eventType != 1);
            if (eventType == 1) {
                throw new Exception("SMACK: server close the connection or timeout happened, last" +
                        " element name=" + str + " host=" + this.b.c());
            }
        } catch (Throwable e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            if (this.d) {
                com.xiaomi.channel.commonutils.logger.b.c("reader is shutdown, ignore the " +
                        "exception.");
            } else {
                a(9, e);
            }
        }
    }

    protected void a() {
        this.d = false;
        this.a = new h(this, "Smack Packet Reader (" + this.b.k + SocializeConstants
                .OP_CLOSE_PAREN);
    }

    void a(int i, Exception exception) {
        this.d = true;
        this.b.a(i, exception);
    }

    public void b() {
        this.a.start();
    }

    public void c() {
        this.d = true;
    }

    void d() {
        this.b.e.clear();
    }
}
