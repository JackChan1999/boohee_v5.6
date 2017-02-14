package com.boohee.model;

import com.boohee.utils.DateHelper;
import com.google.gson.Gson;
import com.umeng.socialize.common.SocializeConstants;

import java.util.Date;

import org.json.JSONObject;

public class RecipeOrder extends Order {
    public Combo    combo;
    public Contract contract;

    public class Combo {
        public int    month;
        public String sku;
        public String title;
    }

    public class Contract {
        public String end_at;
        public String start_at;

        public String formatStartAt() {
            return DateHelper.monthDay(DateHelper.parseString(this.start_at));
        }

        public String formatEndAt() {
            return DateHelper.monthDay(DateHelper.parseString(this.end_at));
        }
    }

    public RecipeOrder(String created_at, String order_no) {
        super(created_at, order_no);
    }

    public String period() {
        if (this.contract.start_at == null) {
            return "";
        }
        return SocializeConstants.OP_OPEN_PAREN + this.contract.formatStartAt() + "~" + this
                .contract.formatEndAt() + SocializeConstants.OP_CLOSE_PAREN;
    }

    public String formatCreatedAt() {
        String time = this.created_at.replace("T", " ");
        try {
            return DateHelper.format(DateHelper.parseFromString(time, "yyyy-MM-dd HH:mm:ss"),
                    "yyyy年MM月dd日 HH:mm");
        } catch (Exception e) {
            return time;
        }
    }

    public String stateName() {
        if (this.state.equals(UchoiceOrder.PAYED)) {
            return "正在服务期";
        }
        if (this.state.equals("initial")) {
            return "待付款";
        }
        if (this.state.equals(UchoiceOrder.EXPIRED)) {
            return "服务已过期";
        }
        if (this.state.equals(UchoiceOrder.SENT)) {
            return "发送成功";
        }
        if (this.state.equals("canceled")) {
            return "取消";
        }
        return null;
    }

    public static boolean isExpired(String end_at) {
        Date endAt = DateHelper.addDays(DateHelper.parseString(end_at), 1);
        if (endAt == null || !new Date().after(endAt)) {
            return false;
        }
        return true;
    }

    public boolean isNeedPay() {
        return this.state.equals("initial");
    }

    public static RecipeOrder parseSelf(JSONObject object) {
        return (RecipeOrder) new Gson().fromJson(object.toString(), RecipeOrder.class);
    }
}
