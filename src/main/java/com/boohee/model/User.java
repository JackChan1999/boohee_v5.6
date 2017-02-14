package com.boohee.model;

import android.text.TextUtils;

import com.boohee.myview.IntFloatWheelView;
import com.boohee.utility.Const;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class User extends ModelBase implements Serializable {
    public static final  String TAG                            = User.class.getSimpleName();
    public static final  String WEEK_REPORT_STATUS_NEW         = "new";
    public static final  String WEEK_REPORT_STATUS_TOBE_UPDATE = "tobeupdate";
    public static final  String WEEK_REPORT_STATUS_UPDATED     = "updated";
    public static final  String WEEK_RESPORT_USER_NEW          = "newbie";
    public static final  String WEEK_RESPORT_USER_REGULAR      = "regular";
    private static final long   serialVersionUID               = -7094516845285508447L;
    public String        avatar_url;
    public int           badges_count;
    public String        begin_date;
    public float         begin_weight;
    public String        birthday;
    public String        cellphone;
    public boolean       cellphone_state;
    public String        description;
    public List<Integer> diseases;
    public String        email;
    public int           envious_count;
    public int           follower_count;
    public int           following_count;
    public float         height;
    public float         latest_waist;
    public String        latest_waist_at;
    public float         latest_weight;
    public String        latest_weight_at;
    public boolean       need_test;
    public String        nick_name;
    public int           post_count;
    public String        sex_type;
    public String        sport_condition;
    public int           target_calory;
    public String        target_date;
    public float         target_weight;
    public String        token;
    public String        updated_at;
    public String        user_key;
    public String        user_name;
    public int           user_type;
    public String        week_report_user_status;
    public String        week_report_user_type;

    public static class Builder {
        private User user = new User();

        public Builder setToken(String token) {
            this.user.token = token;
            return this;
        }

        public Builder setUserKey(String user_key) {
            this.user.user_key = user_key;
            return this;
        }

        public Builder setUserName(String user_name) {
            this.user.user_name = user_name;
            return this;
        }

        public Builder setEmail(String email) {
            this.user.email = email;
            return this;
        }

        public Builder setCellPhone(String cellphone) {
            this.user.cellphone = cellphone;
            return this;
        }

        public Builder setCellPhoneState(boolean cellphone_state) {
            this.user.cellphone_state = cellphone_state;
            return this;
        }

        public Builder setNickName(String nick_name) {
            this.user.nick_name = nick_name;
            return this;
        }

        public Builder setSexType(String sex_type) {
            this.user.sex_type = sex_type;
            return this;
        }

        public Builder setBirthDay(String birthday) {
            this.user.birthday = birthday;
            return this;
        }

        public Builder setHeight(float height) {
            this.user.height = height;
            return this;
        }

        public Builder setBeginWeight(float begin_weight) {
            this.user.begin_weight = begin_weight;
            return this;
        }

        public Builder setTargetWeight(float target_weight) {
            this.user.target_weight = target_weight;
            return this;
        }

        public Builder setLatestWeight(float latest_weight) {
            this.user.latest_weight = latest_weight;
            return this;
        }

        public Builder setTargetDate(String target_date) {
            this.user.target_date = target_date;
            return this;
        }

        public Builder setTargetCalory(int target_calory) {
            this.user.target_calory = target_calory;
            return this;
        }

        public Builder setUpdateAt(String updated_at) {
            this.user.updated_at = updated_at;
            return this;
        }

        public Builder setAvatarUrl(String avatar_url) {
            this.user.avatar_url = avatar_url;
            return this;
        }

        public Builder setDescription(String description) {
            this.user.description = description;
            return this;
        }

        public Builder setUserType(int user_type) {
            this.user.user_type = user_type;
            return this;
        }

        public Builder setLatestWeightAt(String latest_weight_at) {
            this.user.latest_weight_at = latest_weight_at;
            return this;
        }

        public Builder setLatestWaist(Float latest_waist) {
            this.user.latest_waist = latest_waist.floatValue();
            return this;
        }

        public Builder setLatestWaistAt(String latestWaistAt) {
            this.user.latest_waist_at = latestWaistAt;
            return this;
        }

        public Builder setPostCount(int postCount) {
            this.user.post_count = postCount;
            return this;
        }

        public Builder setEnviousCount(int enviousCount) {
            this.user.envious_count = enviousCount;
            return this;
        }

        public Builder setFollowingCount(int followingCount) {
            this.user.following_count = followingCount;
            return this;
        }

        public Builder setFollowerCount(int followerCount) {
            this.user.follower_count = followerCount;
            return this;
        }

        public Builder setBadgesCount(int badgesCount) {
            this.user.badges_count = badgesCount;
            return this;
        }

        public Builder setSportCondition(String sportCondition) {
            this.user.sport_condition = sportCondition;
            return this;
        }

        public Builder setDiseases(String diseases) {
            try {
                if (!TextUtils.isEmpty(diseases)) {
                    List<Integer> integers = new ArrayList();
                    for (String s : diseases.split(",")) {
                        if (!(TextUtils.isEmpty(s) || integers.contains(Integer.valueOf(Integer
                                .valueOf(s).intValue())))) {
                            integers.add(Integer.valueOf(s));
                        }
                    }
                    this.user.diseases = integers;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setDiseases(List<Integer> diseases) {
            this.user.diseases = diseases;
            return this;
        }

        public Builder setNeedTest(int needTest) {
            boolean z = true;
            User user = this.user;
            if (needTest != 1) {
                z = false;
            }
            user.need_test = z;
            return this;
        }

        public Builder setNeedTest(boolean needTest) {
            this.user.need_test = needTest;
            return this;
        }

        public Builder setBeginDate(String beginDate) {
            this.user.begin_date = beginDate;
            return this;
        }

        public User create() {
            return this.user;
        }
    }

    public User(String token, String user_key, String user_name, String email, String cellphone,
                String nick_name) {
        this.token = token;
        this.user_key = user_key;
        this.user_name = user_name;
        this.email = email;
        this.cellphone = cellphone;
        this.nick_name = nick_name;
    }

    public User(String user_name, String sex_type, String birthday, float height, float
            begin_weight, float target_weight, String target_date, int target_calory) {
        this.user_name = user_name;
        this.sex_type = sex_type;
        this.birthday = birthday;
        this.height = height;
        this.begin_weight = begin_weight;
        this.target_weight = target_weight;
        this.target_date = target_date;
        this.target_calory = target_calory;
    }

    public User(String user_name, String sex_type, String birthday, float height, float
            begin_weight, float target_weight, String target_date, int target_calory, String
            token, String cellphone, String user_key, String updated_at) {
        this(user_name, sex_type, birthday, height, begin_weight, target_weight, target_date,
                target_calory);
        this.token = token;
        this.cellphone = cellphone;
        this.user_key = user_key;
        this.updated_at = updated_at;
    }

    public String sexName() {
        if ("1".equals(this.sex_type)) {
            return "男";
        }
        return "女";
    }

    public boolean isMale() {
        return !"2".equals(this.sex_type);
    }

    public String userName() {
        if (this.user_name == null || this.user_name.equals("null")) {
            return "general";
        }
        return this.user_name;
    }

    public String sexType() {
        if (this.sex_type == null || "null".equals(this.sex_type)) {
            return "2";
        }
        return this.sex_type;
    }

    public String birthday() {
        if (TextUtils.isEmpty(this.birthday) || "null".equals(this.birthday)) {
            return "1996-01-01";
        }
        return this.birthday;
    }

    public float height() {
        return this.height == 0.0f ? 160.0f : this.height;
    }

    public float beginWeight() {
        return this.begin_weight == 0.0f ? 60.0f : this.begin_weight;
    }

    public float targetWeight() {
        return this.target_weight == 0.0f ? IntFloatWheelView.DEFAULT_VALUE : this.target_weight;
    }

    public String targetDate() {
        if (TextUtils.isEmpty(this.target_date) || "null".equals(this.target_date)) {
            return DateHelper.format(DateHelper.defaultTargetDate());
        }
        return this.target_date;
    }

    public String beginDate() {
        if (TextUtils.isEmpty(this.begin_date) || "null".equals(this.begin_date)) {
            return DateHelper.today();
        }
        return this.begin_date;
    }

    public void setSexType(String sexType) {
        if ("1".equals(sexType)) {
            this.sex_type = "1";
        } else {
            this.sex_type = "2";
        }
    }

    public void setBirthday(String birthday) {
        if (birthday.equals("null")) {
            this.birthday = "1985-01-01";
        } else {
            this.birthday = birthday;
        }
    }

    public void setHeight(String height) {
        if (height.equals("null")) {
            this.height = 0.0f;
        } else {
            this.height = Float.parseFloat(height);
        }
    }

    public void setBeginWeight(String begin_weight) {
        if (begin_weight.equals("null")) {
            this.begin_weight = 0.0f;
        } else {
            this.begin_weight = Float.parseFloat(begin_weight);
        }
    }

    public void setTargetWeight(String target_weight) {
        if (target_weight.equals("null")) {
            this.target_weight = 0.0f;
        } else {
            this.target_weight = Float.parseFloat(target_weight);
        }
    }

    public void setTargetDate(String target_date) {
        if (target_date.equals("null")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(2, cal.get(2) + 1);
            this.target_date = DateHelper.format(cal.getTime());
            return;
        }
        this.target_date = target_date;
    }

    public int getCheckedItem() {
        return Integer.parseInt(this.sex_type) - 1;
    }

    public float getWeight() {
        return this.begin_weight == 0.0f ? 55.0f : this.begin_weight;
    }

    public float calcBmi(float latest_weight) {
        if (this.height == 0.0f) {
            return 0.0f;
        }
        float h = this.height / 100.0f;
        return (float) (((double) Math.round(10.0f * ((latest_weight > 0.0f ? latest_weight :
                this.begin_weight) / (h * h)))) / 10.0d);
    }

    public float[] calcHealthyWeight() {
        float h = this.height / 100.0f;
        float min_h = ((float) Math.round(((float) (18.5d * ((double) (h * h)))) * 10.0f)) / 10.0f;
        float max_h = ((float) Math.round((24.0f * (h * h)) * 10.0f)) / 10.0f;
        return new float[]{min_h, max_h};
    }

    public int calcBee(float latest_weight) {
        float bee;
        float weight = latest_weight > 0.0f ? latest_weight : this.begin_weight;
        if ("1".equals(this.sex_type)) {
            bee = (float) ((((((double) weight) * 9.99d) + (((double) this.height) * 6.25d)) - ((
                    (double) getAge()) * 4.92d)) + 5.0d);
        } else {
            bee = (float) ((((((double) weight) * 9.99d) + (((double) this.height) * 6.25d)) - ((
                    (double) getAge()) * 4.92d)) - 161.0d);
        }
        bee = (float) (((double) bee) * 0.95d);
        if (bee < 0.0f) {
            bee = 0.0f;
        }
        return Math.round(bee);
    }

    public int[] calcHeartRate() {
        float age = (float) getAge();
        float min = (float) (((double) (220.0f - age)) * 0.6d);
        float max = (float) (((double) (220.0f - age)) * b. do);
        return new int[]{Math.round(min), Math.round(max)};
    }

    public float calcIdealLoseSpeed(Date beigin_date) {
        float speed = (7700.0f * (this.begin_weight - this.target_weight)) / ((float) DateHelper
                .between(DateHelper.parseString(this.target_date), beigin_date));
        if (speed < 0.0f) {
            return 550.0f;
        }
        return speed;
    }

    public int calcTargetCalory(Date beigin_date, float latest_weight) {
        try {
            int bee = calcBee(latest_weight);
            float targetCalory = (float) ((1.3d * ((double) bee)) - ((double) calcIdealLoseSpeed
                    (beigin_date)));
            if (targetCalory < ((float) bee)) {
                return Math.round((0.9f * ((float) bee)) + 200.0f);
            }
            return Math.round(targetCalory);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getAge() {
        int age = 25;
        try {
            age = DateHelper.getAge(DateHelper.parseString(this.birthday));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    public boolean hasProfile() {
        return this.target_calory > 1 && !this.need_test;
    }

    public float calcBodyFat(float latest_weight) {
        int sex = 0;
        if ("1".equals(this.sex_type)) {
            sex = 1;
        }
        float value = (((1.2f * calcBmi(latest_weight)) + (0.23f * ((float) getAge()))) - (10.8f
                * ((float) sex))) - 5.4f;
        if (value < 5.0f) {
            value = 5.0f;
        } else if (value > 60.0f) {
            value = 60.0f;
        }
        if ("1".equals(this.sex_type)) {
            return value;
        }
        return (float) (((double) value) - 1.5d);
    }

    public int calcBodyAge(float latest_weight) {
        int actualAge = getAge();
        if (actualAge < 18) {
            return actualAge;
        }
        float a;
        float b;
        float c;
        float d = 0.0f;
        if (actualAge < 40) {
            a = "1".equals(this.sex_type) ? 14.0f : 17.0f;
            b = "1".equals(this.sex_type) ? 0.238f : 0.429f;
            c = 18.0f;
        } else {
            a = "1".equals(this.sex_type) ? 20.0f : 25.0f;
            b = "1".equals(this.sex_type) ? 0.137f : 0.172f;
            c = 40.0f;
        }
        int subFat = (int) (calcBodyFat(latest_weight) - (a + ((((float) actualAge) - c) * b)));
        if (subFat < 0) {
            d = 0.5f;
        }
        if ("1".equals(this.sex_type)) {
            if (subFat > 2) {
                d = 1.5f;
            } else if (subFat > 0 && subFat <= 2) {
                d = 0.0f;
            }
        } else if (subFat > 3) {
            d = 1.0f;
        } else if (subFat > 0 && subFat <= 3) {
            d = 0.0f;
        }
        int bodyAge = (int) Math.floor((double) (((float) actualAge) + (((float) subFat) * d)));
        if (bodyAge < 18) {
            bodyAge = 18;
        } else if (bodyAge > 60) {
            bodyAge = 60;
        }
        return bodyAge;
    }

    public static User parsePassportUser(JSONObject object) {
        try {
            String token = object.getString("token");
            User user = (User) new Gson().fromJson(object.getJSONObject(Const.USER).toString(),
                    User.class);
            user.token = token;
            Helper.showLog(TAG, "user_type:" + user.user_type);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User parseUser(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (User) new Gson().fromJson(object.toString(), User.class);
    }
}
