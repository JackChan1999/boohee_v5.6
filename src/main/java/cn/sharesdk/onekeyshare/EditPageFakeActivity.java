package cn.sharesdk.onekeyshare;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import com.boohee.utility.TimeLinePatterns;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.R;
import com.zxinsight.share.domain.BMPlatform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EditPageFakeActivity extends FakeActivity {
    protected View backgroundView;
    protected boolean dialogMode;
    protected List<Platform> platforms;
    private ArrayList<ImageInfo> shareImageList;
    protected HashMap<String, Object> shareParamMap;
    protected ArrayList<String> toFriendList;

    public static class ImageInfo {
        public Bitmap bitmap;
        public String paramName;
        public String srcValue;
    }

    protected interface ImageListResultsCallback {
        void onFinish(ArrayList<ImageInfo> arrayList);
    }

    public void setShareData(HashMap<String, Object> data) {
        this.shareParamMap = data;
    }

    public void setDialogMode() {
        this.dialogMode = true;
    }

    public void setBackgroundView(View bgView) {
        this.backgroundView = bgView;
    }

    public void setPlatforms(List<Platform> supportEditPagePlatforms) {
        this.platforms = supportEditPagePlatforms;
    }

    public String getLogoName(String platform) {
        if (platform == null) {
            return "";
        }
        return getContext().getString(R.getStringRes(getContext(), platform));
    }

    protected boolean isShowAtUserLayout(String platformName) {
        return BMPlatform.NAME_SINAWEIBO.equals(platformName) || BMPlatform.NAME_TENCENTWEIBO.equals(platformName) || "Facebook".equals(platformName) || "Twitter".equals(platformName) || "FacebookMessenger".equals(platformName);
    }

    protected String getAtUserButtonText(String platform) {
        return "FacebookMessenger".equals(platform) ? "To" : "@";
    }

    protected String getJoinSelectedUser(HashMap<String, Object> data) {
        if (data == null || !data.containsKey("selected")) {
            return null;
        }
        ArrayList<String> selected = (ArrayList) data.get("selected");
        if ("FacebookMessenger".equals(((Platform) data.get("platform")).getName())) {
            this.toFriendList = selected;
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = selected.iterator();
        while (it.hasNext()) {
            sb.append('@').append((String) it.next()).append(' ');
        }
        return sb.toString();
    }

    public boolean haveImage() {
        String imageUrl = (String) this.shareParamMap.get("imageUrl");
        String imagePath = (String) this.shareParamMap.get("imagePath");
        Bitmap viewToShare = (Bitmap) this.shareParamMap.get("viewToShare");
        String[] imageArray = (String[]) this.shareParamMap.get("imageArray");
        if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
            return true;
        }
        if (viewToShare != null && !viewToShare.isRecycled()) {
            return true;
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            return true;
        }
        if (imageArray == null || imageArray.length <= 0) {
            return false;
        }
        return true;
    }

    protected boolean initImageList(ImageListResultsCallback callback) {
        String imageUrl = (String) this.shareParamMap.get("imageUrl");
        String imagePath = (String) this.shareParamMap.get("imagePath");
        Bitmap viewToShare = (Bitmap) this.shareParamMap.get("viewToShare");
        String[] imageArray = (String[]) this.shareParamMap.get("imageArray");
        this.shareImageList = new ArrayList();
        ImageInfo imageInfo;
        if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
            imageInfo = new ImageInfo();
            imageInfo.paramName = "imagePath";
            imageInfo.srcValue = imagePath;
            this.shareImageList.add(imageInfo);
            this.shareParamMap.remove("imagePath");
        } else if (viewToShare != null && !viewToShare.isRecycled()) {
            imageInfo = new ImageInfo();
            imageInfo.paramName = "viewToShare";
            imageInfo.bitmap = viewToShare;
            this.shareImageList.add(imageInfo);
            this.shareParamMap.remove("viewToShare");
        } else if (!TextUtils.isEmpty(imageUrl)) {
            imageInfo = new ImageInfo();
            imageInfo.paramName = "imageUrl";
            imageInfo.srcValue = imageUrl;
            this.shareImageList.add(imageInfo);
            this.shareParamMap.remove("imageUrl");
        } else if (imageArray != null && imageArray.length > 0) {
            for (String imageUri : imageArray) {
                if (!TextUtils.isEmpty(imageUri)) {
                    imageInfo = new ImageInfo();
                    imageInfo.paramName = "imageArray";
                    imageInfo.srcValue = imageUri;
                    this.shareImageList.add(imageInfo);
                }
            }
            this.shareParamMap.remove("imageArray");
        }
        if (this.shareImageList.size() == 0) {
            return false;
        }
        new AsyncTask<Object, Void, ImageListResultsCallback>() {
            protected ImageListResultsCallback doInBackground(Object... objects) {
                Iterator it = EditPageFakeActivity.this.shareImageList.iterator();
                while (it.hasNext()) {
                    ImageInfo imageInfo = (ImageInfo) it.next();
                    if (imageInfo.bitmap == null) {
                        try {
                            String uri = imageInfo.srcValue;
                            if (uri.startsWith(TimeLinePatterns.WEB_SCHEME) || uri.startsWith("https://")) {
                                uri = BitmapHelper.downloadBitmap(EditPageFakeActivity.this.activity, uri);
                            }
                            Bitmap bitmap = BitmapHelper.getBitmap(uri);
                            if (bitmap != null) {
                                imageInfo.bitmap = bitmap;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
                return (ImageListResultsCallback) objects[0];
            }

            protected void onPostExecute(ImageListResultsCallback callback1) {
                callback1.onFinish(EditPageFakeActivity.this.shareImageList);
            }
        }.execute(new Object[]{callback});
        return true;
    }

    protected void removeImage(ImageInfo imageInfo) {
        if (this.shareImageList != null && imageInfo != null) {
            this.shareImageList.remove(imageInfo);
        }
    }

    protected void setResultAndFinish() {
        Iterator it;
        ArrayList<String> imageArray = new ArrayList();
        if (this.shareImageList != null) {
            it = this.shareImageList.iterator();
            while (it.hasNext()) {
                ImageInfo imageInfo = (ImageInfo) it.next();
                if ("imagePath".equals(imageInfo.paramName) || "imageUrl".equals(imageInfo.paramName)) {
                    this.shareParamMap.put(imageInfo.paramName, imageInfo.srcValue);
                } else if ("viewToShare".equals(imageInfo.paramName)) {
                    this.shareParamMap.put(imageInfo.paramName, imageInfo.bitmap);
                } else if ("imageArray".equals(imageInfo.paramName)) {
                    imageArray.add(imageInfo.srcValue);
                }
            }
            this.shareImageList.clear();
            if (imageArray.size() == 0) {
                this.shareParamMap.put("imageArray", null);
            } else {
                this.shareParamMap.put("imageArray", imageArray.toArray(new String[imageArray.size()]));
            }
        }
        HashMap<Platform, HashMap<String, Object>> editRes = new HashMap();
        for (Platform platform : this.platforms) {
            if ("FacebookMessenger".equals(platform.getName())) {
                HashMap<String, Object> param = new HashMap(this.shareParamMap);
                if (this.toFriendList != null && this.toFriendList.size() > 0) {
                    param.put("address", this.toFriendList.get(this.toFriendList.size() - 1));
                }
                if (param.get("address") == null) {
                    int resId = R.getStringRes(this.activity, "select_a_friend");
                    if (resId > 0) {
                        Toast.makeText(getContext(), this.activity.getString(resId) + " - " + platform.getName(), 0).show();
                        return;
                    }
                    return;
                }
                editRes.put(platform, param);
                ShareSDK.logDemoEvent(3, platform);
            } else {
                ShareSDK.logDemoEvent(3, platform);
                editRes.put(platform, this.shareParamMap);
            }
        }
        HashMap<String, Object> res = new HashMap();
        res.put("editRes", editRes);
        setResult(res);
        finish();
    }

    public boolean onFinish() {
        this.shareImageList = null;
        return super.onFinish();
    }
}
