package com.boohee.modeldao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.User.Builder;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;

import java.util.Date;

public class UserDao extends ModelDaoBase {
    public static final  String AVATAR           = "avatar";
    public static final  String BADGES_COUNT     = "badges_count";
    public static final  String BEGIN_DATE       = "begin_date";
    public static final  String BEGIN_WEIGHT     = "begin_weight";
    public static final  String BIRTHDAY         = "birthday";
    public static final  String CELLPHONE        = "cellphone";
    public static final  String DESCRIPTION      = "description";
    public static final  String DISEASES         = "diseases";
    public static final  String ENVIOUS_COUNT    = "envious_count";
    public static final  String FOLLOWER_COUNT   = "follower_count";
    public static final  String FOLLOWING_COUNT  = "following_count";
    public static final  String HEIGHT           = "height";
    public static final  String LATEST_WAIST     = "latest_waist";
    public static final  String LATEST_WAIST_AT  = "latest_waist_at";
    public static final  String LATEST_WEIGHT    = "latest_weight";
    public static final  String LATEST_WEIGHT_AT = "latest_weight_at";
    public static final  String NEED_TEST        = "need_test";
    public static final  String POST_COUNT       = "post_count";
    public static final  String SEX_TYPE         = "sex_type";
    public static final  String SPORT_CONDITION  = "sport_condition";
    private static final String TABLE_NAME       = "users";
    static final         String TAG              = UserDao.class.getName();
    public static final  String TARGET_CALORY    = "target_calory";
    public static final  String TARGET_DATE      = "target_date";
    public static final  String TARGET_WEIGHT    = "target_weight";
    public static final  String TOKEN            = "token";
    public static final  String UPDATE_AT        = "updated_at";
    public static final  String USER_KEY         = "user_key";
    public static final  String USER_NAME        = "user_name";

    public UserDao(Context ctx) {
        super(ctx);
    }

    public boolean add(User user) {
        if (TextUtils.isEmpty(user.user_key)) {
            user.user_key = UserPreference.getUserKey(this.ctx);
        }
        if (TextUtils.isEmpty(user.token)) {
            user.token = UserPreference.getToken(this.ctx);
        }
        User localUser = queryWithUserKey(user.user_key);
        ContentValues cv = getValidUserData(user, localUser);
        if (localUser == null) {
            cv.put("updated_at", DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            Helper.showLog("", "insert");
            if (this.db.insert(TABLE_NAME, null, cv) > -1) {
                return true;
            }
            return false;
        }
        if (this.db.update(TABLE_NAME, cv, "user_key = ?", new String[]{localUser.user_key}) <=
                -1) {
            return false;
        }
        return true;
    }

    public boolean updateWeight(String token, float weight) {
        ContentValues cv = new ContentValues();
        cv.put(BEGIN_WEIGHT, Float.valueOf(weight));
        if (this.db.update(TABLE_NAME, cv, "token = ?", new String[]{token}) > -1) {
            return true;
        }
        return false;
    }

    private ContentValues getValidUserData(User user, User localUser) {
        int i;
        if (localUser == null) {
            localUser = new User();
        }
        ContentValues cv = new ContentValues();
        cv.put("token", user.token);
        cv.put("user_key", user.user_key);
        if (!(TextUtils.isEmpty(user.user_name) || TextUtils.equals(user.user_name, localUser
                .user_name))) {
            cv.put(USER_NAME, user.user_name);
        }
        if (!(TextUtils.isEmpty(user.sex_type) || TextUtils.equals(user.sex_type, localUser
                .sex_type))) {
            cv.put("sex_type", user.sex_type);
        }
        if (!(TextUtils.isEmpty(user.birthday) || TextUtils.equals(user.birthday, localUser
                .sex_type))) {
            cv.put("birthday", user.birthday);
        }
        if (user.height > 0.0f && user.height != localUser.height) {
            cv.put("height", Float.valueOf(user.height));
        }
        if (user.begin_weight > 0.0f && user.begin_weight != localUser.begin_weight) {
            cv.put(BEGIN_WEIGHT, Float.valueOf(user.begin_weight));
        }
        if ((user.target_weight > 0.0f || user.target_weight == -1.0f) && user.target_weight !=
                localUser.target_weight) {
            cv.put("target_weight", Float.valueOf(user.target_weight));
        }
        if (!(TextUtils.isEmpty(user.target_date) || TextUtils.equals(user.target_date, localUser
                .target_date))) {
            cv.put("target_date", user.target_date);
        }
        if (user.target_calory > 0 && user.target_calory != localUser.target_calory) {
            cv.put("target_calory", Integer.valueOf(user.target_calory));
        }
        if (!(TextUtils.isEmpty(user.avatar_url) || TextUtils.equals(user.avatar_url, localUser
                .avatar_url))) {
            cv.put(AVATAR, user.avatar_url);
        }
        if (!(TextUtils.isEmpty(user.cellphone) || TextUtils.equals(user.cellphone, localUser
                .cellphone))) {
            cv.put("cellphone", user.cellphone);
        }
        if (!(TextUtils.isEmpty(user.description) || TextUtils.equals(user.description, localUser
                .description))) {
            cv.put("description", user.description);
        }
        if (user.latest_weight > 0.0f && user.latest_weight != localUser.latest_weight) {
            cv.put("latest_weight", Float.valueOf(user.latest_weight));
        }
        if (!(TextUtils.isEmpty(user.latest_weight_at) || TextUtils.equals(user.latest_weight_at,
                localUser.latest_weight_at))) {
            cv.put(LATEST_WEIGHT_AT, user.latest_weight_at);
        }
        if (user.latest_waist > 0.0f && user.latest_waist != localUser.latest_waist) {
            cv.put(LATEST_WAIST, Float.valueOf(user.latest_waist));
        }
        if (!(TextUtils.isEmpty(user.latest_waist_at) || TextUtils.equals(user.latest_waist_at,
                localUser.latest_waist_at))) {
            cv.put(LATEST_WAIST_AT, user.latest_waist_at);
        }
        if (user.post_count > 0 && user.post_count != localUser.post_count) {
            cv.put(POST_COUNT, Integer.valueOf(user.post_count));
        }
        if (user.envious_count > 0 && user.envious_count != localUser.envious_count) {
            cv.put(ENVIOUS_COUNT, Integer.valueOf(user.envious_count));
        }
        if (user.following_count > 0 && user.following_count != localUser.following_count) {
            cv.put(FOLLOWING_COUNT, Integer.valueOf(user.following_count));
        }
        if (user.follower_count > 0 && user.follower_count != localUser.follower_count) {
            cv.put(FOLLOWER_COUNT, Integer.valueOf(user.follower_count));
        }
        if (user.badges_count > 0 && user.badges_count != localUser.badges_count) {
            cv.put(BADGES_COUNT, Integer.valueOf(user.badges_count));
        }
        if (!(TextUtils.isEmpty(user.updated_at) || TextUtils.equals(user.updated_at, localUser
                .updated_at))) {
            cv.put("updated_at", DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (!(TextUtils.isEmpty(user.sport_condition) || TextUtils.equals(user.sport_condition,
                localUser.sport_condition))) {
            cv.put(SPORT_CONDITION, user.sport_condition);
        }
        if (!(TextUtils.isEmpty(user.begin_date) || TextUtils.equals(user.begin_date, localUser
                .begin_date))) {
            cv.put("begin_date", user.begin_date);
        }
        String userDiseases = "";
        String localUserDiseases = "";
        if (user.diseases != null) {
            for (i = 0; i < user.diseases.size(); i++) {
                userDiseases = userDiseases + user.diseases.get(i);
                if (i != user.diseases.size() - 1) {
                    userDiseases = userDiseases + ",";
                }
            }
        }
        if (localUser.diseases != null) {
            for (i = 0; i < localUser.diseases.size(); i++) {
                localUserDiseases = localUserDiseases + localUser.diseases.get(i);
                if (i != localUser.diseases.size() - 1) {
                    localUserDiseases = localUserDiseases + ",";
                }
            }
        }
        if (!(TextUtils.isEmpty(userDiseases) || TextUtils.equals(userDiseases,
                localUserDiseases))) {
            cv.put(DISEASES, userDiseases);
        }
        if (user.need_test != localUser.need_test) {
            cv.put(NEED_TEST, Integer.valueOf(user.need_test ? 1 : 0));
        }
        return cv;
    }

    public User queryWithToken(String token) {
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        Cursor c = this.db.rawQuery("select * from users where token = ?", new String[]{token});
        if (c == null || !c.moveToFirst()) {
            return null;
        }
        User user = selectWithCursor(c);
        c.close();
        return user;
    }

    public User queryWithUserKey(String user_key) {
        if (TextUtils.isEmpty(user_key)) {
            return null;
        }
        Cursor c = this.db.rawQuery("select * from users where user_key = ?", new
                String[]{user_key});
        if (c == null || !c.moveToFirst()) {
            return null;
        }
        User user = selectWithCursor(c);
        c.close();
        return user;
    }

    public User selectWithCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        String token = cursor.getString(cursor.getColumnIndex("token"));
        String user_key = cursor.getString(cursor.getColumnIndex("user_key"));
        String cellphone = cursor.getString(cursor.getColumnIndex("cellphone"));
        String user_name = cursor.getString(cursor.getColumnIndex(USER_NAME));
        String sex_type = cursor.getString(cursor.getColumnIndex("sex_type"));
        String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
        float height = cursor.getFloat(cursor.getColumnIndex("height"));
        float begin_weight = cursor.getFloat(cursor.getColumnIndex(BEGIN_WEIGHT));
        float target_weight = cursor.getFloat(cursor.getColumnIndex("target_weight"));
        String target_date = cursor.getString(cursor.getColumnIndex("target_date"));
        int target_calory = cursor.getInt(cursor.getColumnIndex("target_calory"));
        String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
        String avatar = cursor.getString(cursor.getColumnIndex(AVATAR));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        float latest_weight = cursor.getFloat(cursor.getColumnIndex("latest_weight"));
        String latest_weight_at = cursor.getString(cursor.getColumnIndex(LATEST_WEIGHT_AT));
        float latest_waist = cursor.getFloat(cursor.getColumnIndex(LATEST_WAIST));
        String latest_waist_at = cursor.getString(cursor.getColumnIndex(LATEST_WAIST_AT));
        int post_count = cursor.getInt(cursor.getColumnIndex(POST_COUNT));
        int envious_count = cursor.getInt(cursor.getColumnIndex(ENVIOUS_COUNT));
        int following_count = cursor.getInt(cursor.getColumnIndex(FOLLOWING_COUNT));
        int follower_count = cursor.getInt(cursor.getColumnIndex(FOLLOWER_COUNT));
        int badges_count = cursor.getInt(cursor.getColumnIndex(BADGES_COUNT));
        String sport_condition = cursor.getString(cursor.getColumnIndex(SPORT_CONDITION));
        String diseaes = cursor.getString(cursor.getColumnIndex(DISEASES));
        int need_test = cursor.getInt(cursor.getColumnIndex(NEED_TEST));
        String begin_date = cursor.getString(cursor.getColumnIndex("begin_date"));
        cursor.close();
        return new Builder().setToken(token).setUserKey(user_key).setCellPhone(cellphone)
                .setUserName(user_name).setSexType(sex_type).setBirthDay(birthday).setHeight
                        (height).setBeginWeight(begin_weight).setTargetWeight(target_weight)
                .setTargetDate(target_date).setTargetCalory(target_calory).setUpdateAt
                        (updated_at).setAvatarUrl(avatar).setDescription(description)
                .setLatestWeight(latest_weight).setLatestWeightAt(latest_weight_at)
                .setLatestWaist(Float.valueOf(latest_waist)).setLatestWaistAt(latest_waist_at)
                .setPostCount(post_count).setEnviousCount(envious_count).setFollowingCount(following_count).setFollowerCount(follower_count).setBadgesCount(badges_count).setSportCondition(sport_condition).setDiseases(diseaes).setNeedTest(need_test).setBeginDate(begin_date).create();
    }
}
