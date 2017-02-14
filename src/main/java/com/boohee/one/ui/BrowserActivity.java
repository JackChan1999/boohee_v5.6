package com.boohee.one.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import boohee.lib.share.ShareManager;

import com.boohee.api.FavoriteApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.FavoriteArticle;
import com.boohee.model.status.Post;
import com.boohee.one.R;
import com.boohee.one.bet.BetBrowserFragment;
import com.boohee.one.bet.BetPaySuccessActivity;
import com.boohee.one.event.DuShouPayEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.pay.PayService;
import com.boohee.one.pay.PayService.OnFinishPayListener;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.UrlUtils;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import de.greenrobot.event.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"SetJavaScriptEnabled"})
public class BrowserActivity extends GestureActivity implements OnFinishPayListener {
    private static final int    FILECHOOSER_RESULTCODE = 1;
    public static final  String OUT_LINK_URL           = "http://lina.elementfresh" +
            ".com/boohee201508";
    static final         String TAG                    = BrowserActivity.class.getSimpleName();
    public static final  String TITLE                  = "title";
    public static final  String URL                    = "url";
    private boolean backPressed;
    private int     favoriteId = -1;
    private boolean isFavorite = false;
    private   boolean            isPressedBack;
    private   Menu               mMenu;
    private   PayService         mPayService;
    protected ProgressBar        mProgressBar;
    protected String             mTitle;
    private   ValueCallback<Uri> mUploadMessage;
    protected String             mUrl;
    protected String             originUrl;
    private   String             shareDescription;
    private   String             shareImageUrl;
    private   String             shareTitle;
    private   String             shareUrl;
    protected WebView            webView;

    public final class JSInterface {
        @JavascriptInterface
        public void set(String json) {
            try {
                JSONObject obj = new JSONObject(json);
                BrowserActivity.this.shareUrl = obj.optString("url");
                BrowserActivity.this.shareTitle = obj.optString("title");
                BrowserActivity.this.shareDescription = obj.optString("description");
                BrowserActivity.this.shareImageUrl = obj.optString(Post.IMAGE_TYPE);
            } catch (JSONException e) {
            }
        }
    }

    public class MyWebClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            BrowserActivity.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("image/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            BrowserActivity.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("*/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), 1);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String
                capture) {
            BrowserActivity.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("image/*");
            BrowserActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
        }

        public void onProgressChanged(WebView view, int progress) {
            Helper.showLog(BrowserActivity.TAG, progress + "");
            BrowserActivity.this.mProgressBar.setVisibility(0);
            BrowserActivity.this.mProgressBar.setProgress(progress);
            if (progress == 100) {
                BrowserActivity.this.mProgressBar.setVisibility(8);
            }
        }

        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                BrowserActivity.this.mTitle = title;
                BrowserActivity.this.setTitle(BrowserActivity.this.mTitle);
            }
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.e7);
        this.mTitle = getStringExtra("title");
        if (!TextUtils.isEmpty(this.mTitle)) {
            setTitle(this.mTitle);
        }
        this.originUrl = getStringExtra("url");
        this.mUrl = UrlUtils.handleUrl(this.originUrl);
        Helper.showLog(TAG, this.mUrl);
        if (TextUtils.isEmpty(this.mUrl)) {
            Helper.showToast((CharSequence) "无效的链接");
            return;
        }
        initFavoriteMenu();
        initView();
        EventBus.getDefault().register(this);
    }

    protected void initFavoriteMenu() {
        if (TextUtils.isEmpty(this.mUrl) || !this.mUrl.contains("favorite=false")) {
            FavoriteApi.checkFavoriteArticle(this.mUrl, initFavoriteModel(), new JsonCallback
                    (this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    BrowserActivity.this.refreshMenu(object);
                }

                public void onFinish() {
                    super.onFinish();
                }
            }, this.activity);
        }
    }

    private JsonParams initFavoriteModel() {
        JsonParams params = new JsonParams();
        FavoriteArticle article = new FavoriteArticle();
        article.url = this.originUrl;
        if (!TextUtils.isEmpty(this.mTitle)) {
            article.title = this.mTitle;
        }
        try {
            params.put("favorite_article", new JSONObject(FastJsonUtils.toJson(article)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void refreshMenu(JSONObject object) {
        try {
            this.isFavorite = object.optBoolean("exist");
            this.favoriteId = object.optInt("id");
            setUpFavoriteBtn();
            setUpMoreBtn();
        } catch (Exception e) {
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            this.mTitle = intent.getStringExtra("title");
            if (!TextUtils.isEmpty(this.mTitle)) {
                setTitle(this.mTitle);
            }
            this.originUrl = intent.getStringExtra("url");
            this.mUrl = UrlUtils.handleUrl(this.originUrl);
            Helper.showLog(TAG, this.mUrl);
            if (TextUtils.isEmpty(this.mUrl)) {
                Helper.showToast((CharSequence) "无效的链接");
            }
            if (this.webView != null) {
                this.webView.loadUrl(this.mUrl);
            }
        }
    }

    private void initView() {
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.webView = (WebView) findViewById(R.id.wv_content);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setUserAgentString(this.webView.getSettings()
                .getUserAgentString() + " App/boohee");
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.addJavascriptInterface(new JSInterface(), "jsObj");
        this.webView.setWebViewClient(createChromeClient());
        this.webView.setWebChromeClient(new MyWebClient());
    }

    protected WebViewClient createChromeClient() {
        return new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    if (TextUtils.equals(url, BrowserActivity.this.originUrl) && BrowserActivity
                            .this.isPressedBack && !BrowserActivity.this.webView.canGoBack()) {
                        BrowserActivity.this.finish();
                    } else {
                        BrowserActivity.this.isPressedBack = false;
                        Helper.showLog(BrowserActivity.TAG, "url-->" + url);
                        Uri uri = Uri.parse(url);
                        Intent intent;
                        if (url.contains(ShareConstants.PATCH_SUFFIX) || url.contains
                                ("http://lina.elementfresh.com/boohee201508")) {
                            try {
                                intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(uri);
                                BrowserActivity.this.startActivity(intent);
                            } catch (Exception e) {
                            }
                        } else if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains
                                ("https://")) {
                            view.loadUrl(url);
                            if (url.contains("user_diets.html")) {
                                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                            }
                            BrowserActivity.this.originUrl = url;
                            BrowserActivity.this.mUrl = UrlUtils.handleUrl(BrowserActivity.this
                                    .originUrl);
                            BrowserActivity.this.setUpMilstoneBtn();
                            BrowserActivity.this.setupShareBtn();
                            BrowserActivity.this.initFavoriteMenu();
                        } else if (!(BooheeScheme.handleUrl(BrowserActivity.this, url) || uri ==
                                null)) {
                            try {
                                intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(uri);
                                BrowserActivity.this.startActivity(intent);
                            } catch (Exception e2) {
                            }
                        }
                    }
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                BrowserActivity.this.mProgressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                BrowserActivity.this.mProgressBar.setVisibility(8);
                String tmpTitle = BrowserActivity.this.webView.getTitle();
                if (!TextUtils.isEmpty(tmpTitle) && !TextUtils.equals(BrowserActivity.this
                        .mTitle, tmpTitle)) {
                    BrowserActivity.this.mTitle = tmpTitle;
                    if (!TextUtil.isEmpty(BrowserActivity.this.mTitle)) {
                        BrowserActivity.this.setTitle(BrowserActivity.this.mTitle);
                    }
                }
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.e, menu);
        this.mMenu = menu;
        if (!(this.webView == null || TextUtils.isEmpty(this.mUrl))) {
            this.webView.loadUrl(this.mUrl);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        setupShareBtn();
        setUpMilstoneBtn();
        setUpFavoriteBtn();
        setUpMoreBtn();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case R.id.action_directory:
                startActivity(new Intent(this.activity, MilestoneActivity.class));
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.add_favorite:
                addFavorite();
                return true;
            case R.id.delete_favorite:
                deleteFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addFavorite() {
        FavoriteApi.addFavoriteArticle(initFavoriteModel(), new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                BrowserActivity.this.addFavoriteSuccess(object);
            }
        }, this.activity);
    }

    private void addFavoriteSuccess(JSONObject object) {
        try {
            this.favoriteId = object.optInt("id");
            this.isFavorite = true;
            Helper.showToast((int) R.string.b4);
            invalidateOptionsMenu();
        } catch (Exception e) {
        }
    }

    private void deleteFavorite() {
        FavoriteApi.deleteFavoriteArticle(this.favoriteId, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                BrowserActivity.this.deleteFavoriteSuccess();
            }
        }, this.activity);
    }

    private void deleteFavoriteSuccess() {
        this.favoriteId = -1;
        Helper.showToast((int) R.string.i8);
        this.isFavorite = false;
        invalidateOptionsMenu();
    }

    protected void setupShareBtn() {
        if (this.mMenu != null && this.mMenu.size() > 0) {
            MenuItem item = this.mMenu.findItem(R.id.action_share);
            if (item != null) {
                boolean z = !TextUtils.isEmpty(this.mUrl) && this.mUrl.contains("share=true");
                item.setVisible(z);
            }
        }
    }

    protected void setUpMilstoneBtn() {
        if (this.mMenu != null && this.mMenu.size() > 0) {
            MenuItem item = this.mMenu.findItem(R.id.action_directory);
            if (item != null) {
                boolean z = !TextUtils.isEmpty(this.mUrl) && this.mUrl.contains
                        ("/api/v1/milestones/daily");
                item.setVisible(z);
            }
        }
    }

    private void setUpFavoriteBtn() {
        boolean z = false;
        if (this.mMenu != null && this.mMenu.size() > 0) {
            MenuItem addFavorite = this.mMenu.findItem(R.id.add_favorite);
            MenuItem deleteFavorite = this.mMenu.findItem(R.id.delete_favorite);
            if (addFavorite == null || deleteFavorite == null || TextUtils.isEmpty(this.mUrl) ||
                    !this.mUrl.contains("favorite=false")) {
                if (addFavorite != null) {
                    if (!this.isFavorite) {
                        z = true;
                    }
                    addFavorite.setVisible(z);
                }
                if (deleteFavorite != null) {
                    deleteFavorite.setVisible(this.isFavorite);
                    return;
                }
                return;
            }
            addFavorite.setVisible(false);
            deleteFavorite.setVisible(false);
        }
    }

    private void setUpMoreBtn() {
        boolean visible = false;
        for (int id : new int[]{R.id.add_favorite, R.id.delete_favorite, R.id.action_directory, R
                .id.action_share}) {
            if (visible || this.mMenu.findItem(id).isVisible()) {
                visible = true;
            } else {
                visible = false;
            }
            if (visible) {
                break;
            }
        }
        this.mMenu.findItem(R.id.action_more).setVisible(visible);
    }

    private void share() {
        ShareManager.share((Context) this, this.shareTitle, this.shareDescription + this
                .shareUrl, this.shareUrl, this.shareImageUrl);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == -1) {
            if (requestCode == 1) {
                if (this.mUploadMessage != null) {
                    Uri result = (intent == null || resultCode != -1) ? null : intent.getData();
                    this.mUploadMessage.onReceiveValue(result);
                    this.mUploadMessage = null;
                }
            } else if (requestCode == 168 && this.mPayService != null) {
                this.mPayService.onPaymentResult(intent);
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.webView != null) {
            this.webView.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.webView != null) {
            this.webView.onResume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.webView != null) {
            this.webView.stopLoading();
        }
    }

    protected void onDestroy() {
        if (this.webView != null) {
            this.webView.removeAllViews();
            this.webView.destroy();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onPaySuccess() {
    }

    public void onPayFinished() {
    }

    public void onBackPressed() {
        this.isPressedBack = true;
        if (this.webView == null || !this.webView.canGoBack()) {
            super.onBackPressed();
        } else {
            this.webView.goBack();
        }
    }

    public static void comeOnBaby(Context context, String title, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent(context, BrowserActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    public void reload() {
        if (this.webView != null && !TextUtils.isEmpty(this.mUrl)) {
            this.webView.loadUrl(this.mUrl);
        }
    }

    public void onEventMainThread(DuShouPayEvent event) {
        this.mPayService = new PayService(this.activity);
        final int orderId = event.getOrderId();
        this.mPayService.setOnFinishPayLinstener(new OnFinishPayListener() {
            public void onPaySuccess() {
                Helper.showToast((CharSequence) "支付成功");
                BrowserActivity.this.reload();
                EventBus.getDefault().post(BetBrowserFragment.REFFRESH);
                Intent intent = new Intent(BrowserActivity.this.activity, BetPaySuccessActivity
                        .class);
                intent.putExtra("orderId", orderId);
                BrowserActivity.this.activity.startActivity(intent);
                BrowserActivity.this.activity.finish();
            }

            public void onPayFinished() {
            }
        });
        this.mPayService.startPay(orderId, "alipay", true);
    }
}
