package com.xiaomi.smack.provider;

import com.xiaomi.smack.packet.b;
import com.xiaomi.smack.packet.e;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class c {
    private static c a;
    private Map<String, Object> b = new ConcurrentHashMap();
    private Map<String, Object> c = new ConcurrentHashMap();

    private c() {
        b();
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (a == null) {
                a = new c();
            }
            cVar = a;
        }
        return cVar;
    }

    private String b(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(str).append("/>");
        if (str != null) {
            stringBuilder.append("<").append(str2).append("/>");
        }
        return stringBuilder.toString();
    }

    private ClassLoader[] c() {
        int i = 0;
        ClassLoader[] classLoaderArr = new ClassLoader[]{c.class.getClassLoader(), Thread
                .currentThread().getContextClassLoader()};
        List arrayList = new ArrayList();
        int length = classLoaderArr.length;
        while (i < length) {
            Object obj = classLoaderArr[i];
            if (obj != null) {
                arrayList.add(obj);
            }
            i++;
        }
        return (ClassLoader[]) arrayList.toArray(new ClassLoader[arrayList.size()]);
    }

    public Object a(String str, String str2) {
        return this.b.get(b(str, str2));
    }

    public void a(String str, String str2, Object obj) {
        if ((obj instanceof b) || (obj instanceof Class)) {
            this.b.put(b(str, str2), obj);
            return;
        }
        throw new IllegalArgumentException("Provider must be a PacketExtensionProvider or a Class" +
                " instance.");
    }

    protected void b() {
        try {
            for (ClassLoader resources : c()) {
                Enumeration resources2 = resources.getResources("META-INF/smack.providers");
                while (resources2.hasMoreElements()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = ((URL) resources2.nextElement()).openStream();
                        XmlPullParser newPullParser = XmlPullParserFactory.newInstance()
                                .newPullParser();
                        newPullParser.setFeature("http://xmlpull.org/v1/doc/features" +
                                ".html#process-namespaces", true);
                        newPullParser.setInput(inputStream, "UTF-8");
                        int eventType = newPullParser.getEventType();
                        do {
                            if (eventType == 2) {
                                String nextText;
                                String nextText2;
                                String nextText3;
                                Class cls;
                                if (newPullParser.getName().equals("iqProvider")) {
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText = newPullParser.nextText();
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText2 = newPullParser.nextText();
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText3 = newPullParser.nextText();
                                    nextText = b(nextText, nextText2);
                                    if (!this.c.containsKey(nextText)) {
                                        cls = Class.forName(nextText3);
                                        if (a.class.isAssignableFrom(cls)) {
                                            this.c.put(nextText, cls.newInstance());
                                        } else if (b.class.isAssignableFrom(cls)) {
                                            this.c.put(nextText, cls);
                                        }
                                    }
                                } else if (newPullParser.getName().equals("extensionProvider")) {
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText = newPullParser.nextText();
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText2 = newPullParser.nextText();
                                    newPullParser.next();
                                    newPullParser.next();
                                    nextText3 = newPullParser.nextText();
                                    nextText = b(nextText, nextText2);
                                    if (!this.b.containsKey(nextText)) {
                                        try {
                                            cls = Class.forName(nextText3);
                                            if (b.class.isAssignableFrom(cls)) {
                                                this.b.put(nextText, cls.newInstance());
                                            } else if (e.class.isAssignableFrom(cls)) {
                                                this.b.put(nextText, cls);
                                            }
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            eventType = newPullParser.next();
                        } while (eventType != 1);
                    } catch (ClassNotFoundException e2) {
                        e2.printStackTrace();
                    } catch (Throwable th) {
                        try {
                            inputStream.close();
                        } catch (Exception e3) {
                        }
                    }
                    try {
                        inputStream.close();
                    } catch (Exception e4) {
                    }
                }
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }
}
