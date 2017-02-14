package com.boohee.api;

import android.content.Context;

import com.boohee.database.UserPreference;
import com.boohee.model.status.Notification;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.status.UserTimelineActivity;

import java.util.ArrayList;
import java.util.List;

public class StatusApi {
    public static final String BECOME_TEAM_LEADER         = "/api/v1/bet_teams";
    public static final String BET_WEIGHT                 =
            "/api/v1/bets/%1$d/order/%2$d/bet_weight";
    public static final String CHECK_IN                   = "/api/v1/checkin";
    public static final String CHECK_IN_RECORD            = "/api/v1/checkin/month";
    public static final String CHECK_IN_WEEK              = "/api/v1/checkin/week";
    public static final String CLEAR_STORY_DOT            =
            "/api/v1/app_square/%1$d/touch_dot_block";
    public static final String CLUBS                      = "/api/v1/clubs/%d";
    public static final String CLUB_MEMBERS               = "/api/v1/clubs/%d/club_members";
    public static final String GET_BET_WEIGHT             =
            "/api/v1/bets/%1$d/order/%2$d/bet_weights/weight_info";
    public static final String GET_HOME_BLOCKS            = "/api/v1/app_square/entry";
    public static final String GET_MAIN_SQUARE            = "/api/v1/app_square/main";
    public static final String GET_PARTNER_BLOCKS         = "/api/v1/app_square/entrance";
    public static final String GET_STORY_COMMENT          =
            "/api/v1/stories/%1$s/story_comments?page=%2$d";
    public static final String GET_STORY_INFO             = "/api/v1/stories/%1$s/info";
    public static final String MAIN_SQUARE_LIGHT          = "/api/v1/app_square/light";
    public static final String POST_STORY_COMMENT         = "/api/v1/stories/%1$s/story_comments";
    public static final String PRAISE_STORY               = "/api/v1/stories/%1$s/story_feedbacks";
    public static final String REPAIR                     = "/api/v1/checkin/repair";
    public static final String URL_BLOCKS                 = "/api/v1/blocks";
    public static final String URL_BROADCASTS             = "/api/v1/broadcasts";
    public static final String URL_BROADCASTS_CLEAR       = "/api/v1/broadcasts/clear";
    public static final String URL_BROADCASTS_DETAIL      = "/api/v1/broadcasts/%1$d?token=%2$s";
    public static final String URL_CHANNEL_POSTS          = "/api/v1/channels/posts";
    public static final String URL_CHANNEL_POSTS_PREVIOUS = "/api/v1/channels/posts";
    public static final String URL_CLEAR_NOTIFICATION     = "/api/v1/notifications/clear";
    public static final String URL_CLUB_POSTS             = "/api/v1/clubs/%1$d/club_posts";
    public static final String URL_CLUB_POSTS_PREVIOUS    = "/api/v1/clubs/%1$d/club_posts";
    public static final String URL_DELETE_POST            = "/api/v1/posts/%1$d";
    public static final String URL_FEEDBACKS              = "/api/v1/posts/%1$d/feedbacks";
    public static final String URL_FRIENDSHIPS            = "/api/v1/friendships";
    public static final String URL_GET_CLEAR_NEARBY       = "/api/v1/users/nearby";
    public static final String URL_GET_COMMENTS           = "/api/v1/posts/%1$d/comments";
    public static final String URL_GET_FOLLOWERS          = "/api/v1/followers";
    public static final String URL_GET_FOLLOWINGS         = "/api/v1/followings";
    public static final String URL_GET_FOLLOWINGS_SEARCH  = "/api/v1/followings";
    public static final String URL_GET_START_UP           = "/api/v1/app_square/start_up";
    public static final String URL_GET_USERS_PROFILE_FULL = "/api/v1/user_profile/full";
    public static final String URL_GET_USER_ALIKE         = "/api/v1/users/alike";
    public static final String URL_GET_USER_NEARBY        = "/api/v1/users/nearby";
    public static final String URL_GET_USER_RECOMMENDED   = "/api/v1/users/recommended";
    public static final String URL_GET_USER_SEARCH        = "/api/v1/users/search";
    public static final String URL_HOME_TIMELINE          = "/api/v1/home_timeline";
    public static final String URL_MILESTONES_FULL_MENU   = "/api/v1/milestones/full_menu";
    public static final String URL_MILESTONES_RESET       = "/api/v1/milestones/reset";
    public static final String URL_NOTIFICATIONS          = "/api/v1/notifications";
    public static final String URL_RECOMMENDED            = "/api/v1/recommended_posts";
    public static final String URL_RECOMMENDED_MEALS      = "/api/v1/food_square/recommended_meals";
    public static final String URL_RECOMMENDED_PREVIOUS   = "/api/v1/recommended_posts";
    public static final String URL_REPORT                 = "/api/v1/reports";
    public static final String URL_REPOST                 = "/api/v1/posts/%1$d/repost";
    public static final String URL_TOPIC                  = "/api/v1/topics/posts";
    public static final String URL_TOPICS_RECENT          = "/api/v1/topics/recent";
    public static final String URL_TOPIC_CHOICE           = "/api/v1/topics/choices";
    public static final String URL_TOPIC_PREVIOUS         = "/api/v1/topics/posts";
    public static final String URL_TOPIC_SEARCH           =
            "/api/v1/topics/search?title=%1$s&page=%2$d";
    public static final String URL_UNREAD_COUNT           = "/api/v1/notifications/unread_count";
    public static final String URL_USER_TIMELINE          = "/api/v1/user_timeline";
    public static final String USER_PROFILE_DETAIL        = "/api/v1/user_profile/detail";

    public static void getMainSquare(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(GET_MAIN_SQUARE, callback, context);
    }

    public static void getMainSquareLight(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(MAIN_SQUARE_LIGHT, callback, context);
    }

    public static void postStoryComment(String id, String comment, Context context, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        JsonParams body = new JsonParams();
        body.put("body", comment);
        jsonParams.put(Notification.STORY_COMMENT, body);
        BooheeClient.build("status").post(String.format(POST_STORY_COMMENT, new Object[]{id}),
                jsonParams, callback, context);
    }

    public static void getStoryComment(String id, int page, Context context, JsonCallback
            callback) {
        BooheeClient.build("status").get(String.format(GET_STORY_COMMENT, new Object[]{id,
                Integer.valueOf(page)}), null, callback, context);
    }

    public static void unPraiseStory(String id, Context context, JsonCallback callback) {
        BooheeClient.build("status").delete(String.format(PRAISE_STORY, new Object[]{id}), null,
                callback, context);
    }

    public static void praiseStory(String id, Context context, JsonCallback callback) {
        BooheeClient.build("status").put(String.format(PRAISE_STORY, new Object[]{id}), null,
                callback, context);
    }

    public static void getStoryInfo(String id, Context context, JsonCallback callback) {
        BooheeClient.build("status").get(String.format(GET_STORY_INFO, new Object[]{id}),
                callback, context);
    }

    public static void clearBlockDot(int id, Context context, JsonCallback callback) {
        BooheeClient.build("status").get(String.format(CLEAR_STORY_DOT, new Object[]{Integer
                .valueOf(id)}), callback, context);
    }

    @Deprecated
    public static void getUsersProfileFull(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_GET_USERS_PROFILE_FULL, callback, context);
    }

    public static void getStartUpUrl(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_GET_START_UP, callback, context);
    }

    public static void getRecommended(Context context, JsonCallback callback) {
        BooheeClient.build("status").get("/api/v1/recommended_posts", callback, context);
    }

    public static void getPreviousRecommended(Context context, int previous_id, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("previous_id", previous_id);
        BooheeClient.build("status").get("/api/v1/recommended_posts", jsonParams, callback,
                context);
    }

    public static void getChannelPosts(Context context, String slug, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("slug", slug);
        BooheeClient.build("status").get("/api/v1/channels/posts", jsonParams, callback, context);
    }

    public static void getChannelPostsPrevious(Context context, int previous_id, String slug,
                                               JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("previous_id", previous_id);
        jsonParams.put("slug", slug);
        BooheeClient.build("status").get("/api/v1/channels/posts", jsonParams, callback, context);
    }

    public static void getTopicsRecent(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_TOPICS_RECENT, callback, context);
    }

    public static void getMoreCategory(String more_url, int page, Context context, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("page", page);
        BooheeClient.build("status").get(more_url, jsonParams, callback, context);
    }

    public static void deleteMileStonesReset(Context context, JsonCallback callback) {
        BooheeClient.build("status").delete(URL_MILESTONES_RESET, new JsonParams(), callback,
                context);
    }

    public static void getMileStonesFullMenu(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_MILESTONES_FULL_MENU, callback, context);
    }

    public static void postReport(Context context, int id, String type, String category,
                                  JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("id", id);
        jsonParams.put("type", type);
        jsonParams.put("category", category);
        BooheeClient.build("status").post(URL_REPORT, jsonParams, callback, context);
    }

    public static void getRecommendedMeals(Context context, String meal_type, int page,
                                           JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("meal_type", meal_type);
        jsonParams.put("page", page);
        BooheeClient.build("status").get(URL_RECOMMENDED_MEALS, jsonParams, callback, context);
    }

    public static void sendPost(Context context, String url, JsonParams jsonParams, JsonCallback
            callback) {
        BooheeClient.build("status").post(url, jsonParams, callback, context);
    }

    public static void getClubPosts(Context context, int club_id, String type, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("type", type);
        BooheeClient.build("status").get(String.format("/api/v1/clubs/%1$d/club_posts", new
                Object[]{Integer.valueOf(club_id)}), jsonParams, callback, context);
    }

    public static void getClubPostsPrevious(Context context, int club_id, String type, int
            previous_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("type", type);
        jsonParams.put("previous_id", previous_id);
        BooheeClient.build("status").get(String.format("/api/v1/clubs/%1$d/club_posts", new
                Object[]{Integer.valueOf(club_id)}), jsonParams, callback, context);
    }

    public static void getTopicPosts(Context context, String title, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("title", title);
        BooheeClient.build("status").get("/api/v1/topics/posts", jsonParams, callback, context);
    }

    public static void getTopicPostsPrevious(Context context, String title, int previous_id,
                                             JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("title", title);
        jsonParams.put("previous_id", previous_id);
        BooheeClient.build("status").get("/api/v1/topics/posts", jsonParams, callback, context);
    }

    public static void getHomeBlocks(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(GET_HOME_BLOCKS, callback, context);
    }

    public static void getPartnerBlocks(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(GET_PARTNER_BLOCKS, callback, context);
    }

    public static void getCheckIn(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(CHECK_IN, null, callback, context);
    }

    public static void checkIn(Context context, JsonCallback callback) {
        BooheeClient.build("status").post(CHECK_IN, null, callback, context);
    }

    public static void getCheckInWeek(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(CHECK_IN_WEEK, callback, context);
    }

    public static void repair(Context context, String date, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("date", date);
        BooheeClient.build("status").post(REPAIR, params, callback, context);
    }

    public static void getCheckRecord(String year_month, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("year_month", year_month);
        BooheeClient.build("status").get(CHECK_IN_RECORD, params, callback, context);
    }

    public static void getCheckNumber(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(CHECK_IN, callback, context);
    }

    public static void getBetWeight(Context context, int bet_id, int order_id, JsonCallback
            callback) {
        BooheeClient.build("status").get(String.format(GET_BET_WEIGHT, new Object[]{Integer
                .valueOf(bet_id), Integer.valueOf(order_id)}), callback, context);
    }

    public static void postBetWeight(Context context, int bet_id, int order_id, JsonParams
            jsonParams, JsonCallback callback) {
        BooheeClient.build("status").post(String.format(BET_WEIGHT, new Object[]{Integer.valueOf
                (bet_id), Integer.valueOf(order_id)}), jsonParams, callback, context);
    }

    public static void putBetWeight(Context context, int bet_id, int order_id, JsonParams
            jsonParams, JsonCallback callback) {
        BooheeClient.build("status").put(String.format(BET_WEIGHT, new Object[]{Integer.valueOf
                (bet_id), Integer.valueOf(order_id)}), jsonParams, callback, context);
    }

    public static void becomeTeamLeader(Context context, JsonParams jsonParams, JsonCallback
            callback) {
        BooheeClient.build("status").post(BECOME_TEAM_LEADER, jsonParams, callback, context);
    }

    public static void searchTopic(String title, int page, JsonCallback callback, Context context) {
        BooheeClient.build("status").get(String.format(URL_TOPIC_SEARCH, new Object[]{title,
                Integer.valueOf(page)}), callback, context);
    }

    public static void getHomeTimeline(Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_HOME_TIMELINE, jsonParams, callback, context);
    }

    public static void getPreviousHomeTimeline(Context context, int previous_id, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("previous_id", previous_id);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_HOME_TIMELINE, jsonParams, callback, context);
    }

    public static void getMyTimeline(Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void getMyPreviousTimeline(Context context, int previous_id, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("previous_id", previous_id);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void getUserTimelineByNickName(Context context, String nickName, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put(UserTimelineActivity.NICK_NAME, nickName);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void getUserPreviousTimelineByNickName(Context context, String nickName, int
            previous_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put(UserTimelineActivity.NICK_NAME, nickName);
        jsonParams.put("previous_id", previous_id);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void getUserTimelineById(Context context, String user_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_id", user_id);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void getUserPreviousTimelineById(Context context, String user_id, int
            previous_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_id", user_id);
        jsonParams.put("previous_id", previous_id);
        jsonParams.put("status_api_version", "1.1");
        BooheeClient.build("status").get(URL_USER_TIMELINE, jsonParams, callback, context);
    }

    public static void createFriendships(Context context, int user_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_id", user_id);
        BooheeClient.build("status").post(URL_FRIENDSHIPS, jsonParams, callback, context);
    }

    public static void createFriendshipList(Context context, ArrayList<String> idList,
                                            JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_ids", (List) idList);
        BooheeClient.build("status").post(URL_FRIENDSHIPS, jsonParams, callback, context);
    }

    public static void deleteFriendships(Context context, int user_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_key", UserPreference.getUserKey(context));
        jsonParams.put("user_id", user_id);
        BooheeClient.build("status").delete(URL_FRIENDSHIPS, jsonParams, callback, context);
    }

    public static void deleteFriendships(Context context, String nick_name, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("nick_name", nick_name);
        BooheeClient.build("status").delete(URL_FRIENDSHIPS, jsonParams, callback, context);
    }

    public static void createBlocks(Context context, int user_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("user_id", user_id);
        BooheeClient.build("status").post(URL_BLOCKS, jsonParams, callback, context);
    }

    public static void getUserSearch(Context context, String q, int page, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("q", q);
        jsonParams.put("page", page);
        BooheeClient.build("status").get(URL_GET_USER_SEARCH, jsonParams, callback, context);
    }

    public static void getUserNearNby(Context context, String country, String province, String
            city, String district, String lng, String lat, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("country", country);
        jsonParams.put("province", province);
        jsonParams.put("city", city);
        jsonParams.put("district", district);
        jsonParams.put("lng", lng);
        jsonParams.put("lat", lat);
        BooheeClient.build("status").get("/api/v1/users/nearby", jsonParams, callback, context);
    }

    public static void getClearNearNby(Context context, String province, String city, String
            district, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("province", province);
        jsonParams.put("city", city);
        jsonParams.put("district", district);
        BooheeClient.build("status").get(String.format("/api/v1/users/nearby", new
                Object[]{UserPreference.getToken(context), province, city, district}), callback,
                context);
    }

    public static void getUserAlike(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_GET_USER_ALIKE, callback, context);
    }

    public static void getUserRecommended(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_GET_USER_RECOMMENDED, callback, context);
    }

    public static void getFollowings(Context context, int page, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("page", page);
        BooheeClient.build("status").get("/api/v1/followings", jsonParams, callback, context);
    }

    public static void getFollowingsSearch(Context context, String q, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("q", q);
        BooheeClient.build("status").get("/api/v1/followings", jsonParams, callback, context);
    }

    public static void getFollowers(Context context, int page, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("page", page);
        BooheeClient.build("status").get(URL_GET_FOLLOWERS, jsonParams, callback, context);
    }

    public static void deletePost(Context context, int post_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        BooheeClient.build("status").delete(String.format(URL_DELETE_POST, new Object[]{Integer
                .valueOf(post_id)}), jsonParams, callback, context);
    }

    public static void repostPost(Context context, int post_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        BooheeClient.build("status").post(String.format(URL_REPOST, new Object[]{Integer.valueOf
                (post_id)}), jsonParams, callback, context);
    }

    public static void deleteFeedbacks(Context context, int post_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        BooheeClient.build("status").delete(String.format(URL_FEEDBACKS, new Object[]{Integer
                .valueOf(post_id)}), jsonParams, callback, context);
    }

    public static void putFeedbacks(Context context, int post_id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("type", "envious");
        BooheeClient.build("status").put(String.format(URL_FEEDBACKS, new Object[]{Integer
                .valueOf(post_id)}), jsonParams, callback, context);
    }

    public static void getComments(Context context, int post_id, int page, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("page", page);
        BooheeClient.build("status").get(String.format(URL_GET_COMMENTS, new Object[]{Integer
                .valueOf(post_id)}), jsonParams, callback, context);
    }

    public static void sendComments(Context context, int post_id, String content, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        JsonParams body = new JsonParams();
        body.put("body", content);
        jsonParams.put("comment", body);
        BooheeClient.build("status").post(String.format(URL_GET_COMMENTS, new Object[]{Integer
                .valueOf(post_id)}), jsonParams, callback, context);
    }

    public static void clearNotification(Context context, JsonCallback callback) {
        BooheeClient.build("status").delete(URL_CLEAR_NOTIFICATION, new JsonParams(), callback,
                context);
    }

    public static void getUnread(Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("status_api_version", "1.3");
        BooheeClient.build("status").get(URL_UNREAD_COUNT, jsonParams, callback, context);
    }

    public static void getBroadcasts(Context context, JsonCallback callback) {
        BooheeClient.build("status").get(URL_BROADCASTS, callback, context);
    }

    public static void clearBroadcasts(Context context, JsonCallback callback) {
        BooheeClient.build("status").delete(URL_BROADCASTS_CLEAR, null, callback, context);
    }

    public static void getNotifications(Context context, JsonParams jsonParams, JsonCallback callback) {
        BooheeClient.build("status").get(URL_NOTIFICATIONS, jsonParams, callback, context);
    }

    public static void getNotificationsPrevious(Context context, JsonParams jsonParams, JsonCallback callback) {
        BooheeClient.build("status").get(URL_NOTIFICATIONS, jsonParams, callback, context);
    }
}
