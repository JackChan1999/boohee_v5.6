package com.boohee.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Const;
import com.boohee.utility.RegularUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeProfileActivity1 extends GestureActivity {
    public static final String EXTRA_CODE            = "code";
    public static final String EXTRA_CODE_TEXT       = "codeText";
    public static final String EXTRA_DEFAULT_CONTENT = "default_content";
    static final        String TAG                   = ChangeProfileActivity1.class.getSimpleName();
    private TextView attributeText;
    private String   code;
    private EditText contentEdit;
    private String   defaultContent;
    private TextView wordNumTips;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.ef);
        setTitle(R.string.gm);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        this.code = intent.getStringExtra("code");
        this.defaultContent = intent.getStringExtra(EXTRA_DEFAULT_CONTENT);
        Helper.showLog(TAG, "code:" + this.code);
        this.attributeText = (TextView) findViewById(R.id.attribute_text);
        this.attributeText.setText(intent.getStringExtra(EXTRA_CODE_TEXT));
        this.wordNumTips = (TextView) findViewById(R.id.word_number_tips);
        this.contentEdit = (EditText) findViewById(R.id.content);
        this.contentEdit.setText(this.defaultContent);
        if (UserDao.USER_NAME.equals(this.code)) {
            this.contentEdit.setSingleLine();
            this.wordNumTips.setText(R.string.a_j);
        } else if ("description".equals(this.code)) {
            this.contentEdit.setLines(3);
            this.wordNumTips.setText(R.string.lo);
        } else {
            this.contentEdit.setInputType(2);
            this.wordNumTips.setVisibility(8);
        }
        Selection.setSelection(this.contentEdit.getText(), this.contentEdit.length());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.ge).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        onComplete();
        return true;
    }

    private void onComplete() {
        String content = this.contentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Helper.showToast((int) R.string.ex);
        } else if (content.equals(this.defaultContent)) {
            finish();
        } else if (!UserDao.USER_NAME.equals(this.code) || RegularUtils.checkUserName(content)) {
            int length = content.length();
            if (UserDao.USER_NAME.equals(this.code)) {
                if (length > 25) {
                    Helper.showToast((int) R.string.a_j);
                    return;
                }
            } else if ("description".equals(this.code) && length > 50) {
                Helper.showToast((int) R.string.lo);
                return;
            }
            showLoading();
            RecordApi.updateUsersChangeProfile(this.activity, this.code, content, new
                    JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    Intent intent = new Intent();
                    try {
                        User user = User.parseUser(object.getJSONObject("profile"));
                        new UserDao(ChangeProfileActivity1.this.ctx).add(user);
                        intent.putExtra(Const.USER, user);
                        ChangeProfileActivity1.this.setResult(-1, intent);
                        ChangeProfileActivity1.this.finish();
                        EventBus.getDefault().post(new UserIntEvent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    ChangeProfileActivity1.this.dismissLoading();
                }
            });
        } else {
            Helper.showToast((int) R.string.fi);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(UserIntEvent userIntEvent) {
    }
}
