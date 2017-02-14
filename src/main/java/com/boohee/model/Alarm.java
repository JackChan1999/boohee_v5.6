package com.boohee.model;

public class Alarm extends ModelBase {
    public static final String BREAKFAST   = "breakfast";
    public static final String DINNER      = "dinner";
    public static final String DRINK1      = "drink1";
    public static final String DRINK2      = "drink2";
    public static final String DRINK3      = "drink3";
    public static final String DRINK4      = "drink4";
    public static final String DRINK5      = "drink5";
    public static final String DRINK6      = "drink6";
    public static final String DRINK7      = "drink7";
    public static final String DRINK8      = "drink8";
    public static final String GREEDY      = "greedy";
    public static final String LAUNCH      = "lunch";
    public static final String MORNING     = "morning";
    public static final String SNACK1      = "snack1";
    public static final String SNACK2      = "snack2";
    public static final String SNACK3      = "snack3";
    public static final String SPORT       = "sport";
    public static final int    TYPE_SNACK1 = 1;
    public static final int    TYPE_SNACK2 = 2;
    public static final int    TYPE_SNACK3 = 3;
    public String code;
    public String daysofweek;
    public int    enabled;
    public int    hour;
    public int    minute;
    public int    type;

    public enum AlarmType {
        MORNING(Alarm.MORNING, 1),
        DIET("diet", 2),
        SPORT("sport", 3),
        DRINK("drink", 4);

        private String code;
        private int    type;

        private AlarmType(String code, int type) {
            this.code = code;
            this.type = type;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public Alarm(int id, String code, int hour, int minute, int enabled, int type) {
        this.id = id;
        this.code = code;
        this.hour = hour;
        this.minute = minute;
        this.enabled = enabled;
        this.type = type;
    }

    public String formatTime() {
        return format(this.hour) + ":" + format(this.minute);
    }

    private String format(int x) {
        return String.format("%02d", new Object[]{Integer.valueOf(x)});
    }

    public boolean is_open() {
        switch (this.enabled) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return true;
        }
    }
}
