package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import com.mob.tools.FakeActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlatformListFakeActivity extends FakeActivity {
    protected View backgroundView;
    private boolean canceled = false;
    protected ArrayList<CustomerLogo> customerLogos;
    protected boolean dialogMode = false;
    protected HashMap<String, String> hiddenPlatforms;
    protected OnShareButtonClickListener onShareButtonClickListener;
    protected HashMap<String, Object> shareParamsMap;
    protected boolean silent;
    protected ThemeShareCallback themeShareCallback;

    public interface OnShareButtonClickListener {
        void onClick(View view, List<Object> list);
    }

    public void onCreate() {
        super.onCreate();
        this.canceled = false;
        if (this.themeShareCallback == null) {
            finish();
        }
    }

    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.canceled = true;
        }
        return super.onKeyEvent(keyCode, event);
    }

    protected void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean onFinish() {
        if (this.canceled) {
            ShareSDK.logDemoEvent(2, null);
        }
        return super.onFinish();
    }

    public void show(Context context, Intent i) {
        super.show(context, i);
    }

    public HashMap<String, Object> getShareParamsMap() {
        return this.shareParamsMap;
    }

    public void setShareParamsMap(HashMap<String, Object> shareParamsMap) {
        this.shareParamsMap = shareParamsMap;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public ArrayList<CustomerLogo> getCustomerLogos() {
        return this.customerLogos;
    }

    public void setCustomerLogos(ArrayList<CustomerLogo> customerLogos) {
        this.customerLogos = customerLogos;
    }

    public HashMap<String, String> getHiddenPlatforms() {
        return this.hiddenPlatforms;
    }

    public void setHiddenPlatforms(HashMap<String, String> hiddenPlatforms) {
        this.hiddenPlatforms = hiddenPlatforms;
    }

    public View getBackgroundView() {
        return this.backgroundView;
    }

    public void setBackgroundView(View backgroundView) {
        this.backgroundView = backgroundView;
    }

    public OnShareButtonClickListener getOnShareButtonClickListener() {
        return this.onShareButtonClickListener;
    }

    public void setOnShareButtonClickListener(OnShareButtonClickListener onShareButtonClickListener) {
        this.onShareButtonClickListener = onShareButtonClickListener;
    }

    public boolean isDialogMode() {
        return this.dialogMode;
    }

    public void setDialogMode(boolean dialogMode) {
        this.dialogMode = dialogMode;
    }

    public ThemeShareCallback getThemeShareCallback() {
        return this.themeShareCallback;
    }

    public void setThemeShareCallback(ThemeShareCallback themeShareCallback) {
        this.themeShareCallback = themeShareCallback;
    }

    protected void onShareButtonClick(View v, List<Object> checkedPlatforms) {
        if (this.onShareButtonClickListener != null) {
            this.onShareButtonClickListener.onClick(v, checkedPlatforms);
        }
        HashMap<Platform, HashMap<String, Object>> silentShareData = new HashMap();
        List<Platform> supportEditPagePlatforms = new ArrayList();
        for (CustomerLogo item : checkedPlatforms) {
            if (item instanceof CustomerLogo) {
                item.listener.onClick(v);
            } else {
                Platform plat = (Platform) item;
                String name = plat.getName();
                if (this.silent || ShareCore.isDirectShare(plat)) {
                    HashMap<String, Object> shareParam = new HashMap(this.shareParamsMap);
                    shareParam.put("platform", name);
                    silentShareData.put(plat, shareParam);
                } else {
                    supportEditPagePlatforms.add(plat);
                }
            }
        }
        if (silentShareData.size() > 0) {
            this.themeShareCallback.doShare(silentShareData);
        }
        if (supportEditPagePlatforms.size() > 0) {
            showEditPage(supportEditPagePlatforms);
        }
        finish();
    }

    protected void showEditPage(List<Platform> platforms) {
        showEditPage(getContext(), (List) platforms);
    }

    public void showEditPage(Context context, Platform platform) {
        List platforms = new ArrayList(1);
        platforms.add(platform);
        showEditPage(context, platforms);
    }

    protected void showEditPage(Context context, List<Platform> platforms) {
        try {
            EditPageFakeActivity editPageFakeActivity = (EditPageFakeActivity) Class.forName(getClass().getPackage().getName() + ".EditPage").newInstance();
            editPageFakeActivity.setBackgroundView(this.backgroundView);
            editPageFakeActivity.setShareData(this.shareParamsMap);
            editPageFakeActivity.setPlatforms(platforms);
            if (this.dialogMode) {
                editPageFakeActivity.setDialogMode();
            }
            editPageFakeActivity.showForResult(context, null, new FakeActivity() {
                public void onResult(HashMap<String, Object> data) {
                    if (data != null && data.containsKey("editRes")) {
                        PlatformListFakeActivity.this.themeShareCallback.doShare((HashMap) data.get("editRes"));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
