package com.boohee.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boohee.account.NewUserInitActivity;
import com.boohee.apn.ApnActivity;
import com.boohee.food.FoodDetailActivity;
import com.boohee.main.FeedBackSwitcher;
import com.boohee.nice.NiceAdviserActivity;
import com.boohee.nice.NiceServiceActivity;
import com.boohee.one.BuildConfig;
import com.boohee.one.bet.BecomeTeamLeaderActivity;
import com.boohee.one.bet.BetActivity;
import com.boohee.one.bet.BetMainActivity;
import com.boohee.one.event.DuShouPayEvent;
import com.boohee.one.radar.RadarActivity;
import com.boohee.one.sport.SportCourseActivity;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.ChannelPostsActivity;
import com.boohee.one.ui.CheckPhoneActivity;
import com.boohee.one.ui.CouponActivity;
import com.boohee.one.ui.DietPlanActivity;
import com.boohee.one.ui.HomeTimelineActivity;
import com.boohee.one.ui.InviteFriendsActivity;
import com.boohee.one.ui.MainActivity;
import com.boohee.one.ui.PeriodActivity;
import com.boohee.one.ui.SelectStatusActivity;
import com.boohee.one.ui.ShopLabelActivity;
import com.boohee.one.ui.ShopMainActivity;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.ui.StoryDetailActivity;
import com.boohee.one.ui.SuccessStoryActivity;
import com.boohee.one.ui.WebViewPicUploadActivity;
import com.boohee.one.ui.WeightStatusActivity;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.one.video.fragment.SportPlanFragment;
import com.boohee.one.video.ui.NewSportPlanActivity;
import com.boohee.one.video.ui.SportSettingActivity;
import com.boohee.record.DietSportCalendarActivity;
import com.boohee.record.WeightRecordActivity;
import com.boohee.status.CommentListActivity;
import com.boohee.status.HotTopicActivity;
import com.boohee.status.SearchFriendsActivity;
import com.boohee.status.TopicActivity;
import com.boohee.status.UserTimelineActivity;
import com.boohee.uchoice.CartActivity;
import com.boohee.uchoice.GoodsDetailActivity;
import com.boohee.uchoice.OrderListActivity;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AppUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.MeiQiaHelper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.URLDecoderUtils;

import de.greenrobot.event.EventBus;

public class BooheeScheme {
    public static final String ALIPAY                  = "alipay";
    public static final String ANDROID_MARKET          = "android_market";
    public static final String BACK_TO_ONEKEY_HOME     = "back_home";
    public static final String BETS                    = "bets";
    public static final String BET_TEAMS               = "bet_teams";
    public static final String CHANNEL_POSTS           = "channel_posts";
    public static final String COMMENTS                = "comments";
    public static final String COUPON_LIST             = "coupon_list";
    public static final String DIET_PLAN               = "diet_plan";
    public static final String DIET_RECORD             = "diet_record";
    public static final String DU_SHOU_PIC_UPLOAD      = "weight_upload";
    public static final String FEEDBACK_ONLINE         = "feedback_online";
    public static final String FODD_DETAIL             = "food_detail";
    public static final String FRIEND_TIMELINE         = "friend_timeline";
    public static final String GOODS                   = "goods";
    public static final String GOODS_LIST              = "goods_list";
    public static final String HEALTH_REPORT           = "health_report";
    public static final String HOT_TIMELINE            = "hot_timeline";
    public static final String MC_RECORD               = "mc_record";
    public static final String MEAL_POSTS              = "meal_posts";
    public static final String MORE_TOPIC              = "more_topic";
    public static final String NICE_CHAT               = "nice_chat";
    public static final String NICE_SERVICE            = "nice_service";
    public static final String ORDER_LIST              = "order_list";
    public static final String PHONE_COMMIT            = "phone_commit";
    public static final String POST_STATUS             = "post_status";
    public static final String SCHEME                  = "boohee://";
    public static final String SEARCH_FRIEND           = "search_friend";
    public static final String SHARE_TO_FRIENDS        = "share_to_friends";
    public static final String SHOP                    = "shop";
    public static final String SHOP_CART               = "shop_cart";
    public static final String SPORT_COURSE            = "sport_course";
    public static final String SPORT_LESSON            = "sport_lesson";
    public static final String SPORT_LESSON_REFRESH    = "sport_lesson_refresh";
    public static final String SPORT_RECORD            = "sport_record";
    public static final String SPORT_SETTING           = "sport_setting";
    public static final String SPORT_SETTING_REFRESH   = "sport_setting_refresh";
    public static final String STORY_LIST              = "story_list";
    static final        String TAG                     = BooheeScheme.class.getSimpleName();
    public static final String TOPIC_POSTS             = "topic_posts";
    public static final String USER_PROFILE_INIT       = "user_profile_init";
    public static final String USER_PROFILE_TEST       = "user_test";
    public static final String USER_TIMELINE           = "user_timeline";
    public static final String WAGER_WEIGHT_PIC_UPLOAD = "wager_upload";
    public static final String WEEK_REPORT             = "week_report";
    public static final String WEIGHT_PROGRESS         = "user_progress";
    public static final String WEIGHT_RECORD           = "weight_record";

    public static boolean handleUrl(Context context, String url) {
        return handleUrl(context, url, null, false);
    }

    public static boolean handleUrl(Context context, String url, boolean isHandleBySelf) {
        return handleUrl(context, url, null, isHandleBySelf);
    }

    public static boolean handleUrl(Context context, String url, String webTitle) {
        return handleUrl(context, url, webTitle, false);
    }

    public static boolean handleUrl(Context context, String url, String webTitle, boolean
            isHandleBySelf) {
        return handleUrl(context, url, webTitle, null, isHandleBySelf);
    }

    public static boolean handleUrl(Context context, String url, String webTitle, Intent intent,
                                    boolean isHandleBySelf) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Helper.showLog(TAG, "booheeScheme : " + url);
        if (intent == null) {
            intent = new Intent();
        }
        if ((url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains("https://")) &&
                !isHandleBySelf) {
            if (url.contains("http://status.boohee.com/") && url.contains("story") && url
                    .contains("id=")) {
                intent.setClass(context, StoryDetailActivity.class);
                intent.putExtra("title", webTitle);
                intent.putExtra("url", url);
                intent.putExtra("id", TextUtil.checkId(url));
                context.startActivity(intent);
            } else {
                intent.setClass(context, BrowserActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", webTitle);
                context.startActivity(intent);
            }
            return true;
        } else if (!url.contains("boohee://")) {
            return false;
        } else {
            String[] parts = url.substring("boohee://".length()).split("/");
            if (parts.length <= 0) {
                return false;
            }
            String business = parts[0];
            String param = null;
            if (parts.length > 1) {
                param = parts[1];
                Helper.showLog(TAG, param);
            }
            param = URLDecoderUtils.replaceAndDecode(param);
            if (USER_TIMELINE.equalsIgnoreCase(business)) {
                intent.setClass(context, UserTimelineActivity.class);
                intent.putExtra("user_id", param);
                context.startActivity(intent);
            } else if (TOPIC_POSTS.equalsIgnoreCase(business)) {
                intent.setClass(context, TopicActivity.class);
                intent.putExtra(TopicActivity.EXTRA_TOPIC, param);
                context.startActivity(intent);
            } else if (COMMENTS.equalsIgnoreCase(business)) {
                try {
                    intent.setClass(context, CommentListActivity.class);
                    intent.putExtra(CommentListActivity.POST_ID, Integer.parseInt(param));
                    context.startActivity(intent);
                } catch (Exception e) {
                    return false;
                }
            } else if (GOODS.equalsIgnoreCase(business)) {
                try {
                    intent.setClass(context, GoodsDetailActivity.class);
                    intent.putExtra("goods_id", Integer.parseInt(param));
                    context.startActivity(intent);
                } catch (Exception e2) {
                    return false;
                }
            } else if (POST_STATUS.equalsIgnoreCase(business)) {
                intent.setClass(context, StatusPostTextActivity.class);
                intent.putExtra(StatusPostTextActivity.EXTRA_TEXT, param);
                context.startActivity(intent);
            } else if (DIET_RECORD.equalsIgnoreCase(business)) {
                intent.setClass(context, DietSportCalendarActivity.class);
                context.startActivity(intent);
            } else if (SPORT_RECORD.equalsIgnoreCase(business)) {
                intent.setClass(context, DietSportCalendarActivity.class);
                context.startActivity(intent);
            } else if ("weight_record".equalsIgnoreCase(business)) {
                intent.putExtra("code", "weight");
                intent.putExtra("name", "体重");
                intent.setClass(context, WeightRecordActivity.class);
                context.startActivity(intent);
            } else if (SEARCH_FRIEND.equalsIgnoreCase(business)) {
                intent.setClass(context, SearchFriendsActivity.class);
                context.startActivity(intent);
            } else if (USER_PROFILE_INIT.equalsIgnoreCase(business)) {
                intent.setClass(context, NewUserInitActivity.class);
                context.startActivity(intent);
            } else if (MC_RECORD.equalsIgnoreCase(business)) {
                intent.setClass(context, PeriodActivity.class);
                context.startActivity(intent);
            } else if (PHONE_COMMIT.equalsIgnoreCase(business)) {
                intent.setClass(context, CheckPhoneActivity.class);
                intent.putExtra(CheckPhoneActivity.KEY, 2);
                context.startActivity(intent);
            } else if (SHARE_TO_FRIENDS.equals(business)) {
                intent.setClass(context, InviteFriendsActivity.class);
                context.startActivity(intent);
            } else if (FODD_DETAIL.equalsIgnoreCase(business)) {
                intent.setClass(context, FoodDetailActivity.class);
                intent.putExtra("key_food_code", param);
                intent.putExtra(FoodDetailActivity.KEY_IS_RECORD, false);
                context.startActivity(intent);
            } else if (ANDROID_MARKET.equalsIgnoreCase(business)) {
                AppUtils.launchOrDownloadApp(context, param);
            } else if (MEAL_POSTS.equalsIgnoreCase(business)) {
                intent.setClass(context, SelectStatusActivity.class);
                intent.putExtra("meal_type", param);
                context.startActivity(intent);
            } else if (CHANNEL_POSTS.equalsIgnoreCase(business)) {
                intent.setClass(context, ChannelPostsActivity.class);
                intent.putExtra(ChannelPostsActivity.EXTRA_SLUG, param);
                context.startActivity(intent);
            } else if (DU_SHOU_PIC_UPLOAD.equalsIgnoreCase(business)) {
                intent.setClass(context, WebViewPicUploadActivity.class);
                intent.putExtra(WebViewPicUploadActivity.KEY_WEIGHT_PARAMS, param);
                context.startActivity(intent);
            } else if (BACK_TO_ONEKEY_HOME.equalsIgnoreCase(business)) {
                AccountUtils.goHome((Activity) context, MainActivity.class);
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
            } else if (GOODS_LIST.equalsIgnoreCase(business)) {
                try {
                    intent.setClass(context, ShopLabelActivity.class);
                    intent.putExtra("extra_label_id", Integer.parseInt(param));
                    context.startActivity(intent);
                } catch (Exception e3) {
                    return false;
                }
            } else if (SPORT_LESSON_REFRESH.equalsIgnoreCase(business)) {
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                EventBus.getDefault().post(NewSportPlanActivity.REFERSH);
                EventBus.getDefault().post(SportPlanFragment.REFRESH_SPORT_PLAN_FRAGMENT);
                intent.setClass(context, NewSportPlanActivity.class);
                intent.putExtra(NewSportPlanActivity.IS_FIRST, true);
                context.startActivity(intent);
                activity = (Activity) context;
                if (activity instanceof BrowserActivity) {
                    activity.finish();
                }
            } else if (SPORT_SETTING_REFRESH.equalsIgnoreCase(business)) {
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                EventBus.getDefault().post(NewSportPlanActivity.REFERSH);
                EventBus.getDefault().post(SportPlanFragment.REFRESH_SPORT_PLAN_FRAGMENT);
                intent.setClass(context, SportSettingActivity.class);
                context.startActivity(intent);
                activity = (Activity) context;
                if (activity instanceof BrowserActivity) {
                    activity.finish();
                }
            } else if ("sport_lesson".equalsIgnoreCase(business)) {
                intent.setClass(context, NewSportPlanActivity.class);
                context.startActivity(intent);
                activity = (Activity) context;
                if (activity instanceof BrowserActivity) {
                    activity.finish();
                }
            } else if (SPORT_SETTING.equalsIgnoreCase(business)) {
                intent.setClass(context, SportSettingActivity.class);
                context.startActivity(intent);
                activity = (Activity) context;
                if (activity instanceof BrowserActivity) {
                    activity.finish();
                }
            } else if (HEALTH_REPORT.equalsIgnoreCase(business)) {
                intent.setClass(context, NewUserInitActivity.class);
                context.startActivity(intent);
            } else if ("alipay".equalsIgnoreCase(business)) {
                if (context instanceof BrowserActivity) {
                    EventBus.getDefault().post(new DuShouPayEvent().setOrderId(Integer.parseInt
                            (param)));
                }
            } else if (STORY_LIST.equalsIgnoreCase(business)) {
                if (TextUtils.equals(param, BuildConfig.PLATFORM)) {
                    intent.setClass(context, SuccessStoryActivity.class);
                    context.startActivity(intent);
                } else {
                    intent.setClass(context, SuccessStoryActivity.class);
                    intent.putExtra(SuccessStoryActivity.TAGS, param);
                    context.startActivity(intent);
                }
            } else if (BETS.equalsIgnoreCase(business)) {
                if (TextUtils.equals(param, "home")) {
                    intent.setClass(context, BetMainActivity.class);
                    context.startActivity(intent);
                } else {
                    intent.setClass(context, BetActivity.class);
                    intent.putExtra(BetActivity.KEY_BET_ID, Integer.parseInt(param));
                    context.startActivity(intent);
                }
            } else if (BET_TEAMS.equalsIgnoreCase(business)) {
                BecomeTeamLeaderActivity.comeOnBaby(context);
            } else if (FRIEND_TIMELINE.equalsIgnoreCase(business)) {
                intent.setClass(context, HomeTimelineActivity.class);
                context.startActivity(intent);
            } else if (HOT_TIMELINE.equalsIgnoreCase(business)) {
                intent.setClass(context, ChannelPostsActivity.class);
                intent.putExtra(ChannelPostsActivity.EXTRA_SLUG, "hot_posts");
                context.startActivity(intent);
            } else if (USER_PROFILE_TEST.equalsIgnoreCase(business)) {
                intent.setClass(context, NewUserInitActivity.class);
                context.startActivity(intent);
                if (context instanceof BrowserActivity) {
                    ((BrowserActivity) context).finish();
                }
            } else if (ORDER_LIST.equalsIgnoreCase(business)) {
                intent.setClass(context, OrderListActivity.class);
                context.startActivity(intent);
            } else if (SHOP_CART.equalsIgnoreCase(business)) {
                intent.setClass(context, CartActivity.class);
                context.startActivity(intent);
            } else if (FEEDBACK_ONLINE.equalsIgnoreCase(business)) {
                if (FeedBackSwitcher.isFeedbackTime() && (context instanceof Activity)) {
                    MeiQiaHelper.startChat((Activity) context);
                } else {
                    ApnActivity.comeOnBaby(context, true);
                }
            } else if (SHOP.equalsIgnoreCase(business)) {
                intent.setClass(context, ShopMainActivity.class);
                context.startActivity(intent);
            } else if (SPORT_COURSE.equalsIgnoreCase(business)) {
                intent.setClass(context, SportCourseActivity.class);
                context.startActivity(intent);
            } else if (WEIGHT_PROGRESS.equalsIgnoreCase(business)) {
                intent.setClass(context, WeightStatusActivity.class);
                context.startActivity(intent);
            } else if (DIET_PLAN.equalsIgnoreCase(business)) {
                intent.setClass(context, DietPlanActivity.class);
                context.startActivity(intent);
            } else if (WEEK_REPORT.equalsIgnoreCase(business)) {
                intent.setClass(context, RadarActivity.class);
                context.startActivity(intent);
            } else if (MORE_TOPIC.equalsIgnoreCase(business)) {
                intent.setClass(context, HotTopicActivity.class);
                context.startActivity(intent);
            } else if (NICE_SERVICE.equalsIgnoreCase(business)) {
                intent.setClass(context, NiceServiceActivity.class);
                context.startActivity(intent);
            } else if (COUPON_LIST.equalsIgnoreCase(business)) {
                intent.setClass(context, CouponActivity.class);
                context.startActivity(intent);
            } else if (NICE_CHAT.equals(business)) {
                intent.setClass(context, NiceAdviserActivity.class);
                context.startActivity(intent);
            } else {
                Helper.showToast((CharSequence) "为了更好的使用该功能，请升级到最新版本");
            }
            return true;
        }
    }
}
