package me.nereo.multi_image_selector;

import android.view.View;
import android.view.View.OnClickListener;

class MultiImageSelectorActivity$1 implements OnClickListener {
    final /* synthetic */ MultiImageSelectorActivity this$0;

    MultiImageSelectorActivity$1(MultiImageSelectorActivity multiImageSelectorActivity) {
        this.this$0 = multiImageSelectorActivity;
    }

    public void onClick(View view) {
        this.this$0.setResult(0);
        this.this$0.finish();
    }
}
