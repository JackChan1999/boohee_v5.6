package boohee.lib.share;

import android.content.Context;
import android.text.TextUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

class ShareSDKHelper implements IShare {
    public static ShareSDKHelper instance = new ShareSDKHelper();
    public        OnekeyShare    mOks     = new OnekeyShare();

    public static ShareSDKHelper newIntance() {
        return instance;
    }

    private ShareSDKHelper() {
        init();
    }

    private void init() {
    }

    public void share(Context context, String title, String content, String url, String imageUrl,
                      String... picturePath) {
        this.mOks.disableSSOWhenAuthorize();
        this.mOks = new OnekeyShare();
        setLocalImagePath(picturePath);
        setDefaultImageUrlIfNeed(imageUrl, picturePath);
        this.mOks.setSilent(true);
        this.mOks.setTitle(title);
        this.mOks.setText(content);
        this.mOks.setUrl(url);
        this.mOks.setSiteUrl(url);
        this.mOks.setTitleUrl(url);
        this.mOks.show(context);
    }

    private void setLocalImagePath(String[] picturePath) {
        if (picturePath != null && picturePath.length == 1 && !TextUtils.isEmpty(picturePath[0])) {
            this.mOks.setImagePath(picturePath[0]);
        } else if (picturePath != null && picturePath.length > 1) {
            this.mOks.setImageArray(picturePath);
        }
    }

    private void setDefaultImageUrlIfNeed(String imageUrl, String... picturePath) {
        if (!TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(ShareManager.DEF_IMAGE_URL)) {
            if (picturePath == null || picturePath.length <= 0 || TextUtils.isEmpty
                    (picturePath[0])) {
                this.mOks.setImageUrl(imageUrl);
            } else if (!imageUrl.equals(ShareManager.DEF_IMAGE_URL)) {
                this.mOks.setImageUrl(imageUrl);
            }
        }
    }
}
