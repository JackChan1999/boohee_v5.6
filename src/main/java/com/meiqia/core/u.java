package com.meiqia.core;

import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnEndConversationCallback;

class u implements OnClientInfoCallback {
    final /* synthetic */ OnEndConversationCallback a;
    final /* synthetic */ b                         b;

    u(b bVar, OnEndConversationCallback onEndConversationCallback) {
        this.b = bVar;
        this.a = onEndConversationCallback;
    }

    public void onFailure(int i, String str) {
        if (this.a != null) {
            this.a.onFailure(i, str);
        }
    }

    public void onSuccess() {
        this.b.a(null);
        this.b.a(new v(this));
        MQManager.getInstance(this.b.e).closeMeiqiaService();
    }
}
