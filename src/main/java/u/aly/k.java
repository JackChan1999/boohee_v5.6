package u.aly;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* compiled from: UTDIdTracker */
public class k extends a {
    private static final String a = "utdid";
    private static final String b = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final Pattern c = Pattern.compile("UTDID\">([^<]+)");
    private Context d;

    public k(Context context) {
        super("utdid");
        this.d = context;
    }

    public String f() {
        return g();
    }

    private String g() {
        InputStream fileInputStream;
        File h = h();
        if (h == null || !h.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(h);
            String b = b(bu.a(fileInputStream));
            bu.c(fileInputStream);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (Throwable th) {
            bu.c(fileInputStream);
        }
    }

    private String b(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = c.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private File h() {
        if (!bt.a(this.d, b) || !Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        try {
            return new File(Environment.getExternalStorageDirectory().getCanonicalPath(), ".UTSystemConfig/Global/Alvin2.xml");
        } catch (Exception e) {
            return null;
        }
    }
}
