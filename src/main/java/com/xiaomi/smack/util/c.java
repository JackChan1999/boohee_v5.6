package com.xiaomi.smack.util;

import android.text.TextUtils;

import com.boohee.one.BuildConfig;
import com.tencent.connect.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.push.service.ac;
import com.xiaomi.push.service.w;
import com.xiaomi.smack.k;
import com.xiaomi.smack.p;
import com.xiaomi.smack.packet.a;
import com.xiaomi.smack.packet.b;
import com.xiaomi.smack.packet.d;
import com.xiaomi.smack.packet.f;
import com.xiaomi.smack.packet.g;
import com.xiaomi.smack.packet.h;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class c {
    private static XmlPullParser a = null;

    public static a a(String str, String str2, XmlPullParser xmlPullParser) {
        Object a = com.xiaomi.smack.provider.c.a().a(BuildConfig.PLATFORM, "xm:chat");
        return (a == null || !(a instanceof com.xiaomi.push.service.c)) ? null : ((com.xiaomi
                .push.service.c) a).b(xmlPullParser);
    }

    public static b a(XmlPullParser xmlPullParser, com.xiaomi.smack.a aVar) {
        int i;
        String attributeValue = xmlPullParser.getAttributeValue("", "id");
        String attributeValue2 = xmlPullParser.getAttributeValue("", SocializeProtocolConstants
                .PROTOCOL_KEY_SHARE_TO);
        String attributeValue3 = xmlPullParser.getAttributeValue("", "from");
        String attributeValue4 = xmlPullParser.getAttributeValue("", "chid");
        b.a a = b.a.a(xmlPullParser.getAttributeValue("", "type"));
        Map hashMap = new HashMap();
        for (i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            hashMap.put(attributeName, xmlPullParser.getAttributeValue("", attributeName));
        }
        Object obj = null;
        h hVar = null;
        b bVar = null;
        while (obj == null) {
            Object obj2;
            b bVar2;
            h hVar2;
            Object obj3;
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("error")) {
                    hVar = e(xmlPullParser);
                } else {
                    bVar = new b();
                    bVar.a(a(name, namespace, xmlPullParser));
                }
                obj2 = obj;
                bVar2 = bVar;
                hVar2 = hVar;
                obj3 = obj2;
            } else if (next == 3 && xmlPullParser.getName().equals("iq")) {
                bVar2 = bVar;
                hVar2 = hVar;
                i = 1;
            } else {
                obj2 = obj;
                bVar2 = bVar;
                hVar2 = hVar;
                obj3 = obj2;
            }
            obj2 = obj3;
            hVar = hVar2;
            bVar = bVar2;
            obj = obj2;
        }
        if (bVar == null) {
            if (b.a.a == a || b.a.b == a) {
                d dVar = new d();
                dVar.k(attributeValue);
                dVar.m(attributeValue3);
                dVar.n(attributeValue2);
                dVar.a(b.a.d);
                dVar.l(attributeValue4);
                dVar.a(new h(h.a.e));
                aVar.a(dVar);
                com.xiaomi.channel.commonutils.logger.b.d("iq usage error. send packet in packet " +
                        "parser.");
                return null;
            }
            bVar = new e();
        }
        bVar.k(attributeValue);
        bVar.m(attributeValue2);
        bVar.l(attributeValue4);
        bVar.n(attributeValue3);
        bVar.a(a);
        bVar.a(hVar);
        bVar.a(hashMap);
        return bVar;
    }

    public static d a(XmlPullParser xmlPullParser) {
        String attributeValue;
        boolean z;
        if ("1".equals(xmlPullParser.getAttributeValue("", "s"))) {
            attributeValue = xmlPullParser.getAttributeValue("", "chid");
            String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
            String attributeValue3 = xmlPullParser.getAttributeValue("", "from");
            String attributeValue4 = xmlPullParser.getAttributeValue("",
                    SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO);
            String attributeValue5 = xmlPullParser.getAttributeValue("", "type");
            w.b b = w.a().b(attributeValue, attributeValue4);
            w.b b2 = b == null ? w.a().b(attributeValue, attributeValue3) : b;
            if (b2 == null) {
                throw new p("the channel id is wrong while receiving a encrypted message");
            }
            z = false;
            d dVar = null;
            while (!z) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (!"s".equals(xmlPullParser.getName())) {
                        throw new p("error while receiving a encrypted message with wrong format");
                    } else if (xmlPullParser.next() != 4) {
                        throw new p("error while receiving a encrypted message with wrong format");
                    } else {
                        String text = xmlPullParser.getText();
                        if ("5".equals(attributeValue) || Constants.VIA_SHARE_TYPE_INFO.equals
                                (attributeValue)) {
                            dVar = new com.xiaomi.smack.packet.c();
                            dVar.l(attributeValue);
                            dVar.b(true);
                            dVar.n(attributeValue3);
                            dVar.m(attributeValue4);
                            dVar.k(attributeValue2);
                            dVar.f(attributeValue5);
                            a aVar = new a("s", null, (String[]) null, (String[]) null);
                            aVar.b(text);
                            dVar.a(aVar);
                            return dVar;
                        }
                        a(ac.b(ac.a(b2.i, attributeValue2), text));
                        a.next();
                        dVar = a(a);
                    }
                } else if (next == 3 && xmlPullParser.getName().equals("message")) {
                    z = true;
                }
            }
            if (dVar != null) {
                return dVar;
            }
            throw new p("error while receiving a encrypted message with wrong format");
        }
        Object attributeValue6;
        Object attributeValue7;
        d cVar = new com.xiaomi.smack.packet.c();
        String attributeValue8 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue8 == null) {
            attributeValue8 = "ID_NOT_AVAILABLE";
        }
        cVar.k(attributeValue8);
        cVar.m(xmlPullParser.getAttributeValue("", SocializeProtocolConstants
                .PROTOCOL_KEY_SHARE_TO));
        cVar.n(xmlPullParser.getAttributeValue("", "from"));
        cVar.l(xmlPullParser.getAttributeValue("", "chid"));
        cVar.a(xmlPullParser.getAttributeValue("", "appid"));
        try {
            attributeValue6 = xmlPullParser.getAttributeValue("", "transient");
        } catch (Exception e) {
            attributeValue6 = null;
        }
        try {
            attributeValue = xmlPullParser.getAttributeValue("", "seq");
            if (!TextUtils.isEmpty(attributeValue)) {
                cVar.b(attributeValue);
            }
        } catch (Exception e2) {
        }
        try {
            attributeValue7 = xmlPullParser.getAttributeValue("", "mseq");
            if (!TextUtils.isEmpty(attributeValue7)) {
                cVar.c(attributeValue7);
            }
        } catch (Exception e3) {
        }
        try {
            attributeValue7 = xmlPullParser.getAttributeValue("", "fseq");
            if (!TextUtils.isEmpty(attributeValue7)) {
                cVar.d(attributeValue7);
            }
        } catch (Exception e4) {
        }
        try {
            attributeValue7 = xmlPullParser.getAttributeValue("", "status");
            if (!TextUtils.isEmpty(attributeValue7)) {
                cVar.e(attributeValue7);
            }
        } catch (Exception e5) {
        }
        z = !TextUtils.isEmpty(attributeValue6) && attributeValue6.equalsIgnoreCase("true");
        cVar.a(z);
        cVar.f(xmlPullParser.getAttributeValue("", "type"));
        attributeValue8 = g(xmlPullParser);
        if (attributeValue8 == null || "".equals(attributeValue8.trim())) {
            d.u();
        } else {
            cVar.j(attributeValue8);
        }
        attributeValue8 = null;
        boolean z2 = false;
        while (!z2) {
            int next2 = xmlPullParser.next();
            if (next2 == 2) {
                attributeValue = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (TextUtils.isEmpty(namespace)) {
                    namespace = "xm";
                }
                if (attributeValue.equals("subject")) {
                    if (g(xmlPullParser) == null) {
                        cVar.g(f(xmlPullParser));
                    } else {
                        cVar.g(f(xmlPullParser));
                    }
                } else if (attributeValue.equals("body")) {
                    Object attributeValue9 = xmlPullParser.getAttributeValue("", "encode");
                    attributeValue = f(xmlPullParser);
                    if (TextUtils.isEmpty(attributeValue9)) {
                        cVar.h(attributeValue);
                    } else {
                        cVar.a(attributeValue, attributeValue9);
                    }
                } else if (attributeValue.equals("thread")) {
                    if (attributeValue8 == null) {
                        attributeValue8 = xmlPullParser.nextText();
                    }
                } else if (attributeValue.equals("error")) {
                    cVar.a(e(xmlPullParser));
                } else {
                    cVar.a(a(attributeValue, namespace, xmlPullParser));
                }
            } else if (next2 == 3 && xmlPullParser.getName().equals("message")) {
                z2 = true;
            }
        }
        cVar.i(attributeValue8);
        return cVar;
    }

    private static void a(byte[] bArr) {
        if (a == null) {
            try {
                a = XmlPullParserFactory.newInstance().newPullParser();
                a.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        a.setInput(new InputStreamReader(new ByteArrayInputStream(bArr)));
    }

    public static f b(XmlPullParser xmlPullParser) {
        f.b bVar = f.b.available;
        String attributeValue = xmlPullParser.getAttributeValue("", "type");
        if (!(attributeValue == null || attributeValue.equals(""))) {
            try {
                bVar = f.b.valueOf(attributeValue);
            } catch (IllegalArgumentException e) {
                System.err.println("Found invalid presence type " + attributeValue);
            }
        }
        f fVar = new f(bVar);
        fVar.m(xmlPullParser.getAttributeValue("", SocializeProtocolConstants
                .PROTOCOL_KEY_SHARE_TO));
        fVar.n(xmlPullParser.getAttributeValue("", "from"));
        fVar.l(xmlPullParser.getAttributeValue("", "chid"));
        String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue2 == null) {
            attributeValue2 = "ID_NOT_AVAILABLE";
        }
        fVar.k(attributeValue2);
        int i = 0;
        while (i == 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("status")) {
                    fVar.a(xmlPullParser.nextText());
                } else if (name.equals("priority")) {
                    try {
                        fVar.a(Integer.parseInt(xmlPullParser.nextText()));
                    } catch (NumberFormatException e2) {
                    } catch (IllegalArgumentException e3) {
                        fVar.a(0);
                    }
                } else if (name.equals("show")) {
                    name = xmlPullParser.nextText();
                    try {
                        fVar.a(f.a.valueOf(name));
                    } catch (IllegalArgumentException e4) {
                        System.err.println("Found invalid presence mode " + name);
                    }
                } else if (name.equals("error")) {
                    fVar.a(e(xmlPullParser));
                } else {
                    fVar.a(a(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("presence")) {
                i = 1;
            }
        }
        return fVar;
    }

    public static k.b c(XmlPullParser xmlPullParser) {
        k.b bVar = new k.b();
        String attributeValue = xmlPullParser.getAttributeValue("", "id");
        String attributeValue2 = xmlPullParser.getAttributeValue("", SocializeProtocolConstants
                .PROTOCOL_KEY_SHARE_TO);
        String attributeValue3 = xmlPullParser.getAttributeValue("", "from");
        String attributeValue4 = xmlPullParser.getAttributeValue("", "chid");
        k.b.a a = k.b.a.a(xmlPullParser.getAttributeValue("", "type"));
        bVar.k(attributeValue);
        bVar.m(attributeValue2);
        bVar.n(attributeValue3);
        bVar.l(attributeValue4);
        bVar.a(a);
        Object obj = null;
        h hVar = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("error")) {
                    hVar = e(xmlPullParser);
                }
            } else if (next == 3 && xmlPullParser.getName().equals("bind")) {
                obj = 1;
            }
        }
        bVar.a(hVar);
        return bVar;
    }

    public static g d(XmlPullParser xmlPullParser) {
        g gVar = null;
        Object obj = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                gVar = new g(xmlPullParser.getName());
            } else if (next == 3 && xmlPullParser.getName().equals("error")) {
                obj = 1;
            }
        }
        return gVar;
    }

    public static h e(XmlPullParser xmlPullParser) {
        String attributeValue;
        String attributeValue2;
        String str = "urn:ietf:params:xml:ns:xmpp-stanzas";
        List arrayList = new ArrayList();
        String str2 = null;
        String str3 = null;
        String str4 = "-1";
        int i = 0;
        while (i < xmlPullParser.getAttributeCount()) {
            attributeValue = xmlPullParser.getAttributeName(i).equals("code") ? xmlPullParser
                    .getAttributeValue("", "code") : str4;
            attributeValue2 = xmlPullParser.getAttributeName(i).equals("type") ? xmlPullParser
                    .getAttributeValue("", "type") : str3;
            if (xmlPullParser.getAttributeName(i).equals("reason")) {
                str2 = xmlPullParser.getAttributeValue("", "reason");
            }
            i++;
            str3 = attributeValue2;
            str4 = attributeValue;
        }
        Object obj = null;
        attributeValue2 = null;
        attributeValue = null;
        while (obj == null) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("text")) {
                    attributeValue = xmlPullParser.nextText();
                } else {
                    String name = xmlPullParser.getName();
                    String namespace = xmlPullParser.getNamespace();
                    if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(namespace)) {
                        attributeValue2 = name;
                    } else {
                        arrayList.add(a(name, namespace, xmlPullParser));
                    }
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("error")) {
                    obj = 1;
                }
            } else if (next == 4) {
                attributeValue = xmlPullParser.getText();
            }
        }
        return new h(Integer.parseInt(str4), str3 == null ? "cancel" : str3, str2,
                attributeValue2, attributeValue, arrayList);
    }

    private static String f(XmlPullParser xmlPullParser) {
        String str = "";
        int depth = xmlPullParser.getDepth();
        while (true) {
            if (xmlPullParser.next() == 3 && xmlPullParser.getDepth() == depth) {
                return str;
            }
            str = str + xmlPullParser.getText();
        }
    }

    private static String g(XmlPullParser xmlPullParser) {
        int i = 0;
        while (i < xmlPullParser.getAttributeCount()) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals
                    (xmlPullParser.getAttributePrefix(i)))) {
                return xmlPullParser.getAttributeValue(i);
            }
            i++;
        }
        return null;
    }
}
