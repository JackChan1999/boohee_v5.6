package cn.sharesdk.framework.authorize;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;

class c implements OnClickListener {
    final /* synthetic */ RegisterView a;

    c(RegisterView registerView) {
        this.a = registerView;
    }

    public void onClick(View view) {
        try {
            Object string = view.getResources().getString(R.getStringRes(view.getContext(), "website"));
            if (!TextUtils.isEmpty(string)) {
                view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(string)));
            }
        } catch (Throwable th) {
            Ln.e(th);
        }
    }
}
