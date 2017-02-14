package com.boohee.one.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.NewUserInitActivity;
import com.boohee.account.UserProfileActivity;
import com.boohee.api.ApiUrls;
import com.boohee.apn.ApnActivity;
import com.boohee.food.MyFoodActivity;
import com.boohee.model.User;
import com.boohee.model.status.StatusUser;
import com.boohee.nice.NiceServiceActivity;
import com.boohee.one.R;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.radar.RadarActivity;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.HardwareActivity;
import com.boohee.one.ui.JumpBrowserActivity;
import com.boohee.one.ui.MainActivity;
import com.boohee.one.ui.MyFavoriteActivity;
import com.boohee.one.ui.UserSettingActivity;
import com.boohee.status.FriendShipActivity;
import com.boohee.status.MyTimelineActivity;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AccountUtils.OnGetUserProfile;
import com.boohee.utils.WheelUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeMineFragment extends BaseFragment {
    @InjectView(2131427499)
    CircleImageView ivAvatar;
    @InjectView(2131428902)
    ImageView       ivWeekStatus;
    private User mUser;
    @InjectView(2131428899)
    TextView tvBadgeCount;
    @InjectView(2131428897)
    TextView tvFollowerCount;
    @InjectView(2131428895)
    TextView tvFollowingCount;
    @InjectView(2131428383)
    TextView tvPostCount;

    @OnClick({2131428892, 2131428893, 2131428896, 2131428894, 2131428900, 2131428904, 2131428905,
            2131428907, 2131428909, 2131428898, 2131428906, 2131428903, 2131428908, 2131428901})
    public void onClick(View v) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick()) {
            switch (v.getId()) {
                case R.id.view_profile:
                    UserProfileActivity.comeOn(getActivity());
                    return;
                case R.id.view_post:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickStatus);
                    MyTimelineActivity.comeOnBaby(getActivity());
                    return;
                case R.id.view_following:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickFriends);
                    startActivity(new Intent(getActivity(), FriendShipActivity.class).putExtra
                            (FriendShipActivity.FRIENDSHIP_POSITION, 0));
                    return;
                case R.id.view_follower:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickFollowers);
                    startActivity(new Intent(getActivity(), FriendShipActivity.class).putExtra
                            (FriendShipActivity.FRIENDSHIP_POSITION, 1));
                    return;
                case R.id.view_badge:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickBadge);
                    BrowserActivity.comeOnBaby(getActivity(), getString(R.string.tb),
                            BooheeClient.build("status").getDefaultURL(ApiUrls.BADGE_URL));
                    return;
                case R.id.view_health_report:
                    if (this.mUser == null || !this.mUser.hasProfile()) {
                        startActivity(new Intent(getActivity(), NewUserInitActivity.class));
                        return;
                    }
                    MobclickAgent.onEvent(getActivity(), Event.MINE_CLICKHEALTHREPORT);
                    JumpBrowserActivity.comeOnBaby(getActivity(), getString(R.string.t_),
                            BooheeClient.build(BooheeClient.BINGO).getDefaultURL(ApiUrls
                                    .REPORT_URL));
                    return;
                case R.id.view_report:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickWeekReport);
                    RadarActivity.startActivity(getActivity(), this.mUser
                            .week_report_user_status, this.mUser.week_report_user_type);
                    this.mUser.week_report_user_status = User.WEEK_REPORT_STATUS_TOBE_UPDATE;
                    refreshView();
                    return;
                case R.id.view_nice:
                    MobclickAgent.onEvent(getActivity(), Event.MINE_CLICKNICE);
                    NiceServiceActivity.startActivity(getActivity());
                    return;
                case R.id.view_favorite:
                    MobclickAgent.onEvent(getActivity(), Event.MINE_FAVORITE);
                    MyFavoriteActivity.comeOnBaby(getActivity());
                    return;
                case R.id.view_food:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickFood);
                    MyFoodActivity.comeOn(getActivity());
                    return;
                case R.id.view_hardware:
                    HardwareActivity.startActivity(getActivity());
                    return;
                case R.id.view_diamond:
                    MobclickAgent.onEvent(getActivity(), Event.MINE_REDEEM);
                    BrowserActivity.comeOnBaby(getActivity(), getString(R.string.t3),
                            BooheeClient.build("status").getDefaultURL(ApiUrls.DIAMOND_CHECHIN));
                    return;
                case R.id.view_question:
                    ApnActivity.comeOnBaby(getActivity(), false);
                    return;
                case R.id.view_setting:
                    MobclickAgent.onEvent(getActivity(), Event.mine_clickSetting);
                    UserSettingActivity.comeOnBaby(getActivity());
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUser = AccountUtils.getUserProfileLocal(getActivity());
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_mine, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        refreshView();
        requestUser();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onEventMainThread(UserIntEvent event) {
        requestUser();
    }

    private void requestUser() {
        AccountUtils.getUserProfile(getActivity(), new OnGetUserProfile() {
            public void onGetUserProfile(User user) {
                if (user != null) {
                    HomeMineFragment.this.mUser = user;
                    HomeMineFragment.this.refreshView();
                }
            }

            public void onGetUserProfileFinish() {
            }
        });
    }

    private void refreshView() {
        if (this.mUser != null) {
            ImageLoader.getInstance().displayImage(this.mUser.avatar_url, this.ivAvatar,
                    ImageLoaderOptions.avatar());
            ((MainActivity) getActivity()).getToolbarTitle().setText(this.mUser.user_name);
            ((MainActivity) getActivity()).userName = this.mUser.user_name;
            this.tvPostCount.setText(StatusUser.displayCount(this.mUser.post_count));
            this.tvFollowingCount.setText(StatusUser.displayCount(this.mUser.following_count));
            this.tvFollowerCount.setText(StatusUser.displayCount(this.mUser.follower_count));
            this.tvBadgeCount.setText(String.valueOf(this.mUser.badges_count));
            if (TextUtils.equals(this.mUser.week_report_user_status, "new")) {
                this.ivWeekStatus.setImageResource(R.drawable.aac);
                this.ivWeekStatus.setVisibility(0);
            } else if (TextUtils.equals(this.mUser.week_report_user_status, User
                    .WEEK_REPORT_STATUS_UPDATED)) {
                this.ivWeekStatus.setImageResource(R.drawable.aac);
                this.ivWeekStatus.setVisibility(0);
            } else {
                this.ivWeekStatus.setVisibility(8);
            }
        }
    }
}
