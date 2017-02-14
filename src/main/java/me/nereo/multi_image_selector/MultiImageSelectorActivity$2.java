package me.nereo.multi_image_selector;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

class MultiImageSelectorActivity$2 implements OnClickListener {
    final /* synthetic */ MultiImageSelectorActivity this$0;

    MultiImageSelectorActivity$2(MultiImageSelectorActivity multiImageSelectorActivity) {
        this.this$0 = multiImageSelectorActivity;
    }

    public void onClick(View view) {
        if (MultiImageSelectorActivity.access$000(this.this$0) != null && MultiImageSelectorActivity.access$000(this.this$0).size() > 0) {
            Intent data = new Intent();
            data.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, MultiImageSelectorActivity.access$000(this.this$0));
            this.this$0.setResult(-1, data);
            this.this$0.finish();
        }
    }
}
