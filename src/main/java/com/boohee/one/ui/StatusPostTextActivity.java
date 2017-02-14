package com.boohee.one.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.model.ApiError;
import com.boohee.model.QiniuPhoto;
import com.boohee.model.status.AttachMent;
import com.boohee.model.status.DraftBean;
import com.boohee.model.status.Mention;
import com.boohee.model.status.Post;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.event.RefreshPostEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.sport.DownloadService;
import com.boohee.one.ui.adapter.PostPicturePreviewAdapter;
import com.boohee.status.ContactsActivity;
import com.boohee.status.HotTagActivity;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.Const;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FilterDataSyncUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.Keyboard;
import com.boohee.utils.PhotoPickerHelper;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.DraftPopwindow;
import com.boohee.widgets.DraftPopwindow.IPopClickListener;
import com.chaowen.commentlibrary.emoji.EmojiViewPagerAdapter;
import com.chaowen.commentlibrary.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import com.chaowen.commentlibrary.emoji.Emojicon;
import com.chaowen.commentlibrary.emoji.People;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CirclePageIndicator;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusPostTextActivity extends BaseActivity implements OnClickEmojiListener {
    public static final  String   CHALLENGE_POSTS       = "/api/v1/challenges/%d/posts";
    private static final String   CLUB_POSTS            = "/api/v1/clubs/%d/club_posts";
    public static final  String   EXTRA_ATTACHMENT      = "attachMent";
    public static final  String   EXTRA_TEXT            = "extra_text";
    private static final String   KEY_ADD               = "add";
    public static final  String   KEY_COME_WITH_PICTURE = "key_come_with_picture";
    public static final  String   KEY_SELECTED_PICTURES = "key_select_pictures";
    private static final int      POST_MAX_LENGTH       = 500;
    private static final int      REQUEST_CODE_AT       = 1;
    private static final int      REQUEST_CODE_FILTER   = 4;
    private static final int      REQUEST_CODE_PICTURE  = 2;
    private static final int      REQUEST_CODE_TAG      = 5;
    public static final  int      REQUEST_SEND_POST     = 8;
    static final         String   TAG                   = StatusPostTextActivity.class
            .getSimpleName();
    private static final String   TIMELINE_POSTS        = "/api/v1/posts.json";
    private              Runnable asyncSendPost         = new Runnable() {
        public void run() {
            int baseNFId = (int) (System.currentTimeMillis() / 1000);
            StatusPostTextActivity.this.startSend(baseNFId);
            StatusPostTextActivity.this.sendPost(baseNFId);
        }
    };
    private AttachMent attachMent;
    @InjectView(2131427931)
    LinearLayout attachmentLayout;
    @InjectView(2131427554)
    ImageButton  btnEmoji;
    @InjectView(2131427936)
    TextView     charNumTextView;
    @InjectView(2131427934)
    CheckBox     checkBox;
    @InjectView(2131427929)
    EditText     editText;
    private int group_id = -1;
    @InjectView(2131427558)
    CirclePageIndicator       indicatorEmoji;
    @InjectView(2131427932)
    ImageView                 ivAttachment;
    @InjectView(2131427556)
    KPSwitchPanelLinearLayout lyEmoji;
    StatusPostTextActivity    mActivity;
    PostPicturePreviewAdapter mAdapter;
    int mCurEditPicPosition = -1;
    private EmojiViewPagerAdapter mEmojiPagerAdapter;
    private Handler mHandler = new Handler(Looper.myLooper());
    private Builder             mNFBuilder;
    private NotificationManager mNotificationManager;
    @InjectView(2131427930)
    GridView mPicGridView;
    final   ArrayList<String> mSelectPictures = new ArrayList();
    private String            mSendApi        = TIMELINE_POSTS;
    int maxUploadPicNums = 9;
    private DraftPopwindow popWindow;
    private UserPreference preference = UserPreference.getInstance(MyApplication.getContext());
    @InjectView(2131427935)
    CheckBox syncFood;
    @InjectView(2131427933)
    TextView tvAttachment;
    private final long[] vibrate = new long[]{0, 300};
    @InjectView(2131427557)
    ViewPager viewPagerEmoji;

    private class OnItemClick implements OnItemClickListener {
        private OnItemClick() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if ("add".equals((String) StatusPostTextActivity.this.mAdapter.getItem(position))) {
                StatusPostTextActivity.this.openPhotoPicker();
                return;
            }
            StatusPostTextActivity.this.mCurEditPicPosition = position;
            Uri imageUri = Uri.fromFile(new File((String) StatusPostTextActivity.this
                    .mSelectPictures.get(position)));
            Intent intent = new Intent(StatusPostTextActivity.this.mActivity, ImageEditActivity
                    .class);
            intent.setData(imageUri);
            StatusPostTextActivity.this.startActivityForResult(intent, 4);
        }
    }

    private class PopClickListener implements IPopClickListener {
        private PopClickListener() {
        }

        public void onRedoClick() {
        }

        public void onSaveClick() {
            DraftBean bean = DraftBean.parse(StatusPostTextActivity.this.preference.getString
                    (UserPreference.getUserKey(StatusPostTextActivity.this.activity)));
            if (bean == null) {
                bean = new DraftBean();
            }
            bean.setSendTextMsg(StatusPostTextActivity.this.editText.getText().toString());
            if (StatusPostTextActivity.this.mSelectPictures.size() > 1) {
                StatusPostTextActivity.this.mSelectPictures.remove("add");
                bean.selectedPictures = StatusPostTextActivity.this.mSelectPictures;
            }
            if (StatusPostTextActivity.this.attachMent != null) {
                bean.attachMent = StatusPostTextActivity.this.attachMent;
            }
            StatusPostTextActivity.this.preference.putString(UserPreference.getUserKey
                    (StatusPostTextActivity.this.activity), bean.toString());
            StatusPostTextActivity.this.finish();
        }

        public void onUnSaveClick() {
            StatusPostTextActivity.this.removeTempFilterRecord(StatusPostTextActivity.this.ctx);
            StatusPostTextActivity.this.finish();
        }
    }

    public static void startClubPost(Context srcContext, int clubId) {
        Intent intent = new Intent(srcContext, StatusPostTextActivity.class);
        intent.putExtra("clubId", clubId);
        srcContext.startActivity(intent);
    }

    public static void startChallengePost(Context srcContext, int challengeId) {
        Intent intent = new Intent(srcContext, StatusPostTextActivity.class);
        intent.putExtra("challengeId", challengeId);
        srcContext.startActivity(intent);
    }

    public static void comeWithExtraText(Context context, String text) {
        Intent intent = new Intent(context, StatusPostTextActivity.class);
        if (!TextUtils.isEmpty(text)) {
            intent.putExtra(EXTRA_TEXT, text);
        }
        context.startActivity(intent);
    }

    public static void comeWithAttachment(Context context, AttachMent attachment) {
        comeWithAttachmentAndExtraText(context, null, attachment);
    }

    public static void comeWithAttachmentAndExtraText(Context context, String text, AttachMent
            attachment) {
        Intent intent = new Intent(context, StatusPostTextActivity.class);
        if (!TextUtils.isEmpty(text)) {
            intent.putExtra(EXTRA_TEXT, text);
        }
        intent.putExtra(EXTRA_ATTACHMENT, attachment);
        context.startActivity(intent);
    }

    public static void comeWithPicture(Context context, String picUrl) {
        if (context != null && !TextUtils.isEmpty(picUrl)) {
            Intent intent = new Intent(context, StatusPostTextActivity.class);
            intent.putExtra(KEY_COME_WITH_PICTURE, picUrl);
            context.startActivity(intent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db);
        setTitle(R.string.a6p);
        ButterKnife.inject((Activity) this);
        addListener();
        init();
        MobclickAgent.onEvent(MyApplication.getContext(), Event.STATUS_SEND_PAGE);
    }

    private void addListener() {
        this.editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                int currentNum = StatusPostTextActivity.this.editText.length();
                StatusPostTextActivity.this.charNumTextView.setText(currentNum + " / " + 500);
                if (currentNum == 500) {
                    Helper.showToast((int) R.string.a6o);
                }
            }
        });
    }

    private void init() {
        this.mActivity = this;
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.mNFBuilder = new Builder(this.ctx);
        this.group_id = getIntExtra(Const.GROUP_ID);
        ArrayList<String> tempList = getIntent().getStringArrayListExtra(KEY_SELECTED_PICTURES);
        if (tempList != null && tempList.size() > 0) {
            this.mSelectPictures.clear();
            this.mSelectPictures.addAll(0, tempList);
            this.mSelectPictures.add("add");
        }
        initSendApi();
        if (getIntent() != null) {
            this.attachMent = (AttachMent) getIntent().getParcelableExtra(EXTRA_ATTACHMENT);
        }
        restoreDraft();
        initPicGridView();
        if (this.attachMent != null) {
            this.attachmentLayout.setVisibility(0);
            this.imageLoader.displayImage(this.attachMent.pic, this.ivAttachment);
            this.tvAttachment.setText(this.attachMent.title);
        }
        initEmoji();
        handlePictureURL();
    }

    private void handlePictureURL() {
        Intent intent = getIntent();
        if (intent != null) {
            String picUrl = intent.getStringExtra(KEY_COME_WITH_PICTURE);
            if (!TextUtils.isEmpty(picUrl)) {
                this.mSelectPictures.clear();
                this.mSelectPictures.add(picUrl);
                this.mSelectPictures.add("add");
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initEmoji() {
        KeyboardUtil.attach(this, this.lyEmoji);
        KPSwitchConflictUtil.attach(this.lyEmoji, this.btnEmoji, this.editText);
        Emojicon[] emojis = People.DATA;
        List<List<Emojicon>> pagers = new ArrayList();
        List<Emojicon> es = null;
        int size = 0;
        boolean justAdd = false;
        for (Emojicon ej : emojis) {
            if (size == 0) {
                es = new ArrayList();
            }
            if (size == 27) {
                es.add(new Emojicon(""));
            } else {
                es.add(ej);
            }
            size++;
            if (size == 28) {
                pagers.add(es);
                size = 0;
                justAdd = true;
            } else {
                justAdd = false;
            }
        }
        if (!(justAdd || es == null)) {
            int exSize = 28 - es.size();
            for (int i = 0; i < exSize; i++) {
                es.add(new Emojicon(""));
            }
            pagers.add(es);
        }
        this.mEmojiPagerAdapter = new EmojiViewPagerAdapter(this.ctx, pagers, DensityUtil.dip2px
                (this.ctx, 160.0f) / 4, this);
        this.viewPagerEmoji.setAdapter(this.mEmojiPagerAdapter);
        this.indicatorEmoji.setViewPager(this.viewPagerEmoji);
    }

    private void initSendApi() {
        if (getIntent() != null) {
            if (getIntent().getIntExtra("clubId", -1) > 0) {
                this.mSendApi = String.format(CLUB_POSTS, new Object[]{Integer.valueOf(clubId)});
            }
            if (getIntent().getIntExtra("challengeId", -1) > 0) {
                this.mSendApi = String.format(CHALLENGE_POSTS, new Object[]{Integer.valueOf
                        (challengId)});
            }
        }
        Helper.showLog(TAG, "mSendApi : " + this.mSendApi);
    }

    private void initPicGridView() {
        this.mAdapter = new PostPicturePreviewAdapter(this, this.mSelectPictures, this
                .maxUploadPicNums, getItemSize());
        this.mPicGridView.setAdapter(this.mAdapter);
        this.mPicGridView.setOnItemClickListener(new OnItemClick());
    }

    @OnClick({2131427929, 2131427937, 2131427938, 2131427939, 2131427554})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_post_text_editText:
                editTextClicked();
                return;
            case R.id.status_post_text_pictureBtn:
                openPhotoPicker();
                return;
            case R.id.status_post_text_atBtn:
                Keyboard.closeAll(this.ctx);
                startActivityForResult(new Intent(this, ContactsActivity.class), 1);
                return;
            case R.id.status_post_text_tagBtn:
                Keyboard.closeAll(this.ctx);
                startActivityForResult(new Intent(this, HotTagActivity.class), 5);
                return;
            default:
                return;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 1 || event.getKeyCode() != 4 || this.lyEmoji.getVisibility() !=
                0) {
            return super.dispatchKeyEvent(event);
        }
        KPSwitchConflictUtil.hidePanelAndKeyboard(this.lyEmoji);
        return true;
    }

    public void onEmojiClick(Emojicon emoji) {
        input(emoji);
    }

    public void onDelete() {
        backspace();
    }

    private void input(Emojicon emojicon) {
        if (this.editText != null && emojicon != null) {
            int start = this.editText.getSelectionStart();
            int end = this.editText.getSelectionEnd();
            if (start < 0) {
                this.editText.append(emojicon.getEmoji());
            } else {
                this.editText.getText().replace(Math.min(start, end), Math.max(start, end),
                        emojicon.getEmoji(), 0, emojicon.getEmoji().length());
            }
        }
    }

    public void backspace() {
        this.editText.dispatchKeyEvent(new KeyEvent(0, 0, 0, 67, 0, 0, 0, 0, 6));
    }

    public void onResume() {
        super.onResume();
        ViewUtils.setSelection(this.editText);
        String syncData = getIntent().getStringExtra("FILTER_DATA");
        if (TextUtils.isEmpty(syncData)) {
            refreshSyncCheckBox(FilterDataSyncUtil.getTagData());
            return;
        }
        refreshSyncCheckBox(syncData);
        this.syncFood.setChecked(getIntent().getBooleanExtra("IS_NEED_SYNC", false));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            switch (requestCode) {
                case 1:
                    this.editText.append(data.getStringExtra("contact"));
                    return;
                case 2:
                    ArrayList<String> tempList = data.getStringArrayListExtra
                            (MultiImageSelectorActivity.EXTRA_RESULT);
                    if (tempList != null && tempList.size() > 0) {
                        this.mSelectPictures.clear();
                        this.mSelectPictures.addAll(tempList);
                        this.mSelectPictures.add("add");
                        this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                case 4:
                    Uri uri = data.getData();
                    String path = uri == null ? "" : uri.getPath();
                    if (this.mCurEditPicPosition >= 0 && !TextUtils.isEmpty(path) && this
                            .mSelectPictures != null && this.mSelectPictures.size() != 0) {
                        this.mSelectPictures.remove(this.mCurEditPicPosition);
                        this.mSelectPictures.add(this.mCurEditPicPosition, path);
                        this.mAdapter.notifyDataSetChanged();
                        this.editText.setText(FilterDataSyncUtil.getTagData() + this.editText
                                .getText().toString());
                        return;
                    }
                    return;
                case 5:
                    this.editText.append(data.getStringExtra(DownloadService.EXTRA_TAG) + " ");
                    return;
                default:
                    return;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "确定").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        String body = this.editText.getText().toString().trim().replace("\n", "");
        if (TextUtils.isEmpty(body)) {
            Helper.showToast((CharSequence) "状态不能为空哦~");
            return true;
        } else if (body.startsWith("#") && body.endsWith("#") && body.length() < 25 && ((this
                .mSelectPictures == null || this.mSelectPictures.size() == 0) && this.attachMent
                == null)) {
            Helper.showToast((CharSequence) "动态太空了，再说点什么吧~");
            return true;
        } else {
            Keyboard.closeAll(this.ctx);
            this.mHandler.post(this.asyncSendPost);
            return true;
        }
    }

    private void removeTempFilterRecord(Context ctx) {
        FilterDataSyncUtil.removeFilterTag(ctx);
        refreshSyncCheckBox(FilterDataSyncUtil.getTagData());
    }

    private void refreshSyncCheckBox(String data) {
    }

    private void editTextClicked() {
        Keyboard.open(this.activity, this.editText);
    }

    public void onBackPressed() {
        Keyboard.close(this.activity, this.editText);
        handleUnSendPost();
    }

    private void restoreDraft() {
        String extraStr = getStringExtra(EXTRA_TEXT);
        DraftBean bean = DraftBean.parse(this.preference.getString(UserPreference.getUserKey(this
                .ctx)));
        if (bean != null) {
            if (TextUtils.isEmpty(bean.getSendTextMsg()) || TextUtils.isEmpty(extraStr) || !bean
                    .getSendTextMsg().contains(extraStr)) {
                this.editText.setText(extraStr);
                this.editText.append(bean.getSendTextMsg());
            } else {
                this.editText.append(bean.getSendTextMsg());
            }
            if (bean.selectedPictures != null && bean.selectedPictures.size() > 0) {
                this.mSelectPictures.clear();
                this.mSelectPictures.addAll(bean.selectedPictures);
                this.mSelectPictures.add("add");
            }
            if (bean.attachMent != null) {
                this.attachMent = bean.attachMent;
            }
            this.preference.remove(UserPreference.getUserKey(this.activity));
        } else if (!TextUtils.isEmpty(extraStr)) {
            this.editText.setText(getStringExtra(EXTRA_TEXT) + " ");
        }
    }

    private void handleUnSendPost() {
        if (this.popWindow != null && this.popWindow.isShowing()) {
            this.popWindow.dismiss();
        } else if (TextUtils.isEmpty(this.editText.getText().toString()) && this.mSelectPictures
                .size() <= 1 && this.attachMent == null) {
            finish();
        } else {
            if (this.popWindow == null) {
                this.popWindow = new DraftPopwindow(this.activity, new PopClickListener(), true);
            }
            this.popWindow.show();
        }
    }

    private int getItemSize() {
        return ((getResources().getDisplayMetrics().widthPixels - (getResources()
                .getDimensionPixelOffset(R.dimen.gt) * 3)) - (getResources()
                .getDimensionPixelOffset(R.dimen.el) * 2)) / 4;
    }

    private void openPhotoPicker() {
        KeyBoardUtils.closeKeybord(this.activity, this.editText);
        ArrayList tempSelectPath = new ArrayList();
        tempSelectPath.addAll(this.mSelectPictures);
        tempSelectPath.remove("add");
        PhotoPickerHelper.show(this.mActivity, true, this.maxUploadPicNums, tempSelectPath, 2);
    }

    private PendingIntent getSendFaildIntent(int nfId, boolean isNeedSync, String syncData) {
        Intent intent = new Intent(this, getClass());
        ArrayList<String> tempList = new ArrayList();
        tempList.addAll(this.mSelectPictures);
        tempList.remove("add");
        intent.putStringArrayListExtra(KEY_SELECTED_PICTURES, tempList);
        intent.setFlags(268435456);
        intent.putExtra(EXTRA_TEXT, this.editText.getText().toString());
        intent.putExtra("FILTER_DATA", syncData);
        intent.putExtra("IS_NEED_SYNC", isNeedSync);
        intent.putExtra(EXTRA_ATTACHMENT, this.attachMent);
        return PendingIntent.getActivity(this, nfId, intent, 1073741824);
    }

    private void startSend(int nfId) {
        String content = "正在发送";
        try {
            this.mNFBuilder.setContentTitle(content).setTicker(content).setSmallIcon(R.drawable
                    .w0).setProgress(100, 20, true).setWhen(System.currentTimeMillis());
            this.mNotificationManager.notify(nfId, this.mNFBuilder.build());
        } catch (NullPointerException e) {
        }
        FilterDataSyncUtil.removeFilterTag(this.ctx);
    }

    private void sendSuccess(int nfId) {
        String content = "恭喜您，发送成功！";
        try {
            this.mNFBuilder.setContentTitle(content).setTicker(content).setSmallIcon(R.drawable
                    .vz).setVibrate(this.vibrate);
            this.mNotificationManager.notify(nfId, this.mNFBuilder.build());
            this.mNotificationManager.cancel(nfId);
        } catch (NullPointerException e) {
        }
    }

    private void sendFaild(String content, PendingIntent sendFaildIntent, int nfId) {
        try {
            this.mNFBuilder.setContentTitle("发送失败").setTicker(content).setContentText(content)
                    .setSmallIcon(R.drawable.vy).setDefaults(2).setWhen(System.currentTimeMillis
                    ()).setProgress(0, 0, false).setContentIntent(sendFaildIntent).setAutoCancel
                    (true);
            this.mNotificationManager.notify(nfId, this.mNFBuilder.build());
        } catch (NullPointerException e) {
        }
    }

    private void uploadImage(List<String> dataList, boolean isNeedSync, PendingIntent
            sendFaildIntent, int nfId, String syncData) {
        List pathList = new ArrayList();
        pathList.addAll(dataList);
        int index = pathList.indexOf("add");
        if (index >= 0) {
            pathList.remove(index);
        }
        final boolean z = isNeedSync;
        final PendingIntent pendingIntent = sendFaildIntent;
        final int i = nfId;
        final String str = syncData;
        QiniuUploader.upload(Prefix.status, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                StatusPostTextActivity.this.doSend(infos, z, pendingIntent, i, str);
            }

            public void onError(String msg) {
                Helper.showToast((CharSequence) msg);
                StatusPostTextActivity.this.dismissLoading();
            }

            public void onFinish() {
            }
        }, pathList);
    }

    private void sendPost(int nfId) {
        showLoading();
        String syncData = getIntent().getStringExtra("FILTER_DATA");
        if (TextUtils.isEmpty(syncData)) {
            syncData = FilterDataSyncUtil.getSyncData();
        }
        PendingIntent pendingIntent = getSendFaildIntent(nfId, false, syncData);
        if (this.mSelectPictures.size() > 1) {
            uploadImage(this.mSelectPictures, false, pendingIntent, nfId, syncData);
        } else {
            doSend(null, false, pendingIntent, nfId, syncData);
        }
    }

    private void doSend(List<QiniuModel> infos, boolean isNeedSync, PendingIntent
            sendFaildIntent, int nfId, String syncData) {
        final boolean z = isNeedSync;
        final String str = syncData;
        final int i = nfId;
        final PendingIntent pendingIntent = sendFaildIntent;
        StatusApi.sendPost(this.activity, this.mSendApi, createPostJSONObject(infos), new
                JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                MobclickAgent.onEvent(MyApplication.getContext(), Event.STATUS_ADD_STATUS_OK);
                if (object.has("message")) {
                    Helper.showToast(object.optString("message"));
                }
                StatusPostTextActivity.this.setResult(-1);
                FilterDataSyncUtil.syncData(MyApplication.getContext(), z, str);
                StatusPostTextActivity.this.removeTempFilterRecord(MyApplication.getContext());
                StatusPostTextActivity.this.sendSuccess(i);
                EventBus.getDefault().post(new RefreshPostEvent());
                StatusPostTextActivity.this.finish();
            }

            public void fail(String message) {
                super.fail(message);
                StatusPostTextActivity.this.sendFaild("网络异常，请重试~~", pendingIntent, i);
            }

            public void ok(JSONObject object, boolean hasError) {
                if (hasError) {
                    Helper.showToast(ApiError.getErrorMessage(object));
                }
            }

            public void onFinish() {
                super.onFinish();
                StatusPostTextActivity.this.dismissLoading();
                try {
                    StatusPostTextActivity.this.mNotificationManager.cancel(i);
                } catch (Exception e) {
                }
            }
        });
    }

    private JsonParams createPostJSONObject(List<QiniuModel> pictureList) {
        String body = this.editText.getText().toString().trim();
        JsonParams root = new JsonParams();
        JSONObject post = new JSONObject();
        try {
            post.put("body", body);
            post.put("category", "其他");
            if (this.checkBox.isChecked()) {
                post.put("private", true);
            } else {
                post.put("private", false);
            }
            if (this.group_id > 0) {
                post.put(Const.GROUP_ID, this.group_id);
            }
            if (this.attachMent != null) {
                post.put("attachments", new JSONObject(FastJsonUtils.toJson(this.attachMent)));
            }
            if (pictureList != null && pictureList.size() > 0) {
                post.put(WeightRecordDao.PHOTOS, createPicJSONArray(pictureList));
                post.put("type", Post.IMAGE_TYPE);
            }
            root.put(Mention.POST, post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }

    private JSONArray createPicJSONArray(List<QiniuModel> pictureList) {
        JSONArray photos = new JSONArray();
        try {
            for (QiniuModel model : pictureList) {
                JSONObject photo = new JSONObject();
                photo.put("qiniu_key", model.key);
                photo.put("qiniu_hash", model.hash);
                photo.put("_type", QiniuPhoto.TYPE_QINIU);
                photos.put(photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos;
    }
}
