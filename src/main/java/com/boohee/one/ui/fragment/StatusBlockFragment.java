package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.StatusApi;
import com.boohee.database.OnePreference;
import com.boohee.model.status.Block;
import com.boohee.myview.GuidePopWindow;
import com.boohee.myview.GuidePopWindow.OnGuideClickListener;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.ChannelPostsActivity;
import com.boohee.one.ui.DiamondSignActivity;
import com.boohee.one.ui.HomeTimelineActivity;
import com.boohee.one.ui.MyFavoriteActivity;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import org.json.JSONObject;

public class StatusBlockFragment extends BaseFragment {
    @InjectView(2131428356)
    TextView     chatBadge;
    @InjectView(2131428355)
    LinearLayout chatLayout;
    @InjectView(2131428350)
    TextView     checkInBtn;
    @InjectView(2131428349)
    TextView     checkInMsgText;
    @InjectView(2131428164)
    LinearLayout collectLayout;
    @InjectView(2131427647)
    LinearLayout contentLayout;
    @InjectView(2131428352)
    TextView     friendsBadge;
    Handler handler = new Handler();
    @InjectView(2131428354)
    TextView hotBadge;
    public boolean isLoadFirst = true;
    private List<Block> mBlocks;
    private int         mCheckInCount;
    @InjectView(2131427340)
    PullToRefreshScrollView scrollView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gp, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.scrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                StatusBlockFragment.this.initUI();
            }
        });
    }

    public void loadFirst() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (StatusBlockFragment.this.isLoadFirst && StatusBlockFragment.this.scrollView
                        != null) {
                    StatusBlockFragment.this.scrollView.setRefreshing(true);
                    StatusBlockFragment.this.createGuidePopWindow();
                }
            }
        }, 500);
    }

    private void createGuidePopWindow() {
        if (getActivity() != null && !OnePreference.getInstance(getActivity()).isGuidePartner()) {
            GuidePopWindow guidePopWindow = new GuidePopWindow();
            guidePopWindow.init(getActivity(), R.drawable.n5);
            guidePopWindow.setOnGuideClickListener(new OnGuideClickListener() {
                public void onGuideClick() {
                    OnePreference.getInstance(StatusBlockFragment.this.getActivity())
                            .setGuidePartner(true);
                }
            });
            guidePopWindow.show();
        }
    }

    private void initUI() {
        StatusApi.getHomeBlocks(getActivity(), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object != null) {
                    StatusBlockFragment.this.mBlocks = FastJsonUtils.parseList(object.optString
                            ("blocks"), Block.class);
                    StatusBlockFragment.this.initBadge(object);
                    StatusBlockFragment.this.initBlocks();
                }
            }

            public void onFinish() {
                super.onFinish();
                StatusBlockFragment.this.isLoadFirst = false;
                StatusBlockFragment.this.scrollView.onRefreshComplete();
            }
        });
    }

    private void initCheckInData(JSONObject object) {
        JSONObject checkInObj = object.optJSONObject("check_in");
        if (checkInObj != null) {
            this.mCheckInCount = checkInObj.optInt("continue");
            setCheckInUI(this.mCheckInCount, checkInObj.optBoolean("checked"));
        }
    }

    private void setCheckInUI(int count, boolean isChecked) {
        boolean z;
        String message = String.format(getString(R.string.d7), new Object[]{Integer.valueOf
                (count)});
        SpannableString ss = new SpannableString(message);
        ss.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), 5, message.length() - 1, 33);
        this.checkInMsgText.setText(ss);
        this.checkInBtn.setText(isChecked ? R.string.d4 : R.string.d6);
        TextView textView = this.checkInBtn;
        if (isChecked) {
            z = false;
        } else {
            z = true;
        }
        textView.setEnabled(z);
    }

    private void initBadge(JSONObject object) {
        JSONObject badgeObject = object.optJSONObject("badge");
        if (badgeObject != null) {
            initCheckInData(badgeObject);
            this.friendsBadge.setText(badgeObject.optInt("friends") > 0 ? String.valueOf
                    (badgeObject.optInt("friends")) : "");
            this.hotBadge.setText(badgeObject.optInt("hot_posts") > 0 ? String.valueOf
                    (badgeObject.optInt("hot_posts")) : "");
        }
    }

    private void initBlocks() {
        if (this.mBlocks != null && this.mBlocks.size() > 0) {
            this.contentLayout.removeAllViews();
            for (final Block block : this.mBlocks) {
                View contentView = View.inflate(getActivity(), R.layout.j7, null);
                this.contentLayout.addView(contentView);
                LinearLayout showMoreLayout = (LinearLayout) contentView.findViewById(R.id
                        .ll_show_more);
                ImageView bannerImage = (ImageView) contentView.findViewById(R.id.iv_banner);
                ((TextView) contentView.findViewById(R.id.tv_name)).setText(block.getName());
                ViewUtils.setViewScaleHeight(getActivity(), bannerImage, 640, 320);
                this.imageLoader.displayImage(block.getImg(), bannerImage, ImageLoaderOptions
                        .randomColor());
                bannerImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        MobclickAgent.onEvent(StatusBlockFragment.this.getActivity(), Event
                                .status_clickBlock);
                        BooheeScheme.handleUrl(StatusBlockFragment.this.getActivity(), block
                                .link_to, block.name);
                    }
                });
                if (!TextUtils.isEmpty(block.show_more)) {
                    showMoreLayout.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MobclickAgent.onEvent(StatusBlockFragment.this.getActivity(), Event
                                    .status_clickMoreContent);
                            BooheeScheme.handleUrl(StatusBlockFragment.this.getActivity(), block
                                    .show_more, block.name);
                        }
                    });
                }
            }
        }
    }

    @OnClick({2131427590, 2131428350, 2131428351, 2131428353, 2131428355, 2131428164})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_check_in:
                MobclickAgent.onEvent(getActivity(), Event.status_viewCheckinPage);
                DiamondSignActivity.comeOnBaby(getActivity());
                return;
            case R.id.ll_collect:
                MobclickAgent.onEvent(getActivity(), Event.status_favoritePage);
                MyFavoriteActivity.comeOnBaby(getActivity());
                return;
            case R.id.btn_check_in:
                MobclickAgent.onEvent(getActivity(), Event.status_clickCheckin);
                StatusApi.checkIn(getActivity(), new JsonCallback(getActivity()) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        CharSequence message = object.optString("message");
                        if (!TextUtils.isEmpty(message)) {
                            Helper.showToast(message);
                        }
                        StatusBlockFragment.this.mCheckInCount = StatusBlockFragment.this
                                .mCheckInCount + 1;
                        StatusBlockFragment.this.setCheckInUI(StatusBlockFragment.this
                                .mCheckInCount, true);
                        OnePreference.setPrefSignRecord();
                    }
                });
                return;
            case R.id.ll_friends:
                MobclickAgent.onEvent(getActivity(), Event.status_friendTimeline);
                HomeTimelineActivity.comeOnBaby(getActivity());
                this.friendsBadge.setText("");
                return;
            case R.id.ll_hot:
                MobclickAgent.onEvent(getActivity(), Event.status_hotTimeline);
                ChannelPostsActivity.comeOnBaby(getActivity(), "hot_posts");
                this.hotBadge.setText("");
                return;
            default:
                return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (!isDetached() && getActivity() != null) {
        }
    }
}
