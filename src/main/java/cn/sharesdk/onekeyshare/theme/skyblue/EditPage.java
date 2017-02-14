package cn.sharesdk.onekeyshare.theme.skyblue;

import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.EditPageFakeActivity;
import cn.sharesdk.onekeyshare.EditPageFakeActivity.ImageInfo;
import cn.sharesdk.onekeyshare.PicViewer;
import com.mob.tools.utils.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EditPage extends EditPageFakeActivity implements OnClickListener, TextWatcher {
    private static final int MAX_TEXT_COUNT = 140;
    private TextView textCounterTextView;
    private EditText textEditText;
    private EditText titleEditText;

    public void onCreate() {
        if (this.shareParamMap == null || this.platforms == null) {
            finish();
            return;
        }
        this.activity.setContentView(R.getLayoutRes(this.activity, "skyblue_editpage"));
        initView();
    }

    private void initView() {
        if (!this.dialogMode) {
            RelativeLayout mainRelLayout = (RelativeLayout) findViewByResName("mainRelLayout");
            LayoutParams lp = (LayoutParams) mainRelLayout.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            lp.height = -1;
            mainRelLayout.setLayoutParams(lp);
        }
        initTitleView();
        initBodyView();
        initImageListView();
    }

    private void initTitleView() {
        View backImageView = findViewByResName("backImageView");
        backImageView.setTag("close");
        backImageView.setOnClickListener(this);
        View okImageView = findViewByResName("okImageView");
        okImageView.setTag("ok");
        okImageView.setOnClickListener(this);
    }

    private void initBodyView() {
        View closeImageView = findViewByResName("closeImageView");
        closeImageView.setTag("close");
        closeImageView.setOnClickListener(this);
        if (this.shareParamMap.containsKey("title")) {
            this.titleEditText = (EditText) findViewByResName("titleEditText");
            this.titleEditText.setText(String.valueOf(this.shareParamMap.get("title")));
        }
        this.textCounterTextView = (TextView) findViewByResName("textCounterTextView");
        this.textCounterTextView.setText(String.valueOf(140));
        this.textEditText = (EditText) findViewByResName("textEditText");
        this.textEditText.addTextChangedListener(this);
        this.textEditText.setText(String.valueOf(this.shareParamMap.get("text")));
        initAtUserView();
    }

    private void initAtUserView() {
        LinearLayout atLayout = (LinearLayout) findViewByResName("atLayout");
        for (Platform platform : this.platforms) {
            String platformName = platform.getName();
            if (isShowAtUserLayout(platformName)) {
                View view = LayoutInflater.from(this.activity).inflate(R.getLayoutRes(this.activity, "skyblue_editpage_at_layout"), null);
                TextView atDescTextView = (TextView) view.findViewById(R.getIdRes(this.activity, "atDescTextView"));
                TextView atTextView = (TextView) view.findViewById(R.getIdRes(this.activity, "atTextView"));
                OnClickListener atBtnClickListener = new OnClickListener() {
                    public void onClick(View v) {
                        FollowListPage subPage = new FollowListPage();
                        subPage.setPlatform((Platform) v.getTag());
                        subPage.showForResult(EditPage.this.activity, null, EditPage.this);
                    }
                };
                atTextView.setTag(platform);
                atTextView.setOnClickListener(atBtnClickListener);
                atDescTextView.setTag(platform);
                atDescTextView.setOnClickListener(atBtnClickListener);
                atTextView.setText(getAtUserButtonText(platformName));
                atDescTextView.setText(getContext().getString(R.getStringRes(this.activity, "list_friends"), new Object[]{getLogoName(platformName)}));
                atLayout.addView(view);
            }
        }
    }

    private void initImageListView() {
        HorizontalScrollView hScrollView = (HorizontalScrollView) findViewByResName("hScrollView");
        if (!initImageList(new ImageListResultsCallback() {
            public void onFinish(ArrayList<ImageInfo> results) {
                if (results != null) {
                    LinearLayout layout = (LinearLayout) EditPage.this.findViewByResName("imagesLinearLayout");
                    Iterator it = results.iterator();
                    while (it.hasNext()) {
                        ImageInfo imageInfo = (ImageInfo) it.next();
                        if (imageInfo.bitmap != null) {
                            layout.addView(EditPage.this.makeImageItemView(imageInfo));
                        }
                    }
                }
            }
        })) {
            hScrollView.setVisibility(8);
        }
    }

    private View makeImageItemView(final ImageInfo imageInfo) {
        final View view = LayoutInflater.from(this.activity).inflate(R.getLayoutRes(this.activity, "skyblue_editpage_inc_image_layout"), null);
        ImageView imageView = (ImageView) view.findViewById(R.getIdRes(this.activity, "imageView"));
        imageView.setImageBitmap(imageInfo.bitmap);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PicViewer pv = new PicViewer();
                pv.setImageBitmap(imageInfo.bitmap);
                pv.show(EditPage.this.activity, null);
            }
        });
        View removeBtn = view.findViewById(R.getIdRes(this.activity, "imageRemoveBtn"));
        removeBtn.setTag(imageInfo);
        removeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                view.setVisibility(8);
                EditPage.this.removeImage((ImageInfo) v.getTag());
            }
        });
        return view;
    }

    public void onClick(View v) {
        if (v.getTag() != null) {
            String tag = (String) v.getTag();
            if (tag.equals("close")) {
                for (Platform plat : this.platforms) {
                    ShareSDK.logDemoEvent(5, plat);
                }
                finish();
            } else if (tag.equals("ok")) {
                onShareButtonClick(v);
            }
        }
    }

    private void onShareButtonClick(View v) {
        if (this.shareParamMap.containsKey("title")) {
            this.shareParamMap.put("title", this.titleEditText.getText().toString().trim());
        }
        this.shareParamMap.put("text", this.textEditText.getText().toString().trim());
        setResultAndFinish();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int remain = 140 - this.textEditText.length();
        this.textCounterTextView.setText(String.valueOf(remain));
        this.textCounterTextView.setTextColor(remain > 0 ? -3158065 : SupportMenu.CATEGORY_MASK);
    }

    public void afterTextChanged(Editable s) {
    }

    public void onResult(HashMap<String, Object> data) {
        String atText = getJoinSelectedUser(data);
        if (atText != null) {
            this.textEditText.append(atText);
        }
    }

    public boolean onFinish() {
        this.textCounterTextView = null;
        this.textEditText = null;
        this.titleEditText = null;
        return super.onFinish();
    }
}
