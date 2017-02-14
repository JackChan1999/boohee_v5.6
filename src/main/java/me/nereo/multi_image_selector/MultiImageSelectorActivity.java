package me.nereo.multi_image_selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import com.umeng.socialize.common.SocializeConstants;
import java.io.File;
import java.util.ArrayList;

public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment$Callback {
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    public static final String EXTRA_RESULT = "select_result";
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    public static final int MODE_MULTI = 1;
    public static final int MODE_SINGLE = 0;
    private int mDefaultCount;
    private Button mSubmitButton;
    private ArrayList<String> resultList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        Intent intent = getIntent();
        this.mDefaultCount = intent.getIntExtra("max_select_count", 9);
        int mode = intent.getIntExtra("select_count_mode", 1);
        boolean isShow = intent.getBooleanExtra("show_camera", true);
        if (mode == 1 && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            this.resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("max_select_count", this.mDefaultCount);
        bundle.putInt("select_count_mode", mode);
        bundle.putBoolean("show_camera", isShow);
        getSupportFragmentManager().beginTransaction().add(R.id.image_grid, MultiImageSelectorFragment.newInstance(bundle, this.resultList), MultiImageSelectorFragment.TAG).commit();
        findViewById(R.id.btn_back).setOnClickListener(new 1(this));
        this.mSubmitButton = (Button) findViewById(R.id.commit);
        this.mSubmitButton.setOnClickListener(new 2(this));
        if (this.resultList == null || this.resultList.size() <= 0) {
            this.mSubmitButton.setText(getResources().getString(R.string.ok));
            this.mSubmitButton.setEnabled(false);
            return;
        }
        this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + this.resultList.size() + "/" + this.mDefaultCount + SocializeConstants.OP_CLOSE_PAREN);
        this.mSubmitButton.setEnabled(true);
    }

    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        this.resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, this.resultList);
        setResult(-1, data);
        finish();
    }

    public void onImageSelected(String path) {
        if (!this.resultList.contains(path)) {
            this.resultList.add(path);
        }
        if (this.resultList.size() > 0) {
            this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + this.resultList.size() + "/" + this.mDefaultCount + SocializeConstants.OP_CLOSE_PAREN);
            if (!this.mSubmitButton.isEnabled()) {
                this.mSubmitButton.setEnabled(true);
            }
        }
    }

    public void onImageUnselected(String path) {
        if (this.resultList.contains(path)) {
            this.resultList.remove(path);
            this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + this.resultList.size() + "/" + this.mDefaultCount + SocializeConstants.OP_CLOSE_PAREN);
        } else {
            this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + this.resultList.size() + "/" + this.mDefaultCount + SocializeConstants.OP_CLOSE_PAREN);
        }
        if (this.resultList.size() == 0) {
            this.mSubmitButton.setText(getResources().getString(R.string.ok));
            this.mSubmitButton.setEnabled(false);
        }
    }

    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            Intent data = new Intent();
            this.resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, this.resultList);
            setResult(-1, data);
            finish();
        }
    }

    public void refreshSubmitBtnState() {
        if (this.resultList.size() == 0) {
            this.mSubmitButton.setText(getResources().getString(R.string.ok));
            this.mSubmitButton.setEnabled(false);
            return;
        }
        this.mSubmitButton.setEnabled(true);
        this.mSubmitButton.setText(getResources().getString(R.string.ok) + SocializeConstants.OP_OPEN_PAREN + this.resultList.size() + "/" + this.mDefaultCount + SocializeConstants.OP_CLOSE_PAREN);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.resultList != null) {
            this.resultList.clear();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null && requestCode == 1) {
            setResult(-1, data);
            finish();
        }
    }
}
