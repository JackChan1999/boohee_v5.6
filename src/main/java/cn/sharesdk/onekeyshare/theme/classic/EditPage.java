package cn.sharesdk.onekeyshare.theme.classic;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.onekeyshare.EditPageFakeActivity;
import cn.sharesdk.onekeyshare.EditPageFakeActivity.ImageInfo;
import cn.sharesdk.onekeyshare.PicViewer;
import cn.sharesdk.onekeyshare.ShareCore;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.R;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class EditPage extends EditPageFakeActivity implements OnClickListener, TextWatcher {
    private static final int DIM_COLOR = 2133996082;
    private static final int MAX_TEXT_COUNT = 140;
    private Drawable background;
    private EditText etContent;
    private Bitmap image;
    private ImageInfo imgInfo;
    private ImageView ivImage;
    private ImageView ivPin;
    private LinearLayout llBody;
    private LinearLayout llPlat;
    private TitleLayout llTitle;
    private Platform[] platformList;
    private ProgressBar progressBar;
    private RelativeLayout rlPage;
    private RelativeLayout rlThumb;
    private TextView tvCounter;
    private View[] views;

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        Window win = activity.getWindow();
        if (activity.getResources().getConfiguration().orientation == 2) {
            win.setSoftInputMode(35);
        } else {
            win.setSoftInputMode(37);
        }
    }

    public void onCreate() {
        if (this.shareParamMap == null || this.platforms == null || this.platforms.size() < 1) {
            finish();
            return;
        }
        genBackground();
        this.activity.setContentView(getPageView());
        onTextChanged(this.etContent.getText(), 0, this.etContent.length(), 0);
        showThumb();
        new Thread() {
            public void run() {
                try {
                    EditPage.this.platformList = ShareSDK.getPlatformList();
                    if (EditPage.this.platformList != null) {
                        ArrayList<Platform> list = new ArrayList();
                        for (Platform plat : EditPage.this.platformList) {
                            String name = plat.getName();
                            if (!((plat instanceof CustomPlatform) || ShareCore.isUseClientToShare(name))) {
                                list.add(plat);
                            }
                        }
                        EditPage.this.platformList = new Platform[list.size()];
                        for (int i = 0; i < EditPage.this.platformList.length; i++) {
                            EditPage.this.platformList[i] = (Platform) list.get(i);
                        }
                        UIHandler.sendEmptyMessage(1, new Callback() {
                            public boolean handleMessage(Message msg) {
                                EditPage.this.afterPlatformListGot();
                                return false;
                            }
                        });
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }.start();
    }

    private RelativeLayout getPageView() {
        this.rlPage = new RelativeLayout(getContext());
        this.rlPage.setBackgroundDrawable(this.background);
        if (this.dialogMode) {
            RelativeLayout rlDialog = new RelativeLayout(getContext());
            rlDialog.setBackgroundColor(-1070452174);
            int dp_8 = R.dipToPx(getContext(), 8);
            LayoutParams lpDialog = new LayoutParams(R.getScreenWidth(getContext()) - (dp_8 * 2), -2);
            lpDialog.topMargin = dp_8;
            lpDialog.bottomMargin = dp_8;
            lpDialog.addRule(13);
            rlDialog.setLayoutParams(lpDialog);
            this.rlPage.addView(rlDialog);
            rlDialog.addView(getPageTitle());
            rlDialog.addView(getPageBody());
            rlDialog.addView(getImagePin());
        } else {
            this.rlPage.addView(getPageTitle());
            this.rlPage.addView(getPageBody());
            this.rlPage.addView(getImagePin());
        }
        return this.rlPage;
    }

    private TitleLayout getPageTitle() {
        this.llTitle = new TitleLayout(getContext());
        this.llTitle.setId(1);
        this.llTitle.getBtnBack().setOnClickListener(this);
        int resId = R.getStringRes(this.activity, "multi_share");
        if (resId > 0) {
            this.llTitle.getTvTitle().setText(resId);
        }
        this.llTitle.getBtnRight().setVisibility(0);
        resId = R.getStringRes(this.activity, "share");
        if (resId > 0) {
            this.llTitle.getBtnRight().setText(resId);
        }
        this.llTitle.getBtnRight().setOnClickListener(this);
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.addRule(9);
        lp.addRule(10);
        lp.addRule(11);
        this.llTitle.setLayoutParams(lp);
        return this.llTitle;
    }

    private LinearLayout getPageBody() {
        this.llBody = new LinearLayout(getContext());
        this.llBody.setId(2);
        int resId = R.getBitmapRes(this.activity, "edittext_back");
        if (resId > 0) {
            this.llBody.setBackgroundResource(resId);
        }
        this.llBody.setOrientation(1);
        LayoutParams lpBody = new LayoutParams(-2, -2);
        lpBody.addRule(5, this.llTitle.getId());
        lpBody.addRule(3, this.llTitle.getId());
        lpBody.addRule(7, this.llTitle.getId());
        if (!this.dialogMode) {
            lpBody.addRule(12);
        }
        int dp_3 = R.dipToPx(getContext(), 3);
        lpBody.setMargins(dp_3, dp_3, dp_3, dp_3);
        this.llBody.setLayoutParams(lpBody);
        this.llBody.addView(getMainBody());
        this.llBody.addView(getSep());
        this.llBody.addView(getPlatformList());
        return this.llBody;
    }

    private LinearLayout getMainBody() {
        LinearLayout llMainBody = new LinearLayout(getContext());
        llMainBody.setOrientation(1);
        LinearLayout.LayoutParams lpMain = new LinearLayout.LayoutParams(-1, -2);
        lpMain.weight = 1.0f;
        int dp_4 = R.dipToPx(getContext(), 4);
        lpMain.setMargins(dp_4, dp_4, dp_4, dp_4);
        llMainBody.setLayoutParams(lpMain);
        LinearLayout llContent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(-1, -2);
        lpContent.weight = 1.0f;
        llMainBody.addView(llContent, lpContent);
        this.etContent = new EditText(getContext());
        this.etContent.setGravity(51);
        this.etContent.setBackgroundDrawable(null);
        this.etContent.setText(String.valueOf(this.shareParamMap.get("text")));
        this.etContent.addTextChangedListener(this);
        LinearLayout.LayoutParams lpEt = new LinearLayout.LayoutParams(-2, -2);
        lpEt.weight = 1.0f;
        this.etContent.setLayoutParams(lpEt);
        llContent.addView(this.etContent);
        llContent.addView(getThumbView());
        llMainBody.addView(getBodyBottom());
        return llMainBody;
    }

    private RelativeLayout getThumbView() {
        this.rlThumb = new RelativeLayout(getContext());
        this.rlThumb.setId(1);
        this.rlThumb.setLayoutParams(new LinearLayout.LayoutParams(R.dipToPx(getContext(), 82), R.dipToPx(getContext(), 98)));
        this.ivImage = new ImageView(getContext());
        int resId = R.getBitmapRes(this.activity, "btn_back_nor");
        if (resId > 0) {
            this.ivImage.setBackgroundResource(resId);
        }
        this.ivImage.setScaleType(ScaleType.CENTER_INSIDE);
        this.ivImage.setImageBitmap(this.image);
        int dp_4 = R.dipToPx(getContext(), 4);
        this.ivImage.setPadding(dp_4, dp_4, dp_4, dp_4);
        int dp_74 = R.dipToPx(getContext(), 74);
        LayoutParams lpImage = new LayoutParams(dp_74, dp_74);
        int dp_16 = R.dipToPx(getContext(), 16);
        int dp_8 = R.dipToPx(getContext(), 8);
        lpImage.setMargins(0, dp_16, dp_8, 0);
        this.ivImage.setLayoutParams(lpImage);
        this.ivImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (EditPage.this.image != null && !EditPage.this.image.isRecycled()) {
                    PicViewer pv = new PicViewer();
                    pv.setImageBitmap(EditPage.this.image);
                    pv.show(EditPage.this.activity, null);
                }
            }
        });
        this.rlThumb.addView(this.ivImage);
        int dp_24 = R.dipToPx(getContext(), 24);
        this.progressBar = new ProgressBar(getContext());
        this.progressBar.setPadding(dp_24, dp_24, dp_24, dp_24);
        LayoutParams pb = new LayoutParams(dp_74, dp_74);
        pb.setMargins(0, dp_16, dp_8, 0);
        this.progressBar.setLayoutParams(pb);
        this.rlThumb.addView(this.progressBar);
        Button btn = new Button(getContext());
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EditPage.this.rlThumb.setVisibility(8);
                EditPage.this.ivPin.setVisibility(8);
                EditPage.this.removeImage(EditPage.this.imgInfo);
            }
        });
        resId = R.getBitmapRes(this.activity, "img_cancel");
        if (resId > 0) {
            btn.setBackgroundResource(resId);
        }
        int dp_20 = R.dipToPx(getContext(), 20);
        LayoutParams lpBtn = new LayoutParams(dp_20, dp_20);
        lpBtn.addRule(11);
        lpBtn.addRule(12);
        btn.setLayoutParams(lpBtn);
        this.rlThumb.addView(btn);
        if (!haveImage()) {
            this.rlThumb.setVisibility(8);
        }
        return this.rlThumb;
    }

    private void showThumb() {
        initImageList(new ImageListResultsCallback() {
            public void onFinish(ArrayList<ImageInfo> results) {
                if (results != null && results.size() != 0) {
                    EditPage.this.imgInfo = (ImageInfo) results.get(0);
                    EditPage.this.image = EditPage.this.imgInfo.bitmap;
                    EditPage.this.rlThumb.setVisibility(0);
                    EditPage.this.ivPin.setVisibility(0);
                    EditPage.this.progressBar.setVisibility(8);
                    EditPage.this.ivImage.setImageBitmap(EditPage.this.image);
                }
            }
        });
    }

    private LinearLayout getBodyBottom() {
        LinearLayout llBottom = new LinearLayout(getContext());
        llBottom.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        LinearLayout line = getAtLine(((Platform) this.platforms.get(0)).getName());
        if (line != null) {
            llBottom.addView(line);
        }
        this.tvCounter = new TextView(getContext());
        this.tvCounter.setText(String.valueOf(140));
        this.tvCounter.setTextColor(-3158065);
        this.tvCounter.setTextSize(1, 18.0f);
        this.tvCounter.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams lpCounter = new LinearLayout.LayoutParams(-2, -2);
        lpCounter.gravity = 16;
        this.tvCounter.setLayoutParams(lpCounter);
        llBottom.addView(this.tvCounter);
        return llBottom;
    }

    private LinearLayout getAtLine(String platform) {
        if (!isShowAtUserLayout(platform)) {
            return null;
        }
        LinearLayout llAt = new LinearLayout(getContext());
        LinearLayout.LayoutParams lpAt = new LinearLayout.LayoutParams(-2, -2);
        lpAt.rightMargin = R.dipToPx(getContext(), 4);
        lpAt.gravity = 83;
        lpAt.weight = 1.0f;
        llAt.setLayoutParams(lpAt);
        llAt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FollowListPage subPage = new FollowListPage();
                subPage.setPlatform((Platform) EditPage.this.platforms.get(0));
                subPage.showForResult(EditPage.this.activity, null, EditPage.this);
            }
        });
        TextView tvAt = new TextView(getContext());
        int resId = R.getBitmapRes(this.activity, "btn_back_nor");
        if (resId > 0) {
            tvAt.setBackgroundResource(resId);
        }
        int dp_32 = R.dipToPx(getContext(), 32);
        tvAt.setLayoutParams(new LinearLayout.LayoutParams(dp_32, dp_32));
        tvAt.setTextSize(1, 18.0f);
        tvAt.setText(getAtUserButtonText(platform));
        tvAt.setPadding(0, 0, 0, R.dipToPx(getContext(), 2));
        tvAt.setTypeface(Typeface.DEFAULT_BOLD);
        tvAt.setTextColor(-16777216);
        tvAt.setGravity(17);
        llAt.addView(tvAt);
        TextView tvName = new TextView(getContext());
        tvName.setTextSize(1, 18.0f);
        tvName.setTextColor(-16777216);
        resId = R.getStringRes(this.activity, "list_friends");
        tvName.setText(getContext().getString(resId, new Object[]{getName(platform)}));
        LinearLayout.LayoutParams lpName = new LinearLayout.LayoutParams(-2, -2);
        lpName.gravity = 16;
        tvName.setLayoutParams(lpName);
        llAt.addView(tvName);
        return llAt;
    }

    private View getSep() {
        View vSep = new View(getContext());
        vSep.setBackgroundColor(-16777216);
        vSep.setLayoutParams(new LinearLayout.LayoutParams(-1, R.dipToPx(getContext(), 1)));
        return vSep;
    }

    private LinearLayout getPlatformList() {
        LinearLayout llToolBar = new LinearLayout(getContext());
        llToolBar.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        TextView tvShareTo = new TextView(getContext());
        int resId = R.getStringRes(this.activity, "share_to");
        if (resId > 0) {
            tvShareTo.setText(resId);
        }
        tvShareTo.setTextColor(-3158065);
        tvShareTo.setTextSize(1, 18.0f);
        int dp_9 = R.dipToPx(getContext(), 9);
        LinearLayout.LayoutParams lpShareTo = new LinearLayout.LayoutParams(-2, -2);
        lpShareTo.gravity = 16;
        lpShareTo.setMargins(dp_9, 0, 0, 0);
        tvShareTo.setLayoutParams(lpShareTo);
        llToolBar.addView(tvShareTo);
        HorizontalScrollView sv = new HorizontalScrollView(getContext());
        sv.setHorizontalScrollBarEnabled(false);
        sv.setHorizontalFadingEdgeEnabled(false);
        LinearLayout.LayoutParams lpSv = new LinearLayout.LayoutParams(-2, -2);
        lpSv.setMargins(dp_9, dp_9, dp_9, dp_9);
        sv.setLayoutParams(lpSv);
        llToolBar.addView(sv);
        this.llPlat = new LinearLayout(getContext());
        this.llPlat.setLayoutParams(new FrameLayout.LayoutParams(-2, -1));
        sv.addView(this.llPlat);
        return llToolBar;
    }

    private ImageView getImagePin() {
        this.ivPin = new ImageView(getContext());
        int resId = R.getBitmapRes(this.activity, "pin");
        if (resId > 0) {
            this.ivPin.setImageResource(resId);
        }
        LayoutParams lp = new LayoutParams(R.dipToPx(getContext(), 80), R.dipToPx(getContext(), 36));
        lp.topMargin = R.dipToPx(getContext(), 6);
        lp.addRule(6, this.llBody.getId());
        lp.addRule(11);
        this.ivPin.setLayoutParams(lp);
        this.ivPin.setVisibility(8);
        return this.ivPin;
    }

    private void genBackground() {
        this.background = new ColorDrawable(DIM_COLOR);
        if (this.backgroundView != null) {
            try {
                Bitmap bgBm = BitmapHelper.blur(BitmapHelper.captureView(this.backgroundView, this.backgroundView.getWidth(), this.backgroundView.getHeight()), 20, 8);
                this.background = new LayerDrawable(new Drawable[]{new BitmapDrawable(this.activity.getResources(), bgBm), this.background});
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private String getName(String platform) {
        if (platform == null) {
            return "";
        }
        return getContext().getString(R.getStringRes(getContext(), platform.toLowerCase()));
    }

    public void onClick(View v) {
        int i;
        if (v.equals(this.llTitle.getBtnBack())) {
            Platform plat = null;
            for (i = 0; i < this.views.length; i++) {
                if (this.views[i].getVisibility() == 4) {
                    plat = this.platformList[i];
                    break;
                }
            }
            if (plat != null) {
                ShareSDK.logDemoEvent(5, plat);
            }
            finish();
        } else if (v.equals(this.llTitle.getBtnRight())) {
            this.shareParamMap.put("text", this.etContent.getText().toString());
            this.platforms.clear();
            for (i = 0; i < this.views.length; i++) {
                if (this.views[i].getVisibility() != 0) {
                    this.platforms.add(this.platformList[i]);
                }
            }
            if (this.platforms.size() > 0) {
                setResultAndFinish();
                return;
            }
            int resId = R.getStringRes(this.activity, "select_one_plat_at_least");
            if (resId > 0) {
                Toast.makeText(getContext(), resId, 0).show();
            }
        } else if (v instanceof FrameLayout) {
            ((FrameLayout) v).getChildAt(1).performClick();
        } else if (v.getVisibility() == 4) {
            v.setVisibility(0);
        } else {
            v.setVisibility(4);
        }
    }

    public void afterPlatformListGot() {
        int size;
        if (this.platformList == null) {
            size = 0;
        } else {
            size = this.platformList.length;
        }
        this.views = new View[size];
        final int dp_24 = R.dipToPx(getContext(), 24);
        LinearLayout.LayoutParams lpItem = new LinearLayout.LayoutParams(dp_24, dp_24);
        final int dp_9 = R.dipToPx(getContext(), 9);
        lpItem.setMargins(0, 0, dp_9, 0);
        FrameLayout.LayoutParams lpMask = new FrameLayout.LayoutParams(-1, -1);
        lpMask.gravity = 51;
        int selection = 0;
        for (int i = 0; i < size; i++) {
            FrameLayout fl = new FrameLayout(getContext());
            fl.setLayoutParams(lpItem);
            if (i >= size - 1) {
                fl.setLayoutParams(new LinearLayout.LayoutParams(dp_24, dp_24));
            }
            this.llPlat.addView(fl);
            fl.setOnClickListener(this);
            ImageView iv = new ImageView(getContext());
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            iv.setImageBitmap(getPlatLogo(this.platformList[i]));
            iv.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            fl.addView(iv);
            this.views[i] = new View(getContext());
            this.views[i].setBackgroundColor(-805306369);
            this.views[i].setOnClickListener(this);
            String platformName = this.platformList[i].getName();
            for (Platform plat : this.platforms) {
                if (platformName.equals(plat.getName())) {
                    this.views[i].setVisibility(4);
                    selection = i;
                }
            }
            this.views[i].setLayoutParams(lpMask);
            fl.addView(this.views[i]);
        }
        final int postSel = selection;
        UIHandler.sendEmptyMessageDelayed(0, 333, new Callback() {
            public boolean handleMessage(Message msg) {
                ((HorizontalScrollView) EditPage.this.llPlat.getParent()).scrollTo(postSel * (dp_24 + dp_9), 0);
                return false;
            }
        });
    }

    private Bitmap getPlatLogo(Platform plat) {
        if (plat == null || plat.getName() == null) {
            return null;
        }
        int resId = R.getBitmapRes(this.activity, ("logo_" + plat.getName()).toLowerCase());
        if (resId > 0) {
            return BitmapFactory.decodeResource(this.activity.getResources(), resId);
        }
        return null;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int remain = 140 - this.etContent.length();
        this.tvCounter.setText(String.valueOf(remain));
        this.tvCounter.setTextColor(remain > 0 ? -3158065 : SupportMenu.CATEGORY_MASK);
    }

    public void afterTextChanged(Editable s) {
    }

    public void onResult(HashMap<String, Object> data) {
        String atText = getJoinSelectedUser(data);
        if (atText != null) {
            this.etContent.append(atText);
        }
    }

    private void hideSoftInput() {
        try {
            ((InputMethodManager) this.activity.getSystemService("input_method")).hideSoftInputFromWindow(this.etContent.getWindowToken(), 0);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean onFinish() {
        hideSoftInput();
        return super.onFinish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.activity.getResources().getConfiguration().orientation == 2) {
            hideSoftInput();
            this.activity.getWindow().setSoftInputMode(35);
            this.rlPage.setBackgroundColor(DIM_COLOR);
            this.rlPage.postDelayed(new Runnable() {
                public void run() {
                    EditPage.this.genBackground();
                    EditPage.this.rlPage.setBackgroundDrawable(EditPage.this.background);
                }
            }, 1000);
            return;
        }
        hideSoftInput();
        this.activity.getWindow().setSoftInputMode(37);
        this.rlPage.setBackgroundColor(DIM_COLOR);
        this.rlPage.postDelayed(new Runnable() {
            public void run() {
                EditPage.this.genBackground();
                EditPage.this.rlPage.setBackgroundDrawable(EditPage.this.background);
            }
        }, 1000);
    }
}
