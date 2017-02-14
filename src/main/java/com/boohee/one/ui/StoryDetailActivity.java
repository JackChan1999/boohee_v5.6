package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.FavoriteApi;
import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.FavoriteArticle;
import com.boohee.model.StoryInfo;
import com.boohee.model.status.Post;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utility.TimeLineUtility;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class StoryDetailActivity extends GestureActivity {
    public static final String ID    = "id";
    public static final String TITLE = "title";
    public static final String URL   = "url";
    @InjectView(2131429434)
    CheckBox cbCollect;
    @InjectView(2131428102)
    CheckBox cbPraise;
    private int     favoriteId = -1;
    private boolean isFavorite = false;
    private String    mStoryId;
    private String    mTitle;
    private String    mUrl;
    private String    originUrl;
    private String    shareDescription;
    private String    shareImageUrl;
    private String    shareTitle;
    private String    shareUrl;
    private StoryInfo storyInfo;
    @InjectView(2131428105)
    TextView tvComment;
    @InjectView(2131428103)
    TextView tvPraisePlus;
    @InjectView(2131427953)
    WebView  webView;

    public final class JSInterface {
        @JavascriptInterface
        public void set(String json) {
            try {
                JSONObject obj = new JSONObject(json);
                StoryDetailActivity.this.shareUrl = obj.optString("url");
                StoryDetailActivity.this.shareTitle = obj.optString("title");
                StoryDetailActivity.this.shareDescription = obj.optString("description");
                StoryDetailActivity.this.shareImageUrl = obj.optString(Post.IMAGE_TYPE);
            } catch (JSONException e) {
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg);
        ButterKnife.inject((Activity) this);
        this.originUrl = getStringExtra("url");
        this.mUrl = UrlUtils.handleUrl(this.originUrl);
        this.mStoryId = getStringExtra("id");
        this.mTitle = getStringExtra("title");
        if (!TextUtils.isEmpty(this.mTitle)) {
            setTitle(this.mTitle);
        }
        if (TextUtils.isEmpty(this.mUrl)) {
            Helper.showToast((CharSequence) "无效的链接");
        } else if (!TextUtils.isEmpty(this.mStoryId)) {
            initView();
            initCollect();
            initPraiseAndComment();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.w, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (this.shareTitle != null) {
                    ShareManager.share((Context) this, this.shareTitle, this.shareDescription +
                            this.shareUrl, this.shareUrl, this.shareImageUrl);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({2131428101, 2131428104, 2131428164})
    public void onClick(View v) {
        if (this.storyInfo != null) {
            switch (v.getId()) {
                case R.id.rl_praise:
                    if (this.cbPraise.isChecked()) {
                        unPraiseStory();
                        return;
                    } else {
                        praiseStory();
                        return;
                    }
                case R.id.ll_comment:
                    StoryCommentActivity.comeOn(this.activity, this.mStoryId);
                    return;
                case R.id.ll_collect:
                    if (this.isFavorite) {
                        deleteFavorite();
                        return;
                    } else {
                        addFavorite();
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private void praiseStory() {
        StatusApi.praiseStory(this.mStoryId, this.ctx, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (StoryDetailActivity.this.storyInfo != null) {
                    StoryDetailActivity.this.cbPraise.setChecked(true);
                    CheckBox checkBox = StoryDetailActivity.this.cbPraise;
                    StoryInfo access$000 = StoryDetailActivity.this.storyInfo;
                    int i = access$000.envious_count + 1;
                    access$000.envious_count = i;
                    checkBox.setText(String.valueOf(i));
                    TimeLineUtility.setPlusAnimation(StoryDetailActivity.this.tvPraisePlus);
                }
            }
        });
    }

    private void unPraiseStory() {
        StatusApi.unPraiseStory(this.mStoryId, this.ctx, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (StoryDetailActivity.this.storyInfo != null) {
                    StoryDetailActivity.this.cbPraise.setChecked(false);
                    CheckBox checkBox = StoryDetailActivity.this.cbPraise;
                    StoryInfo access$000 = StoryDetailActivity.this.storyInfo;
                    int i = access$000.envious_count - 1;
                    access$000.envious_count = i;
                    checkBox.setText(String.valueOf(i));
                }
            }
        });
    }

    private void initPraiseAndComment() {
        StatusApi.getStoryInfo(this.mStoryId, this.ctx, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                StoryDetailActivity.this.storyInfo = (StoryInfo) FastJsonUtils.fromJson(object,
                        StoryInfo.class);
                if (StoryDetailActivity.this.storyInfo != null) {
                    StoryDetailActivity.this.tvComment.setText(String.valueOf(StoryDetailActivity
                            .this.storyInfo.comment_count));
                    StoryDetailActivity.this.cbPraise.setText(String.valueOf(StoryDetailActivity
                            .this.storyInfo.envious_count));
                    if (StoryDetailActivity.this.storyInfo.feedback) {
                        StoryDetailActivity.this.cbPraise.setChecked(true);
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void initCollect() {
        if (TextUtils.isEmpty(this.mUrl) || !this.mUrl.contains("favorite=false")) {
            FavoriteApi.checkFavoriteArticle(this.mUrl, initFavoriteModel(), new JsonCallback
                    (this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    StoryDetailActivity.this.refreshCollect(object);
                }

                public void onFinish() {
                    super.onFinish();
                }
            }, this.activity);
        }
    }

    private void refreshCollect(JSONObject object) {
        try {
            this.isFavorite = object.optBoolean("exist");
            this.favoriteId = object.optInt("id");
            if (this.isFavorite) {
                this.cbCollect.setChecked(true);
                this.cbCollect.setText("已收藏");
                return;
            }
            this.cbCollect.setText("收藏");
        } catch (Exception e) {
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

    private void addFavorite() {
        FavoriteApi.addFavoriteArticle(initFavoriteModel(), new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                StoryDetailActivity.this.addFavoriteSuccess(object);
            }
        }, this.activity);
    }

    private void addFavoriteSuccess(JSONObject object) {
        try {
            this.favoriteId = object.optInt("id");
            this.isFavorite = true;
            this.cbCollect.setChecked(true);
            this.cbCollect.setText("已收藏");
        } catch (Exception e) {
        }
    }

    private void deleteFavorite() {
        FavoriteApi.deleteFavoriteArticle(this.favoriteId, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                StoryDetailActivity.this.isFavorite = false;
                StoryDetailActivity.this.cbCollect.setChecked(false);
                StoryDetailActivity.this.cbCollect.setText("收藏");
            }
        }, this.activity);
    }

    private void initView() {
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setUserAgentString(this.webView.getSettings()
                .getUserAgentString() + " App/boohee");
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.addJavascriptInterface(new JSInterface(), "jsObj");
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Uri uri = Uri.parse(url);
                    if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains("https://")) {
                        view.loadUrl(url);
                    } else if (!(BooheeScheme.handleUrl(StoryDetailActivity.this, url) || uri ==
                            null)) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            StoryDetailActivity.this.startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                    StoryDetailActivity.this.originUrl = url;
                    StoryDetailActivity.this.mUrl = UrlUtils.handleUrl(StoryDetailActivity.this
                            .originUrl);
                }
                return true;
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.webView.loadUrl(this.mUrl);
    }

    public static void comeOn(Context context, String url, String id, String title) {
        if (context != null && !TextUtils.isEmpty(url) && !TextUtils.isEmpty(id)) {
            Intent intent = new Intent(context, StoryDetailActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }
    }
}
