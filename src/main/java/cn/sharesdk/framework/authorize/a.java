package cn.sharesdk.framework.authorize;

import android.content.Context;
import android.content.Intent;
import com.mob.tools.FakeActivity;

public class a extends FakeActivity {
    protected AuthorizeHelper a;

    public AuthorizeHelper a() {
        return this.a;
    }

    public void a(AuthorizeHelper authorizeHelper) {
        this.a = authorizeHelper;
        super.show(authorizeHelper.getPlatform().getContext(), null);
    }

    public void show(Context context, Intent intent) {
        throw new RuntimeException("This method is deprecated, use show(AuthorizeHelper, Intent) instead");
    }
}
