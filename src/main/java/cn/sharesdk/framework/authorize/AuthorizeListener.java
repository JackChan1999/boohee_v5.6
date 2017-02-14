package cn.sharesdk.framework.authorize;

import android.os.Bundle;

public interface AuthorizeListener {
    void onCancel();

    void onComplete(Bundle bundle);

    void onError(Throwable th);
}
